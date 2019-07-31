resource "aws_security_group_rule" "lb_http_ingress" {
  type      = "ingress"
  from_port = "80"
  to_port   = "80"
  protocol  = "tcp"
  cidr_blocks = [
  "${var.cidr_blocks}"]

  security_group_id = "${aws_security_group.lb_securitygroup.id}"
}


resource "aws_security_group_rule" "lb_testlistnerPort_ingress" {
  type      = "ingress"
  from_port = "8080"
  to_port   = "8080"
  protocol  = "tcp"
  cidr_blocks = [
  "${var.cidr_blocks}"]

  security_group_id = "${aws_security_group.lb_securitygroup.id}"
}
resource "aws_security_group_rule" "lb_http_egress" {
  type      = "egress"
  from_port = "0"
  to_port   = "65535"
  protocol  = "-1"
  cidr_blocks = [
  "${var.cidr_blocks}"]

  security_group_id = "${aws_security_group.lb_securitygroup.id}"
}
resource "aws_security_group" "lb_securitygroup" {
  vpc_id = "${var.vpc_id}"
  name = "SG-${terraform.workspace}-ALB"
  
  tags = {
    Name = "SG-${terraform.workspace}-ALB"
  }
}

resource "aws_lb_target_group" "ecs-lb-targetgroup" {
  name = "Riggo-ECS-${terraform.workspace}"
  depends_on = [
  "aws_lb.ecs-lb"]

  health_check {
    healthy_threshold   = "5"
    interval            = "30"
    protocol            = "HTTP"
    matcher             = "200"
    timeout             = "3"
    path                = "${var.health_checkpath}"
    unhealthy_threshold = "2"
  }

  port     = "${var.container_port}"
  protocol = "HTTP"
  vpc_id   = "${var.vpc_id}"
  deregistration_delay = 30


# lifecycle {
#     create_before_destroy = true
#     ignore_changes = ["name"]
#   }

  tags = {
    env = "${terraform.workspace}"
  }
}


resource "aws_lb_target_group" "ecs-lb-targetgroup-testlistner" {
  name = "Riggo-ECS-${terraform.workspace}-testlistner"
  depends_on = [
  "aws_lb.ecs-lb"]

  health_check {
    healthy_threshold   = "5"
    interval            = "30"
    protocol            = "HTTP"
    matcher             = "200"
    timeout             = "3"
    path                = "${var.health_checkpath}"
    unhealthy_threshold = "2"
  }

  port     = "${var.container_port}"
  protocol = "HTTP"
  vpc_id   = "${var.vpc_id}"
  deregistration_delay = 30


# lifecycle {
#     create_before_destroy = true
#     ignore_changes = ["name"]
#   }

  tags = {
    env = "${terraform.workspace}"
  }
}

resource "aws_lb" "ecs-lb" {
  name               = "Riggo-ECS-${terraform.workspace}"
  load_balancer_type = "application"
  subnets = [
    "${var.public_subnet1}",
  "${var.public_subnet2}"]
  #security_groups = ["${concat(var.security_group_ids, list(aws_security_group.lb_securitygroup.id))}"]
  security_groups = [
  "${aws_security_group.lb_securitygroup.id}"]

  enable_deletion_protection = false

  tags = {
    env = "${terraform.workspace}"
  }


}

resource "aws_lb_listener" "front_end" {
  load_balancer_arn = "${aws_lb.ecs-lb.id}"
  port              = "80"
  protocol          = "HTTP"

  default_action {
    target_group_arn = "${aws_lb_target_group.ecs-lb-targetgroup.arn}"
    type             = "forward"
  }


lifecycle {
  ignore_changes = [default_action]
}
}

resource "aws_lb_listener" "testing-listener" {
  load_balancer_arn = "${aws_lb.ecs-lb.id}"
  port              = "8080"
  protocol          = "HTTP"

  default_action {
    target_group_arn = "${aws_lb_target_group.ecs-lb-targetgroup-testlistner.arn}"
    type             = "forward"

  
  }

lifecycle {
  ignore_changes = [default_action]
}
  

}
