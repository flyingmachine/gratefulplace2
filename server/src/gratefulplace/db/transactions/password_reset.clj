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

;; TODO handle case where token is already consumed?
(defn consume-token
  [user, new-password]
  (db/t (conj (map #(vector :db/retract (:db/id user) % (get user %))
                   [:user/password-reset-token
                    :user/password-reset-token-generated-at])
              (c/mapify {:id (:db/id user)
                         :new-password new-password}
                        mr/change-password->txdata))))