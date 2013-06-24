'use strict';
define(function(require, exports) {

require("./services/EmployeeService");
require("./services/ChiefService");
require("./directives/Chief");
require("./directives/Expert");
require("./directives/Operator");

var chiefTemp = require("./templates/chief.html");
var chiefNewTemp = require("./templates/chief/new.html");
var chiefViewTemp = require("./templates/chief/view.html");

var expertTemp = require("./templates/expert.html");
var operatorTemp = require("./templates/operator.html");

angular.module('ecgEmployee', ['ecgChiefService', 'ecgChief', 'ecgExpert', 'ecgOperator'])
.controller('ChiefController', ['$scope', '$filter', '$timeout', '$location', 'EnumService', 'ChiefService', function ($scope, $filter, $timeout, $location, EnumService, ChiefService) {
    // 表格头
    $scope.subheader.title = "健康中心管理主任";

    // 命名空间
    $scope.chief = {};

    // 表格展示
    $scope.chief.data = ChiefService.queryAll();
    $scope.chief.filteredData = $scope.chief.data;

    // 显示label
    $scope.chief.getGenderLabel = function(chief) {
        return EnumService.getGenderLabel(chief.gender);
    };
    $scope.chief.getDismissedLabel = function(chief) {
        return EnumService.getDismissedLabel(chief.dismissed);
    };

    // 当前选中数据
    $scope.chief.selectedItem = null;

    // 刷新功能
    function refreshGrid() {
        $scope.chief.data = ChiefService.queryAll();
        $scope.chief.filteredData = $scope.chief.data;
    }

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
                    $scope.popup.success("删除成功!");
                    // 刷新
                    refreshGrid();
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.popup.error("无法删除该数据,可能是您的权限不足,请联系管理员!");
                });
            }
        });
    };

    // 过滤功能
    $scope.chief.queryChanged = function(query) {
        // TODO:
        //return $scope.chief.filteredData = $filter("filter")($scope.chief.data, query);
    };

    // 编辑功能
    $scope.chief.showPage = function(chief) {
        $location.path("chief/" + chief.id);
    };

}])
.controller('ChiefNewController', ['$scope', '$timeout', '$location', 'EnumService', 'ChiefService',
    function ($scope, $timeout, $location, EnumService, ChiefService) {
    $scope.subheader.title = "新增主任";

    $scope.chief = {};
    $scope.chief.newobj = ChiefService.getPlainObject();
    $scope.chief.genders = EnumService.getGenders();
    $scope.chief.dismissedStates = EnumService.getDismissedStates();

    $('#chief-birthday').datetimepicker({
        format: "yyyy-MM-dd",
        language: "zh-CN",
        pickTime: false
    }).on('changeDate', function(e) {
        $scope.chief.newobj.birthday = $('#chief-birthday input').val()
    });

    $scope.chief.showDatePicker = function() {
        $('#chief-birthday').datetimepicker('show');
    };

    $scope.chief.create = function() {
        $scope.dialog.showStandby();
        $scope.chief.newobj.birthday = $('#chief-birthday input').val();
        $scope.chief.newobj.pasasword = $scope.chief.newobj.username;
        ChiefService.create($scope.chief.newobj)
        .then(function(result) {
            $scope.dialog.hideStandby();
            if (result) {
                $scope.popup.success("新增成功!");
                $location.path("/chief");
            } else {
                $scope.popup.error("新增失败!");
            }
        }, function() {
            $scope.dialog.hideStandby();
            $scope.popup.error("服务器异常,新增失败!");
        });;
    };
}])
.controller('ChiefViewController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'ChiefService',
    function ($scope, $routeParams, $timeout, $location, EnumService, ChiefService) {
    $scope.subheader.title = "编辑主任";

    $scope.chief = {};
    $scope.chief.tab = 1; // 默认为基本页面

}])
.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
    .when('/chief', {
        template: chiefTemp,
        controller: 'ChiefController'
    })
    .when('/chief/new', {
        template: chiefNewTemp,
        controller: 'ChiefNewController'
    })
    .when('/chief/:id', {
        template: chiefViewTemp,
        controller: 'ChiefViewController'
    })
    .when('/expert', {
        template: expertTemp,
        controller: 'ExpertController'
    })
    .when('/operator', {
        template: operatorTemp,
        controller: 'OperatorController'
    });
}]);

});// end of define