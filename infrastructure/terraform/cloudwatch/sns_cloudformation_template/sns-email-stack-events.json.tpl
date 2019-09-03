{
  "AWSTemplateFormatVersion": "2010-09-09",
  "Resources" : {
    "EmailSNSTopic": {
      "Type" : "AWS::SNS::Topic",
      "Properties" : {
        "TopicName" : "${topic_name}",
        "DisplayName" : "${display_name}",
        "Subscription": [
          ${subscriptions}
        ]
      }
    },
    "SNSPolicy" : {
       "Type" : "AWS::SNS::TopicPolicy",
       "Properties" : {
          "PolicyDocument" :  {
              "Id" : "${PolicyDocument_name}",
              "Version" : "2012-10-17",
              "Statement" : [ {
                 "Sid" : "Event-Sources",
                 "Effect" : "Allow",
                 "Principal" : {
                      "Service" : "events.amazonaws.com"
                 },
                 "Action" : "sns:Publish",
                 "Resource" :  { "Ref" : "EmailSNSTopic" } 
               }]
            },
            "Topics" : [ { "Ref" : "EmailSNSTopic" } ]
       }
    }
  },

  "Outputs" : {
    "ARN" : {
      "Description" : "Email SNS Topic ARN",
      "Value" : { "Ref" : "EmailSNSTopic" }
    }
  }
}