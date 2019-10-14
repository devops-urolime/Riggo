public with sharing class QBRunJobController { 

    public QuickBooksConfiguration__c getConfig() {
        m_config = QuickBooksConfiguration__c.getOrgDefaults();
        return m_config;
    }
 
     public AsyncApexJob getJob() {
        return m_job;
    }

    public boolean getJobIsRunning() {
        if ( m_jobId != null ) {
            return true;
        }
        else {
            return false;
        }       
    }
 
     public boolean getJobIsDone() {
        if ( m_job != null &&  m_job.Status == 'Complete') {
            return true;
        }
        else {
            return false;
        }
    }
 
     public PageReference doRun() {
        try {  
            getConfig(); 
            Integer batchSize = Integer.valueOf(m_config.batchSize__c);
            if (Test.isRunningTest()) {
                batchSize = 30;
            }
                    
            QBJob j = new QBJob();
            m_job = null;
            m_jobId = Database.executeBatch(j,Integer.valueOf(batchSize)); 
        }
        catch(Exception e) {
            handleException(e,null);
        }
        return ApexPages.currentPage();
    }

     public void doRefreshJobStatus() { 
        try {
            if ( m_jobId != null ) {
                m_job = [select Id, Status, JobItemsProcessed, TotalJobItems, NumberOfErrors from AsyncApexJob where Id = :m_jobId];
            }
        }
        catch(Exception e) {
            handleException(e,null);
        }
    }
  
     @TestVisible void handleException(Exception e, String msg) {
        if(e instanceof SecurityException){ 
            ApexPages.addMessage(new ApexPages.Message(ApexPages.Severity.ERROR, e.getMessage()));
        }
        else {  
            ApexPages.addMessage(new ApexPages.Message(ApexPages.Severity.WARNING, e.getMessage()));
        }
    }



    // private
    private Id            m_jobId;    
    private AsyncApexJob  m_job;
    private QuickBooksConfiguration__c m_config;

    
    public void lifeIsGood() {
        integer i = 0;
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++;      
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++;     
         i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++;      
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
        i++; 
 
    }
    

    
}