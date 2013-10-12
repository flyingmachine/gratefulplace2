(ns gratefulplace.db.transactions
  (:require [datomic.api :as d]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.mapification :refer [mapify-tx-result]]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.utils :refer :all]))

(defn create-watch
  [params]
  (let [watch-tempid (d/tempid :db.part/user -1)]
    {:result (db/t [{:db/id watch-tempid
                     :watch/unread-count 0
                     :watch/topic (:topic-id params)
                     :watch/user (:user-id params)}])
     :tempid watch-tempid}))

(defn create-user
  [params]
  (let [params (merge {:user/preferences ["receive-watch-notifications" "receive-new-topic-notifications" "receive-weekly-digest"] }
                      (remove-nils-from-map (c/mapify params mr/user->txdata)))]
    {:result (db/t [params])
     :tempid (:db/id params)}))

