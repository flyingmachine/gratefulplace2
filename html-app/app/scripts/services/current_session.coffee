'use strict'

angular.module("gratefulplaceApp").factory "CurrentSession", (loadedSession, $q, $http, $rootScope, User)->
  currentSession = null
  loggedOut = !loadedSession

  loadedSessionUser = ->
    unless loggedOut
      user = new User(loadedSession)
    else
      false
  
  get = ()->
    currentSession ||= loadedSessionUser()
  
  get: get
  set: (newSession)->
    currentSession = newSession
    $rootScope.$broadcast "auth.logged-in"
  