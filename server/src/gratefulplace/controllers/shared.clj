(ns gratefulplace.controllers.shared
  (:require [gratefulplace.db.query :as db]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c])
  (:use [flyingmachine.webutils.validation :only (if-valid)]))

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

(defmacro defmapifier
  [fn-name & mapify-args]
  `(defn- ~fn-name
     [id#]
     (if-let [ent# (db/ent id#)]
       (c/mapify
        ent#
        ~@mapify-args)
       nil)))


(defmacro validator
  [params validation]
  `(fn [ctx#]
     (if-valid
      ~params ~validation errors#
      false
      [true {:errors errors#
             :representation {:media-type "application/json"}}])))

(defn exists?
  [record]
  (if record
    {:record record}))

(defn exists-in-ctx?
  [ctx]
  (get ctx :record))