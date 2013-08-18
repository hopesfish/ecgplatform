'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services) {
        if (!runCase('user')) {
            return;
        }
        var it = mocha.it,
            httpProvider = angluarjs.httpProvider,
            UserService = services.UserService;

        var user, hacker;
        // 注册用户
        it("the user should be created by user/apk.", function(done) {
            user = {
                mobile: 17 + (new Date()).getTime().toString().substring(0, 9),
                name: '测试用户',
                password: 'passw0rd'
            };
            $.ajax({
                url: PATH + '/api/user',
                data: {
                    'mobile': user.mobile,
                    'name': user.name,
                    'password': user.password,
                    'email': user.mobile + '@test.com'
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

        // 登录
        var userId, token;
        it("the new user should be authenciated.", function(done) {
            $.ajax({
                url: PATH + '/api/user/auth',
                data: {
                    'username': user.mobile,
                    'password': user.password
                },
                type: 'POST',
                dataType: 'json'
            }).then(function(res) {
                expect(res.userId).not.to.be(undefined);
                expect(res.token).not.to.be(undefined);
                userId = res.userId;
                token = res.token;
                done();
            }, function() {
                throw new Error('failed to authnenciate as a new user.');
            });
        });

        // 没有token不能创建请求
        it("the test examinations should not be created without token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/{userId}/examination',
                data: {
                    'apkId': "1"
                },
                type: 'POST'
            }).then(function(res) {
                throw new Error('should not created test examination.');
            }, function() {
                done();
            });
        });

        // 错误token也不能创建请求
        it("the test examinations should not be created with invalid token.", function(done) {
            $.ajax({
                url: PATH + '/api/user/{userId}/examination',
                data: {
                    'apkId': "1"
                },
                type: 'POST',
                headers: {Authorization: 'invalid'}
            }).then(function(res) {
                throw new Error('should not created test examination.');
            }, function() {
                done();
            });
        });

        // 正确的token,没有文件的话 也不能创建测试例子
        it("the test examinations should not be created with invalid token but without file.", function(done) {
            $.ajax({
                url: PATH + '/api/user/{userId}/examination',
                data: {
                    'apkId': "1"
                },
                type: 'POST',
                headers: {Authorization: token}
            }).then(function(res) {
                throw new Error('should not created test examination.');
            }, function() {
                done();
            });
        });

        return;

        // 正确的token,没有文件的话, 能创建测试例子
        it("the test examinations should be created with invalid token but without file.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId + '/examination',
                data: {
                    'apkId': "1",
                    'isTest': true
                },
                type: 'POST',
                headers: {Authorization: token}
            }).then(function(res) {
                done();
            }, function() {
                throw new Error('should not created test examination.');
            });
        });

    };

});