FROM openjdk:17
RUN mkdir /src
RUN mkdir /app
COPY . /src
WORKDIR /src
RUN ./mvnw install -DskipTests

RUN mv target/*.jar /app/jazflix-bo-api.jar
WORKDIR /app
RUN rm -rf /src

CMD java -jar jazflix-bo-api.jar