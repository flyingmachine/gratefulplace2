(ns gratefulplace.controllers.posts
  (:require [datomic.api :as d]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c]
            [cemerick.friend :as friend])
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        [liberator.core :only [defresource]]
        gratefulplace.controllers.shared
        gratefulplace.models.permissions
        gratefulplace.utils))

(defmapifier record
  mr/ent->post
  {:include author-inclusion-options
   :exclude [:content :created-at :topic-id]})

(defresource update! [params auth]
  :allowed-methods [:put :post]
  :available-media-types ["application/json"]

  :malformed? (validator params (:update validations/post))
  :handle-malformed errors-in-ctx

  :authorized? (fn [_]
                 (let [post (record (id))]
                   (if (and (not (:deleted post)) (can-modify-record? post auth))
                     {:record post})))

  :exists? record-in-ctx
  
  :put! (fn [_] (db/t [(c/mapify params mr/post->txdata)]))
  :post! (fn [_] (db/t [(c/mapify params mr/post->txdata)]))
  :new? false
  :respond-with-entity? true
  :handle-ok (fn [_] (record (id))))

(defresource create! [params auth]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :authorized? (logged-in? auth)

  :malformed? (validator params (:create validations/post))
  :handle-malformed errors-in-ctx

  :post! (fn [_]
           (let [post-tempid (d/tempid :db.part/user -1)
                 topic-id (:topic-id params)
                 post (remove-nils-from-map {:post/content (:content params)
                                             :post/topic topic-id
                                             :post/created-at (java.util.Date.)
                                             :content/author (:id auth)
                                             :db/id post-tempid})]
             {:record
              (mapify-tx-result
               (db/t [post
                      {:db/id topic-id
                       :topic/last-posted-to-at (java.util.Date.)}])
               post-tempid
               mr/ent->post
               {:include author-inclusion-options})}))
  :handle-created record-in-ctx)

(defresource delete! [params auth]
  :allowed-methods [:delete]
  :available-media-types ["application/json"]
  :authorized? (fn [_]
                 (let [post (record (id))]
                   (if (can-modify-record? post auth)
                     {:record post})))
  :exists? exists-in-ctx?
  :delete! (fn [_] (delete-content (id))))