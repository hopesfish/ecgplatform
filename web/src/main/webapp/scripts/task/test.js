define(function(require, exports) {

    'use strict';
    var testStageOneForAdminAndChief = require("./test/stageOneForAdminAndChief").test;
    var testStageOneForUser = require("./test/stageOneForUser").test;
    var testStageTwoForOperator = require("./test/stageTwoForOperator").test;
    var testStageThreeForAdminAndChief= require("./test/stageThreeForAdminAndChief").test;
    var testStageThreeForOperator= require("./test/stageThreeForOperator").test;
    var testStageThreeForExpert= require("./test/stageThreeForExpert").test;
    var testDeleteAsEmployee= require("./test/deleteAsEmployee").test;
    var testDeleteAllAsEmployee = require("./test/deleteAllAsEmployee").test;
    var testReportAsAPK = require("./test/reportAsAPK").test;
    
    exports.testTask = function(mocha, angluarjs, services) {
        if (!runCase('task')) {
            return;
        }

        console.debug('all roles will be used in the task test module.');

        var adminRuntime = {
            undone: 0,
            done: 0
        };
        var chiefRuntime = {
            undone: 0,
            done: 0
        };
        var operatorRuntime = {
            undone: 0,
            done: 0
        };
        var operator1Runtime = {
            undone: 0,
            done: 0
        };
        var expertRuntime = {
            undone: 0,
            done: 0
        };
        var expert1Runtime = {
            undone: 0,
            done: 0
        };

        /**
         * 场景1
         * 管理员、主任都可以查询未完成任务的信息,并将当前的环境信息保存
         * 用户可以查询某个范围之内或者之外的task，并删除某个task
         */
        testStageOneForAdminAndChief({
            it: it,
            user: {username: TESTCONFIGS.admin.username, password: TESTCONFIGS.admin.password}
        }, angluarjs, services, adminRuntime);
        
        testStageOneForAdminAndChief({
            it: it,
            user: {username: TESTCONFIGS.chief.username, password: TESTCONFIGS.chief.password}
        }, angluarjs, services, chiefRuntime);

        testStageOneForUser({
            it: it,
            user: {username: TESTCONFIGS.user1.username, password: TESTCONFIGS.user1.password}
        }, angluarjs, services, chiefRuntime);

        // 场景1 结束

        /**
         * 场景2,接线员自己回复，并将更改当前环境
         */
        testStageTwoForOperator({
            it: it,
            user: {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password}
        }, angluarjs, services, operatorRuntime);
        testStageTwoForOperator({
            it: it,
            user: {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password}
        }, angluarjs, services, operator1Runtime);
        // 场景2 结束

        /**
         * 场景3
         * 主任配置接线员和专家的多对多关系，接线员发送回复并转交专家，专家回复
         */
        testStageThreeForAdminAndChief({
            it: it,
            user: {username: TESTCONFIGS.chief.username, password: TESTCONFIGS.chief.password},
            operator: {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password},
            expert: {username: TESTCONFIGS.expert.username, password: TESTCONFIGS.expert.password}
        }, angluarjs, services);
        
        testStageThreeForAdminAndChief({
            it: it,
            user: {username: TESTCONFIGS.chief.username, password: TESTCONFIGS.chief.password},
            operator: {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password},
            expert: {username: TESTCONFIGS.expert1.username, password: TESTCONFIGS.expert1.password}
        }, angluarjs, services);

        testStageThreeForAdminAndChief({
            it: it,
            user: {username: TESTCONFIGS.chief.username, password: TESTCONFIGS.chief.password},
            operator: {username: TESTCONFIGS.operator1.username, password: TESTCONFIGS.operator1.password},
            expert: {username: TESTCONFIGS.expert.username, password: TESTCONFIGS.expert.password}
        }, angluarjs, services);

        testStageThreeForAdminAndChief({
            it: it,
            user: {username: TESTCONFIGS.chief.username, password: TESTCONFIGS.chief.password},
            operator: {username: TESTCONFIGS.operator1.username, password: TESTCONFIGS.operator1.password},
            expert: {username: TESTCONFIGS.expert1.username, password: TESTCONFIGS.expert1.password}
        }, angluarjs, services);

        
        // 接线员2 连续forward,专家和专家1分别发现新任务并处理
        testStageThreeForOperator({
            it: it,
            user: {username: TESTCONFIGS.operator1.username, password: TESTCONFIGS.operator1.password}
        }, angluarjs, services);
        testStageThreeForOperator({
            it: it,
            user: {username: TESTCONFIGS.operator1.username, password: TESTCONFIGS.operator1.password}
        }, angluarjs, services);
        testStageThreeForExpert({
            it: it,
            user: {username: TESTCONFIGS.expert.username, password: TESTCONFIGS.expert.password}
        }, angluarjs, services);
        testStageThreeForExpert({
            it: it,
            user: {username: TESTCONFIGS.expert1.username, password: TESTCONFIGS.expert1.password}
        }, angluarjs, services);

        // 接线员1 forward，专家发现有新任务并处理，接线员1继续forward，专家1发现有新任务并处理
        testStageThreeForOperator({
            it: it,
            user: {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password}
        }, angluarjs, services);
        testStageThreeForExpert({
            it: it,
            user: {username: TESTCONFIGS.expert.username, password: TESTCONFIGS.expert.password}
        }, angluarjs, services);

        testStageThreeForOperator({
            it: it,
            user: {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password}
        }, angluarjs, services);

        testStageThreeForExpert({
            it: it,
            user: {username: TESTCONFIGS.expert1.username, password: TESTCONFIGS.expert1.password}
        }, angluarjs, services);
        // 场景3结束

        it("the runtime should be updated as expectation", function() {
            console.debug(adminRuntime);
            console.debug(chiefRuntime);
            console.debug(operatorRuntime);
            console.debug(operator1Runtime);
            expect(adminRuntime.undone).to.be(chiefRuntime.undone);
        });

        // 最终场景
        /**
         * 专家，接线员不能删除任何task
         * 管理员和主任可以删除某个用户的task
         * 管理员和主任可以删除某个用户的全部 task
         */

        testDeleteAsEmployee({
            it: it,
            user: {username: TESTCONFIGS.expert.username, password: TESTCONFIGS.expert.password}
        }, angluarjs, services, adminRuntime, false);

        testDeleteAsEmployee({
            it: it,
            user: {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password}
        }, angluarjs, services, adminRuntime, false);

        testDeleteAsEmployee({
            it: it,
            user: {username: TESTCONFIGS.admin.username, password: TESTCONFIGS.admin.password}
        }, angluarjs, services, adminRuntime, true);

        testDeleteAsEmployee({
            it: it,
            user: {username: TESTCONFIGS.chief.username, password: TESTCONFIGS.chief.password}
        }, angluarjs, services, adminRuntime, true);

        testDeleteAllAsEmployee({
            it: it,
            employee: {username: TESTCONFIGS.admin.username, password: TESTCONFIGS.admin.password},
            user: {username: TESTCONFIGS.user4.username, password: TESTCONFIGS.user4.password}
        }, angluarjs, services, adminRuntime, true);

        testDeleteAllAsEmployee({
            it: it,
            employee: {username: TESTCONFIGS.chief.username, password: TESTCONFIGS.chief.password},
            user: {username: TESTCONFIGS.user5.username, password: TESTCONFIGS.user5.password}
        }, angluarjs, services, adminRuntime, true);

    };

    exports.testReport = function(mocha, angluarjs, services) {
        if (!runCase('report')) {
            return;
        }
        testReportAsAPK({
            it: it,
            user: {username: TESTCONFIGS.user6.username, password: TESTCONFIGS.user6.password}
        }, angluarjs, services);
    }
});