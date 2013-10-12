(ns gratefulplace.db.transactions.topics
  (:require [datomic.api :as d]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.mapification :refer :all]
            [gratefulplace.db.query :as db]
            [gratefulplace.email.sending.senders :as email]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.utils :refer :all]))

(defmapifier record mr/ent->topic)

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
  (notify-users-of-topic result params))

(defn create-topic
  [params]
  (let [topic-tempid (d/tempid :db.part/user -1)
        post-tempid (d/tempid :db.part/user -2)
        watch-tempid (d/tempid :db.part/user -3)
        author-id (:author-id params)
        topic (remove-nils-from-map {:topic/title (:title params)
                                     :topic/first-post post-tempid
                                     :topic/last-posted-to-at (now)
                                     :content/author author-id
                                     :content/deleted false
                                     :db/id topic-tempid})
        watch {:db/id watch-tempid
               :watch/unread-count 0
               :watch/topic topic-tempid
               :watch/user author-id}
        post {:post/content (:content params)
              :post/topic topic-tempid
              :post/created-at (now)
              :content/author author-id
              :db/id post-tempid}
        result {:result (db/t [topic post watch])
                :tempid topic-tempid}]

    (after-create-topic result params)
    result))