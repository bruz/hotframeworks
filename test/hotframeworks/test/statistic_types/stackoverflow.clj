(ns hotframeworks.test.statistic-types.stackoverflow
  (:require [hotframeworks.statistic-types.stackoverflow :as stackoverflow]
            [hotframeworks.config :as config]
            [stacktraces.tags :as tags])
  (:use midje.sweet))

(fact "statistic provided with valid stackoverflow tag"
  (def framework {:stackoverflow_tag "noir"})
  (stackoverflow/stat framework) => 300
  (provided (tags/by-name "noir" {:key "KEY"}) => {:body {:items [{:count 300}]}})
  (provided (config/lookup "STACKOVERFLOW_API_KEY") => "KEY"))

(fact "statistic is nil without a stackoverflow tag"
  (def framework {})
  (stackoverflow/stat framework) => nil)
