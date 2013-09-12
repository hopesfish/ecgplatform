$(function() {
    var PATH = window.location.pathname.slice(0, window.location.pathname.lastIndexOf("/"));

    function toggle(flag) {
        if (flag) {
            $("#retakePwd").show();
            $("#sendCode").hide();
        } else {
            $("#to").text("");
            $("#password").val("");
            $("#confirmPassword").val("");
            $("#code").val("");
            $("#retakePwd").hide();
            $("#sendCode").show();
        }
    }

    if (window.location.hash === '#byMobileTab') {
        $('.nav-tabs a:last').tab('show');
    }

    $('a[data-toggle="tab"]').on('shown', function (e) {
        var tab = $(e.target).attr("tab");
        window.location.hash = tab;
    });

    $(".sendEmailBtn").click(function() {
        var email = $("#email").val();
        if (!/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(email)) {
            alert("请输入正确邮件格式. 例如: user@ainia.com");
            return;
        }
        $.ajax({
            url: PATH + '/api/user/password/retake?email=' + email,
        }).then(function(res) {
            toggle(true);
            $("#to").text(email);
        }, function() {
            toggle(false);
            alert('无法发送验证码！您提供的邮件格式不对或者没有这个邮箱对应的账号。');
        });
    });

    $(".sendMobileBtn").click(function() {
        var mobile = $("#mobile").val();
        if (!/^1\d{10}$/.test(mobile)) {
            alert("请输入正确邮件格式. 例如: 13812345678");
            return;
        }
        $.ajax({
            url: PATH + '/api/user/password/retake?mobile=' + mobile,
        }).then(function(res) {
            toggle(true);
            $("#to").text(mobile);
        }, function() {
            toggle(false);
            alert('无法发送验证码！您提供的手机号码不对或者没有这个账号。');
        });
    });

    $(".preBtn").click(function() {
        toggle(false);
    });

    $(".retakeBtn").click(function() {
        var password = $("#password").val();
        var confirmPassword = $("#confirmPassword").val();
        var code = $("#code").val();

        if (!password || !confirmPassword || !code) {
            alert("请输入所有信息！");
            return;
        }

        if (password.length < 6) {
            alert("密码长度至少为6位。");
            return;
        }

        if (password !== confirmPassword) {
            alert("两次输入密码不一致！");
            return;
        }

        $.ajax({
            url: PATH + '/api/user/password/retake',
            type: 'POST',
            data: {
                email: $("#email").val(),
                mobile: $("#mobile").val(),
                code: code,
                newPassword: password,
            }
        }).then(function(res) {
            alert('重置成功');
            window.location.href = "ulogin.html";
        }, function() {
            alert('重置失败!');
        });
    });
});