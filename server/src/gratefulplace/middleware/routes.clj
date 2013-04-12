(ns gratefulplace.middleware.routes
  (:require compojure.route
            compojure.handler)
  (:use [compojure.core :only (GET PUT POST ANY defroutes)]))


(defmacro route
  [method path & handlers]
  `(~method ~path {params# :params}
            (->> params#
                ~@handlers)))

(defroutes routes
  (compojure.route/files "/" {:root "../html-app/targets/public"})
  (compojure.route/not-found "Sorry, there's nothing here.")

  )