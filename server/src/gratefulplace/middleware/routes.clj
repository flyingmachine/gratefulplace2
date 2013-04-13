(ns gratefulplace.middleware.routes
  (:require compojure.route
            compojure.handler)
  (:use [compojure.core :as compojure.core :only (GET PUT POST ANY defroutes)]
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
  (compojure.route/not-found "Sorry, there's nothing here."))