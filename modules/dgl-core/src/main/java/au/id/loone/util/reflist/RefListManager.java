package au.id.loone.util.reflist;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.beans.DGLBeanUtil;
import au.id.loone.util.exception.FatalException;
import au.id.loone.util.reflist.transobj.RefListItemIndexedPropertyTO;
import au.id.loone.util.reflist.transobj.RefListItemNonIndexedPropertyTO;
import au.id.loone.util.reflist.transobj.RefListItemPropertyTO;
import au.id.loone.util.reflist.transobj.RefListItemTO;
import au.id.loone.util.reflist.transobj.RefListTO;
import au.id.loone.util.tracing.TraceUtil;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

/**
 * For managing reflist lifecycles.
 *
 * @author David G Loone
 */
public final class RefListManager
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(RefListManager.class);

    /**
     *  The list of constructor arguments (for looking up the constructor).
     */
    private static Class[] EXTERNAL_REFITEM_CONSTRUCTOR_ARGS = new Class[] {
        String.class,
        String.class,
        Boolean.TYPE,
        Set.class
    };

    /**
     */
    public RefListManager()
    {
        super();
    }

    /**
     */
    public static void initOrReload(
            final Class<? extends RefItem> refItemClass
    )
    {
        LOG.trace("initOrReload(" + TraceUtil.formatObj(refItemClass) + ")");
        LOG.info("initOrReload: loading reflist for class: " + refItemClass.getName());

        if (StaticRefItem.class.isAssignableFrom(refItemClass)) {
            initOrReloadStaticRefItem(refItemClass.asSubclass(StaticRefItem.class));
        }
        else if (ExternalRefItem.class.isAssignableFrom(refItemClass)) {
            final Class<? extends ExternalRefItem> externalRefItemClass = refItemClass.asSubclass(ExternalRefItem.class);
            final ExternalRefListSource externalRefListSourceAnnotation =
                    refItemClass.getAnnotation(ExternalRefListSource.class);
            if (externalRefListSourceAnnotation == null) {
                throw new IllegalArgumentException("External ref item class must be annotated with @ExternalRefListSource.");
            }
            else if (externalRefListSourceAnnotation.sourceClass() != null) {
                final ExternalRefListSourceDef source;
                try {
                    source = externalRefListSourceAnnotation.sourceClass().newInstance();
                }
                catch (final InstantiationException e) {
                    throw new RuntimeException(e);
                }
                catch (final IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                final RefListTO content = source.retrieve(externalRefItemClass);
                initOrReloadExternalRefItem(refItemClass.asSubclass(ExternalRefItem.class), content);
            }
            else {
                throw new IllegalArgumentException("@ExternalRefListSource must contain a source value.");
            }
        }

        ConvertUtils.register(new BeanPropertyConverter(), refItemClass);

        LOG.trace("~initOrReload");
    }

    /**
     */
    @SuppressWarnings({"unchecked"})
    private static <T extends StaticRefItem> void initOrReloadStaticRefItem(
            final Class<T> refItemClass
    )
    {
        String name = refItemClass.getName();
        if (name.endsWith("RI")) {
            name = name.substring(0, name.length() - 2);
        }
        final RefList<T> refList = RefList.factory(name, refItemClass);

        final Field[] fields = refItemClass.getFields();
        for (final Field field : fields) {
            if ((field.getAnnotation(RefItemInstance.class) != null) &&
                    Modifier.isStatic(field.getModifiers()) &&
                    Modifier.isPublic(field.getModifiers()) &&
                    field.getType().equals(refItemClass)) {
                try {
                    refList.addItem((T)field.get(null));
                }
                catch (final IllegalAccessException e) {
                    // Do nothing.
                }
            }
        }

        refList.fixate();
        afterFixate(refList);
    }

    /**
     */
    private static <T extends ExternalRefItem> void initOrReloadExternalRefItem(
            final Class<T> refItemClass,
            final RefListTO refListContent
    )
    {
        if (LOG.isTraceEnabled()) {
            LOG.trace("refresh(" + TraceUtil.formatObj(refItemClass) + ", ...)");
        }

        try {
            @SuppressWarnings({"unchecked"})
            final Constructor<T> constructor = refItemClass.getDeclaredConstructor(EXTERNAL_REFITEM_CONSTRUCTOR_ARGS);
            constructor.setAccessible(true);
            final RefList<T> refList = RefList.factory(refListContent.getName(), refListContent.getDescription(),
                    refItemClass);
            if (LOG.isTraceEnabled()) {
                LOG.trace("refresh: " + TraceUtil.formatObj(refListContent, "refListContent.items.size"));
            }
            for (final RefListItemTO itemInfo : refListContent.getItems()) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("refresh: " + TraceUtil.formatObj(itemInfo, "itemInfo.code"));
                    LOG.trace("refresh: " + TraceUtil.formatObj(itemInfo, "itemInfo.description"));
                    LOG.trace("refresh: " + TraceUtil.formatObj(itemInfo, "itemInfo.obsolete"));
                    LOG.trace("refresh: " + TraceUtil.formatObj(itemInfo, "itemInfo.aliases"));
                }
                final Object[] args = new Object[] {
                    itemInfo.getCode(),
                    itemInfo.getDescription(),
                    itemInfo.getObsolete(),
                    itemInfo.getAliases()
                };
                final T item = constructor.newInstance(args);
                for (final RefListItemPropertyTO abstractPropertyInfo : itemInfo.getProperties()) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("refresh: " + TraceUtil.formatObj(abstractPropertyInfo, "abstractPropertyInfo.name"));
                    }
                    final String propertyName = abstractPropertyInfo.getName();
                    if (abstractPropertyInfo instanceof RefListItemNonIndexedPropertyTO) {
                        // Non-indexed property.
                        final RefListItemNonIndexedPropertyTO propertyInfo =
                                (RefListItemNonIndexedPropertyTO)abstractPropertyInfo;
                        if (LOG.isTraceEnabled()) {
                            LOG.trace("refresh: " + TraceUtil.formatObj(propertyInfo, "propertyInfo.value"));
                        }
                        DGLBeanUtil.setProperty(item, propertyName, propertyInfo.getValue());
                    }
                    else if (abstractPropertyInfo instanceof RefListItemIndexedPropertyTO) {
                        // Indexed property.
                        final RefListItemIndexedPropertyTO propertyInfo =
                                (RefListItemIndexedPropertyTO)abstractPropertyInfo;
                        if (LOG.isTraceEnabled()) {
                            LOG.trace("refresh: " + TraceUtil.formatObj(propertyInfo, "propertyInfo.values"));
                        }
                        DGLBeanUtil.setProperty(item, propertyName, propertyInfo.getValues());
                    }
                    else {
                        assert false;
                    }
                }
                refList.addItem(item);
            }

            // Set static values.
            {
                final Field[] fields = refItemClass.getFields();
                for (final Field field : fields) {
                    final RefItemInstance refItemInstanceAnn = field.getAnnotation(RefItemInstance.class);
                    if (refItemInstanceAnn != null) {
                        final String fieldCode = DGLStringUtil.equals(refItemInstanceAnn.code(), "") ?
                                field.getName() : refItemInstanceAnn.code();
                        final RefItem item = refList.factory(fieldCode);
                        if (item == null) {
                            final FatalException fe = new FatalException("REF_ITEM_NOT_FOUND_FOR_STATIC_FIELD");
                            fe.addProperty(field, "field.name");
                            fe.addProperty(fieldCode, "fieldCode");
                            fe.addProperty(refItemClass, "refItemClass.name");
                            throw fe;
                        }
                        field.set(null, item);
                    }
                }
            }

            refList.fixate();
            afterFixate(refList);

            if (LOG.isTraceEnabled()) {
                for (final RefItem refItem : BaseRefItem.getAllItems(refItemClass)) {
                    LOG.trace("refresh: " + TraceUtil.formatObj(refItem, "refItem"));
                }
            }
        }
        catch (final NoSuchMethodException e) {
            LOG.warn("refresh: " + TraceUtil.formatObj(e), e);
            assert false;
        }
        catch (final InstantiationException e) {
            LOG.warn("refresh: " + TraceUtil.formatObj(e), e);
            assert false;
        }
        catch (final IllegalAccessException e) {
            LOG.warn("refresh: " + TraceUtil.formatObj(e), e);
            assert false;
        }
        catch (final InvocationTargetException e) {
            LOG.warn("refresh: " + TraceUtil.formatObj(e), e.getTargetException());
            assert false;
        }
    }

    /**
     * Run any @AfterFixate methods.
     */
    private static void afterFixate(
            final RefList refList
    )
    {
        LOG.trace("afterFixate(" + TraceUtil.formatObj(refList) + ")");

        final Method[] methods = refList.getRefItemClass().getDeclaredMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(AfterFixate.class) &&
                    Modifier.isStatic(method.getModifiers()) &&
                    (method.getParameterTypes().length == 0)) {
                try {
                    LOG.trace("afterFixate: invoking " + TraceUtil.formatObj(method));
                    method.setAccessible(true);
                    method.invoke(null);
                }
                catch (final Exception e) {
                    LOG.warn("afterFixate: " + TraceUtil.formatObj(e), e);
                }
            }
        }

        LOG.trace("~afterFixate");
    }

    /**
     */
    @SuppressWarnings({"unchecked"})
    public static void ensureLoaded(
            final Class<? extends RefItem> refItemClass
    )
    {
        // Load any dependent reflists.
        // TODO: Check for recursion.
        for (final PropertyDescriptor propertyDescriptor : BeanUtils.getPropertyDescriptors(refItemClass)) {
            final Class propertyType = propertyDescriptor.getPropertyType();
            if (propertyType == null) {
                // Do nothing.
            }
            else if (RefItem.class.isAssignableFrom(propertyType)) {
                final Class<? extends RefItem> dependentRefItemClass = (Class<? extends RefItem>)propertyType;
                if (RefList.getRefList(dependentRefItemClass) == null) {
                    // Make sure this reflist is loaded first.
                    ensureLoaded(dependentRefItemClass);
                }
            }
            else if (propertyType.isArray() &&
                    RefItem.class.isAssignableFrom(propertyType.getComponentType())) {
                final Class<? extends RefItem> dependentRefItemClass =
                        (Class<? extends RefItem>)propertyType.getComponentType();
                if (RefList.getRefList(dependentRefItemClass) == null) {
                    // Make sure this reflist is loaded first.
                    ensureLoaded(dependentRefItemClass);
                }
            }
        }

        if (RefList.getRefList(refItemClass) == null) {
            initOrReload(refItemClass);
        }
    }

}