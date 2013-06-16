(ns gratefulplace.controllers.topics
  (:require [datomic.api :as d]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c]
            [cemerick.friend :as friend])
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.utils))

(def index-topic-mapify-options
  {:include (merge {:first-post {}}
                   author-inclusion-options)})

(defmapifier record
  mr/ent->topic
  {:include {:posts {:include author-inclusion-options}}})

(defn query
  [params]
  (reverse-by :last-posted-to-at
              (map #(c/mapify
                     %
                     mr/ent->topic
                     index-topic-mapify-options)
                   (db/all :topic/first-post [:content/deleted false]))))

(defn show
  [params]
  (if-let [topic (record (id))]
    {:body topic}
    NOT-FOUND))

(defn create!
  [params auth]
  (protect
   (:id auth)
   
   (if-valid
    params validations/topic errors
    (let [topic-tempid (d/tempid :db.part/user -1)
          post-tempid (d/tempid :db.part/user -2)
          author-id (:id auth)
          topic (remove-nils-from-map
                 {:topic/title (:title params)
                  :topic/first-post post-tempid
                  :topic/last-posted-to-at (now)
                  :content/author author-id
                  :content/deleted false
                  :db/id topic-tempid})
          post {:post/content (:content params)
                :post/topic topic-tempid
                :post/created-at (now)
                :content/author author-id
                :db/id post-tempid}]
      {:body (mapify-tx-result
              (db/t [topic post])
              topic-tempid
              mr/ent->topic
              index-topic-mapify-options)
       :status 200})
    (invalid errors))))

(defn delete!
  [params auth]
  (let [id (id)]
    (protect
     (can-modify-record? (record id) auth)
     (db/t [{:db/id id
             :content/deleted true}])
     OK)))