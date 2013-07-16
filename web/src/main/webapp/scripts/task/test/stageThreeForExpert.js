'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services, env) {

        var it = mocha.it,
            user = mocha.user,
            httpProvider = angluarjs.httpProvider,
            ProfileService = services.ProfileService,
            TaskService = services.TaskService;

        var token = null;

        // 登录
        it("the expert should authenciated in task test module.", function(done) {
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

        it("the profile of the expert should be retrieved", function(done) {
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

        var task;
        it("the undone task list for expert should be retrieved", function(done) {
            TaskService.queryAllTaskByEmployee(
                user, 
                {status: 'undone'}
            ).then(function(tasks) {
                expect(tasks).not.to.be(null);
                expect(tasks.length).not.to.be(0);
                task = tasks[0];
                expect(task.userId).not.to.be(null);
                expect(task.examinationId).not.to.be(null);
                done();
            }, function() {
                throw new Error('failed to retrieved the undone task list.');
            });
        });

        var examination;
        it("the examination for expert should be retrieved", function(done) {
            expect(task).not.to.be(undefined);
            TaskService.getExamination(task.examinationId)
            .then(function(pesistedExamination) {
                expect(examination).not.to.be(null);
                examination = pesistedExamination;
                done();
            }, function() {
                throw new Error('failed to retrieved the examination');
            });
        });

        var replys;
        it("the replys of the examination for the expert should be greater than 0", function(done) {
            expect(examination).not.to.be(undefined);
            TaskService.getReplyByExamination(examination)
            .then(function(replys) {
                expect(replys).not.to.be(undefined);
                expect(replys).not.to.be(0);
                done();
            }, function() {
                throw new Error('failed to retrieved the replys');
            });
        });
    };

});