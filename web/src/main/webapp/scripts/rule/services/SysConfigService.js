'use strict';
define(function(require, exports) {

angular.module('ecgSysConfigService', [])
    .factory("SysConfigService", function($rootScope, $http) {
        var uri = PATH + "/api/sysconfig";

        return {
            queryAll: function(params) {
                var params = params || {};
                return $http({
                    method: 'GET',
                    url: uri + '?' + $.param(params)
                }).then(function(res) { // 构造session用户
                    if (res.data.datas && res.data.datas.length > 0) {
                        return res.data.datas;
                    } else {
                        return [];    
                    }
                }, function() {
                    return [];
                });
            },
            update: function(config) {
                var config = $.extend({}, config);
                delete config.version;
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(config),
                    url: uri + '/' + config.id
                });
            },
        };
    });
});