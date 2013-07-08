angular.module('gratefulplaceApp').directive 'date', ->
  restrict: 'EA'
  scope:
    date: '=data'
  controller: ['$scope', ($scope)->
    $scope.formatDateTime = (date)->
      moment(date).format("MMM D, YYYY h:mma")
    
  ]
  template: "<div class='date'>{{formatDateTime(date)}}</div>"
  replace: true
