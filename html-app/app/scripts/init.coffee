environment = switch window.location.port
  when '9000' then 'prototype'
  when '8080' then 'development'
  when '80'   then 'production'
  else 'test'


prototypeMocks = angular.module('prototypeMocks', [])
prototypeMocks.factory "$resource", ['$q', '$http', ($q, $http)->
  (url)->
    url = "/data" + url.replace("/:id", "")
    query: (fn)->
      $http.get("#{url}.json").then (data)->
        fn(data.data)
    get: (options, fn)->
      @query (records)->
        fn(_.find(records, (record)-> parseInt(record.id) == parseInt(options.id)))
  ]

@App =
  config: {}
  environment: environment
  modules: switch environment
    when 'prototype' then ['ngResource', 'prototypeMocks']
    else ['ngResource']