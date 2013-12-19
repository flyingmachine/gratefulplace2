(ns gratefulplace.lib.liberator-templates
  (:require [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.models.permissions :refer :all]
            [gratefulplace.db.mapification :refer :all]
            [flyingmachine.webutils.utils :refer :all]
            [flyingmachine.webutils.validation :refer (if-valid)]
            [liberator.core :refer (defresource)]))

(def handlers {:update! :handle-ok
               :create! :handle-created})

(defn errors-in-ctx
  [ctx]
  {:errors (get ctx :errors)})

(defn record-in-ctx
  [ctx]
  (get ctx :record))

(def exists-in-ctx? record-in-ctx)

(defn valid?
  [action form]
  `[:malformed? ~form :handle-malformed errors-in-ctx])

(defn new?
  [action]
  (action {:update! false
           :create! true}))

(defn return
  [action form]
  `[:new? ~(new? action)
    :respond-with-entity? true
    ~(handlers action) ~form])

(def expanders {:valid? valid?
                :return return})

(defn expand
  [action [key form]]
  (if-let [expander (get expanders key)]
    (expander action form)
    [key form]))

(defmacro defshow
  [paramlist & args]
  `(defresource ~'show ~paramlist
     :available-media-types ["application/json"]
     ~@(reduce into (map #(expand :update! %)
                         (partition 2 args)))))

(defmacro defupdate!
  [paramlist & args]
  `(defresource ~'update! ~paramlist
     :allowed-methods [:put]
     :available-media-types ["application/json"]
     :exists? exists-in-ctx?
     ~@(reduce into (map #(expand :update! %)
                         (partition 2 args)))))

(defmacro defcreate!
  [paramlist & args]
  `(defresource ~'create! ~paramlist
     :allowed-methods [:post]
     :available-media-types ["application/json"]
     ~@(reduce into (map #(expand :create! %)
                         (partition 2 args)))))

(defmacro defdelete!
  [paramlist & args]
  `(defresource ~'delete! ~paramlist
     :allowed-methods [:delete]
     :available-media-types ["application/json"]
     :exists? exists-in-ctx?
     ~@(reduce into (map #(expand :delete! %)
                         (partition 2 args)))))