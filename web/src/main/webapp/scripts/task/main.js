define(function(require, exports) {

'use strict';
    
require("./services/TaskService");
require("./directives/TaskView");
require("./directives/ReplyForm");

var todoTemp = require("./templates/todo.html");
var todobarTemplate = require("./templates/todobar.html");
var taskTemp = require("./templates/list.html");

angular.module('ecgTask', ['ecgTaskService', 'ecgTaskView', 'ecgReplyForm'])
.controller('TodoTaskController',
    ['$scope', '$routeParams', '$location', 'ProfileService', 'TaskService',
    function ($scope, $routeParams, $location, ProfileService, TaskService) {
    $scope.subheader.title = "待办工作";

    // 基本变量
    $scope.todo = {};    
    $scope.todo.tasks = null;
    $scope.todo.current = null;
    $scope.todo.replyform = 'hidden';

    function refreshGrid() {
        var user = $scope.session.user, isEmployee = user.isEmployee;
        if (isEmployee && isEmployee()) {
            $scope.dialog.showLoading();
            TaskService.queryAllTaskByEmployee(
                user, 
                {
                    status: 'undone',
                    id: $routeParams.id || ''
                }
            ).then(function(tasks) {
                $scope.dialog.hideStandby();
                $scope.todo.tasks = tasks;
                selectTask();
            });
            // 查总数
            TaskService.queryAllTaskByEmployee(
                user, 
                {status: 'undone'}
            ).then(function(tasks) {
                $scope.subheader.title = "待办工作(共" + tasks.length + "条)";
            });
        } else {
            $location.path("/task");
        }
    }

    $scope.$watch("session.user", function() {
        if (!$scope.session.user.id) { return; }
        refreshGrid();
    });
 
    // 将第一个作为当前选中view
    function selectTask() {
        var len = $scope.todo.tasks.length;
        if (len == 0) { return; }
        $scope.todo.current = $scope.todo.tasks[len - 1];
    };

    // 展开/收缩窗口
    $scope.todo.reply = function(position) {
        if ($scope.todo.replyform == position) {
            $scope.todo.replyform = 'hidden';
        } else {
            $scope.todo.replyform = position;
        }
    };

    // 移动到下一个
    $scope.todo.shift = function() {
        if ($routeParams.id) {
            $location.path("todo");
        } else {
            var len = $scope.todo.tasks.length;
            $scope.todo.current = null;
            $scope.todo.tasks.splice(len - 1, 1);
            $scope.todo.replyform = 'hidden';
            if ($scope.todo.tasks.length > 0) {
                selectTask();
            } else {
                refreshGrid();
            }
        }
    };

    // ignore
    $scope.todo.ignore = function() {
        if ($routeParams.id) {
            $location.path("todo");
        } else {
            var len = $scope.todo.tasks.length;
            
            if (len == 1) {
                refreshGrid();
                return;
            }

            $scope.todo.current = null;
            $scope.todo.tasks.splice(len - 1, 1);
            $scope.todo.replyform = 'hidden';
            selectTask();
        }
    };
}])
.controller('TaskController', ['$scope', 'EnumService', 'ProfileService', 'TaskService', function ($scope, EnumService, ProfileService, TaskService) {
    $scope.subheader.title = "工作清单";

    $scope.task = {};
    $scope.task.data = null;
    $scope.task.selected = null;

    // level名称
    $scope.task.getLevelLabel = EnumService.getLevelLabel;
    // level名称
    $scope.task.getWorkStatusLabel = EnumService.getWorkStatusLabel;

    $scope.task.translateLevel = EnumService.translateLevel;

    function refreshGrid() {
        var user = $scope.session.user, isEmployee = user.isEmployee;
        if (isEmployee && isEmployee()) {
            $scope.dialog.showLoading();
            TaskService.queryAllTaskByEmployee(user).then(function(tasks) {
                $scope.dialog.hideStandby();
                $scope.task.data = tasks;
            });
        } else {
            $scope.dialog.showLoading();
            TaskService.queryAllTaskByUser(user).then(function(tasks) {
                $scope.dialog.hideStandby();
                $scope.task.data = tasks;
            });
        }
    }

    $scope.$watch("session.user", function() {
        if (!$scope.session.user.id) { return; }
        refreshGrid();
    });

    $scope.task.refreshGrid = refreshGrid;
}])
.controller('TodoBarController', ['$scope', '$attrs', 'TaskService', function($scope, $attrs, TaskService) {
    if (!$scope.todo.replyformposition1) {
        $scope.todo.replyformposition1 = $attrs['position'];
    } else if ($scope.todo.replyformposition1) {
        $scope.todo.replyformposition2 = $attrs['position'];
    }
    
}])
.directive("ecgTodoBar", [ '$location', function($location) {
  return {
      restrict : 'A',
      replace : true,
      template : todobarTemplate,
      controller : "TodoBarController",
      link : function($scope, $element, $attrs) {
      }
  };
}])
.config(['$routeProvider', function ($routeProvider) {
$routeProvider
.when('/todo', {
    template: todoTemp,
    controller: 'TodoTaskController'
})
.when('/todo/:id', {
    template: todoTemp,
    controller: 'TodoTaskController'
})
.when('/task', {
    template: taskTemp,
    controller: 'TaskController'
});
}]);

});// end of define