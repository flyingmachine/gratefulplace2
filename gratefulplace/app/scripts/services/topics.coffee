'use strict';
angular.module("gratefulplaceApp")
  .factory "Topics", ["$http", "$q", ($http, $q) ->
    # Public API here
    all: ->
      deferred = $q.defer()
      $http.get("data/topics.json").then (data)->
        deferred.resolve(data.data)
      deferred.promise
    find: ->
      single = $q.defer()
      @all().then (topics)->
        single.resolve
      single.promise
  ]