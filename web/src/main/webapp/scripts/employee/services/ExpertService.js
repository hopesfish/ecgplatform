'use strict';
define(function(require, exports) {

angular.module('ecgExpertService', [])
    .factory("ExpertService", function($rootScope, $http, $q) {
        var uri = PATH + "/api/expert";

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
                    $rootScope.message.error('服务器异常,无法获取数据');
                    return [];
                });
            },
            getTotal: function(params) {
                var params = params || {};
                if (typeof params["page.max"] === 'undefined') {
                    params["page.max"] = 1;
                }
                return $http({
                    method: 'GET',
                    url: uri + '?' + $.param(params)
                }).then(function(res) {
                    if (res.data) {
                        return res.data.total;
                    } else {
                        return 0;    
                    }
                }, function() {
                    return 0;
                });
            },
            getPlainObject: function() {
                return {
                    name: "",
                    username: "",
                    status: "OFFLINE",
                    gender: 1,
                    birthday: "",
                    idCard: "",
                    title: "",
                    mobile: "",
                    company: "AINIA健康中心",
                    enabled: true,
                    dismissed: false,
                    expire: '2099-01-01',
                    roles: "expert"
                };
            },
            create: function(expert) {
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(expert),
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
            get: function(id) {
                return $http({
                    method: 'GET',
                    cache: false,
                    url: uri + '/' + id
                }).then(function(res) {
                    return res.data;
                }, function() {
                    $rootScope.message.error('服务器异常,无法获取标识为' + id + '的数据.');
                    return null;
                });
            },
            update: function(expert) {
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(expert),
                    url: uri + '/' + expert.id
                });
            },
            remove: function(id) {
                return $http({
                    method: 'DELETE',
                    url: uri + '/' + id
                });
            },
            getRules: function(id) {
                return [];
            },
            linkOperators: function(expert, operators) {
                var posts = [], that = this;
                $(operators).each(function(i, operator) {
                    posts.push(that.linkOperator(expert, operator));
                });

                return $q.all(posts).then(function(responses) {
                    var allsuccess = true;
                    $(responses).each(function(i, result){
                        if (!result) {
                            allsuccess = false;
                        }
                    });
                    return allsuccess;
                });
            },
            linkOperator: function(expert, operator) {
                var id = expert.id || expert;
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    url: uri + '/' + id + '/operator/' + operator.id
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
            unlinkOperator: function(expert, operator) {
                var id = expert.id || expert;
                return $http({
                    method: 'DELETE',
                    url: uri + '/' + id + '/operator/' + operator.id
                });
            },
            /**
             * expert Object or String
             */
            getOperators: function(expert) {
                var id = expert.id || expert;
                return $http({
                    method: 'GET',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    url: uri + '/' + id + '/operator'
                }).then(function(res) {
                    return res.data;
                }, function() {
                    return null;
                });
            }
        };
    });
});