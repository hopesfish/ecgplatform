'use strict';

define(function(require, exports) {

var formTemplate = require("../templates/replyform.html");
var dialogTemplate = require("../templates/replydialog.html");

angular.module('ecgReplyForm', [])
.controller('ReplyFormController',  ['$scope', '$q', 'EnumService', 'TaskService', 'RuleService', 'ReplyConfigService',
    function ($scope, $q, EnumService, TaskService, RuleService, ReplyConfigService) {
    
    if ($scope.replyform) { return; }
    $scope.replyform = {};

    // 预设变量
    $scope.replyform.replys = [];

    // level名称
    $scope.replyform.translateLevel = EnumService.translateLevel;
    // status名称
    $scope.replyform.getWorkStatusLabel = EnumService.getWorkStatusLabel;

    $scope.replyform.reset = function() {
        $scope.replyform.canReply = true;
        if ($scope.todo.current.status == 'pending') {
            loadRules();
        } else {
            loadReplies();
        }
    };

    // 加载预设回复
    function loadRules() {
        var codes = EnumService.getCodes(),
            examination = $scope.todo.current.examination,
            rules = {};

        $scope.replyform.replys = [];
        $q.all([RuleService.queryAllGroup(), RuleService.queryAllGroupByUser($scope.todo.current.userId)])
        .then(function(results) {
            var querys = [];
            $(results[0]).each(function(i, rule) {
                if (!rule.employeeId) {
                    rules[rule.code] = rule;
                    rule.customed = false;
                }
            });
            $(results[1]).each(function(i, rule) {
                rules[rule.code] = rule;
                rule.customed = true;
            });
            for (var prop in rules) {
                querys.push(RuleService.queryAllFiltersByGroup(rules[prop]));
            }
            return $q.all(querys);
        })
        .then(function(results) {
            $(results).each(function(i, filters) {
                $(filters).each(function(i, filter) {
                    var val = parseFloat(examination[codes[filter.code].col]),
                        min = parseFloat(filter.min),
                        max = parseFloat(filter.max);
                    if (codes[filter.code] && min <= val && val < max) {
                        // 实际值
                        rules[filter.code].val = val;
                        rules[filter.code].level = filter.level;
                        // 回复
                        ReplyConfigService.queryAllbyRule(filter)
                        .then(function(replyconfigs) {
                            if (replyconfigs.length > 0) {
                                replyconfigs[0].level = filter.level;
                                replyconfigs[0].reason = rules[filter.code].customed ? '根据专家规则判断；' : '根据系统规则判断：';
                                replyconfigs[0].reason += codes[filter.code].label + '的测量值为，在区间[' + min + ',' + max + ')之间，';
                                replyconfigs[0].reason += '属于【' +  EnumService.getLevelLabel(filter.level) + '】。';
                                delete replyconfigs[0].id;
                                $scope.replyform.replys.push(replyconfigs[0]);
                            }
                        });
                    }
                });
            });
        });
    };

    // 加载接线员的评价
    function loadReplies() {
      TaskService.getReplyByExamination($scope.todo.current.examinationId)
      .then(function(replys) {
          $scope.replyform.replys = replys;
      });
    };

    // 监听未完成
    $scope.$watch("todo.current.examination", function() {
        if (!$scope.todo) { return; }
        if (!$scope.todo.current) { return; }
        if (!$scope.todo.current.examination) { return; }
        $scope.replyform.reset();
    });

    // 删除已选回复
    $scope.replyform.remove = function(idx) {
        var reply = $scope.replyform.replys[idx];
        if (reply && reply.rule) {
            reply.rule.used = false;
        }
        if (reply.id) {
            reply.removed = true;
        } else {
            $scope.replyform.replys.splice(idx, 1);
        }
        $scope.replyform.canReply = checkIfCanReply();
    };

    // 编辑回复
    $scope.replyform.edit = function(reply) {
        $scope.replydialog.show({
            reply: reply,
            handler: function(reply) {
                if (reply.reason) {
                   reply.reason += "专家修改";
                }
            }
        });
    };

    // 弹出人工回复窗口
    $scope.replyform.addManual = function() {
        $scope.replydialog.show({
            handler: function(reply) {
                reply.reason = "人工回复";
                $scope.replyform.replys.push(reply);
            }
        });
    };

    // 转发
    $scope.replyform.forward = function() {
        $scope.dialog.showStandby();
        TaskService.replyInBatch($scope.todo.current.examination, $scope.replyform.replys)
        .then(function(flag) {
            $scope.dialog.hideStandby();
            if (flag) {
                $scope.dialog.showStandby();
                TaskService.forward($scope.todo.current)
                .then(function(flag) {
                    $scope.dialog.hideStandby();
                    if (flag) {
                        $scope.message.success("该检测请求已转交给专家处理!");
                        $scope.todo.shift();
                    } else {
                        $scope.message.error("无法转交该任务，可能尚未配置相应专家，请联系管理员!");
                    }
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("无法转交该任务，可能尚未配置相应专家，请联系管理员!");
                });
            } else {
                $scope.message.error("保存新的回复内容时出错，请联系管理员!");
            }
        });
    };

    // 设置完成
    $scope.replyform.canReply = false;

    function checkIfCanReply() {
        var removes = 0, len = $scope.replyform.replys.length;
        $($scope.replyform.replys).each(function(i, reply) {
            if (reply.removed) {
                removes++;
            }
        });
        if (len === 0 || len === removes) {
            return false;
        }
        return true;
    };

    $scope.replyform.complete = function() {
        if (!checkIfCanReply()) {
            $scope.dialog.alert({
                text: '没有回复内容!'
            });
            return;
        }

        $scope.dialog.showStandby();
        TaskService.replyInBatch($scope.todo.current.examination, $scope.replyform.replys)
        .then(function(flag) {
            $scope.dialog.hideStandby();
            if (flag) {
                $scope.dialog.showStandby();
                TaskService.complete($scope.todo.current)
                .then(function(flag) {
                    $scope.dialog.hideStandby();
                    if (flag) {
                        $scope.message.success("该检测请求已回复!");
                        $scope.todo.shift();
                    } else {
                        $scope.message.error("回复失败，请联系管理员!");
                    }
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("回复失败，请联系管理员!");
                });
            } else {
                $scope.message.error("保存新的回复内容时出错，请联系管理员!");
            }
        });
    };
}])
.directive("ecgReplyForm", ['$location', function ($location) {
    return {
        restrict: 'A',
        replace: false,
        template: formTemplate,
        controller: "ReplyFormController",
        link: function ($scope, $element, $attrs) {}
    };
}])
.controller('ReplyDialogController', 
    ['$scope', '$filter', '$timeout', '$location', 'EnumService', 'TaskService', 
    function ($scope, $filter, $timeout, $location, EnumService, TaskService) {

    // 命名空间
    $scope.replydialog = {};

    // 表格展示
    $scope.replydialog.reply = null;

    function reset() {
        $scope.replydialog.reply = TaskService.getPlainReply();
    };
    reset();

    $scope.replydialog.execute = function() {
        var selecteds = [];
        if ($scope.replydialog.handler instanceof Function) {
            $scope.replydialog.handler($scope.replydialog.reply);
        }
        $scope.replydialog.hide();
    };

    $scope.replydialog.hide = function(opts) {
        reset();
        $('#ecgReplyDialog').modal('hide');
    };

    $scope.replydialog.show = function(opts) {
        var opts = opts || {};
        if (opts.reply) {
            $scope.replydialog.reply = opts.reply;
        }
        $scope.replydialog.handler = opts.handler;
        $('#ecgReplyDialog').modal('show');
    };

}])
.directive("ecgReplyDialog", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : dialogTemplate,
        controller : "ReplyDialogController",
        link : function($scope, $element, $attrs) {
        }
    };
}]);

});

/*
设备类型  指标编号  指标名称  正常下限  正常上限  指标说明
11  1 舒张压 60  89  血压的舒张压（毫米汞柱）
11  2 收缩压 89  139 血压的收缩压（毫米汞柱）
11  3 心率  60  100 次/分钟
11  4 血氧饱和度 94  100 百分比
11  5 呼吸  16  20  次/分钟
11  6 体温  36  37  ℃（腋下温度）
11  7 脉率  60  100 次/分钟

10-手机（内置检测硬件的手机）
设备类型  指标编号  指标名称  正常下限  正常上限  指标说明
10  3 心率  60  100 次/分钟
10  6 体温  36  37  ℃（腋下温度）

*/