$(function() {
    var PATH = window.location.pathname.slice(0, window.location.pathname.lastIndexOf("/"));
    function login() {
       var username = $("#username").val(),
            password = $("#password").val();
        if (!username || !password) {
            alert('请输入用户名或者密码');
            return;
        }
        $.cookie('AiniaOpUsername', encodeURI(username), { expires: 7, path: '/' });

        $('#loginBtn').hide();
        $('#loading').show();
        $.ajax({
            url: PATH + '/api/auth',
            data: {
                'username': username,
                'password': password
            },
            type: 'POST',
            dataType: 'json'
        }).then(function(res) {
            $.cookie('AiniaOpAuthToken', encodeURI(res.token), { expires: 1, path: '/' });
            window.location.href = "index.html";
        }, function() {
            alert('用户名或密码不对,请重新尝试!');
            $('#loginBtn').show();
            $('#loading').hide();
        });
    }
    $("#password").keydown(function(e) {
        if(e.keyCode==13){
            login();
        }
    });
    $("#loginBtn").click(function() {
        login();
    });
    // restore the cookie
    $("#username").val($.cookie('AiniaOpUsername') || '');
});