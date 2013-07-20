'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services) {
        if (!runCase('user')) {
            return;
        }
        var it = mocha.it,
            httpProvider = angluarjs.httpProvider,
            UserService = services.UserService;

        // 登录
        var user;
        it("the user should be created by user/apk.", function(done) {
            user = {
                mobile: 13 + (new Date()).getTime().toString().substring(0, 9),
                name: '测试用户',
                password: 'passw0rd'
            };
            $.ajax({
                url: PATH + '/api/user',
                data: {
                    'mobile': user.mobile,
                    'name': user.name,
                    'password': user.password
                },
                type: 'POST'
            }).then(function() {
                if (arguments[2].status === 201) {
                    done();
                } else {
                    throw new Error('failed to get a 201 status code.');
                }
            }, function() {
                throw new Error('failed to create a user');
            });
        });

        it("the user should not be created by user/apk.", function(done) {
            $.ajax({
                url: PATH + '/api/user',
                data: {
                    'mobile': user.mobile,
                    'name': user.name,
                    'password': user.password
                },
                type: 'POST'
            }).then(function(res) {
                throw new Error('should not created a user');
            }, function() {
                done();
            });
        });

        // 登录
        var token;
        it("the new user should authenciated.", function(done) {
            $.ajax({
                url: PATH + '/api/user/auth',
                data: {
                    'username': user.mobile,
                    'password': user.password
                },
                type: 'POST',
                dataType: 'json'
            }).then(function(res) {
                expect(res.token).not.to.be(undefined);
                token = res.token;
                done();
            }, function() {
                throw new Error('failed to authnenciate as a new user.');
            });
        });

        // 没有token
        it("the user's info should not retrieved without token.", function(done) {
            $.ajax({
                url: PATH + '/api/user?mobile=' + user.mobile,
                dataType: 'json'
            }).then(function(res) {
                throw new Error('should not retrieved without token.');
            }, function() {
                done()
            });
        });

        // 错的token
        it("the user's info should not retrieved with empty token.", function(done) {
            $.ajax({
                url: PATH + '/api/user?mobile=' + user.mobile,
                dataType: 'json',
                header: {Authorization: 'invalid'}
            }).then(function(res) {
                throw new Error('should not retrieved with invalid token.');
            }, function() {
                done()
            });
        });

        // 获取id
        var userId;
        it("the user's info should not retrieved with valid token.", function(done) {
            $.ajax({
                url: PATH + '/api/user?username=' + user.mobile,
                dataType: 'json',
                header: {Authorization: token}
            }).then(function(res) {
                expect(res).not.to.be(undefined);
                expect(res.datas).not.to.be(undefined);
                expect(res.datas.length).to.be(1);
                userId = res.datas[0].id;
                expect(userId).not.to.be(undefined);
                done();
            }, function() {
                throw new Error('failed to authnenciate as a new user.');
            });
        });

        // 没有token
        it("the user's info should not retrieved without empty token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId,
                dataType: 'json'
            }).then(function(res) {
                throw new Error('should not retrieved without token.');
            }, function() {
                done()
            });
        });

        // 错的token
        it("the user's info should not retrieved with invalid token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId,
                dataType: 'json',
                header: {Authorization: 'invalid'}
            }).then(function(res) {
                throw new Error('should not retrieved with invalid token.');
            }, function() {
                done()
            });
        });

        // 获取个人资料
        it("the user's info should retrieved with valid token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId,
                dataType: 'json',
                header: {Authorization: token}
            }).then(function(res) {
                done()
            }, function() {
                throw new Error('should not retrieved with invalid token.');
            });
        });

        // 没有token不能修改个人资料
        it("the user's info should not updated without empty token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId,
                type: 'PUT',
                data: user
            }).then(function(res) {
                throw new Error('should not updated without token.');
            }, function() {
                done()
            });
        });

        // 错的token不能修改个人资料
        it("the user's info should not updated with invalid token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId,
                type: 'PUT',
                data: user,
                header: {Authorization: 'invalid'}
            }).then(function(res) {
                throw new Error('should not updated with invalid token.');
            }, function() {
                done()
            });
        });

        // 修改个人资料
        it("the user's info should updated with valid token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId,
                type: 'PUT',
                data: user,
                header: {Authorization: token}
            }).then(function(res) {
                done()
            }, function() {
                throw new Error('failed to update.');
            });
        });

        // 没有token不能修改密码
        it("the user's password should not updated without empty token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId + '/password',
                type: 'PUT',
                data: {oldPassword: user.password, newPassword: user.password},
            }).then(function(res) {
                throw new Error('should not updated without token.');
            }, function() {
                done()
            });
        });

        // 错的token不能修改密码
        it("the user's password should not updated with invalid token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId + '/password',
                type: 'PUT',
                data: {oldPassword: user.password, newPassword: user.password},
                header: {Authorization: 'invalid'}
            }).then(function(res) {
                throw new Error('should not updated with invalid token.');
            }, function() {
                done()
            });
        });

        // 可以修改个人密码
        it("the user's password should updated with valid token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId + '/password',
                type: 'PUT',
                data: {oldPassword: user.password, newPassword: user.password},
                header: {Authorization: token}
            }).then(function(res) {
                done()
            }, function() {
                throw new Error('failed to update.');
            });
        });

        // 没有token不能获取任务信息
        it("the user's task should not be retrieved without empty token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId + '/task',
                type: 'GET'
            }).then(function(res) {
                throw new Error('should not retrieved task without token.');
            }, function() {
                done()
            });
        });

        // 错的token不能修改密码
        it("the user's task should not retrieved with invalid token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId + '/task',
                type: 'GET',
                header: {Authorization: 'invalid'}
            }).then(function(res) {
                throw new Error('should not retrieved task with invalid token.');
            }, function() {
                done()
            });
        });

        // 获取个人任务
        it("the user's task should retrieved with valid token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId + '/task',
                type: 'GET',
                header: {Authorization: token}
            }).then(function(res) {
                done()
            }, function() {
                throw new Error('failed to update.');
            });
        });

        // 没有token不能删除
        it("the user's password should not deleted without empty token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId,
                type: 'DELETE'
            }).then(function(res) {
                throw new Error('should not deleted without token.');
            }, function() {
                done()
            });
        });

        // 错的token不能删除
        it("the user's password should not deleted with invalid token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId + '/password',
                type: 'DELETE',
                header: {Authorization: 'invalid'}
            }).then(function(res) {
                throw new Error('should not deleted with invalid token.');
            }, function() {
                done()
            });
        });

        // 自己也不能删除自己
        it("the user's password should deleted with valid token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId + '/password',
                type: 'DELETE',
                header: {Authorization: token}
            }).then(function(res) {
                throw new Error('should to deleted by myself.');
            }, function() {
                done();
            });
        });
    };

});