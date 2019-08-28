  # variable "aws_profile" {
#   description = "Region for the VPC"
#   default = "default"
# }
#variables for RDS
variable "read_capacity" {
  # default = "20"
}

variable "write_capacity" {
  # default = "20"
}


#variable "security_group_ids" {
#  type        = "list"
#  default     = []
#  description = "The IDs of the security groups from which to allow `ingress` traffic to the DB instance"
#}


variable "identifier" {
  description = "Identifier for DB"
  # default     = "riggo-io"
}


variable "storage_type" {
  description = "Type of the storage ssd or magnetic"
  # default     = "gp2"
}

variable "allocated_storage" {
  description = "ammount of storage allocated in GB"
  # default     = "100"

}


variable "db_engine" {
  description = " DB engine"
  # default     = "postgres"
}


variable "engine_version" {
  description = "DB engine version"
  # default     = "11.2"
}

# variable "iops" {
#   default = "2000"
# }


variable "instance_class" {
  description = "machine type to be used"

  # default = "db.t2.micro"
}

# variable "db_username" {
#   # description = "db admin user"
#   # default     = "terradmin"
# }

# variable "db_password" {
#   # description = "password, provide through your tfvars file"
# }


# variable "public_subnet_cidr" {
#   description = "CIDR for the public subnet"
#   # 
#   type = "list"
# }

# variable "private_subnet_cidr" {
#   description = "CIDR for the private subnet"
#   # 
#   type = "list"
# }
# variable "az_zones" {
#   type = "list"
#    description = "AWS Region Availability Zones"
# }

#variables for bastion

variable "name_regex" {
  description = "amzn name regex"
  #default     = "^amzn2-ami-hvm.*-ebs"
  # default = "ami-068a5e9c87370be8b"
}

variable "public_ssh_port" {
  description = "SSH port"
  # default     = "22"
}

variable "instance_type" {
  description = "instance type of bastion"
  # default     = "t2.micro"
}

variable "keyname" {

  description = "Name of the key pairs"
  # default     = "RiggoKeyPair-qa"
}

# variable "cidr_block" {
#   description = "CIDR for the private subnet"
#   # default     = "0.0.0.0/0"
# }

#variables for s3-cloudfront
variable "client_app_name" {
  description = "app name for s3 bucket"
  # default     = "riggo-io-client-app"
}

variable "sse_algorithm" {

  description = "default s3 encryption key algorithm"
  # default     = "AES256"
}

# variable "s3_region" {

#   description = "region where s3 bucket is created"
#   default = "us-west-2"
#}

#variables for cloudfront

variable "price_class" {
  description = "cloudfront distribution hosted zones"
  # default     = "PriceClass_100"
}

variable "origin_path" {
  # default = ""
}

variable "cached_methods" {
  description = "list of cached methods"
  type        = "list"
  # default = [
  #   "GET",
  # "HEAD"]

}

variable "allowed_methods" {
  description = "list of allowed methods"
  type        = "list"
  # default = [
  #   "GET",
  # "HEAD"]

}

variable "cloudfront_root_object" {
  description = "Landing page of the cloudfront"
  # default = "index.html"
}


variable "cloudfront_acm_arn" {

  description = "ARN of the ACM certificate"
  # default = "arn:aws:acm:us-east-1:845657178663:certificate/155bca16-2b02-4a3e-bdec-f17da6f3b058"
  
  
}

variable "cloudfront_ssl_protocol_ver" {
  description = "version of SSL for ACM"
  # default = "TLSv1.1_2016"
  
}

variable "cname_alias" {
  description = "Domain name to use as cname in cloudfront"
  # default = "*.riggoqa.net"
}

variable "error_caching_min_ttl" {
  type = "map"
  # default = {
  #   403 = "300"
  #   404 = "300"
  # }
}

variable "error_code" {
  type = list(number)
  # default = [404,403]
  
}

variable "response_code" {
   type = "map"
  # default = {
  #   403 = "200"
  #   404 = "200"
  # }
  
}

variable "response_page_path" {
   type = "map"
  # default = {
  #   403 = "/index.html"
  #   404 = "/index.html"
  #}
}


#Variables for VPC

variable "vpc_cidr" {
  description = "CIDR for the VPC"
  # default     = "10.0.0.0/16"
}

variable "public_subnet_cidr" {
  description = "CIDR for the public subnet"
  #type = list
  # default = "10.0.1.0/24"
}

variable "private_subnet_cidr" {
  description = "CIDR for the private subnet"
  #type = list
  # default = "10.0.2.0/24"
}

variable "az2_public_subnet_cidr" {
  description = "CIDR for the public subnet"
  #type = list
  # default = "10.0.3.0/24"
}


variable "az2_private_subnet_cidr" {
  description = "CIDR for the private subnet"
  #type = list
  # default = "10.0.4.0/24"
}
# variable "az_zone1" {
#   #type = list
#   description = "AWS Region Availability Zones"
#   default     = "us-west-2a"
# }
# variable "az_zone2" {
#   #type = list
#   description = "AWS Region Availability Zones"
#   default     = "us-west-2b"
# }


# variable "ami" {
#   description = "AMI for EC2"
#   default = "ami-4fffc834"
# }

# variable "key_path" {
#   description = "SSH Public Key path"
#   default = "/home/core/.ssh/id_rsa.pub"
# }

variable "cidr_block" {
  description = "CIDR for the private subnet"
  # default     = "0.0.0.0/0"
}

variable "from_port" {
  description = "Incoming port"
  # default     = "5432"
}

variable "to_port" {
  description = "Incoming port"
  # default     = "5432"
}

# variable "peering_vpc_id" {
#   description = "Perring VPC id of Riggo-VPC"
#   default = "vpc-0bd2666e449685359"
  
# }

# variable "peer_security_group_id" {
#    description = "sG id of Riggo-VPC"
#    default = "sg-0e0c7b276070e72d8"
  
# }



# variable "env" {
#   description = "staging environment name"
#   default = "stage"
# }

#Variables for Elasticache.

variable "redis_node_type" {

  description = "the type of node for elasticache cluster"
  # default     = "cache.t2.micro"

}

variable "redis_num_cache_nodes" {

  description = "how many cache nodes for the cluster"
  # default     = "1"
}


variable "redis_parameter_group_name" {

  description = "parameter group name for the cluster"
  # default     = "default.redis5.0"
}


variable "redis_engine_version" {
  description = "engine version for the redis"
  # default     = "5.0.4"

}

variable "from_redisport" {
  description = "from port of redis in SG"
  # default     = "6379"

}

variable "to_redisport" {
  description = "to port of redis in SG"
  # default     = "6379"

}

#variables for ecs-cluster

variable "name_ecs_ami" {
  description = "ami search name for ecs"
  # default     = "amazon-ecs-optimized"

}


variable "ecs_instance_type" {

  description = "type of ecs instance"
  # default     = "t2.micro"

}


variable "ecs_volume_type" {
  description = "volume type for ecs instance"
  # default     = "standard"
}


variable "ecs_volume_size" {

  description = "volume size of the EBS"
  # default     = "20"

}

variable "max_ecs_instance-size" {

  description = "max number of instance in autoscaling"
  # default     = "2"
}

variable "min_ecs_instance-size" {

  description = "min number of instance in autoscaling"
  # default     = "1"
}

variable "desired_ecs_instance-size" {

  description = "desired number of instance in autoscaling"
  # default     = "1"
}


variable "deployment_minimum_healh_percent" {

  description = "Minimum health percentage of tasks in service deployment"
  # default     = "0"

}


variable "deployment_maximum_healh_percent" {
  description = "Maximum health percentage of tasks in service deployment"
  # default     = "100"

}


variable "container_port" {

  description = "container port defined to be port mapped to host network"
  type        = number
  # default     = 8088
}


variable "health_checkpath" {

  description = "$PATH to register a healthy instance"
  # default     = "/favicon.ico"
}

# variable "TD_Cpu_limit" {
#   description = "container definition CPu limit in cpu units"
#   default     = "512"
# }

variable "TD_mem_soft_limit" {

  description = "Soft limit memory set for the container in MB"
  # default     = "256"

}

variable "health_check_grace_period_seconds" {

  description = "health check grace period for each ECS instance"
  # default     = "300"

}

variable "ec2_health_check_period" {
  description = "ec2 health check period in austoscaling"
  # default = "30"
  
}

variable "spring_profile_env" {
  description = "java spring profile environment"
  # default = "qa"
  
}


#Variables for route53

variable "service_discovery_ttl" {

  description = "ttl  value for the record service"
  # default     = "60"

}

#variables for Api Gateway

# variable "name" {

#   default = "Riggo Platform"
# }

#variables for Lambda


variable "lambda_handler" {
  description = "handler used while running function"
  # default = "index.handler"
  
}


variable "lambda_env_audience" {

  description = "Application env variable audience"
  # default = "load-resource-api"
  
}

variable "lambda_env_auth0_JWKS_URI" {
  description = "Application env variable JWKS URI"
  # default = "https://riggo-staging.auth0.com/.well-known/jwks.json"
  
}

variable "lambda_env_auth0_TOKEN_ISSUER" {
  description = "Application env variable TOKEN ISSUER"
  # default = "https://riggo-staging.auth0.com/"
  
}


variable "lambda_runtime" {

  description = "The runtime platform in lambda"
  # default = "nodejs10.x"
  
}

variable "lambda_timeout" {
  description = "timeout for the lambda function"
  # default = "30"
  
}


#Variables for Cloudwatch Alarms ECS/CPU

variable "cpu_utilization_high_threshold" {
  description = "The maximum percentage of CPU utilization average."
  # default = "65"
}

variable "cpu_utilization_high_evaluation_periods" {
  description = "Number of periods to evaluate for the alarmn"
  # default = "1"
}

variable "cpu_utilization_high_period" {
  description = "Duration in seconds to evaluate for the alarm"
  # default = "120"
}


#Variables for Cloudwatch Alarms ECS/Memory

variable "memory_utilization_high_threshold" {
  description = "The maximum percentage of Memory utilization average."
}

variable "memory_utilization_high_evaluation_periods" {
  description = "Number of periods to evaluate for the alarmn"
}

variable "memory_utilization_high_period" {
  description = "Duration in seconds to evaluate for the alarm"
}

#Variables for Cloudwatch AWS/RDS

variable "rds_cpu_utilization_high_threshold" {
  description = "The maximum percentage of RDS CPU utilization average."
}

variable "rds_cpu_utilization_high_evaluation_periods" {
  description = "Number of periods to evaluate for the alarm"
}

variable "rds_cpu_utilization_high_period" {
  description = "Duration in seconds to evaluate for the alarm"
}

#Variables for Cloudwatch AWS/RDS-Memory
variable "rds_freeable_memory_low_evaluation_periods" {
  
  description = "Number of periods to evaluate for the alarm"
}

variable "rds_freeable_low_memory_period" {
  description = "Time to evaluate the RDS low memory threshold(In SECS)"
}

variable "rds_freeable_memory_low_threshold" {
  type        = "string"
  description = "Low memory limit in Bytes to alarm in instance"
}

#Variables for Cloudwatch AWS/RDS-diskspace
variable "rds_free_low_storage_space_evaluation_periods" {
  description = "Number of periods to evaluate for the alarm"
}

variable "rds_free_low_storage_space_period" {
  description = "Time to evaluate the RDS low disk space threshold(In SECS)"
}

variable "rds_free_storage_space_threshold" {
  type = "string"
  description = "Disk space value to alarm(in bytes)"
}

#Variables for AWS/ElastiCache-CPU

variable "redis_cpu_utilization_high_threshold" {
  
  description = "The maximum percentage of RDS CPU utilization average."
}

variable "redis_cpu_utilization_evaluation_period" {
  description = "Number of periods to evaluate for the alarm"
}

variable "redis_cpu_utilization_period" {
  description = "Duration in seconds to evaluate for the alarm"
}


#Variables for AWS/ElastiCache-Memory

variable "redis_freeable_memory_low_threshold" {
  type = "string"
  description = "Lowest freeable memory threshold to alarm"
}

variable "redis_freeable_memory_evaluation_period" {
  description = "Number of periods to evaluate for the alarm"
}

variable "redis_freeable_memory_period" {
  description = "Duration in seconds to evaluate for the alarm"
}


#Variables for AWS/ApplicationELB-Unhealthyhost

variable "alb_unhealthy_host_evaluation_period" {
  description = "Number of periods to evaluate for the alarm"
}

variable "alb_unhealthy_host_period" {
  description = "Duration in seconds to evaluate and trigger alarm"
}

variable "alb_unhealthy_host_count_threshold" {
  description = "Unhealthy host count in ALB to get alarm"
}

#variables for SNS

variable "alarms_email" {
  type = "list"
  description = "The email address to receive the notification"
}

variable "protocol" {
  default     = "email"
  description = "SNS Protocol to use. email or email-json"
  type        = "string"
}

#Variables for codebuild
variable "compute_type" {
  type = "string"
}

variable "codebuild_image" {
  description = "docker image for codebuild environment"
  type = "string"
}

variable "environment_variables" {
  
 description = "application related environment variables"
 type = "map"
}

# variable "apiservice_environment_variables" {
  
#  description = "application related environment variables"
#  type = "list"
# }

variable "buildspec_path" {
  
  description ="Path of the buildspec file from root of source"
  type = "map"
}




