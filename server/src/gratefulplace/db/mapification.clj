(ns gratefulplace.db.mapification
  (:require [flyingmachine.cartographer.core :as c]
            [gratefulplace.db.query :as db]))

(defmacro defmapifier
  [fn-name & mapify-args]
  `(defn- ~fn-name
     [id#]
     (if-let [ent# (or (db/ent? id#) (db/ent id#))]
       (c/mapify
        ent#
        ~@mapify-args)
       nil)))

(defn mapify-tx-result
  [tx-result mapifier]
  (let [{:keys [result tempid]} tx-result]
    (-> result
        deref
        :tempids
        (db/resolve-tempid tempid)
        db/ent
        mapifier)))