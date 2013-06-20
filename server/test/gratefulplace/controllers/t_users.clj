(ns gratefulplace.controllers.t-users
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.manage :as db-manage]
            [gratefulplace.controllers.users :as users])
  (:use midje.sweet
        gratefulplace.controllers.test-helpers))

(setup-db-background)

(fact "valid password change params update a user's password"
      (users/change-password! {:id (:id (auth))
                               :current-password "password"
                               :new-password "new-password"
                               :new-password-confirmation "new-password"}
                              (auth))
      => (contains {:status 200}))
