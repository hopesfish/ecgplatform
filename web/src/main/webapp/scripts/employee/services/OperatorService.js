'use strict';
define(function(require, exports) {

angular.module('ecgOperatorService', [])
    .factory("OperatorService", function($rootScope, $http, $q) {
        var uri = PATH + "/api/operator";

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
                    roles: "operator"
                };
            },
            create: function(operator) {
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(operator),
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
            update: function(operator) {
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(operator),
                    url: uri + '/' + operator.id
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
            linkExperts: function(operator, experts) {
                var posts = [], that = this;
                $(experts).each(function(i, expert) {
                    posts.push(that.linkExpert(operator, expert));
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
            linkExpert: function(operator, expert) {
                var id = operator.id || operator;
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    url: uri + '/' + id + '/expert/' + expert.id
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
            unlinkExpert: function(operator, expert) {
                var id = operator.id || operator;
                return $http({
                    method: 'DELETE',
                    url: uri + '/' + id + '/expert/' + expert.id
                });
            },
            getExperts: function(operator) {
                var id = operator.id || operator;
                return $http({
                    method: 'GET',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    url: uri + '/' + id + '/expert'
                }).then(function(res) {
                    return res.data;
                }, function() {
                    return null;
                });
            }
        };
    });
});