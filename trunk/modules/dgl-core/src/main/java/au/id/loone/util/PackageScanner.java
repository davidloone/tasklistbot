package au.id.loone.util;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import au.id.loone.util.exception.FatalException;
import au.id.loone.util.reflist.RefItem;
import au.id.loone.util.tracing.TraceUtil;
import com.thoughtworks.xstream.converters.Converter;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.log4j.Logger;

/**
 * Class for scanning package annotations.
 *
 * @author David G Loone
 */
public final class PackageScanner
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(PackageScanner.class);

    /**
     * Current value of the <b>hibernateAnnotatedClasses</b> property.
     */
    private List<Class> hibernateAnnotatedClasses;

    /**
     * Current value of the <b>hibernateAnnotatedPackages</b> property.
     */
    private List<Package> hibernateAnnotatedPackages;

    /**
     * Current value of the <b>refItemClasses</b> property.
     */
    private List<Class<? extends RefItem>> refItemClasses;

    /**
     * Current value of the <b>rootPackageNames</b> property.
     */
    private String[] rootPackageNames;

    /**
     * Current value of the <b>xstreamClasses</b> property.
     */
    private List<Class> xstreamClasses;

    /**
     * Current value of the <b>xstreamConverterClasses</b> property.
     */
    private List<Class<? extends Converter>> xstreamConverterClasses;

    /**
     */
    public PackageScanner(
            final String[] rootPackageNames
    )
    {
        super();

        this.rootPackageNames = rootPackageNames;

        processPackages();
    }

    /**
     * Process package names specified in the <b>packageNames</b> property.
     */
    private void processPackages()
    {
        LOG.trace("processPackages()");

        hibernateAnnotatedClasses = new LinkedList<Class>();
        hibernateAnnotatedPackages = new LinkedList<Package>();
        xstreamClasses = new LinkedList<Class>();
        xstreamConverterClasses = new LinkedList<Class<? extends Converter>>();
        refItemClasses = new LinkedList<Class<? extends RefItem>>();
        if (rootPackageNames != null) {
            for (final String packageName : rootPackageNames) {
                processPackage(packageName);
            }
        }
        hibernateAnnotatedClasses = Collections.unmodifiableList(hibernateAnnotatedClasses);
        hibernateAnnotatedPackages = Collections.unmodifiableList(hibernateAnnotatedPackages);
        refItemClasses = Collections.unmodifiableList(refItemClasses);
        xstreamClasses = Collections.unmodifiableList(xstreamClasses);
        xstreamConverterClasses = Collections.unmodifiableList(xstreamConverterClasses);

        LOG.trace("~processPackages");
    }

    /**
     */
    private void processPackage(
            final String packageName
    )
    {
        LOG.info("processPackage: scanning package: " + packageName);

        // Have a go at loading the package by accessing the package-info pseudo-class. This will force
        // the package to be loaded, even if we haven't loaded any of the xstreamClasses in the package so far
        // (so long as the package does actually have a package-info class, though it should because that's
        // where the annotations would go anyway).
        try {
            Class.forName(packageName + ".package-info");
        }
        catch (final ClassNotFoundException e) {
            // Do nothing.
        }

        // Access the package annotation.
        final Package pkg = Package.getPackage(packageName);
        if (pkg == null) {
            LOG.warn("processPackage: package not found: " + packageName);
        }
        else {
            final PackageData packageAnnotation = pkg.getAnnotation(PackageData.class);
            if (packageAnnotation != null) {
                for (final Class hibernateAnnotatedClass : packageAnnotation.hibernateAnnotatedClasses()) {
                    LOG.debug("processPackage: adding hibernate annotated class: " + hibernateAnnotatedClass.getName());
                    hibernateAnnotatedClasses.add(hibernateAnnotatedClass);
                }
                for (final Class<? extends RefItem> refItemClass : packageAnnotation.refItemClasses()) {
                    LOG.debug("processPackage: adding ref item class: " + refItemClass.getName());
                    refItemClasses.add(refItemClass);
                }
                for (final Class xstreamClass : packageAnnotation.xstreamClasses()) {
                    LOG.debug("processPackage: adding XStream class: " + xstreamClass.getName());
                    xstreamClasses.add(xstreamClass);
                }
//                for (final Class<? extends Converter> xstreamConverterClass : packageAnnotation.xstreamConverterClasses()) {
//                    LOG.debug("processPackage: adding XStream converter class: " + xstreamConverterClass.getName());
//                    xstreamConverterClasses.add(xstreamConverterClass);
//                }
                for (final PackageData.BeanUtilConverterSpec beanUtilConverterSpec : packageAnnotation.beanUtilConverters()) {
                    LOG.debug("processPackage: adding beanutils converter for class: " + beanUtilConverterSpec.targetClass().getName());
                    try {
                        ConvertUtils.register(beanUtilConverterSpec.converterClass().newInstance(), beanUtilConverterSpec.targetClass());
                    }
                    catch (final InstantiationException e) {
                        final FatalException fe = FatalException.factory(e);
                        fe.addProperty(beanUtilConverterSpec.converterClass(), "converterClass");
                        fe.addProperty(beanUtilConverterSpec.targetClass(), "targetClass");
                        fe.addProperty(pkg, "package");
                        throw fe;
                    }
                    catch (final IllegalAccessException e) {
                        final FatalException fe = FatalException.factory(e);
                        fe.addProperty(beanUtilConverterSpec.converterClass(), "converterClass");
                        fe.addProperty(beanUtilConverterSpec.targetClass(), "targetClass");
                        fe.addProperty(pkg, "package");
                        throw fe;
                    }
                }
                for (final String subPackageName : packageAnnotation.packageNames()) {
                    processPackage(subPackageName);
                }
            }

            // Look for hibernate annotations.
            for (final Annotation annotation : pkg.getAnnotations()) {
                if (annotation.annotationType().getName().startsWith("org.hibernate.annotations")) {
                    LOG.debug("processPackage: adding hibernate annotated package: " + pkg.getName());
                    hibernateAnnotatedPackages.add(pkg);
                    break;
                }
            }
        }
    }

    /**
     * Getter method for the <b>hibernateAnnotatedClasses</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public List<Class> getHibernateAnnotatedClasses() {return hibernateAnnotatedClasses;}

    /**
     * Getter method for the <b>hibernateAnnotatedPackages</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public List<Package> getHibernateAnnotatedPackages() {return hibernateAnnotatedPackages;}

    /**
     * Getter method for the <b>hibernateAnnotatedPackageNames</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String[] getHibernateAnnotatedPackageNames()
    {
        final String[] result;

        if (getHibernateAnnotatedPackages() == null) {
            result = null;
        }
        else {
            final List<Package> packages = getHibernateAnnotatedPackages();
            result = new String[packages.size()];
            int i = 0;
            for (final Package pkg : packages) {
                result[i++] = pkg.getName();
            }
        }

        return result;
    }

    /**
     * Getter method for the <b>refItemClasses</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public List<Class<? extends RefItem>> getRefItemClasses() {return refItemClasses;}

    /**
     * Getter method for the <b>rootPackageNames</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String[] getRootPackageNames() {return rootPackageNames;}

    /**
     * Getter method for the <b>xstreamClasses</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public List<Class> getXstreamClasses() {return xstreamClasses;}

    /**
     * Getter method for the <b>xstreamConverterClasses</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public List<Class<? extends Converter>> getXstreamConverterClasses() {return xstreamConverterClasses;}

}