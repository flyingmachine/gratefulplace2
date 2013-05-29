(ns gratefulplace.controllers.topics
  (:require [datomic.api :as d]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.serializers :as ss]
            [flyingmachine.serialize.core :as s]
            [cemerick.friend :as friend])
  (:use gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.utils))

(def index-topic-serialize-options
  {:include (merge {:first-post {}}
                   author-inclusion-options)})

(defn record
  [id]
  (s/serialize
   (db/ent id)
   ss/ent->topic
   {:include {:posts {:include author-inclusion-options}}}))

(defn query
  [params]
  (reverse-by :last-posted-to-at
              (map #(s/serialize
                     %
                     ss/ent->topic
                     index-topic-serialize-options)
                   (db/all :topic/first-post [:content/deleted false]))))

(defn show
  [params]
  {:body (record (str->int (:id params)))})

(defn create!
  [params auth]
  (println auth)
  (let [topic-tempid (d/tempid :db.part/user -1)
        post-tempid (d/tempid :db.part/user -2)
        author-id (:id auth)
        topic (remove-nils-from-map {:topic/title (:title params)
                                     :topic/first-post post-tempid
                                     :topic/last-posted-to-at (java.util.Date.)
                                     :content/author author-id
                                     :db/id topic-tempid})]
    {:body (serialize-tx-result
            (db/t [topic
                   {:post/content (:content params)
                    :post/topic topic-tempid
                    :post/created-at (java.util.Date.)
                    :content/author author-id
                    :db/id post-tempid}])
            topic-tempid
            ss/ent->topic
            index-topic-serialize-options)
     :status 200}))

(defn delete!
  [params auth]
  (let [id (str->int (:id params))]
    (protect
     (can-modify-record?)
     (db/t [{:db/id id
             :content/deleted true}])
     OK)))