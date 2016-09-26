(ns hotframeworks.models.db
  (:require [clojure.java.jdbc :as sql]
            [korma.db :refer [defdb]]
            [korma.core :refer :all]
            [clj-time.coerce :as time-coerce]
            [clj-time.core :as time-core]
            [environ.core :refer [env]]))

(defdb db
  {:subprotocol "postgresql"
   :subname (str "//" (env :database-host) ":5432/" (env :database-name))
   :user (env :database-user)
   :password (env :database-password)
   :sslmode "require"})

(declare frameworks languages statistics statistic-sets)

(defentity frameworks
  (belongs-to languages {:fk :language_id})
  (has-many statistics {:fk :statistic_d}))

(defentity languages
  (has-many frameworks {:fk :language_id}))

(defentity statistics
  (belongs-to frameworks {:fk :framework_id})
  (belongs-to statistic-sets {:fk :statistic_set_id}))

(defentity statistic-sets
  (table :statistic_sets)
  (has-many statistics {:fk :statistic_set_id}))

(defn all-frameworks []
  (select frameworks))

(defn most-popular-frameworks [max]
  (select frameworks
          (order :latest_score :DESC)
          (limit max)))

(defn framework-combined-score-history [id max-timepoints]
  (reverse
   (select statistics
           (with statistic-sets)
           (fields :statistic_sets.date :score)
           (where {:type "combined"
                   :framework_id id})
           (order :statistic_sets.date :DESC)
           (limit max-timepoints))))

(defn framework-for-url-identifier [identifier]
  (first
   (select frameworks
           (where {:url_identifier identifier}))))

(defn update-framework! [map]
  (let [{:keys [id latest-score latest-delta]} map]
    (update frameworks
            (set-fields {:latest_score latest-score
                         :latest_delta latest-delta})
            (where {:id id}))))

(defn all-languages-by-name []
  (select languages
          (order :name :ASC)))

(defn frameworks-for-language [language]
  (select frameworks
          (where {:language_id (:id language)})
          (order :latest_score :DESC)))

(defn language-for-url-identifier [identifier]
  (first
   (select languages
           (where {:url_identifier identifier}))))

(defn language-for-id [id]
  (first
   (select languages
           (where {:id id}))))

(defn latest-statistic-sets [number]
  (select statistic-sets
          (order :date :DESC)
          (limit number)))

(defn last-statistic-set []
  (first
   (latest-statistic-sets 1)))

(defn add-statistic-set! [date]
  (insert statistic-sets
          (values {:date date})))

(defn statistics-for-sets [set-ids]
  (select statistics
          (where {:statistic_set_id [in set-ids]})))

(defn statistics-for-set [statistic-set]
  (statistics-for-sets [(:id statistic-set)]))

(defn combined-statistics-for-sets [set-ids]
  (select statistics
          (where {:statistic_set_id [in set-ids]
                  :type "combined"})))

(defn delete-statistic-set! [{:keys [id]}]
  (delete statistics
          (where {:statistic_set_id id}))
  (delete statistic-sets
          (where {:id id})))

(def desired-intervals
  (concat [1] (repeat 8 7) (repeat 3 28) (repeat 8 56) (repeat 2 112)))

(defn days-between [before after]
  (time-core/in-days
    (time-core/interval (time-coerce/from-date before) (time-coerce/from-date after))))

(defn first-unneeded-set [sets]
  (let [pairs (partition 2 1 sets)]
    (->> (map vector pairs desired-intervals)
         (filter (fn [[pair desired-interval]]
              (let [first-date (:date (first pair))
                    last-date (:date (last pair))
                    interval (days-between last-date first-date)]
                (< interval desired-interval))))
        ffirst
        first)))

(defn prune-statistic-sets! []
  (loop [sets (select statistic-sets (order :date :DESC))]
    (let [unneeded-set (first-unneeded-set sets)
          remaining-sets (remove #(= % unneeded-set) sets)]
      (if unneeded-set
        (do
          (delete-statistic-set! unneeded-set)
          (recur remaining-sets))))))

(defn add-statistic! [map]
  (let [{:keys [type statistic_set_id framework_id score value delta]} map]
    (insert statistics
            (values {:type type
                     :statistic_set_id statistic_set_id
                     :framework_id framework_id
                     :score (int score)
                     :value (int value)
                     :delta (if (nil? delta) nil (int delta))}))))

(defn latest-statistics []
  (let [statistic-set-id (:id (last-statistic-set))]
    (select statistics
            (with statistic-sets)
            (with frameworks)
            (fields :frameworks.name :frameworks.url_identifier :type :score :delta)
            (where {:statistic_set_id statistic-set-id}))))

(defn latest-statistics-for-framework [framework]
  (let [statistic-set-id (:id (last-statistic-set))]
    (select statistics
            (with statistic-sets)
            (with frameworks)
            (fields :frameworks.name :frameworks.url_identifier :type :score :delta)
            (where {:statistic_set_id statistic-set-id
                    :frameworks.id (:id framework)}))))
