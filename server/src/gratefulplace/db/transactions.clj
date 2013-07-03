(ns gratefulplace.db.transactions
  (:require [datomic.api :as d]
            [gratefulplace.db.query :as db])
  (:use gratefulplace.utils))

(defn create-post
  [params]
  (let [{:keys [topic-id author-id]} params
        post-tempid (d/tempid :db.part/user -1)
        now (java.util.Date.)
        post (remove-nils-from-map {:post/content (:content params)
                                    :post/topic topic-id
                                    :post/created-at now
                                    :content/author author-id
                                    :db/id post-tempid})]
    
    {:result (db/t [post
                    {:db/id topic-id :topic/last-posted-to-at now}
                    [:increment-watch-count topic-id author-id]])
     :post-tempid post-tempid}))

(defn reset-watch-count
  [topic user]
  (let [watch (db/one [:watch/topic topic] [:watch/user user])]
    (db/t [{:db/id (:db/id watch) :watch/unread-count 0}])))