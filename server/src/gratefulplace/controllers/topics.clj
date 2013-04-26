(ns gratefulplace.controllers.topics
  (:require [gratefulplace.db.query :as db]
            [gratefulplace.db.transform :as t]))

(defmacro id
  []
  '(read-string (re-find #"^\d+$" (:id params))))

(defn query
  [params]
  (map #(t/transform-entity (dissoc (:topic t/rules) :posts) %)
       (db/all :topic/title)))

(defn show
  [params]
  (let [id (id)]
    {:body (t/transform-entity (dissoc (:topic t/rules) :first-post) (db/ent id))}))