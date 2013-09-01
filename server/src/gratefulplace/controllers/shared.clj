(ns gratefulplace.controllers.shared
  (:require [gratefulplace.db.query :as db])
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        gratefulplace.models.permissions
        gratefulplace.db.mapification
        gratefulplace.utils))

(def author-inclusion-options
  {:author {:only [:id :username :gravatar]}})

(defn invalid
  [errors]
  {:body {:errors errors}
   :status 412})

(defmacro id
  []
  '(str->int (:id params)))

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

;; TODO figure out how to refactor this
(defmacro can-delete-record?
  [record auth]
  `(fn [_#]
     (let [record# ~record
           auth# ~auth]
       (if (or (author? record# auth#) (moderator? auth#))
         {:record record#}))))

(defmacro can-update-record?
  [record auth]
  `(fn [_#]
     (let [auth# ~auth
           record# ~record]
       (if (and (not (:deleted record#))
                (or (moderator? auth#)
                    (author? record# auth#)))
         {:record record#}))))

(defn create-record
  [creation-fn params mapifier]
  (fn [_]
    (let [result (creation-fn params)]
      {:record (mapify-tx-result result mapifier)})))

(defn create-content
  [creation-fn params auth mapifier]
  (create-record creation-fn (merge params {:author-id (:id auth)}) mapifier))