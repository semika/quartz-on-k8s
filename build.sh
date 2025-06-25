#!/bin/bash

mvn clean install
docker build -t quartz-scheduler:1.20 .
kubectl delete -f ./quartz-scheduler-deployment.yml
kubectl apply -f ./quartz-scheduler-deployment.yml