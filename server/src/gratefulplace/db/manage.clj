(ns gratefulplace.db.manage
  (:require [datomic.api :as d]
            [gratefulplace.db.query :as db])
  (:use environ.core)
  (:import java.io.File))

(defn recreate
  []
  (d/delete-database db/*db-uri*)
  (d/create-database db/*db-uri*))

(defn load-schema
  []
  (map (bound-fn [f] (db/t (read-string (slurp f))))
       (.listFiles (File. "resources/migrations"))))

(defn reload
  []
  (recreate)
  (load-schema))