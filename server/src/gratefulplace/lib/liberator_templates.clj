(ns gratefulplace.lib.liberator-templates
  (:require [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.models.permissions :refer :all]
            [gratefulplace.db.mapification :refer :all]
            [flyingmachine.webutils.utils :refer :all]
            [flyingmachine.webutils.validation :refer (if-valid)]))



(defmacro defupdate!
  [& args])