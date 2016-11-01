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

(defn common [content javascript]
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
        [:li [:a.scroll-link {:href "/#top-frameworks",} "Top Frameworks"]]
        [:li [:a.scroll-link {:href "/#rankings"} "Rankings"]]
        [:li.dropdown
         [:a#download.dropdown-toggle
          {:href "#", :data-toggle "dropdown"}
          "Languages "
          [:span.caret]]
         (language-menu)]
        [:li [:a {:href "/faq"} "FAQ"]]]]]]
    [:div {:class "container"} content]
    [:script {:src "//cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"}]
    [:script {:src "//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"}]
    [:script {:src "//cdnjs.cloudflare.com/ajax/libs/moment.js/2.15.2/moment.min.js"}]
    [:script {:src "//cdnjs.cloudflare.com/ajax/libs/Chart.js/2.3.0/Chart.min.js"}]
    [:script {:src "/js/app.js"}]
    (format "<script>
            %s
            </script>" javascript)
    [:script
     "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
     (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
     m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
     })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

     ga('create', 'UA-3473680-4', 'hotframeworks.com');
     ga('send', 'pageview');
     "]]))
