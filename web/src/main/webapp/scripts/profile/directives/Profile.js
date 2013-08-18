'use strict';
define(function(require, exports) {

var profileEditTemp = require("../templates/profile/edit.html");
var profilePasswordTemp = require("../templates/profile/password.html");

var httpProvider;
angular.module('ecgProfileDirectives', [])
.config(['$httpProvider', function ($httpProvider) {
    httpProvider = $httpProvider;
}])
.controller('ProfileController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'ProfileService',
    function ($scope, $routeParams, $timeout, $location, EnumService, ProfileService) {
    $scope.subheader.title = "个人设置";

    $scope.profile = {};
    $scope.profile.tab = 1; // 默认为基本页面

}])
// 基本信息
.controller('ProfileEditController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'ProfileService',
    function ($scope, $routeParams, $timeout, $location, EnumService, ProfileService) {

   	$scope.profile.user = null;
    $scope.profile.showMobile = false;

    function refresh() {
        var isEmployee = $scope.session.user.isEmployee;
        if (isEmployee && isEmployee()) {
            $scope.profile.showMobile = true;
            ProfileService.get($scope.session.user.username).then(function(user) {
                $scope.profile.user = user;
            });
        } else {
            $scope.profile.user = $scope.session.user;
        }
    }

    $scope.$watch("session.user", function() {
        if (!$scope.session.user.id) { return; }
        refresh();
    });

    $scope.profile.genders = EnumService.getGenders();
    $scope.profile.dismissedStates = EnumService.getDismissedStates();

    $('#profile-birthday').datetimepicker({
        format: "yyyy-MM-dd",
        language: "zh-CN",
        pickTime: false,
    }).on('changeDate', function(e) {
        $scope.profile.user.birthday = $('#profile-birthday input').val();
    });

    $scope.profile.showDatePicker = function() {
        $('#profile-birthday').datetimepicker('show');
    };

    $scope.profile.update = function() {
        var isEmployee = $scope.session.user.isEmployee, promise;
        
        $scope.profile.user.birthday = $('#profile-birthday input').val();
        
        if (isEmployee && isEmployee()) {
            promise = ProfileService.update($scope.profile.user);
        } else {
            promise = ProfileService.updateUser($scope.profile.user);
        }
        
        $scope.dialog.showStandby();
        promise.then(function(result) {
            $scope.dialog.hideStandby();
            $scope.message.success("编辑成功!");
            refresh();
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("编辑失败!");
        });
    };
}])
.directive("ecgProfileEdit", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : profileEditTemp,
        controller : "ProfileEditController",
        link : function($scope, $element, $attrs) {
        }
    };
}])
// 修改密码
.controller('ProfilPasswordController', ['$scope', '$http', '$routeParams', '$timeout', '$location', 'EnumService', 'ProfileService',
    function ($scope, $http, $routeParams, $timeout, $location, EnumService, ProfileService) {

   	$scope.profile.user = null;

    function refresh() {
        var isEmployee = $scope.session.user.isEmployee;
        if (isEmployee && isEmployee()) {
            ProfileService.get($scope.session.user.username).then(function(user) {
                $scope.profile.user = user;
            });
        } else {
            $scope.profile.user = $scope.session.user;
        }
    }

    $scope.$watch("session.user", function() {
        if (!$scope.session.user.id) { return; }
        refresh();
    });

    $scope.profile.passwordIsEuqal = false;
    $scope.profile.compare = function() {
        $scope.profile.passwordIsEuqal = $scope.profile.user.newPassword === $scope.profile.user.confirmPassword;
    };

    $scope.profile.updatePassword = function() {
        var isEmployee = $scope.session.user.isEmployee, promise, updateCookie;
        
        $scope.profile.user.birthday = $('#profile-birthday input').val();
        
        if (isEmployee && isEmployee()) {
            promise = ProfileService.updatePassword($scope.profile.user.id, $scope.profile.user.oldPassword, $scope.profile.user.newPassword);
            updateCookie = function(token) {
                $.cookie('AiniaOpAuthToken', encodeURI(token), { expires: 1, path: '/' });
            };
        } else {
            promise = ProfileService.updateUserPassword($scope.profile.user.id, $scope.profile.user.oldPassword, $scope.profile.user.newPassword);
            updateCookie = function(token) {
                $.cookie('AiniaSelfAuthToken', encodeURI(token), { expires: 3, path: '/' });
            };
        }

        $scope.dialog.showStandby();
        promise.then(function(token) {
            // 后续操作
            $scope.dialog.hideStandby();

            if (!token) {
                $scope.message.error("修改密码失败！您输入正确的旧密码可能不正确。");
                return;
            }
            // 更新请求头
            httpProvider.defaults.headers.common['Authorization'] = token;
            // 更新cookie
            if (updateCookie) { updateCookie(token); }
            
            $scope.message.success("修改密码成功！");
            refresh();
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("修改密码失败！您输入正确的旧密码可能不正确。");
        });;
    };
}])
.directive("ecgProfilePassword", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : profilePasswordTemp,
        controller : "ProfilPasswordController",
        link : function($scope, $element, $attrs) {
        }
    };
}]);


});