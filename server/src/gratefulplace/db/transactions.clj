(ns gratefulplace.db.transactions
  (:require [datomic.api :as d]
            [gratefulplace.db.query :as db])
  (:use gratefulplace.utils))

(defn create-post
  [params]
  (let [post-tempid (d/tempid :db.part/user -1)
        topic-id (:topic-id params)
        post (remove-nils-from-map {:post/content (:content params)
                                    :post/topic topic-id
                                    :post/created-at (java.util.Date.)
                                    :content/author (:author-id params)
                                    :db/id post-tempid})]
    
    {:result (db/t [post
                    {:db/id topic-id :topic/last-posted-to-at (java.util.Date.)}
                    [:db/fn :increment-watch-count topic-id]])
     :post-tempid post-tempid}))