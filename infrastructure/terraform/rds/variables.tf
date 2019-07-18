variable "subnet1" {
}

variable "subnet2" {
}

variable "identifier" {}

variable "storage_type" {}

variable "allocated_storage" {
  # type = "map"
}

variable "db_engine" {}

variable "engine_version" {}

variable "instance_class" {
  # type = "map"
}

# variable "db_username" {}

# variable "db_password" {}

variable "sec_grp1_rds" {}
variable "sec_grp2_rds" {}
#variable "security_group_ids" {}

variable "vpc_id" {}
# variable "env" {}

# variable "iops" {}
