(ns hotframeworks.test.models.graphs
  (:require [hotframeworks.models.graphs :as graphs]
            [hotframeworks.models.db :as db])
  (:use midje.sweet))

(fact "about the most popular frameworks over time graph"
      (graphs/most-popular 3 2) => [{:name "Ruby on Rails"
                                     :data [{:x 1383868800 :y 99}
                                            {:x 1384473600 :y 96}]},
                                    {:name "Django"
                                     :data [{:x 1383868800 :y 88}
                                            {:x 1384473600 :y 91}]},
                                    {:name "Zend"
                                     :data [{:x 1383868800 :y 84}
                                            {:x 1384473600 :y 80}]}]

      (provided (db/most-popular-frameworks 3) => [{:id 1 :name "Ruby on Rails"}
                                                   {:id 2 :name "Django"}
                                                   {:id 3 :name "Zend"}])

      (provided (db/framework-combined-score-history 1 2) => [{:date "2013-11-08" :score 99}
                                                              {:date "2013-11-15" :score 96}])

      (provided (db/framework-combined-score-history 2 2) => [{:date "2013-11-08" :score 88}
                                                              {:date "2013-11-15" :score 91}])

      (provided (db/framework-combined-score-history 3 2) => [{:date "2013-11-08" :score 84}
                                                              {:date "2013-11-15" :score 80}]))
