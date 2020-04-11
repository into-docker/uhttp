(ns uhttp.main
  (:gen-class)
  (:require [unixsocket-http.core :as uhttp]
            [clojure.java.io :as io])
  (:import [org.newsclub.lib.junixsocket.common NarMetadata]
           [java.io File]))

(defn -main
  [& [socket-addr path]]
  (if (and socket-addr path)

    ;; perform the request
    (let [client (uhttp/client socket-addr)]
      (prn (uhttp/get client path)))

    ;; attempt native library load
    (let [library (System/mapLibraryName "junixsocket-native-2.3.2")
          arch    (System/getProperty "os.arch")
          os      (-> (System/getProperty "os.name") (.replaceAll " " ""))
          path    (format "/lib/%s-%s-clang/jni/%s" arch os library)
          tmp-dir (System/getProperty "java.io.tmpdir")]
      (println "Native Library: " library)
      (println "  Architecture: " arch)
      (println "  OS:           " os)
      (println "  Path:         " path)
      (println "  Temp Dir:     " tmp-dir)
      (println "Loading native library ...")
      (let [tmp-file (File/createTempFile "libtmp" library)
            tmp-path (.getAbsolutePath tmp-file)
            stream   (.getResourceAsStream NarMetadata path)]
        (or (when stream
              (println "  Copying to" tmp-path "...")
              (with-open [stream stream
                          out    (io/output-stream tmp-file)]
                (io/copy stream out))
              (println "  Loading" tmp-path "...")
              (System/load (.getAbsolutePath tmp-file))
              (println "  OK!")
              :ok)
            (println "The native library could not be found."))))))
