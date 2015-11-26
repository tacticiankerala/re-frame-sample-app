(ns github-profile.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]))


(defn loading-throbber
  []
  (let [loading? (re-frame/subscribe [:loading?])]
    (when @loading?
      [:div.loading
       [:div.three-quarters-loader "Loading..."]])))

(defn github-id-input
  []
  (let [loading? (re-frame/subscribe [:loading?])
        error? (re-frame/subscribe [:error?])
        github-id (reagent/atom "")
        on-click (fn [_]
                   (when-not (empty? @github-id)
                     (re-frame/dispatch [:set-github-id @github-id])
                     (reset! github-id "")))]
    (fn []
      [:div
       [:div.input-group
        [:input.form-control {:type "text"
                              :placeholder "Enter Github ID"
                              :on-change #(reset! github-id (-> % .-target .-value))}]
        [:span.input-group-btn
         [:button.btn.btn-default {:type "button"
                                   :on-click #(when-not @loading? (on-click %))}
          "Go"]
         ]]
       (when @error?
         [:p.error-text.text-danger "¯\\_(ツ)_/¯  Bad github handle or rate limited!"])])))

(defn user-name-and-avatar
  []
  (fn []
    (let [user-profile (re-frame/subscribe [:user-profile])]
      [:div.user-details
       [:img.img-circle {:src (get @user-profile "avatar_url")}]
       [:h5.text-center (get @user-profile "name")]])))

(defn user-repos-list
  []
  (let [user-repos (re-frame/subscribe [:user-repos])]
    (fn []
      [:ul.list-group
       (map-indexed (fn [i repo]
                      (vector :li.list-group-item {:key i}
                              [:h4.list-group-item-heading (get repo "name")]
                              [:p.list-group-item-text (get repo "description")]))
                    @user-repos)])))
;; home
(defn home-panel []
  (let []
    (fn []
      [:div
       [:div.topbar
        [:div.container
         [:div.row
          [:div.col-lg-6.col-lg-offset-3
           [github-id-input]]]]]
       [:div.main-content
        [:div.container
         [:div.row
          [:div.col-lg-6.col-lg-offset-3
           [user-repos-list]]]]]
       ])))


;; profile

(defn profile-panel []
  (fn []
    [:div "This is the Profile Page."
     [:div [:a {:href "#/"} "go to Home Page"]]
     ]))


;; main

(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :profile-panel [] [profile-panel])
(defmethod panels :default [] [:div])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [:div
       [loading-throbber]
       [user-name-and-avatar]
       (panels @active-panel)
       ])))
