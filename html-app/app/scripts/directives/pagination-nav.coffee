'use strict'

angular.module('gratefulplaceApp').directive 'paginationNav', ->
  restrict: 'E'
  scope:
    paginationData: "="
  controller: ['$scope', '$location', ($scope, $location)->
    $scope.pageNums = ->
      lastPage = $scope.paginationData['page-count'] || 1
      _.range(1, lastPage + 1)
      
    $scope.isActive = (pageNum)->
      pageNum == $scope.paginationData['current-page']

    $scope.page = (pageNum, $event)->
      $event.preventDefault()
      $location.search _.merge($location.search(), page: pageNum)
  ]
  template: """
  <div class="pagination-nav">
    <ul class="page-links">
      <li ng-repeat="pageNum in pageNums()">
        <a href="#" ng-click="page(pageNum, $event)" ng-class="{active: isActive(pageNum)}" >{{pageNum}}</a>
      </li>
    </ul>
    <div class="ent-count"></div>
  </div>
  """
  replace: true