/**
 * 登录
 */
layui.use(['form', 'layer'], function () {
    if (window != top)
        top.location.href = location.href;
    let layer = layui.layer; // 获取layer模块
    let form = layui.form;

    if (code === '401') {
        layer.alert("当前会话已过期，请重新登录！", {
            icon: 0,
            title: null,
            btn: [],
            offset: '30px'
        });
    }

    /**
     * 加载验证码
     */
    let loadCode = function() {
        $("#codeDiv").show();
        $("#user-login-code").attr("lay-verify", "required");
        form.render(null, 'validateCode');
        $("#codeImg").attr("src", ctx + "/noNeedLogin/getLoginCode");
    };
    $("#codeImg").click(function () {
        $(this).attr("src", ctx + "/noNeedLogin/getLoginCode?v=" + new Date().getTime());
    });

    form.on('submit(login)', function (data) {
        // 登录表单提交操作
        layer.loadingWithText("正在努力登录...");
        CommonUtil.postAjax(ctx + "/login", data.field, function (response) {
            layer.closeAll("loading");
            if (!response.ok) {
                if (response.data && response.data['needCode']) {
                    loadCode();
                    if ($("#codeImg").attr("src")) {
                        $("#codeImg").click();
                    }
                }
                layer.error(response.msg);
            } else {
                top.location.href = ctx;
            }
        });
    });

});
