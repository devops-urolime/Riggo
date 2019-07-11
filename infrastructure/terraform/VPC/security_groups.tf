resource "aws_security_group" "sec_grp_rds" {
  name        = "RDS"
  description = "RDS security group"
  vpc_id      = "${aws_vpc.VPC.id}"

  ingress {
    from_port = "${var.from_port}"
    to_port   = "${var.to_port}"
    protocol  = "tcp"
    cidr_blocks = [
    "${var.vpc_cidr}"]
  }

  /*   ingress {
      from_port = 5432
      to_port = 5432
      protocol = "tcp"
      cidr_blocks = [
        "${var.vpc_cidr_block}"]
    } */

  egress {
    from_port = "${var.from_port}"
    to_port   = "${var.to_port}"
    protocol  = "tcp"
    cidr_blocks = [
    "${var.cidr_block}"]
  }

  /*  egress {
     from_port = 5432
     to_port = 5432
     protocol = "tcp"
     cidr_blocks = [
       "${var.vpc_cidr_block}"]
   } */

  tags = {
    Name = "${terraform.workspace} RDS SG"
    env  = "${terraform.workspace}"
  }
}