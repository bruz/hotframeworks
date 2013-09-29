(ns hotframeworks.models.db
  (:require [clojure.java.jdbc :as sql]
            [korma.db :refer [defdb transaction]]
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
  (has-many statistics {:fk :statistic_set_id}))