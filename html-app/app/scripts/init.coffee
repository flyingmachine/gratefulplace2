environment = switch window.location.port
  when '9000' then 'prototype'
  when '8080' then 'development'
  when '80'   then 'production'
  else 'test'


prototypeMocks = angular.module('prototypeMocks', [])
prototypeMocks.factory "$resource", ['$q', '$http', ($q, $http)->
  $save = (success, fail)->
    success(@)
  
  (url)->
    url = "/data" + url.replace("/:id", "")
    constructor = (obj)->
      obj.$save = $save
      obj
    
    constructor.query = (fn)->
      $http.get("#{url}.json").then (data)->
        fn(_.map(data.data, (r)-> new constructor(r)))
    constructor.get = (options, fn)->
      @query (records)->
        record = _.find(records, (record)-> parseInt(record.id) == parseInt(options.id))
        fn(new constructor(record))
    constructor
  ]

modules = ['ngResource', 'btford.markdown']
switch environment
  when 'prototype' then modules.push 'prototypeMocks'

@App =
  config: {}
  environment: environment
  modules: modules