(ns gratefulplace.middleware.routes
  (:require compojure.route
            compojure.handler
            [gratefulplace.controllers.topics :as topics]
            [gratefulplace.controllers.posts :as posts]
            [gratefulplace.controllers.users :as users]
            [gratefulplace.controllers.session :as session]
            [gratefulplace.controllers.js :as js]
            [cemerick.friend :as friend])
  (:use [compojure.core :as compojure.core :only (GET PUT POST DELETE ANY defroutes)]
        environ.core))


(defmacro route
  [method path handler]
  `(~method ~path {params# :params}
            (~handler params#)))

(defmacro authroute
  [method path handler]
  (let [params (quote params)]
    `(~method ~path {:keys [~params] :as req#}
              (~handler ~params (friend/current-authentication req#)))))

(defroutes routes
  (authroute GET "/scripts/load-session.js" js/load-session)
  
  (apply compojure.core/routes
         (map #(compojure.route/files "/" {:root %})
              (env :html-paths)))

  ;; Topics
  (route GET "/topics" topics/query)
  (route GET "/topics/:id" topics/show)
  (authroute POST "/topics" topics/create!)
  (authroute DELETE "/topics/:id" topics/delete!)

  ;; Posts
  (authroute POST "/posts" posts/create!)
  (authroute POST "/posts/:id" posts/update!)
  (authroute DELETE "/posts/:id" posts/delete!)

  ;; Users
  (authroute POST "/users" users/registration-success-response)
  (authroute POST "/users/:id/about" users/update-about!)
  (authroute POST "/users/:id/password" users/change-password!)
  (route GET "/users/:id" users/show)

  ;; auth
  (route POST "/login" session/create!)
  (friend/logout
   (ANY "/logout" []
        (ring.util.response/redirect "/")))

  (ANY "/debug" {:keys [x] :as r}
       (str x))
  
  (compojure.route/not-found "Sorry, there's nothing here."))
