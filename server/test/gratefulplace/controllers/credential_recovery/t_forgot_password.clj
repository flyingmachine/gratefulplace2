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

(fact "consumes token with update"
  )