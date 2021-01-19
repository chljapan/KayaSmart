var isExists = false;
var url = '';

Date.prototype.Format = function (fmt) {
    var o = {
    "M+": this.getMonth() + 1, //月份
    "d+": this.getDate(), //日
    "H+": this.getHours(), //小时
    "m+": this.getMinutes(), //分
    "s+": this.getSeconds(), //秒
    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
    "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
    }
layui.use(['form','layer'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
	//画面ID
	var projectId = getUrlParam('id');
	url = '../../js/'+ projectId + '.js';
	$.ajax({
	    url: url,
	    type: 'HEAD', 
		async: false, //同步方式
	    error: function () {
	        //file not exists 
	          //  isExists = 0;
	    	console.info("file not exists");
	        },
	        success: function () {
	            //file exists 
	        //console.info("file exists");
	        	isExists = true;
	    		var new_element=document.createElement("script");
	    		new_element.setAttribute("type","text/javascript");
	    		new_element.setAttribute("src", url);
	    		document.body.appendChild(new_element);
	        }
	});
})
	
/**
 * 画面初期化
 */
function initPageInfo() {
	if(isExists){
		setTimeout("initPage()","2000");
	}
}
	
/**
 * 画面初期化
 */
function initAddUpdatePageInfo() {
	if(isExists){
		setTimeout("initAddUpdate()","2000");
	}
}
/**
 * 检索
 * @param name
 * @returns url参数取得
 */
function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg); //匹配目标参数
	if (r != null) return unescape(r[2]);
	return 'undefined'; //返回参数值
}

/**
 * 值校验
 * @param form
 * @param layedit
 */
function checkValues(form, layedit) {
	form.verify({
		required: function (value, item) {
			if (value ==0 ) {
				return '* is required!';
			}
		}, fname: function (value) {
			if (value.length < 4) {
				return 'Length is not less than 4!';
			}
		}, contact: function (value) {
			if (value.length < 4) {
				return 'Length is not less than 4!';
			}
		}
		, phone: [/^(1[3|4|5|7|8]\d{9}){0,1}$/, '11桁の有効な携帯電話番号を入力してください。']
		, email: [/^([a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}){0,1}$/, '有効なメールアドレスを入力してください。']
	    , pass: [/^[\S]{6,12}$/,'パスワードが6-12桁の半角英数を入力してください。']
		, content: function(value){
			layedit.sync(editIndex);
		}
	});
}

/**
 * 将数字转换成EXCEL列字母 
 * Convert from numeric position to letter for column names in Excel
 * @param  {int} n Column number
 * @return {string} Column letter(s) name
 */
function createCellPos(n){
	var ordA = 'A'.charCodeAt(0);
	var ordZ = 'Z'.charCodeAt(0);
	var len = ordZ - ordA + 1;
	var s = "";
	while( n >= 0 ) {
		s = String.fromCharCode(n % len + ordA) + s;
		n = Math.floor(n / len) - 1;
	}
	return s;
}