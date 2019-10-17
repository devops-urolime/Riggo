public class ObjectFieldViewCtrl{
    public List<wrapFieldList> wrapFlields {get; set;} 
    public List<childWrapper> childwrapperList{get;set;}
    public List<SelectOption> options{get;set;}
    public string SelectedObject{get;set;}
    public ObjectFieldViewCtrl(){
       
         
        options = new List<SelectOption>();
        for ( Schema.SObjectType obj : Schema.getGlobalDescribe().values() ){
            Schema.DescribeSObjectResult objResult = obj.getDescribe();
            system.debug( 'Sobject: ' + objResult );
            system.debug( 'Sobject API Name: ' + objResult.getName() );
            system.debug( 'Sobject Label Name: ' + objResult.getLabel() );   
            options.add(new SelectOption(objResult.getName(),objResult.getLabel()));
            system.debug('--options---'+options);
            
        }
        SelectedObject = 'rtms__Load__c';
        Map<String , Schema.SObjectType> globalDescription = Schema.getGlobalDescribe();
        Schema.sObjectType objType = globalDescription.get(SelectedObject); 
        Schema.DescribeSObjectResult a_desc = objType.getDescribe(); 
        Map<String, Schema.SObjectField> a_fields = a_desc.fields.getMap();
        wrapFlields = new List<wrapFieldList>();
        childwrapperList = new List<childWrapper>();
        for(Schema.sObjectField fld:a_fields.values()){  
            childWrapper chd = new childWrapper();
            chd.fieldName = string.valueof(fld);
            chd.fieldSelection = false;
            childwrapperList.add(chd);
            if(childwrapperList.size() == 3){
                wrapFieldList wrp = new wrapFieldList();
                childwrapperList = new List<childWrapper>();
                wrp.childWrapper =  childwrapperList;
                wrapFlields.add(wrp);
            }
        }
        
    }
    
    public void newFieldfatch(){ 
         
        Map<String , Schema.SObjectType> globalDescription = Schema.getGlobalDescribe();
        Schema.sObjectType objType = globalDescription.get(SelectedObject); 
        Schema.DescribeSObjectResult a_desc = objType.getDescribe(); 
        Map<String, Schema.SObjectField> a_fields = a_desc.fields.getMap();
        wrapFlields = new List<wrapFieldList>();
        childwrapperList = new List<childWrapper>();
        for(Schema.sObjectField fld:a_fields.values()){  
            childWrapper chd = new childWrapper();
            chd.fieldName = string.valueof(fld);
            chd.fieldSelection = false;
            childwrapperList.add(chd);
            if(childwrapperList.size() == 3){
                wrapFieldList wrp = new wrapFieldList();
                childwrapperList = new List<childWrapper>();
                wrp.childWrapper =  childwrapperList;
                wrapFlields.add(wrp);
            }
        }
    }
    public void save(){
         String filedString = '';
        
        for(wrapFieldList wr : wrapFlields){
            for(childWrapper wrp : wr.childWrapper){
                 
                if(wrp.fieldSelection == true){
                    if(filedString == ''){
                        filedString = wrp.fieldName;
                    }else{
                    	filedString =filedString+','+wrp.fieldName;
                    }
                    system.debug('---filedString---'+filedString);
                }
            }
        }
        if(filedString != ''){
            Business_Rule__c br = new Business_Rule__c();
            br.Object_from__c = SelectedObject;
            br.Object_Field__c = filedString;
            insert br;
            
        }
    }
    public class wrapFieldList{
        public List<childWrapper> childWrapper{get;set;}
    }
    public class childWrapper{
        public string fieldName{get;set;} 
        public boolean fieldSelection{get;set;}
    }
    
}