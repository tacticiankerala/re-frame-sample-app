(ns github-profile.db)

(def default-db
  {
   :loading? false
   :error false
   :name "github profile"
   :user {
          :profile {}
          :repos []
          }
   })
