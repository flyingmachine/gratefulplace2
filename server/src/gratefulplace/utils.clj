(ns gratefulplace.utils
  (:require [flyingmachine.serialize.core :as s]
            [gratefulplace.db.query :as db]))

(defn remove-nils-from-map
  [record]
  (into {} (remove (comp nil? second) record)))

(defn reverse-by
  [key col]
  (sort-by key #(compare %2 %1) col))

(defn str->int
  [str]
  (read-string (re-find #"^\d+$" str)))

(defn serialize-tx-result
  ([tx tempid serializer]
     (serialize-tx-result tx tempid serializer {}))
  ([tx tempid serializer serializer-options]
     (-> tx
         deref
         :tempids
         (db/resolve-tempid tempid)
         db/ent
         (s/serialize serializer serializer-options))))