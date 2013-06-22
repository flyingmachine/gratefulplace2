(ns gratefulplace.paths
  (use flyingmachine.webutils.paths))

(create-path-fns "user" :username "password")
(create-path-fns "post" :id)
(create-path-fns "topic" :id)