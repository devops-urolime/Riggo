output "vpc_id" {

  value       = aws_vpc.VPC.id
  description = "ID of VPC"

}


output "igw_id" {

  value       = aws_internet_gateway.igw.id
  description = "ID of aws_internet_gateway"

}


output "public_subnet_id1" {

  value       = aws_subnet.public-subnet.id
  description = "ID of Public_subnet"
}

output "public_subnet_id2" {
  value = aws_subnet.public-subnet2.id
}


output "private_subnet_id1" {

  value       = aws_subnet.private-subnet.id
  description = "ID of Private_subnet"

}


output "private_subnet_id2" {

  value       = aws_subnet.private-subnet2.id
  description = "ID of Private_subnet"

}

output "public_route_table_id" {

  value       = aws_route_table.public-rt.id
  description = "ID of public_subnet_rt"

}


output "private_route_table_id" {

  value       = aws_route_table.private-rt.id
  description = "ID of private_subnet_rt"

}


output "sec_grp_rds" {
  value = "${aws_security_group.sec_grp_rds.id}"
}
