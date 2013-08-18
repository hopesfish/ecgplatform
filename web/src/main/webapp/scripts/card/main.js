'use strict';
define(function(require, exports) {

require("./services/CardService");
require("./directives/Card");

var uploadTemp = require("./templates/upload.html");
var queryTemp = require("./templates/query.html");
var historyTemp = require("./templates/history.html");
var chargeTemp = require("./templates/charge.html");

angular.module('ecgCard', ['ecgCardService', 'ecgCardDirectives'])
.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
    .when('/card/upload', {
        template: uploadTemp,
        controller: 'CardUploadController'
    })
    .when('/card/query', {
        template: queryTemp,
        controller: 'CardQueryController'
    })
    .when('/card/history', {
        template: historyTemp,
        controller: 'CardHistoryController'
    })
    .when('/card/charge', {
        template: chargeTemp,
        controller: 'CardChargeController'
    });
}]);

});// end of define