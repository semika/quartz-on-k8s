#!/bin/bash

mvn clean install
docker build -t quartz-scheduler:1.33 .
kubectl delete -f ./quartz-scheduler-deployment.yml
kubectl apply -f ./quartz-scheduler-deployment.yml