(ns gratefulplace.db.transactions.posts
  (:require [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.query :as db]
            [gratefulplace.email.sending.senders :as email]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.utils :refer :all]))

(defn users-to-notify-of-post
  [topic-id author-id]
  (db/ents (db/q (conj '[:find ?u :where]
                       (conj '[?w :watch/topic] topic-id)
                       '[?w :watch/user ?u]
                       '[?u :user/preferences "receive-watch-notifications"]
                       [(list 'not= '?u author-id)]))))

(defn- notify-users-of-post
  [result params]
  (let [{:keys [topic-id author-id]} params
        users (users-to-notify-of-post topic-id author-id)
        topic (c/mapify (db/ent topic-id) mr/ent->topic)]
        (email/send-reply-notification users topic params)))

(defn- after-create-post
  [result params]
  (future (notify-users-of-post result params)))

(defn create-post
  [params]
  (let [{:keys [topic-id author-id]} params
        post (remove-nils-from-map (c/mapify params mr/post->txdata))
        result {:result (db/t [post
                               {:db/id topic-id
                                :topic/last-posted-to-at (:post/created-at post)}
                               [:increment-watch-count topic-id author-id]])
                :tempid (:db/id post)}]
    (after-create-post result params)
    result))

(defn update-post
  [params]
  (db/t [(c/mapify params mr/post->txdata {:only [:db/id :post/content]})]))