(ns hotframeworks.models.graphs
  (:require [hotframeworks.models.db :as db]
            [clj-time.coerce :as time-coerce]))

(defn- epoch-seconds [date-string]
  (/
    (time-coerce/to-long date-string)
    1000))

(defn- date-score-to-point [stat]
  {:x (epoch-seconds (:date stat)) :y (:score stat)})

(defn- framework-combined [framework]
  (let [stats (db/framework-combined-score-history (:id framework))]
    {:name (:name framework)
     :data (map date-score-to-point stats)}))

(defn most-popular [max]
  (let [top-five (db/most-popular-frameworks 5)]
    (map framework-combined top-five)))
