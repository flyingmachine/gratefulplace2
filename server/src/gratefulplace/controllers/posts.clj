(ns gratefulplace.controllers.posts
  (:require [datomic.api :as d]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.serializers :as ss]
            [flyingmachine.serialize.core :as s]
            [cemerick.friend :as friend])
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.utils))

(defn record
  [id]
  (s/serialize
   (db/ent id)
   ss/ent->post
   {:include author-inclusion-options
    :exclude [:content :created-at :topic-id]}))

(defn update!
  [params auth]
  (protect
   (can-modify-record? (record (str->int (:id params))) auth)
   (if-valid
    params validations/post errors
    (do
      (db/t [(s/serialize params ss/post->txdata)])
      OK)
    (invalid errors))))

(defn create!
  [params auth]
  (if-valid
   params validations/post errors
   (let [post-tempid (d/tempid :db.part/user -1)
         topic-id (:topic params)
         post (remove-nils-from-map {:post/content (:content params)
                                     :post/topic topic-id
                                     :post/created-at (java.util.Date.)
                                     :content/author (:id auth)
                                     :db/id post-tempid})]
     {:body (serialize-tx-result
             (db/t [post
                    {:db/id topic-id
                     :topic/last-posted-to-at (java.util.Date.)}])
             post-tempid
             ss/ent->post
             {:include author-inclusion-options})})
   (invalid errors)))

(defn delete!
  [params auth]
  (let [id (str->int (:id params))]
    (protect
     (can-modify-record? (record id) auth)
     (db/t [[:db.fn/retractEntity id]])
     OK)))