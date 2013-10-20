(ns hotframeworks.test.models.statistic-set
  (:require [hotframeworks.models.statistic-set :as statistic-set]
            [hotframeworks.models.db :as db]
            [hotframeworks.statistic-types.github :as github]
            [hotframeworks.statistic-types.stackoverflow :as stackoverflow]
            [hotframeworks.statistic-types.awis :as awis])
  (:use midje.sweet))

(def tentacles {:id 1 :github_owner "Raynes" :github_repo "tentacles"})
(def noir {:id 2 :github_owner "noir-clojure" :github_repo "noir"})
(def rails {:id 3 :github_owner "rails" :github_repo "rails"})

(against-background [(db/all-frameworks) => [tentacles noir rails]
                     (github/stat tentacles) => 12
                     (github/stat noir) => 321
                     (github/stat rails) => 1200
                     (stackoverflow/stat tentacles) => 90
                     (stackoverflow/stat noir) => 50
                     (stackoverflow/stat rails) => 400
                     (awis/traffic-stat tentacles) => 2
                     (awis/traffic-stat noir) => 4
                     (awis/traffic-stat rails) => 20
                     (awis/link-stat tentacles) => 40
                     (awis/link-stat noir) => 300
                     (awis/link-stat rails) => 200000]

                    (fact "about pulling stats with a prior stat set"
                          (statistic-set/pull) => {:github
                                                   [{:framework_id 1 :value 12 :score 0 :delta 0}
                                                    {:framework_id 2 :value 321 :score 71 :delta 11}
                                                    {:framework_id 3 :value 1200 :score 100 :delta 0}]
                                                   :stackoverflow
                                                   [{:framework_id 1 :value 90 :score 28 :delta 28}
                                                    {:framework_id 2 :value 50 :score 0 :delta -71}
                                                    {:framework_id 3 :value 400 :score 100 :delta 0}]
                                                   :traffic
                                                   [{:delta 0 :score 0 :framework_id 1 :value 2}
                                                    {:delta -41 :score 30 :framework_id 2 :value 4}
                                                    {:delta 0 :score 100 :framework_id 3 :value 20}]
                                                   :links
                                                   [{:delta 0 :score 0 :framework_id 1 :value 40}
                                                    {:delta -48 :score 23 :framework_id 2 :value 300}
                                                    {:delta 0 :score 100 :framework_id 3 :value 200000}]
                                                   :combined
                                                   [{:delta 3 :score 7 :framework_id 1 :value 7}
                                                    {:delta -9 :score 31 :framework_id 2 :value 31}
                                                    {:delta 2 :score 100 :framework_id 3 :value 100}]}

                          (provided (db/last-statistic-set) => {:id 42 :date "DATE"})

                          (provided (db/statistics-for-set {:id 42 :date "DATE"}) => [{:framework_id 1 :value 10 :score 0 :type "github"}
                                                                                      {:framework_id 2 :value 280 :score 60 :type "github"}
                                                                                      {:framework_id 3 :value 1200 :score 100 :type "github"}
                                                                                      {:framework_id 1 :value 100 :score 0 :type "stackoverflow"}
                                                                                      {:framework_id 2 :value 40 :score 71 :type "stackoverflow"}
                                                                                      {:framework_id 3 :value 420 :score 100 :type "stackoverflow"}
                                                                                      {:framework_id 1 :value 2 :score 0 :type "traffic"}
                                                                                      {:framework_id 2 :value 3 :score 71 :type "traffic"}
                                                                                      {:framework_id 3 :value 21 :score 100 :type "traffic"}
                                                                                      {:framework_id 1 :value 35 :score 0 :type "links"}
                                                                                      {:framework_id 2 :value 390 :score 71 :type "links"}
                                                                                      {:framework_id 3 :value 19900 :score 100 :type "links"}
                                                                                      {:framework_id 1 :value 35 :score 4 :type "combined"}
                                                                                      {:framework_id 2 :value 390 :score 40 :type "combined"}
                                                                                      {:framework_id 3 :value 19900 :score 98 :type "combined"}]))

                    (fact "about pulling stats without a prior stat set"
                          (statistic-set/pull) => {:github
                                                   [{:framework_id 1 :value 12 :score 0}
                                                    {:framework_id 2 :value 321 :score 71}
                                                    {:framework_id 3 :value 1200 :score 100}]
                                                   :stackoverflow
                                                   [{:framework_id 1 :value 90 :score 28}
                                                    {:framework_id 2 :value 50 :score 0}
                                                    {:framework_id 3 :value 400 :score 100}]
                                                   :traffic
                                                   [{:framework_id 1 :value 2 :score 0}
                                                    {:framework_id 2 :value 4 :score 30}
                                                    {:framework_id 3 :value 20 :score 100}]
                                                   :links
                                                   [{:framework_id 1 :value 40 :score 0}
                                                    {:framework_id 2 :value 300 :score 23}
                                                    {:framework_id 3 :value 200000 :score 100}]
                                                   :combined
                                                   [{:framework_id 1 :value 7 :score 7}
                                                    {:framework_id 2 :value 31 :score 31}
                                                    {:framework_id 3 :value 100 :score 100}]}

                          (provided (db/last-statistic-set) => [])))

(def today (statistic-set/today))

(def pulled-stats {:date today
                   :stats {:github [{:framework_id 1 :value 12 :score 0 :delta 0}
                                    {:framework_id 2 :value 321 :score 71 :delta 11}
                                    {:framework_id 3 :value 1200 :score 100 :delta 0}
                                    {:framework_id nil :value nil :score nil :delta nil}]
                           :stackoverflow [{:framework_id 1 :value 90 :score 28 :delta 28}
                                           {:framework_id 2 :value 50 :score 0 :delta -71}
                                           {:framework_id 3 :value 400 :score 100 :delta 0}
                                           {:framework_id nil :value nil :score nil :delta nil}]
                           :combined [{:framework_id 1 :score 28 :delta 28}
                                      {:framework_id 2 :score 0 :delta -71}
                                      {:framework_id 3 :score 100 :delta 0}
                                      {:framework_id nil :score nil :delta nil}]}})

(fact "about saving stats"
      (statistic-set/save! pulled-stats) => 1
      (provided (db/update-framework! {:id 1 :latest_score 28 :latest_delta 28}) => 1)
      (provided (db/update-framework! {:id 2 :latest_score 0 :latest_delta -71}) => 1)
      (provided (db/update-framework! {:id 3 :latest_score 100 :latest_delta 0}) => 1)
      (provided (db/add-statistic-set! today) => {:id 1})
      (provided (db/add-statistic! {:statistic_set_id 1
                                    :framework_id 1
                                    :type "github"
                                    :value 12
                                    :score 0
                                    :delta 0}) => 1)
      (provided (db/add-statistic! {:statistic_set_id 1
                                    :framework_id 2
                                    :type "github"
                                    :value 321
                                    :score 71
                                    :delta 11}) => 2)
      (provided (db/add-statistic! {:statistic_set_id 1
                                    :framework_id 3
                                    :type "github"
                                    :value 1200
                                    :score 100
                                    :delta 0}) => 3)
      (provided (db/add-statistic! {:statistic_set_id 1
                                    :framework_id 1
                                    :type "stackoverflow"
                                    :value 90
                                    :score 28
                                    :delta 28}) => 4)
      (provided (db/add-statistic! {:statistic_set_id 1
                                    :framework_id 2
                                    :type "stackoverflow"
                                    :value 50
                                    :score 0
                                    :delta -71}) => 5)
      (provided (db/add-statistic! {:statistic_set_id 1
                                    :framework_id 3
                                    :type "stackoverflow"
                                    :value 400
                                    :score 100
                                    :delta 0}) => 6))
