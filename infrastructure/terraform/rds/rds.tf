# data "aws_availability_zones" "available" {}
resource "aws_db_subnet_group" "db_sub_gr" {
  description = "terrafom db subnet group"
  name        = "${terraform.workspace} rds subnet group"
  subnet_ids = [
    "${var.subnet1}",
  "${var.subnet2}"]

  #  subnet_ids = [
  #    "${var.api_dev_int_subnet_ids}"]
  tags = {
    Name = "${terraform.workspace} RDS Postgress"
    env  = "${terraform.workspace}"
  }
   lifecycle {
    ignore_changes = [name]
  }
}

resource "aws_db_parameter_group" "postgress" {
  name_prefix   = "${terraform.workspace}-postgres11"
  description = "DB Parameter group for database ${var.identifier}-${terraform.workspace}"
  family = "postgres11"

  parameter {
    name  = "log_statement"
    value = "${var.log_statements}"
  }

  parameter {
    name  = "log_min_duration_statement"
    value = "${var.log_min_duration_statement}"
  }
}


#data "aws_vpc" "select" {
#  id = "${var.vpc_id}"
#}

#data "aws_security_group" "SG" {
#  vpc_id = "${data.aws_vpc.select.id}"
#}

# data "aws_s3_bucket_object" "credentials" {
#   bucket = "data-riggo"
#   key    =  "data.json"
# }


data "external" "rds_db_credentials" {
  program = [
    "aws",
    "s3",
    "cp",
    "s3://datastore-riggo/data.json",
  "-"]
}


# DB_username  = "${data.external.rds_db_credentials.result.username}"
# DB_password  = "${data.external.rds_db_credentials.result.password}"



# output "DB_username" {

#   value = "${data.external.rds_db_credentials.result.username}"

# }

# output "DB_password" {
#   value = "${data.external.rds_db_credentials.result.password}"
# }


# # variable "credentials" {
# #   value = "${data.aws_s3_bucket_object.credentials.body}"
# # }


# data "external" "get-data" {
#   program = ["echo", "${data.aws_s3_bucket_object.credentials.body}"]
# }

# output "username" {
#   value = ["${values(data.external.get-data.result.username)}"]
# }

# output "password" {
#   value = ["${values(data.external.get-data.result.password)}"]
# }


resource "aws_db_instance" "db" {
  identifier        = "${var.identifier}-${terraform.workspace}"
  storage_type      = "${var.storage_type}"
  allocated_storage = "${var.allocated_storage}"
  engine            = "${var.db_engine}"
  engine_version    = "${var.engine_version}"
  instance_class    = "${var.instance_class}"
  name              = "${terraform.workspace}_postgress"
  parameter_group_name = "${aws_db_parameter_group.postgress.name}"
  username          = "${data.external.rds_db_credentials.result.username}"
  password          = "${data.external.rds_db_credentials.result.password}"
  vpc_security_group_ids = [
    "${var.sec_grp1_rds}",
  "${var.sec_grp2_rds}"]
  #vpc_security_group_ids = ["${split(",", var.sec_grp_rds)}"]

  db_subnet_group_name = "${aws_db_subnet_group.db_sub_gr.id}"
  storage_encrypted    = false
  skip_final_snapshot  = true
  publicly_accessible  = false
  multi_az             = false
  backup_retention_period = "${terraform.workspace == "prod" ? 7 : 0}"
  enabled_cloudwatch_logs_exports = ["postgresql","upgrade"]
  apply_immediately    = true

  tags = {
    Name = "${terraform.workspace} RDS Instance"
  }
}