#!/bin/bash

echo "creating subscription------------------"
aws sns subscribe --topic-arn $1  --protocol email --notification-endpoint $2