#Define elastic ip address for nat gateway
resource "aws_eip" "nat-eip" {

  vpc = true

  tags = {
    Name = "${terraform.workspace} NAT EIP"
    env  = "${terraform.workspace}"
  }
}