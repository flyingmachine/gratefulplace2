(ns gratefulplace.controllers.posts
  (:require [datomic.api :as d]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.serializers :as ss]
            [gratefulplace.db.serialize :as s]
            [cemerick.friend :as friend])
  (:use gratefulplace.controllers.shared
        gratefulplace.utils))

(defn create!
  [params]
  (let [post-tempid (d/tempid :db.part/user -1)
        topic-id (:topic params)
        author-id (:id (friend/current-authentication))
        post (remove-nils-from-map {:post/content (:content params)
                                    :post/topic topic-id
                                    :post/created-at (java.util.Date.)
                                    :content/author author-id
                                    :db/id post-tempid})]
    {:body
     (-> (db/t [post
                {:db/id topic-id
                 :topic/last-posted-to-at (java.util.Date.)}])
         deref
         :tempids
         (db/resolve-tempid post-tempid)
         db/ent
         (s/serialize ss/ent->post {:include {:author {:exclude [:email :password]}}}))}))