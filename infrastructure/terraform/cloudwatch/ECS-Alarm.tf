
locals {
  thresholds = {
    CPUUtilizationHighThreshold    = "${min(max(var.cpu_utilization_high_threshold, 0), 100)}"
    MemoryUtilizationHighThreshold = "${min(max(var.memory_utilization_high_threshold, 0), 100)}"
  }
  dimensions_map = {
    "service" = {
      "ClusterName" = "${var.cluster_name}"
      "ServiceName" = "${var.service_name}"
    }

    "cluster" = {
      "ClusterName" = "${var.cluster_name}"
    }
  }
}
resource "aws_cloudwatch_metric_alarm" "ecs_cpu" {
  alarm_name                = "${terraform.workspace}-ECS-cpu-high-utilization-alarm"
  comparison_operator       = "GreaterThanOrEqualToThreshold"
  evaluation_periods        = "${var.cpu_utilization_high_evaluation_periods}"
  metric_name               = "CPUUtilization"
  namespace                 = "AWS/ECS"
  period                    = "${var.cpu_utilization_high_period}"
  statistic                 = "Average"
  threshold                 = "${local.thresholds["CPUUtilizationHighThreshold"]}"
  alarm_description         = "This metric monitors ECS cpu utilization"
  insufficient_data_actions = []

  dimensions = "${local.dimensions_map[var.service_name == "" ? "cluster" : "service"]}"
}


resource "aws_cloudwatch_metric_alarm" "ecs_memory" {
  alarm_name                = "${terraform.workspace}-ECS-memory-high-utilization-alarm"
  comparison_operator       = "GreaterThanOrEqualToThreshold"
  evaluation_periods        = "${var.memory_utilization_high_evaluation_periods}"
  metric_name               = "MemoryUtilization"
  namespace                 = "AWS/ECS"
  period                    = "${var.memory_utilization_high_period}"
  statistic                 = "Average"
  threshold                 = "${local.thresholds["MemoryUtilizationHighThreshold"]}"
  alarm_description         = "This metric monitors ECS memory utilization"
  insufficient_data_actions = []

  dimensions = "${local.dimensions_map[var.service_name == "" ? "cluster" : "service"]}"
}