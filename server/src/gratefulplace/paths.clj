(ns gratefulplace.paths
  (use flyingmachine.webutils.paths))

(create-path-fns "user" :username "edit" "posts" "comments" "notification-settings")
(create-path-fns "post" :id)
(create-path-fns "topic" :id)