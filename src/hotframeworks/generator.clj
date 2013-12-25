(ns hotframeworks.generator
  (:require [hotframeworks.models.statistic-set :as statistic-set]
            [hotframeworks.routes.home :as home]
            [hotframeworks.config :as config]
            [aws.sdk.s3 :as s3]
            [org.satta.glob :refer [glob]])
  (:gen-class))

(def cred
  {:access-key (config/lookup "AWS_ACCESS_KEY")
   :secret-key (config/lookup "AWS_SECRET_KEY")})

(defn upload-file [file web-path content-type]
  (prn (str "web-path: " web-path "cred: " cred "file: " file))
  (s3/put-object cred "hotframeworks.com" web-path file {:content-type content-type}))

(defn upload-js []
  (doseq [file (glob "resources/public/js/*")]
    (upload-file file (str "js/" (.getName file)) "text/javascript")))

(defn upload-css []
  (doseq [file (glob "resources/public/css/*")]
    (upload-file file (str "css/" (.getName file)) "text/css")))

(defn upload-content []
  (let [home-html (home/home)]
    (upload-file home-html "index.html" "text/html")
    (upload-js)
    (upload-css)))

(defn -main []
  (do
    (prn "Collecting statistics...")
    ;;(statistic-set/pull-and-save!)
    (prn "Uploading...")
    (upload-content))
    (prn "Done!"))

