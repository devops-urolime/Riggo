resource "aws_elasticache_subnet_group" "elastic_subnet" {
  name        = "${terraform.workspace}-subnet-group"
  description = "subnet group for elasticcache"
  subnet_ids = [
    "${var.subnet1}",
  "${var.subnet2}"]
}

