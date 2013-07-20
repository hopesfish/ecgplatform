'use strict';
define(function(require, exports) {

require("./services/ProfileService");
require("./directives/Profile");

var profileTemp = require("./templates/profile.html");

angular.module('ecgProfile',  ['ecgProfileService', 'ecgProfileDirectives'])
.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
    .when('/profile', {
        template: profileTemp,
        controller: 'ProfileController'
    });
}]);

});// end of define