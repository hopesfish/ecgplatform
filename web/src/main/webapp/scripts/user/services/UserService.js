define(function(require, exports) {

'use strict';
    
angular.module('ecgUserService', [])
    .factory("UserService", function($rootScope, $http) {
        var uri = PATH + "/api/user";

        return {
            queryAll: function(params) {
                var params = params || {};
                return $http({
                    method: 'GET',
                    url: uri + '?' + $.param(params)
                }).then(function(res) {
                    if (res.data.datas && res.data.datas.length > 0) {
                        return res.data.datas;
                    } else {
                        return [];    
                    }
                }, function() {
                    $rootScope.message.error('服务器异常,无法获取数据');
                    return [];
                });
            },
            getPlainObject: function() {
                return {
                    "mobile": "", // 应该是 mobile 必填
                    "name": "", // 必填
                    "username": "", // 可作为别名登录，但是现在没需求 留空 手机就是唯一登录凭证
                    "password": "",
                    "birthday": "", // 可空
                    "address": "", // 可空
                    "stature": 0, // 可空
                    "weight": 0, // 可空
                    "city": "", // 可空
                    "emContact1": "", // 可空
                    "emContact1Tel": "", // 可空
                    "emContact2": "", // 可空
                    "emContact2Tel": "", // 可空
                    "badHabits": "", // 可空
                    "anamnesis": "", // 可空
                    "remark": "", // 可空
                    "isFree": true
                };
            },
            create: function(user) {
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(user),
                    url: uri
                }).then(function(res) {
                    if (res.status === 201) {
                        return true;
                    } else {
                        return false;
                    }
                }, function() {
                    return false;
                });
            },
            findAllByMobile: function(mobile) {
                return $http({
                    method: 'GET',
                    url: uri + '?mobile=' + mobile
                }).then(function(res) {
                    if (res.data.datas && res.data.datas.length > 0) {
                        return res.data.datas;
                    } else {
                        return [];    
                    }
                }, function() {
                    $rootScope.message.error('服务器异常,无法获取数据');
                    return [];
                });
            },
            get: function(id) {
                return $http({
                    method: 'GET',
                    cache: false,
                    url: uri + '/' + id
                }).then(function(res) {
                    return res.data;
                }, function() {
                    $rootScope.message.error('服务器异常,无法获取标识为' + id + '的用户数据.');
                    return null;
                });
            },
            update: function(user) {
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(user),
                    url: uri + '/' + user.id
                });
            },           
            remove: function(id) {
                return $http({
                    method: 'DELETE',
                    url: uri + '/' + id
                });
            },
        };
    });
});