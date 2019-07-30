module "VPC" {
  source = "../VPC"

  # env    = "${terraform.workspace}"
  vpc_cidr                = "${var.vpc_cidr}"
  public_subnet_cidr      = "${var.public_subnet_cidr}"
  private_subnet_cidr     = "${var.private_subnet_cidr}"
  az2_public_subnet_cidr  = "${var.az2_public_subnet_cidr}"
  az2_private_subnet_cidr = "${var.az2_private_subnet_cidr}"
  # az_zone1                = "${var.az_zone1}"
  # az_zone2                = "${var.az_zone2}"
  cidr_block = "${var.cidr_block}"
  from_port  = "${var.from_port}"
  to_port    = "${var.to_port}"
  # peering_vpc_id = "${var.peering_vpc_id}"
  # peer_security_group_id = "${var.peer_security_group_id}"
}

module "RDS" {
  source = "../rds"



  #   subnets = [

  #     "${module.VPC.Private_subnet_id}",
  # ]


  # env    = "${terraform.workspace}"
  sec_grp1_rds = "${module.VPC.sec_grp_rds}"
  sec_grp2_rds = "${module.bastion.private_instances_security_group}"
  #sec_grp_rds        = ["${var.security_group_ids}"]
  vpc_id            = "${module.VPC.vpc_id}"
  identifier        = "${var.identifier}"
  storage_type      = "${var.storage_type}"
  allocated_storage = "${var.allocated_storage}"
  db_engine         = "${var.db_engine}"
  engine_version    = "${var.engine_version}"
  instance_class    = "${var.instance_class}"
  # db_username       = "${var.db_username}"
  # db_password       = "${var.db_password}"
  # iops    = "${var.iops}"
  subnet1 = "${module.VPC.private_subnet_id1}"
  subnet2 = "${module.VPC.private_subnet_id2}"
}

module "bastion" {
  source = "../bastion"


  # env    = "${terraform.workspace}"
  name_regex      = "${var.name_regex}"
  vpc_id          = "${module.VPC.vpc_id}"
  public_ssh_port = "${var.public_ssh_port}"
  instance_type   = "${var.instance_type}"
  subnet_public   = "${module.VPC.public_subnet_id1}"
  keyname         = "${var.keyname}"
  cidr_blocks     = "${var.cidr_blocks}"
}


# module "s3" {
#   source = "../s3"

#   # env    = "${terraform.workspace}"
#   client_app_name = "${var.client_app_name}"
#   sse_algorithm   = "${var.sse_algorithm}"
#   #s3_region       = "${var.s3_region}"

# }

module "cloudfront" {

  source = "../cloudfront"

  # env    = "${terraform.workspace}"
  client_app_name = "${var.client_app_name}"
  # domain_name     = "${module.s3.domain_name}"
  price_class = "${var.price_class}"
  # domain_id       = "${module.s3.domain_id}"
  origin_path     = "${var.origin_path}"
  allowed_methods = "${var.allowed_methods}"
  cached_methods  = "${var.cached_methods}"
  sse_algorithm   = "${var.sse_algorithm}"
  cloudfront_root_object = "${var.cloudfront_root_object}"
  cloudfront_acm_arn = "${var.cloudfront_acm_arn}"
  cloudfront_ssl_protocol_ver = "${var.cloudfront_ssl_protocol_ver}"
  cname_alias                 = "${var.cname_alias}"
  error_caching_min_ttl        = "${var.error_caching_min_ttl}"
  error_code                   = "${var.error_code}"
  response_code                = "${var.response_code}"
  response_page_path           = "${var.response_page_path}"
}

# module "api-gateway" {
#   source = "../api-gateway"
#   # env    = "${terraform.workspace}"
#   # name = "${var.name}"
#   cloudwatchlogs-globalarn = "${module.iam.cloudwatch_APIGateway_Global_logs}"
#   authorizerArn = "${module.iam.lambda_invoke}"
#   authorize_uri         = "${module.lambda.authorize_uri}"    

# }


module "elasticache" {
  source = "../elasticache"

  # env                  = "${terraform.workspace}"
  vpc_id               = "${module.VPC.vpc_id}"
  subnet1              = "${module.VPC.private_subnet_id1}"
  subnet2              = "${module.VPC.private_subnet_id2}"
  node_type            = "${var.redis_node_type}"
  num_cache_nodes      = "${var.redis_num_cache_nodes}"
  parameter_group_name = "${var.redis_parameter_group_name}"
  engine_version       = "${var.redis_engine_version}"
  from_redisport       = "${var.from_redisport}"
  to_redisport         = "${var.to_redisport}"
  vpc_cidr             = "${var.vpc_cidr}"
  cidr_block           = "${var.cidr_block}"

}

# module "ecs" {
#   source = "../ecs-cluster"

#   # env                  = "${terraform.workspace}"

# }

module "ecs-cluster" {
  source                            = "../ecs-cluster"
  name_ecs_ami                      = "${var.name_ecs_ami}"
  ecs_instance_type                 = "${var.ecs_instance_type}"
  ecs_volume_type                   = "${var.ecs_volume_type}"
  ecs_volume_size                   = "${var.ecs_volume_size}"
  max_ecs_instance-size             = "${var.max_ecs_instance-size}"
  min_ecs_instance-size             = "${var.min_ecs_instance-size}"
  desired_ecs_instance-size         = "${var.desired_ecs_instance-size}"
  vpc_id                            = "${module.VPC.vpc_id}"
  public_subnet1                    = "${module.VPC.public_subnet_id1}"
  public_subnet2                    = "${module.VPC.public_subnet_id2}"
  private_subnet1                   = "${module.VPC.private_subnet_id1}"
  private_subnet2                   = "${module.VPC.private_subnet_id2}"
  cidr_blocks                       = "${var.cidr_blocks}"
  keyname                           = "${var.keyname}"
  bastion_SG                        = "${module.bastion.bastion_host_security_group}"
  deployment_minimum_healh_percent  = "${var.deployment_minimum_healh_percent}"
  deployment_maximum_healh_percent  = "${var.deployment_maximum_healh_percent}"
  container_port                    = "${var.container_port}"
  health_checkpath                  = "${var.health_checkpath}"
  # TD_Cpu_limit                      = "${var.TD_Cpu_limit}"
  TD_mem_soft_limit                 = "${var.TD_mem_soft_limit}"
  workspace                         = "${terraform.workspace}"
  cloudwatch_log                    = "${module.CloudWatch.cloudwatch-log-GroupName}"
  service_discovery_arn             = "${module.Route53.service_discovery_arn}"
  health_check_grace_period_seconds = "${var.health_check_grace_period_seconds}"
  ec2_health_check_period = "${var.ec2_health_check_period}"
  spring_profile_key            =    "${var.spring_profile_env}"
}


module "CloudWatch" {
  source = "../cloudwatch"


}

module "Route53" {
  source = "../Route53"

  cluster_name          = "${module.ecs-cluster.ecs_cluster_name}"
  vpc_id                = "${module.VPC.vpc_id}"
  service_discovery_ttl = "${var.service_discovery_ttl}"

}

# module "iam" {
#   source = "../iam"

  
# }

# module "lambda" {
#   source = "../lambda"

#   lambda_invoke_arn = "${module.iam.lambda_invoke}"
#   handler           = "${var.lambda_handler}"
#   env_audience      = "${var.lambda_env_audience}"
#   env_auth0_JWKS_URI = "${var.lambda_env_auth0_JWKS_URI}"
#   env_auth0_TOKEN_ISSUER = "${var.lambda_env_auth0_TOKEN_ISSUER}"
#   runtime_platform = "${var.lambda_runtime}"
#   timeout          = "${var.lambda_timeout}"
  
# }