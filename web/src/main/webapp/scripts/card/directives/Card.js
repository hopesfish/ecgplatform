'use strict';
define(function(require, exports) {

var dialogTemp = require("../templates/dialog.html");

angular.module('ecgCardDirectives', [])
.controller('CardUploadController', ['$scope', '$filter', '$location', 'EnumService', 'CardService', function ($scope, $filter, $location, EnumService, CardService) {
    // 表格头
    $scope.subheader.title = "上传卡号";

    // 命名空间
    $scope.card = {};

}])
.controller('UploadDialogController', 
    ['$scope', '$filter', '$timeout', '$location', 'EnumService', 'CardService', 
    function ($scope, $filter, $timeout, $location, EnumService, CardService) {

    // 命名空间
    $scope.uploaddialog = {};

    $scope.uploaddialog.execute = function() {
        var iframeWin = document.getElementById("uploadCardFrame").contentWindow;
        if (!iframeWin.upload) { return; }
        iframeWin.upload();
    };

    $scope.uploaddialog.hide = function(opts) {
      $('#ecgCardUploadDialog').modal('hide');
    };
    window.closeUploadCardDialog = $scope.uploaddialog.hide;

    $scope.uploaddialog.show = function(opts) {
      var opts = opts || {};
      $scope.uploaddialog.handler = opts.handler;
      document.getElementById("uploadCardFrame").src = PATH + '/views/card/upload.jsp?token=' +  $.cookie("AiniaOpAuthToken");
      $('#ecgCardUploadDialog').modal('show');
    };

}])
.directive("ecgCardUploadDialog", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : dialogTemp,
        controller : "UploadDialogController",
        link : function($scope, $element, $attrs) {
        }
    };
}])
.controller('CardQueryController', ['$scope', '$timeout', '$location', 'EnumService', 'CardService',
    function ($scope, $timeout, $location, EnumService, CardService) {
    $scope.subheader.title = "查询卡号";

    $scope.card = {};

    $scope.card.viewobj = {};

    $scope.card.query = function() {
        $scope.dialog.showLoading();
        CardService.get($scope.card.viewobj)
        .then(function(card) {
            $scope.dialog.hideStandby();
            if (card) {
                $scope.card.viewobj = card;
            } else {
                $scope.card.viewobj = {};
                $scope.message.error("没有该卡号，如需帮助，请联系管理员!");
            }
        }, function() {
            $scope.dialog.hideStandby();
            $scope.card.viewobj = {};
            $scope.message.error("没有该卡号，如需帮助，请联系管理员!");
        });
    };

    $scope.card.reset = function() {
        $scope.card.viewobj = {};
    };
}])
.controller('CardHistoryController', ['$scope', '$filter', '$routeParams', '$timeout', '$location', 'EnumService', 'CardService',
    function ($scope, $filter, $routeParams, $timeout, $location, EnumService, CardService) {
    $scope.subheader.title = "充值历史";

    // 命名空间
    $scope.card = {};

    // 表格展示
    $scope.card.data = null;
    $scope.card.paging = {
        total: 0,
        curPage: 1,
        goToPage: function(page, max) {
            refreshGrid({'page.curPage': page, 'page.max': max});
        }
    };

    // 刷新功能
    var globalParams = {};
    function refreshGrid(params) {
        globalParams = $.extend(globalParams, params);
        $scope.dialog.showLoading();
        CardService.queryAll(globalParams).then(function(paging) {
            $scope.dialog.hideStandby();
            if (paging) {
                $scope.card.paging.total = paging.total;
                $scope.card.paging.curPage = paging.curPage;
                $scope.card.data = paging.datas;
            } else {
                $scope.message.error("无法加载充值历史!");
            }
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("无法加载充值历史!");
        });
    }
    refreshGrid();

    // 过滤功能
    $scope.card.query = {};

    $scope.card.reset = function() {
        $scope.card.query = {};
        $('#charged-month input').val('')
    };
    
    $scope.card.queryChanged = function(query) {
        // 卡号
        if (query.serial) {
            globalParams['serial:like'] = query.serial;
        } else {
            delete globalParams['serial:like'];
        }
        
        // 充值月份
        var chargedMonth = $('#charged-month input').val();
        if (chargedMonth) {
            globalParams['chargedDate:gth'] = chargedMonth + '-01';
            globalParams['chargedDate:lth'] = chargedMonth + '-31 23:59:59';
        } else {
            delete globalParams['chargedDate:gth'];
            delete globalParams['chargedDate:lth'];
        }
        globalParams['page.curPage'] = 1;
        refreshGrid(globalParams);
    };

    $scope.card.refresh = refreshGrid;

    // 日期功能
    $('#charged-month').datetimepicker({
        format: "yyyy-MM",
        language: "zh-CN",
        viewMode: 1,
        minViewMode: 1,
        pickTime: false
    }).on('changeDate', function(ev){
        // an bug
        //$scope.card.query.chargedMonth = $('#charged-month input').val();
        $scope.$apply();
    });
}])
.controller('CardChargeController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'CardService',
    function ($scope, $routeParams, $timeout, $location, EnumService, CardService) {
    $scope.subheader.title = "在线充值";

    // 命名空间
    $scope.card = {};

    $scope.card.chargeinfo = {};

    $('#card-startdate').datetimepicker({
        format: "yyyy-MM-dd",
        language: "zh-CN",
        pickTime: false,
    }).on('changeDate', function(e) {
        $scope.card.chargeinfo.activedDate = $('#card-startdate input').val();
        $scope.$apply();
    });

    /*
    $('#card-enddate').datetimepicker({
        format: "yyyy-MM-dd",
        language: "zh-CN",
        pickTime: false,
    }).on('changeDate', function(e) {
        $scope.card.chargeinfo.enddate = $('#card-enddate input').val();
        $scope.$apply();
    });*/

    $scope.card.charge = function() {
        $scope.dialog.confirm({
            text: "您将向手机号为 " + $scope.card.chargeinfo.mobile + " 的用户充值，确定继续?",
            handler: function() {
                $scope.dialog.showStandby();
                CardService.charge($scope.session.user, {mobile: $scope.card.chargeinfo.mobile}, $scope.card.chargeinfo)
                .then(function() {
                    $scope.dialog.hideStandby();
                    $scope.message.success("充值成功!");
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("充值失败，请确认卡是否有效性或者和该用户的现有服务时间段冲突!");
                });
            }
        });
        
    };

}]);


});