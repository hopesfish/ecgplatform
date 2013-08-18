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

        // 访问统计接口  近7天数据
        it("the statisticses data can be retrieved.", function(done) {
            expect(userId).not.to.be(null);
            $.ajax({
                url: PATH + '/api/examination/avg?userId=' + userId,
                dataType: 'json',
                headers: {Authorization: token}
            }).then(function(statisticses) {
                expect(statisticses).not.to.be(undefined);
                done();
            }, function() {
                throw new Error('not able to retrieved the statistics data.');
            });
        });

        // 访问统计接口 特定时间段数据
        it("the statisticses data can be retrieved.", function(done) {
            expect(userId).not.to.be(null);
            $.ajax({
                url: PATH + '/api/examination/avg?userId=' + userId + '&start=2013-05-01',
                dataType: 'json',
                headers: {Authorization: token}
            }).then(function(statisticses) {
                expect(statisticses).not.to.be(undefined);
                done();
            }, function() {
                throw new Error('not able to retrieved the statistics data.');
            });
        });

        // 访问统计接口 特定时间段数据
        it("the statisticses data can be retrieved.", function(done) {
            expect(userId).not.to.be(null);
            $.ajax({
                url: PATH + '/api/examination/avg?userId=' + userId + '&start=2013-05-01&end=2013-08-01',
                dataType: 'json',
                headers: {Authorization: token}
            }).then(function(statisticses) {
                expect(statisticses).not.to.be(undefined);
                done();
            }, function() {
                throw new Error('not able to retrieved the statistics data.');
            });
        });
    };

});