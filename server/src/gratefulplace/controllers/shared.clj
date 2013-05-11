(ns gratefulplace.controllers.shared)

(defmacro id
  []
  '(read-string (re-find #"^\d+$" (:id params))))