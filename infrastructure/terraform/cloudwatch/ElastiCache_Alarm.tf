
locals {
  elasticache_thresholds = {
        ElastiCache-CPUUtilizationHighThreshold = "${min(max(var.redis_cpu_utilization_high_threshold, 0), 100)}"
        ElastiCache-FreeableMemoryLowThreshold  = "${min(max(var.redis_freeable_memory_low_threshold, 0), 300000000)}"
  }
  elasticache_dimensions_map = {
    "CacheCluster" = {
      "CacheClusterId" = "${var.elasticache_cluster_name_id}"
    }

    "CacheNode" = {
      "CacheNodeId" = "${var.elasticache_node_id}"
    }
  }
}

resource "aws_cloudwatch_metric_alarm" "redis_cpu_utilization_alarm" {
  count = "${var.elasticache_nodes}"
  alarm_name          = "${terraform.workspace}-REDIS-cpu-utilization-alarm"
  alarm_description   = "Redis cluster CPU utilization"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "${var.redis_cpu_utilization_evaluation_period}"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ElastiCache"
  period              = "${var.redis_cpu_utilization_period}"
  statistic           = "Average"
  threshold = "${local.elasticache_thresholds["ElastiCache-CPUUtilizationHighThreshold"]}"
   dimensions ="${local.elasticache_dimensions_map[var.elasticache_cluster_name_id == "" ? "CacheNode" : "CacheCluster"]}"
  # dimensions  = {
    
  #   CacheClusterId = "${var.elasticache_cluster_name_id}"
  #   CacheNodeId = "${var.elasticache_node_id}"
  # }
  alarm_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]
  ok_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]
  insufficient_data_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]
  #alarm_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]
}

resource "aws_cloudwatch_metric_alarm" "redis_freeable_memory_alarm" {
  count = "${var.elasticache_nodes}"
  alarm_name          = "${terraform.workspace}-REDIS-freeable-memory-alarm"
  alarm_description   = "Redis cluster freeable memory alarm"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = "${var.redis_freeable_memory_evaluation_period}"
  metric_name         = "FreeableMemory"
  namespace           = "AWS/ElastiCache"
  period              = "${var.redis_freeable_memory_period}"
  statistic           = "Average"
  threshold = "${local.elasticache_thresholds["ElastiCache-FreeableMemoryLowThreshold"]}"
   dimensions ="${local.elasticache_dimensions_map[var.elasticache_cluster_name_id == "" ? "CacheNode" : "CacheCluster"]}"
  # dimensions  = {
    
  #   CacheClusterId = "${var.elasticache_cluster_name_id}"
  #   CacheNodeId = "${var.elasticache_node_id}"
  # }
  alarm_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]
  ok_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]
  insufficient_data_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]
  #alarm_actions = ["${aws_cloudformation_stack.sns_alert_topic.outputs["ARN"]}"]
}