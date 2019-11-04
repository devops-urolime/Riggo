

resource "aws_cloudwatch_metric_alarm" "elb-unhealthyhost-count-PROD" {
  alarm_name          = "${terraform.workspace}-ALB-unhealthy-count-ProdTG-alarm"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = "${var.alb_unhealthy_host_evaluation_period}"
  metric_name         = "UnHealthyHostCount"
  namespace           = "AWS/NetworkELB"
  period              = "${var.alb_unhealthy_host_period}"
  statistic           = "Maximum"
  threshold           = "${var.alb_unhealthy_host_count_threshold}"
  alarm_description   = "ALB unhealthy Host count Alarm for production target group"
  dimensions = {
      LoadBalancer = "${var.alb_suffix}"
      TargetGroup = "${var.targetgroup_prod_suffix}"
  }

  alarm_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]
  ok_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]
  insufficient_data_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]
  # insufficient_data_actions = []
} 



resource "aws_cloudwatch_metric_alarm" "elb-unhealthyhost-count-TEST" {
  alarm_name          = "${terraform.workspace}-ALB-unhealthy-count-TestTG-alarm"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = "${var.alb_unhealthy_host_evaluation_period}"
  metric_name         = "UnHealthyHostCount"
  namespace           = "AWS/NetworkELB"
  period              = "${var.alb_unhealthy_host_period}"
  statistic           = "Maximum"
  threshold           = "${var.alb_unhealthy_host_count_threshold}"
  alarm_description   = "ALB unhealthy Host count Alarm for test targetgroup"
  dimensions = {
      LoadBalancer = "${var.alb_suffix}"
      TargetGroup = "${var.targetgroup_test_suffix}"
  }

  #alarm_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]
  ok_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]
  insufficient_data_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]

  # insufficient_data_actions = []
}