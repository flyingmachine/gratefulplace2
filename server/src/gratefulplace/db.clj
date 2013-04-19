(ns gratefulplace.db
  (:require [datomic.api :as d])
  (:use environ.core))

(def db-uri (:db-uri (env :datomic)))
(def conn (d/connect db-uri))
(defmacro db
  []
  '(d/db conn))

;; '[:find ?c :where [?c :topic/title]]
(def q
  #(d/q % (db)))

(def ent
  #(d/entity (db) (first %)))

(def collections
  {:topics
   {:all :topic/title
    :attributes [:topic/title]}})


(defn all
  [collection]
  (let [coldef (get collections collection)]
    (map ent (q [:find '?c :where ['?c (:all coldef)]]))))

