'use strict';
define(function(require, exports) {

var dialogTemp = require("../templates/dialog.html");

angular.module('ecgAPK', [])
.controller('APKController', ['$scope', '$filter', '$timeout', '$location', 'EnumService', 'APKService', function ($scope, $filter, $timeout, $location, EnumService, APKService) {
    // 表格头
    $scope.subheader.title = "APK管理";

    // 命名空间
    $scope.apk = {};

    // 表格展示
    $scope.apk.data = null;
    $scope.apk.filteredData = null;
 
    // 刷新功能
    function refreshGrid() {
        $scope.dialog.showLoading();
        APKService.queryAll().then(function(APKs) {
            $scope.dialog.hideStandby();
            $scope.apk.data = APKs;
            $scope.apk.filteredData = $scope.apk.data;
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("无法加载专家数据!");
        });
    }
    refreshGrid();

    // 当前选中数据
    $scope.apk.selectedItem = null;

    // 删除功能
    $scope.apk.confirmDelete = function() {
        var selectedItem = $scope.apk.selectedItem;
        if (selectedItem === null) {
            $scope.dialog.alert({
                text: '请选择APK!'
            });
            return;
        }
        $scope.dialog.confirm({
            text: "请确认删除标识为:" + selectedItem.id + "的APK, 该操作无法恢复!",
            handler: function() {
                $scope.dialog.showStandby();
                APKService.remove(selectedItem.id)
                .then(function() {
                    $scope.dialog.hideStandby();
                    $scope.apk.selectedItem = null;
                    $scope.message.success("删除APK成功!");
                    // 刷新
                    refreshGrid();
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("无法删除该数据,可能是您的权限不足,请联系管理员!");
                });
            }
        });
    };

    $scope.apk.refresh = refreshGrid;

}])
.controller('APKUploadDialogController', 
    ['$scope', '$filter', '$timeout', '$location', 'EnumService', 'CardService', 
    function ($scope, $filter, $timeout, $location, EnumService, CardService) {

    // 命名空间
    $scope.uploaddialog = {};

    $scope.uploaddialog.execute = function() {
        var iframeWin = document.getElementById("uploadAPKFrame").contentWindow;
        if (!iframeWin.upload) { return; }
        iframeWin.upload();
    };

    $scope.uploaddialog.hide = function(opts) {
      $('#ecgAPKUploadDialog').modal('hide');
      if ($scope.apk && $scope.apk.refresh) {
        $scope.apk.refresh();
      }
    };
    window.closeUploadAPKDialog = $scope.uploaddialog.hide;

    $scope.uploaddialog.show = function(opts) {
      var opts = opts || {};
      $scope.uploaddialog.handler = opts.handler;
      document.getElementById("uploadAPKFrame").src = PATH + '/views/apk/upload.jsp?token=' +  $.cookie("AiniaOpAuthToken");
      $('#ecgAPKUploadDialog').modal('show');
    };

}])
.directive("ecgApkUploadDialog", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : dialogTemp,
        controller : "APKUploadDialogController",
        link : function($scope, $element, $attrs) {
        }
    };
}]);


});