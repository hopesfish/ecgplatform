'use strict';
define(function(require, exports) {

require("./services/RuleService");
require("./services/ReplyConfigService");
require("./services/SysConfigService");
require("./services/APKService");
require("./directives/Rule");
require("./directives/RuleConfig");
require("./directives/SysConfig");
require("./directives/APK");

var ruleTemp = require("./templates/rule.html");
var newTemp = require("./templates/new.html");
var editTemp = require("./templates/edit.html");
var configTemp = require("./templates/config.html");
var sysconfigTemp = require("./templates/sysconfig.html");
var apkTemp = require("./templates/apk.html");

angular.module('ecgSys', ['ecgRuleService', 'ecgReplyConfigService', 'ecgSysConfigService',  'ecgAPKService', 'ecgRuleDirectives', 'ecgRuleConfig', 'ecgSysConfig', 'ecgAPK'])
.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
    .when('/rule', {
        template: ruleTemp,
        controller: 'RuleController'
    })
    .when('/rule/new', {
        template: newTemp,
        controller: 'RuleNewController'
    })
    .when('/rule/:id', {
        template: editTemp,
        controller: 'RuleEditController'
    })
    .when('/rule/:id/config', {
        template: configTemp,
        controller: 'ReplyConfigController'
    })
    .when('/sysconfig', {
        template: sysconfigTemp,
        controller: 'SysConfigController'
    })
    .when('/apk', {
        template: apkTemp,
        controller: 'APKController'
    });
}]);

});// end of define