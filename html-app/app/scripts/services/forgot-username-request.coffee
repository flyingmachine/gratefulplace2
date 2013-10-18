'use strict';
angular.module("gratefulplaceApp").factory "ForgotUsernameRequest", ["$resource", ($resource) ->
  $resource '/credential-recovery/forgot-username/:id', id: '@id'
]