(ns gratefulplace.controllers.likes
  (:require [datomic.api :as d]
            [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c])
  (:use [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.utils))

(defn find-like
  [like]
  (dj/one [:like/post (:like/post like)]
          [:like/user (:like/user like)]))

(defn clean-params
  [params auth]
  (c/mapify (merge params {:user-id (:id auth)}) mr/like->txdata))

(defresource create! [params auth]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :authorized? (logged-in? auth)
  :post! (fn [_]
           (let [like-params (clean-params params auth)]
             (if-not (find-like like-params)
               (dj/t [like-params]))))
  :handle-created "")

(defresource delete! [params auth]
  :allowed-methods [:delete]
  :available-media-types ["application/json"]
  :authorized? (fn [_]
                 (if-let [like (find-like (clean-params params auth))]
                   {:record (:db/id like)}))
  :exists? exists-in-ctx?
  :delete! (fn [ctx]
             (dj/retract (:record ctx))))