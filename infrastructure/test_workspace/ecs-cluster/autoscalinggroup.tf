# need an ASG so we can easily add more ecs host nodes as necessary
#
resource "aws_autoscaling_group" "ecs-autoscaling-group" {
  name             = "${aws_launch_template.ECS-launch-template.name}-ASG"
  max_size         = "${var.max_ecs_instance-size}"
  min_size         = "${var.min_ecs_instance-size}"
  desired_capacity = "${var.desired_ecs_instance-size}"
  health_check_grace_period = 30

  # vpc_zone_identifier = ["subnet-41395d29"]
  vpc_zone_identifier = [
    "${var.private_subnet1}",
  "${var.private_subnet2}"]
  # launch_configuration = "${aws_launch_configuration.ecs-launch-configuration.name}"
  launch_template {
     id = "${aws_launch_template.ECS-launch-template.id}"
     version ="$Latest"
  }
  health_check_type    = "EC2"

   lifecycle {
  create_before_destroy = true
}

  tag {
    key                 = "env"
    value               = "${terraform.workspace}"
    propagate_at_launch = true
  }

  tag {
    key                 = "Name"
    value               = "ECS Instance - EC2ContainerService-${aws_ecs_cluster.ecs-cluster.name}"
    propagate_at_launch = true
  }

  tag {
    key                 = "Managed_By"
    value               = "Terraform"
    propagate_at_launch = true
  }

  
}


resource "aws_autoscaling_attachment" "ecs-targetgroup-attachment" {
  autoscaling_group_name = "${aws_autoscaling_group.ecs-autoscaling-group.id}"
  alb_target_group_arn   = "${aws_lb_target_group.ecs-lb-targetgroup.arn}"
  depends_on = [
  "aws_autoscaling_group.ecs-autoscaling-group"]
}

# resource "aws_autoscaling_attachment" "ecs-targetgrouplistener-attachment" {
#   autoscaling_group_name = "${aws_autoscaling_group.ecs-autoscaling-group.id}"
#   alb_target_group_arn   = "${aws_lb_target_group.ecs-lb-targetgroup-testlistner.arn}"
#   depends_on = [
#   "aws_autoscaling_group.ecs-autoscaling-group"]
# }