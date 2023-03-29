FROM gradle:7.6.1-jdk-alpine as build

RUN mkdir /webapi

ENV APP_HOME=/webapi
WORKDIR $APP_HOME

COPY --chown=gradle:gradle build.gradle settings.gradle $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src

# COPY . .
# RUN gradle build --no-daemon 

EXPOSE 8082