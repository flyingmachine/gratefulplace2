(ns gratefulplace.controllers.topics
  (:require [gratefulplace.db.query :as db]
            [gratefulplace.db.serializers :as ss]
            [gratefulplace.db.serialize :as s]))

(defmacro id
  []
  '(read-string (re-find #"^\d+$" (:id params))))

(defn query
  [params]
  (map #(s/serialize % ss/ent->topic {:include {:first-post {}
                                                :author {:exclude [:email]}}})
       (db/all :topic/title)))

(defn show
  [params]
  (let [id (id)]
    {:body (s/serialize (db/ent id) ss/ent->topic {:include
                                                   {:posts {:include
                                                            {:author {:exclude [:email]}}}}})}))