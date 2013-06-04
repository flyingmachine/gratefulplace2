'use strict'

# This is only here for development purposes. In the live app,
# load_session.js will be generated automatically
angular.module('gratefulplaceApp',)
  .value "loadedSession", { username: "John", id: 1 }