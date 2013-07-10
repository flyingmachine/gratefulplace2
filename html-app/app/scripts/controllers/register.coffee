'use strict'

angular.module('gratefulplaceApp')
  .controller 'RegisterCtrl', ($scope, $location, CurrentSession, User) ->
    $scope.errors = {}
    
    $scope.registration =
      username: ""
      password: ""
      email: ""

    registrationSuccess = (response)->
      CurrentSession.set(response)
      $location.path "/"

    registrationFailure = (response)->
      $scope.errors = response.data.errors
      
    $scope.submitRegistration = ->
      user = new User($scope.registration)
      user.$save([], registrationSuccess, registrationFailure)
      
