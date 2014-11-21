(ns hotframeworks.generator
  (:require [hotframeworks.models.statistic-set :as statistic-set]
            [hotframeworks.views.main :as main]
            [hotframeworks.models.db :as db]
            [aws.sdk.s3 :as s3]
            [org.satta.glob :refer [glob]]
            [environ.core :refer [env]])
  (:gen-class))

(def cred
  {:access-key (env :aws-access-key)
   :secret-key (env :aws-secret-key)})

(defn upload-file [file web-path content-type]
  (s3/put-object cred "hotframeworks.com" web-path file {:content-type content-type}))

(defn framework-page [framework]
  (let [identifier (:url_identifier framework)]
    {:path (str "frameworks/" identifier) :html (main/framework identifier)}))

(defn language-page [language]
  (let [identifier (:url_identifier language)]
    {:path (str "languages/" identifier) :html (main/language identifier)}))

(defn pages []
  (concat [{:path "index.html" :html (main/home)}
           {:path "faq" :html (main/faq)}]
          (map #(framework-page %) (db/all-frameworks))
          (map #(language-page %) (db/all-languages-by-name))))

(defn upload-html []
  (doseq [page (pages)]
    (upload-file (:html page) (:path page) "text/html")))

(defn upload-js []
  (doseq [file (glob "resources/public/js/*")]
    (upload-file file (str "js/" (.getName file)) "text/javascript")))

(defn upload-css []
  (doseq [file (glob "resources/public/css/*")]
    (upload-file file (str "css/" (.getName file)) "text/css")))

(defn upload-content []
  (let [home-html (main/home)]
    (upload-html)
    (upload-js)
    (upload-css)))

(defn prune-old-data! []
  (db/prune-statistic-sets!))

(defn -main []
  (do
    (prn "Collecting statistics...")
    (statistic-set/pull-and-save!)
    (prn "Uploading...")
    (upload-content)
    (prn "Removing old data")
    (prune-old-data!)
    (prn "Done!")))

