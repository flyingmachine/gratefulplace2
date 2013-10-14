(ns gratefulplace.db.transactions.users
  (:require [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.query :as db]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.utils :refer :all]))

(def preferences ["receive-watch-notifications"
                  "receive-new-topic-notifications"])

(def default-preferences ["receive-watch-notifications"
                          "receive-new-topic-notifications"])

(defn create-user
  [params]
  (let [params (merge {:user/preferences default-preferences}
                      (remove-nils-from-map (c/mapify params mr/user->txdata)))]
    {:result (db/t [params])
     :tempid (:db/id params)}))

