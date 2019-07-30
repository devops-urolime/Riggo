# resource "aws_security_group_rule" "ingress_ecs_instance" {
#   description = "Incoming traffic to ECS instances"
#   type        = "ingress"
#   from_port   = "${var.container_port}"
#   to_port     = "${var.container_port}"
#   protocol    = "TCP"

#   security_group_id        = "${aws_security_group.ecs-instance-SG.id}"
#   source_security_group_id = "${aws_security_group.lb_securitygroup.id}"
# }

resource "aws_security_group_rule" "ingress_ecs_dynamicPort1" {
  description = "Incoming traffic to ECS instances"
  type        = "ingress"
  from_port   = "49153"
  to_port     = "65535"
  protocol    = "TCP"

  security_group_id        = "${aws_security_group.ecs-instance-SG.id}"
  source_security_group_id = "${aws_security_group.lb_securitygroup.id}"
}

resource "aws_security_group_rule" "ingress_ecs_dynamicPort2" {
  description = "Incoming traffic to ECS instances"
  type        = "ingress"
  from_port   = "32768"
  to_port     = "61000"
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
  name   = "SG-${terraform.workspace}-ECSinstance"

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
  name_prefix          = "ECS-Riggo-${terraform.workspace}"
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

    # ignore_changes = [ user_data,image_id ]
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

resource "aws_launch_template" "ECS-launch-template" {
name_prefix          = "ECS-Riggo-${terraform.workspace}"
image_id             = "${data.aws_ami.amazon-linux-ecs.id}"
instance_type        = "${var.ecs_instance_type}"
iam_instance_profile  {
                name = "${aws_iam_instance_profile.ecs-instance-profile.name}"
     }
vpc_security_group_ids = ["${aws_security_group.ecs-instance-SG.id}"]


# block_device_mappings {

#   device_name = "/dev/xvda1"
# ebs {
#   volume_type           = "${var.ecs_volume_type}"
#   volume_size           = "${var.ecs_volume_size}"
#   delete_on_termination = true

# }
# }

lifecycle {
    create_before_destroy = true

    # ignore_changes = [ user_data,image_id ]
  }

# network_interfaces {
#     associate_public_ip_address = false
#     security_groups = ["${aws_security_group.ecs-instance-SG.id}"]
#     delete_on_termination = true
#   }
key_name  = "${var.keyname}"
user_data = "${base64encode(data.template_file.userdataECS.rendered)}"
 tag_specifications {
    resource_type = "instance"

    tags = {
      Name = "ECS Instance - EC2ContainerService-${aws_ecs_cluster.ecs-cluster.name}"
    }
  }

   tag_specifications {
    resource_type = "volume"

    tags = {
      Name = "ECS EBS Volume - EC2ContainerService-${aws_ecs_cluster.ecs-cluster.name}"
    }
  }

}