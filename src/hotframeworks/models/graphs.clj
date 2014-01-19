(ns hotframeworks.models.graphs
  (:require [hotframeworks.models.db :as db]
            [clj-time.coerce :as time-coerce]))

(defn- epoch-seconds [date-string]
  (/
    (time-coerce/to-long date-string)
    1000))

(defn- date-score-to-point [stat]
  {:x (epoch-seconds (:date stat)) :y (:score stat)})

(defn framework-combined [framework max-timepoints]
  (let [stats (db/framework-combined-score-history (:id framework) max-timepoints)]
    {:name (:name framework)
     :data (map date-score-to-point stats)}))

(defn- frameworks-combined [frameworks max-timepoints]
  (map #(framework-combined % max-timepoints) frameworks))

(defn most-popular [max-frameworks max-timepoints]
  (let [top-frameworks (db/most-popular-frameworks max-frameworks)]
    (frameworks-combined top-frameworks max-timepoints)))

(defn for-language [language max-timepoints]
  (let [frameworks (db/frameworks-for-language language)]
    (frameworks-combined frameworks max-timepoints)))

(defn for-framework [framework max-timepoints]
  (frameworks-combined [framework] max-timepoints))
