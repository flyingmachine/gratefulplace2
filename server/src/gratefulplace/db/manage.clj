(ns gratefulplace.db.manage
  (:gen-class)
  (:require [datomic.api :as d]
            [gratefulplace.db.query :as db])
  (:use environ.core)
  (:import java.io.File))

(defn create
  []
  (d/create-database db/*db-uri*))

(defn recreate
  []
  (d/delete-database db/*db-uri*)
  (create))

(defn load-schema
  []
  (map (bound-fn [f] (db/t (read-string (slurp f))))
       (.listFiles (File. "resources/migrations"))))

(defn reload
  []
  (recreate)
  (load-schema))

(defn setup
  []
  (if (create)
    (load-schema)))