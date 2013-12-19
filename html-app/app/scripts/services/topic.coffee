'use strict';
angular.module("gratefulplaceApp").factory "Topic", ["$resource", ($resource) ->
  service = $resource '/topics/:id', id: '@id',
    update: {method: 'PUT'}
]