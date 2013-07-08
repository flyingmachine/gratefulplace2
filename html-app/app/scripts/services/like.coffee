'use strict';
angular.module("gratefulplaceApp").factory "Like", ["$resource", ($resource) ->
  service = $resource '/likes/:id', id: '@id'
]
