'use strict';
define(function(require, exports) {

    var testAsEmployee = require("./test/employee.js").test;
    var testAsUser = require("./test/apk.js").test;
    
    exports.testCard = function(mocha, angluarjs, services) {
        if (!runCase('card')) {
            return;
        }

        // 员工获得充值历史
        // 员工充值
        // 员工查询卡号
        runCaseAs('admin') ? testAsEmployee({
            it: mocha.it,
            employee: {username: TESTCONFIGS.admin.username, password: TESTCONFIGS.admin.password},
            user: {username: TESTCONFIGS.user.username, password: TESTCONFIGS.user.password, mobile: TESTCONFIGS.user.mobile},
        }, angluarjs, services) : null;

        runCaseAs('user') ? testAsUser({
            it: mocha.it,
            user: {username: TESTCONFIGS.user.username, password: TESTCONFIGS.user.password, mobile: TESTCONFIGS.user.mobile}
        }, angluarjs, services) : null;

    };

});