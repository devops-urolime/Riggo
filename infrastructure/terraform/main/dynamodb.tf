#create a dynamodb table for locking the state file
resource "aws_dynamodb_table" "dynamodb-terraform-state-lock" {
  name = "terraform-state-lock-dynamodb"
  hash_key = "LockID"
  read_capacity = "${var.read_capacity}"
  write_capacity = "${var.write_capacity}"

  attribute {
    name = "LockID"
    type = "S"
  }

  tags = {
    Name = "${terraform.workspace} DynamoDB Terraform State Lock Table"
  }
}
