resource "aws_codedeploy_deployment_group" "deployment_group" {
  app_name               = "${aws_codedeploy_app.deployment_application.name}"
  deployment_config_name = "CodeDeployDefault.ECSAllAtOnce"
  deployment_group_name  = "${terraform.workspace}-apiservice-ECSdeploymentgroupname"
  service_role_arn       = "${aws_iam_role.deployment_role.arn}"

  auto_rollback_configuration {
    enabled = "${var.enable_auto_rollback}"
    events  = "${var.rollback_events}"
  }
  trigger_configuration {
    trigger_events = ["DeploymentStart","DeploymentSuccess","DeploymentFailure","DeploymentStop","DeploymentRollback"]
    trigger_name = "${terraform.workspace}-Codedeploy-trigger"
    trigger_target_arn = "${var.sns_trigger_alarm_arn}"
  }
  blue_green_deployment_config {
    deployment_ready_option {
      action_on_timeout = "${var.action_on_timeout}"
    }

    terminate_blue_instances_on_deployment_success {
      action                           = "${var.action_on_blue_tasks}"
      termination_wait_time_in_minutes = "${var.bluetask_termination_wait_minutes}"
    }
  }

  deployment_style {
    deployment_option = "WITH_TRAFFIC_CONTROL"
    deployment_type   = "BLUE_GREEN"
  }

  ecs_service {
    cluster_name = "${var.cluster_name}"
    service_name = "${var.service_name}"
  }

  load_balancer_info {
    target_group_pair_info {
      prod_traffic_route {
        listener_arns = ["${var.prod_listener}"]
      }

      target_group {
        name = "${var.target_group_name_blue}"
      }

      target_group {
        name = "${var.target_group_name_green}"
      }

      test_traffic_route {
          listener_arns = ["${var.test_listener}"]
      }
    }
  }
}