(ns hotframeworks.routes.home
  (:require [compojure.core :refer :all]
            [hotframeworks.views.layout :as layout]))

(defn home []
  (layout/common [:h1 "sup"]))

(defroutes home-routes
  (GET "/" [] (home)))
