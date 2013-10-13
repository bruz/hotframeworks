(ns hotframeworks.routes.home
  (:require [compojure.core :refer :all]
            [hotframeworks.views.layout :as layout]))

(defn home []
  (layout/common
   [:div.row
    [:div#about.col-md-12
     [:h1 "Find your new favorite web framework"
      [:br]
      [:small "Measuring web framework popularity so you can find interesting frameworks to check out"]]]]
   [:div.row
    [:div.col-md-7
     [:h1 "sup"]]
    [:div.col-md-5 "yo"]]))

(defroutes home-routes
  (GET "/" [] (home)))
