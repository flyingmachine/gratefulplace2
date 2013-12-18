(ns gratefulplace.controllers.watches
  (:require [gratefulplace.db.validations :as validations]
            [datomic.api :as d]
            [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.transactions.watches :as tx])
  (:use [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.db.mapification
        flyingmachine.webutils.utils))

(defmapifier record
  mr/ent->watch)

(defresource query [params auth]
  :available-media-types ["application/json"]
  :handle-ok (fn [ctx]
               (map (comp record first)
                    (d/q '[:find ?watch
                           :in $ ?userid
                           :where [?watch :watch/user ?userid]
                           [?watch :watch/topic ?topic]
                           [?topic :content/deleted false]]
                         (dj/db)
                         (:id auth)))))

(defresource create! [params auth]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :authorized? (logged-in? auth)

  :post! (create-record tx/create-watch params record)
  :handle-created record-in-ctx)

(defresource delete! [params auth]
  :allowed-methods [:delete]
  :available-media-types ["application/json"]
  :authorized? (fn [_]
                 (let [watch-id (str->int (:id params))
                       watch (dj/ent watch-id)]
                   (if (and watch
                            (= (:db/id (:watch/user watch)) (:id auth)))
                     {:record {:id watch-id}})))
  :exists? exists-in-ctx?
  :delete! (fn [ctx]
             (dj/retract (get-in ctx [:record :id]))))