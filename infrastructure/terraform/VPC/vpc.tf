resource "aws_vpc" "VPC" {
  cidr_block           = "${var.vpc_cidr}"
  enable_dns_hostnames = true

  tags = {
    Name = "${terraform.workspace} VPC"
    env  = "${terraform.workspace}"
  }
}

# Define the public subnet
resource "aws_subnet" "public-subnet" {
  vpc_id     = "${aws_vpc.VPC.id}"
  cidr_block = "${var.public_subnet_cidr}"

  availability_zone = "${data.aws_availability_zones.available.names[0]}"

  tags = {
    Name = "${terraform.workspace} Public Subnet"
    env  = "${terraform.workspace}"
  }
}

resource "aws_subnet" "public-subnet2" {
  vpc_id     = "${aws_vpc.VPC.id}"
  cidr_block = "${var.az2_public_subnet_cidr}"

  availability_zone = "${data.aws_availability_zones.available.names[1]}"

  tags = {
    Name = "${terraform.workspace} Public Subnet2"
    env  = "${terraform.workspace}"
  }
}

# Define the private subnet
resource "aws_subnet" "private-subnet" {
  vpc_id            = "${aws_vpc.VPC.id}"
  cidr_block        = "${var.private_subnet_cidr}"
  availability_zone = "${data.aws_availability_zones.available.names[0]}"

  tags = {
    Name = "${terraform.workspace} Private Subnet"
    env  = "${terraform.workspace}"
  }
}

resource "aws_subnet" "private-subnet2" {
  vpc_id            = "${aws_vpc.VPC.id}"
  cidr_block        = "${var.az2_private_subnet_cidr}"
  availability_zone = "${data.aws_availability_zones.available.names[1]}"

  tags = {
    Name = "${terraform.workspace} Private Subnet2"
    env  = "${terraform.workspace}"
  }
}


