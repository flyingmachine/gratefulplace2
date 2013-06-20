(ns gratefulplace.controllers.topics
  (:require [datomic.api :as d]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c]
            [cemerick.friend :as friend])
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.utils))

(def index-mapify-options
  {:include (merge {:first-post {}}
                   author-inclusion-options)})

(defn index-mapify
  [ent]
  (c/mapify
   ent
   mr/ent->topic
   index-mapify-options))

(defmapifier record
  mr/ent->topic
  {:include {:posts {:include author-inclusion-options}}})

(defresource query
  :available-media-types ["application/json"]
  :handle-ok (fn [ctx]
               (reverse-by :last-posted-to-at
                           (map index-mapify
                                (db/all :topic/first-post [:content/deleted false])))))

(defresource show [params]
  :available-media-types ["application/json"]
  :exists? (fn [ctx]
             (if-let [topic (record (id))]
               {:record topic}))
  :handle-ok (fn [ctx]
               (get ctx :record)))

(defresource create! [params auth]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :authorized? (fn [_] (:id auth))
  :malformed? (validator params validations/topic)
  :handle-created
  (fn [_]
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
      (mapify-tx-result
       (db/t [topic post])
       topic-tempid
       mr/ent->topic
       index-mapify-options)))

  :handle-malformed (fn [ctx]
                      {:errors (:errors ctx)}))

(defresource delete! [params auth]
  :exists? (fn [ctx]
             (if-let [topic (record (id))]
               {:record topic}))
  :allowed-methods [:delete]
  :authorized? (fn [_] (can-modify-record? (record (id)) auth))
  :handle-accepted (fn [_] (db/t [{:db/id (id)
                                  :content/deleted true}])))