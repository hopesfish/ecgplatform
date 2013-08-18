'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services) {
        if (!runCase('user')) {
            return;
        }
        var it = mocha.it,
            user = mocha.user,
            httpProvider = angluarjs.httpProvider;

        // 登录
        var token;
        it("the user should be authenciated.", function(done) {
            $.ajax({
                url: PATH + '/api/user/auth',
                data: {
                    'username': user.username,
                    'password': user.password
                },
                type: 'POST',
                dataType: 'json'
            }).then(function(res) {
                expect(res.userId).not.to.be(undefined);
                expect(res.token).not.to.be(undefined);
                token = res.token;
                done();
            }, function() {
                throw new Error('failed to authnenciate as a new user.');
            });
        });

        // 发送邮件提示
        it("the retake email should be sent.", function(done) {
            $.ajax({
                url: PATH + '/api/user/password/retake?email=' + user.email,
                dataType: 'json'
            }).then(function(res) {
                done();
            }, function() {
                throw new Error('failed to send retake email.');
            });
        });

        if (window.location.href.indexOf("sms") > -1) {
            // 发送邮件提示
            it("the retake sms should be sent.", function(done) {
                $.ajax({
                    url: PATH + '/api/user/password/retake?mobile=' + user.username,
                    dataType: 'json'
                }).then(function(res) {
                    done();
                }, function() {
                    throw new Error('failed to send retake sms.');
                });
            });
        }
        
    };

});