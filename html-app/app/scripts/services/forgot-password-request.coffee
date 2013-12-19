'use strict';
angular.module("gratefulplaceApp").factory "ForgotPasswordRequest", ["$resource", ($resource) ->
  $resource '/credential-recovery/forgot-password/:token', token: '@token',
    update: {method: 'PUT'}
]