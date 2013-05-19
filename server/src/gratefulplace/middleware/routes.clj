(ns gratefulplace.middleware.routes
  (:require compojure.route
            compojure.handler
            [gratefulplace.controllers.topics :as topics]
            [gratefulplace.controllers.users :as users]
            [gratefulplace.controllers.session :as session]
            [gratefulplace.controllers.js :as js]
            [cemerick.friend :as friend])
  (:use [compojure.core :as compojure.core :only (GET PUT POST ANY defroutes)]
        [liberator.core :only [resource]]
        environ.core))


(defmacro route
  [method path & handlers]
  `(~method ~path {params# :params}
            (->> params#
                ~@handlers)))

(defroutes routes
  (route GET "/scripts/load_session.js" js/load_session)
  
  (apply compojure.core/routes
         (map #(compojure.route/files "/" {:root %})
              (env :html-paths)))

  ;; Topics
  (route GET "/topics" topics/query)
  (route GET "/topics/:id" topics/show)
  (route POST "/topics" topics/create!)

  ;; Users
  (POST "/users" [] users/registration-success-response)
  (route GET "/users/:id" users/show)

  ;; auth
  (route POST "/login" session/create!)
  (friend/logout
   (ANY "/logout" []
        (ring.util.response/redirect "/")))
  
  (compojure.route/not-found "Sorry, there's nothing here."))
