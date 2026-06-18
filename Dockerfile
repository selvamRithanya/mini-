# Multi-stage build: compile Java sources then run
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace
COPY . .
RUN mkdir -p target/classes
RUN find src/main/java -name "*.java" -print0 | xargs -0 javac --release 17 -encoding UTF-8 -cp "lib/*" -d target/classes

FROM eclipse-temurin:17-jre
WORKDIR /app
# copy compiled classes, libraries and webapp
COPY --from=builder /workspace/target/classes target/classes
COPY --from=builder /workspace/lib lib
COPY --from=builder /workspace/src/main/webapp webapp

ENV PORT=8080
EXPOSE ${PORT}

CMD ["sh","-c","java -cp 'target/classes:lib/*' com.ems.EmbeddedServer"]
