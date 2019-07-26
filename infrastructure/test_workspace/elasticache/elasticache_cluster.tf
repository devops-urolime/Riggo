resource "aws_elasticache_cluster" "redis" {
  cluster_id           = "${terraform.workspace}-redis-cluster"
  engine               = "redis"
  node_type            = "${var.node_type}"
  num_cache_nodes      = "${var.num_cache_nodes}"
  parameter_group_name = "${var.parameter_group_name}"
  engine_version       = "${var.engine_version}"
  port                 = 6379
  apply_immediately    = "true"

  subnet_group_name = "${aws_elasticache_subnet_group.elastic_subnet.name}"
  security_group_ids = [
  "${aws_security_group.sec_elasticache.id}"]
}
