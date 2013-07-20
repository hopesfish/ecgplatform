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
        it("stage 3 for expert:the expert should authenciated in task test module.", function(done) {
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

        it("stage 3 for expert:the profile of the expert should be retrieved", function(done) {
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
        it("stage 3 for expert:the undone task list for expert should be retrieved", function(done) {
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
        it("stage 3 for expert:the examination for expert should be retrieved", function(done) {
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

        var replys, before;

        it("stage 3 for expert:the replys of the examination for the expert should be greater than 0", function(done) {
            expect(examination).not.to.be(undefined);
            TaskService.getReplyByExamination(examination)
            .then(function(lastest) {
                expect(lastest).not.to.be(undefined);
                expect(lastest.length).not.to.be(0);
                expect(lastest.length).not.to.be(1);
                replys = lastest;
                before = lastest.length;
                done();
            }, function() {
                throw new Error('failed to retrieved the replys');
            });
        });
        
        it("stage 3 for expert: expert can remove, update and create the reply", function(done) {

            expect(examination).not.to.be(undefined);
            expect(replys.length).not.to.be(0);
            expect(replys.length).not.to.be(1);

            replys[0].removed = true;

            replys[1].result += 'updated';
            replys[1].reason += 'updated';

            replys.push({result: "专家建议结果test", content: "专家建议内容", reason: "原因", level: "专家建议级别"})

            TaskService.replyInBatch(examination, replys)
            .then(function(flag) {
                expect(flag).to.be(true);
                done();
            }, function() {
                throw new Error('failed to updated the replys by expert.');
            });
        });

        it("stage 3 for expert: the replys are updated as expectation", function(done) {
            var found = false;
            expect(examination).not.to.be(undefined);
            TaskService.getReplyByExamination(examination)
            .then(function(lastest) {
                expect(lastest).not.to.be(undefined);
                expect(lastest.length).to.be(before);
                $(lastest).each(function(i, reply) {
                    if (reply.result === "专家建议结果test") {
                        found = true;
                    }
                });
                expect(found).to.be(true);
                done();
            }, function() {
                throw new Error('failed to retrieved the replys');
            });
        });

        it("stage 3 for expert: the expert can set it completed", function(done) {
            expect(examination).not.to.be(undefined);
            TaskService.complete(task)
            .then(function(flag) {
                if (flag) {
                    done();
                } else {
                    throw new Error('failed to set it completed.');
                }
            }, function() {
                throw new Error('failed to set it completed.');
            });
        });
    };

});