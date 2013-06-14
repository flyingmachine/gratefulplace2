(ns gratefulplace.controllers.t-users
  (:use midje.sweet)
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.manage :as db-manage]
            [gratefulplace.controllers.users :as users]))

(background
 (before :contents (tdb/with-test-db (db-manage/recreate) (db-manage/load-schema))))

(defn auth
  ([] (auth "flyingmachine"))
  ([username]
     {:id (:db/id (q/one [:user/username username]))
      :username username}))

(tdb/with-test-db
  (fact "valid password change params update a user's password"
    (users/change-password! {:id (:id (auth))
                             :current-password "password"
                             :new-password "new-password"
                             :new-password-confirmation "new-password"}
                            (auth))
    => (contains {:status 200})))
