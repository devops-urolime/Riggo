# Define the Nat gateway
resource "aws_nat_gateway" "ngw" {
  #vpc_id = "${aws_vpc.staging-vpc.id}"
  subnet_id     = "${aws_subnet.public-subnet.id}"
  allocation_id = "${aws_eip.nat-eip.id}"

  tags = {
    Name = "${terraform.workspace} VPC NGW"
    env  = "${terraform.workspace}"
  }
}

#Define the route table
resource "aws_route_table" "private-rt" {
  vpc_id = "${aws_vpc.VPC.id}"


  route {
    cidr_block     = "${var.cidr_block}"
    nat_gateway_id = "${aws_nat_gateway.ngw.id}"
  }

  tags = {
    Name = "${terraform.workspace} Private Subnet RT"
    env  = "${terraform.workspace}"
  }
}

# Assign the route table to the private Subnet
resource "aws_route_table_association" "private-rt" {
  subnet_id      = "${aws_subnet.private-subnet.id}"
  route_table_id = "${aws_route_table.private-rt.id}"
}

resource "aws_route_table_association" "private-rt2" {
  subnet_id      = "${aws_subnet.private-subnet2.id}"
  route_table_id = "${aws_route_table.private-rt.id}"
}