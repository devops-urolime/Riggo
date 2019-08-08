
#Application variables
spring_profile_env = "qa"


#General Variables
keyname = "RiggoKeyPair-qa"
cidr_block = "0.0.0.0/0"


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
cloudfront_acm_arn = "arn:aws:acm:us-east-1:845657178663:certificate/155bca16-2b02-4a3e-bdec-f17da6f3b058"
cloudfront_ssl_protocol_ver = "TLSv1.1_2016"
cname_alias = "*.riggoqa.net"
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

vpc_cidr = "10.0.0.0/16"
public_subnet_cidr = "10.0.1.0/24"
private_subnet_cidr = "10.0.2.0/24"
az2_public_subnet_cidr = "10.0.3.0/24"
az2_private_subnet_cidr = "10.0.4.0/24"
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
#Variables for route53
service_discovery_ttl = "60"

#variables for Lambda

lambda_handler = "index.handler"
lambda_env_audience = "load-resource-api"
lambda_env_auth0_JWKS_URI = "https://riggo-staging.auth0.com/.well-known/jwks.json"
lambda_env_auth0_TOKEN_ISSUER = "https://riggo-staging.auth0.com/"
lambda_runtime = "nodejs10.x"
lambda_timeout = "30"

#Variables for Cloudwatch Alarms

cpu_utilization_high_threshold = "65"
cpu_utilization_high_evaluation_periods = "1"
cpu_utilization_high_period = "120"



