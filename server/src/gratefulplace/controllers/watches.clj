(ns gratefulplace.controllers.watches
  (:require [gratefulplace.db.validations :as validations]
            [datomic.api :as d]
            [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.transactions.watches :as tx]
            [com.flyingmachine.liberator-templates.sets.json-crud
             :refer (defquery defcreate! defdelete!)])
  (:use [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.db.mapification
        flyingmachine.webutils.utils))

(defmapifier record
  mr/ent->watch)

(defquery
  :return (fn [ctx]
            (map (comp record first)
                 (d/q '[:find ?watch
                        :in $ ?userid
                        :where [?watch :watch/user ?userid]
                        [?watch :watch/topic ?topic]
                        [?topic :content/deleted false]]
                      (dj/db)
                      (:id auth)))))

(defcreate!
  :authorized? (logged-in? auth)
  :post! (create-record tx/create-watch params record)
  :return record-in-ctx)

(defdelete!
  :authorized? (fn [_]
                 (let [watch-id (str->int (:id params))
                       watch (dj/ent watch-id)]
                   (if (and watch
                            (= (:db/id (:watch/user watch)) (:id auth)))
                     {:record {:id watch-id}})))
  :delete! (fn [ctx] (dj/retract (get-in ctx [:record :id]))))