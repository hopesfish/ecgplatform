'use strict';
define(function(require, exports) {

angular.module('ecgRuleConfig', [])
.controller('ReplyConfigController', 
    ['$scope', '$routeParams', 'EnumService', 'RuleService', 'ReplyConfigService', 
    function ($scope, $routeParams, EnumService, RuleService, ReplyConfigService) {
    // 表格头
    $scope.subheader.title = "规则设置";

    // 命名空间
    $scope.ruleconfig = {};

    // 对应的规则 usage as group
    $scope.ruleconfig.rule = null; 

    // 是否只读
    $scope.ruleconfig.editable = false;
    $scope.$watch("session.user", function() {
        var user = $scope.session.user;
        if (!user.isAdmin) { return; }
        if (user.isAdmin() || user.isChief()) {
            $scope.ruleconfig.editable = true;
        }
    });
    
    // 数据范围
    $scope.ruleconfig.rules = null; 

    // level名称
    $scope.ruleconfig.getLevelLabel = EnumService.getLevelLabel;

    function refreshRules() {
        reset();
        $scope.dialog.showLoading();
        RuleService.get($routeParams.id)
        .then(function(rule) {
            $scope.dialog.hideStandby();

            $scope.ruleconfig.rule = rule;

            // 更新
            $scope.subheader.title = "规则设置 - " + rule.name;
            
             // 是否readonly
            var user = $scope.session.user;
            $scope.ruleconfig.editable = rule.employeeId == user.id;
            if (user.isAdmin() || user.isChief()) {
                $scope.ruleconfig.editable = true;
            }

            // 查询该组rule
            $scope.dialog.showLoading();
            RuleService.queryAllFiltersByGroup($routeParams.id)
            .then(function(rules) {
                // 初始化检测区间
                $scope.dialog.hideStandby();
                
                if (rules.length === 0) {
                    $scope.dialog.showStandby({text: '正在创建初始化检测区间，请稍候......'});
                    RuleService.initFilterRules(rule)
                    .then(function(res) {
                        $scope.dialog.hideStandby();
                        if (res.success === 3) {
                            $scope.message.success("创建初始化检测区间成功!");
                            refreshRules();
                        } else {
                            $scope.message.error("创建初始化检测区间失败!");
                            refreshRules();
                        }
                    }, function() {
                        $scope.dialog.hideStandby();
                    });
                } else {
                    $scope.ruleconfig.rules = RuleService.sortRules(rules);
                    $($scope.ruleconfig.rules).each(function(i, rule) {
                        rule.replyconfigs = [];
                        refreshReplyConfigs(rule, i);
                    });
                }
            }, function () {
                $scope.dialog.hideStandby();
                $scope.message.error("加载区间失败!");
            });
        });
    };
    refreshRules();

    // 初始化/更新回复
    function refreshReplyConfigs(rule, i) {
        ReplyConfigService.queryAllbyRule(rule).then(function(replyconfigs) {
            rule.replyconfigs = replyconfigs;
            if (i === 1) {
                $scope.ruleconfig.selectedRule = rule;
            }
        });
    };

    // 当前选择中config
    $scope.ruleconfig.selectedRule = null;
    $scope.ruleconfig.selectedReplyConfig = ReplyConfigService.getPlainObject();

    // 是否显示某个label的区间
    $scope.ruleconfig.getRuleText = function(rule) {
        return rule.level === 'outside' ? '' : rule.min + ' - ' + rule.max;
    };

    // 选择某个config
    $scope.ruleconfig.onselectrule = function(rule) {
        $scope.ruleconfig.newobj = {};
        $scope.ruleconfig.selectedRule = rule;
        reset();
    };

    // 选择某个config
    $scope.ruleconfig.onselectconfig = function(config) {
        $scope.ruleconfig.selectedReplyConfig = config;
    };

    // 新区间
    $scope.ruleconfig.newRule = function() {
        var point = parseFloat(prompt("请输入1-1000之间的数字作为区间分界点:"), 10);
        if (isNaN(point)) {
            return;
        }

        if (!angular.isNumber(point)) {
            $scope.message.warn("请输入数字!");
            return;
        }

        if (point < 1 || point > 1000) {
            $scope.message.warn("只能输入1-1000之间的数字!");
            return;
        }

        var updateobj = null, newobj;
        $($scope.ruleconfig.rules).each(function(i, rule) {
            if (rule.min !== -9999 && rule.max !== 9999) {
                if (rule.min < point && point < rule.max) {
                    updateobj = rule;
                    newobj = $.extend({}, rule);
                }
            }
        });

        if (updateobj) {
            updateobj.max = point;
            newobj.min = point;
            delete newobj.id;
            $scope.dialog.showStandby();
            RuleService.update(updateobj)
            .then(function() {
                RuleService.create(newobj)
                .then(function(flag) {
                    if (flag) {
                        $scope.dialog.hideStandby();
                        $scope.message.success("新增区间成功!");
                        refreshRules();
                    } else {
                        $scope.dialog.hideStandby();
                        $scope.message.error("新增区间失败!");
                    }
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("新增区间失败!");
                });
            }, function() {
                $scope.dialog.hideStandby();
                $scope.message.error("修改已有区间失败!");
            });
        } else {
            $scope.message.warn("该点已存在或超出可填范围!");
        }
    };
    
    // 修改level 
    $scope.ruleconfig.setLevel = function(level) {
        if (!$scope.ruleconfig.editable) { return; }
        if ($scope.ruleconfig.selectedRule.level === level) { return; }
        $scope.ruleconfig.selectedRule.level = level;
        $scope.dialog.showStandby();
        RuleService.update($scope.ruleconfig.selectedRule)
        .then(function() {
            $scope.dialog.hideStandby();
            $scope.message.success("修改级别成功!");
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("修改级别失败!");
            refreshRules();
        });
    };

    // 删除区间
    $scope.ruleconfig.confirmDeleteRule = function() {
        var selectedItem = $scope.ruleconfig.selectedRule;
        if (selectedItem === null) {
            $scope.dialog.alert({
                text: '请选择一个区间!'
            });
            return;
        }
        if ($scope.ruleconfig.rules.length === 3) {
            $scope.dialog.alert({
                text: '至少保留一个区间!'
            });
            return;
        }
        $scope.dialog.confirm({
            text: "请确认删除区间[" + selectedItem.min + "," + selectedItem.max + ")及其相关预设回复, 该操作无法恢复!",
            handler: function() {
                var updateobj;
                if (selectedItem.arrayIdx === 1) {
                    updateobj = $scope.ruleconfig.rules[2];
                    updateobj.min = selectedItem.min;
                } else {
                    updateobj = $scope.ruleconfig.rules[selectedItem.arrayIdx - 1];
                    updateobj.max = selectedItem.max;
                }
                $scope.dialog.showStandby();
                RuleService.update(updateobj).then(function() {
                    RuleService.remove(selectedItem.id)
                    .then(function() {
                        $scope.dialog.hideStandby();
                        $scope.message.success("删除区间成功!");
                        // 刷新
                        refreshRules();
                    }, function() {
                        $scope.dialog.hideStandby();
                        $scope.message.error("无法删除该数据,可能是您的权限不足,请联系管理员!");
                        // 刷新
                        refreshRules();
                    });
                }, function() {
                    $scope.message.error("更新相邻区间失败!");
                    // 刷新
                    refreshRules();
                });
            }
        });
    };

    // 新增/更新回复
    $scope.ruleconfig.save = function() {
        if ($scope.ruleconfig.selectedReplyConfig.id) {
            $scope.dialog.showStandby();
            ReplyConfigService.update($scope.ruleconfig.selectedRule, $scope.ruleconfig.selectedReplyConfig)
            .then(function() {
                $scope.dialog.hideStandby();
                $scope.message.success("更新回复设置成功!");
                refreshReplyConfigs($scope.ruleconfig.selectedRule);
            }, function() {
                $scope.dialog.hideStandby();
                $scope.message.error("无法新增,可能是您的权限不足,请联系管理员!");
            });
        } else {
            $scope.dialog.showStandby();
            ReplyConfigService.create($scope.ruleconfig.selectedRule, $scope.ruleconfig.selectedReplyConfig)
            .then(function(flag) {
                $scope.dialog.hideStandby();
                if (flag) {
                    $scope.message.success("新增回复设置成功!");
                    reset();
                    refreshReplyConfigs($scope.ruleconfig.selectedRule);
                } else {
                    $scope.message.error("无法新增回复设置!");
                }
            }, function() {
                $scope.dialog.hideStandby();
                $scope.message.error("无法新增,可能是您的权限不足,请联系管理员!");
            });
        }
        
    };

    // reset
    function reset() {
        $scope.ruleconfig.selectedReplyConfig = ReplyConfigService.getPlainObject();
    }

    $scope.ruleconfig.reset = function() {
        refreshReplyConfigs($scope.ruleconfig.selectedRule);
        reset();
    };

    // 删除某条回复
    $scope.ruleconfig.remove = function(deletedItem) {
        $scope.dialog.confirm({
            text: "请确认删除, 该操作无法恢复!",
            handler: function() {
                reset();
                $scope.dialog.showStandby();
                ReplyConfigService.remove($scope.ruleconfig.selectedRule, deletedItem.id)
                .then(function() {
                    $scope.dialog.hideStandby();
                    $scope.message.success("删除成功!");
                    // 刷新
                    refreshReplyConfigs($scope.ruleconfig.selectedRule);
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("无法删除该数据,可能是您的权限不足,请联系管理员!");
                });
            }
        });
    };

    // 是否显示新增或者修改界面
    $scope.ruleconfig.showForm = function() {
        var flag = false;
        if ($scope.ruleconfig.selectedRule) {
            if ($scope.ruleconfig.selectedReplyConfig.id) {
                flag = true;
            }
            if ($scope.ruleconfig.selectedRule.replyconfigs.length < 1) {
                flag = true;
            }
        }
        flag = flag && $scope.ruleconfig.editable;
        return flag;
    };

    // 为个人规则绑定用户
    $scope.ruleconfig.users = [];

    function refreshLinks() {
        RuleService.getUsers($routeParams.id).then(function(users) {
            $scope.ruleconfig.users = users;
        }, function() {
            $scope.message.error("加载用户数据失败!");
        });
    }
    refreshLinks();

    $scope.ruleconfig.isCheckAll = false;
    $scope.ruleconfig.checkAll = function() {
        $scope.ruleconfig.isCheckAll = !$scope.ruleconfig.isCheckAll;
        $($scope.ruleconfig.users).each(function(i, user) {
            user.removed = $scope.ruleconfig.isCheckAll;
        });
    };

    $scope.ruleconfig.addUsers = function() {
        $scope.userdialog.show({
            excludes: $scope.ruleconfig.users,
            handler: function(users) {
                var len = users.length, count = 0;
                $(users).each(function(i, user) {
                    $scope.dialog.showStandby();
                    RuleService.linkUser($routeParams.id, user)
                    .then(function(flag) {
                        $scope.dialog.hideStandby();
                        if (flag) {
                            count++;
                        } else {
                            $scope.message.error("无法绑定用户：" + user.name);
                        }
                        if (count === len) {
                            $scope.message.success("绑定用户成功！");
                            refreshLinks();
                        }
                    }, function() {
                        $scope.dialog.hideStandby();
                        $scope.message.error("无法绑定接线员：" + user.name);
                    });
                });
            }
        });
    };

    $scope.ruleconfig.removeUsers = function() {
        var removes = [], users = $scope.ruleconfig.users, len = 0, count = 0;

        $(users).each(function(i, user) {
            if (user.removed) {
                removes.push(user);
            }
        });
        
        if (removes.length == 0) {
            $scope.dialog.alert({
                text: '请选择需要解除的绑定!'
            });
            return;
        };

        len = removes.length;

        $scope.dialog.confirm({
            text: "请确认解除这些用户绑定关系, 该操作无法恢复!",
            handler: function() {
                $scope.dialog.showStandby();
                $(removes).each(function(i, remove) {
                    RuleService.unlinkUser($routeParams.id, remove)
                    .then(function() {
                        count++;
                        if (count === len) {
                            $scope.dialog.hideStandby();
                            $scope.message.success("成功解除绑定！");
                            refreshLinks();
                        }
                    }, function() {
                        count++;
                        $scope.dialog.hideStandby();
                        $scope.message.error("无法解除绑定，用户名为：" + remove.name);
                    });
                });
            }
        });    
    };

}]);


});