# resource "aws_sns_topic" "monitoring-notification" {
#   name = "${terraform.workspace}-Alarm-Notifications"

#  }

# resource "null_resource" "alarm_email_subscription" {
  
# # count = "${length(split(",", var.alarms_email))}"
# count = "${length(var.alarms_email)}"
#  provisioner "local-exec" {
#     command = "bash subscription.sh ${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]} ${var.alarms_email}"
#      #command = "aws sns subscribe --topic-arn ${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}  --protocol email --notification-endpoint ${index(var.alarms_email, count.index)}"
#     #interpreter = ["PowerShell","-Command"]
#   }
 
# }

data "template_file" "cloudformation_sns_alert_stack" {
  template = "${file("${path.module}/sns_cloudformation_template/sns-email-stack-alert.json.tpl")}"

  vars = {
    display_name  = "${terraform.workspace}-Alarm-Notifications"
    topic_name = "${terraform.workspace}-Alarm-Notifications"
    subscriptions = "${join("," , formatlist("{ \"Endpoint\": \"%s\", \"Protocol\": \"%s\"  }", var.alarms_email, var.protocol))}"
  }
}

resource "aws_cloudformation_stack" "sns_alert_topic" {
  name          = "${terraform.workspace}-Alarm-SNS-cloudformation-stack"
  template_body = "${data.template_file.cloudformation_sns_alert_stack.rendered}"

  tags = {
    name = "${terraform.workspace}"
  }
}


data "template_file" "cloudformation_sns_event_stack" {
  template = "${file("${path.module}/sns_cloudformation_template/sns-email-stack-events.json.tpl")}"

  vars = {
    display_name  = "${terraform.workspace}-Event-Notifications"
    topic_name = "${terraform.workspace}-Event-Notifications"
    PolicyDocument_name = "${terraform.workspace}-Event-Notification-policy"
    subscriptions = "${join("," , formatlist("{ \"Endpoint\": \"%s\", \"Protocol\": \"%s\"  }", var.alarms_email, var.protocol))}"
  }
}

resource "aws_cloudformation_stack" "sns_event_topic" {
  name          = "${terraform.workspace}-Event-SNS-cloudformation-stack"
  template_body = "${data.template_file.cloudformation_sns_event_stack.rendered}"

  tags = {
    name = "${terraform.workspace}"
  }
}