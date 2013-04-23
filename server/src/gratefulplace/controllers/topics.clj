(ns gratefulplace.controllers.topics
  (:require [gratefulplace.db :as db]))

(defn query
  [params]
  (binding [db/*remove-key-namespace* true]
    (map #(conj % [:post-count
                   (ffirst (db/q [:find '(count ?c) :where ['?c :post/topic (:id %)]]))])
         (db/entseq->maps :topics (db/all :topics)))))

(defn show
  [params]
  (binding [db/*remove-key-namespace* true]
    (db/ent->map :topics (db/one (:id params)))))