define(function(require, exports) {

    'use strict';
    var testStageOneForAdminAndChief = require("./test/stageOneForAdminAndChief").test;
    var testStageTwoForOperator = require("./test/stageTwoForOperator").test;
    var testStageThreeForAdminAndChief= require("./test/stageThreeForAdminAndChief").test;
    var testStageThreeForOperator= require("./test/stageThreeForOperator").test;
    var testStageThreeForExpert= require("./test/stageThreeForExpert").test;
    
    exports.testTask = function(mocha, angluarjs, services) {
        if (!runCase('task')) {
            return;
        }

        console.info('all roles will be used in the task test module.');
        
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
         * 场景1,管理员、主任都可以查询未完成任务的信息,并将当前的环境信息保存
         */
        mocha.user = {username: TESTCONFIGS.admin.username, password: TESTCONFIGS.admin.password};
        testStageOneForAdminAndChief(mocha, angluarjs, services, adminRuntime);
        
        mocha.user = {username: TESTCONFIGS.chief.username, password: TESTCONFIGS.chief.password};
        testStageOneForAdminAndChief(mocha, angluarjs, services, chiefRuntime);
        // 场景2 结束

        /**
         * 场景2,接线员自己回复，并将更改当前环境
         */
        mocha.user = {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password};
        testStageTwoForOperator(mocha, angluarjs, services, operatorRuntime);
        
        mocha.user = {username: TESTCONFIGS.operator1.username, password: TESTCONFIGS.operator1.password};
        testStageTwoForOperator(mocha, angluarjs, services, operator1Runtime);
        // 场景2 结束

        /**
         * 场景3,主任配置接线员和专家的多对多关系，接线员发送回复并转交专家，专家回复
         */
        mocha.user = {username: TESTCONFIGS.chief.username, password: TESTCONFIGS.chief.password};
        
        mocha.operator = {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password};
        mocha.expert = {username: TESTCONFIGS.expert.username, password: TESTCONFIGS.expert.password};
        testStageThreeForAdminAndChief(mocha, angluarjs, services);
        
        mocha.operator = {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password};
        mocha.expert = {username: TESTCONFIGS.expert1.username, password: TESTCONFIGS.expert1.password};
        testStageThreeForAdminAndChief(mocha, angluarjs, services);

        mocha.operator = {username: TESTCONFIGS.operator1.username, password: TESTCONFIGS.operator1.password};
        mocha.expert = {username: TESTCONFIGS.expert.username, password: TESTCONFIGS.expert.password};
        testStageThreeForAdminAndChief(mocha, angluarjs, services);

        mocha.operator = {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password};
        mocha.expert = {username: TESTCONFIGS.expert1.username, password: TESTCONFIGS.expert1.password};
        testStageThreeForAdminAndChief(mocha, angluarjs, services);

        // 接线员1 forward，专家1发现有新任务并处理，接线员1 forwar，专家2发现有新任务并处理
        mocha.user = {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password};
        testStageThreeForOperator(mocha, angluarjs, services);
        mocha.user = {username: TESTCONFIGS.expert.username, password: TESTCONFIGS.expert.password};
        testStageThreeForExpert(mocha, angluarjs, services);

        mocha.user = {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password};
        testStageThreeForOperator(mocha, angluarjs, services);
        mocha.user = {username: TESTCONFIGS.expert1.username, password: TESTCONFIGS.expert1.password};
        testStageThreeForExpert(mocha, angluarjs, services);

        // 接线员2 连续forward,专家1和专家2分别发现新任务并处理
        mocha.user = {username: TESTCONFIGS.operator1.username, password: TESTCONFIGS.operator1.password};
        testStageThreeForOperator(mocha, angluarjs, services);
        testStageThreeForOperator(mocha, angluarjs, services);

        mocha.user = {username: TESTCONFIGS.expert.username, password: TESTCONFIGS.expert.password};
        testStageThreeForExpert(mocha, angluarjs, services);
        mocha.user = {username: TESTCONFIGS.expert1.username, password: TESTCONFIGS.expert1.password};
        testStageThreeForExpert(mocha, angluarjs, services);
        // 场景3结束

        it("the runtime should be updated as expectation", function() {
            console.info(adminRuntime);
            console.info(chiefRuntime);
            console.info(operatorRuntime);
            console.info(operator1Runtime);
            expect(adminRuntime.undone).to.be(chiefRuntime.undone);
        });
    };

});