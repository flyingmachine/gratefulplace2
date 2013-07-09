(ns gratefulplace.controllers.topics
  (:require [datomic.api :as d]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.transactions :as ts]
            [flyingmachine.cartographer.core :as c])
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.utils))

(def query-mapify-options
  {:include (merge {:first-post {}}
                   author-inclusion-options)})

(defmapifier query-record
  mr/ent->topic
  query-mapify-options)

(defmapifier record
  mr/ent->topic
  {:include {:posts {:include author-inclusion-options}
             :watches {}}})

(defresource query [params]
  :available-media-types ["application/json"]
  :handle-ok (fn [_]
               (reverse-by :last-posted-to-at
                           (map query-record
                                (db/all :topic/first-post [:content/deleted false])))))

(defresource show [params auth]
  :available-media-types ["application/json"]
  :exists? (exists? (record (id)))
  :handle-ok (fn [ctx]
               (if auth
                 (ts/reset-watch-count (id) (:id auth)))
               (record-in-ctx ctx)))

(defresource create! [params auth]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :authorized? (logged-in? auth)

  :malformed? (validator params validations/topic)
  :handle-malformed errors-in-ctx
  
  :post! (fn [_]
           (let [{:keys [result tempid]} (ts/create-topic (merge params {:author-id (:id auth)}))]
             {:record (mapify-tx-result
                       result
                       tempid
                       mr/ent->topic
                       query-mapify-options)}))
  :handle-created record-in-ctx)

(defresource delete! [params auth]
  :allowed-methods [:delete]
  :available-media-types ["application/json"]
  :authorized? (can-delete-record? (record (id)) auth)
  :exists? exists-in-ctx?
  :delete! delete-record-in-ctx)