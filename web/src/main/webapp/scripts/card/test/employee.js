'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services) {
        if (!runCaseAs('admin') && !runCaseAs('chief') && !runCaseAs("expert") && !runCaseAs("operator")) {
            return;
        }

        var it = mocha.it,
            employee = mocha.employee,
            user = mocha.user,
            httpProvider = angluarjs.httpProvider,
            ProfileService = services.ProfileService,
            CardService = services.CardService,
            UserService = services.UserService;

 
        var token = null;

        // 登录
        it("the employee should authenciated in card test module.", function(done) {
            $.ajax({
                url: PATH + '/api/auth',
                data: {
                    'username': employee.username,
                    'password': employee.password
                },
                type: 'POST',
                dataType: 'json'
            }).then(function(res) {
                token = res.token;
                expect(token).not.to.be(undefined);
                httpProvider.defaults.headers.common['Authorization'] = token;
                done();
            }, function() {
                throw new Error('failed to authnenciate as admin in card test module');
            });
        });

        // 获得员工id
        var sessionemployee;
        it("the employee's profile should be retrieved.", function(done) {
            expect(ProfileService).not.to.be(undefined);
            ProfileService.get(employee.username).then(function(profile) {
                if (profile) {
                    // retrieved
                    expect(profile).not.to.be(undefined);
                    sessionemployee = profile;
                    done();
                } else {
                    throw new Error('the profile can\'t be retrieved');
                }
            });
        });

        // 获得充值历史
        var usedcard, usedcount = 0;
        it("the charge history can be retrieved.", function(done) {
            expect(CardService).not.to.be(undefined);
            CardService.queryAll().then(function(paging) {
                var cards = paging.datas;
                expect(cards).not.to.be(undefined);
                expect(cards.length).not.to.be(0);
                usedcount = cards.length;
                usedcard = cards[0];
                done();
            }, function() {
                throw new Error('the charge history can\'t be retrieved');
            });
        });

        // 查看已经使用卡的信息
        it("the used card can be retrieved.", function(done) {
            expect(usedcard).not.to.be(undefined);
            CardService.get(usedcard).then(function(card) {
                expect(card).not.to.be(undefined);
                expect(card.serial).to.be(usedcard.serial);
                done();
            }, function() {
                throw new Error('the charge history can\'t be retrieved');
            });
        });

        // 查看尚未使用的卡信息
        var unused = {serial: "0033178902311110", password: ""};
        it("the unused card can be retrieved.", function(done) {
            expect(unused).not.to.be(undefined);
            CardService.get(unused).then(function(card) {
                expect(card).not.to.be(undefined);
                expect(card.serial).to.be(unused.serial);
                expect(card.userId).to.be(null);
                expect(card.activedDate).to.be(null);
                done();
            }, function() {
                throw new Error('the unused card can\'t be retrieved');
            });
        });

        // 没密码不能充值
        it("the unused card can not be used without password.", function(done) {
            expect(unused).not.to.be(undefined);
            unused.activedDate = '2013-08-15';
            CardService.charge(sessionemployee, user, unused).then(function() {
                throw new Error('the unused card can\'t be charged');
            }, function() {
                done();
            });
        });

        // 错密码不能充值
        it("the unused card can not be used with wrong password.", function(done) {
            expect(unused).not.to.be(undefined);
            unused.password = '777777';
            unused.activedDate = '2013-08-15';
            CardService.charge(sessionemployee, user, unused).then(function() {
                throw new Error('the unused card can\'t be charged');
            }, function() {
                done();
            });
        });

        // 空时间不能充值
        it("the unused card can not be used without activedDate.", function(done) {
            expect(unused).not.to.be(undefined);
            unused.password = '8888888';
            unused.activedDate = '';
            CardService.charge(sessionemployee, user, unused).then(function() {
                throw new Error('the unused card can\'t be charged');
            }, function() {
                done();
            });
        });

        // 以前的时间不能充值
        it("the unused card can not be used with wrong activedDate.", function(done) {
            expect(unused).not.to.be(undefined);
            unused.password = '8888888';
            unused.activedDate = '2012-08-15';
            CardService.charge(sessionemployee, user, unused).then(function() {
                throw new Error('the unused card can\'t be charged');
            }, function() {
                done();
            });
        });

        // 为用户充值
        it("the unused card can be used.", function(done) {
            expect(unused).not.to.be(undefined);
            unused.password = '888888';
            unused.activedDate = '2024-08-15';
            CardService.charge(sessionemployee, user, unused).then(function() {
                done();
            }, function() {
                throw new Error('the unused card can\'t be charged');
            });
        });

        // 不能再次用户充值
        it("the unused card can not be used again.", function(done) {
            expect(unused).not.to.be(undefined);
            unused.password = '888888';
            unused.activedDate = '2024-08-15';
            CardService.charge(sessionemployee, user, unused).then(function() {
                throw new Error('the unused card can\'t be charged');
            }, function() {
                done();
            });
        });

        // 被使用卡的信息应该能找到
        it("the used card can be found.", function(done) {
            var found = false;
            CardService.queryAll().then(function(paging) {
                var cards = paging.datas;
                $(cards).each(function(i, card) {
                    if (card.serial == unused.serial) {
                        found = true;
                    }
                });
                expect(usedcount + 1).to.be(cards.length);
                expect(found).to.be(true);
                done();
            }, function() {
                throw new Error('the charge history can\'t be retrieved');
            });
        });

    };

});