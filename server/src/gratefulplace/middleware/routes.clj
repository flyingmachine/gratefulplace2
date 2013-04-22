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

(defmacro json-resource
  [resource-name]
  `(ANY (str "/" ~resource-name)
        []
        (resource
         :available-media-types ["application/json"]
         :handle-ok #(~(symbol (str resource-name "/query"))
                      (get-in % [:request :params])))))

(defroutes routes
  (apply compojure.core/routes
         (map #(compojure.route/files "/" {:root %})
              (env :html-paths)))

  ;; (route ANY "/topics" topics/query)
  (json-resource "topics")

  (compojure.route/not-found "Sorry, there's nothing here."))