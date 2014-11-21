(ns hotframeworks.statistic-types.github
  (:require [environ.core :refer [env]]
            tentacles.repos))

(defn stat
  [framework]
  (let [owner (framework :github_owner )
        repo-name (framework :github_repo )
        auth {:oauth-token (env :github-access-token)}]
    ((tentacles.repos/specific-repo owner repo-name auth) :stargazers_count )))
