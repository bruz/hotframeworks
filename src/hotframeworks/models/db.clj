(ns hotframeworks.models.db
  (:require [clojure.java.jdbc :as sql]
            [korma.db :refer [defdb]]
            [korma.core :refer :all]))

(defdb db
  {:subprotocol "postgresql"
   :subname "hotframeworks"
   :user "bruz"
   :password ""})

(declare frameworks languages statistics statistic-sets)

(defentity frameworks
  (belongs-to languages {:fk :language_id})
  (has-many statistics {:fk :statistic_d}))

(defentity languages
  (has-many frameworks {:fk :language_id}))

(defentity statistics
  (belongs-to frameworks {:fk :statistic_id})
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

(defn update-framework! [map]
  (let [{:keys [id score delta]} map]
    (update frameworks
            (set-fields {:latest_score score
                         :latest_delta delta})
            (where {:id id}))))

(defn all-languages-by-name []
  (select languages
          (order :name :ASC)))

(defn last-statistic-set []
  (first
   (select statistic-sets
           (order :date :DESC)
           (limit 1))))

(defn add-statistic-set! [date]
  (insert statistic-sets
          (values {:date date})))

(defn statistics-for-set [statistic-set]
  (select statistics
          (where {:statistic_set_id (:id statistic-set)})))

(defn add-statistic! [attributes]
  (insert statistics
          (values attributes)))

