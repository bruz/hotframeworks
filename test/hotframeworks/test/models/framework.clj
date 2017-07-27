(ns hotframeworks.test.models.framework
  (:require [hotframeworks.models.framework :as framework]
            [hotframeworks.models.db :as db])
  (:use midje.sweet))

(fact "about the latest scores for frameworks"
      (framework/latest-scores) => [{:name "Ruby on Rails"
                                     :github 95
                                     :stackoverflow 84
                                     :combined 89
                                     :delta 3
                                     :url_identifier nil}
                                    {:name "Django"
                                     :github 81
                                     :stackoverflow 86
                                     :combined 83
                                     :delta -2
                                     :url_identifier nil}]

      (provided (db/latest-statistics) => [{:name "Ruby on Rails"
                                            :type "github"
                                            :score 95
                                            :delta 4}
                                           {:name "Ruby on Rails"
                                            :type "stackoverflow"
                                            :score 84
                                            :delta 2}
                                           {:name "Django"
                                            :type "github"
                                            :score 81
                                            :delta -2}
                                           {:name "Django"
                                            :type "stackoverflow"
                                            :score 86
                                            :delta -2}]))
