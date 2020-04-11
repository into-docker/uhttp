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
RUN $GRAALVM_HOME/bin/native-image \
      -jar target/uhttp.jar \
      -H:Name=uhttp\
      --static \
      --no-server \
      --no-fallback \
      --enable-http \
      --enable-https \
      --enable-all-security-services \
      --initialize-at-build-time \
      --initialize-at-run-time=org.newsclub.net.unix.NativeUnixSocket \
      --allow-incomplete-classpath \
      --report-unsupported-elements-at-runtime \
      -H:EnableURLProtocols=http,https \
      -H:ResourceConfigurationResources=unixsocket/graalvm/resource-config.json \
      -H:ReflectionConfigurationResources=unixsocket/graalvm/reflect-config.json \
      -H:JNIConfigurationResources=unixsocket/graalvm/jni-config.json \
      -H:+ReportExceptionStackTraces \
      -H:+JNI \
      -J-Dclojure.spec.skip-macros=true \
      -J-Dclojure.compiler.direct-linking=true \
      -J-Xmx3g

FROM busybox:glibc
WORKDIR /opt/app
RUN mkdir -p /var/tmp
COPY --from=graalvm /opt/app/uhttp .
ENTRYPOINT [ "/opt/app/uhttp" ]
