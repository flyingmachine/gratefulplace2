angular.module("gratefulplaceApp").factory "Paginator", ['$location', ($location)->
  class Paginator
    constructor: ()->
      @data = {}
      @data['current-page'] = if $location.search().page
        parseInt $location.search().page
      else
        1
    receive: (data)->
      _.merge(@data, data[0])
      _.rest(data)
      
  Paginator
]