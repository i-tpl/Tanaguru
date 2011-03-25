package org.opens.tanaguru.service;

import java.util.HashSet;
import org.opens.tanaguru.entity.audit.Audit;
import org.opens.tanaguru.entity.audit.AuditStatus;
import org.opens.tanaguru.entity.reference.Test;
import org.opens.tanaguru.entity.subject.Page;
import org.opens.tanaguru.entity.subject.Site;
import org.opens.tanaguru.entity.service.audit.AuditDataService;
import org.opens.tanaguru.entity.service.subject.WebResourceDataService;
import java.util.List;
import java.util.Set;
import org.opens.tanaguru.entity.service.audit.ContentDataService;
import org.opens.tanaguru.entity.service.audit.ProcessResultDataService;
import org.opens.tanaguru.entity.service.reference.TestDataService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author ADEX
 */
public class AuditServiceImpl implements AuditService, AuditServiceListener {

    private AnalyserService analyserService;
    private AuditDataService auditDataService;
    private ConsolidatorService consolidatorService;
    private ContentAdapterService contentAdapterService;
    private CrawlerService crawlerService;
    private ProcessorService processorService;
    private ProcessResultDataService processResultDataService;
    private TestDataService testDataService;
    private WebResourceDataService webResourceDataService;
    private ContentDataService contentDataService;
    private AuditServiceThreadFactory auditServiceThreadFactory;
    private AuditServiceThreadQueue auditServiceThreadQueue;
    private Set<AuditServiceListener> listeners;

    @Autowired
    public AuditServiceImpl(ContentDataService contentDataService) {
        super();
        this.contentDataService = contentDataService;
    }

    public void add(AuditServiceListener listener) {
        if (listeners == null) {
            listeners = new HashSet<AuditServiceListener>();
        }
        listeners.add(listener);
    }

    public void remove(AuditServiceListener listener) {
        if (listeners == null) {
            return;
        }
        listeners.remove(listener);
    }

    public void setAuditServiceThreadQueue(AuditServiceThreadQueue auditServiceThreadQueue) {
        if (this.auditServiceThreadQueue != null) {
            this.auditServiceThreadQueue.remove(this);
        }
        this.auditServiceThreadQueue = auditServiceThreadQueue;
        this.auditServiceThreadQueue.add(this);
    }

    @Override
    public Audit auditPage(String pageUrl, String[] testCodeList) {
        Page page = webResourceDataService.createPage(pageUrl);

        List<Test> testList = testDataService.findAllByCode(testCodeList);

        Audit audit = auditDataService.create();
        audit.setSubject(page);
        audit.setTestList(testList);
        audit.setStatus(AuditStatus.CRAWLING);

        auditServiceThreadQueue.addPageAudit(audit);
        return audit;
    }

    @Override
    public Audit auditSite(String siteUrl, String[] testCodeList) {
        Site site = webResourceDataService.createSite(siteUrl);

        List<Test> testList = testDataService.findAllByCode(testCodeList);

        Audit audit = auditDataService.create();
        audit.setSubject(site);
        audit.setTestList(testList);
        audit.setStatus(AuditStatus.CRAWLING);

        auditServiceThreadQueue.addPageAudit(audit);
        return audit;
    }

    @Override
    public Audit auditSite(String siteUrl, String[] pageUrlList, String[] testCodeList) {
        Site site = webResourceDataService.createSite(siteUrl);
        for (String pageUrl : pageUrlList) {
            site.addChild(webResourceDataService.createPage(pageUrl));
        }

        List<Test> testList = testDataService.findAllByCode(testCodeList);

        Audit audit = auditDataService.create();
        audit.setSubject(site);
        audit.setTestList(testList);
        audit.setStatus(AuditStatus.CRAWLING);

        auditServiceThreadQueue.addSiteAudit(audit);
        return audit;
    }

    @Override
    public Audit audit(Audit audit) {
        AuditServiceThread auditServiceThread = auditServiceThreadFactory.create(auditDataService, contentDataService, processResultDataService, webResourceDataService, crawlerService, contentAdapterService, processorService, consolidatorService, analyserService, audit);
        auditServiceThread.run();
        audit = auditServiceThread.getAudit();
        return audit;
    }

    @Override
    public Audit init(Audit audit) {
        AuditServiceThread auditServiceThread = auditServiceThreadFactory.create(auditDataService, contentDataService, processResultDataService, webResourceDataService, crawlerService, contentAdapterService, processorService, consolidatorService, analyserService, audit);
        auditServiceThread.init();
        audit = auditServiceThread.getAudit();
        return audit;
    }

    @Override
    public Audit crawl(Audit audit) {
        AuditServiceThread auditServiceThread = auditServiceThreadFactory.create(auditDataService, contentDataService, processResultDataService, webResourceDataService, crawlerService, contentAdapterService, processorService, consolidatorService, analyserService, audit);
        auditServiceThread.crawl();
        audit = auditServiceThread.getAudit();
        return audit;
    }

    @Override
    public Audit loadContent(Audit audit) {
        AuditServiceThread auditServiceThread = auditServiceThreadFactory.create(auditDataService, contentDataService, processResultDataService, webResourceDataService, crawlerService, contentAdapterService, processorService, consolidatorService, analyserService, audit);
        auditServiceThread.loadContent();
        audit = auditServiceThread.getAudit();
        return audit;
    }

    @Override
    public Audit adaptContent(Audit audit) {
        AuditServiceThread auditServiceThread = auditServiceThreadFactory.create(auditDataService, contentDataService, processResultDataService, webResourceDataService, crawlerService, contentAdapterService, processorService, consolidatorService, analyserService, audit);
        auditServiceThread.adaptContent();
        audit = auditServiceThread.getAudit();
        return audit;
    }

    @Override
    public Audit process(Audit audit) {
        AuditServiceThread auditServiceThread = auditServiceThreadFactory.create(auditDataService, contentDataService, processResultDataService, webResourceDataService, crawlerService, contentAdapterService, processorService, consolidatorService, analyserService, audit);
        auditServiceThread.process();
        audit = auditServiceThread.getAudit();
        return audit;
    }

    @Override
    public Audit consolidate(Audit audit) {
        AuditServiceThread auditServiceThread = auditServiceThreadFactory.create(auditDataService, contentDataService, processResultDataService, webResourceDataService, crawlerService, contentAdapterService, processorService, consolidatorService, analyserService, audit);
        auditServiceThread.consolidate();
        audit = auditServiceThread.getAudit();
        return audit;
    }

    @Override
    public Audit analyse(Audit audit) {
        AuditServiceThread auditServiceThread = auditServiceThreadFactory.create(auditDataService, contentDataService, processResultDataService, webResourceDataService, crawlerService, contentAdapterService, processorService, consolidatorService, analyserService, audit);
        auditServiceThread.analyse();
        audit = auditServiceThread.getAudit();
        return audit;
    }

    public void setAnalyserService(AnalyserService analyserService) {
        this.analyserService = analyserService;
    }

    public void setAuditServiceThreadFactory(AuditServiceThreadFactory auditServiceThreadFactory) {
        this.auditServiceThreadFactory = auditServiceThreadFactory;
    }

    public void setContentDataService(ContentDataService contentDataService) {
        this.contentDataService = contentDataService;
    }

    public void setAuditDataService(AuditDataService auditDataService) {
        this.auditDataService = auditDataService;
    }
    
    public AuditDataService getAuditDataService() {
        return this.auditDataService;
    }

    public void setConsolidatorService(ConsolidatorService consolidatorService) {
        this.consolidatorService = consolidatorService;
    }

    public void setContentAdapterService(
            ContentAdapterService contentAdapterService) {
        this.contentAdapterService = contentAdapterService;
    }

    public void setCrawlerService(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    public void setProcessorService(ProcessorService processorService) {
        this.processorService = processorService;
    }

    public void setProcessResultDataService(
            ProcessResultDataService processResultDataService) {
        this.processResultDataService = processResultDataService;
    }

    public void setTestDataService(TestDataService testDataService) {
        this.testDataService = testDataService;
    }

    public void setWebResourceDataService(
            WebResourceDataService webResourceDAOService) {
        this.webResourceDataService = webResourceDAOService;
    }

    @Override
    public void auditCompleted(Audit audit) {
        fireAuditCompleted(audit);
    }

    private void fireAuditCompleted(Audit audit) {
        if (listeners == null) {
            return;
        }
        for (AuditServiceListener listener : listeners) {
            listener.auditCompleted(audit);
        }
    }
}
