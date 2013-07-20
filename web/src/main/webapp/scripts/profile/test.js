'use strict';
define(function(require, exports) {

    var testProfileAsEmployee = require("./test/profile.js").test;
    var testProfileAsUser = require("./test/uprofile.js").test;

    exports.testProfile = function(mocha, angluarjs, services) {
        if (!runCase('profile')) {
            return;
        }

        // 个人资料编辑功能
        runCaseAs('admin') ? testProfileAsEmployee({
            it: mocha.it,
            user: {username: TESTCONFIGS.admin.username, password: TESTCONFIGS.admin.password}
        }, angluarjs, services) : null;

        runCaseAs('chief') ? testProfileAsEmployee({
            it: mocha.it,
            user: {username: TESTCONFIGS.chief.username, password: TESTCONFIGS.chief.password}
        }, angluarjs, services) : null;

        runCaseAs('expert') ? testProfileAsEmployee({
            it: mocha.it,
            user: {username: TESTCONFIGS.expert.username, password: TESTCONFIGS.expert.password}
        }, angluarjs, services) : null;

        runCaseAs('operator') ? testProfileAsEmployee({
            it: mocha.it,
            user: {username: TESTCONFIGS.operator.username, password: TESTCONFIGS.operator.password}
        }, angluarjs, services) : null;

        runCaseAs('user') ? testProfileAsUser({
            it: mocha.it,
            user: {username: TESTCONFIGS.user.username, password: TESTCONFIGS.user.password}
        }, angluarjs, services) : null;
    };

});