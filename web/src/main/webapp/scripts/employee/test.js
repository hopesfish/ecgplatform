'use strict';
define(function(require, exports) {

    var testChiefAsAdmin = require("./test/chief/base.js").test;
    var testExpertAsAdminOrChief = require("./test/expert/base.js").test;
    var testOperatorAsAdminOrChief = require("./test/operator/base.js").test;
    var testLinkAsAdminOrChief = require("./test/link/base.js").test;

    exports.testEmployee = function(mocha, angluarjs, services) {
        if (!runCase('employee')) {
            return;
        }

        // 主任模块功能
        runCaseAs('admin') ? testChiefAsAdmin({
            it: mocha.it,
            user: {username: TESTCONFIGS.admin.username, password: TESTCONFIGS.admin.password}
        }, angluarjs, services) : null;

        // 专家模块
        runCaseAs('admin') ? testExpertAsAdminOrChief({
            it: mocha.it,
            user: {username: TESTCONFIGS.admin.username, password: TESTCONFIGS.admin.password}
        }, angluarjs, services) : null;

        runCaseAs('chief') ? testExpertAsAdminOrChief({
            it: mocha.it,
            user: {username: TESTCONFIGS.chief.username, password: TESTCONFIGS.chief.password}
        }, angluarjs, services) : null;

        // 接线员模块
        runCaseAs('admin') ? testOperatorAsAdminOrChief({
            it: mocha.it,
            user: {username: TESTCONFIGS.admin.username, password: TESTCONFIGS.admin.password}
        }, angluarjs, services) : null;

        runCaseAs('chief') ? testOperatorAsAdminOrChief({
            it: mocha.it,
            user: {username: TESTCONFIGS.chief.username, password: TESTCONFIGS.chief.password}
        }, angluarjs, services) : null;

        // 专家和接线员绑定功能
        runCaseAs('admin') ? testLinkAsAdminOrChief({
            it: mocha.it,
            user: {username: TESTCONFIGS.admin.username, password: TESTCONFIGS.admin.password}
        }, angluarjs, services) : null;

        runCaseAs('chief') ? testLinkAsAdminOrChief({
            it: mocha.it,
            user: {username: TESTCONFIGS.chief.username, password: TESTCONFIGS.chief.password}
        }, angluarjs, services) : null;
    };

});