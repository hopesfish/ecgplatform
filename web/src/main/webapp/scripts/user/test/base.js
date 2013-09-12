'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services) {
        if (!runCase('user')) {
            return;
        }
        var it = mocha.it,
            employee = mocha.user,
            httpProvider = angluarjs.httpProvider,
            UserService = services.UserService;

        // 登录
        var token;
        it("the user should authenciated in user test module.", function(done) {
            $.ajax({
                url: PATH + '/api/auth',
                data: {
                    'username': employee.username,
                    'password': employee.password
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

        // User
        it("the user list should be retrieved", function(done) {
            expect(UserService).not.to.be(undefined);
            UserService.queryAll().then(function(users) {
                expect (users.length).not.to.be(0);
                done();
            }, function() {
                throw new Error('the user list can\'t be retrieved');
            });
        });

        it("the user should not be created without username and name", function(done) {
            expect(UserService.getPlainObject).not.to.be(undefined);
            var invalid = UserService.getPlainObject();
            UserService.create(invalid).then(function(flag) {
                if (flag) {
                    throw new Error('the user can be created');
                } else {
                    done();
                }
            });
        });

        it("the user should not be created without password", function(done) {
            var invalid = UserService.getPlainObject();
            invalid.mobile = mobile;
            invalid.name = 'user' + (new Date()).getTime();
            invalid.password = '';
 
            UserService.create(invalid).then(function(flag) {
                if (flag) {
                    throw new Error('the user can be created');
                } else {
                    done();
                }
            });
        });

        it("the user should not be created with invalid email", function(done) {
            var invalid = UserService.getPlainObject();
            invalid.mobile = mobile;
            invalid.name = 'user' + (new Date()).getTime();
            invalid.password = 'passw0rd';
            invalid.email = 'notaemailaddress';
 
            UserService.create(invalid).then(function(flag) {
                if (flag) {
                    throw new Error('the user can be created');
                } else {
                    done();
                }
            });
        });

        var user, mobile = 19 + (new Date()).getTime().toString().substring(0, 9);

        it("the user should be created when username, name and password are set", function(done) {
            user = UserService.getPlainObject();
            user.mobile = mobile;
            user.username = mobile;
            user.name = 'user' + (new Date()).getTime();
            user.password = user.mobile;
 
            UserService.create(user).then(function(flag) {
                if (flag) {
                    done();
                } else {
                    throw new Error('the user can\'t be created');
                }
            });
        });

        it("the user should be retrieved when moblie is given", function(done) {
            user.mobile = mobile;
 
            UserService.findAllByMobile(user.mobile).then(function(users) {
                if (users.length == 1) {
                    user.id = users[0].id;
                    done();
                } else {
                    throw new Error('the user can\'t be created');
                }
            });
        });

        it("the user should be updated as expectation", function(done) {
            UserService.get(user.id).then(function(user) {
                user.gender = 0;
                user.birthday = '1983-01-11';
                user.address = '地址';
                user.idCard = '440803198801122455';
                user.stature = 1.72;
                user.weight = 50;
                user.city = '北京';
                user.emContact1 = "联系人1",
                user.emContact1Tel = "11111",
                user.emContact2 = "联系人2",
                user.emContact2Tel = "22222",
                user.badHabits = "不良嗜好",
                user.anamnesis = "心脏病",
                user.isFree = false;
                user.gender = 1;
                user.married = 1;
                delete user.version;
                UserService.update(user).then(function() {
                    UserService.get(user.id).then(function(pesistedUser) {
                        if (pesistedUser) {
                            expect(pesistedUser.id).to.be(user.id);
                            expect(pesistedUser.username).to.be(user.username);
                            expect(pesistedUser.name).to.be(user.name);
                            expect(pesistedUser.status).to.be(user.status);
                            expect(pesistedUser.title).to.be(user.title);
                            expect(pesistedUser.mobile).to.be(user.mobile);
                            expect(pesistedUser.birthday).to.be(user.birthday);
                            expect(pesistedUser.company).to.be(user.company);
                            expect(pesistedUser.enabled).to.be(user.enabled);
                            expect(pesistedUser.dismissed).to.be(user.dismissed);
                            expect(pesistedUser.roles).to.be(user.roles);
                            expect(pesistedUser.gender).to.be(user.gender);
                            expect(pesistedUser.married).to.be(user.married);
                            done();
                        } else {
                            throw new Error('the user can\'t be retieved again');
                        }
                    });
                }, function() {
                    throw new Error('the user can\'t be updated by id');
                });
            }, function() {
                throw new Error('the user can\'t be retrieved by id');
            });
        });

        it("the user should be removed", function(done) {
            UserService.remove(user.id).then(function() {
                done();
            }, function() {
                throw new Error('the user can\'t be removed');
            });
        });
    };

});