'use strict';
define(function(require, exports) {

require("./Nav");
require("./Header");
require("./Message");
require("./Footer");
require("./Dialog");

angular.module('ecgCommon', ["angular-table", "ecgNav", "ecgHeader", "ecgMessage", "ecgFooter", "ecgDialog"])
.factory("EnumService", function() {
    var levels = {
        'danger': '危险',
        'warning': '值得关注',
        'success': '健康',
        'outside': '数据异常'
    };
    var workstatus = {
        'pending': '等待接线员处理',
        'proceeding': '等待专家处理',
        'completed': '已处理'
    };
    var codes = {
        '3': {col: "heartRhythm", label: "心率"},
        '4': {col: "bloodOxygen", label: "血氧饱和度"},
        '5': {col: "breath", label: "呼吸"}
    };
    return {
        getGenders: function() {
            return [{label: '男', value: 1}, {label: '女', value: 0}];
        },
        getGenderLabel: function(gender) {
            return gender ?  '男': '女';
        },
        getDismissedStates: function() {
            return [{label: '离职', value: true}, {label: '在职', value: false}];
        },
        getDismissedLabel: function(dismissed) {
            return !dismissed ? '在职' : '离职';
        },
        getLevelLabel: function(level) {
            return levels[level] || '未知';
        },
        translateLevel : function(level) {
            switch(level) {
            case 'danger':
                return 'important';
            break;
            case 'success':
                return 'success';
            break;
            case 'warning':
                return 'warning';
            break;
            case 'outside':
                return 'inverse';
            break;
            }
        },
        getWorkStatusLabel: function(status) {
            return workstatus[status] || '未知状态';
        },
        getCodes: function() {
            return codes;
        }
    };
});

});
