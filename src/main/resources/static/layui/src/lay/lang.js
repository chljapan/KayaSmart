/**
 * cookie操作
 */
var getCookie = function(name, value, options) {

    if (typeof value != 'undefined') { // name and value given, set cookie
        options = options || {};
        if (value === null) {
            value = '';
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
        }
        var path = options.path ? '; path=' + options.path : '';
        var domain = options.domain ? '; domain=' + options.domain : '';
        var s = [cookie, expires, path, domain, secure].join('');
        var secure = options.secure ? '; SameSite=None; Secure ' : '';
        var c = [name, '=', encodeURIComponent(value)].join('');
        var cookie = [c, expires, path, domain, secure].join('')
        document.cookie = cookie;
    } else { // only name given, get cookie
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                // Does this cookie string begin with the name we want?
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};

/**
 * 获取浏览器语言类型
 * @return {string} 浏览器国家语言
 */
var getNavLanguage = function(){
    var navLanguage = navigator.language || navigator.browserLanguage;
    return navLanguage.substr(0,2);
}

/**
 * 设置语言类型： 默认为中文
 */
var i18nLanguage = "zh-CN";

/*
设置一下网站支持的语言种类
 */
var webLanguage = ['zh-CN', 'ja-JP', 'en-US'];

/**
 * 执行页面i18n方法
 * @return
 */ 
var execI18n = function(){
     
    //首先获取用户浏览器设备之前选择过的语言类型
    if (getCookie("selectedLanguage")) {
       i18nLanguage = getCookie("selectedLanguage");
    } else {
        // 获取浏览器语言
        var navLanguage = getNavLanguage();
      
        if (navLanguage) {
            // 判断是否在网站支持语言数组里
            var charSize = $.inArray(navLanguage, webLanguage);
            if (charSize > -1) {
                i18nLanguage = navLanguage;
                // 存到缓存中
                getCookie("selectedLanguage",navLanguage);
            };
        } else{
            console.log("not navigator");
            return false;
        }
    }
    
    /* 需要引入 i18n 文件*/
    if ($.i18n == undefined) {
        console.log("请引入i18n js 文件")
        return false;
    };

   /*
        这里需要进行i18n的翻译
    */
    $.i18n.properties({          
        name : 'messages', //资源文件名称
        path :  '/i18n/', //资源文件路径
        mode : 'map', //用Map的方式使用资源文件中的值
        language : i18nLanguage,
        callback : function() {//加载成功后设置显示内容
//
//             var i18nEle =$(":contains(i18nT.)");
//             i18nEle.each(function() {
//                // 包含【i18nT.】的元素的国际化替换
//                if($(this)[0].childElementCount==0 && $(this)[0].innerText.indexOf("i18nT.")==0){
//                	$(this).text(i18nValue($(this)[0].innerText));
//                }
//            });

             // 第二类：layui的i18n
             var insertEle = jQuery(".i18n"); // 获得所有class为i18n的元素
             insertEle.each(function() {  // 遍历，根据i18n元素的 name 获取语言库对应的内容写入
            	   jQuery(this).html(jQuery.i18n.prop(jQuery(this).attr('name')));
                });
        }
    });
}

//初始化i18n函数
function i18nValue(msgKey) {
    try {
        return $.i18n.prop(msgKey);
        
    } catch (e) {
        return msgKey;
    }
}   


//取得国际化值
var i18n=function(msgKey){

	//首先获取用户浏览器设备之前选择过的语言类型
    if (getCookie("selectedLanguage")) {
       i18nLanguage = getCookie("selectedLanguage");
    } else {
        // 获取浏览器语言
        var navLanguage = getNavLanguage();
      
        if (navLanguage) {
            // 判断是否在网站支持语言数组里
            var charSize = $.inArray(navLanguage, webLanguage);
            if (charSize > -1) {
                i18nLanguage = navLanguage;
                // 存到缓存中
                getCookie("selectedLanguage",navLanguage);
            };
        } else{
            console.log("not navigator");
            return false;
        }
    }
 	//初始化i18n插件
	$.i18n.properties({
        path: '/i18n/',//这里表示访问路径
        name: 'messages',//文件名开头
        language: i18nLanguage,//文件名语言 例如en_US
        encoding: 'UTF-8',
        mode: 'both'//默认值
    });
 
    try {
        return $.i18n.prop(msgKey);
        
    } catch (e) {
        return msgKey;
    }

}

/*页面执行加载执行*/
$(function(){
    /*执行I18n翻译*/
    execI18n();

    /*将语言选择默认选中缓存中的值*/
    $("#locale option[value="+i18nLanguage+"]").attr("selected",true);

    /* 选择语言 */
    $("#locale").on('change', function() {
        var language = $(this).children('option:selected').val()
        //console.log(language);
        getCookie("selectedLanguage",language,{
            expires: 30,
            path:'/'
        });
        
        //画面reload前，将选择的顶部和左侧导航的选择值存入session
        var layuiThis = $(".layui-this");
		var navBarMenu=[];
        layuiThis.each(function() {       	
        	if($(this).attr("lay-id") == undefined && $(this).hasClass("layui-this") && !$(this).hasClass("layui-nav-child")){
					if($(this).find("cite").text() != ""){
						var cur = {"title":$(this).find("cite").text(),
						"dataMenu":$(this).attr("data-menu") !=undefined ? $(this).attr("data-menu"):"",
						"type":$(this).parents(".mobileTopLevelMenus").length != 0 ?"mobile":"pc"}
						navBarMenu.push(cur);
					}
				}    
        });
    	window.sessionStorage.setItem("navBarMenu",JSON.stringify(navBarMenu));     
        window.sessionStorage.setItem("cache","true");

        location.reload();

    });
});


