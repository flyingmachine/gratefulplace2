(ns gratefulplace.controllers.topics
  (:require [datomic.api :as d]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c])
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.utils))

(def query-mapify-options
  {:include (merge {:first-post {}}
                   author-inclusion-options)})

(defn query-mapify
  [ent]
  (c/mapify
   ent
   mr/ent->topic
   query-mapify-options))

(defmapifier record
  mr/ent->topic
  {:include {:posts {:include author-inclusion-options}
             :watches {}}})

(defresource query
  :available-media-types ["application/json"]
  :handle-ok (fn [ctx]
               (reverse-by :last-posted-to-at
                           (map query-mapify
                                (db/all :topic/first-post [:content/deleted false])))))

(defresource show [params]
  :available-media-types ["application/json"]
  :exists? (exists? (record (id)))
  :handle-ok record-in-ctx)

(defresource create! [params auth]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :authorized? (logged-in? auth)

  :malformed? (validator params validations/topic)
  :handle-malformed errors-in-ctx
  
  :post! (fn [_]
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
             {:record
              (mapify-tx-result
               (db/t [topic post])
               topic-tempid
               mr/ent->topic
               query-mapify-options)}))
  :handle-created record-in-ctx)

(defresource delete! [params auth]
  :allowed-methods [:delete]
  :available-media-types ["application/json"]
  :authorized? (can-delete-record? (record (id)) auth)
  :exists? exists-in-ctx?
  :delete! delete-record-in-ctx)