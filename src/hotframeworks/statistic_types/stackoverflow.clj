(ns hotframeworks.statistic-types.stackoverflow
  (:require [environ.core :refer [env]]
            [stacktraces.tags :as tags]))

(defn stat
  [framework]
  (let [tag (:stackoverflow_tag framework)
        options {:key (env :stackoverflow-api-key)}
        response (tags/by-name tag options)]
    (if tag
      (or
       (get-in response [:body :items 0 :count])
       0)
      nil)))
