(ns gratefulplace.controllers.topics
  (:require [datomic.api :as d]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.transactions :as ts]
            [clojure.math.numeric-tower :as math])
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.db.mapification
        gratefulplace.utils))

(def query-mapify-options
  {:include (merge {:first-post {:only [:content :likers]}}
                   author-inclusion-options)})

(defmapifier query-record mr/ent->topic query-mapify-options)

(defmapifier record
  mr/ent->topic
  {:include {:posts {:include author-inclusion-options}
             :watches {}}})


(defn paginate
  [topics params]
  (let [per-page 15
        topic-count (count topics)
        page-count (math/ceil (/ topic-count per-page))
        current-page (or (str->int (:page params)) 1)
        skip (* (dec current-page) per-page)
        paged-topics (take per-page (drop skip topics))]
    (conj paged-topics {:page-count page-count :topic-count topic-count})))

(defresource query [params]
  :available-media-types ["application/json"]
  :handle-ok (fn [_]
               (paginate
                (reverse-by :last-posted-to-at
                            (map query-record
                                 (db/all :topic/first-post [:content/deleted false])))
                params)))

(defresource show [params auth]
  :available-media-types ["application/json"]
  :exists? (exists? (record (id)))
  :handle-ok (fn [ctx]
               (if auth
                 (ts/reset-watch-count (id) (:id auth)))
               (record-in-ctx ctx)))

(defresource create! [params auth]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :authorized? (logged-in? auth)

  :malformed? (validator params validations/topic)
  :handle-malformed errors-in-ctx
  
  :post! (create-content ts/create-topic params auth query-record)
  :handle-created record-in-ctx)

(defresource delete! [params auth]
  :allowed-methods [:delete]
  :available-media-types ["application/json"]
  :authorized? (can-delete-record? (record (id)) auth)
  :exists? exists-in-ctx?
  :delete! delete-record-in-ctx)