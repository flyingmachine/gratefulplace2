(ns gratefulplace.db.manage
  (:gen-class)
  (:require [datomic.api :as d]
            [gratefulplace.db.query :as db]
            [clojure.java.io :as io])
  (:use environ.core)
  (:import java.io.File))

(defn create
  []
  (d/create-database db/*db-uri*))

(defn delete
  []
  (d/delete-database db/*db-uri*))

(defn recreate
  []
  (delete)
  (create))

;; TODO so this is super fun. Have to list each migration because you
;; can't list the contents of a directory in a jar file with io/resource
(defn migrate
  []
  (map (bound-fn [f]
         (-> f io/resource slurp read-string db/t))
       (-> ["migrations/20130521-161013-schema.edn" "migrations/20130521-161014-seed-data.edn"])))

(defn reload
  []
  (recreate)
  (migrate))