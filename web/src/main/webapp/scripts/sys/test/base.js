'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services) {
        if (!runCaseAs('admin') && !runCaseAs('chief') && !runCaseAs("expert")) {
            return;
        }

        var it = mocha.it,
            user = mocha.user,
            httpProvider = angluarjs.httpProvider,
            RuleService = services.RuleService,
            ReplyConfigService = services.ReplyConfigService,
            UserService = services.UserService;

        /**
         * 查看所有系统级别rule
         * 查看所有系统和个人级别的rule
         * 可以检测某个系统级别的rule是否唯一
         * 可以创建一个系统级别的rule
         * 可以编辑一个系统级别的rule
         * 可以初始化某个rule的检测区间
         * 可以新增某个检测区间
         * 可以删除某个检测区间
         * 可以新增某个检测区间下的回复设置
         * 可以编辑某个检测区间下的回复设置
         * 可以删除某个检测区间下的回复设置
         * 可以绑定规则到某个用户
         * 可以删除某个用户绑定
         * 可以删除rule
         */

        var token = null;

        // 登录
        it("the user should authenciated in rule test module.", function(done) {
            $.ajax({
                url: PATH + '/api/auth',
                data: {
                    'username': user.username,
                    'password': user.password
                },
                type: 'POST',
                dataType: 'json'
            }).then(function(res) {
                token = res.token;
                expect(token).not.to.be(undefined);
                httpProvider.defaults.headers.common['Authorization'] = token;
                done();
            }, function() {
                throw new Error('failed to authnenciate as admin in rule test module');
            });
        });
        
        // 默认rule应该为3个
        it("the group rule list with 3 items should be retrieved", function(done) {
            expect(token).not.to.be(undefined);
            expect(RuleService).not.to.be(undefined);
            RuleService.queryAllGroup().then(function(rules) {
                expect(rules.length).not.to.be(0);
                var rule = rules[0];
                expect(rule.type).not.to.be(undefined);
                expect(rule.code).not.to.be(undefined);
                expect(rule.name).not.to.be(undefined);
                expect(rule.min).not.to.be(undefined);
                expect(rule.max).not.to.be(undefined);
                expect(rule.remark).not.to.be(undefined);
                done();
            }, function() {
                throw new Error('the rule list can\'t be retrieved');
            });
        });

        // 空字符串无法创建
        it("the rule should not be created without required fields", function(done) {
            expect(RuleService.getPlainObject).not.to.be(undefined);
            var invalid = RuleService.getPlainObject();
            RuleService.create(invalid).then(function(flag) {
                if (flag) {
                    throw new Error('the rule can be created');
                } else {
                    done();
                }
            });
        });

        var rule  = null;

        // 新增rule
        it("the rule should be created when required fields", function(done) {
            rule = RuleService.getPlainObject();
            rule.type = '11';
            rule.code = (new Date()).getTime();
            rule.name = '测试';
            rule.min = 0;
            rule.max = 100;
            rule.remark = "说明";
 
            RuleService.create(rule).then(function(flag) {
                if (flag) {
                    done();
                } else {
                    throw new Error('the rule can\'t be created');
                }
            }, function() {
                throw new Error('the rule can\'t be created');
            });
        });
        
        // 查询rule获得id
        it("the rule should be retrieved when type, code is given and usage set as filter", function(done) {
            RuleService.queryAllGroup({
                type: rule.type,
                code: rule.code
            }).then(function(rules) {
                expect(rules.length).to.be(1);
                expect(rules[0].groupId).to.be(null); // 否则判定健康状态和自动回复都会出问题
                if (user.username === 'admin' || user.username === 'chief') {
                    expect(rules[0].employeeId).to.be(null); // 否则判定健康状态和自动回复都会出问题
                }
                rule.id = rules[0].id;
                done();
            });
        });

        return;


        // 能更新
        it("the rule should be updated as expectation", function(done) {
            rule.name = " updated";
            rule.remark += " updated";;
            RuleService.update(rule).then(function() {
                RuleService.get(rule.id).then(function(pesistedRule) {
                    expect(pesistedRule).not.to.be(undefined);
                    expect(pesistedRule.id).to.be(rule.id);
                    expect(pesistedRule.name).to.be(rule.name);
                    expect(pesistedRule.remark).to.be(rule.remark);
                    done();
                }, function() {
                    throw new Error('the rule can\'t be retrieved');
                });
            }, function() {
                throw new Error('the rule can\'t be updated by id');
            });
        });

        // 获取测试用户的信息
        var user;
        it("the user should be retrieved", function(done) {
            expect(UserService).not.to.be(undefined);

            UserService.queryAll({mobile: TESTCONFIGS.user.mobile})
            .then(function(paging) {
                expect(paging).not.to.be(undefined);
                expect(paging.datas.length).not.to.be(0);
                user = paging.datas[0];
                done();
            }, function() {
                throw new Error('the replyconfig can\'t be removed');
            });
        });

        // 绑定用户
        it("the users should be linked to the specific rule", function(done) {
            expect(user).not.to.be(undefined);

            RuleService.linkUser(rule, user)
            .then(function(flag) {
                if (flag) {
                    done();
                } else {
                    throw new Error('the users should be linked to the specific rule');
                }
            }, function() {
                throw new Error('the users should be linked to the specific rule');
            });
        });

        // 绑定的用户数应该为1
        it("one link should be retrieved by the specific rule", function(done) {
            expect(RuleService).not.to.be(undefined);
            expect(rule).not.to.be(undefined);
            expect(user).not.to.be(undefined);

            RuleService.getUsers(rule)
            .then(function(users) {
                
                expect(users).not.to.be(undefined);
                expect(users.length).to.be(1);
                expect(users[0].id).to.be(user.id);
                done();
            }, function() {
                throw new Error('the users should be linked to the specific rule');
            });
        });

        // 解除用户绑定
        it("the users should be unlinked to the specific rule", function(done) {
            expect(RuleService).not.to.be(undefined);

            RuleService.unlinkUser(rule, user)
            .then(function() {
                done();
            }, function() {
                throw new Error('the users should be linked to the specific rule');
            });
        });
        
        // 初始化检测区间
        it("the filter rule should be created", function(done) {
            RuleService.initFilterRules(rule).then(function(res) {
                expect(res).not.to.be(undefined);
                expect(res.success).to.be(3);
                done();
            }, function() {
                throw new Error('the rule can\'t be retrieved by id');
            });
        });

        // 初始化的检测区间是3个
        var filterRule;

        it("the filter rule with 3 items should be retrieved", function(done) {
            RuleService.queryAllFiltersByGroup(rule).then(function(rules) {
                expect(rules).not.to.be(undefined);
                expect(rules.length).to.be(3);
                filterRule = rules[1];
                done();
            }, function() {
                throw new Error('the rule can\'t be retrieved by id');
            });
        });

        // 不能创建非法数据
        it("the replyconfig for a specific filter rule should not be created without required fields", function(done) {
            expect(ReplyConfigService).not.to.be(undefined);
            expect(ReplyConfigService.getPlainObject).not.to.be(undefined);
            expect(filterRule).not.to.be(undefined);
            
            var invalid = ReplyConfigService.getPlainObject();

            ReplyConfigService.create(filterRule, invalid)
            .then(function(flag) {
                if (flag) {
                    throw new Error('the replyconfig can\'t not be created');
                } else {
                    done();
                }
            }, function() {
                throw new Error('the replyconfig can\'t not be created');
            });
        });

        // 创建成功
        it("the replyconfig for a specific filter rule should not be created without required fields", function(done) {
            expect(filterRule).not.to.be(undefined);
            expect(ReplyConfigService).not.to.be(undefined);
            expect(ReplyConfigService.getPlainObject).not.to.be(undefined);
            
            var valid = ReplyConfigService.getPlainObject();
            valid.result = "预设结果";
            valid.content = "预设建议内容";

            ReplyConfigService.create(filterRule, valid)
            .then(function(flag) {
                if (flag) {
                    done();
                } else {
                    throw new Error('the replyconfig can\'t not be created');
                }
            }, function() {
                throw new Error('the replyconfig can\'t not be created');
            });
        });

        // 获取新的replyconfig的id
        var replyconfig;
        it("the new replyconfig for a specific rule should be retrieved", function(done) {
            expect(filterRule).not.to.be(null);

            ReplyConfigService.queryAllbyRule(filterRule)
            .then(function(replyconfigs) {
                expect(replyconfigs).not.to.be(undefined);
                expect(replyconfigs.length).to.be(1);
                replyconfig = replyconfigs[0];
                done();
            }, function() {
                throw new Error('the replyconfig can\'t be created');
            });
        });

        // 可以更改
        it("the replyconfig for a specific rule should be updated as expectation", function(done) {
            expect(filterRule).not.to.be(null);
            expect(replyconfig).not.to.be(null);
            
            replyconfig.result = "修改结果" + (new Date()).getTime();
            replyconfig.content = "修改内容" + (new Date()).getTime();

            ReplyConfigService.update(filterRule, replyconfig)
            .then(function() {
                ReplyConfigService.queryAllbyRule(filterRule).then(function(pesistedReplyConfigs) {
                    var pesistedReplyConfig;
                    expect(pesistedReplyConfigs).not.to.be(null);
                    expect(pesistedReplyConfigs.length).to.be(1);

                    pesistedReplyConfig = pesistedReplyConfigs[0];
                    expect(pesistedReplyConfig).not.to.be(null);
                    expect(pesistedReplyConfig.result).to.be(replyconfig.result);
                    expect(pesistedReplyConfig.content).to.be(replyconfig.content);
                    done();
                }, function() {
                    throw new Error('the replyconfig can\'t be retrieved');
                });
            }, function() {
                throw new Error('the replyconfig can\'t be updated');
            });
        });

        // 可以被删除
        it("the replyconfig for a specific rule should be removed", function(done) {
            expect(filterRule).not.to.be(null);
            expect(replyconfig).not.to.be(null);

            ReplyConfigService.remove(filterRule, replyconfig.id)
            .then(function() {
                done();
            }, function() {
                throw new Error('the replyconfig can\'t be removed');
            });
        });

        // 删除rule
        it("the rule should be removed", function(done) {
            RuleService.remove(rule.id).then(function() {
                done();
            }, function() {
                throw new Error('the rule can\'t be removed');
            });
        });

        // 相关的filter rule也被删了
        it("the filter rule with 0 items should be retrieved", function(done) {
            RuleService.queryAllFiltersByGroup(rule).then(function(rules) {
                expect(rules).not.to.be(undefined);
                expect(rules.length).to.be(0);
                done();
            }, function() {
                throw new Error('the rule can\'t be retrieved by id');
            });
        });
    };

});