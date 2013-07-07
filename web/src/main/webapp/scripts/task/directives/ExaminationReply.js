'use strict';
define(function(require, exports) {

    var template = require("../templates/examinationreply.html");

    angular.module('ecgExaminationReply', [])
    .controller('ExaminationReplyController', ['$scope', 'TaskService',
    function($scope, TaskService) {
        $scope.examinationreply = {};
        $scope.examinationreply.reply = null;
        
        // 监听已完成
        $scope.$watch('done.selected',function() {
            if (!$scope.done) { return; }
            if(!$scope.done.selected) { return; }

            TaskService.getReplyByExamination($scope.done.selected.examinationId)
            .then(function(reply) {
                $scope.examinationreply.reply = reply;
            });
        });
    }])
    .directive("ecgExaminationReply", [ '$location', function($location) {
        return {
            restrict : 'A',
            replace : false,
            template : template,
            controller : "ExaminationReplyController",
            link : function($scope, $element, $attrs) {
            }
        };
    } ]);
});