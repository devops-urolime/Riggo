resource "aws_security_group" "sec_elasticache" {
  name        = "${terraform.workspace}-ElasticCache-Redis"
  description = "Security group for redis elasticcache"
  vpc_id      = "${var.vpc_id}"

  ingress {
    from_port = "${var.from_redisport}"
    to_port   = "${var.to_redisport}"
    protocol  = "tcp"
    cidr_blocks = [
    "${var.vpc_cidr}"]
  }


  # ingress {
  #   from_port = "${var.from_redisport}"
  #   to_port   = "${var.to_redisport}"
  #   protocol  = "tcp"
  #   security_groups  = ["sg-0aeba5c68bf373446"]
  # }

  /*   ingress {
      from_port = 5432
      to_port = 5432
      protocol = "tcp"
      cidr_blocks = [
        "${var.vpc_cidr_block}"]
    } */

  egress {
    from_port = "${var.from_redisport}"
    to_port   = "${var.to_redisport}"
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
    Name = "${terraform.workspace} ElastiCache Redis"
    env  = "${terraform.workspace}"
  }
  lifecycle {
    ignore_changes = [name]
  }
}


# resource "aws_elasticache_security_group" "SG-elasticcache" {
#   name                 = "${terraform.workspace}-elasticache-security-group"
#   security_group_names = ["${aws_security_group.sec_elasticache.name}"]
# }