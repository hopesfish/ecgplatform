'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services) {
        if (!runCase('profile')) {
            return;
        }

        var it = mocha.it,
            user = mocha.user,
            httpProvider = angluarjs.httpProvider,
            ProfileService = services.ProfileService;

        var token;
        // 登录
        it("the user should authenciated in employee/profile test module.", function(done) {
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

        // profile
        var sessionemployee;
        it("the session employee's profile should be retrieved.", function(done) {
            expect(ProfileService).not.to.be(undefined);
            ProfileService.get(user.username).then(function(profile) {
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

        it("the session employee's profile should be updated.", function(done) {
            expect(ProfileService).not.to.be(undefined);
            expect(sessionemployee).not.to.be(undefined);
            sessionemployee.gender = 0;
            // updated
            ProfileService.update(sessionemployee).then(function() {
                done();
            }, function() {
                throw new Error('the profile can\'t be updated');
            });
        });

        
        it("the session employee's password should be updated", function(done) {
            expect(sessionemployee).not.to.be(undefined);
            // updated
            ProfileService.updatePassword(sessionemployee.id, user.password, user.password + 'updated')
            .then(function(token) {
                expect(token).not.to.be(undefined);
                httpProvider.defaults.headers.common['Authorization'] = token;
                done();
            }, function() {
                throw new Error('the session employee\'s password can\'t be updated');
            });
        });

        it("the session employee's password should be useful", function(done) {
            expect(sessionemployee).not.to.be(undefined);
            $.ajax({
                url: PATH + '/api/auth',
                data: {
                    'username': user.username,
                    'password': user.password + 'updated'
                },
                type: 'POST',
                dataType: 'json'
            }).then(function(res) {
                // expect token
                token = res.token;
                expect(token).not.to.be(undefined);
                httpProvider.defaults.headers.common['Authorization'] = token;
                done();
            }, function() {
                throw new Error('the session employee can\'t login in with new password.');
            });
        });

        
        it("the session employee's password should be rollback", function(done) {
            expect(sessionemployee).not.to.be(undefined);
            // updated
            ProfileService.updatePassword(sessionemployee.id, user.password + 'updated', user.password)
            .then(function(token) {
                expect(token).not.to.be(undefined);
                httpProvider.defaults.headers.common['Authorization'] = token;
                done();
            }, function() {
                throw new Error('the session employee\'s password can\'t be rollback');
            });
        });

        it("the session employee is heartbeating", function(done) {
            expect(sessionemployee).not.to.be(undefined);
            ProfileService.heartbeat(sessionemployee)
            .then(function() {
                done();
            }, function() {
                throw new Error('the session employee is not heartbeating.');
            });
        });

    };

});