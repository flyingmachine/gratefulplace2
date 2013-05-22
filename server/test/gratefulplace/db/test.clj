(ns gratefulplace.db.test
  (:require [datomic.api :as d]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.manage :as manage])
  (:use environ.core)
  (:import java.io.File))

(def test-db-uri (:test-uri (env :datomic)))
(def initialized (atom false))

(defmacro with-test-db
  [& body]
  `(binding [q/*db-uri* test-db-uri]
     ~@body))

(defn initialize
  []
  (with-test-db
    (manage/recreate)
    ;; TODO Not sure why I have to put this doall here before the swap!
    (doall (manage/load-schema)))
    (swap! initialized (fn [_] true)))
