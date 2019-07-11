resource "aws_security_group_rule" "ingress_ecs_instance" {
  description = "Incoming traffic to ECS instances"
  type        = "ingress"
  from_port   = "${var.container_port}"
  to_port     = "${var.container_port}"
  protocol    = "TCP"

  security_group_id        = "${aws_security_group.ecs-instance-SG.id}"
  source_security_group_id = "${aws_security_group.lb_securitygroup.id}"
}
resource "aws_security_group_rule" "egress_ecs_instance" {
  description = "Outgoing traffic from ECS instances"
  type        = "egress"
  from_port   = "0"
  to_port     = "65535"
  protocol    = "-1"
  cidr_blocks = [
  "${var.cidr_blocks}"]

  security_group_id = "${aws_security_group.ecs-instance-SG.id}"
}

resource "aws_security_group_rule" "ingress_ecs_ssh_instance" {
  description = "Incoming ssh traffic to ECS instances"
  type        = "ingress"
  from_port   = "22"
  to_port     = "22"
  protocol    = "TCP"

  security_group_id        = "${aws_security_group.ecs-instance-SG.id}"
  source_security_group_id = "${var.bastion_SG}"
}

resource "aws_security_group" "ecs-instance-SG" {
  vpc_id = "${var.vpc_id}"

  tags = {
    Name = "SG-${terraform.workspace}-ECSinstance"
  }
}

data "aws_ami" "amazon-linux-ecs" {
  most_recent = true
  owners = [
  "amazon"]
  name_regex = "${var.name_ecs_ami}"

  filter {
    name = "architecture"
    values = [
    "x86_64"]
  }
}

data "template_file" "userdataECS" {
  template = "${file("${path.module}/userdata.sh")}"

  vars = {
    CLUSTER_NAME = "${aws_ecs_cluster.ecs-cluster.name}"

  }
}


resource "aws_launch_configuration" "ecs-launch-configuration" {
  name                 = "ECS-Riggo-${terraform.workspace}-launch-configuration"
  image_id             = "${data.aws_ami.amazon-linux-ecs.id}"
  instance_type        = "${var.ecs_instance_type}"
  iam_instance_profile = "${aws_iam_instance_profile.ecs-instance-profile.id}"
  security_groups = [
  "${aws_security_group.ecs-instance-SG.id}"]
  root_block_device {
    volume_type           = "${var.ecs_volume_type}"
    volume_size           = "${var.ecs_volume_size}"
    delete_on_termination = true
  }

  lifecycle {
    create_before_destroy = true

    ignore_changes = [ user_data ]
  }

  associate_public_ip_address = "false"

  key_name  = "${var.keyname}"
  user_data = "${data.template_file.userdataECS.rendered}"
  #
  # register the cluster name with ecs-agent which will in turn coord
  # with the AWS api about the cluster
  #
  # user_data = <<EOF
  #                                 #!/bin/bash
  #                                 echo ECS_CLUSTER="${aws_ecs_cluster.ecs-cluster.name}" >> /etc/ecs/ecs.config
  #                                 EOF

}