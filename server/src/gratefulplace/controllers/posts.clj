(ns gratefulplace.controllers.posts
  (:require [datomic.api :as d]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.transactions.posts :as tx]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.mapification :refer :all]
            [flyingmachine.cartographer.core :as c])
  (:use [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.utils))

(defmapifier record  mr/ent->post {:include author-inclusion-options})

(defresource update! [params auth]
  :allowed-methods [:put :post]
  :available-media-types ["application/json"]

  :malformed? (validator params (:update validations/post))
  :handle-malformed errors-in-ctx

  :authorized? (can-update-record? (record (id)) auth)
  :exists? record-in-ctx
  :put! (update-record params tx/update-post)
  :post! (update-record params tx/update-post)
  :new? false
  :respond-with-entity? true
  :handle-ok (fn [_] (record (id))))

(defresource create! [params auth]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :authorized? (logged-in? auth)

  :malformed? (validator params (:create validations/post))
  :handle-malformed errors-in-ctx

  :post! (create-content tx/create-post params auth record)
  :handle-created record-in-ctx)

(defresource delete! [params auth]
  :allowed-methods [:delete]
  :available-media-types ["application/json"]
  :authorized? (can-delete-record? (record (id)) auth)
  :exists? exists-in-ctx?
  :delete! delete-record-in-ctx)