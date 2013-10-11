(ns gratefulplace.db.mapification
  (:require [flyingmachine.cartographer.core :as c]
            [gratefulplace.db.query :as db]))

(defmacro defmapifier
  [fn-name rules & mapify-opts]
  (let [fn-name fn-name]
    `(defn- ~fn-name
       ([id#]
          (~fn-name id# {}))
       ([id# addtl-mapify-args#]
          (if-let [ent# (or (db/ent? id#) (db/ent id#))]
            (let [mapify-opts# (merge-with (fn [_# x#] x#) ~@mapify-opts addtl-mapify-args#)]
              (c/mapify
               ent#
               ~rules
               mapify-opts#))
            nil)))))

(defn tx-result->ent
  [tx-result]
  (let [{:keys [result tempid]} tx-result]
    (-> result
        deref
        :tempids
        (db/resolve-tempid tempid)
        db/ent)))

(defn mapify-tx-result
  [tx-result mapifier]
  (-> tx-result
      tx-result->ent
      mapifier))