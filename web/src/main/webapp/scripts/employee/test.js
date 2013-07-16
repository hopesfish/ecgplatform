'use strict';
define(function(require, exports) {

    var testForProfile = require("./test/profile.js").test;
    var testAsAdmin = require("./test/base.js").test;

    exports.testEmployee = function(mocha, angluarjs, services) {
        if (!runCase('employee')) {
            return;
        }

        mocha.user = {username: TESTCONFIGS.admin.username, password: TESTCONFIGS.admin.password};
        runCaseAs('admin') ? testForProfile(mocha, angluarjs, services) : null;


    };

});