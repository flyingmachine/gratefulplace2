(ns gratefulplace.controllers.topics
  (:require [datomic.api :as d]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.serializers :as ss]
            [flyingmachine.serialize.core :as s]
            [cemerick.friend :as friend])
  (:use gratefulplace.controllers.shared
        gratefulplace.utils))

(def single-topic-serialize-options
  {:include {:posts (merge author-inclusion-options)}})

(def index-topic-serialize-options
  (merge-with merge
              {:include {:first-post {}}}
              author-inclusion-options))

(defn query
  [params]
  (sort-by :last-posted-to-at
           #(compare %2 %1)
           (map #(s/serialize
                  %
                  ss/ent->topic
                  index-topic-serialize-options)
                (db/all :topic/first-post))))

(defn show
  [params]
  {:body (s/serialize
          (db/ent (id (:id params)))
          ss/ent->topic
          single-topic-serialize-options)})

(defn create!
  [params]
  (let [topic-tempid (d/tempid :db.part/user -1)
        post-tempid (d/tempid :db.part/user -2)
        author-id (:id (friend/current-authentication))
        topic (remove-nils-from-map {:topic/title (:title params)
                                     :topic/first-post post-tempid
                                     :topic/last-posted-to-at (java.util.Date.)
                                     :content/author author-id
                                     :db/id topic-tempid})]
    {:body (serialize-tx-result
            (db/t [topic
                   {:post/content (:content params)
                    :post/topic topic-tempid
                    :content/author author-id
                    :db/id post-tempid}]))}))
