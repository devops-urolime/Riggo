output "bastion_host_security_group" {
  value = "${aws_security_group.bastion_host_security_group.id}"
}

output "private_instances_security_group" {
  value = "${aws_security_group.private_instances_security_group.id}"
}

