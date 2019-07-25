variable "name_ecs_ami" {

}


variable "ecs_instance_type" {

}

variable "ecs_volume_type" {

}


variable "ecs_volume_size" {

}

variable "max_ecs_instance-size" {

}

variable "min_ecs_instance-size" {

}

variable "desired_ecs_instance-size" {

}

variable "vpc_id" {

}

variable "public_subnet1" {

}

variable "public_subnet2" {

}

variable "private_subnet1" {
}

variable "private_subnet2" {

}


variable "cidr_blocks" {

}

variable "keyname" {

}

variable "bastion_SG" {

}

variable "deployment_minimum_healh_percent" {

}

variable "deployment_maximum_healh_percent" {

}

variable "container_port" {
  type = number
}


variable "health_checkpath" {

}

# variable "TD_Cpu_limit" {

# }

variable "TD_mem_soft_limit" {

}

variable "workspace" {

}

variable "cloudwatch_log" {

}


variable "service_discovery_arn" {

}

variable "health_check_grace_period_seconds" {

}

variable "ec2_health_check_period" {
  
}

variable "spring_profile_key" {
  
}
