'use strict';
define(function(require, exports) {

angular.module('ecgAPKService', [])
    .factory("APKService", function($rootScope, $http) {
        var uri = PATH + "/api/apk";

        return {
            queryAll: function(params) {
                var params = params || {};
                if (typeof params["page.max"] === 'undefined') {
                    params["page.max"] = 999;
                }
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
                    return null;
                });
            },
            remove: function(id) {
                return $http({
                    method: 'DELETE',
                    url: uri + '/' + id
                });
            }
        };
    });
});