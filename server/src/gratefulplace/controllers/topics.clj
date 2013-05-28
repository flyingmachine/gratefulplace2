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
  {:body (s/serialize
          (db/ent (str->int (:id params)))
          ss/ent->topic
          {:include {:posts {:include author-inclusion-options}}})})

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
     (can-modify-record? (record id) auth)
     (db/t [{:db/id id
             :content/deleted true}])
     OK)))