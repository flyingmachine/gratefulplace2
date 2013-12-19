(ns gratefulplace.controllers.posts
  (:require [datomic.api :as d]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.transactions.posts :as tx]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.mapification :refer :all]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.lib.liberator-templates :refer (defupdate! defcreate! defdelete!)])
  (:use [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        flyingmachine.webutils.utils))

(defmapifier record  mr/ent->post {:include author-inclusion-options})


(defupdate!
  [params auth]
  :valid? (validator params (:update validations/post))
  :authorized? (can-update-record? (record (id)) auth)
  :put! (update-record params tx/update-post)
  :return (fn [_] (record (id))))

(defcreate!
  [params auth]
  :valid? (validator params (:create validations/post))
  :authorized? (logged-in? auth)
  :post! (create-content tx/create-post params auth record)
  :return record-in-ctx)

(defdelete!
  [params auth]
  :authorized? (can-delete-record? (record (id)) auth)
  :delete! delete-record-in-ctx)