'use strict';
angular.module("gratefulplaceApp").factory "Post", ["$resource", ($resource) ->
  $resource '/posts/:id', id: '@id'
]