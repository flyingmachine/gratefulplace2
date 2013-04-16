(ns gratefulplace.db
  (:require [datomic.api :as d])
  (:use environ.core))

(def db-uri (:db-uri (env :datomic)))
(def conn (d/connect db-uri))
(defmacro db
  []
  '(d/db conn))

(def q
  #(d/q % (db)))

