(ns gratefulplace.db.transactions
  (:require [datomic.api :as d]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.mapification :refer [mapify-tx-result]]
            [gratefulplace.models.mailer :as mailer]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.utils :refer :all]))

(defn users-to-notify-of-post
  [topic-id author-id]
  (db/ents (db/q (conj '[:find ?u :where]
                       (conj '[?w :watch/topic] topic-id)
                       '[?w :watch/user ?u]
                       '[?u :user/preferences "receive-watch-notifications"]
                       [(list 'not= '?u author-id)]))))

(defn create-post
  [params]
  (let [{:keys [topic-id author-id]} params
        post-tempid (d/tempid :db.part/user -1)
        now (now)
        post (remove-nils-from-map {:post/content (:content params)
                                    :post/topic topic-id
                                    :post/created-at now
                                    :content/author author-id
                                    :db/id post-tempid})
        result (db/t [post
                      {:db/id topic-id :topic/last-posted-to-at now}
                      [:increment-watch-count topic-id author-id]])
        record (mapify-tx-result {:result result
                                  :tempid post-tempid})]

    ;; TODO find a better home for this
    (future
      (let [users (users-to-notify-of-post topic-id author-id)
            topic (c/mapify (db/ent topic-id) mr/ent->topic {:except [:last-posted-to-at]})]
        (doseq [user users]
          (mailer/send-reply-notification user params topic))))
    record))

(defn update-post
  [params]
  (fn [_] (db/t [(c/mapify params mr/post->txdata)])))

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
        result (db/t [topic post watch])]

    ;; TODO find a better home for this
    ;; get topic reuslt here and actualize it
    (comment
      (future
        (let [topic (c/mapify (db/ent topic-id) mr/ent->topic {:except [:last-posted-to-at]})]
          (doseq [watch watches]
            (let [user (c/mapify (:watch/user watch) mr/ent->user)]
              (if (and
                   (get-in user [:preferences "receive-watch-notifications"])
                   (not= author-id (:id user)))
                (mailer/send-reply-notification user params topic)))))))
    
    {:result result
     :tempid topic-tempid}))

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

(defn reset-watch-count
  [topic user]
  (let [watch (db/one [:watch/topic topic] [:watch/user user])]
    (db/t [{:db/id (:db/id watch) :watch/unread-count 0}])))