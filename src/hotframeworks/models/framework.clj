(ns hotframeworks.models.framework
  [:require [hotframeworks.models.db :as db]
            [hotframeworks.utils :as utils]])

(defn find-by-type [scores type]
  (first
   (filter #(= type (:type %)) scores)))

(defn average-metric [scores metric]
  (let [values (map metric scores)]
    (utils/average values)))

(defn collapse-scores [name scores]
  (let [github (find-by-type scores "github")
        stackoverflow (find-by-type scores "stackoverflow")]
    {:name name
     :url_identifier (or (:url_identifier github) (:url_identifier stackoverflow))
     :combined (average-metric scores :score)
     :delta (average-metric scores :delta)
     :github (:score github)
     :stackoverflow (:score stackoverflow)}))

(defn latest-scores []
  (let [statistics (db/latest-statistics)
        by-framework (group-by :name statistics)]
    (sort #(> (:combined %1) (:combined %2))
          (map (fn [[name scores]]
                 (collapse-scores name scores))
               by-framework))))

(defn latest-score-for-framework [framework type]
  (let [statistics (db/latest-statistics-for-framework framework)]
    (:score (find-by-type statistics type))))
