(ns gratefulplace.app
  (:gen-class)
  (:require [gratefulplace.server :as server]
            [gratefulplace.db.manage :as db]))

(defn -main
  [cmd]
  (cond 
   (= cmd "server") (server/-main)
   (= cmd "db/reload") (do (println (db/reload)) (System/exit 0))))