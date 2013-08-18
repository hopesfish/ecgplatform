'use strict';
define(function(require, exports) {

    var template = require("../templates/expertstaskreport.html");

    angular.module('ecgExpertsTaskReport', [])
    .controller('ExpertsTaskReportController', ['$scope', '$q', 'ExpertService', 'TaskService',
    function($scope, $q, ExpertService, TaskService) {
        if (!$scope.overview) { $scope.overview = {}; }

        $scope.overview.expertreport = {};
        $scope.overview.expertreport.experts = null;

        ExpertService.queryAll().then(function(experts) {
            $scope.overview.expertreport.experts = experts;
            $(experts).each(function(i, expert) {
                var querys = [], statuses = ['proceeding', 'completed'];
                expert.proceeding = 0;
                expert.completed = 0;
                expert.percent = 0;

                $(statuses).each(function(i, status) {
                    querys.push(TaskService.queryAllTaskByEmployee(
                        {id: expert.id, roles: "expert"}, 
                        {status: status, 'page.max': 1}
                    ));
                });

                $q.all(querys).then(function(pagings) {
                    var sum = 0;
                    expert.proceeding = pagings[0].total;
                    expert.completed = pagings[1].total;
                    sum = expert.proceeding + expert.completed;
                    if (sum > 0) {
                        expert.percent = parseInt(expert.completed / sum  * 100);
                    }
                });
            });
        });
    }])
    .directive("ecgExpertsTaskReport", [ '$location', function($location) {
        return {
            restrict : 'A',
            replace : false,
            template : template,
            controller : "ExpertsTaskReportController",
            link : function($scope, $element, $attrs) {
            }
        };
    } ]);
});