#!/bin/bash

mvn clean install
docker build -t quartz-scheduler:1.36 .
kubectl delete -f ./k8s-job.yml
kubectl apply -f ./k8s-job.yml