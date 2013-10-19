'use strict'

angular.module('gratefulplaceApp')
  .controller 'CredentialRecoveryResetPasswordCtrl', ($rootScope, $scope, ForgotPasswordRequest) ->
    # check token expiration
    # show error message if expired
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
    
    