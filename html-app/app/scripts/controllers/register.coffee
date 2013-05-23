'use strict'

angular.module('gratefulplaceApp')
  .controller 'RegisterCtrl', ($scope, $location, CurrentSession, Users) ->
    $scope.errors = {}
    
    $scope.registration =
      username: ""
      password: ""
      email: ""

    registrationSuccess = (response)->
      console.log "Reg success", response
      CurrentSession.set(response)
      $location.path "/"

    registrationFailure = (response)->
      $scope.errors = response.data.errors
      
    $scope.submitRegistration = ->
      user = new Users($scope.registration)
      user.$save([], registrationSuccess, registrationFailure)
      