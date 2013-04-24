(ns gratefulplace.controllers.topics
  (:require [gratefulplace.db :as db]))

(defmacro id
  []
  '(BigInteger. (:id params)))

(defn query
  [params]
  (binding [db/*remove-key-namespace* true]
    (map #(conj % [:post-count
                   (ffirst (db/q [:find '(count ?c) :where ['?c :post/topic (:id %)]]))])
         {:body (db/entseq->maps :topics (db/all :topics))})))

(defn show
  [params]
  (binding [db/*remove-key-namespace* true]
    {:body (db/ent->map :topics (db/ent (id)))}))