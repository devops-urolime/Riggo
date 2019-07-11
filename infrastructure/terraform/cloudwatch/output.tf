output "cloudwatch-log-GroupName" {
  value = "${aws_cloudwatch_log_group.ecs_logs.name}"
}
