(ns hotframeworks.statistic-types.github
  (:require [hotframeworks.config :as config]
            tentacles.repos))

(defn stat
  [framework]
  (let [owner (framework :github_owner )
        repo-name (framework :github_repo )
        auth {:oauth-token (config/lookup "GITHUB_ACCESS_TOKEN")}]
    ((tentacles.repos/specific-repo owner repo-name auth) :stargazers_count )))
