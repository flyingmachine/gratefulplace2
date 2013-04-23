(ns gratefulplace.db
  (:require [datomic.api :as d])
  (:use environ.core
        gratefulplace.utils))

(declare ent->map)

(def ^:dynamic *remove-key-namespace* false)
(def db-uri (:db-uri (env :datomic)))
(def conn (d/connect db-uri))
(defmacro db
  []
  '(d/db conn))

(def collections
  {:topics
   {:all :topic/title
    :attributes [:topic/title
                 [:topic/first-post :posts]]}
   :posts
   {:all :post/topic
    :attributes [:post/content]}})

;; '[:find ?c :where [?c :topic/title]]
(def q
  #(d/q % (db)))

(def ent
  #(d/entity (db) (first %)))

(defn datomic-key
  [key]
  (remove-key-namespace key))

(defn attr-value
  [attr entity]
  (if (keyword? attr)
    [(datomic-key attr) (attr entity)]
    (let [[attr collection] attr
          child-entity (attr entity)]
      [(datomic-key attr) (ent->map collection child-entity)])))

(defn ent->map
  [collection entity]
  (reduce (fn [acc attr]
            (conj acc (attr-value attr entity)))
          {}
          (:attributes (get collections collection))))

(defn entseq->maps
  [collection seq]
  (map (partial ent->map collection) seq))

(defn all
  [collection]
  (let [coldef (get collections collection)]
    (map ent (q [:find '?c :where ['?c (:all coldef)]]))))
