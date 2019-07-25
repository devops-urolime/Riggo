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
