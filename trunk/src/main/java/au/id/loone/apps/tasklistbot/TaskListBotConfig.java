/*
 * Copyright (C) 2010  David G Loone
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package au.id.loone.apps.tasklistbot;

import java.net.URL;

import au.id.loone.util.config.ConfigData;
import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;

/**
 * Configuration for the Task List Bot.
 *
 * @author David G Loone
 */
@SuppressWarnings({"UnusedDeclaration"})
public final class TaskListBotConfig
        extends ConfigData
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(TaskListBotConfig.class);

    /**
     * Current value of the <b>consumerData</b> property.
     */
    private String consumerData;

    /**
     * Current value of the <b>consumerKey</b> property.
     */
    private String consumerKey;

    /**
     * Current value of the <b>robotAvatarUrl</b> property.
     */
    private URL robotAvatarUrl;

    /**
     * Current value of the <b>robotName</b> property.
     */
    private String robotName;

    /**
     * Current value of the <b>robotProfilePageUrl</b> property.
     */
    private URL robotProfilePageUrl;

    /**
     * Current value of the <b>securityToken</b> property.
     */
    private String securityToken;

    /**
     * Current value of the <b>taskTrackerGadgetUrl</b> property.
     */
    private URL taskTrackerGadgetUrl;

    /**
     * Current value of the <b>verificationToken</b> property.
     */
    private String verificationToken;

    /**
     */
    public TaskListBotConfig()
    {
        super();
    }

    /**
     */
    public static TaskListBotConfig factory()
    {
        return new TaskListBotConfig();
    }

    /**
     * Setter for the <b>consumerData</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setConsumerData(final String consumerData) {this.consumerData = consumerData;}

    /**
     * Getter method for the <b>consumerData</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getConsumerData() {return consumerData;}

    /**
     * Setter for the <b>consumerKey</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setConsumerKey(final String consumerKey) {this.consumerKey = consumerKey;}

    /**
     * Getter method for the <b>consumerKey</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getConsumerKey() {return consumerKey;}

    /**
     * Setter for the <b>robotAvatarUrl</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setRobotAvatarUrl(final URL robotAvatarUrl) {this.robotAvatarUrl = robotAvatarUrl;}

    /**
     * Getter method for the <b>robotAvatarUrl</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public URL getRobotAvatarUrl() {return robotAvatarUrl;}

    /**
     * Setter for the <b>robotName</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setRobotName(final String robotName) {this.robotName = robotName;}

    /**
     * Getter method for the <b>robotName</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getRobotName() {return robotName;}

    /**
     * Setter for the <b>robotProfilePageUrl</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setRobotProfilePageUrl(final URL robotProfilePageUrl) {this.robotProfilePageUrl = robotProfilePageUrl;}

    /**
     * Getter method for the <b>robotProfilePageUrl</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public URL getRobotProfilePageUrl() {return robotProfilePageUrl;}

    /**
     * Setter for the <b>securityToken</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setSecurityToken(final String securityToken) {this.securityToken = securityToken;}

    /**
     * Getter method for the <b>securityToken</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getSecurityToken() {return securityToken;}

    /**
     * Setter for the <b>taskTrackerGadgetUrl</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setTaskTrackerGadgetUrl(final URL taskTrackerGadgetUrl) {this.taskTrackerGadgetUrl = taskTrackerGadgetUrl;}

    /**
     * Getter method for the <b>taskTrackerGadgetUrl</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public URL getTaskTrackerGadgetUrl() {return taskTrackerGadgetUrl;}

    /**
     * Setter for the <b>verificationToken</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setVerificationToken(final String verificationToken) {this.verificationToken = verificationToken;}

    /**
     * Getter method for the <b>verificationToken</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getVerificationToken() {return verificationToken;}

}