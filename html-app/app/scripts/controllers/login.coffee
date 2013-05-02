'use strict'

angular.module('gratefulplaceApp')
  .controller 'LoginCtrl', ($scope, Users) ->
    $scope.errors = {}
    
    $scope.registration =
      username: ""
      password: ""
      email: ""

    registrationSuccess = ->

    registrationFailure = (response)->
      $scope.errors = response.data.errors
      
    $scope.submitRegistration = ->
      user = new Users($scope.registration)
      user.$save([], registrationSuccess, registrationFailure)
      