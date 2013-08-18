'use strict';
define(function(require, exports) {
  var dialogTempalte = require("./templates/dialog.html");

  angular.module('ecgDialog', [])
  .controller('DialogController', ['$scope', '$timeout', function ($scope, $timeout) {
  	$scope.dialog = {};
  	$scope.dialog.OKLabel = '确定';
    $scope.dialog.shown = false;
    $scope.dialog.loading = "数据加载中，请耐心等待......";
    $scope.dialog.isLoading = false;

	  $scope.dialog.showHeader = function() {
  		return true;
  	};
    $scope.dialog.showClose = function() {
      return !$scope.dialog.isLoading;
    };
  	$scope.dialog.showFooter = function() {
  		if ($scope.dialog.state === 'standby') {
  			return false;
  		}
  		return true;
  	};
    $scope.dialog.showCancelButton = function() {
      if ($scope.dialog.state === 'alert') {
        return false;
      }
      return true;
    };
	  $scope.dialog.showOKButton = function() {
  		if ($scope.dialog.state === 'alert') {
  			return false;
  		}
  		return true;
  	};

  	$scope.dialog.execute = function() {
  		if ($scope.dialog.handler instanceof Function) {
  			$scope.dialog.handler();
  		}
  	};

    function show() {
      $('#ecgDialog').modal('show');
    }
    $('#ecgDialog').on('hidden', function () {
      $scope.dialog.shown = false;
    });
    $('#ecgDialog').on('shown', function () {
      $scope.dialog.shown = true;
    });

  	$scope.dialog.alert = function(opts) {
  		var opts = opts || {};
  		$scope.dialog.state = 'alert';
  		$scope.dialog.dialogTitle = opts.title || '提示';
  		$scope.dialog.dialogText = opts.text || '提示';
  		$scope.dialog.OKLabel = opts.OKLabel || '确定';
      $scope.dialog.handler = function() { $('#ecgDialog').modal('hide'); };
  		show();
  	};
  	$scope.dialog.confirm = function(opts) {
  		var opts = opts || {};
  		$scope.dialog.state = 'confirm';
  		$scope.dialog.dialogTitle = opts.title || '请确认';
  		$scope.dialog.dialogText = opts.text || '请确认该操作';
  		$scope.dialog.OKLabel = opts.OKLabel || '确定';
  		$scope.dialog.handler = opts.handler || function() {};
      show();
  	};
    var showtime = 0;
  	$scope.dialog.showStandby = function(opts) {
  		var opts = opts || {};
  		$scope.dialog.state = 'standby';
  		$scope.dialog.dialogTitle = opts.title || '提示';
  		$scope.dialog.dialogText = opts.text || '操作进行中,请耐心等待.';
      showtime = (new Date()).getTime();
      $scope.dialog.isLoading = true;
      show();
  	};
    $scope.dialog.showLoading = function() {
      $scope.dialog.isLoading = true;
      $scope.dialog.showStandby({text: $scope.dialog.loading});
    };
  	$scope.dialog.hideStandby = function() {
      function hide() {
    		$scope.dialog.state = 'standby';
    		$scope.dialog.state = '';
    		$scope.dialog.dialogText = '';
        $scope.dialog.isLoading = false;
        $('#ecgDialog').modal('hide');
      }
      var current = (new Date()).getTime();
      if (current - showtime > 700) {
        hide();
      } else {
        $timeout(hide, 700);
      }
  	};
  }])
  .directive("ecgDialog", ['$location', function ($location) {
    return {
      restrict: 'A',
      replace: true,
      template: dialogTempalte,
      controller: "DialogController",
      link: function ($scope, $element, $attrs) {
      }
    };
  }]);

});