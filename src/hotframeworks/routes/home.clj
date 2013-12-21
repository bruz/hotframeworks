(ns hotframeworks.routes.home
  (:require [compojure.core :refer :all]
            [clojure.data.json :as json]
            [hotframeworks.views.layout :as layout]
            [hotframeworks.models.db :as db]
            [hotframeworks.models.graphs :as graphs]
            [hotframeworks.models.framework :as framework]))

(defn mini-ranking [frameworks]
  [:table {:class "table table-striped"}
   [:thead
    [:tr
     [:th "Framework"]
     [:th.score "Score"]
     [:th.score "Weekly Change"]]]
   [:tbody
    (map (fn [framework]
           [:tr
            [:td (:name framework)]
            [:td.score (:latest_score framework)]
            [:td.score (:latest_delta framework)]])
         frameworks)]])

(defn full-rankings []
  (let [frameworks (framework/latest-scores)]
    [:table {:class "table table-striped"}
     [:thead
      [:tr
       [:th "Framework"]
       [:th.score "Github Score"]
       [:th.score "Link Score"]
       [:th.score "Traffic Score"]
       [:th.score "Stackoverflow Score"]
       [:th.score "Overall Score"]
       [:th.score "Weekly Change"]]]
     [:tbody
      (map (fn [framework]
             [:tr
              [:td.score (:name framework)]
              [:td.score (:github framework)]
              [:td.score (:links framework)]
              [:td.score (:traffic framework)]
              [:td.score (:stackoverflow framework)]
              [:td.score (:combined framework)]
              [:td.score (:delta framework)]])
           frameworks)]]))

(defn top-frameworks-data []
  (json/write-str
   (graphs/most-popular 10 10)))

(defn languages-data []
  (into {}
        (map (fn [language]
               [(:id language)
                {:name (:name language)
                 :data (graphs/for-language language 10)}])
             (db/all-languages-by-name))))

(defn graph-data-json []
  (json/write-str
   {:topFrameworks (graphs/most-popular 10 10)
    :languages (languages-data)}))

(defn home []
  (layout/common
   [:div
    [:div#top-frameworks.row
     [:div#about.col-md-12.page-header
      [:h1 "Find your new favorite web framework"
       [:br]
       [:small "Measuring web framework popularity so you can find interesting frameworks to check out"]]]]
    [:div.row
     [:div.col-md-9
      [:div#graph-container
       [:div#graph]]]
     [:div.col-md-3
      [:div#legend]]]
    [:div#rankings.row
     [:div.col-md-12.page-header
      [:h1 "Rankings"]
      (full-rankings)]]
    [:div#languages
     [:h1 "Languages"]
     (map (fn [language]
            (let [name (:name language)]
              [:div.row {:id (str "language-" (:id language))}
               [:h2 name]
               [:div.col-md-4
                (mini-ranking (db/frameworks-for-language language))]
               [:div.col-md-6
                [:div {:id (str "language-chart-" (:id language))}]]
               [:div.col-md-2
                [:div {:id (str "language-legend-" (:id language))}]]]))
            (db/all-languages-by-name))]]
   (format "var data = %s;
            Hotframeworks.graphAll(data);"
           (graph-data-json))))

(defroutes home-routes
  (GET "/" [] (home)))
