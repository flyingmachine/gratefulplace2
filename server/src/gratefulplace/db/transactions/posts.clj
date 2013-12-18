(ns gratefulplace.db.transactions.posts
  (:require [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.mapification :refer :all]
            [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.email.sending.senders :as email]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.utils :refer :all]))

(defmapifier post-params->txdata* mr/post->txdata)
(def post-params->txdata (comp remove-nils-from-map post-params->txdata*))

(defmapifier watch-params->txdata mr/watch->txdata)

(defn users-to-notify-of-post
  [topic-id author-id]
  (dj/ents (dj/q {:find '[?u]
                  :where [['?w :watch/topic topic-id]
                          ['?w :watch/user '?u]
                          ['?u :user/preferences "receive-watch-notifications"]
                          [(list 'not= '?u author-id)]]})))

(defn- notify-users-of-post
  [result params]
  (let [{:keys [topic-id author-id]} params
        users (users-to-notify-of-post topic-id author-id)
        topic (c/mapify (dj/ent topic-id) mr/ent->topic)]
    (email/send-reply-notification users topic params)))

(defn- after-create-post
  [result params]
  (future (notify-users-of-post result params)))

(defn- add-create-params
  [params]
  (merge params (dj/tempids :watch-id)))

(defn create-post
  [params]
  (let [final-params (add-create-params params)
        {:keys [topic-id author-id]} final-params
        post (post-params->txdata final-params)
        result {:result (dj/t [post
                               {:db/id topic-id
                                :topic/last-posted-to-at (:post/created-at post)}
                               (watch-params->txdata final-params)
                               [:increment-watch-count topic-id author-id]])
                :tempid (:db/id post)}]
    (after-create-post result final-params)
    result))

(defn update-post
  [params]
  (dj/t [(c/mapify params mr/post->txdata {:only [:db/id :post/content]})]))