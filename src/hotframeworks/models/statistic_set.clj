(ns hotframeworks.models.statistic-set
  (:require [hotframeworks.models.db :as db]
            [hotframeworks.statistic-types.awis :as awis]
            [hotframeworks.statistic-types.github :as github]
            [hotframeworks.statistic-types.stackoverflow :as stackoverflow]))

(def statistic-types [:github :stackoverflow :traffic :links])

(defn stat [framework type]
  (do
    (prn (str "Pulling " (name type) " stat for " (framework :name) "..."))
    (cond
      (= type :github ) (github/stat framework)
      (= type :stackoverflow ) (stackoverflow/stat framework)
      (= type :traffic ) (awis/traffic-stat framework)
      (= type :links ) (awis/link-stat framework))))

(defn stats [frameworks type]
  (map (fn [framework]
         {:framework_id (:id framework)
          :value (stat framework type)}) frameworks))

(defn log-values [stats]
  (map (fn [stat]
         (let [value (:value stat)]
           (if (nil? value)
             nil
             (Math/log value)))) stats))

(defn score-value [value min-value max-value]
  (let [offset (- (Math/log value) min-value)
        range (- max-value min-value)]
    (int (* (/ offset range) 100))))

(defn score-stats [stats]
  (let [stat-values (log-values stats)
        non-nil (remove nil? stat-values)
        min-value (apply min non-nil)
        max-value (apply max non-nil)]
    (map (fn [stat]
           (if-let [value (:value stat)]
             (assoc stat :score (score-value value min-value max-value))
             nil))
      stats)))

(defn last-set []
  (let [set (db/last-statistic-set)]
    (if set
      (group-by :type (db/statistics-for-set set))
      nil)))

(defn delta-set [type new-stats]
  (if-let [last-stats (get (last-set) (name type))]
    {type (map (fn [new-stat]
                 (let [id (:framework_id new-stat)
                       last-stat (first (filter #(= id (:framework_id %)) last-stats))
                       new-score (:score new-stat)
                       last-score (:score last-stat)]
                   (assoc new-stat :delta (- new-score last-score)))) new-stats)}
    {type new-stats}))

(defn pull []
  (let [frameworks (db/all-frameworks)]
    (into {}
          (map (fn [type]
                 (->> type
                      (stats frameworks)
                      score-stats
                      (delta-set type)))
               statistic-types))))

(defn save! [data]
  (let [set (db/add-statistic-set! (:date data))
        id (set :id)]
    (doseq [[type stats] (:stats data)]
      (doseq [stat stats]
        (when (:value stat)
          (db/add-statistic!
            (assoc stat :statistic_set_id id :type (name type))))))
    id))

(defn today []
  (new java.sql.Date (.getTime (new java.util.Date))))

(defn pull-and-save! []
  (save! {:date (today) :stats (pull)}))
