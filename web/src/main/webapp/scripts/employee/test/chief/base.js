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
            ProfileService = services.ProfileService;
        
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

        // Chief
        var firstId, secondId;
        it("the chief list should be retrieved", function(done) {
            expect(ChiefService).not.to.be(undefined);
            ChiefService.queryAll().then(function(chiefs) {
                expect(chiefs.length).not.to.be(0);
                expect(chiefs.length).not.to.be(1);
                firstId = chiefs[0].id;
                secondId = chiefs[1].id;
                done();
            }, function() {
                throw new Error('the chief list can\'t be retrieved');
            });
        });

        // Chief
        it("the chief list should be retrieved with only the first item", function(done) {
            expect(ChiefService).not.to.be(undefined);
            ChiefService.queryAll({'page.max': 1}).then(function(chiefs) {
                expect(chiefs.length).to.be(1);
                expect(chiefs[0].id).to.be(firstId);
                done();
            }, function() {
                throw new Error('the chief list can\'t be retrieved');
            });
        });

        // Chief
        it("the chief list should be retrieved with only the second items", function(done) {
            expect(ChiefService).not.to.be(undefined);
            ChiefService.queryAll({'page.curPage': 2, 'page.max': 1}).then(function(chiefs) {
                expect(chiefs.length).to.be(1);
                expect(chiefs[0].id).to.be(secondId);
                done();
            }, function() {
                throw new Error('the chief list can\'t be retrieved');
            });
        });

        it("the chief should not be created without username and name", function(done) {
            expect(ChiefService.getPlainObject).not.to.be(undefined);
            var invalid = ChiefService.getPlainObject();
            ChiefService.create(invalid).then(function(flag) {
                if (flag) {
                    throw new Error('the chief can be created');
                } else {
                    done();
                }
            });
        });

        it("the chief should not be created without password", function(done) {
            var invalid = ChiefService.getPlainObject();
            invalid.name = 'test' + (new Date()).getTime();
            invalid.username = 'test' + (new Date()).getTime();
            invalid.password = '';
 
            ChiefService.create(invalid).then(function(flag) {
                if (flag) {
                    throw new Error('the chief can be created');
                } else {
                    done();
                }
            });
        });

        
        var chief;
        it("the chief should be created when username, name and password are set", function(done) {
            chief = ChiefService.getPlainObject();
            chief.name = 'test' + (new Date()).getTime();
            chief.username = 'test' + (new Date()).getTime();
            chief.password = chief.name;
 
            ChiefService.create(chief).then(function(flag) {
                if (flag) {
                    done();
                } else {
                    throw new Error('the chief can\'t be created');
                }
            });
        });

        it("the chief should able to login in with default password", function(done) {
            $.ajax({
                url: PATH + '/api/auth',
                data: {
                    'username': chief.username,
                    'password': chief.password
                },
                type: 'POST',
                dataType: 'json'
            }).then(function(res) {
                done();
            }, function() {
                throw new Error('the chief can\'t login in');
            });
        });

        it("the chief should be retrieved by ProfileService", function(done) {
            ProfileService.get(chief.username).then(function(employee) {
                if (employee) {
                    expect(employee.id).not.to.be(undefined);
                    expect(employee.username).to.be(chief.username);
                    chief = employee;
                    done();
                } else {
                    throw new Error('the chief id can\'t be created');
                }
            });
        });

        it("the chief should be updated as expectation", function(done) {
            ChiefService.get(chief.id).then(function(chief) {
                chief.status = 'ONLINE';
                chief.gender = 0;
                chief.birthday = '1983-01-21';
                chief.idCard = '440803198801122455';
                chief.title= '主任1';
                chief.mobile= '13800000000';
                chief.company = 'chief company';
                chief.enabled = false;
                chief.dismissed = true;
                delete chief.version;
                ChiefService.update(chief).then(function() {
                    ChiefService.get(chief.id).then(function(pesistedChief) {
                        if (pesistedChief) {
                            expect(pesistedChief.id).to.be(chief.id);
                            expect(pesistedChief.username).to.be(chief.username);
                            expect(pesistedChief.name).to.be(chief.name);
                            expect(pesistedChief.status).to.be(chief.status);
                            expect(pesistedChief.title).to.be(chief.title);
                            expect(pesistedChief.mobile).to.be(chief.mobile);
                            expect(pesistedChief.birthday).to.be(chief.birthday);
                            expect(pesistedChief.company).to.be(chief.company);
                            expect(pesistedChief.enabled).to.be(chief.enabled);
                            expect(pesistedChief.dismissed).to.be(chief.dismissed);
                            expect(pesistedChief.roles).to.be(chief.roles);
                            done();
                        } else {
                            throw new Error('the chief can\'t be retieved again');
                        }
                    });
                }, function() {
                    throw new Error('the chief can\'t be updated by id');
                });
            }, function() {
                throw new Error('the chief can\'t be retrieved by id');
            });
        });

        it("the chief\' password should be reset", function(done) {
            ProfileService.resetPassword(chief.id)
            .then(function() {
                done();
            }, function() {
                throw new Error('the chief\'s password can\'t be reset.');
            });
        });

        it("the chief should able to login in with reset password", function(done) {
            $.ajax({
                url: PATH + '/api/auth',
                data: {
                    'username': chief.username,
                    'password': chief.username
                },
                type: 'POST',
                dataType: 'json'
            }).then(function(res) {
                done();
            }, function() {
                throw new Error('the chief can\'t login in');
            });
        });

        it("the chief should be removed", function(done) {
            ChiefService.remove(chief.id).then(function() {
                done();
            }, function() {
                throw new Error('the chief can\'t be removed');
            });
        });

    };

});