(ns gratefulplace.db.connection
  (:require [datomic.api :as d])
  (:use environ.core)
  (:import java.io.File))

(declare initialize)

(def initialized (atom false))

(def db-uri (:test-uri (env :datomic)))
(defn conn
  []
  (when (not @initialized)
    (initialize))
  (d/connect db-uri))
(defn db []
  (d/db (conn)))

(defn schema-paths
  "Returns a vector of schema files as specified by [:datomic :schemas]"
  [schemas]
  (let [[_ filenames] schemas]
    (map #(clojure.java.io/resource (str "schema/" %)) filenames)))

(defn initialize
  []
  (d/create-database db-uri)
  (let [conn (d/connect db-uri)]
    (doseq [schema-file (schema-paths (:schemas (env :datomic)))]
      (d/transact conn (read-string (slurp schema-file)))))
  (swap! initialized (fn [_] true)))

(def q
  #(d/q % (db)))

(def ent
  #(d/entity (db) %))

(defn one
  [& conditions]
  (let [conditions (map #(concat ['?c] %) conditions)]
    (->(q {:find ['?c]
           :where conditions})
       ffirst
       ent)))

(defn all
  [common-attribute & conditions]
  (let [conditions (concat [['?c common-attribute]]
                           (map #(concat ['?c] %) conditions))]
    (map #(ent (first %)) (q {:find ['?c]
                              :where conditions}))))

(def t
  #(d/transact (conn) %))