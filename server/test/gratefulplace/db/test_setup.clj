(ns gratefulplace.db.test-setup
  (:require [datomic.api :as d]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.manage :as manage])
  (:use environ.core)
  (:import java.io.File))

(def test-db-uri (:test-uri (env :datomic)))

(defn initialize
  []
  (binding [q/*db-uri* test-db-uri]
    (manage/recreate)
    (manage/load-schema)))