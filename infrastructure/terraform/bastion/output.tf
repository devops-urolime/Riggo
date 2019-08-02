output "bastion_host_security_group" {
  value = "${aws_security_group.bastion_host_security_group.id}"
}

output "private_instances_security_group" {
  value = "${aws_security_group.private_instances_security_group.id}"
}

output "instance_id" {
  value = "${aws_instance.bastion.id}"
}

output "public_ip" {
  value = "${aws_instance.bastion.public_ip}"
}


