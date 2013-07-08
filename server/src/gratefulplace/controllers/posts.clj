(ns gratefulplace.controllers.posts
  (:require [datomic.api :as d]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.transactions :as ts]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c])
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.utils))

(defmapifier record
  mr/ent->post
  {:include author-inclusion-options
   :exclude [:content :created-at :topic-id]})

(defresource update! [params auth]
  :allowed-methods [:put :post]
  :available-media-types ["application/json"]

  :malformed? (validator params (:update validations/post))
  :handle-malformed errors-in-ctx

  :authorized? (can-update-record? (record (id)) auth)
  :exists? record-in-ctx
  :put! (fn [_] (db/t [(c/mapify params mr/post->txdata)]))
  :post! (fn [_] (db/t [(c/mapify params mr/post->txdata)]))
  :new? false
  :respond-with-entity? true
  :handle-ok (fn [_] (record (id))))

(defresource create! [params auth]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :authorized? (logged-in? auth)

  :malformed? (validator params (:create validations/post))
  :handle-malformed errors-in-ctx

  ;; TODO possibly change mapify tx result so that it takes a certain
  ;; kind of record that has all the details it needs?
  :post! (fn [_]
           (let [{:keys [result post-tempid]} (ts/create-post (merge params {:author-id (:id auth)}))]
             {:record
              (mapify-tx-result
               result
               post-tempid
               mr/ent->post
               {:include author-inclusion-options})}))
  :handle-created record-in-ctx)

(defresource delete! [params auth]
  :allowed-methods [:delete]
  :available-media-types ["application/json"]
  :authorized? (can-delete-record? (record (id)) auth)
  :exists? exists-in-ctx?
  :delete! delete-record-in-ctx)