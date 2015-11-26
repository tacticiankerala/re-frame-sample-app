(ns github-profile.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

(re-frame/register-sub
 :name
 (fn [db]
   (reaction (:name @db))))

(re-frame/register-sub
 :active-panel
 (fn [db _]
   (reaction (:active-panel @db))))

(re-frame/register-sub
 :github-id
 (fn [db]
   (reaction (get-in @db [:user :github-id]))))

(re-frame/register-sub
 :loading?
 (fn [db]
   (reaction (:loading? @db))))

(re-frame/register-sub
 :error?
 (fn [db]
   (reaction (:error @db))))


(re-frame/register-sub
 :user-profile
 (fn [db]
   (reaction (get-in @db [:user :profile]))))

(re-frame/register-sub
 :user-repos
 (fn [db]
   (reaction (get-in @db [:user :repos]))))
