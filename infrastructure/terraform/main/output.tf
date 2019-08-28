output "subnet1" {
  value = [
    "${module.VPC.private_subnet_id1}"]
}
output "subnet2" {
  value = [
    "${module.VPC.private_subnet_id2}"]
}

output "sec_grp_rds" {
  value = "${module.VPC.sec_grp_rds}"
}

# output "subnet_public" {
#  value = "${module.VPC.Public_subnet_id}"
# }

# output "cidr_blocks" {

#   value = "${module.VPC.cidr_blocks}"
# }

output "vpc_id" {
  value = "${module.VPC.vpc_id}"
}

# output "domain_name" {
#   value = "${module.s3.domain_name}"
# }

output "private_instances_security_group" {
  value = "${module.bastion.private_instances_security_group}"
}

# output "username" {
#   value = "${module.external.username}"
# }

# output "password" {
#   value = "${module.external.password}"
# }

output "Public_subnet_id1" {

  value = "${module.VPC.public_subnet_id1}"
  description = "ID of Public_subnet"
}

output "Public_subnet_id2" {
  value = "${module.VPC.public_subnet_id2}"
}


#output of ECS cluster

output "ECS_Cluster_name" {
  value = "${module.ecs-cluster.ecs_cluster_name}"
}

output "ECS_Service_name" {
  value = "${module.ecs-cluster.ecs_service_name}"
}

output "Task_definition" {
  value = "${module.ecs-cluster.task_definition}"
}

output "container_definition" {
  value = "${module.ecs-cluster.container_definition}"
}


#output for RDS

output "rds_instance_endpoint" {
  value = "${module.RDS.db_instance_endpoint}"
}

output "db_instance_username" {
  value = "${module.RDS.db_instance_username}"
}

output "db_instance_password" {
  value = "${module.RDS.db_instance_password}"
}

output "db_instance_port" {
  value = "${module.RDS.db_instance_port}"
}

# output "db_instance_db" {
#   value = "${module.RDS.db_instance_db}"
# }


#output from bastion

output "bastion_instance_id" {
  value = "${module.bastion.instance_id}"
}

output "bastion_public_ip_address" {
  value = "${module.bastion.public_ip}"
}


#output from cloudfront and s3.

output "s3-bucket-for-cloudfront" {
  value = "${module.cloudfront.s3-bucket}"
}

output "cloudfront_distribution" {
  value = "${module.cloudfront.cloudfront_domain_name}"
}

output "cloudfront_distribution_id" {
  value = "${module.cloudfront.cloudfront_distribution_id}"
}

output "s3_bucket_region_for_cloudfront" {
  value = "${module.cloudfront.s3_bucket_region}"
}


#output from elasticache

output "elasticache_endpoint" {
  value = "${module.elasticache.elasticache_endpoint}"
}




