define(function(require, exports) {

'use strict';

angular.module('ecgTaskService', [])
    .factory("TaskService", ['$rootScope', '$http', '$q', function($rootScope, $http, $q) {
        return {
            queryAllTaskByEmployee: function(user, opts) {
                var opts = opts || {}, url, params = '?';

                url = "/api/task";
                
                if (user.roles === 'operator') {
                    url = "/api/operator/" + user.id + '/task';
                } else if (user.roles === 'expert') {
                    url = "/api/expert/" + user.id + '/task';
                }

                if (opts.status === 'undone') {
                    if (user.roles === 'operator') {
                        params += 'status=pending';
                    } else if (user.roles === 'expert') {
                        params += 'status=proceeding'
                    } else {
                        params += 'status:ne=completed';
                    }
                } else if (opts.status === 'done') {
                    params += 'status=completed';
                } else if (opts.status){
                    params += 'status=' + opts.status;
                }

                if (opts.id) {
                    params += '&id=' + opts.id;
                }

                var paging = {};
                paging['page.max'] = typeof opts['page.max'] === 'undefined' ? 15 : opts['page.max'];
                paging['page.curPage'] = opts['page.curPage'] || 1;

                return $http({
                    method: 'GET',
                    url: PATH + url + params + '&' + $.param(paging)
                }).then(function(res) {
                    if (res.data) {
                        return res.data;
                    } else {
                        return null;    
                    }
                }, function() {
                    return null;
                });
            },
            queryAllTaskByUser: function(user, opts) {
                var opts = opts || {}, url, params = '?', id;

                id = user.id || user;

                var paging = {};
                paging['page.max'] = opts['page.max'] || 15;
                paging['page.curPage'] = opts['page.curPage'] || 1;

                url = "/api/user/" + id + "/task";

                return $http({
                    method: 'GET',
                    url: PATH + url + params + '&' + $.param(paging)
                }).then(function(res) {
                    if (res.data) {
                        return res.data;
                    } else {
                        return null;    
                    }
                }, function() {
                    return null;
                });
            },
            get: function(id) {
                return $http({
                    method: 'GET',
                    cache: false,
                    url: PATH + '/api/task/' + id
                }).then(function(res) {
                    return res.data;
                }, function() {
                    return null;
                });
            },
            getExamination: function(id) {
                return $http({
                    method: 'GET',
                    cache: false,
                    url: PATH + '/api/examination/' + id
                }).then(function(res) {
                    return res.data;
                }, function() {
                    $rootScope.message.error('服务器异常,无法获取标识为' + id + '的数据.');
                    return null;
                });
            },
            getPlainReply: function() {
                return {
                    result: "",
                    content: "",
                    reason: "reason",
                    level: "success"
                };
            },
            reply: function(examination, reply) {
                var newreply = this.getPlainReply(), promise;

                newreply.result = reply.result;
                newreply.content = reply.content;
                newreply.reason = reply.reason;
                newreply.level = reply.level;

                if (!reply.id) {
                    promise = $http({
                        method: 'POST',
                        headers:{'Content-Type':'application/x-www-form-urlencoded'},
                        data: $.param(newreply),
                        url: PATH + '/api/examination/' + examination.id + '/reply'
                    }).then(function(res) {
                        if (res.status === 201) {
                            return true;
                        } else {
                            return false;
                        }
                    }, function() {
                        return false;
                    });
                } else {
                    if (reply.removed) {
                        promise = $http({
                            method: 'DELETE',
                            url: PATH + '/api/reply/' + reply.id
                        }).then(function(res) {
                            return true;
                        }, function() {
                            return false;
                        });
                    } else {
                        promise = $http({
                            method: 'PUT',
                            headers:{'Content-Type':'application/x-www-form-urlencoded'},
                            data: $.param(newreply),
                            url: PATH + '/api/reply/' + reply.id
                        }).then(function(res) {
                            return true;
                        }, function() {
                            return false;
                        });
                    }
                }
                return promise;
            },
            queryExpertsByOperators: function(user) {
                return $http({
                    method: 'GET',
                    url: PATH + '/api/expert'
                }).then(function(res) {
                    if (res.data.datas && res.data.datas.length > 0) {
                        return res.data.datas;
                    } else {
                        return [];    
                    }
                }, function() {
                    $rootScope.message.error('服务器异常,无法获取数据');
                    return [];
                });
            },
            replyInBatch: function(examination, replies) {
                var posts = [], that = this;
                $(replies).each(function(i, reply) {
                    posts.push(that.reply(examination, reply));
                });

                return $q.all(posts).then(function(responses) {
                    var allsuccess = true;
                    $(responses).each(function(i, result){
                        if (!result) {
                            allsuccess = false;
                        }
                    });
                    return allsuccess;
                });
            },
            forward: function(task) {
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param({
                        status: 'proceeding'
                    }),
                    url: PATH + '/api/task/' + task.id
                }).then(function(res) {
                    return true;
                }, function() {
                    return false;
                });
            },
            complete: function(task) {
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param({
                        status: 'completed'
                    }),
                    url: PATH + '/api/task/' + task.id
                }).then(function(res) {
                    return true;
                }, function() {
                    return false;
                });
            },
            getReplyByExamination: function(examination) {
                var id = examination.id || examination;
                return $http({
                    method: 'GET',
                    url: PATH + '/api/examination/' + id + '/reply'
                }).then(function(res) {
                    if (res.data && res.data.length > 0) {
                        return res.data;
                    } else {
                        return [];    
                    }
                }, function() {
                    $rootScope.message.error('服务器异常,无法获取数据');
                    return [];
                });
            },
            remove: function(task) {
                return $http({
                    method: 'DELETE',
                    url: PATH + '/api/user/' + task.userId + '/task/' + task.id
                });
            },
            removeUserTasks: function(task) {
                return $http({
                    method: 'DELETE',
                    url: PATH + '/api/user/' + task.userId + '/task'
                });
            }
        };
    }]);
});