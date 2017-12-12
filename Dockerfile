FROM openjdk:8u131-jre-alpine

ENV ENTRYPOINT_NAME=docker-entrypoint.sh \
    HEALTHCHECK_NAME=docker-healthcheck.sh \
    APP_NAME=demo-service \
    APP_PORT=6001

ENV ENTRYPOINT_EXEC=/usr/local/bin/${ENTRYPOINT_NAME} \
    HEALTHCHECK_EXEC=/usr/local/bin/${HEALTHCHECK_NAME} \
    APP_HOME=/usr/local/${APP_NAME}

ENV APP_EXEC=${APP_HOME}/bin/${APP_NAME}

EXPOSE ${APP_PORT}

RUN apk add --update bash curl && \
    rm -rf /var/cache/apk/*

COPY ${ENTRYPOINT_NAME} ${ENTRYPOINT_EXEC}
COPY ${HEALTHCHECK_NAME} ${HEALTHCHECK_EXEC}
COPY build/install/${APP_NAME} ${APP_HOME}

HEALTHCHECK CMD ${HEALTHCHECK_EXEC} || exit 1

ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
