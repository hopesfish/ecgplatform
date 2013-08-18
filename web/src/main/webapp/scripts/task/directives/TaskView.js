
define(function(require, exports) {

    'use strict';

    var viewTemplate = require("../templates/taskview.html");
    var usercardTemplate = require("../templates/usercard.html");
    var examinationTemplate = require("../templates/examinationview.html");
    var plotTemplate = require("../templates/plot.html");
    var replyTemplate = require("../templates/examinationreply.html");

    angular.module('ecgTaskView', [])
    .controller('TaskViewController', ['$scope', 'EnumService',
    function($scope, EnumService) {
        $scope.taskview = {};
        $scope.taskview.id = '';

        // 监听未完成
       $scope.$watch("todo.current", function() {
           if (!$scope.todo) { return; }
           if (!$scope.todo.current) { return; }

           $scope.taskview.level = $scope.todo.current.level;
       });

       // 监听已完成
       $scope.$watch("task.selected", function() {
           if (!$scope.task) { return; }
           if (!$scope.task.selected) { return; }

           $scope.taskview.level = $scope.task.selected.level;
       });
    }])
    .directive("ecgTaskView", [ '$location', function($location) {
        return {
            restrict : 'A',
            replace : false,
            template : viewTemplate,
            controller : "TaskViewController",
            link : function($scope, $element, $attrs) {
            }
        };
    }])
    .controller('UserCardController', ['$scope', 'UserService', function($scope, UserService) {
       $scope.usercard = {};

       // 监听未完成
       $scope.$watch("todo.current", function() {
           if (!$scope.todo) { return; }
           if (!$scope.todo.current) { return; }

           $scope.taskview.id = $scope.todo.current.id;

           if (!$scope.todo.current.user) {
               $scope.usercard.user = null;
               UserService.get($scope.todo.current.userId)
               .then(function(user) {
                   $scope.usercard.user = user;
                   $scope.todo.current.user = user;
               }, function() {
                   $scope.message.error("加载用户数据失败!");
               });
           } else {
               $scope.usercard.user = $scope.todo.current.user;
           }
       });

       // 监听已完成
       $scope.$watch("task.selected", function() {
           if (!$scope.task) { return; }
           if (!$scope.task.selected) { return; }

           $scope.taskview.id = $scope.task.selected.id;
           
           if (!$scope.task.selected.user) {
               $scope.usercard.user = null;
               UserService.get($scope.task.selected.userId)
               .then(function(user) {
                   $scope.usercard.user = user;
                   $scope.task.selected.user = user;
               }, function() {
                   $scope.message.error("加载用户数据失败!");
               });
           } else {
               $scope.usercard.user = $scope.task.selected.user;
           }
       });
   }])
   .directive("ecgUserCard", [ '$location', function($location) {
       return {
           restrict : 'A',
           replace : false,
           template : usercardTemplate,
           controller : "UserCardController",
           link : function($scope, $element, $attrs) {
           }
       };
   }])
    .controller('ExaminationViewController', ['$scope', 'EnumService', 'TaskService',
       function($scope, EnumService, TaskService) {
        $scope.examinationview = {};
        $scope.examinationview.examination = null;

        $scope.examinationview.getLevelLabel = EnumService.getLevelLabel;
        $scope.examinationview.translateLevel = EnumService.translateLevel;
        // level名称
        $scope.examinationview.getWorkStatusLabel = EnumService.getWorkStatusLabel;

        // 监听未完成
        $scope.$watch('todo.current',function() {
            $scope.examinationview.readonly = false;
            
            if (!$scope.todo) { return; }
            if(!$scope.todo.current) { return; }
            if($scope.todo.current.examination) { 
                $scope.examinationview.examination = $scope.todo.current.examination;
                return; 
            }

            
            $scope.examinationview.examination = null;
            TaskService.getExamination($scope.todo.current.examinationId)
            .then(function(examination) {
                $scope.examinationview.examination = examination;
                $scope.todo.current.examination = examination;
            });
        });

        // 监听已完成
        $scope.$watch('task.selected',function() {
            $scope.examinationview.readonly = true;

            if (!$scope.task) { return; }
            if(!$scope.task.selected) { return; }
            if($scope.task.selected.examination) { return; }

            $scope.examinationview.examination = null;
            TaskService.getExamination($scope.task.selected.examinationId)
            .then(function(examination) {
                $scope.examinationview.examination = examination;
                $scope.task.selected.examination = examination;
            });
        });
    }])
    .directive("ecgExaminationView", [ '$location', function($location) {
        return {
            restrict : 'A',
            replace : false,
            template : examinationTemplate,
            controller : "ExaminationViewController",
            link : function($scope, $element, $attrs) {
            }
        };
    }])
    .controller('PlotController', ['$scope',
    function($scope) {
        $scope.examinationplot = {};
        $scope.examinationplot.examinationId = null;
        $scope.examinationplot.zoom = 'small';
        // 监听未完成
        $scope.$watch('todo.current',function() {
            if (!$scope.todo) { return; }
            if(!$scope.todo.current) { return; }
            $scope.examinationplot.userId = $scope.todo.current.userId;
            $scope.examinationplot.examinationId = $scope.todo.current.examinationId;
        });

        // 监听已完成
        $scope.$watch('task.selected',function() {
            if (!$scope.task) { return; }
            if(!$scope.task.selected) { return; }
            $scope.examinationplot.userId = $scope.task.selected.userId;
            $scope.examinationplot.examinationId = $scope.task.selected.examinationId;
        });
    }])
   .directive("ecgPlot", [ '$location', function($location) {
       return {
           restrict : 'A',
           replace : false,
           template : plotTemplate,
           controller : "PlotController",
           link : function($scope, $element, $attrs) {
           }
       };
   } ])
  .controller('ExaminationReplyController', ['$scope', 'EnumService', 'TaskService', function($scope, EnumService, TaskService) {
      $scope.examinationreply = {};
      $scope.examinationreply.replys = null;
      
      $scope.examinationreply.translateLevel = EnumService.translateLevel;
      // 监听已完成
      $scope.$watch('todo.current',function() {
          if (!$scope.todo) { return; }
          if(!$scope.todo.current) { return; }
          if ($scope.todo.current.replys) {
              $scope.examinationreply.replys = $scope.todo.current.replys;
              return;
          }

          TaskService.getReplyByExamination($scope.todo.current.examinationId)
          .then(function(replys) {
              $scope.examinationreply.replys = replys;
              $scope.todo.current.replys = replys;
          });
      });

      // 监听已完成
      $scope.$watch('task.selected',function() {
          if (!$scope.task) { return; }
          if(!$scope.task.selected) { return; }
          if ($scope.task.selected.replys) {
              $scope.examinationreply.replys = $scope.task.selected.replys;
              return;
          }

          TaskService.getReplyByExamination($scope.task.selected.examinationId)
          .then(function(replys) {
              $scope.examinationreply.replys = replys;
              $scope.task.selected.replys = replys;
          });
      });
  }])
  .directive("ecgExaminationReply", [ '$location', function($location) {
      return {
          restrict : 'A',
          replace : true,
          template : replyTemplate,
          controller : "ExaminationReplyController",
          link : function($scope, $element, $attrs) {
          }
      };
  } ]);
});