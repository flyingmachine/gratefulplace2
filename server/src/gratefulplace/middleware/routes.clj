(ns gratefulplace.middleware.routes
  (:require compojure.route
            compojure.handler
            [gratefulplace.controllers.topics :as topics])
  (:use [compojure.core :as compojure.core :only (GET PUT POST ANY defroutes)]
        [liberator.core :only [resource]]
        environ.core))


(defmacro route
  [method path & handlers]
  `(~method ~path {params# :params}
            (->> params#
                ~@handlers)))

(defroutes routes
  (apply compojure.core/routes
         (map #(compojure.route/files "/" {:root %})
              (env :html-paths)))

  (route GET "/topics" topics/query)
  (route GET "/topics/:id" topics/show)
  
  (compojure.route/not-found "Sorry, there's nothing here."))