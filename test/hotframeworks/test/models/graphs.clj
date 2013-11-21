(ns hotframeworks.test.models.graphs
  (:require [hotframeworks.models.graphs :as graphs]
            [hotframeworks.models.db :as db])
  (:use midje.sweet))

(fact "about the most popular frameworks over time graph"
      (graphs/most-popular 3) => [{:name "Ruby on Rails"
                                   :data [{:x 1383264000 :y 98}
                                          {:x 1383868800 :y 99}
                                          {:x 1384473600 :y 96}]},
                                  {:name "Django"
                                   :data [{:x 1383264000 :y 90}
                                          {:x 1383868800 :y 88}
                                          {:x 1384473600 :y 91}]},
                                  {:name "Zend"
                                   :data [{:x 1383264000 :y 85}
                                          {:x 1383868800 :y 84}
                                          {:x 1384473600 :y 80}]}]

      (provided (db/most-popular-frameworks 5) => [{:id 1 :name "Ruby on Rails"}
                                                   {:id 2 :name "Django"}
                                                   {:id 3 :name "Zend"}])

      (provided (db/framework-combined-score-history 1) => [{:date "2013-11-01" :score 98}
                                                            {:date "2013-11-08" :score 99}
                                                            {:date "2013-11-15" :score 96}])

      (provided (db/framework-combined-score-history 2) => [{:date "2013-11-01" :score 90}
                                                            {:date "2013-11-08" :score 88}
                                                            {:date "2013-11-15" :score 91}])

      (provided (db/framework-combined-score-history 3) => [{:date "2013-11-01" :score 85}
                                                            {:date "2013-11-08" :score 84}
                                                            {:date "2013-11-15" :score 80}]))
