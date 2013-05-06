'use strict'

angular.module('gratefulplaceApp')
  .controller 'LoginCtrl', ($scope, $http, $location, Users) ->
    $scope.errors = {}
    
    $scope.login =
      username: ""
      password: ""

    loginSuccess = ->
      $location.path "/"
      $scope.$apply()

    loginFailure = (response)->
      $scope.errors = response.data.errors
      
    $scope.submitLogin = ->
      $http.post("/login", $scope.login)
        .success(loginSuccess)
        .error(loginFailure)