'use strict';
angular.module("gratefulplaceApp").factory "Watch", ["$resource", ($resource) ->
  $resource '/watches/:id', id: '@id'
]