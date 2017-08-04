// 把生成命名空间的方法绑定在jQuery上
$.namespace = function() {
    var a=arguments, o=null, i, j, d;
    for (i=0; i<a.length; i=i+1) {
        d=a[i].split(".");
        o=window;
        for (j=0; j<d.length; j=j+1) {
            o[d[j]]=o[d[j]] || {};
            o=o[d[j]];
        }
    }
    return o;
};

$.namespace("quartzweb.common");

quartzweb.common =function () {
    return {
        // 初始化信息
        init: function () {
            //alert("init");
        },
        logout: function () {
            $.ajax({
                type: 'POST',
                url: "submitLogout",
                dataType: "text",
                success: function(data) {
                    if("success" == data)
                        location.href = "index.html";
                    else {
                        alert("error");
                    }
                }

            });
        },
        /**
         * 根据序列,创建HTML头部(header)信息
         * 序列用户激活active属性
         */
        createHeaderHTML: function (index) {
            $.get('header.html', function (html) {
                $(document.body).prepend(html);
                $("#navbar-main li").eq(index).addClass("active");

            }, "html");
        },
        /**
         * 创建HTML脚部(footer)信息
         */
        createFooterHTML: function () {
            $.get('footer.html', function (html) {
                //alert(html);
                $("#footer").html(html);
            }, "html");
        },
        createHTML: function (id, url) {
            $.ajax({
                type: 'GET',
                url: url,
                async: false,
                dataType: "html",
                success: function (html) {
                    $("#"+id).html(html);
                }
            });
        },
        //获取URL中的某个参数
        getUrlVar: function (url, name) {
            var vars = {};
            var parts = url.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
                vars[key] = value;
            });
            return vars[name];
        },
        getWindowhrefVar: function (name) {
            var vars = {};
            var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
                vars[key] = value;
            });
            // URL解码
            vars[name] = decodeURI(vars[name]);
            return vars[name];
        },

    };
}();

$(document).ready(function() {
    quartzweb.common.init();
});


