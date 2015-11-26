(ns github-profile.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [github-profile.handlers]
            [github-profile.subs]
            [github-profile.routes :as routes]
            [github-profile.views :as views]))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))


(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
