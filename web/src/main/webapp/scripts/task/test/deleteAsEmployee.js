'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services, allowed) {
        if (!runCase('task')) {
            return;
        }
        var it = mocha.it,
            employee = mocha.user,
            httpProvider = angluarjs.httpProvider,
            TaskService = services.TaskService,
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

        var task;
        it("the task should be retrieved.", function(done) {
            expect(TaskService).not.to.be(undefined);
            // to get a task
            sessionemployee.roles = "admin";
            TaskService.queryAllTaskByEmployee(sessionemployee)
            .then(function(paging) {
                var tasks = paging.datas;
                expect(tasks).not.to.be(undefined);
                expect(tasks.length).not.to.be(0);
                task = tasks[0];
                done();
            });
        });

        // 员工不能通过 /task/{taskId} 接口删除任务
        it("a specific task should not be deleted by the api /api/task/{id}.", function(done) {
            $.ajax({
                url: PATH + '/api/task/' + task.id,
                type: 'DELETE',
                headers: {Authorization: token}
            }).then(function(res) {
                throw new Error('should not be deleted by the api /api/task/{id}.');
            }, function() {
                done();
            });
        });

        // 员工不能通过 /task 接口删除所有任务
        it("the all task should not be deleted by the api /api/task", function(done) {
            $.ajax({
                url: PATH + '/api/task/',
                type: 'DELETE',
                headers: {Authorization: token}
            }).then(function(res) {
                throw new Error('should not be deleted by /api/task.');
            }, function() {
                done();
            });
        });

        if (allowed) {
            it("the task should be deleted by admin/chief.", function(done) {
                expect(TaskService).not.to.be(undefined);
                expect(task).not.to.be(undefined);
                TaskService.remove(task).then(function() {
                    done();
                }, function() {
                    throw new Error('the task can not be deleted');
                });
            });
        } else {
            it("the task should not be deleted by non admin/chief.", function(done) {
                expect(TaskService).not.to.be(undefined);
                expect(task).not.to.be(undefined);
                TaskService.remove(task).then(function() {
                    throw new Error('the task should not be deleted');
                }, function() {
                    done();
                });
            });

            it("all task should not be deleted by non admin/chief.", function(done) {
                expect(TaskService).not.to.be(undefined);
                expect(task).not.to.be(undefined);
                TaskService.removeUserTasks(task).then(function() {
                    done();
                }, function() {
                    throw new Error('all task can not be deleted');
                });
            });
        }
    };

});