layui.config({
    base: ctx + '/static/plugin/layui/module/'
}).extend({
    treetable: 'treetable-lay/treetable',
    xmSelect: 'xm-select/xm-select',
    dtree: 'dtree/dtree'
})
layui.use(["layer", "table"], function () {
    let layer = layui.layer;
    let table = layui.table;

    $.extend(table.config, {
        limit: 20,
        limits: [20, 100, 200],
        autoSort: false
    })
    $.extend(layer, {
        _defultOption: {
            success: {
                time: 1000,
                icon: 1
            },
            warning: {
                time: 3000,
                icon: 3
            },
            error: {
                icon: 2
            },
            loadingWithText: {}

        },
        /**
         * 成功提示
         */
        success: function (msg, option) {
            let opt = $.extend(this._defultOption.success, option);
            this.msg(msg, opt);
        },
        /**
         * 错误提示，需要手动关闭
         */
        error: function (msg, option) {
            let opt = $.extend(this._defultOption.error, option);
            this.msg(msg, opt);
        },
        warning: function (msg, option) {
            let opt = $.extend(this._defultOption.warning, option);
            this.msg(msg, opt);
        },
        /**
         * 带文字的加载提示
         */
        loadingWithText: function (msg, option) {
            option = option || {
                icon: 16,
                shade: 0.01
            };
            this.msg(msg, option);
        },
        getElementLayerIndex: function (element) {
            return element.closest("div.layui-layer").prop("id").replace("layui-layer", "");
        }
    });
});
