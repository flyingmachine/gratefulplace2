(ns gratefulplace.db.transactions.topics
  (:require [datomic.api :as d]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.mapification :refer :all]
            [gratefulplace.db.query :as db]
            [gratefulplace.email.sending.senders :as email]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.utils :refer :all]))

(defmapifier record mr/ent->topic {:include [:first-post]})

(defmapifier topic-txdata* mr/topic->txdata)
(def topic-txdata (comp remove-nils-from-map topic-txdata*))

(defmapifier watch-txdata mr/watch->txdata)

(defmapifier post-txdata mr/post->txdata)

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

(defn- add-create-params
  [params]
  (merge params (db/tempids :topic-id :post-id :watch-id)))

(defn- topic-transaction-data
  [params]
  (map #(% params) [topic-txdata watch-txdata post-txdata]))

(defn create-topic
  [params]
  (let [final-params (add-create-params params)
        result {:result (-> final-params
                            topic-transaction-data
                            db/t)
                :tempid (:topic-id final-params)}]
    (after-create-topic result final-params)
    result))