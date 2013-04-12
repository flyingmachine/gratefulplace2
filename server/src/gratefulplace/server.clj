(ns gratefulplace.server
  (:use [ring.adapter.jetty :only (run-jetty)]
        ring.middleware.params
        ring.middleware.keyword-params
        ring.middleware.nested-params
        ring.middleware.session
        ring.middleware.json
        [gratefulplace.middleware.routes :only (routes)]))

; The ring app
(def app
  (-> routes
      ;; (wrap-session {:cookie-name "gratefulplace-session" :store (db-session-store)})
      wrap-keyword-params
      wrap-nested-params
      wrap-params))

(defn -main
  "Start the jetty server"
  []
  (run-jetty #'app {:port (Integer. (get (System/getenv) "PORT" 8080)) :join? false}))