(ns gratefulplace.controllers.watches
  (:require [gratefulplace.db.validations :as validations]
            [datomic.api :as d]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c])
  (:use [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.utils))

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
                         (db/db)
                         (:id auth)))))

(defresource create! [params auth]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :authorized? (logged-in? auth)

  :post! (fn [_]
           (let [watch-tempid (d/tempid :db.part/user -1)]
             {:record
              (mapify-tx-result
               (db/t [{:db/id watch-tempid
                       :watch/unread-count 0
                       :watch/topic (:topic-id params)
                       :watch/user (:user-id params)}])
               watch-tempid
               mr/ent->watch)}))
  :handle-created record-in-ctx)

(defresource delete! [params auth]
  :allowed-methods [:delete]
  :available-media-types ["application/json"]
  :authorized? (fn [_]
                 (let [watch-id (str->int (:id params))
                       watch (db/ent watch-id)]
                   (if (and watch
                            (= (:db/id (:watch/user watch)) (:id auth)))
                     {:record {:id watch-id}})))
  :exists? exists-in-ctx?
  :delete! (fn [ctx]
             (db/retract-entity (get-in ctx [:record :id]))))