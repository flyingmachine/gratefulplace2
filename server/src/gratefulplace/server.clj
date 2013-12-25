(ns gratefulplace.server
  (:gen-class)
  (:require [gratefulplace.db.manage :as db]
            [compojure.core :as compojure]
            [rabble.ring-app :as ring-app]
            [rabble.middleware.routes :as routes]
            [ring.adapter.jetty :refer (run-jetty)]))

(defn debug [f]
  (fn [{:keys [uri request-method params session] :as request}]
    (println params)
    (f request)))

(def app (ring-app/wrap (compojure/routes routes/auth-routes routes/rabble-routes)))

(defn -main
  "Start the jetty server"
  []
  (run-jetty #'app {:port (Integer. (get (System/getenv) "PORT" 8080)) :join? false}))