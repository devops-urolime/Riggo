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
  log_statements = "${var.log_statements}"
  log_min_duration_statement = "${var.log_min_duration_statement}"
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
  cidr_blocks     = "${var.cidr_block}"
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

module "api-gateway" {
  source = "../api-gateway"
  # env    = "${terraform.workspace}"
  # name = "${var.name}"
  # cloudwatchlogs-globalarn = "${module.iam.cloudwatch_APIGateway_Global_logs}"
  # authorizerArn = "${module.iam.lambda_invoke}"
  # authorize_uri         = "${module.lambda.authorize_uri}"    
  elb_endpoint = "${module.ecs-cluster.elb_endpoint}"
  rest_api_name = "${var.rest_api_name}"
  authorizer_auth0_audience = "${var.authorizer_auth0_audience}"
  authorizer_auth0_jwks_uri = "${var.authorizer_auth0_jwks_uri}"
  authorizer_auth0_token_issuer = "${var.authorizer_auth0_token_issuer}"
  elb_arn                       =    "${module.ecs-cluster.elb_arn}"
  custom_name_cert = "${var.wildcard_hostedzone_cert_arn}"
  hosted_zone_name = "${var.hosted_zone_name}"
  basepath_apigateway = "${var.basepath_apigateway}"
} 


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
  cidr_blocks                       = "${var.cidr_block}"
  keyname                           = "${var.keyname}"
  bastion_SG                        = "${module.bastion.bastion_host_security_group}"
  deployment_minimum_healh_percent  = "${var.deployment_minimum_healh_percent}"
  deployment_maximum_healh_percent  = "${var.deployment_maximum_healh_percent}"
  container_port                    = "${var.container_port}"
  health_checkpath                  = "${var.health_checkpath}"
  # TD_Cpu_limit                      = "${var.TD_Cpu_limit}"
  TD_mem_hard_limit                 = "${var.TD_mem_hard_limit}"
  TD_mem_soft_limit                 = "${var.TD_mem_soft_limit}"
  workspace                         = "${terraform.workspace}"
  cloudwatch_log                    = "${module.CloudWatch.cloudwatch-log-GroupName}"
  service_discovery_arn             = "${module.Route53.service_discovery_arn}"
  health_check_grace_period_seconds = "${var.health_check_grace_period_seconds}"
  ec2_health_check_period = "${var.ec2_health_check_period}"
  spring_profile_key            =    "${var.spring_profile_env}"
  taskdef_path                  =    "${var.taskdef_path}"
  enable_proxy_protocol         =    "${var.enable_proxy_protocol}"
  target_deregistration_delay   =    "${var.target_deregistration_delay}"
  # alb_acm_cert                  =    "${var.wildcard_hostedzone_cert_arn}"
}


module "CloudWatch" {
  source = "../cloudwatch"
  cpu_utilization_high_threshold = "${var.cpu_utilization_high_threshold}"
  cluster_name = "${module.ecs-cluster.ecs_cluster_name}"
  service_name = "${module.ecs-cluster.ecs_service_name}"
  cpu_utilization_high_evaluation_periods = "${var.cpu_utilization_high_evaluation_periods}"
  cpu_utilization_high_period = "${var.cpu_utilization_high_period}"
  memory_utilization_high_threshold = "${var.memory_utilization_high_threshold}"
  memory_utilization_cluster_high_threshold = "${var.memory_utilization_cluster_high_threshold}"
  memory_utilization_high_evaluation_periods = "${var.memory_utilization_high_evaluation_periods}"
  memory_utilization_high_period = "${var.memory_utilization_high_period}"
  rds_cpu_utilization_high_threshold = "${var.rds_cpu_utilization_high_threshold}"
  rds_cpu_utilization_high_evaluation_periods = "${var.rds_cpu_utilization_high_evaluation_periods}"
  rds_cpu_utilization_high_period = "${var.rds_cpu_utilization_high_period}"
  rds_db_instance_id = "${module.RDS.db_instance_id}"
  rds_freeable_memory_low_evaluation_periods = "${var.rds_freeable_memory_low_evaluation_periods}"
  rds_freeable_low_memory_period = "${var.rds_freeable_low_memory_period}"
  rds_freeable_memory_low_threshold = "${var.rds_freeable_memory_low_threshold}"
  rds_free_low_storage_space_evaluation_periods = "${var.rds_free_low_storage_space_evaluation_periods}"
  rds_free_low_storage_space_period = "${var.rds_free_low_storage_space_period}"
  rds_free_storage_space_threshold = "${var.rds_free_storage_space_threshold}"
  redis_cpu_utilization_high_threshold = "${var.redis_cpu_utilization_high_threshold}"
  redis_cpu_utilization_evaluation_period = "${var.redis_cpu_utilization_evaluation_period}"
  redis_cpu_utilization_period = "${var.redis_cpu_utilization_period}"
  elasticache_cluster_name_id = "${module.elasticache.elasticache_redis_cluster_id}"
  elasticache_node_id = "${module.elasticache.elasticache_redis_node_id}"
  elasticache_nodes = "${module.elasticache.elasticache_nodes_count}"
  redis_freeable_memory_evaluation_period = "${var.redis_freeable_memory_evaluation_period}"
  redis_freeable_memory_period = "${var.redis_freeable_memory_period}"
  redis_freeable_memory_low_threshold = "${var.redis_freeable_memory_low_threshold}"
  alb_unhealthy_host_evaluation_period = "${var.alb_unhealthy_host_evaluation_period}"
  alb_unhealthy_host_period = "${var.alb_unhealthy_host_period}"
  alb_unhealthy_host_count_threshold = "${var.alb_unhealthy_host_count_threshold}"
  alb_suffix = "${module.ecs-cluster.ApplicationELB_Id}"
  targetgroup_prod_suffix = "${module.ecs-cluster.production_targetgroup_suffix}"
  targetgroup_test_suffix =  "${module.ecs-cluster.testing_targetgroup_suffix}"
  alarms_email = "${var.alarms_email}"
  protocol = "${var.protocol}"
  pipeline_jobs =["${module.codepipeline.codepipeline_apiservice_name}","${module.codepipeline.codepipeline_clientapp_name}"]
  codebuild_projectnames = ["${lookup(local.codebuild_project_name, "apiservice")}","${lookup(local.codebuild_project_name, "clientapp")}"]
}

module "Route53" {
  source = "../Route53"

  cluster_name          = "${module.ecs-cluster.ecs_cluster_name}"
  vpc_id                = "${module.VPC.vpc_id}"
  service_discovery_ttl = "${var.service_discovery_ttl}"
  hosted_zone_name      = "${var.hosted_zone_name}"
  cloudfront_alias_name = "${var.cname_alias}"
  cloudfront_domain_name = "${module.cloudfront.cloudfront_domain_name}"
  cloudfront_zone_id = "${module.cloudfront.cloudfront_hosted_zone_id}"
  # elb_dns_name          = "${module.ecs-cluster.elb_endpoint}"
  # elb_dns_zone_id       = "${module.ecs-cluster.elb_hosted_zone_id}"
  api_custom_domain_name = "${module.api-gateway.custom_domain_name}"
  api_regional_domain_name = "${module.api-gateway.regional_domain_name}"
  api_regional_zone_id = "${module.api-gateway.regional_zone_id}"
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

module "codebuild" {
  source = "../CI-CD/CodeBuild"
  # environment_variables = "${var.environment_variables}"
  # private_subnet1_arn = "${var.private_subnet1_arn}"
  # private_subnet2_arn = "${var.private_subnet2_arn}"
  # private_subnet1_id
  clientapp-loggroup = "${module.CloudWatch.clientapp_loggroup}"
  apiservice-loggroup = "${module.CloudWatch.apiservice_loggroup}"
  vpc_id = "${module.VPC.vpc_id}"
  client-app-s3-bucket = "${module.cloudfront.s3-bucket}"
  compute_type = "${var.compute_type}"
  codebuild_image = "${var.codebuild_image}"
  cloudfront_distribution_id = "${module.cloudfront.cloudfront_distribution_id}"
  codepipeline_artifact_bucket = "${module.codepipeline.codepipeline_s3_bucket}"
  buildspec_path = "${var.buildspec_path}"
  private_subnet_ids = ["${module.VPC.private_subnet_id1}","${module.VPC.private_subnet_id2}"]
  artifactory_directory = "${local.artifact}"
  task_definition = "${module.ecs-cluster.task_definition_family}"
  # build_output_deploy_dir = "${local.build_output_deploy_dir}"
  # build_output_image_dir = "${local.build_output_image_dir}"
  # build_output_image_dir = "${lookup(local.apiservices, "build_output_image_dir")}"
  # build_output_deploy_dir = "${lookup(local.apiservices, "build_output_deploy_dir")}"
  container_name = "${module.ecs-cluster.container_name}"
  taskdef_path = "${var.taskdef_path}"
  ECS_securitygroup_ids = ["${module.ecs-cluster.ECS_instance_SG}"]
  apigateway_invoke_url = "${module.api-gateway.invoke_url}"
  
}


module "codepipeline" {
  source = "../CI-CD/codepipeline"
  github_repository_name = "${var.github_repository_name}"
  github_branch_name = "${var.github_branch_name}"
  # codebuild_project_name = "${module.codebuild.codebuild_project_name_apiservice}"
  codedeploy_application_name = "${module.codedeploy.codedeploy_application_name}"
  codebuild_project_name = "${local.codebuild_project_name}"
  codedeploy_application_deploymentgroupname = "${module.codedeploy.codedeploy_application_deploymentgroupname}"
  # source_output_dir = "${local.source_output_dir}"
  # build_output_image_dir = "${local.build_output_image_dir}"
  # build_output_deploy_dir = "${local.build_output_deploy_dir}"
  # placeholder_text = "${local.placeholder_text}"
  # source_output_dir = "${lookup(local.apiservices, "source_output_dir")}"
  # build_output_image_dir = "${lookup(local.apiservices, "build_output_image_dir")}"
  # build_output_deploy_dir = "${lookup(local.apiservices, "build_output_deploy_dir")}"
  # placeholder_text = "${lookup(local.apiservices, "placeholder_text")}"
   artifactory_directory = "${local.artifact}"
   github_organization_name = "${var.github_organization_name}"
  
  

}


module "codedeploy" {
  source = "../CI-CD/codedeploy"
  enable_auto_rollback = "${var.enable_auto_rollback}"
  rollback_events = "${var.rollback_events}"
  action_on_timeout = "${var.action_on_timeout}"
  action_on_blue_tasks = "${var.action_on_blue_tasks}"
  bluetask_termination_wait_minutes = "${var.bluetask_termination_wait_minutes}"
  cluster_name = "${module.ecs-cluster.ecs_cluster_name}"
  service_name = "${module.ecs-cluster.ecs_service_name}"
  prod_listener = "${module.ecs-cluster.prod_listener}"
  test_listener = "${module.ecs-cluster.test_listener}"
  target_group_name_blue = "${module.ecs-cluster.target_group_name_blue}"
  target_group_name_green = "${module.ecs-cluster.target_group_name_green}"
  sns_trigger_alarm_arn = "${module.CloudWatch.arn_alarm_sns}"
  

}

module "ses" {
  source = "../ses"
  ses_email_address = "${var.SES_email_address}"
  ses_smtp_user_domain = "${var.SES_smtp_user_domain}"
  
}


# locals {
#   source_output_dir = "${var.source_output_artifact_dir}"
#   build_output_image_dir = "${var.build_output_image_artifact_dir}"
#   build_output_deploy_dir = "${var.build_output_deploy_artifact_dir}"

#   placeholder_text = "${var.image_placeholder_text}"
# }


# locals {
  
#   source_output_dir = "${var.source_output_artifact_dir}"
#   build_output_image_dir = "${var.build_output_image_artifact_dir}"
#   build_output_deploy_dir = "${var.build_output_deploy_artifact_dir}"

#   placeholder_text = "${var.image_placeholder_text}"
# }

 locals {
  
  # clientapp = {
  # source_output_dir = "${var.source_output_artifact_dir}"
  # build_output_image_dir = "${var.build_output_image_artifact_dir}"
  # build_output_deploy_dir = "${var.build_output_deploy_artifact_dir}"
  # placeholder_text = "${var.image_placeholder_text}"
  # }

  artifact = {
    source_output_dir = "${lookup(var.artifact, "source_output_artifact_dir")}"
  build_output_image_dir = "${lookup(var.artifact, "build_output_image_artifact_dir")}"
  build_output_deploy_dir = "${lookup(var.artifact, "build_output_deploy_artifact_dir")}"
  placeholder_text = "${lookup(var.artifact, "image_placeholder_text")}"

  }

codebuild_project_name = {
  clientapp = "${module.codebuild.codebuild_project_name_clientapp}"
  apiservice = "${module.codebuild.codebuild_project_name_apiservice}"
}

}


 