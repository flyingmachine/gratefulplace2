(ns gratefulplace.middleware.routes
  (:require compojure.route
            compojure.handler
            [ring.util.response :as resp]
            [gratefulplace.controllers.credential-recovery.forgot-username :as forgot-username]
            [gratefulplace.controllers.credential-recovery.forgot-password :as forgot-password]
            [gratefulplace.controllers.admin.users :as ausers]
            [cemerick.friend :as friend]
            [flyingmachine.webutils.routes :refer :all])
  (:use [compojure.core :as compojure.core :only (GET PUT POST DELETE ANY defroutes)]
        gratefulplace.config))

(doseq [as '[topics watches watched-topics posts likes stats users session js]]
  (require [(symbol (str "gratefulplace.controllers." as)) :as as]))

(def authfn friend/current-authentication)
(defroutemacro auth-resource-routes
  :route-op authroute
  :route-args [authfn])

(defroutes routes
  (authroute GET "/scripts/load-session.js" js/load-session authfn)

  ;; Serve up angular app
  (apply compojure.core/routes
         (map #(compojure.core/routes
                (GET "/" [] (resp/file-response "index.html" {:root %}))
                (GET "/" [] (resp/resource-response "index.html" {:root %})))
              (config :html-paths)))
  
  (apply compojure.core/routes
         (map #(compojure.core/routes
                (compojure.route/files "/" {:root %})
                (compojure.route/resources "/" {:root %}))
              (config :html-paths)))

  ;; Topics
  (auth-resource-routes topics :only [:query :show :create! :delete!])

  ;; Watches
  (auth-resource-routes watches :only [:query :create! :delete!])

  ;; Watched Topics
  (auth-resource-routes watched-topics :only [:query])

  ;; Posts
  (auth-resource-routes posts :only [:create! :update! :delete!])
  (authroute POST "/posts/:id" posts/update! authfn)

  ;; Likes
  (auth-resource-routes likes
                        :only [:create! :delete!]
                        :suffixes [":post-id"])

  ;; Users
  (authroute POST "/users" users/registration-success-response authfn)
  (route GET "/users/:id" users/show)
  (authroute POST "/users/:id" users/update! authfn)
  (authroute POST "/users/:id/password" users/change-password! authfn)

  (route POST "/credential-recovery/forgot-username" forgot-username/create!)
  
  (route GET "/credential-recovery/forgot-password/:token" forgot-password/show)
  (route PUT "/credential-recovery/forgot-password/:token" forgot-password/update!)
  (route POST "/credential-recovery/forgot-password/:token" forgot-password/update!)
  (route POST "/credential-recovery/forgot-password" forgot-password/create!)

  ;; Stats
  (route GET "/stats" stats/query)

  ;; Admin users
  (authroute GET "/admin/users" ausers/query authfn)

  ;; auth
  (route POST "/login" session/create!)
  (friend/logout
   (ANY "/logout" []
        (ring.util.response/redirect "/")))

  (ANY "/debug" {:keys [x] :as r}
       (str x))

  (compojure.route/not-found "Sorry, there's nothing here."))
