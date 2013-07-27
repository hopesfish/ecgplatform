'use strict';
define(function(require, exports) {

    exports.test = function(mocha, angluarjs, services) {
        if (!runCase('task')) {
            return;
        }
        var it = mocha.it,
            user = mocha.user,
            httpProvider = angluarjs.httpProvider,
            UserService = services.UserService;

        // 登录
        var token;
        it("the user should authenciated.", function(done) {
            $.ajax({
                url: PATH + '/api/user/auth',
                data: {
                    'username': user.username,
                    'password': user.password
                },
                type: 'POST',
                dataType: 'json'
            }).then(function(res) {
                expect(res.token).not.to.be(undefined);
                token = res.token;
                done();
            }, function() {
                throw new Error('failed to authnenciate as a new user.');
            });
        });

        // 获取id
        var userId;
        it("the user's info should be retrieved.", function(done) {
            $.ajax({
                url: PATH + '/api/user?username=' + user.username,
                dataType: 'json',
                headers: {Authorization: token}
            }).then(function(res) {
                expect(res).not.to.be(undefined);
                expect(res.datas).not.to.be(undefined);
                expect(res.datas.length).to.be(1);
                userId = res.datas[0].id;
                expect(userId).not.to.be(undefined);
                done();
            }, function() {
                throw new Error('failed to retrieve user\'s  id.');
            });
        });

        // 获取某个id
        var taskId, examinationId, apkId, anotherApkId;
        it("the user's task should be retrieved.", function(done) {
            $.ajax({
                url: PATH + '/api/user/' + userId + '/task',
                dataType: 'json',
                headers: {Authorization: token}
            }).then(function(res) {
                expect(res).not.to.be(undefined);
                expect(res.datas).not.to.be(undefined);
                expect(res.datas.length).not.to.be(0);
                expect(res.datas.length).not.to.be(1);
                expect(res.datas.length).not.to.be(2);
                taskId = res.datas[0].id;
                examinationId = res.datas[0].examinationId;
                apkId = res.datas[0].apkId;
                anotherApkId = res.datas[1].apkId;
                expect(taskId).not.to.be(undefined);
                expect(examinationId).not.to.be(undefined);
                expect(apkId).not.to.be(undefined);
                expect(anotherApkId).not.to.be(undefined);
                done();
            }, function() {
                throw new Error('failed to retrieve a task id.');
            });
        });

        it("the user's task should be retrieved with one apkId.", function(done) {
            expect(apkId).not.to.be(undefined);
            $.ajax({
                url: PATH + '/api/user/' + userId + '/task?apkId:in=' + apkId,
                dataType: 'json',
                headers: {Authorization: token}
            }).then(function(res) {
                expect(res).not.to.be(undefined);
                expect(res.datas).not.to.be(undefined);
                expect(res.datas.length).to.be(1);
                expect(res.datas[0].apkId).to.be(apkId);
                done();
            }, function() {
                throw new Error('failed to retrieve a task id.');
            });
        });

        it("the user's task should be retrieved with two apkIds.", function(done) {
            expect(apkId).not.to.be(undefined);
            expect(anotherApkId).not.to.be(undefined);
            $.ajax({
                url: PATH + '/api/user/' + userId + '/task?apkId:in=' + apkId + ',' + anotherApkId,
                dataType: 'json',
                headers: {Authorization: token}
            }).then(function(res) {
                expect(res).not.to.be(undefined);
                expect(res.datas).not.to.be(undefined);
                expect(res.datas.length).to.be(2);
                var found1 = false, found2 = false;
                $(res.datas).each(function(i, task) {
                    if (task.apkId === apkId) {
                        found1 = true;
                    }
                    if (task.apkId === anotherApkId) {
                        found2 = true;
                    }
                });
                expect(found1).to.be(true);
                expect(found2).to.be(true);
                done();
            }, function() {
                throw new Error('failed to retrieve a task id.');
            });
        });

        it("the user's task should not be retrieved with one apkId.", function(done) {
            expect(apkId).not.to.be(undefined);
            expect(anotherApkId).not.to.be(undefined);
            $.ajax({
                url: PATH + '/api/user/' + userId + '/task?apkId:notIn=' + apkId,
                dataType: 'json',
                headers: {Authorization: token}
            }).then(function(res) {
                expect(res).not.to.be(undefined);
                expect(res.datas).not.to.be(undefined);
                expect(res.datas.length).not.to.be(0);
                var found = false;
                $(res.datas).each(function(i, task) {
                    if (task.apkId === apkId) {
                        found = true;
                    }
                });
                expect(found).not.to.be(true);
                done();
            }, function() {
                throw new Error('failed to retrieve a task id.');
            });
        });

        it("the user's task should not be retrieved with two apkIds.", function(done) {
            expect(apkId).not.to.be(undefined);
            $.ajax({
                url: PATH + '/api/user/' + userId + '/task?apkId:notIn=' + apkId + ',' + anotherApkId,
                dataType: 'json',
                headers: {Authorization: token}
            }).then(function(res) {
                expect(res).not.to.be(undefined);
                expect(res.datas).not.to.be(undefined);
                expect(res.datas.length).not.to.be(0);
                var found1= false, found2 = false;
                $(res.datas).each(function(i, task) {
                    if (task.apkId === apkId) {
                        found1 = true;
                    }
                    if (task.apkId === apkId) {
                        found1 = true;
                    }
                });
                expect(found1).not.to.be(true);
                expect(found2).not.to.be(true);
                done();
            }, function() {
                throw new Error('failed to retrieve a task id.');
            });
        });

        it("the user's task should be deleted", function(done) {
            expect(taskId).not.to.be(undefined);
            $.ajax({
                url: PATH + '/api/user/' + userId + '/task/' + taskId,
                type: "DELETE",                
                headers: {Authorization: token}
            }).then(function(res) {
                done();
            }, function() {
                throw new Error('failed to deleted a task.');
            });
        });

        it("the user's examination should be deleted either", function(done) {
            expect(examinationId).not.to.be(undefined);
            $.ajax({
                url: PATH + '/api/examination/' + examinationId,
                type: "GET",                
                headers: {Authorization: token}
            }).then(function(res) {
                throw new Error('the examination should be deleted.');
            }, function() {
                done();
            });
        });

        var fromDate;
        // 如果是逆序获得task
        it("the user's examination should be retrieved", function(done) {
            expect(userId).not.to.be(undefined);
            $.ajax({
                url: PATH + '/api/user/' + userId + '/examination',
                type: "GET",                
                headers: {Authorization: token}
            }).then(function(res) {
                expect(res.datas).not.to.be(undefined);
                expect(res.datas.length).not.to.be(0);
                fromDate = res.datas[0].createdDate;
                expect(fromDate).not.to.be(undefined);
                fromDate = fromDate.substring(0, 11);
                done();
            }, function() {
                throw new Error('the examination should be retrieved.');
            });
        });

        it("the user's examination should be retrieved with condition ge", function(done) {
            expect(userId).not.to.be(undefined);
            $.ajax({
                url: PATH + '/api/user/' + userId + '/examination?createdDate:geth=' + fromDate,
                type: "GET",                
                headers: {Authorization: token}
            }).then(function(res) {
                expect(res.datas).not.to.be(undefined);
                expect(res.datas.length).to.be(1);
                done();
            }, function() {
                throw new Error('the examination should be retrieved with condition ge.');
            });
        });

        it("the user's examination should not be retrieved with condition gt", function(done) {
            expect(userId).not.to.be(undefined);
            $.ajax({
                url: PATH + '/api/user/' + userId + '/examination?createdDate:gth=' + fromDate,
                type: "GET",                
                headers: {Authorization: token}
            }).then(function(res) {
                expect(res.datas).not.to.be(undefined);
                expect(res.datas.length).to.be(0);
                done();
            }, function() {
                throw new Error('the examination should not be retrieved with condition gt.');
            });
        });
    };

});