define(function(require, exports) {
    
'use strict';

var userEditTemp = require("../templates/user/edit.html");
var usersDialogTemp = require("../templates/user/usersdialog.html");

angular.module('ecgUserModules', [])
.controller('UserController', ['$scope', '$filter', '$timeout', '$location', 'EnumService', 'UserService', function ($scope, $filter, $timeout, $location, EnumService, UserService) {
    // 表格头
    $scope.subheader.title = "用户管理";

    // 命名空间
    $scope.user = {};

    // 表格展示
    $scope.user.data = null;
    $scope.user.filteredData = null;
    // 刷新功能
    function refreshGrid() {
        $scope.dialog.showLoading();
        UserService.queryAll().then(function(users) {
            $scope.dialog.hideStandby();
            $scope.user.data = users;
            $scope.user.filteredData = $scope.user.data;
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("无法加载用户数据!");
        });
    }
    refreshGrid();

    // 显示label
    $scope.user.getGenderLabel = function(user) {
        return EnumService.getGenderLabel(user.gender);
    };
    $scope.user.getDismissedLabel = function(user) {
        return EnumService.getDismissedLabel(user.dismissed);
    };

    // 当前选中数据
    $scope.user.selectedItem = null;

    // 禁用功能
    $scope.user.confirmDisable = function() {
        var selectedItem = $scope.user.selectedItem;
        if (selectedItem === null) {
            $scope.dialog.alert({
                text: '请选择一条记录!'
            });
            return;
        }
        $scope.dialog.confirm({
            text: "禁用用户:" + selectedItem.name + ", 是否继续!",
            handler: function() {
                $scope.dialog.showStandby();
                UserService.disable(selectedItem.id)
                .then(function() {
                    $scope.dialog.hideStandby();
                    $scope.user.selectedItem = null;
                    $scope.message.success("删除成功!");
                    // 刷新
                    refreshGrid();
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("无法删除该数据,可能是您的权限不足,请联系管理员!");
                });
            }
        });
    };

    // 删除功能
    $scope.user.confirmDelete = function() {
        var selectedItem = $scope.user.selectedItem;
        if (selectedItem === null) {
            $scope.dialog.alert({
                text: '请选择一条记录!'
            });
            return;
        }
        $scope.dialog.confirm({
            text: "请确认删除用户:" + selectedItem.name + ", 该操作无法恢复!",
            handler: function() {
                $scope.dialog.showStandby();
                UserService.remove(selectedItem.id)
                .then(function() {
                    $scope.dialog.hideStandby();
                    $scope.user.selectedItem = null;
                    $scope.message.success("删除成功!");
                    // 刷新
                    refreshGrid();
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("无法删除该数据,可能是您的权限不足,请联系管理员!");
                });
            }
        });
    };

    // 过滤功能
    $scope.user.queryChanged = function(query) {
        return $scope.user.filteredData = $filter("filter")($scope.user.data, query);
    };

    // 编辑功能
    $scope.user.showPage = function(user) {
        $location.path("user/" + user.id);
    };

}])
.controller('UserNewController', ['$scope', '$timeout', '$location', 'EnumService', 'ProfileService', 'UserService',
    function ($scope, $timeout, $location, EnumService, ProfileService, UserService) {
    $scope.subheader.title = "新增用户";

    $scope.user = {};
    $scope.user.newobj = UserService.getPlainObject();
    $scope.user.genders = EnumService.getGenders();

    $('#user-birthday').datetimepicker({
        format: "yyyy-MM-dd",
        language: "zh-CN",
        pickTime: false
    });

    $scope.user.showDatePicker = function() {
        $('#user-birthday').datetimepicker('show');
    };

    $scope.user.isUnique = true;
    $scope.user.checkUnique = function() {
        UserService.findAllByMobile($scope.user.newobj.mobile).then(function(users) {
            if (users.length > 0) { 
                $scope.user.isUnique = false;
                $scope.message.warn("手机号码" + $scope.user.newobj.mobile + "已存在!");
            } else {
                $scope.user.isUnique = true;
            }
        }, function() {
            $scope.user.isUnique = true;
            $scope.message.warn("查询用户是否唯一时出错!");
        });
    };

    $scope.user.create = function() {
        $scope.dialog.showStandby();
        $scope.user.newobj.birthday = $('#user-birthday input').val();
        $scope.user.newobj.password = $scope.user.newobj.mobile;
        UserService.create($scope.user.newobj)
        .then(function(result) {
            $scope.dialog.hideStandby();
            if (result) {
                $scope.message.success("新增用户成功!");
                $location.path("/user");
            } else {
                $scope.message.error("新增用户失败!");
            }
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("服务器异常,新增失败!");
        });;
    };
}])
.controller('UserViewController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'UserService',
    function ($scope, $routeParams, $timeout, $location, EnumService, UserService) {
    $scope.subheader.title = "编辑用户";

    $scope.user = {};
    $scope.user.tab = 1; // 默认为基本页面

}])
// 基本信息
.controller('UserEditController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'UserService',
    function ($scope, $routeParams, $timeout, $location, EnumService, UserService) {
    $scope.user.updateobj = null; //UserService.get($routeParams.id);

    // 初始化界面,并获得最新version
    function refresh() {
        $scope.dialog.showLoading();
        UserService.get($routeParams.id).then(function(user) {
            $scope.dialog.hideStandby();
            $scope.user.updateobj = user;
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("加载用户数据失败!");
        });
    };
    refresh();

    $scope.user.genders = EnumService.getGenders();

    $('#user-birthday').datetimepicker({
        format: "yyyy-MM-dd",
        language: "zh-CN",
        pickTime: false,
    }).on('changeDate', function(e) {
        $scope.user.updateobj.birthday = $('#user-birthday input').val();
    });

    $scope.user.showDatePicker = function() {
        $('#user-birthday').datetimepicker('show');
    };

    $scope.user.update = function() {
        $scope.dialog.showStandby();
        $scope.user.updateobj.birthday = $('#user-birthday input').val();
        UserService.update($scope.user.updateobj)
        .then(function(result) {
            $scope.dialog.hideStandby();
            $scope.message.success("编辑用户成功!");
            refresh();
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("编辑用户失败!");
        });;
    };

    $scope.user.resetPassword = function() {
        $scope.dialog.confirm({
            text: "重置后登录密码将与手机号码一致，确定继续?",
            handler: function() {
                $scope.dialog.showStandby();
                $scope.user.updateobj.password = $scope.user.updateobj.mobile;
                UserService.update($scope.user.updateobj)
                .then(function(result) {
                    $scope.dialog.hideStandby();
                    $scope.message.success("重置密码成功!");
                    refresh();
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("重置密码失败!");
                });
            }
        });
    };
}])
.directive("ecgUserEdit", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : userEditTemp,
        controller : "UserEditController",
        link : function($scope, $element, $attrs) {
        }
    };
}])
.controller('UserDialogController', 
    ['$scope', '$filter', '$timeout', '$location', 'EnumService', 'UserService', 
    function ($scope, $filter, $timeout, $location, EnumService, UserService) {

    // 命名空间
    $scope.userdialog = {};

    // 表格展示
    $scope.userdialog.data = null;
    function refreshGrid() {
        UserService.queryAll().then(function(users) {
            $scope.userdialog.data = users;
        });
    };
    refreshGrid();

    $scope.userdialog.execute = function() {
        var selecteds = [];
        $($scope.userdialog.data).each(function(i, expert) {
            if (expert.selected) {
                selecteds.push(expert);
            }
        });
        $scope.userdialog.hide();
        if ($scope.userdialog.handler instanceof Function) {
            $scope.userdialog.handler(selecteds);
        }
    };

    $scope.userdialog.hide = function(opts) {
      $('#ecgUsersDialog').modal('hide');
    };

    $scope.userdialog.show = function(opts) {
      var opts = opts || {};
      $scope.userdialog.handler = opts.handler;
      $('#ecgUsersDialog').modal('show');
      refreshGrid();
    };

}])
.directive("ecgUserDialog", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : usersDialogTemp,
        controller : "UserDialogController",
        link : function($scope, $element, $attrs) {
        }
    };
}]);


});