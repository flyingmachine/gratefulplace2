(ns gratefulplace.controllers.credential-recovery.t-forgot-username
  (:require [com.flyingmachine.datomic-junk :as dj])
  (:use midje.sweet
        gratefulplace.controllers.test-helpers))

(setup-db-background)

(defn email
  []
  (:user/email (dj/ent (:id (auth)))))

(fact "when given an existing email, returns ok"
  (res :post "/credential-recovery/forgot-username" {:email (email)})
  => (contains {:status 201}))

(fact "when give a nonexistant email, returns error"
  (res :post "/credential-recovery/forgot-username" {:email "nobody@nobody.com"})
  => (contains {:status 404}))

(fact "when given an invalid input, returns error"
  (res :post "/credential-recovery/forgot-username" {:email ""})
  => (contains {:status 400}))