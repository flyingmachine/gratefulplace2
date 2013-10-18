'use strict';
angular.module("gratefulplaceApp").factory "ForgotPasswordRequest", ["$resource", ($resource) ->
  $resource '/credential-recovery/forgot-password/:id', id: '@id'
]