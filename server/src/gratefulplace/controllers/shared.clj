(ns gratefulplace.controllers.shared)

(defn id
  [str-id]
  (read-string (re-find #"^\d+$" str-id)))