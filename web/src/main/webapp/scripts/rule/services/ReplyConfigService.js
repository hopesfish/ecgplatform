'use strict';
define(function(require, exports) {

angular.module('ecgReplyConfigService', [])
    .factory("ReplyConfigService", function($rootScope, $http) {
        var mock = null; /*[{
            id: 1,
            result: 1,
            text: '请您用正确的方式再重新测量一遍！'
        }, {
            id: 2,
            result: 1,
            text: '您的心电图显示，心律、心率是正常的，但您手机上的心率数字是不正确的，可能与心电测试时姿势不正确有关。建议您重新测试一次！'
        }, {
            id: 4,
            result: 1,
            text: '检测波型不正确，按照正常的操作方式重新检测发送。'
        }, {
            id: 5,
            result: 1,
            text: '您刚才发送的心电信息图形不完整，请您按照正确的操作方式重新检测发送，谢谢！'
        }, {
            id: 3,
            result: 2,
            text: '心动过缓,心率低于30次／分以下,可能为病理性或药物性的变化,建议您不定期监测心率并发送数据给服务中心,稍后会有专家给您致电提出健康指导，并建议您赴医院治疗,必要时安装心脏起博器。'
        }];*/

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
                    return mock || [];
                });
            },
            getTotal: function() {
                return 0;
            },
            getPlainObject: function() {
                return {
                    title: "标题",
                    result: "",
                    content: ""
                };
            },
            create: function(rule, reply) {
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(reply),
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
            update: function(rule, reply) {
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(reply),
                    url: PATH + "/api/rule/" + rule.id + "/replyconfig/" + reply.id
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