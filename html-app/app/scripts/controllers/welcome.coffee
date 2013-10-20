'use strict'

angular.module('gratefulplaceApp')
  .controller 'WelcomeCtrl', ($scope, $http, $location, CurrentSession) ->
    if CurrentSession.get().loggedIn()
      $location.path("/topics")