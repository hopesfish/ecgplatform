'use strict';
define(function(require, exports) {

var ruleListTemp = require("../templates/list.html");

angular.module('ecgRuleDirectives', [])
.controller('RuleController', ['$scope', '$timeout', '$location', 'RuleService', function ($scope, $timeout, $location, RuleService) {
    // 表格头
    $scope.subheader.title = "规则列表";

    // 命名空间
    $scope.rule = {};

}])
.controller('RuleListController', ['$scope', '$timeout', '$location', 'EnumService', 'RuleService', function ($scope, $timeout, $location, EnumService, RuleService) {

    if (!$scope.rule) {
        $scope.rule = {};
    }
    // 当前选择的item
    $scope.rule.selectedItem = null;

    // 规则数据
    $scope.rule.rules = null;
    $scope.rule.sysRules = null;
    $scope.rule.customRules = null;

    $scope.rule.getLabel = EnumService.getCodeLabel;

    // 初始化及刷新功能
    function filteredRules() {
        var sysRules = [], customRules = [], user = $scope.session.user,
            isAdmin = user.isAdmin(), isChief = user.isChief();
        $($scope.rule.rules).each(function(i, rule) {
            rule.editable = false;

            if (rule.employeeId === null) {
                sysRules.push(rule);
            } else {
                if (isAdmin || isChief) {
                    customRules.push(rule);
                } else if (rule.employeeId === user.id) {
                    customRules.push(rule);
                }
            }

            if (isAdmin || isChief) {
                rule.editable = true;
            } else if (rule.employeeId === user.id) {
                rule.editable = true;
            }
        });
        $scope.rule.sysRules = sysRules;
        $scope.rule.customRules = customRules;
    };

    function refreshGrid() {
        $scope.dialog.showLoading();
        RuleService.queryAllGroup().then(function(rules) {
            $scope.dialog.hideStandby();
            $scope.rule.rules = rules;
            filteredRules();
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("无法加载规则数据!");
        });
    };
    refreshGrid();

    $scope.rule.refresh = refreshGrid;

    // 编辑功能
    $scope.rule.showPage = function(item) {
        if (!item.editable) { return; }
        $location.path("rule/" + item.id);
    };

    // edit config
    $scope.rule.editReplyConfig = function(item) {
        $location.path("rule/" + item.id + "/config");
    };

    // 删除功能
    $scope.rule.confirmDelete = function() {
        var selectedReplyConfig = $scope.rule.selectedItem;
        if (selectedReplyConfig === null) {
            $scope.dialog.alert({
                text: '请选择一条记录!'
            });
            return;
        }
        $scope.dialog.confirm({
            text: "请确认删除[" + selectedReplyConfig.name + "]的规则, 该操作无法恢复!",
            handler: function() {
                $scope.dialog.showStandby();
                RuleService.remove(selectedReplyConfig.id)
                .then(function() {
                    $scope.dialog.hideStandby();
                    $scope.rule.selectedItem = null;
                    $scope.message.success("删除规则成功!");
                    // 刷新
                    refreshGrid();
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("无法删除该规则,可能是您的权限不足,请联系管理员!");
                });
            }
        });
    };

    // 设置上下限,主要是为了清除selectedItem
    $scope.rule.setRange = function(item, range, value) {
        item[range] = value;
        $timeout(function() {
            $scope.rule.selectedItem = null;
        }, 0);
    };
}])
.directive("ecgRuleList", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : ruleListTemp,
        controller : "RuleListController",
        link : function($scope, $element, $attrs) {
        }
    };
}])
.controller('RuleNewController', ['$scope', '$location', 'EnumService', 'RuleService', function ($scope, $location, EnumService, RuleService) {
    // 表格头
    $scope.subheader.title = "新增规则";

    // 命名空间
    $scope.rule = {};

    // codes
    $scope.rule.codes = EnumService.getCodesList();

    // 规则是否唯一
    $scope.rule.isUnique = true;

    // 当前选择的item
    $scope.rule.newobj = RuleService.getPlainObject();

    // session user
    var user = null;
    $scope.$watch("session.user" , function() {
        user = $scope.session.user;
        if (user.isAdmin && !user.isAdmin() && !user.isChief()) {
            $scope.rule.newobj.employeeId = user.id;
        }
    });

    $scope.$watch("rule.newobj.code" , function() {
        var code = $scope.rule.newobj.code, owner = '';
        if (!code || !user) { return; }
        if (user.isExpert()) { owner = user.name + '自定义的'; }
        $scope.rule.newobj.name = owner + EnumService.getCodeLabel(code) + '规则';

        // 检测唯一
        $scope.rule.checkUnique();
    });

    // 测试系统级别rule唯一性
    $scope.rule.checkUnique = function() {
        if (!$scope.rule.newobj.code) { return; }
        if (user.isAdmin() || user.isExpert() || user.isChief()) {
            RuleService.queryAll({
                code: $scope.rule.newobj.code,
                usage: 'group', 
                employeeId: user.isExpert() ? user.id : null
            }).then(function(rules) {
                var  owner = '';
                if (user.isExpert()) { owner = user.name + '自定义的'; }
                if (rules.length > 0) { 
                    $scope.rule.isUnique = false;
                    $scope.message.warn(owner + EnumService.getCodeLabel($scope.rule.newobj.code) + "规则已存在!");
                } else {
                    $scope.rule.isUnique = true;
                }
            }, function() {
                $scope.rule.isUnique = true;
                $scope.message.warn("查询系统规则是否唯一时出错!");
            });
        }
    };

    // 创建函数
    $scope.rule.create = function() {
        if (parseFloat($scope.rule.newobj.min) > parseFloat($scope.rule.newobj.max)) {
            $scope.message.warn("区间范围异常,无法保存。");
            return;
        }

        $scope.dialog.showStandby();
        RuleService.create($scope.rule.newobj)
        .then(function(result) {
            $scope.dialog.hideStandby();
            if (result) {
                $scope.message.success("新增规则成功!");
            } else {
                $scope.message.error("新增规则失败!");
            }
            $location.path("/rule");
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("服务器异常,新增失败!");
        });
    };

}])
.controller('RuleEditController', ['$scope', '$routeParams', 'RuleService', function ($scope, $routeParams, RuleService) {
    // 表格头
    $scope.subheader.title = "编辑规则";

    // 命名空间
    $scope.rule = {};

    // 当前编辑的item
    $scope.rule.updateobj = null;

    // 初始化界面,并获得最新version
    function refresh() {
        $scope.dialog.showLoading();
        RuleService.get($routeParams.id).then(function(rule) {
            $scope.dialog.hideStandby();
            $scope.rule.updateobj = rule;
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("加载规则数据失败.");
        });
    };
    refresh();

    // 创建函数
    $scope.rule.update = function() {
        $scope.dialog.showStandby();
        RuleService.update($scope.rule.updateobj)
        .then(function(result) {
            $scope.dialog.hideStandby();
            if (result) {
                $scope.message.success("编辑规则成功!");
                refresh();
            } else {
                $scope.message.error("编辑规则失败!");
            }
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("服务器异常,编辑规则失败!");
        });
    };

}]);


});