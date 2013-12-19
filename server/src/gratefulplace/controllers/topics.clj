(ns gratefulplace.controllers.topics
  (:require [datomic.api :as d]
            [gratefulplace.db.validations :as validations]
            [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.transactions.topics :as topic-tx]
            [gratefulplace.db.transactions.watches :as watch-tx]
            [clojure.math.numeric-tower :as math]
            [liberator.core :refer [defresource]]
            [gratefulplace.controllers.shared :refer :all]
            [gratefulplace.models.permissions :refer :all]
            [gratefulplace.db.mapification :refer :all]
            [flyingmachine.webutils.utils :refer :all]
            [gratefulplace.lib.liberator-templates :refer (defshow defcreate! defdelete!)]))

(def query-mapify-options
  {:include (merge {:first-post {:only [:content :likers :id]}}
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

(defn visibility
  [auth]
  (when-not (logged-in? auth)
    [:topic/visibility :visibility/public]))

(defresource query [params auth]
  :available-media-types ["application/json"]
  :handle-ok (fn [_]
               (paginate
                (reverse-by :last-posted-to-at
                            (map query-record
                                 (dj/all :topic/first-post
                                         [:content/deleted false]
                                         (visibility auth))))
                params)))

(defshow
  [params auth]
  :exists? (exists? (record (id)))
  :handle-ok (fn [ctx]
               (if auth (watch-tx/reset-watch-count (id) (:id auth)))
               (record-in-ctx ctx)))

(defcreate!
  [params auth]
  :authorized? (logged-in? auth)
  :valid? (validator params validations/topic)
  :post! (create-content topic-tx/create-topic params auth query-record)
  :return record-in-ctx)

(defdelete!
  [params auth]
  :authorized? (can-delete-record? (record (id)) auth)
  :delete! delete-record-in-ctx)