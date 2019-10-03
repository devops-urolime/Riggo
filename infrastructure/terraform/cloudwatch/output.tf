output "cloudwatch-log-GroupName" {
  value = "${aws_cloudwatch_log_group.ecs_logs.name}"
}

output "arn_alarm_sns" {
  value       = "${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"
  description = "Email SNS topic ARN"
}

output "clientapp_loggroup" {
  value = "${aws_cloudwatch_log_group.clientapp-codebuild.name}"
}

output "apiservice_loggroup" {
  value = "${aws_cloudwatch_log_group.apiservice-codebuild.name}"
}
