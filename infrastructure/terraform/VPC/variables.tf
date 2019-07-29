data "aws_region" "current" {}

variable "vpc_cidr" {
  # description = "CIDR for the VPC"
  # default = "10.0.0.0/16"
}

variable "public_subnet_cidr" {
  # description = "CIDR for the public subnet"
  # #type = list
  # default = "10.0.1.0/24"
}

variable "private_subnet_cidr" {
  # description = "CIDR for the private subnet"
  # #type = list
  # default = "10.0.2.0/24"
}

variable "az2_public_subnet_cidr" {
  # description = "CIDR for the public subnet"
  # #type = list
  # default = "10.0.3.0/24"
}


variable "az2_private_subnet_cidr" {
  # description = "CIDR for the private subnet"
  # #type = list
  # default = "10.0.4.0/24"
}
# variable "az_zone1" {

# }
# variable "az_zone2" {

#}

data "aws_availability_zones" "available" {}

# variable "ami" {
#   # description = "AMI for EC2"
#   # default = "ami-4fffc834"
# }

# variable "key_path" {
#   # description = "SSH Public Key path"
#   # default = "/home/core/.ssh/id_rsa.pub"
# }

variable "cidr_block" {
  # description = "CIDR for the private subnet"
  # default = "0.0.0.0/0"
}

variable "from_port" {
  # description = "Incoming port"
  # default = "5432"
}

variable "to_port" {
  # description = "Incoming port"
  # default = "5432"
}

# variable "peering_vpc_id" {
  
# }

# variable "peer_security_group_id" {
  
# }



# variable "env" {
# }



