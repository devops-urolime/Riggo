resource "aws_sns_topic" "monitoring-notification" {
  name = "${terraform.workspace}-Alarm-Notifications"

 }

resource "null_resource" "alarm_email_subscription" {
  
# count = "${length(split(",", var.alarms_email))}"

 provisioner "local-exec" {
    command = "aws sns subscribe --topic-arn ${aws_sns_topic.monitoring-notification.arn}  --protocol email --notification-endpoint [${var.alarms_email}]"
  }
 
}