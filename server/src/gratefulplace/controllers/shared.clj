(ns gratefulplace.controllers.shared
  (:require [flyingmachine.serialize.core :as s]
            [gratefulplace.db.query :as db]))

(def author-inclusion-options
  {:include {:author {:exclude [:email :password]}}})

(defn id
  [str-id]
  (read-string (re-find #"^\d+$" str-id)))

(defn serialize-tx-result
  [tx tempid serializer serializer-options]
  (-> tx
      deref
      :tempids
      (db/resolve-tempid tempid)
      db/ent
      (s/serialize serializer serializer-options)))