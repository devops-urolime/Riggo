output "elasticache_endpoint" {
  value = "${join(":", list(aws_elasticache_cluster.redis.cache_nodes.0.address, aws_elasticache_cluster.redis.cache_nodes.0.port))}"
}

output "elasticache_redis_cluster_id" {
  value = "${aws_elasticache_cluster.redis.cluster_id}"
}

output "elasticache_redis_node_id" {
  value = "${aws_elasticache_cluster.redis.cache_nodes.0.id}"
}

output "elasticache_nodes_count" {
  value = "${aws_elasticache_cluster.redis.num_cache_nodes}"
}
