(ns gratefulplace.server
  (:use [ring.adapter.jetty :only (run-jetty)]
        ring.middleware.params
        ring.middleware.keyword-params
        ring.middleware.nested-params
        ring.middleware.session
        [ring.middleware.format-params :only [wrap-restful-params]]
        [ring.middleware.format-response :only [wrap-restful-response]]
        [gratefulplace.middleware.routes :only (routes)]))

; The ring app
(def app
  (-> routes
      ;; (wrap-session {:cookie-name "gratefulplace-session" :store (db-session-store)})
      wrap-restful-params
      wrap-restful-format
      ))

(defn -main
  "Start the jetty server"
  []
  (run-jetty #'app {:port (Integer. (get (System/getenv) "PORT" 8080)) :join? false}))