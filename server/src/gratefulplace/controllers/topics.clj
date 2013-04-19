(ns gratefulplace.controllers.topics
  (:require [gratefulplace.db :as db]))

(defn query
  [params]
  (db/entseq->maps :topics (db/all :topics)))