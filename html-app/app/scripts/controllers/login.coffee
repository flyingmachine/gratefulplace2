'use strict'

angular.module('gratefulplaceApp')
  .controller 'LoginCtrl', ($scope, $http, $location, CurrentSession) ->
    $scope.errors = {}
    
    $scope.login =
      username: ""
      password: ""

    loginSuccess = (response)->
      CurrentSession.set(response)
      $location.path "/"

    loginFailure = (response)->
      $scope.errors = response.errors
      
    $scope.submitLogin = ->
      $http.post("/login", $scope.login)
        .success(loginSuccess)
        .error(loginFailure)