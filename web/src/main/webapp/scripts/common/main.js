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
        'pending': '等待接线员回复',
        'proceeding': '等待专家回复',
        'completed': '已回复'
    };

    /*
    设备类型  指标编号  指标名称  正常下限  正常上限  指标说明
    11  1 舒张压 60  89  血压的舒张压（毫米汞柱）
    11  2 收缩压 89  139 血压的收缩压（毫米汞柱）
    11  3 心率  60  100 次/分钟
    11  4 血氧饱和度 94  100 百分比
    11  5 呼吸  16  20  次/分钟
    11  6 体温  36  37  ℃（腋下温度）
    11  7 脉率  60  100 次/分钟
    11  8 血糖  毫摩

    10-手机（内置检测硬件的手机）
    设备类型  指标编号  指标名称  正常下限  正常上限  指标说明
    10  3 心率  60  100 次/分钟
    10  6 体温  36  37  ℃（腋下温度）

    */

    var codes = {
        '1': {col: "bloodPressureLow", label: "舒张压"},
        '2': {col: "bloodPressureHigh", label: "收缩压"},
        '3': {col: "heartRhythm", label: "心率"},
        '4': {col: "bloodOxygen", label: "血氧饱和度"},
        '5': {col: "breath", label: "呼吸"},
        '6': {col: "bodyTemp", label: "体温"},
        //'7': {col: "pulserate", label: "脉率"}
        '8': {col: "bloodSugar", label: "血糖"}
    };
    var employeestatus = [
        {key: 'ONLINE', label: "正在上班"},
        {key: 'AWAY', label: "暂离"},
        {key: 'OFFLINEING', label: "准备下班"},
        {key: 'OFFLINE', label: "下班"}
    ];
    return {
        getGenders: function() {
            return [{label: '男', value: 1}, {label: '女', value: 0}];
        },
        getGenderLabel: function(gender) {
            return gender ?  '男': '女';
        },
        getBools: function() {
            return [{label: '是', value: true}, {label: '否', value: false}];
        },
        getBoolLabel: function(value) {
            return value ?  '是': '否';
        },
        getMarrieds: function() {
            return [{label: '未婚', value: 0}, {label: '已婚', value: 1}, {label: '离异', value: 2}];
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
        },
        getCodesList: function() {
            var list = [];
            for (var prop in codes) {
                list.push({value: prop, label: codes[prop].label});
            }
            return list;
        },
        getCodeLabel: function(_prop) {
            var label = '未知';
            for (var prop in codes) {
                if (_prop == prop) {
                    label = codes[prop].label;
                    break;
                }
            }
            return label;
        },
        getEmployeeStatus: function() {
            return employeestatus;
        },
        getEmployeeStatusLabel: function(status) {
            var label;
            $(employeestatus).each(function(i, estatus) {
                if (estatus.key === status) {
                    label = estatus.label;
                }
            });
            return label;
        }
    };
});

});
