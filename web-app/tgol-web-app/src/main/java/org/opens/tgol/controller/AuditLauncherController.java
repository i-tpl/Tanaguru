/*
 * Tanaguru - Automated webpage assessment
 * Copyright (C) 2008-2011  Open-S Company
 *
 * This file is part of Tanaguru.
 *
 * Tanaguru is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact us by mail: open-s AT open-s DOT com
 */
package org.opens.tgol.controller;

import org.opens.tgol.command.AuditSetUpCommand;
import org.opens.tgol.entity.contract.Contract;
import org.opens.tgol.entity.product.Restriction;
import org.opens.tgol.orchestrator.TanaguruOrchestrator;
import org.opens.tgol.util.HttpStatusCodeFamily;
import org.opens.tgol.util.TgolKeyStore;
import org.opens.tgol.voter.restriction.RestrictionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.archive.net.UURIFactory;
import org.opens.tanaguru.entity.audit.Audit;
import org.opens.tanaguru.entity.audit.AuditStatus;
import org.opens.tanaguru.entity.parameterization.Parameter;
import org.opens.tanaguru.entity.parameterization.ParameterElement;
import org.opens.tanaguru.entity.service.parameterization.ParameterElementDataService;
import org.opens.tanaguru.entity.subject.Page;
import org.opens.tanaguru.entity.subject.Site;
import org.opens.tgol.exception.LostInSpaceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/** 
 *
 * @author jkowalczyk
 */
@Controller
public class AuditLauncherController extends AuditDataHandlerController {

    private static final Logger LOGGER = Logger.getLogger(AuditLauncherController.class);
    private static String PROXY_HOST_PARAM_KEY="PROXY_HOST";
    private static String PROXY_PORT_PARAM_KEY="PROXY_PORT";
    private static String DEPTH_PARAM_KEY="DEPTH";
    private static String MAX_DOCUMENT_PARAM_KEY="MAX_DOCUMENTS";
    private static String DEPTH_PAGE_PARAM_VALUE="0";
    private String groupePagesName = "";
    /**
     * The TanaguruOrchestrator instance needed to launch the audit process
     */
    private TanaguruOrchestrator tanaguruExecutor;

    @Autowired
    public final void setTanaguruExecutor(TanaguruOrchestrator tanaguruExecutor) {
        this.tanaguruExecutor = tanaguruExecutor;
    }
    /**
     * The RestrictionHandler instance needed to decide 
     */
    private RestrictionHandler restrictionHandler;

    @Autowired
    public final void setRestrictionHandler(RestrictionHandler restrictionHandler) {
        this.restrictionHandler = restrictionHandler;
    }
    /**
     * The ParameterElementDataService instance
     */
    private ParameterElementDataService parameterElementDataService;

    @Autowired
    public void setParameterElementDataService(ParameterElementDataService parameterElementDataService) {
        this.parameterElementDataService = parameterElementDataService;
        setParameterElementMap(parameterElementDataService);
    }
    private Map<String, ParameterElement> parameterElementMap = new HashMap<String, ParameterElement>();

    private void setParameterElementMap(ParameterElementDataService peds) {
        for (ParameterElement pe : peds.findAll()) {
            parameterElementMap.put(pe.getParameterElementCode(), pe);
        }
    }
    /**
     * default audit page parameter set.
     */
    Set<Parameter> auditPageParamSet = null;

    private String httpProxyHost;
    public String getProxyHost() {
        return httpProxyHost;
    }

    public void setProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    private String httpProxyPort;
    public String getProxyPort() {
        return httpProxyPort;
    }

    public void setProxyPort(String httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    /**
     * Multiple Url can be set through a unique String separated by ;
     */
    private List<String> proxyExclusionUrlList = new ArrayList<String>();
    public List<String> getProxyExclusionUrlList() {
        return proxyExclusionUrlList;
    }

    public void setProxyExclusionUrl(String proxyExclusionUrl) {
        proxyExclusionUrlList.addAll(Arrays.asList(proxyExclusionUrl.split(";")));
    }

    public AuditLauncherController() {
        super();
    }

    /**
     * This methods enables an authenticated user to launch an audit and deals with
     * the breadcrumb
     * @param contractId
     * @param model
     * @return
     */
    @RequestMapping(value = TgolKeyStore.LAUNCH_AUDIT_CONTRACT_URL, method = RequestMethod.GET)
    @Secured(TgolKeyStore.ROLE_GUEST_KEY)
    public String launchAudit(
            @RequestParam(TgolKeyStore.CONTRACT_ID_KEY) String contractId,
            Model model,
            HttpServletRequest request) {
        Contract contract = getContractDataService().read(Long.valueOf(contractId));
        if (isContractExpired(contract)) {
            return displayContractView(contract, model);
        } else {
            // before launching the audit, we check whether any restriction on the
            //contract forbids it.
            String checkResult = restrictionHandler.checkRestriction(contract, getClientIpAddress());
            if (!checkResult.equalsIgnoreCase(TgolKeyStore.ACT_ALLOWED)) {
                return checkResult;
            }
            // the parameter is retrieved through the session due to the redirection
            AuditSetUpCommand auditSetUpCommand = (AuditSetUpCommand) request.getSession().getAttribute(TgolKeyStore.AUDIT_SET_UP_COMMAND_KEY);
            request.getSession().removeAttribute(TgolKeyStore.AUDIT_SET_UP_COMMAND_KEY);
            if (auditSetUpCommand != null && !auditSetUpCommand.isAuditSite()) {
                return preparePageAudit(auditSetUpCommand, model);
            }
            tanaguruExecutor.auditSite(
                    contract,
                    contract.getUrl(),
                    getClientIpAddress(),
                    getUserParamSet(auditSetUpCommand, contract.getId(), -1, contract.getUrl()));
            model.addAttribute(TgolKeyStore.TESTED_URL_KEY, contract.getUrl());
            model.addAttribute(TgolKeyStore.CONTRACT_ID_KEY, contract.getId());
            model.addAttribute(TgolKeyStore.CONTRACT_NAME_KEY, contract.getLabel());
            model.addAttribute(TgolKeyStore.AUTHENTICATED_USER_KEY, getCurrentUser());
            return TgolKeyStore.AUDIT_IN_PROGRESS_VIEW_NAME;
        }
    }

    /**
     * This methods controls the validity of the form and launch an audit with
     * values populated by the user. In case of audit failure, an appropriate
     * message is displayed
     * @param auditSetUpCommand
     * @param model
     * @return
     */
    private String preparePageAudit(
            AuditSetUpCommand auditSetUpCommand,
            Model model) {
        Audit audit = null;
        // if the form is correct, we launch the audit
        if (auditSetUpCommand.getUrlList().isEmpty()) {
            audit = launchUploadAudit(
                    getContractDataService().read(auditSetUpCommand.getContractId()),
                    auditSetUpCommand);
        } else {
            audit = launchPageAudit(
                    getContractDataService().read(auditSetUpCommand.getContractId()),
                    auditSetUpCommand);
        }
        if (audit.getStatus() != AuditStatus.COMPLETED) {
            return prepareFailedAuditData(audit,model);
        }
        model.addAttribute(TgolKeyStore.WEBRESOURCE_ID_KEY, audit.getSubject().getId());
        if (audit.getSubject() instanceof Site) {
            // in case of group of page, we display the list of audited pages
            model.addAttribute(TgolKeyStore.STATUS_KEY, HttpStatusCodeFamily.f2xx);
            return TgolKeyStore.PAGE_LIST_XXX_VIEW_REDIRECT_NAME;
        } else if (audit.getSubject() instanceof Page) {
            return TgolKeyStore.RESULT_PAGE_VIEW_REDIRECT_NAME;
        }
        throw new LostInSpaceException(getCurrentUser());
    }

    /**
     * This method launches the audit process using the tanaguru orchestrator
     * bean
     * @param url
     * @return
     */
    private Audit launchPageAudit(
            final Contract contract,
            final AuditSetUpCommand auditSetUpCommand) {
        int urlCounter = 0;
        // 10 String are received from the form even if these String are empty.
        // We sort the string and only keep the not empty ones.
        List<String> trueUrl = new ArrayList<String>();
        for (String str : auditSetUpCommand.getUrlList()) {
            if (str != null && !str.isEmpty()) {
                try {
                    trueUrl.add(urlCounter, UURIFactory.getInstance(str.trim()).getEscapedURI());
                } catch (URIException ex) {
                    Logger.getLogger(this.getClass()).warn(ex);
                    trueUrl.add(urlCounter, str.trim());
                }
                urlCounter++;
            }
        }
        Set<Parameter> paramSet = getUserParamSet(auditSetUpCommand, contract.getId(), trueUrl.size(), trueUrl.get(0));
        if (trueUrl.size() == 1) {
            LOGGER.debug("Launch " + trueUrl.get(0) + " audit in page mode");
            return tanaguruExecutor.auditPage(
                    contract,
                    trueUrl.get(0),
                    getClientIpAddress(),
                    paramSet);
        } else if (trueUrl.size() > 1) {
            String[] finalUrlTab = new String[trueUrl.size()];
            for (int i = 0; i < trueUrl.size(); i++) {
                finalUrlTab[i] = trueUrl.get(i);
            }
            groupePagesName = extractGroupNameFromUrl(finalUrlTab[0]);
            LOGGER.debug("Launch " + groupePagesName + " audit in group of pages mode");
            return tanaguruExecutor.auditSite(
                    contract,
                    groupePagesName,
                    trueUrl,
                    getClientIpAddress(),
                    paramSet);
        } else {
            return null;
        }
    }

    private Audit launchUploadAudit(
            final Contract contract,
            final AuditSetUpCommand auditSetUpCommand) {
        Map<String, String> fileMap = auditSetUpCommand.getFileMap();
        Set<Parameter> paramSet = getUserParamSet(auditSetUpCommand, contract.getId(), fileMap.size(), null);
        return tanaguruExecutor.auditPageUpload(
                contract,
                fileMap,
                getClientIpAddress(),
                paramSet);
    }

    /**
     * This methods extracts the name of a group of pages from an url
     * @param url
     * @return
     */
    private String extractGroupNameFromUrl(String url) {
        int fromIndex = 0;
        if (url.startsWith(TgolKeyStore.HTTP_PREFIX)) {
            fromIndex = TgolKeyStore.HTTP_PREFIX.length();
        } else if (url.startsWith(TgolKeyStore.HTTPS_PREFIX)) {
            fromIndex = TgolKeyStore.HTTPS_PREFIX.length();
        } else {
            url = TgolKeyStore.HTTP_PREFIX + url;
            fromIndex = TgolKeyStore.HTTP_PREFIX.length();
        }
        if (url.indexOf(TgolKeyStore.SLASH_CHAR, fromIndex) != -1) {
            return url.substring(0, url.indexOf(TgolKeyStore.SLASH_CHAR, fromIndex));
        } else {
            return url;
        }
    }

    /**
     * This method gets the default parameters for an audit and eventually
     * override some of them in case of contract restriction.
     * @param auditSetUpCommand
     * @param contractId
     * @param nbOfPages
     * @param url
     * @return
     */
    private Set<Parameter> getUserParamSet(AuditSetUpCommand auditSetUpCommand, Long contractId, int nbOfPages, String url) {
        Set<Parameter> paramSet = getDefaultParamSet();
        Set<Parameter> userParamSet = new HashSet<Parameter>();
        if (auditSetUpCommand != null) {
            if (!auditSetUpCommand.isAuditSite()) {
                paramSet = getParameterDataService().updateParameterSet(paramSet, getAuditPageParameterSet(nbOfPages));
            }
            for (Map.Entry<String, String> entry : auditSetUpCommand.getAuditParameter().entrySet()) {
                Parameter param = getParameterDataService().getParameter(parameterElementMap.get(entry.getKey()), entry.getValue());
                userParamSet.add(param);
            }
            paramSet = getParameterDataService().updateParameterSet(paramSet, userParamSet);
        } else {
            Set<? extends Restriction> restrictionSet =
                    getContractDataService().read(contractId).getRestrictionSet();
            for (Parameter param : paramSet) {
                for (Restriction restriction : restrictionSet) {
                    if (restriction.getRestrictionElement().getCode().
                            equalsIgnoreCase(param.getParameterElement().getParameterElementCode())) {
                        param = getParameterDataService().getParameter(param.getParameterElement(), restriction.getValue());
                        break;
                    }
                }
                userParamSet.add(param);
            }
            paramSet = getParameterDataService().updateParameterSet(paramSet, userParamSet);
        }
        paramSet = setProxyParameters(paramSet, url);
        return paramSet;
    }

    /**
     * 
     * @param paramSet
     * @param url
     */
    private Set<Parameter> setProxyParameters(Set<Parameter> paramSet, String url) {
	// Bug #325 : if the url is null, in the case of an upload audit for example, 
        // the parameters are not modified.
        if (StringUtils.isEmpty(url)){
            return paramSet;
        }

        if ((StringUtils.isEmpty(httpProxyHost) && StringUtils.isEmpty(httpProxyPort))) {
            return paramSet;
        }
        for (String urlExclusion : proxyExclusionUrlList) {
            if (StringUtils.isNotEmpty(urlExclusion) && url.contains(urlExclusion)) {
                return paramSet;
            }
        }
        try {
            Integer.valueOf(httpProxyPort);
        } catch (NumberFormatException nfe) {
            LOGGER.warn("Incorrect value of proxy Port : "+ httpProxyPort +". Proxy parameters are ignored");
            return paramSet;
        }
        Set<Parameter> proxyParamSet = new HashSet<Parameter>();
        ParameterElement proxyHostParameterElement =
                parameterElementDataService.getParameterElement(PROXY_HOST_PARAM_KEY);
        ParameterElement proxyPortParameterElement =
                parameterElementDataService.getParameterElement(PROXY_PORT_PARAM_KEY);
        Parameter proxyHostParameter = getParameterDataService().getParameter(proxyHostParameterElement, httpProxyHost);
        Parameter proxyPortParameter = getParameterDataService().getParameter(proxyPortParameterElement, httpProxyPort);
        proxyParamSet.add(proxyHostParameter);
        proxyParamSet.add(proxyPortParameter);
        LOGGER.debug("paramSet.size() " + paramSet.size());
        LOGGER.debug("proxyParamSet " + proxyParamSet.size());
        for (Parameter param: paramSet) {
            if (param.getParameterElement().getParameterElementCode().equals(PROXY_HOST_PARAM_KEY)) {
                LOGGER.debug(param.getValue());
            } else if (param.getParameterElement().getParameterElementCode().equals(PROXY_PORT_PARAM_KEY)) {
                LOGGER.debug(param.getValue());
            }
        }
        paramSet = getParameterDataService().updateParameterSet(paramSet, proxyParamSet);
        LOGGER.debug("after update paramSet.size() " + paramSet.size());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Audit is set to use proxy with parameters " + httpProxyHost + " : " + httpProxyPort);
        }
        for (Parameter param: paramSet) {
            if (param.getParameterElement().getParameterElementCode().equals(PROXY_HOST_PARAM_KEY)) {
                LOGGER.debug(param.getValue());
            } else if (param.getParameterElement().getParameterElementCode().equals(PROXY_PORT_PARAM_KEY)) {
                LOGGER.debug(param.getValue());
            } 
        }
        return paramSet;
    }

    /**
     * The default parameter set embeds a depth value that corresponds to the
     * site audit. We need here to replace this parameter by a parameter value
     * equals to 0.
     * @return
     */
    private Set<Parameter> getAuditPageParameterSet(int nbOfPages) {
        if (auditPageParamSet == null) {
            Set<Parameter> paramSet = new HashSet<Parameter>();
            ParameterElement depthParameterElement = parameterElementDataService.getParameterElement(DEPTH_PARAM_KEY);
            ParameterElement maxDocParameterElement = parameterElementDataService.getParameterElement(MAX_DOCUMENT_PARAM_KEY);
            Parameter depthParameter = getParameterDataService().getParameter(depthParameterElement, DEPTH_PAGE_PARAM_VALUE);
            Parameter maxDocParameter = getParameterDataService().getParameter(maxDocParameterElement, String.valueOf(nbOfPages));
            paramSet.add(depthParameter);
            paramSet.add(maxDocParameter);
            auditPageParamSet = getParameterDataService().updateParameterSet(getDefaultParamSet(), paramSet);
        }
        return auditPageParamSet;
    }

}
