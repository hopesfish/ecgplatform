define(function(require, exports) {

'use strict';
    
angular.module('ecgUserService', [])
    .factory("UserService", function($rootScope, $http) {
        var uri = PATH + "/api/user";

        return {
            queryAll: function(params) {
                var params = params || {};
                if (typeof params["page.max"] == 'undefined') {
                    params["page.max"] = 15; // user.html的每页行数也需要一起修改
                }
                return $http({
                    method: 'GET',
                    url: uri + '?' + $.param(params)
                }).then(function(res) {
                    if (res.data.datas) {
                        return res.data;
                    } else {
                        return null;    
                    }
                }, function() {
                    return null;
                });
            },
            getPlainObject: function() {
                return {
                    "mobile": "", // 应该是 mobile 必填
                    "name": "", // 必填
                    "username": "", // 可作为别名登录，但是现在没需求 留空 手机就是唯一登录凭证
                    "email": "",
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
                    "gender": 1, // 男
                    "married": 1, // 已婚
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
                    return [];
                });
            },
            findAllByEmail: function(email) {
                return $http({
                    method: 'GET',
                    url: uri + '?email=' + email
                }).then(function(res) {
                    if (res.data.datas && res.data.datas.length > 0) {
                        return res.data.datas;
                    } else {
                        return [];    
                    }
                }, function() {
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
            resetPassword: function(user) {
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param({oldPassword: '', newPassword: ''}),
                    url: uri + '/' + user.id + '/password'
                });
            },        
            remove: function(id) {
                return $http({
                    method: 'DELETE',
                    url: uri + '/' + id
                });
            },
            charge: function(user, card) {
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param({
                        serial: card.serial,
                        activedDate: card.activedDate,
                        password: card.password
                    }),
                    url: uri + '/' + user.id + '/charge'
                });
            },
            queryAllCharge: function(user, params) {
                var params = params || {};
                if (typeof params["page.max"] == 'undefined') {
                    params["page.max"] = 15; // user.html的每页行数也需要一起修改
                }
                return $http({
                    method: 'GET',
                    url: uri + '/' + user.id + '/card?' + $.param(params)
                }).then(function(res) {
                    if (res.data.datas) {
                        return res.data;
                    } else {
                        return null;    
                    }
                }, function() {
                    return null;
                });
            },
        };
    });
});