data "template_file" "container-definition" {
  template = "${file("${path.module}/containerdefinition.json")}"

  vars = {
    terraform-workspace = "${terraform.workspace}"
    container_port      = "${var.container_port}"
    TD_Cpu_limit        = "${var.TD_Cpu_limit}"
    TD_mem_hard_limit   = "${var.TD_mem_hard_limit}"
    cloudwatch_logname  = "${var.cloudwatch_log}"

  }
}

resource "aws_ecs_task_definition" "riggo-ecs-TD" {
  #task_role_arn = "${aws_iam_role.ecs-service-role.arn}"
  family                = "Riggo-ECS-${terraform.workspace}-TD"
  container_definitions = "${data.template_file.container-definition.rendered}"

  lifecycle {
    create_before_destroy = true
  }

  #depends_on = ["null_resource.efs_url_update"]
}