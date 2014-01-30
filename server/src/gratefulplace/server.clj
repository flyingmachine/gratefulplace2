(ns gratefulplace.server
  (:gen-class)
  (:require [compojure.core :as compojure]
            [rabble.ring-app :as ring-app]
            [rabble.middleware.dispatcher :refer (rabble-dispatcher)]
            [rabble.middleware.auth :refer (auth)]
            [ring.adapter.jetty :refer (run-jetty)]
            [gratefulplace.config :refer (config)]
            [gratefulplace.rabble-redefs])
  (:import [gratefulplace.rabble_redefs GPDispatcher]))

(def app (ring-app/site [(rabble-dispatcher (GPDispatcher.)) auth] []))

(defn start
  "Start the jetty server"
  []
  (run-jetty #'app {:port (or (config :port) 8080) :join? false}))
