'use strict';
angular.module("gratefulplaceApp").factory "User", ["$resource", ($resource) ->
  $resource '/users/:id/:attribute', id: '@id',
    update: {method: 'PUT'}
    changePassword:
      method: 'POST'
      params: {attribute: 'password'}
]