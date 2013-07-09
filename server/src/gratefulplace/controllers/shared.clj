(ns gratefulplace.controllers.shared
  (:require [gratefulplace.db.query :as db]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c])
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        gratefulplace.models.permissions))

(def author-inclusion-options
  {:author {:only [:id :username :gravatar]}})

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
     (if-let [ent# (or (db/ent? id#) (db/ent id#))]
       (c/mapify
        ent#
        ~@mapify-args)
       nil)))


(defmacro validator
  "Used in malformed? which is why truth values are reversed"
  [params validation]
  `(fn [ctx#]
     (if-valid
      ~params ~validation errors#
      false
      [true {:errors errors#
             :representation {:media-type "application/json"}}])))



;; working with liberator

(defn exists?
  [record]
  (if record
    {:record record}))

(defn record-in-ctx
  [ctx]
  (get ctx :record))

(def exists-in-ctx? record-in-ctx)

(defn errors-in-ctx
  [ctx]
  {:errors (get ctx :errors)})

(defn delete-record-in-ctx
  [ctx]
  (db/t [{:db/id (get-in ctx [:record :id])
          :content/deleted true}]))

(defmacro can-delete-record?
  [record auth]
  `(fn [_#]
     (let [record# ~record]
       (if (or (author? record# ~auth) (moderator? ~auth))
         {:record record#}))))

(defmacro can-update-record?
  [record auth]
  `(fn [_#]
     (let [record# ~record]
       (if (and (author? record# ~auth) (not (:deleted ~record)))
         {:record record#}))))