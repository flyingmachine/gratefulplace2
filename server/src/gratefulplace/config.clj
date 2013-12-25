(ns gratefulplace.config
  (:require [flyingmachine.webutils.utils :refer :all]
            [environ.core :refer :all]))

(def conf
  (let [environment (or (env :app-env) "development")]
    (merge
     {:app-env environment
      
      :moderator-names ["flyingmachine"]}
     (read-resource (str "config/environments/" environment ".edn")))))

(defn config
  [& keys]
  (get-in conf keys))

(defn print-config
  []
  (println env))