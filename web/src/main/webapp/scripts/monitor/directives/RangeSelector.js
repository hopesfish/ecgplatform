'use strict';
define(function(require, exports) {

    var template = require("../templates/rangeselector.html");

    angular.module('ecgRangeSelector', [])
    .controller('RangeSelectorController', ['$scope', 'TaskService',
    function($scope, TaskService) {
    }])
    .directive("ecgRangeSelector", [ '$location', function($location) {
        return {
            restrict : 'A',
            replace : true,
            template : template,
            controller : "RangeSelectorController",
            link : function($scope, $element, $attrs) {
            }
        };
    } ]);
});