(ns gratefulplace.controllers.topics
  (:require [datomic.api :as d]
            [gratefulplace.db.query :as db]
            [gratefulplace.db.serializers :as ss]
            [gratefulplace.db.serialize :as s]
            [cemerick.friend :as friend])
  (:use gratefulplace.controllers.shared))

(defn query
  [params]
  (map #(s/serialize
         %
         ss/ent->topic
         {:include {:first-post {}
                    :author {:exclude [:email]}}})
       (db/all :topic/first-post)))

(defn show
  [params]
  (let [id (id)]
    {:body (s/serialize
            (db/ent id)
            ss/ent->topic
            {:include
             {:posts {:include
                      {:author {:exclude [:email]}}}}})}))

;; TODO more functional way to build the topic
(defn create!
  [params]
  (let [author-id (:id (friend/current-authentication))
        topic-base {:topic/first-post #db/id[:db.part/user -200]
                    :content/author author-id
                    :db/id #db/id[:db.part/user -100]}
        topic (if-let [title (:title params)]
                (merge topic-base {:topic/title title})
                topic-base)]
    (println
     (db/t [topic
            {:post/content (:content params)
             :post/topic #db/id[:db.part/user -100]
             :content/author author-id
             :db/id #db/id[:db.part/user -200]}])))
  {:body {}})
