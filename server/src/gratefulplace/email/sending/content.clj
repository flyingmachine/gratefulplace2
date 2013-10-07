(ns gratefulplace.email.sending.content
  (:require [clojure.java.io :as io]
            [stencil.core :as stencil]
            [gratefulplace.utils :refer :all])
  (:import org.apache.commons.mail.HtmlEmail))

(def template-directory "email-templates/")

(defn template-path
  [template-name extension]
  (str template-directory template-name "." extension))

(defn template
  [name extension]
  (-> name
      (template-path extension)
      io/resource
      (ifn slurp)))

(defn body
  [template-name data]
  (let [html-template (template template-name "html")
        text-template (template template-name "txt")]
    {:text(stencil/render-string text-template data)
     :html (stencil/render-string html-template data)}))
