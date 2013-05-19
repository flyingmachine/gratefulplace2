(ns gratefulplace.utils)

(defn remove-nils-from-map
  [record]
  (into {} (remove (comp nil? second) record)))