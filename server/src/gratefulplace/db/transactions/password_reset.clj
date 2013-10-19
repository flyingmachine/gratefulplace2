(ns gratefulplace.db.transactions.password-reset
  (:require [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.mapification :refer :all]
            [gratefulplace.db.query :as db]
            [gratefulplace.email.sending.senders :as email]
            [flyingmachine.cartographer.core :as c]
            [crypto.random]
            [gratefulplace.utils :refer :all]))

(defn generate-token
  []
  (crypto.random/url-part 10))

(defn create-token
  [user]
  (db/t [{:db/id (:db/id user)
          :user/password-reset-token (generate-token)
          :user/password-reset-token-generated-at (now)}]))