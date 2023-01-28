#!/usr/bin/env bash

java -Djava.net.preferIPv4Stack=true -Djava.util.logging.config.file=/app/conf/logging.properties -jar app.jar /app/conf/smppsim.props
