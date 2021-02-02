/**
 * 全局变量
 */
/** 表头全局变量 */
var G_ColumnsMap = new Map();
/** 各模型主键全局变量 */
var G_BusinessKeyListMap = new Map();
/** 各画面记录主键全局变量 */
var G_BusinessKeyMap = new Map();
/** BusinessSubKey */
var G_BusinessSubkeyListMap = new Map();
/** 父页面选择行信息 */
var G_DataRowListMap = new Map();
var G_SelectDataRowIndexMap = new Map();
/** OrientationKey 多表关联信息 */
var G_Orientationkey = new Map();
/** workflow key关联信息 */
var G_WorkflowBusinessKey = new Map();
/** 登录用户信息 */
var loginUserInfo = parent.loginUserInfo;

/**
 * 加载通用模块信息（tablePlug）
 */
layui.config({
	base : '../../layui/plug/'
}).extend({
	selectM: 'layui_extends/selectM',

	tablePlug: 'tablePlug/tablePlug',
	base_ui: 'base_ui/base_ui'
}).use(['layer','form','element','jquery','laydate','tablePlug', 'selectM','base_ui','carousel'],function(){
	$ = layui.jquery;	
	var form = layui.form
	,selectM = layui.selectM
	,laydate = layui.laydate
	,layer = layui.layer // 弹出窗口功能模块
	,element = layui.element //Tab的切换功能，切换事件监听等，需要依赖element模块
	,table = layui.table
	,carousel = layui.carousel;

	// Menu动态加载处理
	$.ajax({
		url : "/kayamenu",
		method : "POST",
		async : false, //改为同步方式
		dataType : 'json',
		success : function(data) {
			var ulHtml = "";
			$('#menutree').html(editTreeHtmlAuto(ulHtml, data.menuTree));
			element.init();
		}
	});

	/**
	 * 校验监听
	 */
	form.verify({
		required: function (value, item) {
			//alert(value);
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
		, phone: [/^1[3|4|5|7|8]\d{9}$/, '手机必须11位，只能是数字！']
		, email: [/^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/, '邮箱格式不对']
		, number_int: function(value){
			if( value != "" && (isNaN(value) || value.indexOf(".")>=0 || value != parseInt(value))) return 'Please enter the integer numbers.';
		}
		, number_float: function(value){
			if( value != "" && isNaN(value)) return 'Please enter the numbers.';
		}
		, maxlength:function (value, item) {
			if ( parseInt(item.maxLength)>0 && value.replace(/[^\x00-\xff]/g,'**').length> parseInt(item.maxLength)) {
				return 'Length is longer than ' + item.maxLength + ' bits!';
			}
		}
		,regexp:function (value,item){
			strRegexps=item.pattern.split("]:[");
			reg1=eval(strRegexps[0].substring(1));
			if(value !="" && !reg1.test(value)){
				return strRegexps[1].substring(0,strRegexps[1].length-1);
			}
		}
	});

	//
	/**
	 * 菜单监听处理
	 * 当点击有site-demo-active属性的标签时，
	 * 即左侧菜单栏中内容 ，触发点击事件
	 */
	$('.site-demo-active').on('click', function () {
		var dataid = $(this);
		var kayaModelId = dataid.attr("data-id");
		var url = dataid.attr("data-url");
		var title = dataid.attr("data-title");
		var wfType = dataid.attr("data-wftype");
		var column = [];
		var businessSubKeys = [];
		var businessKeys = [];
		var buttonItems=[];

		toolbar = getToolbar(kayaModelId,title,wfType);

		// 取得初期的行信息，子表迁移按钮信息
		getInitColumnsAuto(kayaModelId,column,buttonItems,businessKeys,businessSubKeys);
		//这时会判断右侧.layui-tab-title属性下的有lay-id属性的li的数目，即已经打开的tab项数目
		if ($(".layui-tab-title li[lay-id]").length <= 0) {
			//如果比零小，则直接打开新的tab项
			active.tabAdd(url, kayaModelId, title, buttonItems, column,"","",wfType);
		} else {
			//否则判断该tab项是否以及存在
			var isData = false; //初始化一个标志，为false说明未打开该tab项 为true则说明已有
			$.each($(".layui-tab-title li[lay-id]"), function () {
				//如果点击左侧菜单栏所传入的id 在右侧tab项中的lay-id属性可以找到，则说明该tab项已经打开
				if ($(this).attr("lay-id") == dataid.attr("data-id")) {
					isData = true;
					//切换到指定Tab项
					element.tabChange('home-tabs', kayaModelId); //根据传入的id传入到指定的tab项
					return;
				}
			});

			if (!isData) {
				active.tabAdd(url, kayaModelId, title, buttonItems, column,"","",wfType);
			} else {
				return;
			}
		}

		form.render('select');
		table.init(kayaModelId, {
			smartReloadModel: true,
			toolbar: toolbar,
			// 避免表头被刷新，需要增加page分页参数
			page: {
				limit: 12
			},
			limits: [2, 5, 12]
		});

		/* 监听头工具栏事件*/
		table.on('toolbar('+ kayaModelId + ')', function(obj){
			var checkStatus = table.checkStatus(obj.config.id);
			var data = checkStatus.data; //获取选中的数据
			switch(obj.event){
			case 'add':// 追加
				addRow(kayaModelId,title,false);
				break;
			case 'update':// 更新
				addRow(kayaModelId,title,true);
				break;
			case 'delete':// 删除
				deleteRow(kayaModelId,data,title);
				break;
			};
		});

		//最后不管是否新增tab，最后都转到要打开的选项页面上
		active.tabChange(kayaModelId);

	});

	/**
	 * Tab右键菜单监听
	 */
	$(".rightmenu li").click(function () {
		//当前的tabId
		var currentTabId = $(this).attr("data-id");
		if ($(this).attr("data-type") == "closeOthers") { //关闭其他
			var tabtitle = $(".layui-tab-title li");
			$.each(tabtitle, function (i) {
				if ($(this).attr("lay-id") != currentTabId) {
					active.tabDelete($(this).attr("lay-id"))
				}
			});
		} else if ($(this).attr("data-type") == "closeAll") { //关闭全部
			var tabtitle = $(".layui-tab-title li");
			$.each(tabtitle, function (i) {
				active.tabDelete($(this).attr("lay-id"))
			})

		} else if ($(this).attr("data-type") == "refresh") { //刷新页面
			active.tabRefresh($(this).attr("data-id"));

		} else if ($(this).attr("data-type") == "closeRight") { //关闭右边所有
			//找到当前聚焦的li之后的所有li标签 然后遍历
			var tabtitle = $(".layui-tab-title li[lay-id=" + currentTabId + "]~li");
			$.each(tabtitle, function (i) {
				active.tabDelete($(this).attr("lay-id"))
			})
		}

		$('.rightmenu').hide();
	});

	/**
	 * Tab 绑定事件
	 */
	var element = layui.element;
	active = {
			//在这里给active绑定几项事件，后面可通过active调用这些事件
			//新增一个Tab项 传入三个参数，分别是tab页面的地址，还有一个规定的id，对应其标题，是标签中data-id的属性值
			tabAdd: function(url, kayaModelId, name, buttonItems, column,parentKayaModelId,parentTitle,wfType) {
				

				//关于tabAdd的方法所传入的参数可看layui的开发文档中基础方法部分
				var html = '<iframe tab-id="'+kayaModelId+'" frameborder="0" src="'+url+'" scrolling="yes" class="x-iframe"></iframe>';
				var kayaModelIdTab = kayaModelId;
				var searchDate = [];
				var searchPulldown = [];
				var isWorkflowFlg = false;
				// UI 编辑基础方法调用
				var base_ui = layui.base_ui;

				// KayaModel选项的场合
				if (kayaModelId!=undefined) {
					//+++++++++++++++++++++++++++重新设定TabId Start+++++++++++++++++++++++++++++++++++++++++++++
					kayaModelIdTab = kayaModelId;
					//+++++++++++++++++++++++++++重新设定TabId End+++++++++++++++++++++++++++++++++++++++++++++++
					//+++++++++++++++++++++++++++主画面生成+++++++++++++++++++++++++++++++++++++++++++++
					html = "<div name=\"" + name + "\" tab-id='" + kayaModelId + "' class=\"x-body\" id=\"middle\">" +
					"	<div class=\"layui-row\">";

					// 子表头部处理
					if (parentKayaModelId != "") {
						//+++++++++++++++++++++++++++++++++++++++++++详细信息开始++++++++++++++++++++++++++++++++++++++++++++
						html = html + 
						"		<fieldset id=\"parent_" + kayaModelId + "\"class=\"layui-elem-field\">" +
						"		<legend><span class=\"layui-bg-cyan\"  style=\"font-size:16px;\">" + parentTitle + "</span></legend>" +
						"			<div class=\"layui-collapse\" lay-filter=\"test\">" +
						"				<div class=\"layui-colla-item\"><h2 class=\"layui-colla-title\"><span  style=\"color:#009688\">Detailed Information</span></h2>" +
						"				<div class=\"layui-colla-content\">" +
						"				<div class=\"layui-field-box\">" +
						"					<table class=\"layui-table\">" +
						"					    <tbody>";

						var rowData = G_DataRowListMap.get(parentKayaModelId);
						G_Orientationkey.set(kayaModelId,rowData['orientationkey']);
						// 取得父表各元素信息
						var columnsMap = G_ColumnsMap.get(parentKayaModelId);
						var displaText = "";

						var businessKeyText = "";
						var businessKeys_Json = {};

						// UI 编辑处理
						html = base_ui.editeDetailedInfo(columnsMap,rowData,html,businessKeys_Json);

						businessKeys = businessKeys_Json;

						G_BusinessKeyMap.set(kayaModelId,businessKeys_Json);

						html = html + 
						"			</tbody>" +
						"		</table>" +
						"	</div></div></div></div>" +
						"</fieldset>";
					} else {
						//G_FromMenuMap.set(kayaModelId,true);
					}
					//+++++++++++++++++++++++++++++++++++++++++++详细信息结束++++++++++++++++++++++++++++++++++++++++++++

					//+++++++++++++++++++++++++++++++++++++++++++检索条件开始++++++++++++++++++++++++++++++++++++++++++++
					html = html +
					"<form class=\"layui-form\" lay-filter=\"searchForm_" + kayaModelId + "\">		<fieldset class=\"layui-elem-field\">" +
					"		<legend><span class=\"layui-bg-cyan\"  style=\"font-size:16px;\">Search by</span></legend>";
					var i = 0;
					for(x in column){
						if(column[x]['searchFlg']==true){
							i++;
							var startline = '<div class="layui-inline">';
							var endline = '';

							if(i==1){
								startline = '<div class="layui-form-item"><div class="layui-inline">';
							} else if (i%2==1){
								startline = '</div><div class="layui-form-item"><div class="layui-inline">';
							}

							html += startline
							+'  <label class="layui-form-label" style="width:170px">'+column[x]['title']+'</label>';
							var columnsMap = G_ColumnsMap.get(kayaModelId);

							if(column[x]['datatype']=='Master'){

								html +=  "<div class=\"layui-input-inline\" id=\"" + column[x]['field'] +"\">";
								var valueData = [];
								for (var key in column[x]['editor']['options']['data']) {
									if(column[x]['editor']['options']['data'][key].id!='') {
										valueData.push({"id" : column[x]['editor']['options']['data'][key].id,"name" : column[x]['editor']['options']['data'][key].text});
									}
								}
								// 检索部分有Master的时候变成select显示，为了变成多选，要把id和pulldown里面的内容存储下来
								searchPulldown.push({"id" : column[x]['field'], "values" :valueData,"max":valueData.length});		

							} else if (column[x]['datatype']=='Group'){
								html +=  "<div class=\"layui-input-inline\" style=\"width:300px\"><input name = \"" + column[x]['field'] + "\" title=\"全て\" type=\"radio\" checked=\"\" value=\"\"><input name = \"" + column[x]['field'] + "\" title=\"on\" type=\"radio\" value=\"on\"><input name = \"" + column[x]['field'] + "\" title=\"off\" type=\"radio\"  value=\"off\">";					        		
							} else if (column[x]['datatype']=='boolean'){
								html +=  "<div class=\"layui-input-inline\" style=\"width:300px\"><input name = \"" + column[x]['field'] + "\" title=\"全て\" type=\"radio\" checked=\"\" value=\"\"><input name = \"" + column[x]['field'] + "\" title=\"on\" type=\"radio\" value=\"on\"><input name = \"" + column[x]['field'] + "\" title=\"off\" type=\"radio\"  value=\"off\">";					        		
							} else {
								// 根据控件类型生成画面
								switch(column[x]['editor']['type']) {
								case 'dateY':
									html +=  "<div class=\"layui-input-inline\"><input type=\"text\" class=\"layui-input\" name = \"" + column[x]['field'] + "\" id = \"" + column[x]['field'] + "\"/>";
									// 检索部分有Date的时候变成From - to 显示，要把id存储下来
									searchDate.push({"id" : column[x]['field'],"type" : "year","format" : "yyyy"});	
									break;
								case 'dateYM':
									html +=  "<div class=\"layui-input-inline\"><input type=\"text\" class=\"layui-input\" name = \"" + column[x]['field'] + "\" id = \"" + column[x]['field'] + "\"/>";
									// 检索部分有Date的时候变成From - to 显示，要把id存储下来
									searchDate.push({"id" : column[x]['field'],"type" : "month","format" : "yyyy-MM"});	
									break;
								case 'dateYMD':
									html +=  "<div class=\"layui-input-inline\"><input type=\"text\" class=\"layui-input\" name = \"" + column[x]['field'] + "\" id = \"" + column[x]['field'] + "\"/>";
									// 检索部分有Date的时候变成From - to 显示，要把id存储下来
									searchDate.push({"id" : column[x]['field'],"type" : "date","format" : "yyyy-MM-dd"});	
									break;
								case 'dataTime':
									html +=  "<div class=\"layui-input-inline\"><input type=\"text\" class=\"layui-input\" name = \"" + column[x]['field'] + "\" id = \"" + column[x]['field'] + "\"/>";
									// 检索部分有Date的时候变成From - to 显示，要把id存储下来
									searchDate.push({"id" : column[x]['field'],"type" : "datetime","format" : "yyyy-MM-dd HH:mm:ss"});	
									break;
								case 'time':
									html +=  "<div class=\"layui-input-inline\"><input type=\"text\" class=\"layui-input\" name = \"" + column[x]['field'] + "\" id = \"" + column[x]['field'] + "\"/>";
									// 检索部分有Date的时候变成From - to 显示，要把id存储下来
									searchDate.push({"id" : column[x]['field'],"type" : "time","format" : "HH:mm:ss"});	
									break;
								default:
									html += '  <div class="layui-input-inline"><input type="text" class="layui-input" name= "' + column[x]['field'] + '"';
								html += '/>';
								}
							}
							html += '  </div></div>';
						}
					}
					if(i==0){
						html = html + "	<div class=\"layui-form\">" +
						"				<div class=\"layui-input-inline\">" +
						"                	<select id=\"searchkey_" + kayaModelId +"\" lay-verify=\"chapterRender\" style=\"width:80px\">";
						for (x in column) {
							// Hidden项非表示
							html = html +  "	<option value=\"" + column[x]['field'] + "\">" + column[x]['title'] + "</option>";
						}

						html = html +  "	</select>" +
						"               </div>" +
						"				<div class=\"layui-input-inline\">" +
						"					<input type=\"text\" id=\"searchvalue_" + kayaModelId +"\" " +
						"						autocomplete=\"off\" class=\"layui-input\">" +
						"				</div>";
					}

					html = html + 
					"				<input type=\"button\" class=\"layui-btn layui-block\" onclick=\"doSearch(\'" + kayaModelId + "\',\'" + name + "\',false,\'" + wfType + "\')\" value=\"Search\"/></div>" +
					"		</fieldset></form>" +
					"	<div class=\"layui-form-item\"></div>" +
					"</div>" +
					"<xblock id=\"xblock_" + kayaModelId +"\">";
					for (x in buttonItems) {
						// TODO:Tab的Index取得
						html = html + 
						"<button onclick=\"EditDetailedInfo('"+ kayaModelId + "','"  + buttonItems[x]['kayamodelid'] + "','" + buttonItems[x]['title']+ "','" + name + "')\" class=\"layui-btn layui-btn-radius subButton" + kayaModelId + "\">" + buttonItems[x]['title'] + "</button>";
					}
					var toolbar = InitToolbar(wfType);

					html = html + 
					"</xblock>" +
					"<div id=\"tablediv_" + kayaModelIdTab + "\">" +
					"	<table class=\"layui-table\"  id=\""+kayaModelIdTab +"\" lay-data=\"{toolbar:\'#" + toolbar + "\',defaultToolbar:'[]'}\" lay-filter=\""+kayaModelIdTab +"\">";

					//+++++++++++++++++++++++++++++++++++++++++++检索条件结束++++++++++++++++++++++++++++++++++++++++++++

					//+++++++++++++++++++++++++++++++++++++++++++表格编辑开始++++++++++++++++++++++++++++++++++++++++++++
					var levelMap = new Map();
					// 表头UI编辑处理 
					base_ui.editeTableColumns(column,levelMap);
					html = html + "<thead>";

					for (var i = levelMap.size;i>0;i--) {
						if (i ==levelMap.size) {
							html = html + "<tr><th lay-data=\"{type:'numbers'}\" rowspan=\"" + levelMap.size + "\" style=\"width:10px\">No.</th>";

							html = html + "<th lay-data=\"{type:'checkbox'}\" rowspan=\"" + levelMap.size + "\" style=\"width:18px\"></th>";

							html = html + levelMap.get(i) + "</tr>";
						}　else {
							html = html + "<tr>" + levelMap.get(i) + "</tr>";
						}　　　　　　
					}
					html = html + "</thead></table></div></div>";
				}

				//+++++++++++++++++++++++++++++++++++++++++++表格编辑结束++++++++++++++++++++++++++++++++++++++++++++
				element.tabAdd('home-tabs', {
					smartReloadModel: true
					,title: name 
					,content:html

					,page: {
						limit: 12
					}
				,id: kayaModelIdTab
				});

				for(var x in searchPulldown){

					//多选标签-基本配置
					var tagIns1 =  selectM({
						//元素容器【必填】
						elem: '#'+searchPulldown[x].id
						//候选数据【必填】
						,data: searchPulldown[x].values
						,max:searchPulldown[x].max
						,width:400  
					}); 
				}

				for(var x in searchDate){
					laydate.render({
						elem: '#'+searchDate[x].id
						,type: searchDate[x].type
						,range: '～'
							,format: searchDate[x].format
					});
				}
				form.render();

				form.on('switch', function(data) {
					$(data.elem).attr('type', 'hidden').val(this.checked ? 'on':'');

				});
				// 按钮设定
				$(".subButton"+kayaModelId).attr('disabled', true);
				$(".subButton"+kayaModelId).css({"background-color":"grey"});
				//通过鼠标mouseover事件  动态将新增的tab下的li标签绑定鼠标右键功能的菜单
				//下面的json.id是动态新增Tab的id，一定要传入这个id,避免重复加载mouseover数据
				$(".layui-tab-title li[lay-id=" + kayaModelIdTab + "]").mouseover(function () {
					CustomRightClick(kayaModelId); //给tab绑定右击事件
					//FrameWH(); //计算ifram层的大小
				});
				element.init();
				return businessKeys_Json;
			},
			tabChange: function (kayaModelIdTab) {
				//切换到指定Tab项
				element.tabChange('home-tabs', kayaModelIdTab); //根据传入的id传入到指定的tab项
			},
			tabDelete: function (kayaModelIdTab) {
				element.tabDelete('home-tabs', kayaModelIdTab); //删除
			},
			tabRefresh: function (kayaModelIdTab) { //刷新页面
				$("iframe[data-frameid='" + kayaModelIdTab + "']").attr("src", $("iframe[data-frameid='" + kayaModelIdTab + "']").attr("src")) //刷新框架
			}
	};

	// WorkFlow
	var renderDom = function (elem, stepItems, postion) {
		var stepDiv = '<div class="lay-step">';
		for (var i = 0; i < stepItems.length; i++) {
			stepDiv += '<div class="step-item">';
			// 线
			if (i < (stepItems.length - 1)) {
				if (i < postion) {
					stepDiv += '<div class="step-item-tail"><i class="step-item-tail-done"></i></div>';
				} else {
					stepDiv += '<div class="step-item-tail"><i class=""></i></div>';
				}
			}

			// 数字
			var number = stepItems[i].number;
			if (!number) {
				number = i + 1;
			}
			if (i == postion) {
				stepDiv += '<div class="step-item-head step-item-head-active"><i class="layui-icon">' + number + '</i></div>';
			} else if (i < postion) {
				stepDiv += '<div class="step-item-head"><i class="layui-icon layui-icon-ok"></i></div>';
			} else {
				stepDiv += '<div class="step-item-head "><i class="layui-icon">' + number + '</i></div>';
			}

			// 标题和描述
			var title = stepItems[i].title;
			var desc = stepItems[i].desc;
			if (title || desc) {
				stepDiv += '<div class="step-item-main">';
				if (title) {
					stepDiv += '<div class="step-item-main-title">' + title + '</div>';
				}
				if (desc) {
					stepDiv += '<div class="step-item-main-desc">' + desc + '</div>';
				}
				stepDiv += '</div>';
			}
			stepDiv += '</div>';
		}
		stepDiv += '</div>';

		$(elem).prepend(stepDiv);

		// 计算每一个条目的宽度
		var bfb = 100 / stepItems.length;
		$('.step-item').css('width', bfb + '%');
	};

	step = {
			// 渲染步骤条
			render: function (param) {
				param.indicator = 'none';  // 不显示指示器
				param.arrow = 'always';  // 始终显示箭头
				param.autoplay = false;  // 关闭自动播放
				if (!param.stepWidth) {
					param.stepWidth = '600px';
				}

				// 渲染轮播图
				carousel.render(param);

				// 渲染步骤条
				var stepItems = param.stepItems;
				renderDom(param.elem, stepItems, 0);
				$('.lay-step').css('width', param.stepWidth);

				//监听轮播切换事件
				carousel.on('change(' + param.filter + ')', function (obj) {
					$(param.elem).find('.lay-step').remove();
					renderDom(param.elem, stepItems, obj.index);
					$('.lay-step').css('width', param.stepWidth);
				});

				// 隐藏左右箭头按钮
				$(param.elem).find('.layui-carousel-arrow').css('display', 'none');

				// 去掉轮播图的背景颜色
				$(param.elem).css('background-color', 'transparent');
			},
			// 下一步
			next: function (elem) {
				$(elem).find('.layui-carousel-arrow[lay-type=add]').trigger('click');
			},
			// 上一步
			pre: function (elem) {
				$(elem).find('.layui-carousel-arrow[lay-type=sub]').trigger('click');
			}
	};

});

/**
 * 鼠标右键Tab菜单处理
 * @param id MenuId
 * @returns 处理结果
 */
function CustomRightClick(id) {
	//取消右键  rightmenu属性开始是隐藏的 ，当右击的时候显示，左击的时候隐藏
	$('.layui-tab-title li').on('contextmenu', function () {
		return false;
	});
	$('.layui-tab-title,.layui-tab-title li').click(function () {
		$('.rightmenu').hide();
	});
	//单击取消右键菜单
	$('body,#aaa').click(function () {
		$('.rightmenu').hide();
	});
	//tab点击右键
	$('.layui-tab-title li').on('contextmenu', function (e) {
		var popupmenu = $(".rightmenu");
		popupmenu.find("li").attr("data-id", id); //在右键菜单中的标签绑定id属性

		//判断右侧菜单的位置
		l = ($(document).width() - e.clientX) < popupmenu.width() ? (e.clientX - popupmenu.width()) : e.clientX;
		t = ($(document).height() - e.clientY) < popupmenu.height() ? (e.clientY - popupmenu.height()) : e.clientY;
		popupmenu.css({
			left: l,
			top: t
		}).show(); //进行绝对定位
		return false;
	});
}

/**
 * 递归处理树状菜单
 * @param ulHtml
 * @param nodes
 * @returns
 */
function editTreeHtmlAuto(ulHtml,nodes) {

	var liHtml="<li class=\"layui-nav-item layui-nav-itemed\">";
	var dlHtml = "<dl clsss=\"layui-nav-child\">";
	for(var x = 0; x < nodes.length; x++) {
		if(nodes[x]['children'] !=undefined) {
			var _liHtml = "<li class=\"layui-nav-item\">";
			var _aHtml = "<a href=\"javascript:void(0);\">";
			var _iHtml = "<i class=\"iconfont\" style=\"10px;color:#009688;\">&#xe6b4;</i>";
			var spanHtml="<span>" + nodes[x]['text'] + "</span>";
			var dl_1Html = "<dl class=\"layui-nav-child layui-nav-itemed\">";
			dl_1Html = editTreeHtmlAuto(dl_1Html, nodes[x]['children']) + "</dl>";
			_liHtml =_liHtml + (_aHtml + _iHtml + spanHtml + "</a>" + dl_1Html +"</li>");
			dlHtml = dlHtml + _liHtml;

		} else {
			//var workflowId = nodes[x]['attributes']['WorkFlowId'];
			var _aHtml = "<dd><a class=\"site-demo-active\" data-url=\"admin-role.html\" data-id=\"" + 
			nodes[x]['id'] + "\" data-title=\"" + nodes[x]['text'] + "\" data-wftype=\"" + nodes[x]['wftype'] + "\" data-type=\"tabAdd\" href=\"javascript:void(0);\">" + nodes[x]['text'] +"</a></dd>";
			dlHtml=dlHtml + _aHtml;
			if(nodes[x]['iconCls'] == 'WorkFlow') {
				var _aHtml = "<dd><a class=\"site-demo-active\" data-url=\"admin-role.html\" data-id=\"" + 
				"mg-" + nodes[x]['id'] + "\" data-title=\"" + nodes[x]['text'] + "(Manager)" + "\"data-type=\"tabAdd\" href=\"javascript:void(0);\">" + nodes[x]['text'] + "(Manager)" +"</a></dd>";
				dlHtml=dlHtml + _aHtml;
			}
		}
	}

	liHtml = liHtml + dlHtml + "</dl></li>";
	ulHtml = ulHtml + liHtml ;
	return ulHtml;
}

/**
 * 取得初期的行信息，子表迁移按钮信息
 * @param kayaModelId
 * @param column 表头信息
 * @param buttonItems 子表迁移按钮信息
 * @returns
 */
function getInitColumnsAuto(kayaModelId,column,buttonItems,businessKeys,businessSubKeys){
	$.ajax({
		url : "/kayainit",
		method : "POST",
		async: false, //改为同步方式
		data : {
			'kayaModelId' : kayaModelId,
			'searchUser' : "m1"
		},
		success : function(data) {
			_column = data.columnsList;
			// 拿到主键
			businessSubKeys = data.businessSubKeyList;
			businessKeys = data.businessKeyList;

			// 搜索栏初期表示处理
			for (x in _column) {
				// Hidden项非表示
				if (_column[x]['role']==false) {
					column.push(_column[x]);
					// 对Role进行显示编辑(子画面Link)
				} else {
					buttonItems.push(_column[x]);
				}
			} 
		}
	});

	G_ColumnsMap.set(kayaModelId,column);
	G_BusinessSubkeyListMap.set(kayaModelId,businessSubKeys);
	G_BusinessKeyListMap.set(kayaModelId,businessKeys);
}

/**
 *  子表Tab选项卡追加处理
 * @param index
 * @param parentKayaModelId
 * @param kayaModelId
 * @param title
 * @returns
 */
function EditDetailedInfo(parentKayaModelId,kayaModelId,title,parentTitle) {
	// 父类数据为空时不处理
	if (!G_DataRowListMap.has(parentKayaModelId)) {
		//alert("请选择数据");
		return;
	} else {
		if (G_DataRowListMap.has(kayaModelId)) {
			G_DataRowListMap.delete(kayaModelId);
			G_SelectDataRowIndexMap.delete(kayaModelId);
		}
	}
	//++++++++++++++++++++++++++++++++个别画面检索结果操作按钮（toolbar）设定 Start++++++++++++++++++++++++++++++++++++
	toolbar = getToolbar(kayaModelId,title,'');
	//++++++++++++++++++++++++++++++++个别画面检索结果操作按钮（toolbar）设定 End++++++++++++++++++++++++++++++++++++++
	var column = [];// 列明数组
	var buttonItems=[];// 按钮数组
	var businessSubKeys =[];
	var businessKeys = [];
	getInitColumnsAuto(kayaModelId,column,buttonItems,businessKeys,businessSubKeys);
	var isData = false;

	kayaModelIdTab = kayaModelId;

	//这时会判断右侧.layui-tab-title属性下的有lay-id属性的li的数目，即已经打开的tab项数目
	if ($(".layui-tab-title li[lay-id]").length > 0) {
		//初始化一个标志，为false说明未打开该tab项 为true则说明已有
		$.each($(".layui-tab-title li[lay-id]"), function () {
			//如果点击左侧菜单栏所传入的id 在右侧tab项中的lay-id属性可以找到，则说明该tab项已经打开
			if ($(this).attr("lay-id") == kayaModelIdTab) {
				isData = true;
				active.tabChange(kayaModelIdTab);
				return;
				// alert("有问题");
			}
		});
	}

	var businessKeysData;
	if (!isData) {
		//标志为false 新增一个tab项
		//---------------------------------按钮，表列明编辑---------------------------------------
		businessKeysData= active.tabAdd("", kayaModelId, title, buttonItems,column,parentKayaModelId,parentTitle,"");
		active.tabChange(kayaModelIdTab);
		if(!G_BusinessKeyMap.has(kayaModelId)) {
			G_BusinessKeyMap.push(kayaModelId,businessKeysData);
		}
	}

	// -------Table生成列名处理------------------------------------------------------------------------
	layui.config({base: '../../layui/plug/'})
	.use(['tablePlug', 'laydate'], function () {
		var table = layui.table;
		var form = layui.form;
		form.render('select');
		table.init(kayaModelId, {
			// toolbar:'default'
			smartReloadModel: true
			,page: {
				limit: 12
			}
		,stoolbar:toolbar
		});

		//监听头工具栏事件
		//监听头工具栏事件
		table.on('toolbar('+ kayaModelId + ')', function(obj){
			var checkStatus = table.checkStatus(obj.config.id);
			var data = checkStatus.data; //获取选中的数据
			switch(obj.event){
			case 'add':
				addRow(kayaModelId,title,false);
				break;
			case 'update':
				addRow(kayaModelId,title,true);
				break;
			case 'delete':
				deleteRow(kayaModelId,data,title);
				break;
			};
		});
	});	
	// -------Table生成列名处理------------------------------------------------------------------------
	active.tabChange(kayaModelId);
}
/**
 * 检索
 * @param kayaModelId
 * @returns
 */
function doSearch(kayaModelId,title,isDownload,wfType) {

	var rows = [];
	var kvParamaterList = [];
	if(G_BusinessKeyMap.has(kayaModelId)) {
		kvParamaterList.push(G_BusinessKeyMap.get(kayaModelId));
	} else {
		kvParamaterList.push(eval('({})'));
	}
	layui.config({base: '../../layui/plug/'})
	.use(['tablePlug', 'laydate'], function () {
		var table = layui.table;
		var form = layui.form;
		// 检索条件设定
		var fromValue = JSON.stringify(form.val('searchForm_' + kayaModelId));
		var searchKey=$('#searchkey_' + kayaModelId).val();
		var searchValue=$('#searchvalue_' + kayaModelId).val();
		var columnsMap = G_ColumnsMap.get(kayaModelId);

		// Master项目处理
		for (x in columnsMap) {
			// Hidden项非表示
			if (columnsMap[x]['searchFlg']!=false) {
				if ('combobox' == columnsMap[x]['editor'].type) {

					for(var items in columnsMap[x]['editor'].options.data)
					{
						// 通过Text查询Id 设置显示内容
						if(searchValue != '' && columnsMap[x]['editor'].options.data[items].text.indexOf(searchValue)!=-1){
							searchValue = columnsMap[x]['editor'].options.data[items].id;
						}
					}
				}
			}
		}

		var workflowActionId = $(this).attr("actionid");
		$.ajax({
			url : "/kayaselect",
			method : "POST",
			async: false, //同步方式
			data : {
				'kayaModelId' : kayaModelId,
				'searchname' : searchKey,
				'searchvalue' : searchValue,
				'wftype' : wfType,
				'orientationKey' : G_Orientationkey.get(kayaModelId),
				"searchParamaterList" : '[' + fromValue +']',
				"kvParamaterList" : JSON.stringify(kvParamaterList)
			},
			success : function(data) {
				rows = data.resultDetails;
			}
		});

		if(!isDownload) {
			//toolbar = 'default';
			defaultToolbar = ['filter', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
				title: 'ダウンロード'
					,layEvent: 'download'
						,icon: 'layui-icon-export'
			}, 'print'];
			if(rows.length>0){
				$(".subButton"+kayaModelId).attr('disabled', false);
				$(".subButton"+kayaModelId).css({"background-color":"#009688"});
			} else {
				$(".subButton"+kayaModelId).attr('disabled', true);
				$(".subButton"+kayaModelId).css({"background-color":"grey"});
				//toolbar = '#toolbar1c';
				defaultToolbar = '[]';
			}

			//+++++++++++++++++++++++++++重新设定TabId Start+++++++++++++++++++++++++++++++++++++++++++++
			kayaModelIdTab = kayaModelId;
			//+++++++++++++++++++++++++++重新设定TabId End+++++++++++++++++++++++++++++++++++++++++++++++
			var rest_toolbar = getToolbar(kayaModelId,title,wfType);

			form.render('select');
			table.reload(kayaModelIdTab, {
				smartReloadModel: true,
				data: rows,
				//toolbar:'default',
				toolbar:'#' +rest_toolbar,
				defaultToolbar: defaultToolbar,
				//even: true,
				//totalRow:true,
				page: {
					limit: 12
				},
				limits: [2, 5, 12],
				done: function (res, curr, count) {
					// 设置第一行为默认选择状态，更改背景色以及字体颜色
					Layui_SetDataTableRowColor(kayaModelId, 0, '#2F4056', '#fff');
					// 取得被选择行数据
					G_DataRowListMap.set(kayaModelId,res.data[0]);
//					G_SelectDataRowIndexMap.set(kayaModelId,res.data[0]['LAY_TABLE_INDEX']);
					//alert(JSON.stringify(res.data[0]));
				}
			});


			//监听排序事件 
			table.on('sort('+ kayaModelIdTab + ')', function(obj){ //注：sort 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
				Layui_SetDataTableRowColor(kayaModelId,0, '#2F4056', '#fff');
//				// 设置第一行为默认选择状态，更改背景色以及字体颜色
				//Layui_SetDataTableRowColor('tablediv_' + kayaModelId, 0, '#2F4056', '#fff');
//				G_DataRowListMap.set(kayaModelId,obj.data);
				//G_SelectDataRowIndexMap.set(kayaModelId,G_DataRowListMap.get(kayaModelId)['LAY_TABLE_INDEX']);
				//alert(JSON.stringify(G_DataRowListMap.get(kayaModelId)['LAY_TABLE_INDEX']));

				//Layui_SetDataTableRowColor('tablediv_' + kayaModelId, G_SelectDataRowIndexMap.get(kayaModelId), '#2F4056', '#fff');
				// 取得被选择行数据
				//G_DataRowListMap.set(kayaModelId,res.data[0]);
				//尽管我们的 table 自带排序功能，但并没有请求服务端。
				//有些时候，你可能需要根据当前排序的字段，重新向服务端发送请求，从而实现服务端排序，如：
				table.reload(kayaModelId, {
					done: function (res, curr, count) {
						// 设置第一行为默认选择状态，更改背景色以及字体颜色

						// 取得被选择行数据
						//G_DataRowListMap.set(kayaModelId,res.data[G_SelectDataRowIndexMap.get(kayaModelId)]);
						//G_SelectDataRowIndexMap.set(kayaModelId,res.data[0]['LAY_TABLE_INDEX']);


						alert(JSON.stringify(G_DataRowListMap.get(kayaModelId)));
						alert(JSON.stringify(res.data[0]['LAY_TABLE_INDEX']));
						//G_SelectDataRowIndexMap.set(kayaModelId,res.data[0]['LAY_TABLE_INDEX']);
					}


				});

			});

			//监听行单击事件（双击事件为：rowDouble）
			table.on('row(' + kayaModelIdTab +')', function(obj){
				//alert(JSON.stringify(obj.data));
				// 恢复默认行的背景色以及字体颜色
				Layui_SetDataTableRowColor(kayaModelId, 0, '#fff', '#666');

				// 标注选中样式
				obj.tr.addClass('layui-bg-cyan').siblings().removeClass('layui-bg-cyan');
				// 取得被选择行数据
				G_DataRowListMap.set(kayaModelId,obj.data);
				G_SelectDataRowIndexMap.set(kayaModelId,obj.data['LAY_TABLE_INDEX']);

			});
		} else {			
			var column = G_ColumnsMap.get(kayaModelId);
			var columnDetail={};
			var title={};
			var col = 0;
			var row = rows.length + 1;
			// 用来把头转换成文字，并把字段顺序保留下来
			for (x in column) {
				if(column[x]['hidden']!='true'){
					if(column[x]['datatype']=='Master') {
						title[column[x]['field'] + "_NM"]=column[x]['title'];
						columnDetail[col]=column[x]['field'] + "_NM";
					} else {
						title[column[x]['field']]=column[x]['title'];
						columnDetail[col]=column[x]['field'];
					}
					col++;
				}
			}
			//download
			var bodysArr = new Array();
			// 加表头
			bodysArr.unshift(title);
			for (x in rows) {
				var detail = {};
				for(i in columnDetail) {
					detail[columnDetail[i]]= rows[x][columnDetail[i]];
				}
				bodysArr.push(detail);
			}
			var date = new Date();
			date = date.Format('yyyyMMddHHmmssS');
			// 意思是：列宽120px
			var pos = createCellPos(col-1);
			var colConf = LAY_EXCEL.makeColConfig({'AZ': 120},120);
			// 画表头
			LAY_EXCEL.setExportCellStyle(bodysArr, 'A1:'+pos+'1', {
				s: {
					fill: {bgColor: {indexed: 64}, fgColor: {rgb: "FFFFE0"}},
					alignment: {
						horizontal: 'center',
						vertical: 'center'
					}
				}
			});
			// 画边框
			LAY_EXCEL.setExportCellStyle(bodysArr, 'A1:'+pos+row, {
				s: {
					border: {
						top: {style: 'thin', color: {rgb: 'FF000000'}},
						bottom: {style: 'thin', color: {rgb: 'FF000000'}},
						left: {style: 'thin', color: {rgb: 'FF000000'}},
						right: {style: 'thin', color: {rgb: 'FF000000'}}
					}
				}
			});
			// 3. 执行导出函数，系统会弹出弹框
			LAY_EXCEL.exportExcel({
				list: bodysArr
			}, kayaModelId+'_' + date + '.xlsx', 'xlsx', {
				extend: {
					'!cols': colConf
				}
			});
		}
	});
}
/**
 * 追加更新处理
 * @param kayaModelId
 * @param Title
 * @param isEditFlg 更新Flg(true=编辑, false=追加)
 * @returns
 */
function addRow(kayaModelId,Title,isEditFlg,wfType) {
	//alert(JSON.stringify(businessKeysData));
	var rowData={};
	if (isEditFlg) {
		// 如果存在迁移数据
		rowData = G_DataRowListMap.get(kayaModelId);
		if(rowData.length ==0 || rowData ==null) {
			return;
			// 动态设置题目
		} else {
			// 编辑的情况下没有数据则无动作
			//Title = Title + "-[修改]";
		}
	} else {
		// 动态设置题目
		//Title = Title + "-[追加]";
	}
	layui.config({base: '../../layui/plug/'})
	.use(['laydate'], function () {

		//触发事件
		var actionButton = [];
		var actionButtonId = [];
		var layer = layui.layer;
		var laydate = layui.laydate;
		var contentHtml = " <div class=\"container\">";
		var hi = 0;
		var Readonly="";
		var Required="";
		var isDisplay="";
		//++++++++++++++++++++++++++++++++新规窗口Start++++++++++++++++++++++++++++++++++++

		var jsonBusinessSubkey = {};
		var insertField = "[";
		var dateDetail = []; // 日期类型控件可控显示
		var width = '300px';

		var actionItems = ['0'];
		$.ajax({
			url : "/kayainitwindow",
			method : "POST",
			async: false, //改为同步方式
			data : {
				'kayaModelId' : kayaModelId,
				'actionItems' : JSON.stringify(actionItems)
			},
			success : function(data) {
				var columns = [];
				var center = '';
				var businessKeyList = [];// 主键信息
				var workflowList = []; // 流程信息
				columns = data.labelList; // 表列信息
				businessKeyList = data.businessKeyList;// 取得主键信息
				workflowList = data.workflowList; // 取得流程信息

				// 业务流程的情况添加状态
				if (workflowList.length!=0) {
					contentHtml = contentHtml + 
					" <div style=\"background-color: #eee;\">" +
					"     <div style=\"height:20px\"></div>" +
					"     <header><div class=\"layui-carousel\" id=\"stepForm\" lay-filter=\"stepForm\" style=\"margin: 40 50 50 50;\"></div></header>" +
					"     <div style=\"height:40px\"></div>" +
					" </div>";
				}

				contentHtml = contentHtml + " <main style=\"height:400px;overflow: auto;\">" +
				"<div id=\"" + kayaModelId + "\" class=\"layui-form layui-form-pane\" carousel-item=\"\" lay-filter=\"" + kayaModelId+ "\" style=\"max-width: 500px;border:15px solid #fff;top:0;margin: 10px 0px 10px 140px;\">";

				// 入力内容编辑
				var base_ui = layui.base_ui;
				var _contentHtml = "";
				_contentHtml = base_ui.editAddRows(columns,_contentHtml,isEditFlg,rowData,jsonBusinessSubkey);
				contentHtml = contentHtml +_contentHtml;
				contentHtml = contentHtml +"</main><div align=\"right\" style=\"border:15px solid #fff;bottom:0;left:50;\"><footer style=\"height:70px;background-color: #eee;\"> <div style=\"height:15px\"></div>";
				insertField = insertField + "]";
				// 按钮内容编辑
				// 流程元素处理
				var buttonItems = "";
				var propertyItem= "";
				if (isEditFlg) {
					contentHtml = contentHtml + "<button class=\"layui-btn\" lay-submit id=\"update\">Update</button>";
				} else {			
					for (x in workflowList) {					
						switch(workflowList[x]['editor'])
						{
						case 'Action':
							buttonItems = buttonItems + "<button class=\"layui-btn\" top=\"50%\" lay-submit=\"\" actionid=\"insert\"  id=\"" + workflowList[x]['kayamodelid'] + "\">" + workflowList[x]['label'] + "</button>";
							break;
						case 'Property':
							propertyItem = propertyItem + "<div class=\"layui-form-item\">" +
							"<label class=\"layui-form-label\">" + workflowList[x]['label']  + "</label>" +
							"<div class=\"layui-input-inline\">" +
							"<input type=\"text\" id=\"" + workflowList[x]['field'] + "\" name=\"" + workflowList[x]['field'] + "\" required=\"\"" +
							"autocomplete=\"off\" class=\"layui-input\">" +
							" </input></div>" +
							"</div>";
							break;
						default:
							//inputCenter = inputCenter + "easyui-textbox";
						}
					}

				}

				// 没有绑定WorkFlow的场合
				if (workflowList.length==0) {
					if (isEditFlg) {
						//contentHtml = contentHtml + "<button class=\"layui-btn\" lay-submit id=\"update\">Update</button>";
					} else {
						contentHtml = contentHtml + "<button class=\"layui-btn\" lay-submit id=\"insert\">Ok</button>";
					}
				} else {
					contentHtml = contentHtml +	propertyItem + buttonItems;
				}
				contentHtml = contentHtml + "<button class=\"layui-btn\" lay-submit id=\"cencel\">Cencel</button>";
			}
		});			

		//++++++++++++++++++++++++++++++++新规窗口End++++++++++++++++++++++++++++++++++++++

		contentHtml = contentHtml +"</footer></div>";

		layer.open({
			type: 1
			,title: Title //不显示标题栏
			,closeBtn: false
			//,area: 'auto'
			//,area: ['500px', '\'' + hi + 'px\'']
			,area:'900px'
				,maxHeight:660
				,offset:[60,0]
		,shade: 0.8
		,id: 'add_'+kayaModelId //设定一个id，防止重复弹出
		,moveType: 1 //拖拽模式，0或者1
		,content:contentHtml
		,success: function(layero,index){
			var businessKeyMap = new Map();
			var updateJson={};
			if(G_BusinessKeyMap.has(kayaModelId)) {
				businessKeyMap = G_BusinessKeyMap.get(kayaModelId);
				for(var key in businessKeyMap){
					jsonBusinessSubkey[key]=businessKeyMap[key];
				}
			}

			form = layui.form;
			for(var x in dateDetail){
				laydate.render({
					elem: '#'+dateDetail[x].id
					,type: dateDetail[x].type
					,format: dateDetail[x].format
				});
			}

			form.on('switch', function(data) {
				$(data.elem).attr('type', 'hidden').val(this.checked ? 'on':'');

			});

			form.on('select', function(data){
				// 只有更改过的字段才会传到后台
				jsonBusinessSubkey[data.elem.getAttribute("id")]=data.value;
			});

			// 绑定业务流得场合
			form.render();
			step.render({
				elem: '#stepForm',
				filter: kayaModelId,
				width: '600px', //设置容器宽度
				stepWidth: '500px',
				height: '20px',
				stepItems: [{
					title: '填写信息'
				}, {
					title: '确认信息'
				}, {
					title: '完成'
				}]
			});

			form.render('select');

			form.on('submit', function(data){

				$(layero).find("input").each(function(i,v) {					
					// 开关的时候转换一下登陆
					if(RegExp(/switch/).exec($(this).attr("lay-skin"))){
						// 传递所有值到后台，参与后台的业务逻辑处理
						jsonBusinessSubkey[this.name]=this.checked ? 'on':'off';
					} else if(RegExp(/layui-input/).exec($(this).attr("class"))){
						// 传递所有值到后台，参与后台的业务逻辑处理
						jsonBusinessSubkey[this.name]=this.value;
					}
				});
				$(layero).find("textarea").each(function(i,v) {					
					// 当class中包含layui-input的时候
					if(RegExp(/layui-textarea/).exec($(this).attr("class")))
					{
						// 传递所有值到后台，参与后台的业务逻辑处理
						jsonBusinessSubkey[this.name]=this.value;
					}
				});

				var actionId = $(this).attr("id");
				var workflowActionId = $(this).attr("actionid");
				if (actionId=='cencel') {
					layer.closeAll();
				} else {
					// 画面根据基本设定的控件check
					if(!validation()){
						return;
					}
					inserteOrUpdateRow(kayaModelId,actionId,jsonBusinessSubkey,insertField,Title,wfType);
				}
			});
		}
		});
	});
}

/**
 * 追加更新行
 * @param kayaModelId
 * @param actionId
 * @param data
 * @param insertField
 * @param Title
 * @returns
 */
function inserteOrUpdateRow(kayaModelId,actionId,data,insertField,Title,wfType){
	var insertOrUpdate = "";
	if(actionId=='cencel'){
		layer.closeAll();
		return;
	} else if (actionId=='insert') {
		insertOrUpdate = "/kayainsert";
	} else if(actionId=='update'){
		insertOrUpdate = "/kayaupdate";
	} else {
		insertOrUpdate = "/kayainsert";
	}

	var kvParamaterList = [];
	var kvParamaterList = [];
	kvParamaterList.push(data);
	// 更新
	$.ajax({
		url : insertOrUpdate,
		method : "POST",
		async: false, //改为同步方式
		data : {
			"kvParamaterList" : JSON.stringify(kvParamaterList),
			'kayaModelId' : kayaModelId,
			'actionId' : actionId,
			'orientationKey' : G_Orientationkey.get(kayaModelId),
			'insertField': insertField
		},
		success : function(data) {
			doSearch(kayaModelId,Title,false,wfType);
		}
	});
	layer.closeAll();
}
/**
 * 删除操作
 * @param kayaModelId
 * @param rowsData
 * @param title
 * @param wfType
 * @returns
 */
function deleteRow(kayaModelId,rowsData,title,wfType) {
	if(rowsData.length === 0){
		layer.msg('Please Select One.');
	} else {
		var kvBusinessKeyList = [];
		var kvParamaterList = [];
		if(G_BusinessKeyMap.has(kayaModelId)) {
			kvBusinessKeyList.push(G_BusinessKeyMap.get(kayaModelId));
		} else {
			kvBusinessKeyList.push(eval('({})'));
		}

		var columnsMap = G_ColumnsMap.get(kayaModelId);
		for(i in rowsData) {

			for (x in columnsMap) {
				// Hidden项非表示
				if (columnsMap[x]['searchFlg']!=false) {
					if ('combobox' == columnsMap[x]['editor'].type) {

						for(var items in columnsMap[x]['editor'].options.data)
						{
							if(rowsData[i][columnsMap[x]['field']]== columnsMap[x]['editor'].options.data[items].text){
								rowsData[i][columnsMap[x]['field']] = columnsMap[x]['editor'].options.data[items].id;
							}
						}
					}
				}
			}
			kvParamaterList.push(rowsData[i]);
		}
		//alert(JSON.stringify(kvParamaterList));

		// 删除
		$.ajax({
			url : "/kayadelete",
			method : "POST",
			data : {
				"kvBusinessKeyList" : JSON.stringify(kvBusinessKeyList),
				"kvParamaterList" : JSON.stringify(kvParamaterList),
				'orientationKey' : G_Orientationkey.get(kayaModelId),
				'kayaModelId' : kayaModelId
			},
			success : function(data) {
				doSearch(kayaModelId,title,false,wfType);
			}
		});
	}
}

/**
 * 
 * @param DivId datatable父div的 id
 * @param RowIndex  行序列号
 * @param BackgroundColor 背景颜色
 * @param Color 字体颜色
 * @returns
 */
function Layui_SetDataTableRowColor(DivId,RowIndex, BackgroundColor,Color)
{
	try
	{
		var div = document.getElementById('tablediv_' + DivId);
		if(div != null) //找到对象了
		{
			var table_main = div.getElementsByClassName('layui-table-main');   //通过class获取table_main
			if (table_main != null && table_main.length > 0)
			{
				var table = table_main[0].getElementsByClassName('layui-table');   //通过class获取table
				if (table != null && table.length > 0) {
					var trs = table[0].querySelectorAll("tr");
					if (trs != null && trs.length > 0) {
						trs[RowIndex].style.color = Color;//字体颜色 
						trs[RowIndex].style.backgroundColor= BackgroundColor;//背景颜色
					}
				}
			}
		}
	}
	catch(e)
	{
		console.log(e.message);
	}
}
/**
 * 模型更新
 * @returns
 */
function RefreshModel(){
	// MenuTerr菜单树初期处理
	$.ajax({
		url : "/kayareset",
		method : "POST",
		dataType:'json',
		data : {

		},
		success : function(data) {
			window.location.href = 'http://localhost:8080/X-admin/index.html';
			alert("Model loaded successfully!");
		}
	});
}

/**
 * 入力项目验证
 * @returns
 */
function validation(){//仿照源码写的校验，返回布尔类型
	verify = form.config.verify, stop = null
	,DANGER = 'layui-form-danger', field = {} ,elem = $('.layui-form')
	,verifyElem = elem.find('*[lay-verify]') //获取需要校验的元素
	,formElem = elem //获取当前所在的form元素，如果存在的话
	,fieldElem = elem.find('input,select,textarea') //获取所有表单域
	,filter = '*'; //获取过滤器

	//开始校验
	layui.each(verifyElem, function(_, item){
		var othis = $(this), vers = othis.attr('lay-verify').split('|'), tips = '';
		var value = othis.val();
		othis.removeClass(DANGER);

		layui.each(vers, function(_, ver){
			var  isFn = typeof verify[ver] === 'function';
			if(verify[ver] && (isFn ? tips = verify[ver](value, item) : !verify[ver][0].test(value)) ){
				layer.msg(tips || verify[ver][1], {
					icon: 5
					,shift: 6
				});
				//非移动设备自动定位焦点
				device = layui.device();
				if(!device.android && !device.ios){
					item.focus(); 
				}
				othis.addClass(DANGER);
				return stop = true;
			}
		});
		if(stop) return stop;
	});

	if(stop) {return false;}
	else{return true;}
}

//获取当前时间，格式YYYY-MM-DD
function getNowFormatDate() {
	var date = new Date();
	var seperator1 = "-";
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = year + seperator1 + month + seperator1 + strDate;
	return currentdate;
}

/**
 * 初期画面操作按钮（toolbar）设定
 * @param wfType 画面类型（一般画面，申请画面，审批画面）
 */
function InitToolbar(wfType) {
	var toolbar = '';
	switch(wfType){
	case '':
		toolbar = 'toolbareadd';
		break;
	case 'apply':
		toolbar = 'toolbareadd';
		break;
	case 'approval':
		toolbar = '';
		break;
	default:
		toolbar = 'toolbareadd';
	}
	return toolbar;
}
/**
 * 检索结果操作按钮（toolbar）设定
 * 
 * @param kayaModelId 画面ID 
 * @param title 画面标题 
 * @param wfType 画面类型（一般画面，申请画面，审批画面）
 */
function getToolbar(kayaModelId,title,wfType) {
	var toolbar = '';
	switch(wfType){
	case '':
		toolbar = 'toolbarall';
		break;
	case 'apply':
		toolbar = 'toolbarall';
		break;
	case 'approval':
		toolbar = 'toolbaredit';
		break;
	default:
		toolbar = 'toolbarall';
	}
	return toolbar;
}
