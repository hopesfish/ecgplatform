'use strict';
define(function(require, exports) {

angular.module('ecgSysConfig', [])
.controller('SysConfigController', 
    ['$scope', '$routeParams', 'EnumService', 'SysConfigService', 
    function ($scope, $routeParams, EnumService, SysConfigService) {
    // 表格头
    $scope.subheader.title = "系统参数";

    // 命名空间
    $scope.sysconfig = {};

    // 参数对象
    $scope.sysconfig.config = null;

    function loadSysConfig() {
        SysConfigService.queryAll().then(function(configs) {
            if (configs.length == 1) {
                $scope.sysconfig.config = configs[0];
            } else {
                $scope.message.error("加载系统参数失败!");
            }
        }, function() {

        });
    };
    loadSysConfig();

    // 删除区间
    $scope.sysconfig.save = function() {
        $scope.dialog.confirm({
            text: "您确定更改系统参数？",
            handler: function() {
                $scope.dialog.showStandby();
                SysConfigService.update($scope.sysconfig.config).then(function() {
                    $scope.dialog.hideStandby();
                    $scope.message.success("更改系统参数成功!");
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("更改系统参数失败!");
                });
                
            }
        });
    };

    /**
     * 重置
     */
    $scope.sysconfig.reset = function() {
        loadSysConfig();
    };

}]);


});