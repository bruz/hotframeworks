(ns hotframeworks.routes.home
  (:require [compojure.core :refer :all]
            [hotframeworks.views.layout :as layout]
            [hotframeworks.models.db :as db]))

(defn mini-ranking [frameworks]
  [:table {:class "table table-striped"}
   [:thead
    [:tr
     [:th "Framework"]
     [:th "Score"]
     [:th "Weekly Change"]]]
   [:tbody
    (map (fn [framework]
           [:tr
            [:td (:name framework)]
            [:td (:latest_score framework)]
            [:td (:latest_delta framework)]])
         frameworks)]])

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
    [:div.col-md-5
     (mini-ranking (db/most-popular-frameworks 10))]]))

(defroutes home-routes
  (GET "/" [] (home)))
