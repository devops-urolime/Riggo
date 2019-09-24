#Application variables
spring_profile_env = "uat"

#General Variables
keyname = "RiggoKeyPair-test"
cidr_block = "0.0.0.0/0"
hosted_zone_name = "riggostaging.net"
wildcard_hostedzone_cert_arn = "arn:aws:acm:us-west-2:845657178663:certificate/563bf243-b716-46e6-b22c-5660b59603b4"

#variables for RDS
read_capacity  = "20"
write_capacity = "20"
identifier =  "riggo-io"
storage_type = "gp2"
allocated_storage = "100"
db_engine = "postgres"
engine_version = "11.2"
instance_class = "db.t2.micro"

#variables for bastion
name_regex = "ami-068a5e9c87370be8b"
public_ssh_port ="22"
instance_type = "t2.micro"


#variables for s3-cloudfront
client_app_name = "riggo-io-client-app"
sse_algorithm = "AES256"
price_class = "PriceClass_100"
origin_path = ""
cached_methods = ["GET","HEAD"]
allowed_methods = ["GET","HEAD"]
cloudfront_root_object = "index.html"
cloudfront_acm_arn = "arn:aws:acm:us-east-1:845657178663:certificate/c1f02af2-b09b-40ce-b010-4e0141f72c65"
cloudfront_ssl_protocol_ver = "TLSv1.1_2016"
cname_alias = "test.riggostaging.net"
error_caching_min_ttl = {
  403 = "300"
  404 = "300"
}
error_code = [404,403]

response_code = {
  403 = "200"
  404 = "200"
}

response_page_path = {
  403 = "/index.html"
  404 = "/index.html"
}

#Variables for VPC

vpc_cidr = "10.10.0.0/16"
public_subnet_cidr = "10.10.1.0/24"
private_subnet_cidr = "10.10.2.0/24"
az2_public_subnet_cidr = "10.10.3.0/24"
az2_private_subnet_cidr = "10.10.4.0/24"
from_port = "5432"
to_port = "5432"

#Variables for Elasticache.
redis_node_type = "cache.t2.micro"
redis_num_cache_nodes = "1"
redis_parameter_group_name = "default.redis5.0"
redis_engine_version = "5.0.4"
from_redisport = "6379"
to_redisport = "6379"

#variables for ecs-cluster

name_ecs_ami = "amazon-ecs-optimized"
ecs_instance_type = "t2.micro"
ecs_volume_type = "standard"
ecs_volume_size = "20"
max_ecs_instance-size = "2"
min_ecs_instance-size = "1"
desired_ecs_instance-size = "1"
deployment_minimum_healh_percent = "0"
deployment_maximum_healh_percent = "100"
container_port = 8088
health_checkpath = "/favicon.ico"
TD_mem_soft_limit = "256"
health_check_grace_period_seconds = "300"
ec2_health_check_period = "30"
taskdef_path = "CI-CD/test-taskdef.json"

#Variables for route53
service_discovery_ttl = "60"



#variables for Lambda

lambda_handler = "index.handler"
lambda_env_audience = "load-resource-api"
lambda_env_auth0_JWKS_URI = "https://riggo-staging.auth0.com/.well-known/jwks.json"
lambda_env_auth0_TOKEN_ISSUER = "https://riggo-staging.auth0.com/"
lambda_runtime = "nodejs10.x"
lambda_timeout = "30"

#Variables for Cloudwatch Alarm ECS/CPU

cpu_utilization_high_threshold = "65"
cpu_utilization_high_evaluation_periods = "1"
cpu_utilization_high_period = "120"


#Variables for Cloudwatch ECS/Memory

memory_utilization_high_threshold = "65"
memory_utilization_high_evaluation_periods = "1"
memory_utilization_high_period = "60"

#Variables for Cloudwatch AWS/RDS"

rds_cpu_utilization_high_threshold = "65"
rds_cpu_utilization_high_evaluation_periods = "1"
rds_cpu_utilization_high_period = "120"

#Variables for Cloudwatch AWS/RDS-Memory
 rds_freeable_memory_low_evaluation_periods = "1"
 rds_freeable_low_memory_period = "120"
 rds_freeable_memory_low_threshold = 256000000

 #Variables for Cloudwatch AWS/RDS-diskspace

 rds_free_low_storage_space_evaluation_periods = "1"
 rds_free_low_storage_space_period = "120"
 rds_free_storage_space_threshold = 37580963840

 #Variables for AWS/ElastiCache-CPU

redis_cpu_utilization_high_threshold = "65"
redis_cpu_utilization_evaluation_period = "1"
redis_cpu_utilization_period = "120"

 #Variables for AWS/ElastiCache-Memory

redis_freeable_memory_evaluation_period = "1"
redis_freeable_memory_period = "120"
redis_freeable_memory_low_threshold = 256000000

 #Variables for AWS/ApplicationELB

alb_unhealthy_host_evaluation_period = "1"
alb_unhealthy_host_period = "60"
alb_unhealthy_host_count_threshold = "1"

#variables for SNS

alarms_email = ["alerts@riggo.io", "muhasin.mohammed@urolime.com"]

#variables for codebuild

compute_type = "BUILD_GENERAL1_SMALL"
codebuild_image = "aws/codebuild/standard:2.0"
buildspec_path = {
  #PATH is from root of the respository.
  clientapp  = "CI-CD/test-clientapp-buildspec.yaml"
  apiservice = "CI-CD/test-apiservices-buildspec.yaml"
}

#Variables for  Codedeploy

enable_auto_rollback = "true"
rollback_events = ["DEPLOYMENT_FAILURE"]
action_on_timeout = "CONTINUE_DEPLOYMENT"
action_on_blue_tasks = "TERMINATE"
bluetask_termination_wait_minutes = 0
# taskdef_path = "CI-CD/taskdef.json"
#variables for codepipeline
artifact = {
source_output_artifact_dir = "Riggo-source"
build_output_image_artifact_dir = "Riggo-image"
build_output_deploy_artifact_dir = "Riggo-deploy"
image_placeholder_text = "<IMAGE1_NAME>"
  }
github_organization_name = "rig-go"
github_repository_name = "riggo"
github_branch_name = {
  clientapp = "test-client-app"
  apiservices = "test-api-services"
}

#variables for api gateway

rest_api_name = "Riggo Platform API"
basepath_apigateway = "v1"