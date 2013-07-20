(ns gratefulplace.config
  (:use environ.core))

(def conf
  (merge-with
   merge
   {:html-paths ["html-app"
                 "../html-app/app"
                 "../html-app/.tmp"]
    :datomic {:db-uri "datomic:free://localhost:4334/gp2"
              :test-uri "datomic:mem://gp2"}
    :moderator-names ["flyingmachine"]
    :gp-email {:from-address "notifications@gratefulplace.com"
               :from-name "Grateful Place Notifications"}}
   {:gp-email {:username (env :gp-email-username)
               :password (env :gp-email-password)}}))

(defn config
  [& keys]
  (get-in conf keys))