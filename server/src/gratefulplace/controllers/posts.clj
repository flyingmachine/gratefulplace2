(ns gratefulplace.controllers.posts
  (:require [datomic.api :as d]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.transactions.posts :as tx]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.mapification :refer :all]
            [flyingmachine.cartographer.core :as c]
            [com.flyingmachine.liberator-templates.sets.json-crud
             :refer (defupdate! defcreate! defdelete!)])
  (:use gratefulplace.controllers.shared
        gratefulplace.models.permissions
        flyingmachine.webutils.utils))

(defmapifier record  mr/ent->post {:include author-inclusion-options})

(defupdate!
  :invalid? (validator params (:update validations/post))
  :authorized? (can-update-record? (record (id)) auth)
  :put! (update-record params tx/update-post)
  :return (fn [_] (record (id))))

(defcreate!
  :invalid? (validator params (:create validations/post))
  :authorized? (logged-in? auth)
  :post! (create-content tx/create-post params auth record)
  :return record-in-ctx)

(defdelete!
  :authorized? (can-delete-record? (record (id)) auth)
  :delete! delete-record-in-ctx)