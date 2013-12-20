(ns hotframeworks.test.models.framework
  (:require [hotframeworks.models.framework :as framework]
            [hotframeworks.models.db :as db])
  (:use midje.sweet))

(fact "about the latest scores for frameworks"
      (framework/latest-scores) => [{:name "Ruby on Rails"
                                     :github 95
                                     :stackoverflow 84
                                     :links 75
                                     :traffic 82
                                     :combined 87
                                     :delta -2}
                                    {:name "Django"
                                     :github 81
                                     :stackoverflow 86
                                     :links 81
                                     :traffic 84
                                     :combined 83
                                     :delta 1}]

      (provided (db/latest-statistics) => [{:name "Ruby on Rails"
                                            :type "github"
                                            :score 95
                                            :delta 4}
                                           {:name "Ruby on Rails"
                                            :type "stackoverflow"
                                            :score 84
                                            :delta 2}
                                           {:name "Ruby on Rails"
                                            :type "links"
                                            :score 75
                                            :delta -2}
                                           {:name "Ruby on Rails"
                                            :type "traffic"
                                            :score 82
                                            :delta 4}
                                           {:name "Ruby on Rails"
                                            :type "combined"
                                            :score 87
                                            :delta -2}
                                           {:name "Django"
                                            :type "github"
                                            :score 81
                                            :delta -2}
                                           {:name "Django"
                                            :type "stackoverflow"
                                            :score 86
                                            :delta -2}
                                           {:name "Django"
                                            :type "links"
                                            :score 81
                                            :delta -2}
                                           {:name "Django"
                                            :type "traffic"
                                            :score 84
                                            :delta -2}
                                           {:name "Django"
                                            :type "combined"
                                            :score 83
                                            :delta 1}]))