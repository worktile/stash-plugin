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

public class LesschatGlobalSettingsServlet extends HttpServlet {
    static final String KEY_GLOBAL_SETTING_HOOK_URL = "stash2lesschat.globalsettings.hookurl";
    static final String KEY_GLOBAL_SETTING_CHANNEL_NAME = "stash2lesschat.globalsettings.channelname";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsopenedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsreopenedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsupdatedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsapprovedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsunapprovedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsdeclinedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsmergedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationscommentedenabled";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL = "stash2lesschat.globalsettings.lesschatnotificationslevel";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL = "stash2lesschat.globalsettings.lesschatnotificationsprlevel";
    static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationspushenabled";

    private final PageBuilderService pageBuilderService;
    private final LesschatGlobalSettingsService lesschatGlobalSettingsService;
    private final SoyTemplateRenderer soyTemplateRenderer;
    private final PermissionValidationService validationService;
    private final I18nService i18nService;

    public LesschatGlobalSettingsServlet(PageBuilderService pageBuilderService,
                                      LesschatGlobalSettingsService lesschatGlobalSettingsService,
                                      SoyTemplateRenderer soyTemplateRenderer,
                                      PermissionValidationService validationService,
                                      I18nService i18nService) {
        this.pageBuilderService = pageBuilderService;
        this.lesschatGlobalSettingsService = lesschatGlobalSettingsService;
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

        String globalWebHookUrl = req.getParameter("lesschatGlobalWebHookUrl");
        if (null != globalWebHookUrl) {
            lesschatGlobalSettingsService.setWebHookUrl(KEY_GLOBAL_SETTING_HOOK_URL, globalWebHookUrl);
        }

        String lesschatChannelName = req.getParameter("lesschatChannelName");
        if (null != lesschatChannelName) {
            lesschatGlobalSettingsService.setChannelName(KEY_GLOBAL_SETTING_CHANNEL_NAME, lesschatChannelName);
        }

        Boolean lesschatNotificationsEnabled = false;
        if (null != req.getParameter("lesschatNotificationsEnabled") && req.getParameter("lesschatNotificationsEnabled").equals("on")) {
            lesschatNotificationsEnabled = true;
        }
        lesschatGlobalSettingsService.setlesschatNotificationsEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED, lesschatNotificationsEnabled.toString());

        Boolean lesschatNotificationsOpenedEnabled = false;
        if (null != req.getParameter("lesschatNotificationsOpenedEnabled") && req.getParameter("lesschatNotificationsOpenedEnabled").equals("on")) {
            lesschatNotificationsOpenedEnabled = true;
        }
        lesschatGlobalSettingsService.setlesschatNotificationsOpenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED, lesschatNotificationsOpenedEnabled.toString());

        Boolean lesschatNotificationsReopenedEnabled = false;
        if (null != req.getParameter("lesschatNotificationsReopenedEnabled") && req.getParameter("lesschatNotificationsReopenedEnabled").equals("on")) {
            lesschatNotificationsReopenedEnabled = true;
        }
        lesschatGlobalSettingsService.setlesschatNotificationsReopenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED, lesschatNotificationsReopenedEnabled.toString());

        Boolean lesschatNotificationsUpdatedEnabled = false;
        if (null != req.getParameter("lesschatNotificationsUpdatedEnabled") && req.getParameter("lesschatNotificationsUpdatedEnabled").equals("on")) {
            lesschatNotificationsUpdatedEnabled = true;
        }
        lesschatGlobalSettingsService.setlesschatNotificationsUpdatedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED, lesschatNotificationsUpdatedEnabled.toString());

        Boolean lesschatNotificationsApprovedEnabled = false;
        if (null != req.getParameter("lesschatNotificationsApprovedEnabled") && req.getParameter("lesschatNotificationsApprovedEnabled").equals("on")) {
            lesschatNotificationsApprovedEnabled = true;
        }
        lesschatGlobalSettingsService.setlesschatNotificationsApprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED, lesschatNotificationsApprovedEnabled.toString());

        Boolean lesschatNotificationsUnapprovedEnabled = false;
        if (null != req.getParameter("lesschatNotificationsUnapprovedEnabled") && req.getParameter("lesschatNotificationsUnapprovedEnabled").equals("on")) {
            lesschatNotificationsUnapprovedEnabled = true;
        }
        lesschatGlobalSettingsService.setlesschatNotificationsUnapprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED, lesschatNotificationsUnapprovedEnabled.toString());

        Boolean lesschatNotificationsDeclinedEnabled = false;
        if (null != req.getParameter("lesschatNotificationsDeclinedEnabled") && req.getParameter("lesschatNotificationsDeclinedEnabled").equals("on")) {
            lesschatNotificationsDeclinedEnabled = true;
        }
        lesschatGlobalSettingsService.setlesschatNotificationsDeclinedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED, lesschatNotificationsDeclinedEnabled.toString());

        Boolean lesschatNotificationsMergedEnabled = false;
        if (null != req.getParameter("lesschatNotificationsMergedEnabled") && req.getParameter("lesschatNotificationsMergedEnabled").equals("on")) {
            lesschatNotificationsMergedEnabled = true;
        }
        lesschatGlobalSettingsService.setlesschatNotificationsMergedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED, lesschatNotificationsMergedEnabled.toString());

        Boolean lesschatNotificationsCommentedEnabled = false;
        if (null != req.getParameter("lesschatNotificationsCommentedEnabled") && req.getParameter("lesschatNotificationsCommentedEnabled").equals("on")) {
            lesschatNotificationsCommentedEnabled = true;
        }
        lesschatGlobalSettingsService.setlesschatNotificationsCommentedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED, lesschatNotificationsCommentedEnabled.toString());

        NotificationLevel notificationLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("lesschatNotificationLevel")) {
            notificationLevel = NotificationLevel.valueOf(req.getParameter("lesschatNotificationLevel"));
        }
        lesschatGlobalSettingsService.setNotificationLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL, notificationLevel.toString());

        NotificationLevel notificationPrLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("lesschatNotificationPrLevel")) {
            notificationPrLevel = NotificationLevel.valueOf(req.getParameter("lesschatNotificationPrLevel"));
        }
        lesschatGlobalSettingsService.setNotificationPrLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL, notificationPrLevel.toString());

        Boolean lesschatNotificationsEnabledForPush = false;
        if (null != req.getParameter("lesschatNotificationsEnabledForPush") && req.getParameter("lesschatNotificationsEnabledForPush").equals("on")) {
            lesschatNotificationsEnabledForPush = true;
        }
        lesschatGlobalSettingsService.setlesschatNotificationsEnabledForPush(KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED, lesschatNotificationsEnabledForPush.toString());


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

        String webHookUrl = lesschatGlobalSettingsService.getWebHookUrl(KEY_GLOBAL_SETTING_HOOK_URL);
        if (null == webHookUrl || webHookUrl.equals("")) {
            webHookUrl = "";
        }

        String channelName = lesschatGlobalSettingsService.getChannelName(KEY_GLOBAL_SETTING_CHANNEL_NAME);
        if (null == channelName || channelName.equals("")) {
            channelName = "";
        }

        Boolean lesschatNotificationsEnabled = lesschatGlobalSettingsService.getlesschatNotificationsEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED);
        Boolean lesschatNotificationsOpenedEnabled = lesschatGlobalSettingsService.getlesschatNotificationsOpenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED);
        Boolean lesschatNotificationsReopenedEnabled = lesschatGlobalSettingsService.getlesschatNotificationsReopenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED);
        Boolean lesschatNotificationsUpdatedEnabled = lesschatGlobalSettingsService.getlesschatNotificationsUpdatedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED);
        Boolean lesschatNotificationsApprovedEnabled = lesschatGlobalSettingsService.getlesschatNotificationsApprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED);
        Boolean lesschatNotificationsUnapprovedEnabled = lesschatGlobalSettingsService.getlesschatNotificationsUnapprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED);
        Boolean lesschatNotificationsDeclinedEnabled = lesschatGlobalSettingsService.getlesschatNotificationsDeclinedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED);
        Boolean lesschatNotificationsMergedEnabled = lesschatGlobalSettingsService.getlesschatNotificationsMergedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED);
        Boolean lesschatNotificationsCommentedEnabled = lesschatGlobalSettingsService.getlesschatNotificationsCommentedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED);
        Boolean lesschatNotificationsEnabledForPush = lesschatGlobalSettingsService.getlesschatNotificationsEnabledForPush(KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED);
        String notificationLevel = lesschatGlobalSettingsService.getNotificationLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL).toString();
        String notificationPrLevel = lesschatGlobalSettingsService.getNotificationPrLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL).toString();

        render(response,
                "stash.page.lesschat.global.settings.viewGloballesschatSettings",
                ImmutableMap.<String, Object>builder()
                        .put("lesschatGlobalWebHookUrl", webHookUrl)
                        .put("lesschatChannelName", channelName)
                        .put("lesschatNotificationsEnabled", lesschatNotificationsEnabled)
                        .put("lesschatNotificationsOpenedEnabled", lesschatNotificationsOpenedEnabled)
                        .put("lesschatNotificationsReopenedEnabled", lesschatNotificationsReopenedEnabled)
                        .put("lesschatNotificationsUpdatedEnabled", lesschatNotificationsUpdatedEnabled)
                        .put("lesschatNotificationsApprovedEnabled", lesschatNotificationsApprovedEnabled)
                        .put("lesschatNotificationsUnapprovedEnabled", lesschatNotificationsUnapprovedEnabled)
                        .put("lesschatNotificationsDeclinedEnabled", lesschatNotificationsDeclinedEnabled)
                        .put("lesschatNotificationsMergedEnabled", lesschatNotificationsMergedEnabled)
                        .put("lesschatNotificationsCommentedEnabled", lesschatNotificationsCommentedEnabled)
                        .put("lesschatNotificationsEnabledForPush", lesschatNotificationsEnabledForPush)
                        .put("notificationLevel", notificationLevel)
                        .put("notificationPrLevel", notificationPrLevel)
                        .put("notificationLevels", new SelectFieldOptions(NotificationLevel.values()).toSoyStructure())
                        .build()
        );
    }

    private void render(HttpServletResponse response, String templateName, Map<String, Object> data)
            throws IOException, ServletException {
        pageBuilderService.assembler().resources().requireContext("plugin.adminpage.lesschat");
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
