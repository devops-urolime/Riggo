/**
 * Created by Viktor S. Yaremenko on 12/1/2018.
 */

public with sharing class RiggohTaskList {

    private Integer counter { get; set; }
    private String massMode { get; set; }//delete, complete, defer

    public transient Boolean pageSelected { get; set; }
    public transient List<TaskWrapper> listTasks;
    public Set<Id> selectedTasks;
    public Integer pageSize = 10;
    public Integer totalRecords { get; set; }
    public Integer totalPages { get; set; }
    public Integer currentPage {
        get {
            return counter / pageSize + 1;
        }
        set;
    }
    public Boolean showSelected { get; set; }
    public Date fromDate { get; set; }
    public Date toDate { get; set; }
    public String filterStatus { get; set; }
    public String filterPriority { get; set; }
    public Map<ApexPages.Severity, List<String>> pageMessages = new Map<ApexPages.Severity, List<String>>();

    public Id actionId { get; set; }

    public RiggohTaskList() {
        initCounters();
    }

    private String getWhereClause() {
        String soql = '';
        List<String> wher = new List<String>();
        if (showSelected == true && !selectedTasks.isEmpty()) {
            List<String> t = new List<String>();
            for (Id sid : selectedTasks) {
                t.add('\'' + sid + '\'');
            }
            wher.add(' Id IN (' + String.join(t, ', ') + ')');
        }
        if (String.isNotBlank(filterStatus) && filterStatus != 'any') {
            wher.add(' Status = \'' + filterStatus + '\'');
        }
        if (String.isNotBlank(filterPriority) && filterPriority != 'any') {
            wher.add(' Priority = \'' + filterPriority + '\'');
        }
        if (fromDate != null) {
            wher.add(' ActivityDate >= ' + String.valueOf(fromDate) + ' ');
        }
        if (toDate != null) {
            wher.add(' ActivityDate <= ' + String.valueOf(toDate) + ' ');
        }
        if (!wher.isEmpty()) {
            soql = soql + ' WHERE ' + String.join(wher, ' AND ');
        }
        return soql;
    }

    private void initCounters() {
        String soql = 'SELECT COUNT() FROM Task ' + getWhereClause();
        counter = 0;
        totalRecords = Database.countQuery(soql);
        if (math.mod(totalRecords, pageSize) > 0) {
            totalPages = totalRecords / pageSize + 1;
        } else {
            totalPages = (totalRecords / pageSize);
        }
    }

    public void addPageMessage(ApexPages.Severity sev, String msg) {
        if (pageMessages.get(sev) == null)
            pageMessages.put(sev, new List<String>());
        pageMessages.get(sev).add(msg);
        ApexPages.addMessage(new ApexPages.message(sev, msg));
    }

    public void showPageMessage() {
        for (ApexPages.Severity sev : pageMessages.keySet())
            for (String msg : pageMessages.get(sev))
                ApexPages.addMessage(new ApexPages.message(sev, msg));
    }

    public Boolean getHasNext() {
        return currentPage < totalPages;
    }

    public Boolean getHasPrevious() {
        return currentPage > 1;
    }

    public PageReference next() {
        pageMessages.clear();
        processSelected();
        counter += pageSize;
        if (counter > 2000 ) {
            counter = 2000; //OFFSET Limitation
            addPageMessage(ApexPages.severity.INFO,
                    'Offset limitation 2000 records. Please use filters.'
            );
        }
        showPageMessage();
        return null;
    }

    public PageReference last() {
        pageMessages.clear();
        processSelected();
        counter = totalRecords - math.mod(totalRecords, pageSize);
        if (counter > 2000 ) {
            counter = 2000; //OFFSET Limitation
            addPageMessage(ApexPages.severity.INFO,
                    'Offset limitation 2000 records. Please use filters.'
            );
        }
        showPageMessage();
        return null;
    }

    public PageReference first() {
        pageMessages.clear();
        processSelected();
        counter = 0;
        showPageMessage();
        return null;
    }

    public PageReference previous() {
        pageMessages.clear();
        processSelected();
        counter -= pageSize;
        showPageMessage();
        return null;
    }

    public PageReference setFilter() {
        pageMessages.clear();
        //processSelected();
        initCounters();
        getRecords();
        showPageMessage();
        return null;
    }

    public PageReference nop() {
        showPageMessage();
        return null;
    }

    public PageReference setComplete() {
        pageMessages.clear();
        if (String.isBlank(actionId)) {
            addPageMessage(ApexPages.severity.INFO,
                    'Please select the record.'
            );
            showPageMessage();
            return null;
        }
        Task updTask = new Task(Id = actionId, Status = 'Completed');
        Database.SaveResult dr = Database.update(updTask, false); // Boolean mean All_or_None
        system.debug(dr);
        if (!dr.isSuccess()) {
            for (Database.Error err : dr.getErrors()) {
                addPageMessage(ApexPages.severity.ERROR, err.getMessage());
            }
        }
        showPageMessage();
        getRecords();
        return null;
    }

    public PageReference setDefer() {
        pageMessages.clear();
        if (String.isBlank(actionId)) {
            addPageMessage(ApexPages.severity.INFO,
                    'Please select the record.'
            );
            showPageMessage();
            return null;
        }
        Task updTask = new Task(Id = actionId, Status = 'Deferred');
        Database.SaveResult dr = Database.update(updTask, false); // Boolean mean All_or_None
        system.debug(dr);
        if (!dr.isSuccess()) {
            for (Database.Error err : dr.getErrors()) {
                addPageMessage(ApexPages.severity.ERROR, err.getMessage());
            }
        }
        showPageMessage();
        getRecords();
        return null;
    }

    public Pagereference massDelete() {
        pageMessages.clear();
        processSelected();
        if (selectedTasks.isEmpty()) {
            addPageMessage(ApexPages.severity.INFO,
                    'Please select at least one record.'
            );
            showPageMessage();
            return null;
        }
        showSelected = true;
        massMode = 'delete';
        addPageMessage(
                ApexPages.severity.WARNING,
                'Are you sure you want to delete '
                        + selectedTasks.size()
                        + ' record(s)?'
        );
        showPageMessage();
        return null;
    }

    public Pagereference massComplete() {
        pageMessages.clear();
        processSelected();
        if (selectedTasks.isEmpty()) {
            addPageMessage(ApexPages.severity.INFO,
                    'Please select at least one record.'
            );
            showPageMessage();
            return null;
        }
        showSelected = true;
        massMode = 'complete';
        addPageMessage(
                ApexPages.severity.WARNING,
                'Are you sure you want to complete '
                        + selectedTasks.size()
                        + ' record(s)?'
        );
        showPageMessage();
        return null;
    }

    public Pagereference massDefer() {
        pageMessages.clear();
        processSelected();
        if (selectedTasks.isEmpty()) {
            addPageMessage(ApexPages.severity.INFO,
                    'Please select at least one record.'
            );
            showPageMessage();
            return null;
        }
        showSelected = true;
        massMode = 'defer';
        addPageMessage(
                ApexPages.severity.WARNING,
                'Are you sure you want to defer '
                        + selectedTasks.size()
                        + ' record(s)?'
        );
        showPageMessage();
        return null;
    }

    public PageReference back() {
        pageMessages.clear();
        processSelected();
        showSelected = false;
        pageMessages.clear();
        return null;
    }

    public Pagereference confirm() {
        pageMessages.clear();
        processSelected();
        Integer suc = 0; // success processed record counter
        showSelected = false;
        List<Id> s = new List<Id>();
        // real mass delete
        s.addAll(selectedTasks);
        selectedTasks.clear();
        listTasks.clear();
        if (massMode == 'delete') {
            Database.DeleteResult[] drList = Database.delete(s, false); // Boolean mean All_or_None
            for (Database.DeleteResult dr : drList) {
                if (dr.isSuccess()) {
                    suc++;
                } else {
                    for (Database.Error err : dr.getErrors()) {
                        addPageMessage(ApexPages.severity.ERROR, err.getMessage());
                    }
                }
            }
        } else { // complete, defer
            String stat = massMode == 'complete' ? 'Completed' : 'Deferred';
            List<Task> updTasks = new List<Task>();
            for (Id tid : s) {
                updTasks.add(new Task(Id = tid, Status = stat));
            }
            Database.SaveResult[] drList = Database.update(updTasks, false); // Boolean mean All_or_None
            for (Database.SaveResult dr : drList) {
                if (dr.isSuccess()) {
                    suc++;
                } else {
                    for (Database.Error err : dr.getErrors()) {
                        addPageMessage(ApexPages.severity.ERROR, err.getMessage());
                    }
                }
            }
        }

        if (suc > 0) {
            addPageMessage(
                    ApexPages.severity.INFO,
                    'Number of records deleted: ' + suc
            );
        }
        getRecords();
        return null;
    }


    public void processSelected() {
        if (listTasks == null) return;
        for (TaskWrapper ro : listTasks) {
            if (ro.selected) {
                selectedTasks.add(ro.obj.Id);
            } else {
                selectedTasks.remove(ro.obj.Id);
            }
        }
    }

    public List<TaskWrapper> getRecords() {
        List<Id> ids = new List<Id>();

        if (listTasks == null) listTasks = new List<TaskWrapper>();
        if (selectedTasks == null) selectedTasks = new Set<Id>();
        listTasks.clear();
        String soql = 'SELECT Id, ActivityDate, Status, Subject, Priority, Who.Name, What.Name, Owner.Name FROM Task '
                + getWhereClause() + ' ORDER BY ActivityDate DESC LIMIT ' + String.valueOf(pageSize) + ' OFFSET ' + counter;
        for (Task em : Database.query(soql)) {
            Boolean contain = selectedTasks.contains(em.Id);
            listTasks.add(new TaskWrapper(em, contain));
        }
        return listTasks;
    }

    public List<SelectOption> getStatuses() {
        return getPicklistOptions('Task', 'Status');
    }

    public List<SelectOption> getPriorities() {
        return getPicklistOptions('Task', 'Priority');
    }

    public class TaskListException extends Exception {
    }

    public class TaskWrapper {

        public Task obj { get; set; }
        public Boolean selected { get; set; }

        public TaskWrapper(Task o) {
            this.selected = false;
            this.obj = o;
        }

        public TaskWrapper(Task o, Boolean check) {
            this.selected = (check == null) ? false : check;
            this.obj = o;
        }

    }
    private static Map<String, Schema.SObjectType> sGDesc {
        get {
            if (null == sGDesc) sGDesc = Schema.getGlobalDescribe();
            return sGDesc;
        }
        set;
    }

    public static List<SelectOption> getPicklistOptions(String objectName, String fieldName) {
        List<SelectOption> res = new List<SelectOption>();
        res.add(new SelectOption('any', 'Any'));

        SObjectType o = sGDesc.get(objectName);
        if (null == o) return res;

        Schema.DescribeSObjectResult od = o.getDescribe();
        if (!od.isAccessible()) return res;

        Schema.sObjectField f = od.fields.getMap().get(fieldName);
        if (null == f) return res;

        DescribeFieldResult fd = f.getDescribe();
        if (!fd.isAccessible()) return res;

        try {
            List<Schema.PicklistEntry> pvs = fd.getPicklistValues();
            if (pvs == null) {
                return res;
            }
            for (Schema.PicklistEntry pv : pvs) {
                res.add(new SelectOption(
                        pv.getValue(),
                        pv.getLabel()
                ));
            }
        } catch (Exception e) {
        }
        return res;
    }

}