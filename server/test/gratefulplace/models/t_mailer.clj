(ns gratefulplace.models.t-mailer
  (:require [gratefulplace.models.mailer :as mailer])
  (:use midje.sweet))

(fact "body returns applied template for text and html"
  (mailer/body "test" {:var1 "a" :var2 "b" :htmlvar1 "<p>a</p>" :htmlvar2 "<p>b</p>"})
  => (contains {:content "Test a b\n" :type "text/plain"}
               {:content "<p>Test <p>a</p> &lt;p&gt;b&lt;/p&gt;</p>\n", :type "text/html"}
               :in-any-order))