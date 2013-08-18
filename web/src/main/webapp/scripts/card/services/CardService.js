'use strict';
define(function(require, exports) {

angular.module('ecgCardService', [])
    .factory("CardService", function($rootScope, $http) {
        var uri = PATH + "/api/card";

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
            charge: function(employee, user, card) {
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param({
                        mobile: user.mobile,
                        employeeId: employee.id,
                        activedDate: card.activedDate,
                        password: card.password
                    }),
                    url: uri + '/' + card.serial + '/charge'
                });
            },
            get: function(card) {
                return $http({
                    method: 'GET',
                    cache: false,
                    url: uri + '/' + card.serial
                }).then(function(res) {
                    return res.data;
                }, function() {
                    return null;
                });
            }
        };
    });
});