'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services) {
        if (!runCaseAs('user')) {
            return;
        }

        var it = mocha.it,
            user = mocha.user;

        var token = null, userId = null;

        // 登录
        it("the user should authenciated in rule test module.", function(done) {
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
                userId = res.userId;
                token = res.token;
                done();
            }, function() {
                throw new Error('failed to authnenciate as admin in rule test module');
            });
        });

        /**
         * 查看充值历史
         */
        // 没有token看不到历史
        it("the user's charge history should not retrieved without empty token.", function(done) {
            expect(userId).not.to.be(null);
            $.ajax({
                url: PATH + '/api/user/' + userId + "/card",
                dataType: 'json'
            }).then(function(res) {
                throw new Error('should not retrieved without token.');
            }, function() {
                done();
            });
        });

        // 错的token看不到历史
        it("the user's charge history should not retrieved with invalid token.", function(done) {
            expect(userId).not.to.be(null);
            $.ajax({
                url: PATH + '/api/user/' + userId + "/card",
                dataType: 'json',
                headers: {Authorization: 'invalid'}
            }).then(function(res) {
                throw new Error('should not retrieved with invalid token.');
            }, function() {
                done();
            });
        });

        // 获取充值历史
        var usedcard;
        it("the user's charge history should retrieved with valid token.", function(done) {
            expect(userId).not.to.be(null);
            $.ajax({
                url: PATH + '/api/user/' + userId + "/card",
                dataType: 'json',
                headers: {Authorization: token}
            }).then(function(res) {
                expect(res).not.to.be(undefined);
                expect(res.datas).not.to.be(undefined);
                expect(res.datas.length).not.to.be(0);
                usedcard = res.datas[0];
                done();
            }, function() {
                throw new Error('should not retrieved with invalid token.');
            });
        });

        /**
         * 用户查看卡历史
         */ 
        // 没有token看不到历史
        it("the user's charged card should not retrieved without empty token.", function(done) {
            expect(userId).not.to.be(null);
            $.ajax({
                url: PATH + '/api/user/' + userId + "/card",
                dataType: 'json'
            }).then(function(res) {
                throw new Error('should not retrieved without token.');
            }, function() {
                done();
            });
        });

        // 错的token看不到历史
        it("the user's charged card should not retrieved with invalid token.", function(done) {
            expect(userId).not.to.be(null);
            $.ajax({
                url: PATH + '/api/user/' + userId + "/card",
                dataType: 'json',
                headers: {Authorization: 'invalid'}
            }).then(function(res) {
                throw new Error('should not retrieved with invalid token.');
            }, function() {
                done();
            });
        });
        // 获得充值卡具体信息
        it("the charged card can be retrieved.", function(done) {
            expect(userId).not.to.be(null);
            expect(usedcard).not.to.be(undefined);
            $.ajax({
                url: PATH + '/api/card/' + usedcard.serial,
                dataType: 'json',
                headers: {Authorization: token}
            }).then(function(card) {
                expect(card).not.to.be(undefined);
                expect(card.userId + '').to.be(userId);
                done();
            }, function() {
                throw new Error('should not retrieved with invalid token.');
            });
        });

        /*
         * 充值新卡测试用例
         */
        var unusedcard = {serial: "0067890200312293", password: ""};
        
        // 没有token不能充值
        it("the unused card can not be used without token.", function(done) {
            expect(userId).not.to.be(null);
            expect(usedcard).not.to.be(undefined);
            unusedcard.activedDate = "2014-08-16";
            $.ajax({
                url: PATH + '/api/user/' + userId + "/charge",
                type: 'POST',
                data: unusedcard
            }).then(function(card) {
                throw new Error('should not retrieved with invalid token.');
            }, function() {
                done();
            });
        });

        // 错误token不能充值
        it("the unused card can not be used with invalid token.", function(done) {
            expect(userId).not.to.be(null);
            expect(usedcard).not.to.be(undefined);
            unusedcard.activedDate = "2014-08-16";
            $.ajax({
                url: PATH + '/api/user/' + userId + "/charge",
                type: 'POST',
                data: unusedcard,
                headers: {Authorization: 'invalid'}
            }).then(function(card) {
                
                throw new Error('should not retrieved with invalid token.');
            }, function() {
                done();
            });
        });

        // 空密码不能充值
        it("the used card can be used without password.", function(done) {
            expect(userId).not.to.be(null);
            expect(usedcard).not.to.be(undefined);
            unusedcard.activedDate = "2014-08-16";
            $.ajax({
                url: PATH + '/api/user/' + userId + "/charge",
                type: 'POST',
                data: unusedcard,
                headers: {Authorization: token}
            }).then(function(card) {
                throw new Error('should not retrieved with invalid token.');
            }, function() {
                done();
            });
        });

        // 错密码不能充值
        it("the used card can be used with wrong password.", function(done) {
            expect(userId).not.to.be(null);
            expect(usedcard).not.to.be(undefined);
            unusedcard.password = "777777";
            unusedcard.activedDate = "2014-08-16";
            $.ajax({
                url: PATH + '/api/user/' + userId + "/charge",
                type: 'POST',
                data: unusedcard,
                headers: {Authorization: token}
            }).then(function(card) {
                throw new Error('should not retrieved with invalid token.');
            }, function() {
                done();
            });
        });

        // 错误时间不能充值 因为员工已经帮他充值 从2013-01-12/2014-01-12
        it("the used card can not be used with wrong activedDate.", function(done) {
            expect(userId).not.to.be(null);
            expect(usedcard).not.to.be(undefined);
            unusedcard.password = "888888";
            unusedcard.activedDate = "2013-09-16";
            $.ajax({
                url: PATH + '/api/user/' + userId + "/charge",
                type: 'POST',
                data: unusedcard,
                headers: {Authorization: token}
            }).then(function(card) {
                throw new Error('should not retrieved with invalid token.');
            }, function() {
                done();
            });
        });

        // 错误时间不能充值 因为员工已经帮他充值 从2013-08-15/2014-08-15
        it("the used card can not be used with wrong activedDate.", function(done) {
            expect(userId).not.to.be(null);
            expect(usedcard).not.to.be(undefined);
            unusedcard.password = "888888";
            unusedcard.activedDate = "2013-08-01";
            $.ajax({
                url: PATH + '/api/user/' + userId + "/charge",
                type: 'POST',
                data: unusedcard,
                headers: {Authorization: token}
            }).then(function(card) {
                throw new Error('should not retrieved with invalid token.');
            }, function() {
                done();
            });
        });

        // 正确时间才能充值
        it("the used card can be used.", function(done) {
            expect(userId).not.to.be(null);
            expect(usedcard).not.to.be(undefined);
            unusedcard.activedDate = "2014-08-16";
            $.ajax({
                url: PATH + '/api/user/' + userId + "/charge",
                type: 'POST',
                data: unusedcard,
                headers: {Authorization: token}
            }).then(function(card) {
                done();
            }, function() {
                throw new Error('should not retrieved with invalid token.');
            });
        });

        // 不能充两次
        it("the used card can not be used again.", function(done) {
            expect(userId).not.to.be(null);
            expect(usedcard).not.to.be(undefined);
            unusedcard.activedDate = "2014-08-16";
            $.ajax({
                url: PATH + '/api/user/' + userId + "/charge",
                type: 'POST',
                data: unusedcard,
                headers: {Authorization: token}
            }).then(function(card) {
                throw new Error('should not retrieved with invalid token.');
            }, function() {
                done();
            });
        });
    };

});