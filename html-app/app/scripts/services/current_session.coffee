'use strict'
angular.module("gratefulplaceApp").factory "CurrentSession", (loadedSession)->
  currentSession = {}
  get = ()->
    currentSession.username || loadedSession.username
  
  get: -> loadedSession
  