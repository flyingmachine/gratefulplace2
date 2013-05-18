'use strict'
angular.module("gratefulplaceApp").factory "CurrentSession", (loadedSession, $http, $rootScope)->
  currentSession = null
  loggedOut = !loadedSession
  
  get = ()->
    currentSession || (!loggedOut && loadedSession)
  
  get: get
  set: (newSession)->
    currentSession = newSession
    $rootScope.$broadcast "auth.logged-in"
  logout: ->
    $http.get('/logout')
    currentSession = null
    loggedOut = true
    $rootScope.$broadcast "auth.logged-out"
  