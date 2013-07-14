(ns gratefulplace.config)

(def conf
  {:html-paths ["resources/html-app"
                "../html-app/app"
                "../html-app/.tmp"]
   :datomic {:db-uri "datomic:free://localhost:4334/gp2"
             :test-uri "datomic:mem://gp2"}
   :moderator-names ["flyingmachine"]})

(defn config
  [& keys]
  (get-in conf keys))