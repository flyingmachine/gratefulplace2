'use strict'

angular.module('gratefulplaceApp').directive 'paginationNav', ->
  restrict: 'E'
  scope:
    paginationData: "="
  controller: ['$scope', ($scope)->
    $scope.pageNums = ->
      console.log $scope
      lastPage = $scope.paginationData['page-count'] || 1
      _.range(1, lastPage + 1)
      
    $scope.isActive = (pageNum)->
      pageNum == $scope.paginationData['current-page']
    console.log $scope.paginationData
  ]
  template: """
  <div class="pagination-nav">
    <ul class="page-links">
      <li ng-repeat="pageNum in pageNums()">
        <a href="/#/?page={{pageNum}}" ng-class="{active: isActive(pageNum)}" >{{pageNum}}</a>
      </li>
    </ul>
    <div class="topic-count"></div>
  </div>
  """
  replace: true