'use strict';
define(function(require, exports) {

var operatorEditTemp = require("../templates/operator/edit.html");
var operatorRulesTemp = require("../templates/operator/rules.html");
var operatorExpertsTemp = require("../templates/operator/experts.html");
var operatorDialogTemp = require("../templates/operator/operatorsdialog.html");

angular.module('ecgOperator', [])
.controller('OperatorController', ['$scope', '$filter', '$timeout', '$location', 'EnumService', 'OperatorService', function ($scope, $filter, $timeout, $location, EnumService, OperatorService) {
    // 表格头
    $scope.subheader.title = "接线员";

    // 命名空间
    $scope.operator = {};

    // 表格展示
    $scope.operator.data = null;
    $scope.operator.filteredData = null;
    // 刷新功能
    function refreshGrid() {
        $scope.dialog.showLoading();
        OperatorService.queryAll().then(function(operators) {
            $scope.dialog.hideStandby();
            $scope.operator.data = operators;
            $scope.operator.filteredData = $scope.operator.data;
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("无法加载用户数据!");
        });
    }
    refreshGrid();

    // 显示label
    $scope.operator.getGenderLabel = function(operator) {
        return EnumService.getGenderLabel(operator.gender);
    };
    $scope.operator.getDismissedLabel = function(operator) {
        return EnumService.getDismissedLabel(operator.dismissed);
    };

    // 当前选中数据
    $scope.operator.selectedItem = null;

    // 删除功能
    $scope.operator.confirmDelete = function() {
        var selectedItem = $scope.operator.selectedItem;
        if (selectedItem === null) {
            $scope.dialog.alert({
                text: '请选择一条记录!'
            });
            return;
        }
        $scope.dialog.confirm({
            text: "请确认删除专家:" + selectedItem.name + ", 该操作无法恢复!",
            handler: function() {
                $scope.dialog.showStandby();
                OperatorService.remove(selectedItem.id)
                .then(function() {
                    $scope.dialog.hideStandby();
                    $scope.operator.selectedItem = null;
                    $scope.message.success("删除成功!");
                    // 刷新
                    refreshGrid();
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("无法删除该数据,可能是您的权限不足,请联系管理员!");
                });
            }
        });
    };

    // 过滤功能
    $scope.operator.queryChanged = function(query) {
        return $scope.operator.filteredData = $filter("filter")($scope.operator.data, query);
    };

    // 编辑功能
    $scope.operator.showPage = function(operator) {
        $location.path("operator/" + operator.id);
    };

}])
.controller('OperatorNewController', ['$scope', '$timeout', '$location', 'EnumService', 'ProfileService', 'OperatorService',
    function ($scope, $timeout, $location, EnumService, ProfileService, OperatorService) {
    $scope.subheader.title = "新增专家";

    $scope.operator = {};
    $scope.operator.newobj = OperatorService.getPlainObject();
    $scope.operator.genders = EnumService.getGenders();
    $scope.operator.dismissedStates = EnumService.getDismissedStates();

    $('#operator-birthday').datetimepicker({
        format: "yyyy-MM-dd",
        language: "zh-CN",
        pickTime: false
    });

    $scope.operator.showDatePicker = function() {
        $('#operator-birthday').datetimepicker('show');
    };

    $scope.operator.isUnique = true;
    $scope.operator.checkUnique = function() {
        if (!$scope.operator.newobj.operatorname) { return; }
        ProfileService.get($scope.operator.newobj.operatorname).then(function(operator) {
            if (operator) { 
                $scope.operator.isUnique = false;
                $scope.message.warn("用户" + $scope.operator.newobj.operatorname + "已存在!");
            } else {
                $scope.operator.isUnique = true;
            }
        }, function() {
            $scope.operator.isUnique = true;
            $scope.message.warn("查询用户是否唯一时出错!");
        });
    };

    $scope.operator.create = function() {
        $scope.dialog.showStandby();
        $scope.operator.newobj.birthday = $('#operator-birthday input').val();
        $scope.operator.newobj.password = $scope.operator.newobj.operatorname;
        OperatorService.create($scope.operator.newobj)
        .then(function(result) {
            $scope.dialog.hideStandby();
            if (result) {
                $scope.message.success("新增成功!");
                $location.path("/operator");
            } else {
                $scope.message.error("新增失败!");
            }
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("服务器异常,新增失败!");
        });;
    };
}])
.controller('OperatorViewController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'OperatorService',
    function ($scope, $routeParams, $timeout, $location, EnumService, OperatorService) {
    $scope.subheader.title = "编辑专家";

    $scope.operator = {};
    $scope.operator.tab = 1; // 默认为基本页面

}])
// 基本信息
.controller('OperatorEditController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'ProfileService', 'OperatorService',
    function ($scope, $routeParams, $timeout, $location, EnumService, ProfileService, OperatorService) {
    $scope.operator.updateobj = null; //OperatorService.get($routeParams.id);

    // 初始化界面,并获得最新version
    function refresh() {
        $scope.dialog.showLoading();
        OperatorService.get($routeParams.id).then(function(operator) {
            $scope.dialog.hideStandby();
            $scope.operator.updateobj = operator;
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("加载操作员数据失败!");
        });
    };
    refresh();

    $scope.operator.genders = EnumService.getGenders();
    $scope.operator.dismissedStates = EnumService.getDismissedStates();

    $('#operator-birthday').datetimepicker({
        format: "yyyy-MM-dd",
        language: "zh-CN",
        pickTime: false,
    }).on('changeDate', function(e) {
        $scope.operator.updateobj.birthday = $('#operator-birthday input').val();
    });

    $scope.operator.showDatePicker = function() {
        $('#operator-birthday').datetimepicker('show');
    };

    $scope.operator.update = function() {
        $scope.dialog.showStandby();
        $scope.operator.updateobj.birthday = $('#operator-birthday input').val();
        OperatorService.update($scope.operator.updateobj)
        .then(function(result) {
            $scope.dialog.hideStandby();
            $scope.message.success("编辑成功!");
            refresh();
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("编辑失败!");
        });;
    };

    $scope.operator.resetPassword = function() {
        $scope.dialog.confirm({
            text: "重置后登录密码将于登录名一致，确定继续?",
            handler: function() {
                $scope.dialog.showStandby();
                ProfileService.resetPassword($scope.operator.updateobj.id)
                .then(function(result) {
                    $scope.dialog.hideStandby();
                    $scope.message.success("重置密码成功!");
                    refresh();
                }, function() {
                    $scope.dialog.hideStandby();
                    $scope.message.error("重置密码失败!");
                });
            }
        });
    };
}])
.directive("ecgOperatorEdit", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : operatorEditTemp,
        controller : "OperatorEditController",
        link : function($scope, $element, $attrs) {
        }
    };
} ])
// 自定义规则
.controller('OperatorRulesController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'OperatorService',
    function ($scope, $routeParams, $timeout, $location, EnumService, OperatorService) {
    $scope.operator.rules = OperatorService.getRules($routeParams.id);

    $scope.operator.updateRules = function() {
        $scope.dialog.showStandby();
        /* TODO: */
        // OperatorService.update($scope.operator.updateobj);
        $timeout(function() {
            $scope.dialog.hideStandby();
            $scope.message.success("修改自定义规则成功!");
        }, 2000);
    };
}])
.directive("ecgOperatorRules", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : operatorRulesTemp,
        controller : "OperatorRulesController",
        link : function($scope, $element, $attrs) {
        }
    };
} ])
// 配置接线员
.controller('OperatorExpertsController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'OperatorService',
    function ($scope, $routeParams, $timeout, $location, EnumService, OperatorService) {
    $scope.operator.experts = null;

    function refreshLinks() {
        OperatorService.getExperts($routeParams.id).then(function(experts) {
            $scope.operator.experts = experts;
        }, function() {
            $scope.message.error("加载接线员数据失败!");
        });
    }
    refreshLinks();

    $scope.operator.check = function(expert) {
        if (expert.removed === true) {
            expert.removed = false;
            return;
        } else {
            expert.removed = true;
        }
    };

    $scope.operator.isCheckAll = false;
    $scope.operator.checkAll = function() {
        $scope.operator.isCheckAll = !$scope.operator.isCheckAll;
        $($scope.operator.experts).each(function(i, expert) {
            expert.removed = $scope.operator.isCheckAll;
        });
    };

    $scope.operator.addExperts = function() {
        $scope.expertdialog.show({
            handler: function(experts) {
                var len = experts.length, count = 0;
                $(experts).each(function(i, expert) {
                    $scope.dialog.showStandby();
                    OperatorService.linkExpert($routeParams.id, expert)
                    .then(function(flag) {
                        $scope.dialog.hideStandby();
                        if (flag) {
                            count++;
                        } else {
                            $scope.message.error("无法绑定专家：" + expert.name);
                        }
                        if (count === len) {
                            $scope.message.success("成功绑定！");
                            refreshLinks();
                        }
                    }, function() {
                        $scope.dialog.hideStandby();
                        $scope.message.error("无法绑定接线员：" + operator.name);
                    });
                });
            }
        });
    };
    
    $scope.operator.removeExperts = function() {
        
        var removes = [], experts = $scope.operator.experts, len = 0, count = 0;

        $(experts).each(function(i, expert) {
            if (expert.removed) {
                removes.push(expert);
            }
        });
        
        if (removes.length == 0) {
            $scope.dialog.alert({
                text: '请选择需要删除的绑定!'
            });
            return;
        };

        len = removes.length;

        $scope.dialog.confirm({
            text: "请确认删除这些专家与接线员绑定, 该操作无法恢复!",
            handler: function() {
                $scope.dialog.showStandby();
                $(removes).each(function(i, remove) {
                    OperatorService.unlinkExpert($routeParams.id, remove)
                    .then(function() {
                        count++;
                        if (count === len) {
                            $scope.dialog.hideStandby();
                            $scope.message.success("成功删除绑定！");
                            refreshLinks();
                        }
                    }, function() {
                        count++;
                        $scope.dialog.hideStandby();
                        $scope.message.error("无法删除该绑定，用户名为：" + remove.name);
                    });
                });
            }
        });
    };
}])
.directive("ecgOperatorExperts", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : operatorExpertsTemp,
        controller : "OperatorExpertsController",
        link : function($scope, $element, $attrs) {
        }
    };
}])
.controller('OperatorDialogController', 
    ['$scope', '$filter', '$timeout', '$location', 'EnumService', 'OperatorService', 
    function ($scope, $filter, $timeout, $location, EnumService, OperatorService) {

    // 命名空间
    $scope.operatordialog = {};

    // 表格展示
    $scope.operatordialog.data = null;
    function refreshGrid() {
        OperatorService.queryAll().then(function(operators) {
            $scope.operatordialog.data = operators;
        });
    };
    refreshGrid();

    $scope.operatordialog.execute = function() {
        var selecteds = [];
        $($scope.operatordialog.data).each(function(i, operator) {
            if (operator.selected) {
                selecteds.push(operator);
            }
        });
        $scope.operatordialog.hide();
        if ($scope.operatordialog.handler instanceof Function) {
            $scope.operatordialog.handler(selecteds);
        }
    };

    $scope.operatordialog.hide = function(opts) {
      $('#ecgOperatorsDialog').modal('hide');
    };

    $scope.operatordialog.show = function(opts) {
      var opts = opts || {};
      $scope.operatordialog.handler = opts.handler;
      $('#ecgOperatorsDialog').modal('show');
      refreshGrid();
    };

}])
.directive("ecgOperatorDialog", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : operatorDialogTemp,
        controller : "OperatorDialogController",
        link : function($scope, $element, $attrs) {
        }
    };
}]);

});