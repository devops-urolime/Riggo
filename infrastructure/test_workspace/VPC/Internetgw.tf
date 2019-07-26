# Define the internet gateway
resource "aws_internet_gateway" "igw" {
  vpc_id = "${aws_vpc.VPC.id}"

  tags = {
    Name = "${terraform.workspace} VPC IGW"
    env  = "${terraform.workspace}"
  }
}

# Define the route table
resource "aws_route_table" "public-rt" {
  vpc_id = "${aws_vpc.VPC.id}"

  route {
    cidr_block = "${var.cidr_block}"
    gateway_id = "${aws_internet_gateway.igw.id}"
  }

  tags = {
    Name = "${terraform.workspace} Public Subnet RT"
    env  = "${terraform.workspace}"
  }
}

# Assign the route table to the public Subnet
resource "aws_route_table_association" "public-rt" {
  subnet_id      = "${aws_subnet.public-subnet.id}"
  route_table_id = "${aws_route_table.public-rt.id}"
}

resource "aws_route_table_association" "public-rt2" {
  subnet_id      = "${aws_subnet.public-subnet2.id}"
  route_table_id = "${aws_route_table.public-rt.id}"
}