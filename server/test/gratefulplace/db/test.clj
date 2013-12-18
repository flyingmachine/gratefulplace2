(ns gratefulplace.db.test
  (:require [datomic.api :as d]
            [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.db.manage :as manage])
  (:use gratefulplace.config)
  (:import java.io.File))

(def test-db-uri (config :datomic :test-uri))
(def initialized (atom false))

(defmacro with-test-db
  [& body]
  `(binding [dj/*db-uri* test-db-uri]
     ~@body))

(defn initialize
  []
  (doall (manage/reload)))
