'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services, allowed) {
        if (!runCase('task')) {
            return;
        }
        var it = mocha.it,
            employee = mocha.employee,
            user = mocha.user,
            httpProvider = angluarjs.httpProvider,
            TaskService = services.TaskService,
            UserService = services.UserService,
            ProfileService = services.ProfileService;

        // 获取token
        var token;
        it("the employee should authenciated in rule test module.", function(done) {
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

        // profile
        var sessionemployee;
        it("the session employee's profile should be retrieved.", function(done) {
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

        var user;
        it("the user should be retrieved.", function(done) {
            expect(UserService).not.to.be(undefined);
            UserService.queryAll({username: user.username}).then(function(paging) {
                var users = paging.datas;
                expect(users).not.to.be(undefined);
                expect(users.length).to.be(1);
                user = users[0];
                done();
            });
        });

        it("all task should be deleted by admin/chief.", function(done) {
            expect(TaskService).not.to.be(undefined);
            expect(user).not.to.be(undefined);
            TaskService.removeUserTasks({userId: user.id}).then(function() {
                done();
            }, function() {
                throw new Error('all task can not be deleted');
            });
        });
    };

});