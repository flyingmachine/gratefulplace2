(ns gratefulplace.controllers.topics
  (:require [gratefulplace.db :as db]))

(defmacro id
  []
  '(read-string (re-find #"^\d+$" (:id params))))

(defn query
  [params]
  (binding [db/*remove-key-namespace* true]
    (map #(merge % {:post-count (ffirst (db/q [:find '(count ?c) :where ['?c :post/topic (:id %)]]))})
         (db/entseq->maps :topics (db/all :topics)))))

(defn show
  [params]
  (binding [db/*remove-key-namespace* true]
    (let [id (id)]
      {:body (->> (db/ent id)
                  (db/ent->map :topics)
                  (merge {:posts (db/entseq->maps :posts (db/all :posts [:post/topic id]))}))})))