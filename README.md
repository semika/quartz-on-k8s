## Build Docker image
> docker build -t quartz-scheduler:1.0 .

## Run docker image
> docker run --name quartz-scheduler -p 9001:9001 -p 3306:3306 quartz-scheduler:1.0

## Remove docker container
> docker container rm e106980b5830

## Deploying in K8s cluster
> kubectl apply -f quartz-scheduler-deployment.yml

## Undeploying
> kubectl delete -f quartz-scheduler-deployment.yml
> 
## Checkout the code

> git clone git@github.com-personal:semika/quartz-on-k8s.git

## SQS 
This project contains some SQS example too.

## SNS
How use SNS AWS Java SDK v2 to publish a message to SNS topic.

## Quartz
How to configure multiple quartz scheduler jobs.

## Kinesis
1. How to send messages into Kinesis data stream usding AWS SDK
2. How to consume messages from a Kinesis data stream using AWS SDK