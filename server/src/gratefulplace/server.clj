(ns gratefulplace.server
  (:gen-class)
  (:require [compojure.core :as compojure]
            [rabble.ring-app :refer (app)]
            [ring.adapter.jetty :refer (run-jetty)]
            [gratefulplace.config :refer (config)]))

(defn start
  "Start the jetty server"
  []
  (run-jetty #'app {:port (or (config :port) 8080) :join? false}))
