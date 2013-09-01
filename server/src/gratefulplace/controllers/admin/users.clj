(ns gratefulplace.controllers.admin.users
  (:require [gratefulplace.db.query :as db]
            [datomic.api :as d]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.mapification :refer :all]
            [gratefulplace.controllers.shared :refer :all]
            [gratefulplace.models.permissions :refer [moderator?]]
            [gratefulplace.utils :refer :all]
            [flyingmachine.cartographer.core :as c]
            [liberator.core :refer [defresource]]))

(defmapifier record mr/ent->user)

(defresource query [params auth]
  :authorized? (moderator? auth)
  :available-media-types ["application/json"]
  :handle-ok (fn [_]
               (map record
                    (db/all :user/username))))
