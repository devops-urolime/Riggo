public class DetentionEmailsCtrl {
    public static void detentionRecordCreation(Set<Id> stopId){
        system.debug('--stopId--'+stopId);
        List<rtms__Stop__c> stopList = [Select Id,rtms__Location2__c,Carrier_Sales_Rep_Email__c,LoadStatus__c,rtms__Expected_Date__c,rtms__Appointment_Time__c,rtms__Load__c , (select id from Detention_Emails__r) from rtms__Stop__c where ID IN : stopId];
        system.debug('--stopList--'+stopList.size());
        set<id> accIdSet = new set<id>();
        Set<Id> loadIdSet = new Set<Id>();
        Map<Id,Boolean> sendDetentionEmailMap = new Map<Id,Boolean>();
        Map<Id,decimal> detentionPolicyinminutesMap = new Map<Id,decimal>();
        for(rtms__Stop__c  stp: stopList){
            if(stp.rtms__Location2__c != null){
                accIdSet.add(stp.rtms__Location2__c);
                loadIdSet.add(stp.rtms__Load__c);
            } 
        }
        
        if(loadIdSet.size() > 0){
            List<rtms__Load__c> loadList = [select id,rtms__Load_Status__c,rtms__Customer__c,rtms__Customer__r.Detention_Policy_in_minutes__c,rtms__Customer__r.Send_Detention_Email__c from rtms__Load__c where ID IN : loadIdSet];
            for(rtms__Load__c ld : loadList){
                if(ld.rtms__Customer__r.Send_Detention_Email__c && ld.rtms__Load_Status__c != 'Unassigned' && ld.rtms__Load_Status__c !=  'Cancelled' && ld.rtms__Load_Status__c !=  'Declined' && ld.rtms__Load_Status__c != 'Expired'){
                    sendDetentionEmailMap.put(ld.Id,ld.rtms__Customer__r.Send_Detention_Email__c);
                    if(ld.rtms__Customer__r.Detention_Policy_in_minutes__c != null && ld.rtms__Customer__r.Detention_Policy_in_minutes__c > 0){
                        detentionPolicyinminutesMap.put(ld.Id,ld.rtms__Customer__r.Detention_Policy_in_minutes__c);
                    }
                }
            } 
        }
        
        Map<Id,String> timezonemap = new Map<Id,String>();
        if(accIdSet.size() > 0){
            List<Account> accList = [select id,Time_Zone__c from Account where Id IN:accIdSet and Time_Zone__c != null];
            if(accList.size() > 0){
                for(Account acc : accList){
                    timezonemap.put(acc.Id,acc.Time_Zone__c);
                }
            }
        }
        List<Detention_Email__c> detEmailList = new List<Detention_Email__c>();
        for(rtms__Stop__c  st: stopList){
            system.debug('-sendDetentionEmailMap.containskey(st.rtms__Load__c)---'+sendDetentionEmailMap.containskey(st.rtms__Load__c));
            system.debug('---timezonemap--'+timezonemap);
            if(st.rtms__Appointment_Time__c != null && sendDetentionEmailMap.containskey(st.rtms__Load__c)){
                if(timezonemap.containskey(st.rtms__Location2__c)){ 
                    system.debug('---timezone--');
                    Delete st.Detention_Emails__r;
                    string timezone  = timezonemap.get(st.rtms__Location2__c);
                    integer detentiontime = 0;
                    Datetime detentionEmailTimebased = null;
                    if(detentionPolicyinminutesMap.containskey(st.rtms__Load__c)){
                        detentiontime  =   integer.valueof(detentionPolicyinminutesMap.get(st.rtms__Load__c));
                    }
                    Integer hoursCalBasedOntimeZone = 0;
                    if(timezone == 'CDT - Central Time'){
                        hoursCalBasedOntimeZone = 1;
                    }else if(timezone == 'MDT - Mountain Time' || timezone == 'MST - Mountain Standard Time'){
                        hoursCalBasedOntimeZone = 2;
                    }else  if(timezone == 'PDT - Pacific Time'){
                        hoursCalBasedOntimeZone = 3;
                    }
                    if(timezone  != null && timezone != ''){
                        Detention_Email__c dtEm = new Detention_Email__c();
                        dtEm.Expected_DateTime__c= DateTime.newInstance(
                            st.rtms__Expected_Date__c, 
                            Time.newInstance(
                                (Integer.valueOf(st.rtms__Appointment_Time__c.split(':')[0]) + (hoursCalBasedOntimeZone - 1)),
                                (Integer.valueof(st.rtms__Appointment_Time__c.split(':')[1]) + detentiontime), 
                                0, 0));
                        dtEm.Detention_Email_Send_DateTime__c = DateTime.newInstance(
                            st.rtms__Expected_Date__c, 
                            Time.newInstance(
                                (Integer.valueOf(st.rtms__Appointment_Time__c.split(':')[0]) + hoursCalBasedOntimeZone),
                                (Integer.valueof(st.rtms__Appointment_Time__c.split(':')[1]) + detentiontime), 
                                0, 0));
                        system.debug('--dtEm.Expected_DateTime__c--'+dtEm.Expected_DateTime__c);
                        system.debug('--System.NOW()--'+System.NOW());
                        
                        if(dtEm.Detention_Email_Send_DateTime__c != null){
                            detentionEmailTimebased = DateTime.newInstance(
                            st.rtms__Expected_Date__c, 
                            Time.newInstance(
                                (Integer.valueOf(st.rtms__Appointment_Time__c.split(':')[0]) + hoursCalBasedOntimeZone),
                                (Integer.valueof(st.rtms__Appointment_Time__c.split(':')[1]) + detentiontime+1), 
                                0, 0));
                        }
                        
                        if(detentionEmailTimebased >= System.NOW()){
                            system.debug('--System.NOW()--'+System.NOW());
                        dtEm.Stop__c = st.Id;
                        dtEm.Load__c = st.rtms__Load__c;
                        dtEm.LoadStatus__c = st.LoadStatus__c;
                        dtEm.Carrier_Sales_Rep_Email__c = st.Carrier_Sales_Rep_Email__c;
                        detEmailList.add(dtEm);
                        }
                    } 
                }
            }
        } 
        if(detEmailList.size() > 0){
            try{
                insert detEmailList;
            }catch(Exception e){
                system.debug('--E--'+e);
            }
        }
    }
}