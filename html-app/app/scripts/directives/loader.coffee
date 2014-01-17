'use strict'

angular.module('gratefulplaceApp').directive 'loader', ->
  restrict: 'E'
  template: """
  <div class="zeload" ng-show="loading"></div>
  """
  replace: true
