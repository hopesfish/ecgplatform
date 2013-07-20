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
                expect(res.token).not.to.be(undefined);
                token = res.token;
                httpProvider.defaults.headers.common['Authorization'] = token;
                done();
            }, function() {
                throw new Error('failed to authnenciate as admin in rule test module');
            });
        });

        // Operator
        it("the operator list should be retrieved", function(done) {
            expect(OperatorService).not.to.be(undefined);
            OperatorService.queryAll().then(function(operators) {
                expect(operators.length).not.to.be(0);
                done();
            }, function() {
                throw new Error('the operator list can\'t be retrieved');
            });
        });

        it("the operator should not be created without username and name", function(done) {
            expect(OperatorService.getPlainObject).not.to.be(undefined);
            var invalid = OperatorService.getPlainObject();
            OperatorService.create(invalid).then(function(flag) {
                if (flag) {
                    throw new Error('the operator can be created');
                } else {
                    done();
                }
            });
        });

        it("the operator should not be created without password", function(done) {
            expect(OperatorService.getPlainObject).not.to.be(undefined);
            var invalid = OperatorService.getPlainObject();
            invalid.name = 'test' + (new Date()).getTime();
            invalid.username = 'test' + (new Date()).getTime();
            invalid.password = '';
 
            OperatorService.create(invalid).then(function(flag) {
                if (flag) {
                    throw new Error('the operator can be created');
                } else {
                    done();
                }
            });
        });

        var operator;

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

        it("the operator should able to login in with default password", function(done) {
            $.ajax({
                url: '/api/auth',
                data: {
                    'username': operator.username,
                    'password': operator.password
                },
                type: 'POST',
                dataType: 'json'
            }).then(function(res) {
                done();
            }, function() {
                throw new Error('the operator can\'t login in');
            });
        });

        it("the operator id should be retrieved by ProfileService", function(done) {
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

        it("the operator should be updated as expectation", function(done) {
            OperatorService.get(operator.id).then(function(operator) {
                operator.status = 'ONLINE';
                operator.gender = 0;
                operator.birthday = '1983-01-21';
                operator.idCard = '440803198801122455';
                operator.title= '主任1';
                operator.mobile= '13800000000';
                operator.company = 'operator company';
                operator.enabled = false;
                operator.dismissed = true;
                delete operator.version;
                OperatorService.update(operator).then(function() {
                    OperatorService.get(operator.id).then(function(pesistedOperator) {
                        if (pesistedOperator) {
                            expect(pesistedOperator.id).to.be(operator.id);
                            expect(pesistedOperator.username).to.be(operator.username);
                            expect(pesistedOperator.name).to.be(operator.name);
                            expect(pesistedOperator.status).to.be(operator.status);
                            expect(pesistedOperator.title).to.be(operator.title);
                            expect(pesistedOperator.mobile).to.be(operator.mobile);
                            expect(pesistedOperator.birthday).to.be(operator.birthday);
                            expect(pesistedOperator.company).to.be(operator.company);
                            expect(pesistedOperator.enabled).to.be(operator.enabled);
                            expect(pesistedOperator.dismissed).to.be(operator.dismissed);
                            expect(pesistedOperator.roles).to.be(operator.roles);
                            done();
                        } else {
                            throw new Error('the operator can\'t be retieved again');
                        }
                    });
                }, function() {
                    throw new Error('the operator can\'t be updated by id');
                });
            }, function() {
                throw new Error('the operator can\'t be retrieved by id');
            });
        });

        it("the operator\' password should be reset", function(done) {
            ProfileService.resetPassword(operator.id)
            .then(function() {
                done();
            }, function() {
                throw new Error('the operator\'s password can\'t be reset.');
            });
        });

        it("the operator should able to login in with reset password", function(done) {
            $.ajax({
                url: '/api/auth',
                data: {
                    'username': operator.username,
                    'password': operator.username
                },
                type: 'POST',
                dataType: 'json'
            }).then(function(res) {
                done();
            }, function() {
                throw new Error('the operator can\'t login in');
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