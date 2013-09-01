'use strict';
angular.module("gratefulplaceApp").factory "AdminUser", ["$resource", ($resource) ->
  $resource '/admin/users/:id', id: '@id'
]