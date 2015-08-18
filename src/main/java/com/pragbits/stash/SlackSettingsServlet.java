package com.pragbits.stash;

import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.stash.exception.AuthorisationException;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.repository.RepositoryService;
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

public class SlackSettingsServlet extends HttpServlet {
    private final PageBuilderService pageBuilderService;
    private final SlackSettingsService slackSettingsService;
    private final RepositoryService repositoryService;
    private final SoyTemplateRenderer soyTemplateRenderer;
    private final PermissionValidationService validationService;
    private final I18nService i18nService;
    
    private Repository repository = null;

    public SlackSettingsServlet(PageBuilderService pageBuilderService,
                                    SlackSettingsService slackSettingsService,
                                    RepositoryService repositoryService,
                                    SoyTemplateRenderer soyTemplateRenderer,
                                    PermissionValidationService validationService,
                                    I18nService i18nService) {
        this.pageBuilderService = pageBuilderService;
        this.slackSettingsService = slackSettingsService;
        this.repositoryService = repositoryService;
        this.soyTemplateRenderer = soyTemplateRenderer;
        this.validationService = validationService;
        this.i18nService = i18nService;
    }

    @Override
    protected  void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        try {
            validationService.validateForRepository(this.repository, Permission.REPO_ADMIN);
        } catch (AuthorisationException e) {
            // Skip form processing
            doGet(req, res);
            return;
        }

        boolean overrideEnabled = true;
//        if (null != req.getParameter("slackNotificationsOverrideEnabled") && req.getParameter("slackNotificationsOverrideEnabled").equals("on")) {
//            overrideEnabled = true;
//        }

        boolean enabled = false;
        if (null != req.getParameter("slackNotificationsEnabled") && req.getParameter("slackNotificationsEnabled").equals("on")) {
          enabled = true;
        }

        boolean openedEnabled = false;
        if (null != req.getParameter("slackNotificationsOpenedEnabled") && req.getParameter("slackNotificationsOpenedEnabled").equals("on")) {
            openedEnabled = true;
        }

        boolean reopenedEnabled = false;
        if (null != req.getParameter("slackNotificationsReopenedEnabled") && req.getParameter("slackNotificationsReopenedEnabled").equals("on")) {
            reopenedEnabled = true;
        }

        boolean updatedEnabled = false;
        if (null != req.getParameter("slackNotificationsUpdatedEnabled") && req.getParameter("slackNotificationsUpdatedEnabled").equals("on")) {
            updatedEnabled = true;
        }

        boolean approvedEnabled = false;
        if (null != req.getParameter("slackNotificationsApprovedEnabled") && req.getParameter("slackNotificationsApprovedEnabled").equals("on")) {
            approvedEnabled = true;
        }

        boolean unapprovedEnabled = false;
        if (null != req.getParameter("slackNotificationsUnapprovedEnabled") && req.getParameter("slackNotificationsUnapprovedEnabled").equals("on")) {
            unapprovedEnabled = true;
        }

        boolean declinedEnabled = false;
        if (null != req.getParameter("slackNotificationsDeclinedEnabled") && req.getParameter("slackNotificationsDeclinedEnabled").equals("on")) {
            declinedEnabled = true;
        }

        boolean mergedEnabled = false;
        if (null != req.getParameter("slackNotificationsMergedEnabled") && req.getParameter("slackNotificationsMergedEnabled").equals("on")) {
            mergedEnabled = true;
        }

        boolean commentedEnabled = false;
        if (null != req.getParameter("slackNotificationsCommentedEnabled") && req.getParameter("slackNotificationsCommentedEnabled").equals("on")) {
            commentedEnabled = true;
        }

        boolean enabledPush = false;
        if (null != req.getParameter("slackNotificationsEnabledForPush") && req.getParameter("slackNotificationsEnabledForPush").equals("on")) {
            enabledPush = true;
        }

        NotificationLevel notificationLevel = NotificationLevel.VERBOSE;
//        if (null != req.getParameter("slackNotificationLevel")) {
//            notificationLevel = NotificationLevel.valueOf(req.getParameter("slackNotificationLevel"));
//        }

        NotificationLevel notificationPrLevel = NotificationLevel.VERBOSE;
//        if (null != req.getParameter("slackNotificationPrLevel")) {
//            notificationPrLevel = NotificationLevel.valueOf(req.getParameter("slackNotificationPrLevel"));
//        }

        String channel = "";
        String webHookUrl = req.getParameter("slackWebHookUrl");
        slackSettingsService.setSlackSettings(
                repository,
                new ImmutableSlackSettings(
                        overrideEnabled,
                        enabled,
                        openedEnabled,
                        reopenedEnabled,
                        updatedEnabled,
                        approvedEnabled,
                        unapprovedEnabled,
                        declinedEnabled,
                        mergedEnabled,
                        commentedEnabled,
                        enabledPush,
                        notificationLevel,
                        notificationPrLevel,
                        channel,
                        webHookUrl));

        doGet(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (Strings.isNullOrEmpty(pathInfo) || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String[] pathParts = pathInfo.substring(1).split("/");
        if (pathParts.length != 2) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String projectKey = pathParts[0];
        String repoSlug = pathParts[1];
        
        this.repository = repositoryService.getBySlug(projectKey, repoSlug);
        if (repository == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        doView(repository, response);

    }

    private void doView(Repository repository, HttpServletResponse response)
            throws ServletException, IOException {
        validationService.validateForRepository(repository, Permission.REPO_ADMIN);
        SlackSettings slackSettings = slackSettingsService.getSlackSettings(repository);
        render(response,
                "stash.page.slack.settings.viewSlackSettings",
                ImmutableMap.<String, Object>builder()
                        .put("repository", repository)
                        .put("slackSettings", slackSettings)
                        .put("notificationLevels", new SelectFieldOptions(NotificationLevel.values()).toSoyStructure())
                        .build()
        );
    }

    private void render(HttpServletResponse response, String templateName, Map<String, Object> data)
            throws IOException, ServletException {
        pageBuilderService.assembler().resources().requireContext("plugin.page.slack");
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
