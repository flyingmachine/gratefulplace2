(ns gratefulplace.utils)

(defn remove-key-namespace
  [k]
  (keyword (clojure.string/replace (str k) #".*?\/" "")))
