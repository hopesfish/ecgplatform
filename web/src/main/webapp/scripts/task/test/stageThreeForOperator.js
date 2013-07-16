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
        it("the operator should authenciated in task test module.", function(done) {
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

        it("the profile of the opearator should be retrieved", function(done) {
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

        var task, undones;
        it("the undone task list for operator should be retrieved", function(done) {
            TaskService.queryAllTaskByEmployee(
                user, 
                {status: 'undone'}
            ).then(function(tasks) {
                expect(tasks).not.to.be(null);
                expect(tasks.length).not.to.be(0);
                task = tasks[0];
                undones = tasks.length;
                expect(task.userId).not.to.be(null);
                expect(task.examinationId).not.to.be(null);
                done();
            }, function() {
                throw new Error('failed to retrieved the list');
            });
        });

        var examination;
        it("the examination of a specific undone task should be retrieved", function(done) {
            expect(task).not.to.be(undefined);
            TaskService.getExamination(task.id)
            .then(function(pesistedExamination) {
                expect(pesistedExamination).not.to.be(undefined);
                expect(pesistedExamination).not.to.be(null);
                examination = pesistedExamination;
                done();
            });
        });

        var replys;
        it("two replies of the examination be created", function(done) {
            expect(task).not.to.be(undefined);
            expect(examination).not.to.be(undefined);

            var len = 0, count = 0;
            replys = []
            replys.push({result: "结果4", content: "建议4"});
            replys.push({result: "结果5", content: "建议5"});
            len = replys.length;

            $(replys).each(function(i, reply) {
                TaskService.reply(examination, reply)
                .then(function(flag) {
                    if (flag) {
                        count++;
                    }
                    if (count === len) {
                        done();
                    }
                }, function() {
                    throw new Error("failed to create a reply for a specific examination.");
                });
            });
        });

        it("the task cloud be forwarded", function(done) {
            TaskService.forward(task)
            .then(function() {
                done();
            }, function() {
                throw new Error("failed to forward.");
            });
        });

        var task;
        it("the undone task count should be decreased with 1", function(done) {
            TaskService.queryAllTaskByEmployee(
                user, 
                {status: 'undone'}
            ).then(function(tasks) {
                expect(tasks).not.to.be(null);
                expect(tasks.length).to.be(undones - 1);
                done();
            }, function() {
                throw new Error("failed to retrieved list again.");
            });
        });

    };

});