FROM openjdk:8

WORKDIR /app

ADD target/*.jar /app/app.jar
ADD startsmppsim.sh /app/startsmppsim.sh
ADD src/main/resources/logging.properties /app/conf/logging.properties
ADD src/main/resources/smppsim.props /app/conf/smppsim.props

RUN chmod +x /app/startsmppsim.sh

EXPOSE 2775

EXPOSE 8885

WORKDIR /app

CMD ["/app/startsmppsim.sh"]
