(ns github-profile.handlers
  (:require [ajax.core :refer [GET]]
            [re-frame.core :as re-frame]
            [github-profile.db :as db]))

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/register-handler
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/register-handler
 :set-github-id
 (fn [db [_ github-id]]
   (re-frame/dispatch [:fetch-gh-user-details github-id])
   (assoc-in db [:user :github-id] github-id)))

(re-frame/register-handler
 :fetch-gh-user-details
 (fn [db [_ github-id]]
   (ajax.core/GET
    (str "https://api.github.com/users/" github-id)
    {:handler       #(re-frame/dispatch [:process-user-response %1])
     :error-handler #(re-frame/dispatch [:bad-response %1])})
   (ajax.core/GET
    (str "https://api.github.com/users/" github-id "/repos?sort=updated")
    {:handler       #(re-frame/dispatch [:process-repo-response %1])
     :error-handler #(re-frame/dispatch [:bad-response %1])})
   (-> db
       (assoc :loading? true)
       (assoc :error false))))

(re-frame/register-handler
 :process-user-response
 (fn [db [_ response]]
   (-> db
       (assoc :loading? false)
       (assoc-in [:user :profile] (js->clj response)))))

(re-frame/register-handler
 :process-repo-response
 (fn [db [_ response]]
   (-> db
       (assoc :loading? false)
       (assoc-in [:user :repos] (js->clj response)))))

(re-frame/register-handler
 :bad-response
 (fn [db [_ _]]
   (-> db
       (assoc :loading? false)
       (assoc :error true)
       (assoc-in [:user :repos] [])
       (assoc-in [:user :profile] {}))))
