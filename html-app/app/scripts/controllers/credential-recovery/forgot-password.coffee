'use strict'

angular.module('gratefulplaceApp')
  .controller 'CredentialRecoveryForgotPasswordCtrl', ($rootScope, $scope, ForgotPasswordRequest) ->
    $scope.success = false
    $scope.forgotPasswordRequest = {}
    $scope.submitForgotPasswordRequest = ->
      forgotPasswordRequest = new ForgotPasswordRequest($scope.forgotPasswordRequest)
      forgotPasswordRequest.$save(->
        $scope.success = true
        $scope.forgotPasswordRequest = {}
        $scope.errorMessages = null
      , (res)->
        $scope.errorMessages = res.data.errors
      )
    
    