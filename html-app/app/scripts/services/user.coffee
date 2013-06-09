'use strict';
angular.module("gratefulplaceApp").factory "User", ["$resource", ($resource) ->
  $resource '/users/:id', id: '@id'
]