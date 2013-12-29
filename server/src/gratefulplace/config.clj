(ns gratefulplace.config
  (:require [flyingmachine.webutils.utils :refer :all]
            [com.flyingmachine.config :refer (defconfig)]
            environ.core))

(defconfig config environ.core/env :app)

(defn print-config
  []
  (println (config)))
