define(function(require, exports) {

'use strict';
    
require("./services/TaskService");
require("./directives/TaskView");
require("./directives/ReplyForm");

var todoTemp = require("./templates/todo.html");
var todobarTemplate = require("./templates/todobar.html");
var taskTemp = require("./templates/list.html");
var examinationTemp = require("./templates/examination.html");

angular.module('ecgTask', ['ecgTaskService', 'ecgTaskView', 'ecgReplyForm'])
.controller('TodoTaskController',
    ['$scope', '$route', '$timeout', '$routeParams', '$location', 'ProfileService', 'TaskService',
    function ($scope, $route, $timeout, $routeParams, $location, ProfileService, TaskService) {
    $scope.subheader.title = "待办工作";

    // 基本变量
    $scope.todo = {};    
    $scope.todo.tasks = null;
    $scope.todo.current = null;
    $scope.todo.replyform = 'hidden';
    $scope.todo.cursor = 0;
    $scope.todo.total = 0;

    var refreshHandler = null;

    function refreshGrid(opts) {
        var user = $scope.session.user, opts = opts || {auto: false};

        // 如果当前用户正在操作,不刷新
        if (opts.auto && 
            $scope.todo.current &&
            $scope.todo.tasks &&
            $scope.todo.current.id !== $scope.todo.tasks[$scope.todo.tasks.length - 1].id) {
            return;
        } else {
            if(refreshHandler) {
                $timeout.cancel(refreshHandler);
            }
        }

        $scope.dialog.showLoading();
        TaskService.queryAllTaskByEmployee(
            user, 
            {
                status: 'undone',
                id: $routeParams.id || ''
            }
        ).then(function(paging) {
            var tasks = paging.datas;
            $scope.todo.total = paging.total;
            $scope.dialog.hideStandby();
            $scope.todo.tasks = tasks;
            $scope.todo.cursor = $scope.todo.tasks.length - 1;
            $scope.subheader.title = "待办工作(共" + paging.total + "条)";
            selectTask();
        });

        // 60秒后刷新
        $timeout.cancel(refreshHandler);
        refreshHandler = $timeout(function() {
            refreshGrid({auto: true});
        }, 1000 * 60 * 5);
    }

    $scope.$on('$routeChangeStart', function(next, current) { 
        $timeout.cancel(refreshHandler);
    });

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
    $scope.todo.refresh = function() {
        refreshGrid();
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
                $scope.todo.total--;
                $scope.subheader.title = "待办工作(共" + $scope.todo.total + "条)";
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
            if ($scope.todo.cursor == 0) {
                refreshGrid();
                return;
            }
            $scope.todo.current = $scope.todo.tasks[$scope.todo.cursor--];
            $scope.todo.replyform = 'hidden';
        }
    };
}])
.controller('TaskController', ['$scope', '$timeout', 'EnumService', 'ProfileService', 'TaskService',
    function ($scope, $timeout, EnumService, ProfileService, TaskService) {
    $scope.subheader.title = "工作清单";

    $scope.task = {};
    $scope.task.data = null;
    $scope.task.selected = null;

    $scope.task.paging = {
        total: 0,
        curPage: 1,
        goToPage: function(page, max) {
            refreshGrid({'page.curPage': page, 'page.max': max});
        }
    };

    // level名称
    $scope.task.getLevelLabel = EnumService.getLevelLabel;
    // level名称
    $scope.task.getWorkStatusLabel = EnumService.getWorkStatusLabel;

    $scope.task.translateLevel = EnumService.translateLevel;

    var refreshHandler = null;

    function refreshGrid(opts) {
        var opts = opts || {auto: false};
        if (!opts.auto && refreshHandler) {
            $timeout.cancel(refreshHandler);
        }

        var user = $scope.session.user;
        
        $scope.dialog.showLoading();
        TaskService.queryAllTaskByEmployee(user, opts)
        .then(function(paging) {
            var tasks = paging.datas;
            $scope.dialog.hideStandby();
            $scope.task.selected = null;
            $scope.task.paging.total = paging.total;
            $scope.task.paging.curPage = paging.curPage;
            $scope.task.data = tasks;
        });

        $timeout.cancel(refreshHandler);
        refreshHandler = $timeout(function() {
            refreshGrid({auto: true});
        }, 1000 * 60 * 10);
    }

    $scope.$on('$routeChangeStart', function(next, current) {
        $timeout.cancel(refreshHandler);
    });

    $scope.$watch("session.user", function() {
        if (!$scope.session.user.id) { return; }
        if (!$scope.session.user.isEmployee) {
            $scope.subheader.title = "体检记录";
        }
        refreshGrid();
    });

    $scope.task.refresh = refreshGrid;

    // 删除功能
    $scope.task.confirmDelete = function() {
        var selectedItem = $scope.task.selected;
        if (selectedItem === null) {
            $scope.dialog.alert({
                text: '请选择一条记录!'
            });
            return;
        }
        $scope.dialog.confirm({
            text: "请确认删除ID为:" + selectedItem.id + "的体检请求, 该操作无法恢复!",
            handler: function() {
                $scope.dialog.showStandby();
                TaskService.remove(selectedItem)
                .then(function() {
                    $scope.dialog.hideStandby();
                    $scope.task.selected = null;
                    $scope.message.success("删除记录成功!");
                    // 刷新
                    refreshGrid();
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("无法删除该请求,可能是您的权限不足,请联系管理员!");
                });
            }
        });
    };
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
.controller('ExaminationController', ['$scope', '$timeout', '$routeParams', '$location', 'EnumService', 'ProfileService', 'TaskService',
    function ($scope, $timeout,$routeParams, $location, EnumService, ProfileService, TaskService) {
    $scope.subheader.title = "体检记录";

    $scope.task = {};
    $scope.task.data = null;
    $scope.task.selected = null;
    $scope.task.showDetail = false;

    $scope.task.paging = {
        total: 0,
        curPage: 1,
        goToPage: function(page, max) {
            refreshGrid({'page.curPage': page, 'page.max': max});
        }
    };

    // level名称
    $scope.task.getLevelLabel = EnumService.getLevelLabel;
    // level名称
    $scope.task.getWorkStatusLabel = EnumService.getWorkStatusLabel;

    $scope.task.translateLevel = EnumService.translateLevel;

    $scope.task.view = function(item, bool) {
        $scope.task.showDetail = bool;
        $scope.task.selected = item;
        if (bool) {
            $location.path("examination/" + item.id);
        }
    };


    var refreshHandler = null;

    function refreshGrid(opts) {
        var opts = opts || {auto: false};
        if (!opts.auto && refreshHandler) {
            $timeout.cancel(refreshHandler);
        }

        var user = $scope.session.user;
        
        $scope.dialog.showLoading();
        TaskService.queryAllTaskByUser(user, opts)
        .then(function(paging) {
            var tasks = paging.datas;
            $scope.dialog.hideStandby();
            $scope.task.selected = null;
            $scope.task.paging.total = paging.total;
            $scope.task.paging.curPage = paging.curPage;
            $scope.task.data = tasks;
        });

        $timeout.cancel(refreshHandler);
        refreshHandler = $timeout(function() {
            refreshGrid({auto: true});
        }, 1000 * 60 * 10);
    }

    $scope.$on('$routeChangeStart', function(next, current) { 
        $timeout.cancel(refreshHandler);
    });

    $scope.$watch("session.user", function() {
        if (!$scope.session.user.id) { return; }
        if ($scope.session.user.isEmployee) { return; }
        if ($routeParams.id) {
            $scope.dialog.showLoading();
            TaskService.get($routeParams.id).then(function(obj) {
                $scope.dialog.hideStandby();
                if (obj) {
                    $scope.task.showDetail = true;
                    $scope.task.data = [obj];
                    $scope.task.selected = obj;
                }
            });
        } else {
            refreshGrid();
        }
    });

    // 删除功能
    $scope.task.confirmDelete = function() {
        var selectedItem = $scope.task.selected;
        if (selectedItem === null) {
            $scope.dialog.alert({
                text: '请选择一条记录!'
            });
            return;
        }
        $scope.dialog.confirm({
            text: "请确认删除ID为:" + selectedItem.id + "的体检请求, 该操作无法恢复!",
            handler: function() {
                $scope.dialog.showStandby();
                TaskService.remove(selectedItem)
                .then(function() {
                    $scope.dialog.hideStandby();
                    $scope.task.selected = null;
                    $scope.message.success("删除记录成功!");
                    // 刷新
                    refreshGrid();
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("无法删除该请求,可能是您的权限不足,请联系管理员!");
                });
            }
        });
    };

    // 刷新功能
    $scope.task.refresh = refreshGrid;
}])
.config(['$routeProvider', function ($routeProvider) {
$routeProvider
.when('/examination', {
    template: examinationTemp,
    controller: 'ExaminationController'
})
.when('/examination/:id', {
    template: examinationTemp,
    controller: 'ExaminationController'
})
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