data "template_file" "container-definition" {
  template = "${file("${path.module}/containerdefinition.json")}"

  vars = {
    terraform-workspace = "${terraform.workspace}"
    container_port      = "${var.container_port}"
    container_name      = "Riggo-resource-svr-${terraform.workspace}"
    # TD_Cpu_limit        = "${var.TD_Cpu_limit}"
    TD_mem_hard_limit   = "${var.TD_mem_hard_limit}"
    cloudwatch_logname  = "${var.cloudwatch_log}"
    spring_profile_key  = "${var.spring_profile_key}"

  }
}

resource "aws_ecs_task_definition" "riggo-ecs-TD" {
  #task_role_arn = "${aws_iam_role.ecs-service-role.arn}"
  family                = "Riggo-ECS-${terraform.workspace}-TD"
  container_definitions = "${data.template_file.container-definition.rendered}"

  lifecycle {
    create_before_destroy = true
    ignore_changes = [container_definitions]
  }

  #depends_on = ["null_resource.efs_url_update"]
}




resource "null_resource" "task_definition_file" {
  count = "${! fileexists("${path.module}/../../../${var.taskdef_path}") ? 1 : 0}"
depends_on = ["aws_ecs_task_definition.riggo-ecs-TD"]

#  triggers = {
#     # policy_sha1 = "${sha1(file("../../../${var.taskdef_path}"))}"
#     files = "${! fileexists("${path.module}/../../../${var.taskdef_path}")}"
#   } 
  
  


  provisioner "local-exec" {
   
    command = "FILE=../../../${var.taskdef_path};aws ecs describe-task-definition --task-definition ${aws_ecs_task_definition.riggo-ecs-TD.family} |   jq '.taskDefinition' > $FILE"
  }
}
