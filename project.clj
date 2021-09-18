(defproject uhttp "0.1.0-SNAPSHOT"
  :description "Native CLI tool created using GraalVM and unixsocket-http."
  :url "https://github.com/into-docker/uhttp"
  :license {:name "MIT"
            :url "https://choosealicense.com/licenses/mit"
            :year 2020
            :key "mit"
            :comment "MIT License"}
  :dependencies [[org.clojure/clojure "1.10.2"]
                 [unixsocket-http "1.0.10"]]

  :profiles {:uberjar {:uberjar-name "uhttp.jar"
                       :global-vars  {*assert* false}
                       :jvm-opts     ["-Dclojure.compiler.direct-linking=true"
                                      "-Dclojure.spec.skip-macros=true"]
                       :main         uhttp.main
                       :aot          :all}}

  :pedantic? :abort)
