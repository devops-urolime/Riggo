# resource "aws_sns_topic" "monitoring-notification" {
#   name = "${terraform.workspace}-Alarm-Notifications"

#  }

# resource "null_resource" "alarm_email_subscription" {
  
# # count = "${length(split(",", var.alarms_email))}"
# count = "${length(var.alarms_email)}"
#  provisioner "local-exec" {
#     command = "bash subscription.sh ${aws_cloudformation_stack.sns_topic.outputs["ARN"]} ${var.alarms_email}"
#      #command = "aws sns subscribe --topic-arn ${aws_cloudformation_stack.sns_topic.outputs["ARN"]}  --protocol email --notification-endpoint ${index(var.alarms_email, count.index)}"
#     #interpreter = ["PowerShell","-Command"]
#   }
 
# }

data "template_file" "cloudformation_sns_stack" {
  template = "${file("${path.module}/sns_cloudformation_template/sns-email-stack.json.tpl")}"

  vars = {
    display_name  = "${terraform.workspace}-Alarm-Notifications"
    topic_name = "${terraform.workspace}-Alarm-Notifications"
    subscriptions = "${join("," , formatlist("{ \"Endpoint\": \"%s\", \"Protocol\": \"%s\"  }", var.alarms_email, var.protocol))}"
  }
}

resource "aws_cloudformation_stack" "sns_topic" {
  name          = "${terraform.workspace}-SNS-cloudformation-stack"
  template_body = "${data.template_file.cloudformation_sns_stack.rendered}"

  tags = {
    name = "${terraform.workspace}"
  }
}