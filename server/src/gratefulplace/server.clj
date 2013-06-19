(ns gratefulplace.server
  (:use [ring.adapter.jetty :only (run-jetty)]
        ring.middleware.params
        ring.middleware.keyword-params
        ring.middleware.nested-params
        ring.middleware.session
        ring.middleware.format
        [gratefulplace.middleware.routes :only (routes)]
        [gratefulplace.middleware.auth :only (auth)]
        [gratefulplace.middleware.db-session-store :only (db-session-store)]))

(defn wrap-exception [f]
  (fn [request]
    (try (f request)
      (catch Exception e
        (do
          (println e)
          {:status 500
           :body "Exception caught"})))))

(defn debug [f]
  (fn [{:keys [uri request-method params session] :as request}]
    (println session)
    (f request)))

(defn wrap
  [to-wrap]
  (-> to-wrap
      (wrap-session {:cookie-name "gratefulplace-session" :store (db-session-store {})})
      (wrap-restful-format :formats [:json-kw])
      wrap-exception
      wrap-keyword-params
      wrap-nested-params
      wrap-params))

; The ring app
(def app
  (-> routes
      auth
      ;; debug
      wrap))

(defn -main
  "Start the jetty server"
  []
  (run-jetty #'app {:port (Integer. (get (System/getenv) "PORT" 8080)) :join? false}))