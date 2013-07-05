'use strict';
'use strict';
define(function(require, exports) {

    exports.testRule = function(it, RuleService) {

        // Rule
        it("the ruleService should be defined", function() {
            expect(RuleService).not.to.be(undefined);
        });

        it("the rule list should be retrieved", function(done) {
            RuleService.queryAll().then(function(rules) {
                if (rules.length > 0) {
                    var rule = rules[0];
                    expect(rule.type).not.to.be(undefined);
                    expect(rule.code).not.to.be(undefined);
                    expect(rule.name).not.to.be(undefined);
                    expect(rule.min).not.to.be(undefined);
                    expect(rule.max).not.to.be(undefined);
                    expect(rule.remark).not.to.be(undefined);
                    done();
                } else {
                    throw new Error('the rule list can\'t be retrieved');
                }
            }, function() {
                throw new Error('the rule list can\'t be retrieved');
            });
        });

        it("the getPlainObject method of RuleService should be defined", function() {
            expect(RuleService.getPlainObject).not.to.be(undefined);
        });

        it("the rule should not be created without required fields", function(done) {
            var invalid = RuleService.getPlainObject();
            RuleService.create(invalid).then(function(flag) {
                if (flag) {
                    throw new Error('the rule can be created');
                } else {
                    done();
                }
            });
        });

        var rule = RuleService.getPlainObject();

        it("the rule should be created when required fields are set", function(done) {
            rule.type = '11';
            rule.code = '1';
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
            });
        });

        it("the rule should be retrieved when type, code is given and usage set as filter", function(done) {
            RuleService.queryAll({
            	type: rule.type,
            	code: rule.code,
            	usage: 'filter'
            }).then(function(rules) {
                if (rules.length == 1) {
                    rule.id = rules[0].id;
                    done();
                } else {
                    throw new Error('the rule can\'t be created');
                }
            });
        });

        it("the rule should be updated as expectation", function(done) {
            RuleService.get(rule.id).then(function(rule) {
                rule.min = 2;
                rule.max = 99;;
                delete rule.version;
                RuleService.update(rule).then(function() {
                    RuleService.get(rule.id).then(function(pesistedRule) {
                        if (pesistedRule) {
                        	expect(pesistedRule.id).to.be(rule.id);
                            expect(pesistedRule.min).to.be(rule.min);
                            expect(pesistedRule.max).to.be(rule.max);
                            done();
                        } else {
                            throw new Error('the rule can\'t be retieved again');
                        }
                    });
                }, function() {
                    throw new Error('the rule can\'t be updated by id');
                });
            }, function() {
                throw new Error('the rule can\'t be retrieved by id');
            });
        });

        it("the rule should be removed", function(done) {
            RuleService.remove(rule.id).then(function() {
                done();
            }, function() {
                throw new Error('the rule can\'t be removed');
            });
        });
    };

});