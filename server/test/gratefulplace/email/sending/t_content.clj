(ns gratefulplace.email.sending.t-content
  (:require [gratefulplace.email.sending.content :as email-content]
            [gratefulplace.config :refer [config]])
  (:use midje.sweet))

(def template-vars
  {:var1 "a"
   :var2 "b"
   :htmlvar1 "<p>a</p>"
   :htmlvar2 "<p>b</p>"})

(fact "body returns applied template for text and html"
  (email-content/body "test" template-vars)
  => (contains {:text "Test a b\n"}
               {:html "<p>Test <p>a</p> &lt;p&gt;b&lt;/p&gt;</p>\n"}
               :in-any-order))