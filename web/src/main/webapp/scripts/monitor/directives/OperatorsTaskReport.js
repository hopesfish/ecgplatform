'use strict';
define(function(require, exports) {

    var template = require("../templates/operatorstaskreport.html");

    angular.module('ecgOperatorsTaskReport', [])
    .controller('OperatorsTaskReportController', ['$scope', '$q', 'OperatorService', 'TaskService',
    function($scope, $q, OperatorService, TaskService) {
        if (!$scope.overview) { $scope.overview = {}; }

        $scope.overview.operatorreport = {};
        $scope.overview.operatorreport.operators = null;

        OperatorService.queryAll().then(function(operators) {
            $scope.overview.operatorreport.operators = operators;
            $(operators).each(function(i, operator) {
                var querys = [], statuses = ['pending', 'completed'];
                operator.pending = 0;
                operator.completed = 0;
                operator.percent = 0;

                $(statuses).each(function(i, status) {
                    querys.push(TaskService.queryAllTaskByEmployee(
                        {id: operator.id, roles: "operator"}, 
                        {status: status}
                    ));
                });

                $q.all(querys).then(function(results) {
                    var sum = 0;
                    operator.pending = results[0].length;
                    operator.completed = results[1].length;
                    sum = operator.pending + operator.completed;
                    if (sum > 0) {
                        operator.percent = parseInt(operator.completed / sum * 100);
                    }
                });
            });
        });
    }])
    .directive("ecgOperatorsTaskReport", [ '$location', function($location) {
        return {
            restrict : 'A',
            replace : false,
            template : template,
            controller : "OperatorsTaskReportController",
            link : function($scope, $element, $attrs) {
            }
        };
    } ]);
});