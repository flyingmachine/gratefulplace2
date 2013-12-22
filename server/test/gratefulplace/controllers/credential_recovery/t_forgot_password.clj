(ns gratefulplace.controllers.credential-recovery.t-forgot-password
  (:require [gratefulplace.controllers.credential-recovery.forgot-password :as fp]
            [com.flyingmachine.datomic-junk :as dj])
  (:use midje.sweet
        gratefulplace.paths
        gratefulplace.controllers.test-helpers))

(setup-db-background)

(fact "when given a valid username, creates token"
  (res :post "/credential-recovery/forgot-password" {:username (:username (auth))})
  (dj/ent-count :user/password-reset-token)
  => 1)

(facts "about token"
  (res :post "/credential-recovery/forgot-password" {:username (:username (auth))})
  (let [token (:user/password-reset-token (dj/one :user/password-reset-token))
        path (str "/credential-recovery/forgot-password/" token)]
    (fact "returns ok when token exists"
      (res :get path)
      => (contains {:status 200}))
    (fact "returns 501 when token doesn't exist"
      (res :get "/credential-recovery/forgot-password/xyz")
      => (contains {:status 400}))
    (fact "consumes token with update"
      (res :put path {:new-password "test1234" :new-password-confirmation "test1234"})
      (dj/ent-count :user/password-reset-token)
      => 0)))