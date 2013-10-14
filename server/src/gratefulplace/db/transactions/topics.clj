(ns gratefulplace.db.transactions.topics
  (:require [datomic.api :as d]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.mapification :refer :all]
            [gratefulplace.db.query :as db]
            [gratefulplace.email.sending.senders :as email]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.utils :refer :all]))

(defmapifier record mr/ent->topic {:include [:first-post]})

;: TODO refactor with post notification query
(defn users-to-notify-of-topic
  [author-id]
  (db/ents (db/q (conj '[:find ?u :where]
                       '[?u :user/preferences "receive-new-topic-notifications"]
                       [(list 'not= '?u author-id)]))))

(defn- notify-users-of-topic
  [result params]
  (let [{:keys [topic-id author-id]} params
        users (users-to-notify-of-topic author-id)
        topic (mapify-tx-result result record)]
    (email/send-new-topic-notification users topic)))

(defn- after-create-topic
  [result params]
  (future (notify-users-of-topic result params)))

(defn create-topic
  [params]
  (let [params (merge params (db/tempids :topic-id :post-id :watch-id))
        topic (remove-nils-from-map (c/mapify params mr/topic->txdata))
        watch (c/mapify params mr/watch->txdata)
        post (c/mapify params mr/post->txdata)]
    (let [result {:result (db/t [topic post watch])
                  :tempid (:topic-id params)}]
      (after-create-topic result params)
      result)))