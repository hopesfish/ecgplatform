'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services) {
        if (!runCase('employee')) {
            return;
        }

        var it = mocha.it,
            user = mocha.user,
            httpProvider = angluarjs.httpProvider,
            ProfileService = services.ProfileService,
            ExpertService = services.ExpertService,
            OperatorService = services.OperatorService;
        
        // 登录
        var token;
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

        var expert, experts, operator, operators;
        // 测试数据准备
        it("the expert list should be retrieved", function(done) {
            expect(ExpertService).not.to.be(undefined);
            ExpertService.queryAll().then(function(items) {
                expect(items.length).not.to.be(0);
                experts = items;
                done();
            }, function() {
                throw new Error('the expert list can\'t be retrieved');
            });
        });

        it("the expert should be created when username, name and password are set", function(done) {
            expert = ExpertService.getPlainObject()
            expert.name = 'test' + (new Date()).getTime();
            expert.username = 'test' + (new Date()).getTime();
            expert.password = expert.name;
 
            ExpertService.create(expert).then(function(flag) {
                if (flag) {
                    done();
                } else {
                    throw new Error('the expert can\'t be created');
                }
            });
        });

        it("the expert id should be retrieved", function(done) {
            ProfileService.get(expert.username).then(function(employee) {
                if (employee) {
                    expect(employee.id).not.to.be(undefined);
                    expert = employee;
                    done();
                } else {
                    throw new Error('the operator id can\'t be created');
                }
            });
        });

        it("the operator list should be retrieved", function(done) {
            expect(OperatorService).not.to.be(undefined);
            OperatorService.queryAll().then(function(items) {
                expect(items.length).not.to.be(0);
                operators = items;
                done();
            }, function() {
                throw new Error('the operator list can\'t be retrieved');
            });
        });

        it("the operator should be created when username, name and password are set", function(done) {
            operator = OperatorService.getPlainObject();
            operator.name = 'test' + (new Date()).getTime();
            operator.username = 'test' + (new Date()).getTime();
            operator.password = operator.name;
 
            OperatorService.create(operator).then(function(flag) {
                if (flag) {
                    done();
                } else {
                    throw new Error('the operator can\'t be created');
                }
            });
        });

        it("the operator id should be retrieved", function(done) {
            ProfileService.get(operator.username).then(function(employee) {
                if (employee) {
                    expect(employee.id).not.to.be(undefined);
                    operator = employee;
                    done();
                } else {
                    throw new Error('the operator id can\'t be created');
                }
            });
        });

        // 测试专家接口
        it("the expert should be linked with the test operators in batch", function(done) {
            expect(expert).not.to.be(undefined);
            expect(operators.length).not.to.be(0);

            ExpertService.linkOperators(expert, operators).then(function(flag) {
                if (flag) {
                    done()
                } else {
                    throw new Error('the expert can\'t be linked again');
                }
            }, function() {
                throw new Error('the expert can\'t be linked again with server errors');
            });
        });

        it("the expert should be linked with the new operator", function(done) {
            expect(expert).not.to.be(undefined);
            expect(operator.length).not.to.be(0);

            ExpertService.linkOperator(expert, operator).then(function(flag) {
                if (flag) {
                    done()
                } else {
                    throw new Error('the expert can\'t be linked again');
                }
            }, function() {
                throw new Error('the expert can\'t be linked again with server errors');
            });
        });

        it("the links should be retrieved by expert ", function(done) {
            expect(expert).not.to.be(undefined);
            expect(operator).not.to.be(undefined);
            ExpertService.getOperators(expert).then(function(links) {
                expect(links.length).to.be(operators.length + 1);
                done();
            }, function() {
                throw new Error('the links can\'t be retrieved by expert');
            });
        });

        it("the expert should be unlinked from the expert", function(done) {
            expect(expert).not.to.be(undefined);
            expect(operator).not.to.be(undefined);
            ExpertService.unlinkOperator(expert, operator).then(function(flag) {
                done();
            }, function() {
                throw new Error('the expert can\'t be unlinked with server errors');
            });
        });

        it("the links should be retrieved as expectation again. ", function(done) {
            expect(expert).not.to.be(undefined);
            expect(operator).not.to.be(undefined);
            ExpertService.getOperators(expert).then(function(links) {
                expect(links.length).to.be(operators.length);
                done();
            }, function() {
                throw new Error('the links can\'t be retrieved as expectation again');
            });
        });

        // 测试接线员接口
        it("the operator should be linked with the test experts in batch", function(done) {
            expect(operator).not.to.be(undefined);
            expect(experts.length).not.to.be(0);

            OperatorService.linkExperts(operator, experts).then(function(flag) {
                if (flag) {
                    done();
                } else {
                    throw new Error('the expert can\'t be linked again');
                }
            }, function() {
                throw new Error('the expert can\'t be linked again with server errors');
            });
        });

        it("the operator should be linked with the expert", function(done) {
            expect(operator).not.to.be(undefined);
            expect(expert).not.to.be(undefined);
            OperatorService.linkExpert(operator, expert).then(function(flag) {
                if (flag) {
                    done();
                } else {
                    throw new Error('the operator can\'t be linked again');
                }
            }, function() {
                throw new Error('the operator can\'t be linked again with server errors');
            });
        });

        it("the link should be retrieved by operator.", function(done) {
            expect(expert).not.to.be(undefined);
            expect(operator).not.to.be(undefined);
            OperatorService.getExperts(operator).then(function(links) {
                expect(experts.length + 1).to.be(links.length);
                done();
            }, function() {
                throw new Error('the expert can\'t be linked again with server errors');
            });
        });

        it("the expert should be unlinked with the operator", function(done) {
            expect(expert).not.to.be(undefined);
            expect(operator).not.to.be(undefined);
            OperatorService.unlinkExpert(operator, expert).then(function() {
                done();
            }, function() {
                throw new Error('the expert can\'t be unlinked');
            });
        });

        it("the link should be retrieved again ", function(done) {
            expect(expert).not.to.be(undefined);
            expect(operator).not.to.be(undefined);
            OperatorService.getExperts(operator).then(function(links) {
                expect(experts.length).to.be(links.length);
                done();
            }, function() {
                throw new Error('the expert can\'t be linked again with server errors');
            });
        });

        // 用于测试的expert和operator都可以被删除
        it("the expert should be removed", function(done) {
            ExpertService.remove(expert.id).then(function() {
                done();
            }, function() {
                throw new Error('the expert can\'t be removed');
            });
        });

        it("the operator should be removed", function(done) {
            OperatorService.remove(operator.id).then(function() {
                done();
            }, function() {
                throw new Error('the operator can\'t be removed');
            });
        });

    };

});