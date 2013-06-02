(ns gratefulplace.controllers.shared)

(def author-inclusion-options
  {:author {:exclude [:email]}})

(defn invalid
  [errors]
  {:body {:errors errors}
   :status 412})

(def OK
  {:status 200})>

(def NOT-AUTHORIZED
  {:status 401})

(def NOT-FOUND
  {:status 404})