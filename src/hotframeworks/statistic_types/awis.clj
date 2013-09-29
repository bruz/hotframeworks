(ns hotframeworks.statistic-types.awis
  (:require [hotframeworks.config :as config]
            [clojure.zip :as zip]
            [clj-amazon-awis.core :as awis])
  (:use [clojure.data.zip.xml]))

(defn zipped-url-info
  [response-group url]
  (let [auth {:aws-access-key (config/lookup "AWS_ACCESS_KEY")
              :aws-secret-key (config/lookup "AWS_SECRET_KEY")}]
    (zip/xml-zip (awis/url-info {:response-group response-group :url url} auth))))

(defn first-number
  [data]
  (if-let [first-item (first data)]
    (read-string first-item)
    nil))

(defn traffic-stat
  [framework]
  (if-let [traffic-url (framework :traffic_url)]
    (let [zipped (zipped-url-info "UsageStats" traffic-url)]
      (first-number (xml-> zipped :aws:Response
                                  :aws:UrlInfoResult
                                  :aws:Alexa
                                  :aws:TrafficData
                                  :aws:UsageStatistics
                                  :aws:UsageStatistic
                                  [:aws:TimeRange :aws:Days (text= "7")]
                                  :aws:Reach
                                  :aws:PerMillion
                                  :aws:Value
                                  text)))
    nil))

(defn link-stat
  [framework]
  (if-let [link-url (framework :link_url)]
    (let [zipped (zipped-url-info "LinksInCount" link-url)]
      (first-number (xml-> zipped :aws:Response
                                  :aws:UrlInfoResult
                                  :aws:Alexa
                                  :aws:ContentData
                                  :aws:LinksInCount
                                  text)))
    nil))
