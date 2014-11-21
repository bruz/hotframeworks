(ns hotframeworks.test.statistic-types.stackoverflow
  (:require [hotframeworks.statistic-types.stackoverflow :as stackoverflow]
            [environ.core :refer [env]]
            [stacktraces.tags :as tags])
  (:use midje.sweet))

(fact "statistic provided with valid stackoverflow tag"
  (def framework {:stackoverflow_tag "noir"})
  (stackoverflow/stat framework) => 300
  (provided (tags/by-name "noir" {:key "KEY"}) => {:body {:items [{:count 300}]}})
  (provided (env :stackoverflow-api-key) => "KEY"))

(fact "statistic 0 with valid stackoverflow tag"
  (def framework {:stackoverflow_tag "uki"})
  (stackoverflow/stat framework) => 0
  (provided (tags/by-name "uki" {:key "KEY"}) => {:body {:items []}})
  (provided (env :stackoverflow-api-key) => "KEY"))

(fact "statistic is nil without a stackoverflow tag"
  (def framework {})
  (stackoverflow/stat framework) => nil)
