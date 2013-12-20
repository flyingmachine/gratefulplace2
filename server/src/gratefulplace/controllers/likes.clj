(ns gratefulplace.controllers.likes
  (:require [datomic.api :as d]
            [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c]
            [com.flyingmachine.liberator-templates.sets.json-crud
             :refer (defcreate! defdelete!)])
  (:use [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        flyingmachine.webutils.utils))

(defn find-like
  [like]
  (dj/one [:like/post (:like/post like)]
          [:like/user (:like/user like)]))

(defn clean-params
  [params auth]
  (c/mapify (merge params {:user-id (:id auth)}) mr/like->txdata))

(defcreate!
  :post! (fn [_]
           (let [like-params (clean-params params auth)]
             (if-not (find-like like-params)
               (dj/t [like-params]))))
  :return "")

(defdelete!
  :authorized? (fn [_]
                 (if-let [like (find-like (clean-params params auth))]
                   {:record (:db/id like)}))
  :delete! (fn [ctx] (dj/retract (:record ctx))))