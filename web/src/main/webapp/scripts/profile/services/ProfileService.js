'use strict';
define(function(require, exports) {

angular.module('ecgProfileService', [])
    .factory("ProfileService", function($rootScope, $http) {
        var employeeUri = PATH + "/api/employee";
        var userUri = PATH + "/api/user";

        function initUser(props) {
            var user = $.extend({}, props || {roles: ''});
            user.isEmployee = function() {
                return user.isAdmin() || user.isChief() || user.isExpert() || user.isOperator();
            };
            user.isAdmin = function() {
                return this.roles.indexOf('admin') >= 0;
            };
            user.isChief = function() {
                return this.roles.indexOf('chief') >= 0;
            };
            user.isExpert = function() {
                return this.roles.indexOf('expert') >= 0;
            };
            user.isOperator = function() {
                return this.roles.indexOf('operator') >= 0;
            };
            return user;
        };

        return {
            get: function(username) {
                return $http({
                    method: 'GET',
                    cache: false,
                    url: employeeUri + "?username=" + username
                }).then(function(res) {
                    if (res.data.datas && res.data.datas.length === 1) {
                        return initUser(res.data.datas[0]);
                    } else {
                        return null;
                    }
                }, function() {
                    return null;
                });
            },
            update: function(employee) {
                var data = $.extend({}, employee);
                delete data.isEmployee;
                delete data.isAdmin;
                delete data.isChief;
                delete data.isExpert;
                delete data.isOperator;
                delete data.version;
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(data),
                    url: employeeUri + '/' + employee.id
                });
            },
            updatePassword: function(id, oldpwd, newpwd) {
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param({oldPassword: oldpwd, newPassword: newpwd}),
                    url: employeeUri + '/' + id + '/password'
                });
            },
            resetPassword: function(id) {
                return this.updatePassword(id, '', '');
            },
            updateUser: function(user) {
                var data = $.extend({}, user);
                delete data.isEmployee;
                delete data.isAdmin;
                delete data.isChief;
                delete data.isExpert;
                delete data.isOperator;
                delete data.version;
                delete data.username;
                delete data.mobile;
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(data),
                    url: userUri + '/' + user.id
                });
            },
            updateUserPassword: function(id, oldpwd, newpwd) {
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param({oldPassword: oldpwd, newPassword: newpwd}),
                    url: userUri + '/' + id + '/password'
                });
            },
        };
    });
});