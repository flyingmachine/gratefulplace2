'use strict';
angular.module("gratefulplaceApp").factory "User", ["$resource", ($resource) ->
  $resource '/users/:id/:attribute', id: '@id',
    changePassword:
      method: 'POST'
      params: {attribute: 'password'}
]