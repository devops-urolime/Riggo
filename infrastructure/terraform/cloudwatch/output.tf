output "cloudwatch-log-GroupName" {
  value = "${aws_cloudwatch_log_group.ecs_logs.name}"
}

output "arn" {
  value       = "${aws_cloudformation_stack.sns_topic.outputs["ARN"]}"
  description = "Email SNS topic ARN"
}