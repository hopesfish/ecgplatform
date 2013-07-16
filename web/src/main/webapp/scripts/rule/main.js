'use strict';
define(function(require, exports) {

require("./services/RuleService");
require("./services/ReplyConfigService");
require("./directives/Rule");
require("./directives/RuleConfig");

var ruleTemp = require("./templates/rule.html");
var newTemp = require("./templates/new.html");
var editTemp = require("./templates/edit.html");
var configTemp = require("./templates/config.html");

angular.module('ecgRule', ['ecgRuleService', 'ecgReplyConfigService', 'ecgRuleBaseDirectives', 'ecgRuleConfig'])
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
    });
}]);

});// end of define