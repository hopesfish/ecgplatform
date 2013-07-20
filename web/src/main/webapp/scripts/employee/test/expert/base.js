'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services) {
        if (!runCase('employee')) {
            return;
        }

        var it = mocha.it,
            user = mocha.user,
            httpProvider = angluarjs.httpProvider,
            ChiefService = services.ChiefService,
            ExpertService = services.ExpertService,
            OperatorService = services.OperatorService,
            ProfileService = services.ProfileService;
        
        // 登录
        var token
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

        // Expert
        it("the expert list should be retrieved", function(done) {
            expect(ExpertService).not.to.be(undefined);
            ExpertService.queryAll().then(function(experts) {
                expect(experts.length).not.to.be(0);
                done();
            }, function() {
                throw new Error('the expert list can\'t be retrieved');
            });
        });

        it("the expert should not be created without username and name", function(done) {
            expect(ExpertService.getPlainObject).not.to.be(undefined);
            var invalid = ExpertService.getPlainObject();
            ExpertService.create(invalid).then(function(flag) {
                if (flag) {
                    throw new Error('the expert can be created');
                } else {
                    done();
                }
            });
        });

        it("the expert should not be created without password", function(done) {
            var invalid = ExpertService.getPlainObject();
            invalid.name = 'test' + (new Date()).getTime();
            invalid.username = 'test' + (new Date()).getTime();
            invalid.password = '';
 
            ExpertService.create(invalid).then(function(flag) {
                if (flag) {
                    throw new Error('the expert can be created');
                } else {
                    done();
                }
            });
        });

        var expert;

        it("the expert should be created when username, name and password are set", function(done) {
            expert = ExpertService.getPlainObject();
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

        it("the expert should able to login in with default password", function(done) {
            expect(expert).not.to.be(undefined);
            $.ajax({
                url: '/api/auth',
                data: {
                    'username': expert.username,
                    'password': expert.password
                },
                type: 'POST',
                dataType: 'json'
            }).then(function(res) {
                done();
            }, function() {
                throw new Error('the expert can\'t login in');
            });
        });

        it("the expert id should be retrieved by ProfileService", function(done) {
            expect(expert).not.to.be(undefined);
            ProfileService.get(expert.username).then(function(employee) {
                if (employee) {
                    expect(employee.id).not.to.be(undefined);
                    expert = employee;
                    done();
                } else {
                    throw new Error('the expert id can\'t be created');
                }
            });
        });

        it("the expert should be updated as expectation", function(done) {
            expect(expert).not.to.be(undefined);
            ExpertService.get(expert.id).then(function(expert) {
                expert.status = 'ONLINE';
                expert.gender = 0;
                expert.birthday = '1983-01-21';
                expert.idCard = '440803198801122455';
                expert.title= '主任1';
                expert.mobile= '13800000000';
                expert.company = 'expert company';
                expert.enabled = false;
                expert.dismissed = true;
                delete expert.version;
                ExpertService.update(expert).then(function() {
                    ExpertService.get(expert.id).then(function(pesistedExpert) {
                        if (pesistedExpert) {
                            expect(pesistedExpert.id).to.be(expert.id);
                            expect(pesistedExpert.username).to.be(expert.username);
                            expect(pesistedExpert.name).to.be(expert.name);
                            expect(pesistedExpert.status).to.be(expert.status);
                            expect(pesistedExpert.title).to.be(expert.title);
                            expect(pesistedExpert.mobile).to.be(expert.mobile);
                            expect(pesistedExpert.birthday).to.be(expert.birthday);
                            expect(pesistedExpert.company).to.be(expert.company);
                            expect(pesistedExpert.enabled).to.be(expert.enabled);
                            expect(pesistedExpert.dismissed).to.be(expert.dismissed);
                            expect(pesistedExpert.roles).to.be(expert.roles);
                            done();
                        } else {
                            throw new Error('the expert can\'t be retieved again');
                        }
                    });
                }, function() {
                    throw new Error('the expert can\'t be updated by id');
                });
            }, function() {
                throw new Error('the expert can\'t be retrieved by id');
            });
        });

        it("the expert\' password should be reset", function(done) {
            expect(expert).not.to.be(undefined);
            ProfileService.resetPassword(expert.id)
            .then(function() {
                done();
            }, function() {
                throw new Error('the expert\'s password can\'t be reset.');
            });
        });

        it("the expert should able to login in with reset password", function(done) {
            expect(expert).not.to.be(undefined);
            $.ajax({
                url: '/api/auth',
                data: {
                    'username': expert.username,
                    'password': expert.username
                },
                type: 'POST',
                dataType: 'json'
            }).then(function(res) {
                done();
            }, function() {
                throw new Error('the expert can\'t login in');
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

    };

});