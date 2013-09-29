(ns hotframeworks.test.statistic-types.awis
  (:require [hotframeworks.statistic-types.awis :as awis]
            [hotframeworks.config :as config]
            clj-amazon-awis.core)
  (:use midje.sweet))

(fact "statistic provided with valid traffic url"
  (def framework {:traffic_url "http://webnoir.org/"})
  (awis/traffic-stat framework) => 8.4
  (provided (config/lookup "AWS_ACCESS_KEY") => "ACCESS KEY")
  (provided (config/lookup "AWS_SECRET_KEY") => "SECRET KEY")
  (provided (clj-amazon-awis.core/url-info
              {:response-group "UsageStats"
               :url "http://webnoir.org/"}
              {:aws-access-key "ACCESS KEY"
               :aws-secret-key "SECRET KEY"}) =>
    {:tag :aws:UrlInfoResponse, :attrs {:xmlns:aws "http://alexa.amazonaws.com/doc/2005-10-05/"},
     :content [{:tag :aws:Response, :attrs {:xmlns:aws "http://awis.amazonaws.com/doc/2005-07-11"},
                :content [{:tag :aws:UrlInfoResult, :attrs nil,
                           :content [{:tag :aws:Alexa, :attrs nil,
                                      :content [{:tag :aws:TrafficData, :attrs nil,
                                                 :content [{:tag :aws:UsageStatistics, :attrs nil,
                                                            :content [{:tag :aws:UsageStatistic, :attrs nil,
                                                                       :content [{:tag :aws:TimeRange, :attrs nil,
                                                                                  :content [{:tag :aws:Days, :attrs nil, :content ["7"]}]}
                                                                                 {:tag :aws:Reach, :attrs nil,
                                                                                  :content [{:tag :aws:PerMillion, :attrs nil,
                                                                                             :content [{:tag :aws:Value, :attrs nil,
                                                                                                        :content ["8.4"]}]}]}]}]}]}]}]}]}]}))

(fact "statistic is nil without a traffic url"
  (def framework {})
  (awis/traffic-stat framework) => nil)

(fact
  (def framework {:link_url "http://webnoir.org/"})
  (awis/link-stat framework) => 1617
  (provided (config/lookup "AWS_ACCESS_KEY") => "ACCESS KEY")
  (provided (config/lookup "AWS_SECRET_KEY") => "SECRET KEY")
  (provided (clj-amazon-awis.core/url-info
              {:response-group "LinksInCount"
               :url "http://webnoir.org/"}
              {:aws-access-key "ACCESS KEY"
               :aws-secret-key "SECRET KEY"}) =>
    {:tag :aws:UrlInfoResponse, :attrs {:xmlns:aws "http://alexa.amazonaws.com/doc/2005-10-05/"},
     :content [{:tag :aws:Response, :attrs {:xmlns:aws "http://awis.amazonaws.com/doc/2005-07-11"},
                :content [{:tag :aws:UrlInfoResult, :attrs nil,
                           :content [{:tag :aws:Alexa, :attrs nil,
                                      :content [{:tag :aws:ContentData, :attrs nil,
                                                 :content [{:tag :aws:LinksInCount, :attrs nil, :content ["1617"]}]}]}]}]}]}))

(fact "statistic is nil without a link url"
  (def framework {})
  (awis/link-stat framework) => nil)
