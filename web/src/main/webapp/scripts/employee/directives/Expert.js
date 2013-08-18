'use strict';
define(function(require, exports) {

var expertEditTemp = require("../templates/expert/edit.html");
var expertRulesTemp = require("../templates/expert/rules.html");
var expertOperatorsTemp = require("../templates/expert/operators.html");
var expertDialogTemp = require("../templates/expert/expertsdialog.html");

angular.module('ecgExpert', [])
.controller('ExpertController', ['$scope', '$filter', '$timeout', '$location', 'EnumService', 'ExpertService', function ($scope, $filter, $timeout, $location, EnumService, ExpertService) {
    // 表格头
    $scope.subheader.title = "专家";

    // 命名空间
    $scope.expert = {};

    // 表格展示
    $scope.expert.data = null;
    $scope.expert.filteredData = null;
    // 刷新功能
    function refreshGrid() {
        $scope.dialog.showLoading();
        ExpertService.queryAll().then(function(experts) {
            $scope.dialog.hideStandby();
            $scope.expert.data = experts;
            $scope.expert.filteredData = $scope.expert.data;
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("无法加载专家数据!");
        });
    }
    refreshGrid();

    // 显示label
    $scope.expert.getGenderLabel = function(expert) {
        return EnumService.getGenderLabel(expert.gender);
    };
    $scope.expert.getDismissedLabel = function(expert) {
        return EnumService.getDismissedLabel(expert.dismissed);
    };

    // 当前选中数据
    $scope.expert.selectedItem = null;

    // 删除功能
    $scope.expert.confirmDelete = function() {
        var selectedItem = $scope.expert.selectedItem;
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
                ExpertService.remove(selectedItem.id)
                .then(function() {
                    $scope.dialog.hideStandby();
                    $scope.expert.selectedItem = null;
                    $scope.message.success("删除专家成功!");
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
    $scope.expert.queryChanged = function(query) {
        return $scope.expert.filteredData = $filter("filter")($scope.expert.data, query);
    };

    // 编辑功能
    $scope.expert.showPage = function(expert) {
        $location.path("expert/" + expert.id);
    };

    // 刷新功能
    $scope.expert.refresh = refreshGrid;

}])
.controller('ExpertNewController', ['$scope', '$timeout', '$location', 'EnumService', 'ProfileService', 'ExpertService',
    function ($scope, $timeout, $location, EnumService, ProfileService, ExpertService) {
    $scope.subheader.title = "新增专家";

    $scope.expert = {};
    $scope.expert.newobj = ExpertService.getPlainObject();
    $scope.expert.genders = EnumService.getGenders();
    $scope.expert.dismissedStates = EnumService.getDismissedStates();

    $('#expert-birthday').datetimepicker({
        format: "yyyy-MM-dd",
        language: "zh-CN",
        pickTime: false
    }).on('changeDate', function(e) {
        $scope.expert.newobj.birthday = $('#expert-birthday input').val();
    });

    $scope.expert.showDatePicker = function() {
        $('#expert-birthday').datetimepicker('show');
    };

    $scope.expert.isUnique = true;
    $scope.expert.checkUnique = function() {
        if (!$scope.expert.newobj.username) { return; }
        ProfileService.get($scope.expert.newobj.username).then(function(user) {
            if (user) { 
                $scope.expert.isUnique = false;
                $scope.message.warn("登录名为" + $scope.expert.newobj.username + "的员工已存在!");
            } else {
                $scope.expert.isUnique = true;
            }
        }, function() {
            $scope.expert.isUnique = true;
            $scope.message.warn("查询登录名是否唯一时出错!");
        });
    };

    $scope.expert.create = function() {
        $scope.dialog.showStandby();
        $scope.expert.newobj.birthday = $('#expert-birthday input').val();
        $scope.expert.newobj.password = $scope.expert.newobj.username;
        ExpertService.create($scope.expert.newobj)
        .then(function(result) {
            $scope.dialog.hideStandby();
            if (result) {
                $scope.message.success("新增专家成功!");
                $location.path("/expert");
            } else {
                $scope.message.error("新增专家失败!");
            }
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("服务器异常,新增专家失败!");
        });;
    };
}])
.controller('ExpertViewController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'ExpertService',
    function ($scope, $routeParams, $timeout, $location, EnumService, ExpertService) {
    $scope.subheader.title = "编辑专家";

    $scope.expert = {};
    $scope.expert.tab = 1; // 默认为基本页面

}])
// 基本信息
.controller('ExpertEditController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'ProfileService', 'ExpertService',
    function ($scope, $routeParams, $timeout, $location, EnumService, ProfileService, ExpertService) {
    $scope.expert.updateobj = null; //ExpertService.get($routeParams.id);

    // 初始化界面,并获得最新version
    function refresh() {
        $scope.dialog.showLoading();
        ExpertService.get($routeParams.id).then(function(expert) {
            $scope.dialog.hideStandby();
            $scope.expert.updateobj = expert;
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("加载专家数据失败!");
        });
    };
    refresh();

    $scope.expert.genders = EnumService.getGenders();
    $scope.expert.dismissedStates = EnumService.getDismissedStates();

    $('#expert-birthday').datetimepicker({
        format: "yyyy-MM-dd",
        language: "zh-CN",
        pickTime: false,
    }).on('changeDate', function(e) {
        $scope.expert.updateobj.birthday = $('#expert-birthday input').val();
    });

    $scope.expert.showDatePicker = function() {
        $('#expert-birthday').datetimepicker('show');
    };

    $scope.expert.update = function() {
        $scope.dialog.showStandby();
        $scope.expert.updateobj.birthday = $('#expert-birthday input').val();
        ExpertService.update($scope.expert.updateobj)
        .then(function(result) {
            $scope.dialog.hideStandby();
            $scope.message.success("编辑专家成功!");
            refresh();
        }, function() {
            $scope.dialog.hideStandby();
            $scope.message.error("编辑专家失败!");
        });;
    };

    $scope.expert.resetPassword = function() {
        $scope.dialog.confirm({
            text: "重置后登录密码将于登录名一致，确定继续?",
            handler: function() {
                $scope.dialog.showStandby();
                ProfileService.resetPassword($scope.expert.updateobj.id)
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
.directive("ecgExpertEdit", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : expertEditTemp,
        controller : "ExpertEditController",
        link : function($scope, $element, $attrs) {
        }
    };
} ])
// 自定义规则
.controller('ExpertRulesController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'ExpertService',
    function ($scope, $routeParams, $timeout, $location, EnumService, ExpertService) {
    $scope.expert.rules = ExpertService.getRules($routeParams.id);

    $scope.expert.updateRules = function() {
        $scope.dialog.showStandby();
        /* TODO: */
        // ExpertService.update($scope.expert.updateobj);
        $timeout(function() {
            $scope.dialog.hideStandby();
            $scope.message.success("修改自定义规则成功!");
        }, 2000);
    };
}])
.directive("ecgExpertRules", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : expertRulesTemp,
        controller : "ExpertRulesController",
        link : function($scope, $element, $attrs) {
        }
    };
} ])
// 配置接线员
.controller('ExpertOperatorsController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'ExpertService',
    function ($scope, $routeParams, $timeout, $location, EnumService, ExpertService) {
    $scope.expert.operators = null;

    function refreshLinks() {
        ExpertService.getOperators($routeParams.id).then(function(operators) {
            $scope.expert.operators = operators;
        }, function() {
            $scope.message.error("加载接线员数据失败!");
        });
    }
    refreshLinks();

    $scope.expert.check = function(operator) {
        if (operator.removed === true) {
            operator.removed = false;
            return;
        } else {
            operator.removed = true;
        }
    };

    $scope.expert.isCheckAll = false;
    $scope.expert.checkAll = function() {
        $scope.expert.isCheckAll = !$scope.expert.isCheckAll;
        $($scope.expert.operators).each(function(i, operator) {
            operator.removed = $scope.expert.isCheckAll;
        });
    };

    $scope.expert.addOperators = function() {
        $scope.operatordialog.show({
            excludes: $scope.expert.operators,
            handler: function(operators) {
                var len = operators.length, count = 0;
                $(operators).each(function(i, operator) {
                    $scope.dialog.showStandby();
                    ExpertService.linkOperator($routeParams.id, operator)
                    .then(function(flag) {
                        $scope.dialog.hideStandby();
                        if (flag) {
                            count++;
                        } else {
                            $scope.message.error("无法绑定接线员：" + operator.name);
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
    
    $scope.expert.removeOperators = function() {
        
        var removes = [], operators = $scope.expert.operators, len = 0, count = 0;

        $(operators).each(function(i, operator) {
            if (operator.removed) {
                removes.push(operator);
            }
        });
        
        if (removes.length == 0) {
            $scope.dialog.alert({
                text: '请先选择专家!'
            });
            return;
        };

        len = removes.length;

        $scope.dialog.confirm({
            text: "请确认解除绑定, 该操作无法恢复!",
            handler: function() {
                $scope.dialog.showStandby();
                $(removes).each(function(i, remove) {
                    ExpertService.unlinkOperator($routeParams.id, remove)
                    .then(function() {
                        count++;
                        if (count === len) {
                            $scope.dialog.hideStandby();
                            $scope.message.success("成功解除绑定！");
                            refreshLinks();
                        }
                    }, function() {
                        count++;
                        $scope.dialog.hideStandby();
                        $scope.message.error("无法解除绑定，用户名为：" + remove.name);
                    });
                });
            }
        });
    };
}])
.directive("ecgExpertOperators", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : expertOperatorsTemp,
        controller : "ExpertOperatorsController",
        link : function($scope, $element, $attrs) {
        }
    };
}])
.controller('ExpertDialogController', 
    ['$scope', '$filter', '$timeout', '$location', 'EnumService', 'ExpertService', 
    function ($scope, $filter, $timeout, $location, EnumService, ExpertService) {

    // 命名空间
    $scope.expertdialog = {};
    // 只能单选
    $scope.expertdialog.single = true;

    // 表格展示
    $scope.expertdialog.data = null;
    function refreshGrid(params) {
        ExpertService.queryAll(params).then(function(experts) {
            $scope.expertdialog.data = experts;
        });
    };
    refreshGrid();

    $scope.expertdialog.select = function(item) {
        if ($scope.expertdialog.single) {
            $($scope.expertdialog.data).each(function(i, expert) {
                expert.selected = false;
            });
        }
        item.selected = !item.selected;
    };

    $scope.expertdialog.execute = function() {
        var selecteds = [];
        $($scope.expertdialog.data).each(function(i, expert) {
            if (expert.selected) {
                selecteds.push(expert);
            }
        });
        $scope.expertdialog.hide();
        if ($scope.expertdialog.handler instanceof Function) {
            $scope.expertdialog.handler(selecteds);
        }
    };

    $scope.expertdialog.hide = function(opts) {
      $('#ecgExpertsDialog').modal('hide');
    };

    $scope.expertdialog.show = function(opts) {
      var opts = opts || {}, ids = '', params;

      if (opts.excludes) {
        $(opts.excludes).each(function(i, user) {
            var comma = '';
            if (i != 0) {
                comma = ',';
            }
            ids += comma + user.id;
        });
        if (opts.excludes.length > 0) {
            params = {'id:notIn': ids};
        }
      }

      $scope.expertdialog.handler = opts.handler;
      $('#ecgExpertsDialog').modal('show');
      refreshGrid(params);
    };

}])
.directive("ecgExpertDialog", [ '$location', function($location) {
    return {
        restrict : 'A',
        replace : false,
        template : expertDialogTemp,
        controller : "ExpertDialogController",
        link : function($scope, $element, $attrs) {
        }
    };
}]);


});