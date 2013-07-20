'use strict';
define(function(require, exports) {

    var template = require("../templates/taskcurrentreport.html");

    angular.module('ecgTaskCurrentReport', [])
    .controller('TaskCurrentReportController', ['$scope', 'TaskService',
    function($scope, TaskService) {
        if (!$scope.overview) { $scope.overview = {}; }

        $scope.overview.currentreport = {pending: '-', proceeding: '-', completed: '-'};

        $(['pending', 'proceeding', 'completed']).each(function(i, status) {
            TaskService.queryAllTaskByEmployee({}, {status: status})
            .then(function(results) {
                $scope.overview.currentreport[status] = results.length;
            });
        });
    }])
    .directive("ecgTaskCurrentReport", [ '$location', function($location) {
        return {
            restrict : 'A',
            replace : false,
            template : template,
            controller : "TaskCurrentReportController",
            link : function($scope, $element, $attrs) {
            }
        };
    } ]);
});