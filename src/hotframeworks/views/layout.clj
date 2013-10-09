(ns hotframeworks.views.layout
  (:require [hiccup.page :refer [html5 include-css]]
            [hotframeworks.models.db :refer [all-languages-by-name]]))

(defn language-menu []
  (let [languages (all-languages-by-name)]
    (into [] (concat
               [:ul.dropdown-menu {:aria-labelledby "languages"}
                 (map (fn [language]
                      (let [{:keys [name url_identifier]} language]
                        [:li [:a {:href (str "/languages/" url_identifier)} name]]))
                 languages)]))))

(defn common [& content]
  (html5
   [:head
    [:title "Web framework rankings | HotFrameworks"]
    [:meta
     {:content "width=device-width, initial-scale=1.0",
      :name "viewport"}]
    (include-css "/css/bootstrap.min.css")
    (include-css "/css/hotframeworks.css")
    "<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->"
    "<!--[if lt IE 9]>"
    "<script src=\"/js/html5shiv.js\"></script>"
    "<script src=\"/js/respond.min.js\"></script>"
    "![endif]-->"]
   [:body
    [:div.navbar.navbar-default.navbar-fixed-top
     [:div.container
      [:div.navbar-header
       [:a.navbar-brand {:href "/"} "HotFrameworks"]
       [:button.navbar-toggle
        {:data-target "#navbar-main",
         :data-toggle "collapse",
         :type "button"}
        [:span.icon-bar]
        [:span.icon-bar]
        [:span.icon-bar]]]
      [:div#navbar-main.navbar-collapse.collapse
       [:ul.nav.navbar-nav
        [:li [:a {:href "/rankings"} "Rankings"]]
        [:li.dropdown
         [:a#download.dropdown-toggle
          {:href "#", :data-toggle "dropdown"}
          "Languages "
          [:span.caret]]
         (language-menu)]
        [:li [:a {:href "/faq"} "FAQ"]]]]]]
    [:div {:class "container"} content]
    [:script {:src "//code.jquery.com/jquery.js"}]
    [:script {:src "/js/bootstrap.min.js"}]]))
