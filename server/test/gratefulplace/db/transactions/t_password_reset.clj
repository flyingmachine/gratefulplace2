(ns gratefulplace.db.transactions.t-password-reset
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.mapification :refer :all]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.transactions.password-reset :as pr]
            [cemerick.friend.credentials :as friend])
  (:use midje.sweet
        gratefulplace.controllers.test-helpers))

(setup-db-background)
(defn user
  ([]
     (user "flyingmachine"))
  ([username]
     (q/one [:user/username username])))

(fact "generating a token and consuming it will change a password"
  (pr/create-token (user))
  (pr/consume-token (user) "newpass")
  (friend/bcrypt-verify "newpass" (:user/password (user)))
  => true)
