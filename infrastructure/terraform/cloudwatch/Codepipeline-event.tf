resource "aws_cloudwatch_event_rule" "codepipeline-event" {
  name        = "${terraform.workspace}-codepipeline-events"
  description = "This is the rule to push the codepipeline events of pipelines api service & client-app in ${terraform.workspace} environment and send notify users through SNS."

#   event_pattern = <<PATTERN
# {
#     "source": [
#     "aws.codepipeline"
#   ],
#   "detail-type": [
#     "CodePipeline Stage Execution State Change"
#   ],
#   "detail": {
#     "pipeline": [
#       "${jsonencode(var.pipeline_jobs)}"

#     ],
#     "state": [
#       "FAILED",
#       "SUCCEEDED"
#     ]
#   }
  
# }
# PATTERN
event_pattern = jsonencode({
    "source": [
    "aws.codepipeline"
  ],
  "detail-type": [
    "CodePipeline Stage Execution State Change"
  ],
  "detail": {
    "pipeline": "${var.pipeline_jobs}"

    "state": [
      "FAILED",
      "SUCCEEDED"
    ]
    }
  })
  

}

resource "aws_cloudwatch_event_target" "codepipeline-event-target" {
  rule      = "${aws_cloudwatch_event_rule.codepipeline-event.name}"
  target_id = "Codepipeline-SendToSNS"
  arn       = "${aws_cloudformation_stack.sns_event_topic.outputs["ARN"]}"
  input_transformer {
      input_paths = {"pipeline-name":"$.detail.pipeline","stage-name":"$.detail.stage","state":"$.detail.state"}
      input_template = tostring("\"This message is to notify you that the <stage-name> stage in code pipeline <pipeline-name> for ${terraform.workspace} environment is <state>.\"")
  }
}

# locals {
#   pipeline_jobs = "${slice(var.pipeline_jobs, 0, length(var.pipeline_jobs))}"
# }
