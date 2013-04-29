'use strict';
angular.module("gratefulplaceApp")
  .factory "Users", ["$resource", ($resource) ->
    $resource '/users/:id'
  ]