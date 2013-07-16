'use strict';
define(function(require, exports) {

angular.module('ecgRuleService', [])
    .factory("RuleService", function($rootScope, $http) {
        var uri = PATH + "/api/rule";

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
                    $rootScope.message.error('服务器异常,无法获取数据');
                    return [];
                });
            },
            queryAllGroup: function(params) {
                var params = params || {};
                params.usage = 'group';
                return this.queryAll(params); 
            },
            queryAllFiltersByGroup: function(rule, params) {
                var id = rule.id || rule, params = params || {};
                params.groupId = id;
                params.usage = 'filter';
                return this.queryAll(params);
            },
            queryAllGroupByUser: function(user) {
                var id = user.id || user;
                return $http({
                    method: 'GET',
                    url: PATH + "/api/user/" + id + "/rule"
                }).then(function(res) { // 构造session用户
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
                    "type": 11, // 必填 数字
                    "code": "", // 必填 数字
                    "name": "", // 必填
                    "min": 0, // 必填 数字
                    "max": 1000, // 必填 数字
                    "unit": "单位", // 可填 单位
                    "remark": "", // 可填 说明
                    "level": "success", // 可填 级别
                    "usage": "group",
                    "canReply": true,
                    "employeeId": null, // 所有者
                    "groupId": null // 所需规则
                };
            },
            create: function(rule) {
                var rule = $.extend({}, rule || {});
                delete rule.id;
                delete rule.replys;
                delete rule.replyconfigs;
                delete rule.percent;
                delete rule.arrayIdx;
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(rule),
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
            update: function(rule) {
                var rule = $.extend({}, rule);
                delete rule.replys;
                delete rule.replyconfigs;
                delete rule.version;
                delete rule.percent;
                delete rule.arrayIdx;
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(rule),
                    url: uri + '/' + rule.id
                });
            },
            remove: function(id) {
                return $http({
                    method: 'DELETE',
                    url: uri + '/' + id
                });
            },
            initFilterRules: function(inRule) {
                var that = this,
                    groupId = inRule.id, low, mid, high,
                    success = 0, error = 0, rule;

                rule = $.extend({}, inRule);
                delete rule.id;

                low = $.extend({}, rule);
                low.max =low.min;
                low.min = -9999;
                low.usage = "filter";
                low.level = 'outside';
                low.groupId = groupId;

                mid = $.extend({}, rule);
                mid.usage = "filter";
                mid.groupId = groupId;

                high = $.extend({}, rule);
                high.min =high.max;
                high.max = 9999;
                high.usage = "filter";
                high.level = 'outside';
                high.groupId = groupId;

                return this.create(low)
                .then(function(result) {
                    if (result) {
                        success++;
                    } else {
                        error++
                    }
                }, function() {
                    error++
                })
                .then(function() {
                    return that.create(mid);
                })
                .then(function(result) {
                    if (result) {
                        success++;
                    } else {
                        error++
                    }
                }, function() {
                    error++
                })
                .then(function() {
                    return that.create(high);
                })
                .then(function(result) {
                    if (result) {
                        success++;
                    } else {
                        error++
                    }
                }, function() {
                    error++
                })
                .then(function() {
                    return {success: success, error: error};
                });
            },
            sortRules: function(rules) {
                var min = 999999, max = -999999, range = 100, that = this;
                $(rules).each(function(i, rule) {
                    if (rule.level !== 'outside') {
                        rule.min = parseFloat(rule.min);
                        rule.max = parseFloat(rule.max);
                        if (rule.min < min) {
                            min = rule.min;
                        }
                        if (rule.max > max) {
                            max = rule.max;
                        }
                    }
                });
                range = max - min;
                $(rules).each(function(i, rule) {
                    if (rule.min === -9999 || rule.max === 9999) {
                        rule.percent = '5';
                    } else {
                        rule.percent = (rule.max - rule.min) / range * 90;
                    }
                });

                rules.sort(function(a, b) {
                    return a.min > b.min ? 1 : -1;
                });

                $(rules).each(function(i, rule) {
                    rule.arrayIdx = i;
                });
                return rules;
            },
            getUsers: function(rule) {
                var id = rule.id || rule;
                return $http({
                    method: 'GET',
                    url: uri + '/' + id + '/user'
                }).then(function(res) {
                    if (res.data && res.data.length > 0) {
                        return res.data;
                    } else {
                        return [];    
                    }
                }, function() {
                    //$rootScope.message.error('服务器异常,无法获取数据');
                    return [];
                });
            },
            linkUser: function(rule, user) {
                var id = rule.id || rule;
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    url: uri + '/' + id + '/user/' + user.id
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
            unlinkUser: function(rule, user) {
                var id = rule.id || rule;
                return $http({
                    method: 'DELETE',
                    url: uri + '/' + id + '/user/' + user.id
                });
            }

        };
    });
});