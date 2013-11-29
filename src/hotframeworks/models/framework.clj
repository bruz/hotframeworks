(ns hotframeworks.models.framework
  [:require [hotframeworks.models.db :as db]])

(defn find-by-type [scores type]
  (first
   (filter #(= type (:type %)) scores)))

(defn collapse-scores [name scores]
  (let [combined (find-by-type scores "combined")]
    (reduce merge {:name name :combined (:score combined) :delta (:delta combined)}
            (map (fn [type]
                   (let [score (find-by-type scores type)]
                     {(keyword type) (:score score)}))
                 ["github" "stackoverflow" "links" "traffic"]))))

(defn latest-scores []
  (let [statistics (db/latest-statistics)
        by-framework (group-by :name statistics)]
    (sort #(> (:combined %1) (:combined %2))
          (map (fn [[name scores]]
                 (collapse-scores name scores))
               by-framework))))
