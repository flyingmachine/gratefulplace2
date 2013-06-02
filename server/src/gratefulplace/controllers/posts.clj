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

(defserialization record
  ss/ent->post
  {:include author-inclusion-options
   :exclude [:content :created-at :topic-id]})

(defn update!
  [params auth]
  (let [retrieve-record (fn [] (record (id)))]
    (protect
     (can-modify-record? (retrieve-record) auth)
     (if-valid
      params validations/post errors
      (do
        (db/t [(s/serialize params ss/post->txdata)])
        {:body (retrieve-record)})
      (invalid errors)))))

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
  (let [id (id)]
    (protect
     (can-modify-record? (record id) auth)
     (db/t [{:db/id id
             :content/deleted true}])
     OK)))