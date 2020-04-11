(defproject uhttp "0.1.0-SNAPSHOT"
  :description "Native CLI tool created using GraalVM and unixsocket-http."
  :url "https://github.com/into-docker/unixsocket-http-graalvm"
  :license {:name "MIT License"
            :url "none"
            :year 2020
            :key "mit"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [unixsocket-http "1.0.2-SNAPSHOT"]]

  :profiles {:uberjar {:uberjar-name "uhttp.jar"
                       :global-vars  {*assert* false}
                       :jvm-opts     ["-Dclojure.compiler.direct-linking=true"
                                      "-Dclojure.spec.skip-macros=true"]
                       :main         uhttp.main
                       :aot          :all}}

  :pedantic? :abort)
