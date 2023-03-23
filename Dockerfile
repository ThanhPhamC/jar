FROM gradle:5.3.1-jdk8-alpine AS builder

WORKDIR /webapi

EXPOSE 8080 5005