(ns gratefulplace.models.t-mailer
  (:require [gratefulplace.models.mailer :as mailer])
  (:use midje.sweet))

(fact "body returns applied template for text and html"
  (mailer/body "test" {:var1 "a" :var2 "b" :htmlvar1 "<p>a</p>" :htmlvar2 "<p>b</p>"})
  => (contains {:text "Test a b\n"}
               {:html "<p>Test <p>a</p> &lt;p&gt;b&lt;/p&gt;</p>\n"}
               :in-any-order))