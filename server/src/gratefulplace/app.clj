(ns gratefulplace.app
  (:gen-class)
  (:require [gratefulplace.server :as server]
            [rabble.db.tasks :as db-tasks]
            [gratefulplace.config :as config]))

(defmacro final
  [& body]
  `(do ~@body (System/exit 0)))

(defn -main
  [cmd]
  (cond 
   (= cmd "server") (server/-main)
   (= cmd "db/reload") (final (println (db-tasks/reload)))
   (= cmd "db/seed") (final (println (db-tasks/seed)))
   (= cmd "db/install-schemas") (final (println (db-tasks/install-schemas)))
   (= cmd "config/print-config") (final (config/print-config))))