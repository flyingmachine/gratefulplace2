(ns gratefulplace.db.transactions.users
  (:require [gratefulplace.db.maprules :as mr]
            [com.flyingmachine.datomic-junk :as dj]
            [flyingmachine.cartographer.core :as c]
            [flyingmachine.webutils.utils :refer :all]))

(def preferences ["receive-watch-notifications"
                  "receive-new-topic-notifications"])

(def default-preferences ["receive-watch-notifications"
                          "receive-new-topic-notifications"])

(defn create-user
  [params]
  (let [params (merge {:user/preferences default-preferences}
                      (remove-nils-from-map (c/mapify params mr/user->txdata)))]
    {:result (dj/t [params])
     :tempid (:db/id params)}))

