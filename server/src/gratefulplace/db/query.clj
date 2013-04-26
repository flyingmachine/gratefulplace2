(ns gratefulplace.db.query
  (:require [datomic.api :as d])
  (:use environ.core
        gratefulplace.utils))

(def db-uri (:db-uri (env :datomic)))
(def conn (d/connect db-uri))
(defmacro db
  []
  '(d/db conn))

;; '[:find ?c :where [?c :topic/title]]
(def q
  #(d/q % (db)))

(def ent
  #(d/entity (db) %))

(defn all
  [common-attribute & conditions]
  (let [conditions (concat [['?c common-attribute]]
                           (map #(concat ['?c] %) conditions))]
    (map #(ent (first %)) (q {:find ['?c]
                              :where conditions}))))