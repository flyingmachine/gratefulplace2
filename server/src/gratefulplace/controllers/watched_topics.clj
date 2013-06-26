(ns gratefulplace.controllers.watched-topics
  (:require [gratefulplace.db.query :as db]
            [datomic.api :as d]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c]
            [cemerick.friend :as friend])
  (:use [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.utils))

(defmapifier record
  mr/ent->topic
  {:include (merge {:first-post {}}
                   author-inclusion-options)})

;; TODO check whether this is super roundabout
(defresource query [params auth]
  :available-media-types ["application/json"]
  :handle-ok (fn [ctx]
               (reverse-by :last-posted-to-at
                           (map (comp record :db/id :watch/topic db/ent first)
                                (d/q '[:find ?watch
                                       :in $ ?userid
                                       :where [?watch :watch/user ?userid]
                                              [(comp not :content/deleted :watch/topic ?watch)]]
                                     (db/db)
                                     (:id auth))))))