'use strict';
define(function(require, exports) {

var chiefEditTemp = require("../templates/chief/edit.html");

angular.module('ecgChief', [])
.controller('ChiefController', ['$scope', '$filter', '$timeout', '$location', 'EnumService', 'ChiefService', function ($scope, $filter, $timeout, $location, EnumService, ChiefService) {
    // 表格头
    $scope.subheader.title = "健康中心管理主任";

    // 命名空间
    $scope.chief = {};

    // 表格展示
    $scope.chief.data = null;
    $scope.chief.filteredData = null;
    // 刷新功能
    function refreshGrid() {
        $scope.dialog.showLoading();
        ChiefService.queryAll().then(function(chiefs) {
            $scope.dialog.hideStandby();
            $scope.chief.data = chiefs;
            $scope.chief.filteredData = $scope.chief.data;
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("无法加载专家数据!");
        });
    }
    refreshGrid();

    // 显示label
    $scope.chief.getGenderLabel = function(chief) {
        return EnumService.getGenderLabel(chief.gender);
    };
    $scope.chief.getDismissedLabel = function(chief) {
        return EnumService.getDismissedLabel(chief.dismissed);
    };

    // 当前选中数据
    $scope.chief.selectedItem = null;

    // 删除功能
    $scope.chief.confirmDelete = function() {
        var selectedItem = $scope.chief.selectedItem;
        if (selectedItem === null) {
            $scope.dialog.alert({
                text: '请选择一条记录!'
            });
            return;
        }
        $scope.dialog.confirm({
            text: "请确认删除主任:" + selectedItem.name + ", 该操作无法恢复!",
            handler: function() {
                $scope.dialog.showStandby();
                ChiefService.remove(selectedItem.id)
                .then(function() {
                    $scope.dialog.hideStandby();
                    $scope.chief.selectedItem = null;
                    $scope.message.success("删除主任成功!");
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
    $scope.chief.queryChanged = function(query) {
        return $scope.chief.filteredData = $filter("filter")($scope.chief.data, query);
    };

    // 编辑功能
    $scope.chief.showPage = function(chief) {
        $location.path("chief/" + chief.id);
    };

}])
.controller('ChiefNewController', ['$scope', '$timeout', '$location', 'EnumService', 'ProfileService', 'ChiefService',
    function ($scope, $timeout, $location, EnumService, ProfileService, ChiefService) {
    $scope.subheader.title = "新增主任";

    $scope.chief = {};
    $scope.chief.newobj = ChiefService.getPlainObject();
    $scope.chief.genders = EnumService.getGenders();
    $scope.chief.dismissedStates = EnumService.getDismissedStates();

    $('#chief-birthday').datetimepicker({
        format: "yyyy-MM-dd",
        language: "zh-CN",
        pickTime: false
    });

    $scope.chief.showDatePicker = function() {
        $('#chief-birthday').datetimepicker('show');
    };

    $scope.chief.isUnique = true;
    $scope.chief.checkUnique = function() {
        if (!$scope.chief.newobj.username) { return; }
        ProfileService.get($scope.chief.newobj.username).then(function(user) {
            if (user) { 
                $scope.chief.isUnique = false;
                $scope.message.warn("登录名为" + $scope.chief.newobj.username + "的员工已存在!");
            } else {
                $scope.chief.isUnique = true;
            }
        }, function() {
            $scope.chief.isUnique = true;
            $scope.message.warn("查询登录名是否唯一时出错!");
        });
    };

    $scope.chief.create = function() {
        $scope.dialog.showStandby();
        $scope.chief.newobj.birthday = $('#chief-birthday input').val();
        $scope.chief.newobj.password = $scope.chief.newobj.username;
        ChiefService.create($scope.chief.newobj)
        .then(function(result) {
            $scope.dialog.hideStandby();
            if (result) {
                $scope.message.success("新增主任成功!");
                $location.path("/chief");
            } else {
                $scope.message.error("新增主任失败!");
            }
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("服务器异常,新增主任失败!");
        });;
    };
}])
.controller('ChiefViewController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'ChiefService',
    function ($scope, $routeParams, $timeout, $location, EnumService, ChiefService) {
    $scope.subheader.title = "编辑主任";

    $scope.chief = {};
    $scope.chief.tab = 1; // 默认为基本页面

}])
// 基本信息
.controller('ChiefEditController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'ProfileService', 'ChiefService',
    function ($scope, $routeParams, $timeout, $location, EnumService, ProfileService, ChiefService) {
    $scope.chief.updateobj = null; //ChiefService.get($routeParams.id);

    // 初始化界面,并获得最新version
    function refresh() {
        $scope.dialog.showLoading();
        ChiefService.get($routeParams.id).then(function(chief) {
            $scope.dialog.hideStandby();
            $scope.chief.updateobj = chief;
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("加载主任数据失败.");
        });
    };
    refresh();

    $scope.chief.genders = EnumService.getGenders();
    $scope.chief.dismissedStates = EnumService.getDismissedStates();

    $('#chief-birthday').datetimepicker({
        format: "yyyy-MM-dd",
        language: "zh-CN",
        pickTime: false,
    }).on('changeDate', function(e) {
        $scope.chief.updateobj.birthday = $('#chief-birthday input').val();
    });

    $scope.chief.showDatePicker = function() {
        $('#chief-birthday').datetimepicker('show');
    };

    $scope.chief.update = function() {
        $scope.dialog.showStandby();
        $scope.chief.updateobj.birthday = $('#chief-birthday input').val();
        ChiefService.update($scope.chief.updateobj)
        .then(function(result) {
            $scope.dialog.hideStandby();
            $scope.message.success("编辑主任成功!");
            refresh();
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("编辑主任失败!");
        });;
    };

    $scope.chief.resetPassword = function() {
        $scope.dialog.confirm({
            text: "重置后登录密码将于登录名一致，确定继续?",
            handler: function() {
                $scope.dialog.showStandby();
                ProfileService.resetPassword($scope.chief.updateobj.id)
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
.directive("ecgChiefEdit", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : chiefEditTemp,
        controller : "ChiefEditController",
        link : function($scope, $element, $attrs) {
        }
    };
}]);


});