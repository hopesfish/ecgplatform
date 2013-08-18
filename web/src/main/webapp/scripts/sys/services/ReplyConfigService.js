'use strict';
define(function(require, exports) {

angular.module('ecgReplyConfigService', [])
    .factory("ReplyConfigService", function($rootScope, $http) {
        return {
            queryAllbyRule: function(rule) {
                return $http({
                    method: 'GET',
                    url: PATH + "/api/rule/" + rule.id + "/replyconfig"
                }).then(function(res) {
                    if (res.data && res.data.length > 0) {
                        return res.data;
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
                    title: "标题",
                    result: "",
                    content: ""
                };
            },
            create: function(rule, replyconfig) {
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(replyconfig),
                    url: PATH + "/api/rule/" + rule.id + "/replyconfig"
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
            get: function(rule, replyconfig) {
                return $http({
                    method: 'GET',
                    url: PATH + "/api/rule/" + rule.id + "/replyconfig/" + replyconfig.id
                }).then(function(res) {
                    return res.data;
                }, function() {
                    return null;
                });
            },
            update: function(rule, replyconfig) {
                delete replyconfig.version;
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(replyconfig),
                    url: PATH + "/api/rule/" + rule.id + "/replyconfig/" + replyconfig.id
                });
            },
            remove: function(rule, id) {
                return $http({
                    method: 'DELETE',
                    url: PATH + "/api/rule/" + rule.id + "/replyconfig/" + id
                });
            },
        };
    });
});