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