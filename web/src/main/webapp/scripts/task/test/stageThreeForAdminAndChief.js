'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services, env) {

        var it = mocha.it,
            user = mocha.user,
            operator = mocha.operator,
            expert = mocha.expert,
            httpProvider = angluarjs.httpProvider,
            ProfileService = services.ProfileService,
            OperatorService = services.OperatorService;

        var token = null;

        // 登录
        it("the user should authenciated in task test module.", function(done) {
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

        it("the profile of the user should be retrieved", function(done) {
            ProfileService.get(user.username)
            .then(function(persistedUser) {
                expect(persistedUser).not.to.be(null);
                expect(persistedUser.username).to.be(user.username);
                user = persistedUser;
                done();
            }, function() {
                throw new Error('failed to retrieved the profile');
            });
        });

        var operator, expert;
        it("the operator should be retrieved", function(done) {
            ProfileService.get(operator.username)
            .then(function(persistedUser) {
                expect(persistedUser).not.to.be(null);
                expect(persistedUser.username).to.be(operator.username);
                operator = persistedUser;
                done();
            }, function() {
                throw new Error('failed to retrieved the profile');
            });
        });

        it("the expert should be retrieved", function(done) {
            ProfileService.get(expert.username)
            .then(function(persistedUser) {
                expect(persistedUser).not.to.be(null);
                expect(persistedUser.username).to.be(expert.username);
                expert = persistedUser;
                done();
            }, function() {
                throw new Error('failed to retrieved the profile');
            });
        });
        
        it("the operator and the expert should be linked", function(done) {
            OperatorService.linkExpert(operator, expert)
            .then(function() {
                done();
            }, function() {
                throw new Error('failed to linked');
            });
        });
    };

});