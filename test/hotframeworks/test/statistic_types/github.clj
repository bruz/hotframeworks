(ns hotframeworks.test.statistic-types.github
  (:require [hotframeworks.statistic-types.github :as github]
            [hotframeworks.config :as config]
            [tentacles.repos :as repos])
  (:use midje.sweet))

(fact "statistic provided with valid github owner/repo"
  (def framework {:github_owner "Raynes" :github_repo "tentacles"})
  (github/stat framework) => 99
  (provided (config/lookup "GITHUB_ACCESS_TOKEN") => "SECRET")
  (provided (repos/specific-repo "Raynes" "tentacles" {:oauth-token "SECRET"}) => {:stargazers_count 99}))

(fact "statistic is nil without github owner/repo"
  (def framework {})
  (github/stat framework) => nil
  (provided (config/lookup "GITHUB_ACCESS_TOKEN") => "SECRET"))
