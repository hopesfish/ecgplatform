'use strict';
define(function(require, exports) {

require("./services/MonitorService");
require("./directives/TaskCurrentReport");
require("./directives/EmployeeStatusReport");
require("./directives/TaskProcessReport");
require("./directives/ExpertsTaskReport");
require("./directives/OperatorsTaskReport");
require("./directives/RangeSelector");

var overviewTemp = require("./templates/overview.html");

angular.module('ecgMonitor', 
	['ecgMonitorService',
	 'ecgTaskCurrentReport',
	 'ecgEmployeeStatusReport',
	 'ecgTaskProcessReport',
	 'ecgExpertsTaskReport',
	 'ecgOperatorsTaskReport',
   'ecgRangeSelector'])
.controller('OverviewController', ['$scope', function ($scope) {
    // register the inner namespace
    $scope.overview = {};
    $scope.subheader.title = "工作巡查";
}])
.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
    .when('/overview', {
      template: overviewTemp,
      controller: 'OverviewController'
    });
}]);

});// end of define