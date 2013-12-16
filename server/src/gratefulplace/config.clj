(ns gratefulplace.config
  (:require [gratefulplace.utils :refer :all]
            [environ.core :refer :all]))

(def conf
  (let [environment (or (env :app-env) "development")]
    (merge
     {:app-env environment
      :html-paths ["../html-app/.tmp"
                   "../html-app/app"
                   "html-app"]
      :datomic {:db-uri "datomic:free://localhost:4334/gp2"
                :test-uri "datomic:mem://gp2-test"}
      :moderator-names ["flyingmachine"]
      :send-email false
      :email {:host "smtp.gmail.com"
              :from-address "notifications@gratefulplace.com"
              :from-name "Grateful Place Notifications"
              :test-recipient "nonrecursive@gmail.com"
              :username (env :gp-email-username)
              :password (env :gp-email-password)}}
     (read-resource (str "config/environments/" environment ".edn")))))

(defn config
  [& keys]
  (get-in conf keys))