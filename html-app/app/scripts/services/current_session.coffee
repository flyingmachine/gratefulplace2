'use strict'

angular.module("gratefulplaceApp").factory "CurrentSession", (loadedSession, $q, $http, $rootScope, User)->
  currentSession =
    loggedIn: -> @id
  loggedOut = !loadedSession

  loadedSessionUser = ->
    unless loggedOut
      user = new User(loadedSession)
    else
      false

  get = ()->
    if currentSession.loggedIn()
      currentSession
    else
      _.merge(currentSession, loadedSessionUser())
  
  get: get
  set: (newSession)->
    _.merge(currentSession, newSession)
    $rootScope.$broadcast "auth.logged-in"
  
