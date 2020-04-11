FROM clojure:lein-2.9.1 AS lein
FROM oracle/graalvm-ce:20.0.0-java11 AS graalvm

# Native Image Dependencies
ENV GRAALVM_HOME=$JAVA_HOME
RUN gu install native-image
WORKDIR /opt/app

# Leiningen
COPY --from=lein /usr/local/bin/lein /usr/local/bin/lein
COPY --from=lein /root/.lein /root/.lein
COPY --from=lein /root/.m2 /root/.m2
COPY --from=lein /usr/share/java/leiningen-2.9.1-standalone.jar \
                 /usr/share/java/leiningen-2.9.1-standalone.jar

# Uberjar
COPY project.clj .
RUN lein deps
COPY src src
COPY resources resources
RUN lein uberjar

# Native Image
COPY compile-native-image .
RUN ./compile-native-image target/uhttp.jar uhttp

FROM busybox:glibc
WORKDIR /opt/app
RUN mkdir -p /var/tmp
COPY --from=graalvm /opt/app/uhttp .
ENTRYPOINT [ "/opt/app/uhttp" ]
