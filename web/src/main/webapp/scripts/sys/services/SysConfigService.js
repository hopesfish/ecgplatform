'use strict';
define(function(require, exports) {

angular.module('ecgSysConfigService', [])
    .factory("SysConfigService", function($rootScope, $q, $http) {
        var uri = PATH + "/api/sysconfig";

        return {
            queryAll: function(params) {
                var params = params || {};
                if (typeof params["page.max"] === 'undefined') {
                    params["page.max"] = 999;
                }
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
            update: function(configs) {
                var puts = [];
                $(configs).each(function(i, _config) {
                    var config = $.extend({}, _config);
                    delete config.version;
                    puts.push($http({
                        method: 'PUT',
                        headers:{'Content-Type':'application/x-www-form-urlencoded'},
                        data: $.param(config),
                        url: uri + '/' + config.id
                    }));
                });

                return $q.all(puts).then(function(responses) {
                    var allsuccess = true;
                    $(responses).each(function(i, result){
                        if (!result) {
                            allsuccess = false;
                        }
                    });
                    return allsuccess;
                });
            },
        };
    });
});