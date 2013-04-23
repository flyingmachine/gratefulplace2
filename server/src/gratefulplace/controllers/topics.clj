(ns gratefulplace.controllers.topics
  (:require [gratefulplace.db :as db]))

(defn query
  [params]
  (binding [db/*remove-key-namespace* true]
    (db/entseq->maps :topics (db/all :topics))))