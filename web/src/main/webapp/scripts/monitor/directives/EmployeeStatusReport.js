'use strict';
define(function(require, exports) {

    var template = require("../templates/employeestatusreport.html");

    angular.module('ecgEmployeeStatusReport', [])
    .controller('EmployeeStatusReportController', ['$scope', '$q', 'ExpertService', 'OperatorService',
    function($scope, $q, ExpertService, OperatorService) {
        $scope.employeestatus = {
            expert: {online: 0, away: 0},
            operator: {online: 0, away: 0}
        };

        $(['online', 'away']).each(function(i, status) {
            ExpertService.getTotal({status: status.toUpperCase()}).then(function(total) {
                $scope.employeestatus.expert[status] = total;
            });

            OperatorService.getTotal({status: status.toUpperCase()}).then(function(total) {
                $scope.employeestatus.operator[status] = total;
            });
        });
    }])
    .directive("ecgEmployeeStatusReport", ['$location', function($location) {
        return {
            restrict : 'A',
            replace : false,
            template : template,
            controller : "EmployeeStatusReportController",
            link : function($scope, $element, $attrs) {
            }
        };
    } ]);
});