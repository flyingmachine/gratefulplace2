(ns gratefulplace.db.transactions.watches
  (:require [com.flyingmachine.datomic-junk :as dj]
            [datomic.api :as d]))

(defn reset-watch-count
  [topic user]
  (let [watch (dj/one [:watch/topic topic] [:watch/user user])]
    (dj/t [{:db/id (:db/id watch) :watch/unread-count 0}])))

(defn create-watch
  [params]
  (let [watch-tempid (d/tempid :db.part/user -1)]
    {:result (dj/t [{:db/id watch-tempid
                     :watch/unread-count 0
                     :watch/topic (:topic-id params)
                     :watch/user (:user-id params)}])
     :tempid watch-tempid}))