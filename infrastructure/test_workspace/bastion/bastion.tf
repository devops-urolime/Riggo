# data "aws_ami" "amazon-linux-2" {
#   most_recent = true
#   owners      = ["amazon"]
#   name_regex  = "${var.name_regex}"

#   filter {
#     name   = "architecture"
#     values = ["x86_64"]
#   }
# }


resource "aws_security_group" "bastion_host_security_group" {
  description = "Enable SSH access to the bastion host from external via SSH port"
  name        = "${terraform.workspace}-bastion_host_security_group"
  vpc_id      = "${var.vpc_id}"

  tags = {
    Name = "${terraform.workspace}-Bastion-SG"
    env  = "${terraform.workspace}"

  }
}

resource "aws_security_group_rule" "ingress_bastion" {
  description = "Incoming traffic to bastion"
  type        = "ingress"
  from_port   = "${var.public_ssh_port}"
  to_port     = "${var.public_ssh_port}"
  protocol    = "TCP"
  cidr_blocks = [
  "${var.cidr_blocks}"]

  security_group_id = "${aws_security_group.bastion_host_security_group.id}"
}

resource "aws_security_group_rule" "egress_bastion" {
  description = "Outgoing traffic from bastion to instances"
  type        = "egress"
  from_port   = "0"
  to_port     = "65535"
  protocol    = "-1"
  cidr_blocks = [
  "${var.cidr_blocks}"]

  security_group_id = "${aws_security_group.bastion_host_security_group.id}"
}


resource "aws_security_group" "private_instances_security_group" {
  description = "Enable SSH access to the Private instances from the bastion via SSH port"
  name        = "${terraform.workspace}-priv-instances"
  vpc_id      = "${var.vpc_id}"

  tags = {
    Name = "${terraform.workspace}-Bastion2PrivNet"
    env  = "${terraform.workspace}"
  }
}
resource "aws_security_group_rule" "ingress_instances" {
  description = "Incoming traffic from bastion"
  type        = "ingress"
  from_port   = "${var.public_ssh_port}"
  to_port     = "${var.public_ssh_port}"
  protocol    = "TCP"

  source_security_group_id = "${aws_security_group.bastion_host_security_group.id}"

  security_group_id = "${aws_security_group.private_instances_security_group.id}"
}



resource "aws_security_group_rule" "egress_instances" {
  description = "Outgoing traffic to bastion"
  type        = "egress"
  from_port   = "0"
  to_port     = "65535"
  protocol    = "-1"
  cidr_blocks = [
  "${var.cidr_blocks}"]

  security_group_id = "${aws_security_group.private_instances_security_group.id}"
}

#data "template_file" "user_data" {
#  template = "${file("${path.module}/user_data.sh")}"
#}

resource "aws_instance" "bastion" {
  #ami                         = "${data.aws_ami.amazon-linux-2.id}"
  ami           = "${var.name_regex}"
  instance_type = "${var.instance_type}"
  #user_data                   = "${data.template_file.user_data.template}"
  vpc_security_group_ids = [
  "${aws_security_group.bastion_host_security_group.id}"]
  associate_public_ip_address = "true"
  subnet_id                   = "${var.subnet_public}"
  key_name                    = "${var.keyname}"


  tags = {
    Name = "${terraform.workspace}-Bastion-EC2"
    env  = "${terraform.workspace}"
  }
}