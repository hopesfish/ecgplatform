'use strict';
define(function(require) {
    
require("./common/main");
require("./task/main");
require("./user/main");
require("./profile/main");

var welcomeTemp = require("./user/templates/welcome.html");
var helpTemp = require("./user/templates/help.html");
var faqTemp = require("./user/templates/faq.html");

// GOABAL VAL
window.PATH = window.location.pathname.slice(0, window.location.pathname.lastIndexOf("/"));
    
angular.module('ecgApp', ['ecgCommon', 'ecgTask', 'ecgUser', 'ecgProfile'])
.config(['$httpProvider', '$routeProvider', function ($httpProvider, $routeProvider) {
        var token = $.cookie('AiniaSelfAuthToken');
        // header头带认证参数
         $httpProvider.defaults.headers.common['Authorization'] = token;
    
        // 配置路由,和模块相关的配置均在相应模块下的main.js
        $routeProvider
        .when('/welcome', {
            template: welcomeTemp,
            controller: function($scope) {
                $scope.subheader.title = "个人中心";
            }
        })
        .when('/help', {
            template: helpTemp,
            controller: function($scope) {
                $scope.subheader.title = "使用帮助";
            }
        })
        .when('/faq', {
            template: faqTemp,
            controller: function($scope) {
                $scope.subheader.title = "常见问题";
            }
        })
        .otherwise({
            redirectTo: '/task'
        });
}])
.run(['$rootScope', '$http', '$location', 'UserService', function($rootScope, $http, $location, UserService) {
    // 设置全局变量
    $rootScope.PATH = PATH;

    // 公用函数:退出系统
    function logout(msg) {
        if (msg) { alert(msg); }
        window.location.href = "ulogin.html";
    };
    window.logout = logout;

    // 加载完成, 显示工作界面
    function inited() {
        $(document.body).removeClass("noscroll");
        $("#loadingpage").hide();
    }

    // 判断是否登录
    var username = $.cookie("AiniaSelfUsername");
    if (!username) {
        logout('请先登录!');
        return;
    }

    // 设置session的用户
    $rootScope.session = {user: {roles: ""}};
    UserService.findAllByMobile(username)
    .then(function(users) {
        if (users && users.length == 1) {
            var user = users[0];
            user.roles = "user";
            user.isAdmin =  user.isChief = user.isExpert = user.isOperator = function() {
                return false;
            };
            $rootScope.session.user = user;
            $.cookie("AiniaSelfUserId", user.id, { expires: 1, path: '/' });
            $location.path("/task");
        } else {
            logout('无法获取您登录名为' + username +'的用户信息。请与管理员联系!');
        }
    }, function() {
        logout('获取您登录名为' + username +'用户信息时, 服务器异常。请与管理员联系!');
    })
    .then(function() { // 显示工作界面
        inited();
    });

}]);

angular.bootstrap(document, ["ecgApp"]);

});