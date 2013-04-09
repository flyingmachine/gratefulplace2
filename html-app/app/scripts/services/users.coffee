'use strict';
angular.module("gratefulplaceApp")
  .factory "Users", ["$http", "$q", ($http, $q) ->
    # Public API here
    all: ->
      deferred = $q.defer()
      $http.get("data/users.json").then (data)->
        deferred.resolve(data.data)
      deferred.promise
    find: (id)->
      deferred = $q.defer()
      @all().then (topics)->
        deferred.resolve _.find(topics, (topic)-> parseInt(topic.id) == parseInt(id))
      deferred.promise
  ]