'use strict';
define(function(require, exports) {

require("./services/ChiefService");
require("./services/ExpertService");
require("./services/OperatorService");
require("./directives/Chief");
require("./directives/Expert");
require("./directives/Operator");

var chiefTemp = require("./templates/chief.html");
var chiefNewTemp = require("./templates/chief/new.html");
var chiefViewTemp = require("./templates/chief/view.html");

var expertTemp = require("./templates/expert.html");
var expertNewTemp = require("./templates/expert/new.html");
var expertViewTemp = require("./templates/expert/view.html");

var operatorTemp = require("./templates/operator.html");
var operatorNewTemp = require("./templates/operator/new.html");
var operatorViewTemp = require("./templates/operator/view.html");

angular.module('ecgEmployee', 
    ['ecgChiefService', 'ecgExpertService', 'ecgOperatorService',
     'ecgChief', 'ecgExpert', 'ecgOperator'])
.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
    .when('/chief', {
        template: chiefTemp,
        controller: 'ChiefController'
    })
    .when('/chief/new', {
        template: chiefNewTemp,
        controller: 'ChiefNewController'
    })
    .when('/chief/:id', {
        template: chiefViewTemp,
        controller: 'ChiefViewController'
    })
    .when('/expert', {
        template: expertTemp,
        controller: 'ExpertController'
    })
    .when('/expert/new', {
        template: expertNewTemp,
        controller: 'ExpertNewController'
    })
    .when('/expert/:id', {
        template: expertViewTemp,
        controller: 'ExpertViewController'
    })
    .when('/operator', {
        template: operatorTemp,
        controller: 'OperatorController'
    })
    .when('/operator/new', {
        template: operatorNewTemp,
        controller: 'OperatorNewController'
    })
    .when('/operator/:id', {
        template: operatorViewTemp,
        controller: 'OperatorViewController'
    });
}]);

});// end of define