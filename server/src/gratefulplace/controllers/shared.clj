(ns gratefulplace.controllers.shared
  (:require [gratefulplace.db.query :as db]
            [gratefulplace.db.serializers :as ss]
            [flyingmachine.serialize.core :as s]))

(def author-inclusion-options
  {:author {:exclude [:email]}})

(defn invalid
  [errors]
  {:body {:errors errors}
   :status 412})

(def OK
  {:status 200})>

(def NOT-AUTHORIZED
  {:status 401})

(def NOT-FOUND
  {:status 404})

(defmacro id
  []
  '(str->int (:id params)))

(defmacro defserialization
  [fn-name & serialize-args]
  `(defn- ~fn-name
     [id#]
     (if-let [ent# (db/ent id#)]
       (s/serialize
        ent#
        ~@serialize-args)
       nil)))