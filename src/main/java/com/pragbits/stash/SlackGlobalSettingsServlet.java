package com.pragbits.stash;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.stash.exception.AuthorisationException;
import com.atlassian.stash.nav.NavBuilder;
import com.pragbits.stash.PluginMetadata;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.repository.RepositoryService;
import com.atlassian.stash.avatar.AvatarService;
import com.atlassian.stash.user.Permission;
import com.atlassian.stash.user.PermissionValidationService;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import com.atlassian.stash.i18n.I18nService;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.pragbits.stash.soy.SelectFieldOptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class SlackGlobalSettingsServlet extends HttpServlet {
    static final String KEY_GLOBAL_SETTING_HOOK_URL = "stash2slack.globalsettings.hookurl";
    static final String KEY_GLOBAL_SETTING_CHANNEL_NAME = "stash2slack.globalsettings.channelname";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED = "stash2slack.globalsettings.slacknotificationsenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED = "stash2slack.globalsettings.slacknotificationsopenedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED = "stash2slack.globalsettings.slacknotificationsreopenedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED = "stash2slack.globalsettings.slacknotificationsupdatedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED = "stash2slack.globalsettings.slacknotificationsapprovedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED = "stash2slack.globalsettings.slacknotificationsunapprovedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED = "stash2slack.globalsettings.slacknotificationsdeclinedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED = "stash2slack.globalsettings.slacknotificationsmergedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED = "stash2slack.globalsettings.slacknotificationscommentedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL = "stash2slack.globalsettings.slacknotificationslevel";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL = "stash2slack.globalsettings.slacknotificationsprlevel";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED = "stash2slack.globalsettings.slacknotificationspushenabled";

    private final PageBuilderService pageBuilderService;
    private final SlackGlobalSettingsService slackGlobalSettingsService;
    private final SoyTemplateRenderer soyTemplateRenderer;
    private final PermissionValidationService validationService;
    private final I18nService i18nService;

    public SlackGlobalSettingsServlet(PageBuilderService pageBuilderService,
                                      SlackGlobalSettingsService slackGlobalSettingsService,
                                      SoyTemplateRenderer soyTemplateRenderer,
                                      PermissionValidationService validationService,
                                      I18nService i18nService) {
        this.pageBuilderService = pageBuilderService;
        this.slackGlobalSettingsService = slackGlobalSettingsService;
        this.soyTemplateRenderer = soyTemplateRenderer;
        this.validationService = validationService;
        this.i18nService = i18nService;

    }

    @Override
    protected  void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        try {
            validationService.validateForGlobal(Permission.SYS_ADMIN);
        } catch (AuthorisationException e) {
            // Skip form processing
            doGet(req, res);
            return;
        }

        String globalWebHookUrl = req.getParameter("slackGlobalWebHookUrl");
        if (null != globalWebHookUrl) {
            slackGlobalSettingsService.setWebHookUrl(KEY_GLOBAL_SETTING_HOOK_URL, globalWebHookUrl);
        }

        String slackChannelName = req.getParameter("slackChannelName");
        if (null != slackChannelName) {
            slackGlobalSettingsService.setChannelName(KEY_GLOBAL_SETTING_CHANNEL_NAME, slackChannelName);
        }

        Boolean slackNotificationsEnabled = false;
        if (null != req.getParameter("slackNotificationsEnabled") && req.getParameter("slackNotificationsEnabled").equals("on")) {
            slackNotificationsEnabled = true;
        }
        slackGlobalSettingsService.setSlackNotificationsEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED, slackNotificationsEnabled.toString());

        Boolean slackNotificationsOpenedEnabled = false;
        if (null != req.getParameter("slackNotificationsOpenedEnabled") && req.getParameter("slackNotificationsOpenedEnabled").equals("on")) {
            slackNotificationsOpenedEnabled = true;
        }
        slackGlobalSettingsService.setSlackNotificationsOpenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED, slackNotificationsOpenedEnabled.toString());

        Boolean slackNotificationsReopenedEnabled = false;
        if (null != req.getParameter("slackNotificationsReopenedEnabled") && req.getParameter("slackNotificationsReopenedEnabled").equals("on")) {
            slackNotificationsReopenedEnabled = true;
        }
        slackGlobalSettingsService.setSlackNotificationsReopenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED, slackNotificationsReopenedEnabled.toString());

        Boolean slackNotificationsUpdatedEnabled = false;
        if (null != req.getParameter("slackNotificationsUpdatedEnabled") && req.getParameter("slackNotificationsUpdatedEnabled").equals("on")) {
            slackNotificationsUpdatedEnabled = true;
        }
        slackGlobalSettingsService.setSlackNotificationsUpdatedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED, slackNotificationsUpdatedEnabled.toString());

        Boolean slackNotificationsApprovedEnabled = false;
        if (null != req.getParameter("slackNotificationsApprovedEnabled") && req.getParameter("slackNotificationsApprovedEnabled").equals("on")) {
            slackNotificationsApprovedEnabled = true;
        }
        slackGlobalSettingsService.setSlackNotificationsApprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED, slackNotificationsApprovedEnabled.toString());

        Boolean slackNotificationsUnapprovedEnabled = false;
        if (null != req.getParameter("slackNotificationsUnapprovedEnabled") && req.getParameter("slackNotificationsUnapprovedEnabled").equals("on")) {
            slackNotificationsUnapprovedEnabled = true;
        }
        slackGlobalSettingsService.setSlackNotificationsUnapprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED, slackNotificationsUnapprovedEnabled.toString());

        Boolean slackNotificationsDeclinedEnabled = false;
        if (null != req.getParameter("slackNotificationsDeclinedEnabled") && req.getParameter("slackNotificationsDeclinedEnabled").equals("on")) {
            slackNotificationsDeclinedEnabled = true;
        }
        slackGlobalSettingsService.setSlackNotificationsDeclinedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED, slackNotificationsDeclinedEnabled.toString());

        Boolean slackNotificationsMergedEnabled = false;
        if (null != req.getParameter("slackNotificationsMergedEnabled") && req.getParameter("slackNotificationsMergedEnabled").equals("on")) {
            slackNotificationsMergedEnabled = true;
        }
        slackGlobalSettingsService.setSlackNotificationsMergedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED, slackNotificationsMergedEnabled.toString());

        Boolean slackNotificationsCommentedEnabled = false;
        if (null != req.getParameter("slackNotificationsCommentedEnabled") && req.getParameter("slackNotificationsCommentedEnabled").equals("on")) {
            slackNotificationsCommentedEnabled = true;
        }
        slackGlobalSettingsService.setSlackNotificationsCommentedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED, slackNotificationsCommentedEnabled.toString());

        NotificationLevel notificationLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("slackNotificationLevel")) {
            notificationLevel = NotificationLevel.valueOf(req.getParameter("slackNotificationLevel"));
        }
        slackGlobalSettingsService.setNotificationLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL, notificationLevel.toString());

        NotificationLevel notificationPrLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("slackNotificationPrLevel")) {
            notificationPrLevel = NotificationLevel.valueOf(req.getParameter("slackNotificationPrLevel"));
        }
        slackGlobalSettingsService.setNotificationPrLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL, notificationPrLevel.toString());

        Boolean slackNotificationsEnabledForPush = false;
        if (null != req.getParameter("slackNotificationsEnabledForPush") && req.getParameter("slackNotificationsEnabledForPush").equals("on")) {
            slackNotificationsEnabledForPush = true;
        }
        slackGlobalSettingsService.setSlackNotificationsEnabledForPush(KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED, slackNotificationsEnabledForPush.toString());


        doGet(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doView(response);

    }

    private void doView(HttpServletResponse response)
            throws ServletException, IOException {

        validationService.validateForGlobal(Permission.ADMIN);

        String webHookUrl = slackGlobalSettingsService.getWebHookUrl(KEY_GLOBAL_SETTING_HOOK_URL);
        if (null == webHookUrl || webHookUrl.equals("")) {
            webHookUrl = "";
        }

        String channelName = slackGlobalSettingsService.getChannelName(KEY_GLOBAL_SETTING_CHANNEL_NAME);
        if (null == channelName || channelName.equals("")) {
            channelName = "";
        }

        Boolean slackNotificationsEnabled = slackGlobalSettingsService.getSlackNotificationsEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED);
        Boolean slackNotificationsOpenedEnabled = slackGlobalSettingsService.getSlackNotificationsOpenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED);
        Boolean slackNotificationsReopenedEnabled = slackGlobalSettingsService.getSlackNotificationsReopenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED);
        Boolean slackNotificationsUpdatedEnabled = slackGlobalSettingsService.getSlackNotificationsUpdatedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED);
        Boolean slackNotificationsApprovedEnabled = slackGlobalSettingsService.getSlackNotificationsApprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED);
        Boolean slackNotificationsUnapprovedEnabled = slackGlobalSettingsService.getSlackNotificationsUnapprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED);
        Boolean slackNotificationsDeclinedEnabled = slackGlobalSettingsService.getSlackNotificationsDeclinedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED);
        Boolean slackNotificationsMergedEnabled = slackGlobalSettingsService.getSlackNotificationsMergedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED);
        Boolean slackNotificationsCommentedEnabled = slackGlobalSettingsService.getSlackNotificationsCommentedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED);
        Boolean slackNotificationsEnabledForPush = slackGlobalSettingsService.getSlackNotificationsEnabledForPush(KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED);
        String notificationLevel = slackGlobalSettingsService.getNotificationLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL).toString();
        String notificationPrLevel = slackGlobalSettingsService.getNotificationPrLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL).toString();

        render(response,
                "stash.page.slack.global.settings.viewGlobalSlackSettings",
                ImmutableMap.<String, Object>builder()
                        .put("slackGlobalWebHookUrl", webHookUrl)
                        .put("slackChannelName", channelName)
                        .put("slackNotificationsEnabled", slackNotificationsEnabled)
                        .put("slackNotificationsOpenedEnabled", slackNotificationsOpenedEnabled)
                        .put("slackNotificationsReopenedEnabled", slackNotificationsReopenedEnabled)
                        .put("slackNotificationsUpdatedEnabled", slackNotificationsUpdatedEnabled)
                        .put("slackNotificationsApprovedEnabled", slackNotificationsApprovedEnabled)
                        .put("slackNotificationsUnapprovedEnabled", slackNotificationsUnapprovedEnabled)
                        .put("slackNotificationsDeclinedEnabled", slackNotificationsDeclinedEnabled)
                        .put("slackNotificationsMergedEnabled", slackNotificationsMergedEnabled)
                        .put("slackNotificationsCommentedEnabled", slackNotificationsCommentedEnabled)
                        .put("slackNotificationsEnabledForPush", slackNotificationsEnabledForPush)
                        .put("notificationLevel", notificationLevel)
                        .put("notificationPrLevel", notificationPrLevel)
                        .put("notificationLevels", new SelectFieldOptions(NotificationLevel.values()).toSoyStructure())
                        .build()
        );
    }

    private void render(HttpServletResponse response, String templateName, Map<String, Object> data)
            throws IOException, ServletException {
        pageBuilderService.assembler().resources().requireContext("plugin.adminpage.slack");
        response.setContentType("text/html;charset=UTF-8");
        try {
            soyTemplateRenderer.render(response.getWriter(), PluginMetadata.getCompleteModuleKey("soy-templates"), templateName, data);
        } catch (SoyException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw new ServletException(e);
        }
    }

}
