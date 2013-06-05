angular.module('gratefulplaceApp').directive 'errorMessages',  ->
  restrict: 'A'
  replace: true
  scope:
    messages: '=errorMessages'
  template: '<div class="error" ng-show="messages"><ul><li ng-repeat="message in messages">{{message}}</li></ul></div>'