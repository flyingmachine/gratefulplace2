(ns gratefulplace.server
  (:gen-class)
  (:require [compojure.core :as compojure]
            [rabble.ring-app :as ring-app]
            [rabble.middleware.auth :refer (auth)]
            [ring.adapter.jetty :refer (run-jetty)]
            [gratefulplace.config :refer (config)]
            [gratefulplace.rabble-redefs]))

(def app (ring-app/site [auth] []))

(defn start
  "Start the jetty server"
  []
  (run-jetty #'app {:port (or (config :port) 8080) :join? false}))
