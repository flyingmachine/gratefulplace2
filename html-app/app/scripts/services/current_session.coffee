'use strict'

angular.module("gratefulplaceApp").factory "CurrentSession", (loadedSession, $http, $rootScope, User)->
  currentSession = null
  loggedOut = !loadedSession

  loadedSessionUser = ->
    unless loggedOut
      new User(loadedSession)
    else
      false
  
  get = ()->
    currentSession ||= loadedSessionUser()
  
  get: get
  set: (newSession)->
    currentSession = newSession
    $rootScope.$broadcast "auth.logged-in"
  logout: ->
    $http.get('/logout')
    currentSession = null
    loggedOut = true
    $rootScope.$broadcast "auth.logged-out"
  