(ns gratefulplace.controllers.likes
  (:require [datomic.api :as d]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c])
  (:use [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.utils))

(defresource create! [params auth]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :authorized? (logged-in? auth)

  :post! (fn [_]
           (db/t [{:db/id (d/tempid :db.part/user -1)
                   :like/user (:id auth)
                   :like/post (:post-id params)}]))
  :handle-created "")