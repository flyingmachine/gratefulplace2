(ns gratefulplace.server
  (:gen-class)
  (:require [compojure.core :as compojure]
            [rabble.ring-app :as ring-app]
            [rabble.middleware.routes :as routes]
            [rabble.middleware.auth :refer (auth)]
            [ring.adapter.jetty :refer (run-jetty)]
            [gratefulplace.config :refer (config)]))

(def app (ring-app/wrap
          (auth (compojure/routes routes/auth-routes routes/rabble-routes))))

(defn start
  "Start the jetty server"
  []
  (run-jetty #'app {:port (config :port) :join? false}))
