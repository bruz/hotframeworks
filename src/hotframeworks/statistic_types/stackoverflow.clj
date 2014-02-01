(ns hotframeworks.statistic-types.stackoverflow
  (:require [hotframeworks.config :as config]
            [stacktraces.tags :as tags]))

(defn stat
  [framework]
  (let [tag (:stackoverflow_tag framework)
        options {:key (config/lookup "STACKOVERFLOW_API_KEY")}
        response (tags/by-name tag options)]
    (if tag
      (or
       (get-in response [:body :items 0 :count])
       0)
      nil)))
