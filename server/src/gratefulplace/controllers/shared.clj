(ns gratefulplace.controllers.shared)

(def author-inclusion-options
  {:author {:exclude [:email]}})

(defn invalid
  [errors]
  {:body {:errors errors}
   :status 412})