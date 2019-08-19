
locals {
  rds_thresholds = {
    RDS-CPUUtilizationHighThreshold    = "${min(max(var.rds_cpu_utilization_high_threshold, 0), 100)}"
    RDS-FreeableMemoryThreshold = "${min(max(var.rds_freeable_memory_low_threshold, 0), 300000000)}"
    RDS-FreeStorageSpaceThreshold = "${min(max(var.rds_free_storage_space_threshold, 0), 42949672960)}"
  }
}


   
resource "aws_cloudwatch_metric_alarm" "rds_cpu" {
  alarm_name                = "${terraform.workspace}-RDS-cpu-high-utilization-alarm"
  comparison_operator       = "GreaterThanOrEqualToThreshold"
  evaluation_periods        = "${var.rds_cpu_utilization_high_evaluation_periods}"
  metric_name               = "CPUUtilization"
  namespace                 = "AWS/RDS"
  period                    = "${var.rds_cpu_utilization_high_period}"
  statistic                 = "Average"
  threshold                 = "${local.rds_thresholds["RDS-CPUUtilizationHighThreshold"]}"
  alarm_description         = "This metric monitors RDS instance cpu utilization"
  # insufficient_data_actions = []
  
  alarm_actions = ["${aws_cloudformation_stack.sns_topic.outputs["ARN"]}"]
  ok_actions = ["${aws_cloudformation_stack.sns_topic.outputs["ARN"]}"]
  insufficient_data_actions = ["${aws_cloudformation_stack.sns_topic.outputs["ARN"]}"]

  dimensions = {
    DBInstanceIdentifier = "${var.rds_db_instance_id}"
  }
}


resource "aws_cloudwatch_metric_alarm" "rds_freeable_low_memory" {
  alarm_name                = "${terraform.workspace}-RDS-freeable-memory-too-low-alarm"
  comparison_operator       = "LessThanThreshold"
  evaluation_periods        = "${var.rds_freeable_memory_low_evaluation_periods}"
  metric_name               = "FreeableMemory"
  namespace                 = "AWS/RDS"
  period                    = "${var.rds_freeable_low_memory_period}"
  statistic                 = "Average"
  threshold                 = "${local.rds_thresholds["RDS-FreeableMemoryThreshold"]}"
  alarm_description         = "This metric monitors low freeable memory in the instance"
  # insufficient_data_actions = []

  alarm_actions = ["${aws_cloudformation_stack.sns_topic.outputs["ARN"]}"]
  ok_actions = ["${aws_cloudformation_stack.sns_topic.outputs["ARN"]}"]
  insufficient_data_actions = ["${aws_cloudformation_stack.sns_topic.outputs["ARN"]}"]

  dimensions = {
    DBInstanceIdentifier = "${var.rds_db_instance_id}"
  }
}

resource "aws_cloudwatch_metric_alarm" "rds_free_storage_space_too_low" {
  alarm_name          = "${terraform.workspace}-RDS-free-storage-space-alarm"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = "${var.rds_free_low_storage_space_evaluation_periods}"
  metric_name         = "FreeStorageSpace"
  namespace           = "AWS/RDS"
  period              = "${var.rds_free_low_storage_space_period}"
  statistic           = "Average"
  threshold           = "${local.rds_thresholds["RDS-FreeStorageSpaceThreshold"]}"
  alarm_description   = "Average database free storage space over last 10 minutes too low"
  # alarm_actions       = ["${aws_sns_topic.default.arn}"]
  # ok_actions          = ["${aws_sns_topic.default.arn}"]
  alarm_actions = ["${aws_cloudformation_stack.sns_topic.outputs["ARN"]}"]
  ok_actions = ["${aws_cloudformation_stack.sns_topic.outputs["ARN"]}"]
  insufficient_data_actions = ["${aws_cloudformation_stack.sns_topic.outputs["ARN"]}"]

  dimensions = {
    DBInstanceIdentifier = "${var.rds_db_instance_id}"
  }
}