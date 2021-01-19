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
}).use(['layer','form','element','jquery','laydate','tablePlug', 'selectM','base_ui'],function(){
	$ = layui.jquery;	
	var form = layui.form
	,selectM = layui.selectM
	,laydate = layui.laydate
	,layer = layui.layer // 弹出窗口功能模块
	,element = layui.element //Tab的切换功能，切换事件监听等，需要依赖element模块
	,table = layui.table;

	// Menu动态加载处理
	$.ajax({
		url : "/kayamenu_html5",
		method : "POST",
		async : false, //改为同步方式
		dataType : 'json',
		success : function(data) {
			var ulHtml = "";
			$('#menutree').html(editTreeHtmlAuto(ulHtml, data.menuTree));
			element.init();

		}
	});

	//+++++++++++++++++++++++++++++++++++++++++校验监听-Start++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
	//+++++++++++++++++++++++++++++++++++++++++校验监听-End++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	//+++++++++++++++++++++++++++++++++++++++++Menu监听-Start++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//当点击有site-demo-active属性的标签时，即左侧菜单栏中内容 ，触发点击事件
	$('.site-demo-active').on('click', function () {
		var dataid = $(this);
		var kayaModelId = dataid.attr("data-id");
		var url = dataid.attr("data-url");
		var title = dataid.attr("data-title");
		var column = [];
		var businessSubKeys = [];
		var businessKeys = [];
		var buttonItems=[];

		//++++++++++++++++++++++++++++++++个别画面检索结果操作按钮（toolbar）设定 Start++++++++++++++++++++++++++++++++++++
		toolbar = getToolbar(kayaModelId,title);
		//++++++++++++++++++++++++++++++++个别画面检索结果操作按钮（toolbar）设定 End++++++++++++++++++++++++++++++++++++++
		// 取得初期的行信息，子表迁移按钮信息
		getInitColumns(kayaModelId,column,buttonItems,businessKeys,businessSubKeys);
		//这时会判断右侧.layui-tab-title属性下的有lay-id属性的li的数目，即已经打开的tab项数目
		if ($(".layui-tab-title li[lay-id]").length <= 0) {
			//如果比零小，则直接打开新的tab项
			active.tabAdd(url, kayaModelId, title, buttonItems, column,"","");
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
				active.tabAdd(url, kayaModelId, title, buttonItems, column,"","");
			} else {
				return;
			}
		}

		//++++++++++++++++++++++++++++++++个别画面初期化 Start++++++++++++++++++++++++++++++++++++
		initPage(kayaModelId);
		//++++++++++++++++++++++++++++++++个别画面初期化 End++++++++++++++++++++++++++++++++++++++

		form.render('select');
		table.init(kayaModelId, {
			// toolbar:'default' // 设置默认ToolBar
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
	//+++++++++++++++++++++++++++++++++++++++++Menu监听-End++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//+++++++++++++++++++++++++++++++++++++++++Menu右键菜单监听-Start++++++++++++++++++++++++++++++++++++++++++++
	// Tab右键菜单
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
	//+++++++++++++++++++++++++++++++++++++++++Menu右键菜单监听-End++++++++++++++++++++++++++++++++++++++++++++

	//+++++++++++++++++++++++++++++++++++++++++TODO：++++++++++++++++++++++++++++++++++++++++++++++++++++++
	var element = layui.element;
	/**
	 * Tab 绑定事件
	 */
	active = {
			//在这里给active绑定几项事件，后面可通过active调用这些事件
			tabAdd: function(url, kayaModelId, name, buttonItems, column,parentKayaModelId,parentTitle) {
				//新增一个Tab项 传入三个参数，分别是tab页面的地址，还有一个规定的id，对应其标题，是标签中data-id的属性值
				//关于tabAdd的方法所传入的参数可看layui的开发文档中基础方法部分
				var html = '<iframe tab-id="'+kayaModelId+'" frameborder="0" src="'+url+'" scrolling="yes" class="x-iframe"></iframe>';
				var kayaModelIdTab = kayaModelId;
				var searchDate = [];
				var searchPulldown = [];
				var isWorkflowFlg = false;
				if(G_BusinessSubkeyListMap.has(kayaModelId)) {
					if(G_BusinessSubkeyListMap.get(kayaModelId)=='workflow') {
						isWorkflowFlg = true;
					}
				}

				// KayaModel选项的场合
				if (kayaModelId!=undefined) {
					//+++++++++++++++++++++++++++重新设定TabId Start+++++++++++++++++++++++++++++++++++++++++++++
					kayaModelIdTab = resetKayaModelId(kayaModelId,name);
					//+++++++++++++++++++++++++++重新设定TabId End+++++++++++++++++++++++++++++++++++++++++++++++
					//+++++++++++++++++++++++++++主画面生成+++++++++++++++++++++++++++++++++++++++++++++

					//++++++++++++++++++++++++++++++++个别画面检索结果Checkbox栏表示否设定 Start++++++++++++++++++++++++++++++++++++
					hasCheckBox = hasCheck(kayaModelId, name);
					//++++++++++++++++++++++++++++++++个别画面检索结果Checkbox栏表示否设定 End++++++++++++++++++++++++++++++++++++++
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

//						var base_ui = layui.base_ui;
//						
//						var temp_htmlstr = '';
//						base_ui.editeDetailedInfo(columnsMap,rowData,temp_htmlstr);
						
						
						var displaText = "";
						var data = '{';
						var businessKeyText = "";
//						for (x in columnsMap) {
//							html = html + "<tr><th>";
//							businessKeyText = rowData[columnsMap[x]['field']];
//							// 取得本画面的主键信息
//							G_BusinessKeyListMap.get(kayaModelId).find(function(value){
//
//								var keyData = data;
//								if ('Master' == columnsMap[x].datatype) {
//									for(var items in columnsMap[x]['editor'].options.data){
//										// 通过Text查询Id 设置显示内容
//										if (rowData[columnsMap[x]['field']] == columnsMap[x]['editor'].options.data[items].id && rowData[columnsMap[x]['field']]!='') {
//
//											keyData = keyData + columnsMap[x]['field'] + ':"' + columnsMap[x]['editor'].options.data[items].text + '",';
//											businessKeyText = rowData[columnsMap[x]['field']] + ':' + columnsMap[x]['editor'].options.data[items].text;
//											break;
//										}
//									}
//								} else {
//									keyData = keyData + columnsMap[x]['field'] + ':"' + rowData[columnsMap[x]['field']] + '",';
//								}
//								if (value == columnsMap[x]['field']) {
//									html = html + "<span style=\"color:red\">*</span>";
//
//									// 本画面的主键信息
//									businessKeyText = "<span style=\"color:red\">" + businessKeyText + "</span>";
//									data = keyData;
//								}
//							});
//							html = html + columnsMap[x]['title'] + "</th><td>" + businessKeyText + "</td>";
//						}
						for (x in columnsMap) {
							html = html + "<tr><th>";
							businessKeyText = rowData[columnsMap[x]['field']];
							// 取得本画面的主键信息


							var keyData = data;
							if ('Master' == columnsMap[x].datatype) {
								for(var items in columnsMap[x]['editor'].options.data){
									// 通过Text查询Id 设置显示内容
									if (rowData[columnsMap[x]['field']] == columnsMap[x]['editor'].options.data[items].id && rowData[columnsMap[x]['field']]!='') {

										keyData = keyData + columnsMap[x]['field'] + ':"' + columnsMap[x]['editor'].options.data[items].text + '",';
										businessKeyText = rowData[columnsMap[x]['field']] + ':' + columnsMap[x]['editor'].options.data[items].text;
										break;
									}
								}
							} else if ('Group' == columnsMap[x].datatype) {
								
							} else {
								keyData = keyData + columnsMap[x]['field'] + ':"' + rowData[columnsMap[x]['field']] + '",';
							}
							if (true == columnsMap[x]['uniquekey']) {
								html = html + "<span style=\"color:red\">*</span>";

								// 本画面的主键信息
								businessKeyText = "<span style=\"color:red\">" + businessKeyText + "</span>";
								data = keyData;
							}

							html = html + columnsMap[x]['title'] + "</th><td>" + businessKeyText + "</td>";
						}
						data = data + '}';
						businessKeys = data;
						// 字符串转json
						data = eval('(' + data + ')');
						G_BusinessKeyMap.set(kayaModelId,data);

						html = html + 
						"			</tbody>" +
						"		</table>" +
						"	</div></div></div></div>" +
						"</fieldset>";
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
//							//必须项前面加上*，文字变成红色
//							if(searchKey.required){
//							hotNewsHtml += startline
//							+'  <label class="layui-form-label" style="width:100px"><span style="color:red">*' + searchKey.name+'</span></label>';
//							} else {
							html += startline
							+'  <label class="layui-form-label" style="width:170px">'+column[x]['title']+'</label>';
//							}
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
							//	+'    <input type="text" class="layui-input" placeholder="検索内容を入力してください。" style="width:220px" name= "' + data.searchKeyList[i].id +  '" id = "' + data.searchKeyList[i].id +  '"/>'
							html += '  </div>'
								+'</div>';
//							+ endline;
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
					if(isWorkflowFlg){
						html = html + 
						"				<input type=\"button\" class=\"layui-btn layui-block\" onclick=\"doSearchWorkflow(\'" + kayaModelId + "\',\'" + name + "\',false)\" value=\"Search\"/></div>" +
						"		</fieldset></form>" +
						"	<div class=\"layui-form-item\"></div>" +
						"</div>" +
						"<xblock id=\"xblock_" + kayaModelId +"\">";
						for (x in buttonItems) {
							// TODO:Tab的Index取得
							html = html + 
							"<button onclick=\"EditDetailedInfoWF('"+ kayaModelId + "','"  + buttonItems[x]['kayamodelid'] + "','" + buttonItems[x]['title']+ "','" + name + "')\" class=\"layui-btn layui-btn-radius subButton" + kayaModelId + "\">" + buttonItems[x]['title'] + "</button>";
						}
						html = html + 
						"</xblock>" +
						"<div id=\"tablediv_" + kayaModelIdTab + "\">" +
						//"	<table class=\"layui-table\"  id=\""+kayaModelIdTab +"\" lay-data=\"{toolbar:'#toolbar1c',defaultToolbar:'[]'}\" lay-filter=\""+kayaModelIdTab +"\">" +
						"	<table class=\"layui-table\"  id=\""+kayaModelIdTab +"\" lay-data=\"{toolbar:'',defaultToolbar:'[]'}\" lay-filter=\""+kayaModelIdTab +"\">";
					} else {
						html = html + 
						"				<input type=\"button\" class=\"layui-btn layui-block\" onclick=\"doSearch(\'" + kayaModelId + "\',\'" + name + "\',false)\" value=\"Search\"/></div>" +
						"		</fieldset></form>" +
						"	<div class=\"layui-form-item\"></div>" +
						"</div>" +
						"<xblock id=\"xblock_" + kayaModelId +"\">";
						for (x in buttonItems) {
							// TODO:Tab的Index取得
							html = html + 
							"<button onclick=\"EditDetailedInfo('"+ kayaModelId + "','"  + buttonItems[x]['kayamodelid'] + "','" + buttonItems[x]['title']+ "','" + name + "')\" class=\"layui-btn layui-btn-radius subButton" + kayaModelId + "\">" + buttonItems[x]['title'] + "</button>";
						}
						html = html + 
						"</xblock>" +
						"<div id=\"tablediv_" + kayaModelIdTab + "\">" +
						"	<table class=\"layui-table\"  id=\""+kayaModelIdTab +"\" lay-data=\"{toolbar:'#toolbar1c',defaultToolbar:'[]'}\" lay-filter=\""+kayaModelIdTab +"\">";
					}

					//+++++++++++++++++++++++++++++++++++++++++++检索条件结束++++++++++++++++++++++++++++++++++++++++++++
					//+++++++++++++++++++++++++++++++++++++++++++子表按钮开始++++++++++++++++++++++++++++++++++++++++++++

					//+++++++++++++++++++++++++++++++++++++++++++子表按钮结束++++++++++++++++++++++++++++++++++++++++++++
					//+++++++++++++++++++++++++++++++++++++++++++表格编辑开始++++++++++++++++++++++++++++++++++++++++++++
					//var tempHtml = '';
					var levelMap = new Map();
					
					
					var base_ui = layui.base_ui;
					// 按阶层取得表头信息
					base_ui.editeColumns(column,levelMap);

					html = html + "<thead>";

					for (var i = levelMap.size;i>0;i--) {
						if (i ==levelMap.size) {
			　　　				html = html + "<tr><th lay-data=\"{type:'numbers'}\" rowspan=\"" + levelMap.size + "\" style=\"width:10px\">No.</th>";
			　　　				if(hasCheckBox){
								html = html + "<th lay-data=\"{type:'checkbox'}\" rowspan=\"" + levelMap.size + "\" style=\"width:18px\"></th>";
							}
			　　　				html = html + levelMap.get(i) + "</tr>";
			　　　			}　else {
			　　　				html = html + "<tr>" + levelMap.get(i) + "</tr>";
			　　　			}　　　　　　
					}
					
					
//					levelMap.forEach(function(value,key){
//			　　　　　　　　　　　　alert(value + 'key:' + key);
//						// No.和CheckBox列处理
//			　　　			if (key ==levelMap.size) {
//			　　　				html = html + "<tr><th lay-data=\"{type:'numbers'}\" rowspan=\"" + levelMap.size + "\" style=\"width:10px\">No.</th>";
//			　　　				if(hasCheckBox){
//								html = html + "<th lay-data=\"{type:'checkbox'}\" rowspan=\"" + levelMap.size + "\" style=\"width:18px\"></th>";
//							}
//			　　　				
//			　　　				html = html + value + "</tr>";
//			　　　			}　else {
//			　　　				html = html + "<tr>" + value + "</tr>";
//			　　　			}　　　　　　
//				
//					});
//					alert(levelMap.size);
					
					html = html + "</thead></table></div></div>";
				}

				//+++++++++++++++++++++++++++++++++++++++++++表格编辑结束++++++++++++++++++++++++++++++++++++++++++++
				element.tabAdd('home-tabs', {
					title: name 
					,content:html
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
				return data;
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
			},
			tabAddWF: function(url, kayaModelId, name, buttonItems, column,parentKayaModelId,parentTitle,actionItemList) {
				//新增一个Tab项 传入三个参数，分别是tab页面的地址，还有一个规定的id，对应其标题，是标签中data-id的属性值
				//关于tabAdd的方法所传入的参数可看layui的开发文档中基础方法部分
				var html = '<iframe tab-id="'+kayaModelId+'" frameborder="0" src="'+url+'" scrolling="yes" class="x-iframe"></iframe>';
//				var data = "{";
				var kayaModelIdTab = kayaModelId;
				var searchDate = [];
				var searchPulldown = [];
				var isWorkflowFlg = false;
				if(G_BusinessSubkeyListMap.has(kayaModelId)) {
					if(G_BusinessSubkeyListMap.get(kayaModelId)=='workflow') {
						isWorkflowFlg = true;
					}
				}
//				if(column[0]['title']=='WorkFlow') {
//				isWorkflowFlg = true;
//				}
				// KayaModel选项的场合
				if (kayaModelId!=undefined) {
					//+++++++++++++++++++++++++++重新设定TabId Start+++++++++++++++++++++++++++++++++++++++++++++
					kayaModelIdTab = resetKayaModelId(kayaModelId,name);
					//+++++++++++++++++++++++++++重新设定TabId End+++++++++++++++++++++++++++++++++++++++++++++++
					//+++++++++++++++++++++++++++主画面生成+++++++++++++++++++++++++++++++++++++++++++++
//					switch(kayaModelId)
//					{
//					// 调配画面
//					case 'id-0065-00000019-0000001b':
//					html = getAllocatePageHtml(kayaModelId, name, buttonItems, column,parentKayaModelId,parentTitle);
//					break;
//					default:
					//++++++++++++++++++++++++++++++++个别画面检索结果Checkbox栏表示否设定 Start++++++++++++++++++++++++++++++++++++
					hasCheckBox = hasCheck(kayaModelId, name);
					//++++++++++++++++++++++++++++++++个别画面检索结果Checkbox栏表示否设定 End++++++++++++++++++++++++++++++++++++++
					html = "<div name=\"" + name + "\" tab-id='" + kayaModelId + "' class=\"x-body\" id=\"middle\">" +
					"	<div class=\"layui-row\">";
					//alert("ssss");
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
						//alert(parentKayaModelId + ":" + parentTitle);
						var rowData = G_DataRowListMap.get(parentKayaModelId);
						G_Orientationkey.set(kayaModelId,rowData['orientationkey']);
						// 取得父表各元素信息
						var columnsMap = G_ColumnsMap.get(parentKayaModelId);

						var displaText = "";
						var data = '{';
						var businessKeyText = "";
						for (x in columnsMap) {
							html = html + "<tr><th>";
							businessKeyText = rowData[columnsMap[x]['field']];
							// 取得本画面的主键信息
							G_BusinessKeyListMap.get(kayaModelId).find(function(value){
//								if ('combobox' == columnsMap[x]['editor'].type) {
								var keyData = data;
								if ('Master' == columnsMap[x].datatype) {
									for(var items in columnsMap[x]['editor'].options.data){
										// 通过Text查询Id 设置显示内容
										if (rowData[columnsMap[x]['field']] == columnsMap[x]['editor'].options.data[items].id && rowData[columnsMap[x]['field']]!='') {

											keyData = keyData + columnsMap[x]['field'] + ':"' + columnsMap[x]['editor'].options.data[items].text + '",';
											businessKeyText = rowData[columnsMap[x]['field']] + ':' + columnsMap[x]['editor'].options.data[items].text;
											break;
										}
									}
								} else {
									keyData = keyData + columnsMap[x]['field'] + ':"' + rowData[columnsMap[x]['field']] + '",';

								}
								if (value == columnsMap[x]['field']) {
									html = html + "<span style=\"color:red\">*</span>";

									// 本画面的主键信息
									businessKeyText = "<span style=\"color:red\">" + businessKeyText + "</span>";
									data = keyData;
								}
							});


							html = html + columnsMap[x]['title'] + "</th><td>" + businessKeyText + "</td>";
						}
						data = data + '}';
						businessKeys = data;
						//字符串转json
						data = eval('(' + data + ')');
						//alert(kayaModelId + ":" + JSON.stringify(data));
						G_BusinessKeyMap.set(kayaModelId,data);

						html = html + 
						"			</tbody>" +
						"		</table>" +
						"	</div></div></div></div>" +
						"</fieldset>";
					}
					//+++++++++++++++++++++++++++++++++++++++++++详细信息结束++++++++++++++++++++++++++++++++++++++++++++
					html = html +
					"<form class=\"layui-form\" lay-filter=\"workflowForm_" + kayaModelId + "\">		<fieldset class=\"layui-elem-field\">";
					html = html + "<xblock id=\"xblock_" + kayaModelId +"\">";
					var buttons = "";
					var pros = "";
//					for (x in buttonItems) {
//					// TODO:Tab的Index取得
//					html = html + 
//					"<button onclick=\"EditDetailedInfo('"+ kayaModelId + "','"  + buttonItems[x]['kayamodelid'] + "','" + buttonItems[x]['title']+ "','" + name + "')\" class=\"layui-btn layui-btn-radius subButton" + kayaModelId + "\">" + buttonItems[x]['title'] + "</button>";
//					}
					for (x in actionItemList) {					
						switch(actionItemList[x]['editor'])
						{
						case 'Action':
//							buttons = buttons + "<button class=\"layui-btn\" top=\"50%\" lay-submit=\"\"  id=\"" + actionItemList[x]['kayamodelid'] 
//							+ " onclick=\"doWorkflow(\'" + kayaModelId + "\',\'" + name + "\',false)\""
//							+ "\">" + actionItemList[x]['label'] + "</button>";
							buttons = buttons +"<button onclick=\"doWorkflow('"+ kayaModelId + "','"  + actionItemList[x]['kayamodelid'] + "','" + actionItemList[x]['label'] + "','" + name + "','" + parentKayaModelId
							+ "')\" class=\"layui-btn " + actionItemList[x]['kayamodelid'] + "\">" + actionItemList[x]['label'] + "</button>";

							break;
						case 'Property':
//							pros = pros + "<div class=\"layui-form-item\">" +
//							"<label class=\"layui-form-label\">" + actionItemList[x]['label']  + "</label>" +
//							"<div class=\"layui-input-inline\">" +
//							"<input type=\"text\" id=\"" + actionItemList[x]['field'] + "\" name=\"" + actionItemList[x]['field'] + "\" required=\"\"" +
//							"autocomplete=\"off\" class=\"layui-input\">" +
//							" </input></div>" +
//							"</div>";
							pros = pros + "<div class=\"layui-inline\">"+	"<label class=\"layui-form-label\">" + actionItemList[x]['label']  + "</label>" 
							+ "<input type=\"text\" class=\"layui-input\" style=\"width:220px\"  id=\"" + actionItemList[x]['field'] + "\" name=\"" 
							+ actionItemList[x]['field'] + "\" placeholder=\""+actionItemList[x]['label'] + "を入力してください\" />"
//							+ "<input type=\"text\" id=\"" + actionItemList[x]['field'] + "\" name=\"" + actionItemList[x]['field'] + "\" required=\"\"" 
//							+ "autocomplete=\"off\" class=\"layui-input\"/>" +
							+"</div>";
							break;
						default:
							//inputCenter = inputCenter + "easyui-textbox";
						}
					}
					html = html + buttons +"</xblock> "+ pros +" </fieldset></form>";

					//+++++++++++++++++++++++++++++++++++++++++++表格编辑开始++++++++++++++++++++++++++++++++++++++++++++


					html = html + 
					//"</xblock>" +
					"<div id=\"tablediv_" + kayaModelIdTab + "\">" +
					"	<table class=\"layui-table\"  id=\""+kayaModelIdTab +"\" lay-data=\"{toolbar:'#toolbar1c',defaultToolbar:'[]'}\" lay-filter=\""+kayaModelIdTab +"\">" +
					"		<thead>" +
					"		<tr>" +
					// No.和CheckBox列处理
					"			<th lay-data=\"{type:'numbers'}\"  style=\"width:10px\">No.</th>";
					if(hasCheckBox){
						html = html + "<th lay-data=\"{type:'checkbox'}\"  style=\"width:18px\"></th>";
					}

					if(column.length > 10) {
						for (x in column) {

							var length = column[x]['title'].length;
							length = length*18;
							if(length<120) {
								length = 120;
							}
							if(column[x]['datatype']=='Master') {
								html = html +  "<th lay-data=\"{field:\'" + column[x]['field'] + "_NM\', width:" + length + "}\">" + column[x]['title'] + "</th>";
							} else {
								html = html +  "<th lay-data=\"{field:\'" + column[x]['field'] + "\', width:" + length + "}\">" + column[x]['title'] + "</th>";
							}
							//alert(column[x]['title']);
						}
					} else {
						for (x in column) {
							if(column[x]['datatype']=='Master') {
								html = html +  "<th lay-data=\"{field:\'" + column[x]['field'] + "_NM\'}\">" + column[x]['title'] + "</th>";
							} else {
								html = html +  "<th lay-data=\"{field:\'" + column[x]['field'] + "\'}\">" + column[x]['title'] + "</th>";
							}
							//alert(column[x]['title']);
						}
					}
					html = html + "</tr></thead></table></div></div>";
					//alert(html);
//					}
				}

				//+++++++++++++++++++++++++++++++++++++++++++表格编辑结束++++++++++++++++++++++++++++++++++++++++++++
				html = html + 
				"		<fieldset id=\"workflow_" + kayaModelId + "\"class=\"layui-elem-field layui-field-title\">" +
				"		<legend><span class=\"layui-bg-cyan\"  style=\"font-size:16px;\">" + parentTitle +" workflow " + "</span></legend></fieldset>" +
				"			<ul class=\"layui-timeline\" >" +
				"				<div class=\"detailArea\"></div></ul>" 


				element.tabAdd('home-tabs', {
					title: name 
					,content:html
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
				var buttonId = rowData['buttonId']
				$("."+buttonId).attr('disabled', true);
				$("."+buttonId).css({"background-color":"grey"});
				//通过鼠标mouseover事件  动态将新增的tab下的li标签绑定鼠标右键功能的菜单
				//下面的json.id是动态新增Tab的id，一定要传入这个id,避免重复加载mouseover数据
				$(".layui-tab-title li[lay-id=" + kayaModelIdTab + "]").mouseover(function () {
					CustomRightClick(kayaModelId); //给tab绑定右击事件
					//FrameWH(); //计算ifram层的大小
				});
				element.init();
				return data;
			}
	};
});




/**
 * 绑定主键值
 */
function bindingBusinessKeys(){

}

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

function editTreeHtml(ulHtml,nodes) {
	// ++++++++++++++++++++++++++业务分Menu生成++++++++++++++++++++++++++
	return createBusinessMenu(ulHtml,nodes);
}

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
			var _aHtml = "<dd><a class=\"site-demo-active\" data-url=\"admin-role.html\" data-id=\"" + 
			nodes[x]['id'] + "\" data-title=\"" + nodes[x]['text'] + "\"data-type=\"tabAdd\" href=\"javascript:void(0);\">" + nodes[x]['text'] +"</a></dd>";
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
			//businessKeyList = ;
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
		}
	}
	//++++++++++++++++++++++++++++++++个别画面检索结果操作按钮（toolbar）设定 Start++++++++++++++++++++++++++++++++++++
	toolbar = getToolbar(kayaModelId,title);
	//++++++++++++++++++++++++++++++++个别画面检索结果操作按钮（toolbar）设定 End++++++++++++++++++++++++++++++++++++++
	var column = [];// 列明数组
	var buttonItems=[];// 按钮数组
	var businessSubKeys =[];
	var businessKeys = [];
	getInitColumns(kayaModelId,column,buttonItems,businessKeys,businessSubKeys);
	var isData = false;

	kayaModelIdTab = resetKayaModelId(kayaModelId,title);

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
		//alert($(this).children('a').attr('kayaid'));
		//---------------------------------按钮，表列明编辑---------------------------------------
		businessKeysData= active.tabAdd("", kayaModelId, title, buttonItems,column,parentKayaModelId,parentTitle);
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
			toolbar:toolbar
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
				//alert(JSON.stringify(G_BusinessKeyMap.get(kayaModelId)));
				//alert(JSON.stringify(G_BusinessKeyListMap.get(kayaModelId)));
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
function doSearch(kayaModelId,title,isDownload) {
	var rows = [];
	var kvParamaterList = [];
	if(G_BusinessKeyMap.has(kayaModelId)) {
		kvParamaterList.push(G_BusinessKeyMap.get(kayaModelId));
	} else {
		kvParamaterList.push(eval('({})'));
	}

	layui.config({base: '../../layui/plug/'})
	.use(['tablePlug', 'laydate'], function () {
		//var tablePlug = layui.tablePlug;
		var table = layui.table;
		var form = layui.form;
		// 检索条件设定
		var fromValue = JSON.stringify(form.val('searchForm_' + kayaModelId));
		// alert(JSON.stringify(kvParamaterList));
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

		//++++++++++++++++++++++++++++++++个别画面检索结果操作按钮（toolbar）设定 Start++++++++++++++++++++++++++++++++++++
		toolbar = getToolbar(kayaModelId,title);
		//++++++++++++++++++++++++++++++++个别画面检索结果操作按钮（toolbar）设定 End++++++++++++++++++++++++++++++++++++++
		//alert(searchValue);
		var workflowActionId = $(this).attr("actionid");
		$.ajax({
			url : "/kayaselect",
			method : "POST",
			async: false, //同步方式
			data : {
				'kayaModelId' : kayaModelId,
				'searchname' : searchKey,
				'searchvalue' : searchValue,
				'orientationKey' : G_Orientationkey.get(kayaModelId),
				"searchParamaterList" : '[' + fromValue +']',
				"kvParamaterList" : JSON.stringify(kvParamaterList)
			},
			success : function(data) {
				rows = data.resultDetails;
			}
		});

		if(!isDownload) {
			toolbar = 'default';
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
				toolbar = '#toolbar1c';
				defaultToolbar = '[]';
			}

			//++++++++++++++++++++++++++++++++个别画面运算处理 Start++++++++++++++++++++++++++++++++
			postSearch(kayaModelId, rows);
			//++++++++++++++++++++++++++++++++个别画面运算处理 End++++++++++++++++++++++++++++++++++
			//+++++++++++++++++++++++++++重新设定TabId Start+++++++++++++++++++++++++++++++++++++++++++++
			kayaModelIdTab = resetKayaModelId(kayaModelId,title);
			//+++++++++++++++++++++++++++重新设定TabId End+++++++++++++++++++++++++++++++++++++++++++++++

			form.render('select');
			table.reload(kayaModelIdTab, {
				smartReloadModel: true,
				data: rows,
				//toolbar:'default',
				toolbar: toolbar,
				defaultToolbar: defaultToolbar,
				//even: true,
				//totalRow:true,
				page: {
					limit: 12
				},
				limits: [2, 5, 12],
				done: function (res, curr, count) {
					// 设置第一行为默认选择状态，更改背景色以及字体颜色
					Layui_SetDataTableRowColor('tablediv_' + kayaModelId, 0, '#2F4056', '#fff');
					// 取得被选择行数据
					G_DataRowListMap.set(kayaModelId,res.data[0]);
					//alert(JSON.stringify(res.data[0]));
				}
			});
			//监听头工具栏事件
			table.on('toolbar('+ kayaModelIdTab + ')', function(obj){
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
					//alert(JSON.stringify(G_BusinessKeyMap.get(kayaModelId)));
					//alert(kayaModelId + ":" +JSON.stringify(G_BusinessKeyListMap.get(kayaModelId)));
					deleteRow(kayaModelId,data,title);
					break;
				case 'download':
					//alert(JSON.stringify(G_BusinessKeyMap.get(kayaModelId)));
					//alert(kayaModelId + ":" +JSON.stringify(G_BusinessKeyListMap.get(kayaModelId)));
					doSearch(kayaModelId,title,true);
					break;
				};
			});

			//监听行单击事件（双击事件为：rowDouble）
			table.on('row(' + kayaModelIdTab +')', function(obj){
				//alert(JSON.stringify(obj.data));
				// 恢复默认行的背景色以及字体颜色
				Layui_SetDataTableRowColor('tablediv_' + kayaModelId, 0, '#fff', '#666');

				// 标注选中样式
				obj.tr.addClass('layui-bg-cyan').siblings().removeClass('layui-bg-cyan');
				// 取得被选择行数据
				G_DataRowListMap.set(kayaModelId,obj.data);

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
function addRow(kayaModelId,Title,isEditFlg) {


	//alert(parentKayaModelId);
	//alert(JSON.stringify(businessKeysData));
	var rowData=null;
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
		//alert(JSON.stringify(rowData));
	} else {
		// 动态设置题目
		//Title = Title + "-[追加]";
	}
	layui.config({base: '../../layui/plug/'})
	.use(['laydate'], function () {

		//触发事件
		var actionButton = [];
		var actionButtonId = [];
		//var that = this; 
		var layer = layui.layer;
		var laydate = layui.laydate;

		var contentHtml = "<div class=\"layui-form\"  lay-filter=\"" + kayaModelId+ "\">";
		var hi = 0;

		var Readonly="";
		var Required="";
		var isDisplay="";
		//++++++++++++++++++++++++++++++++新规窗口Start++++++++++++++++++++++++++++++++++++

		var jsonRowData="{";
		var jsonBusinessSubkey = "{";
		var insertField = '[';
		var dateDetail = []; // 日期类型控件可控显示
		var width = '300px';

		//var thisBusinessKeys = new Map();

		var actionItems = ['0'];
		//var actionItems = ['0','1','2','3','4'];
		$.ajax({
			url : "/kayainitwindow",
			method : "POST",
			async: false, //改为同步方式
			data : {
				'kayaModelId' : kayaModelId,
				'actionItems' : JSON.stringify(actionItems)
			},
			success : function(data) {
				var labels = [];
				var center = '';


				var businessKeyList = [];// 主键信息
				var workflowList = []; // 流程信息
				labels = data.labelList; // 表列信息
				businessKeyList = data.businessKeyList;// 取得主键信息
				workflowList = data.workflowList; // 取得流程信息


				// 画面项目初期表示处理
				for (x in labels) {
					hi= hi + 10;
					// 本画面的主键信息
					var businessKeySpan = "";
					Required = "";
					var isRequired = (labels[x]['Required'] == 'true');
					if(isRequired) {
						businessKeySpan = "*";
						Required = " lay-verify=\"required\" ";
					}

					var dataTypes_int = ["Integer", "int", "short","long"];
					var isNum_int =false;
					fLen = dataTypes_int.length;
					for (i = 0; i < fLen; i++) {
						if(labels[x]['editor']==dataTypes_int[i]){
							isNum_int=true;
							break;
						}
					} 
					if(isNum_int){
						if(Required == ""){
							Required = " lay-verify=\"number_int\" ";
						}else{
							var disLength = Required.length;
							Required = Required.substring(0,disLength-2) + "|number_int\" ";
						}
					}

					var dataTypes_float = ["float","double","Double","BigDecimal"];
					var isNum_float=false;
					fLen = dataTypes_float.length;
					for (i = 0; i < fLen; i++) {
						if(labels[x]['editor']==dataTypes_float[i]){
							isNum_float=true;
							break;
						}
					} 
					if(isNum_float){
						if(Required == ""){
							Required = " lay-verify=\"number_float\" ";
						}else{
							var disLength = Required.length;
							Required = Required.substring(0,disLength-2) + "|number_float\" ";
						}
					}

					var DataLength="";
					if(labels[x]['DataLength'] > 0){
						DataLength=" maxlength='" + labels[x]['DataLength']  +"' ";
						if(Required == ""){
							Required = " lay-verify=\"maxlength\" ";
						}else{
							var disLength = Required.length;
							Required = Required.substring(0,disLength-2) + "|maxlength\" ";
						}
					}		

					var Pattern="";
					var reg=labels[x]['validation'];			
					if(reg !=null && reg !="" && reg !="[RegularExpression]:[message]" ){
						if(Required == ""){
							Required = " lay-verify=\"regexp\" ";
						}else{
							var disLength = Required.length;
							Required = Required.substring(0,disLength-2) + "|regexp\" ";
						}
						Pattern=" pattern='" + reg + "'  ";
					}		

					businessKeySpan = businessKeySpan + labels[x]['label'];
					var inputValue= "";
					var calendarType= "";

					Readonly = "";
					// 修正模式的场合
					if (isEditFlg) {
						//++++++++++++++++++++++++++++++++个别画面控件显示设定 Start++++++++++++++++++++++++++++++++++++
						isDisplay = getDisplay(kayaModelId, 'update', labels[x]['field']);
						Readonly = getReadOnly(kayaModelId, 'update', labels[x]['field']);
						//++++++++++++++++++++++++++++++++个别画面控件显示设定 End++++++++++++++++++++++++++++++++++++++
						inputValue = rowData[labels[x]['field']];// 表示值

						// TODO:新增加Cloum处理rowData[labels[x]['field']]
						if (rowData[labels[x]['field']]==undefined) {
							//displayStr = '';
							insertField = insertField + "'" + labels[x]['field'] + "',";

						}else {
							//inputValue = rowData[labels[x]['field']];
						}
						//alert(insertField);

						switch(labels[x]['editor']){
						case 'Master':	//alert(eval('(' + JSON.stringify(labels[x]['options']) + ')'));
							//alert(labels[x]['field'] + ":" +rowData[labels[x]['field']]);
							for(var key in labels[x]['options']){

								if (rowData[labels[x]['field']] == undefined) {
									//insertField = insertField + "'" + labels[x]['field'] + "',";
								} else {
									if(labels[x]['options'][key].id==rowData[labels[x]['field']]) {
										jsonRowData = jsonRowData + labels[x]['field'] + ':"' + labels[x]['options'][key].id + '",';
									}
								}

//								if(labels[x]['options'][key]==rowData[labels[x]['field']]) {
//								jsonRowData = jsonRowData + labels[x]['field'] + `:"` + key + `",`;
//								}
							}
							break;
						default:
							jsonRowData = jsonRowData + labels[x]['field'] + ':"' + rowData[labels[x]['field']] + '",';
						break;
						}
						// Add模式的场合
					} else {
						Readonly = "";// 只读
						//++++++++++++++++++++++++++++++++个别画面控件显示设定 Start++++++++++++++++++++++++++++++++++++
						isDisplay = getDisplay(kayaModelId, 'insert', labels[x]['field']);
						//++++++++++++++++++++++++++++++++个别画面控件显示设定 End++++++++++++++++++++++++++++++++++++++
					}
					if(isDisplay==null) {
						isDisplay = (labels[x]['Display'] == 'true');
					}

					// 取得本画面的主键信息(标签红色处理)
					var businessKeyFlg = false;
					businessKeyList.find(function(val){
						if (val == labels[x]['field'] ) {
							businessKeySpan = "<span style=\"color:red\">*" + labels[x]['label'] + "</span>";

							//Required = " lay-verify=\"required\" ";

							// 编辑状态只读
							if (isEditFlg){
								Readonly = " disabled=\"disabled\" ";
								switch(labels[x]['editor'])
								{
								case 'Master':	//alert(eval('(' + JSON.stringify(labels[x]['options']) + ')'));
									for(var key in labels[x]['options']){
										if(labels[x]['options'][key].id==rowData[labels[x]['field']]) {
											jsonBusinessSubkey = jsonBusinessSubkey + labels[x]['field'] + ':"' + labels[x]['options'][key].id + '",';
										}
									}
									break;
								default:
									jsonBusinessSubkey = jsonBusinessSubkey + labels[x]['field'] + ':"' + rowData[labels[x]['field']] + '",';
								break;
								}
							}
						} 
					});

					// 表示
					if(isDisplay) {
						contentHtml = contentHtml + "<div class=\"layui-form-item\"  style=\"margin-top: 5px;\">";

						switch(labels[x]['editor'])
						{
						case 'Auto':
							if (isEditFlg){
								contentHtml = contentHtml +  "<label class=\"layui-form-label\">" + businessKeySpan + "</label><div class=\"layui-input-inline\" style=\"width:"+ width + "\"><input hidden = 'true' type=\"text\"" + Readonly + Required + DataLength + Pattern + " id=\"" + labels[x]['field'] + "\" name=\"" + labels[x]['field'] + "\" required=\"\"" +
								"autocomplete=\"off\" class=\"layui-input\" value=\"" + inputValue + "\">";
							} else {
								contentHtml = contentHtml +  "<div class=\"layui-input-inline\" style=\"width:"+ width + "\"><input style=\"display: none\" type=\"text\"" + Readonly + " id=\"" + labels[x]['field'] + "\" name=\"" + labels[x]['field'] + "\"" +
								"autocomplete=\"off\" class=\"layui-input\" value=\"" + inputValue + "\">";
							}

							break;
						case 'Master':// Master对象
							//contentHtml = contentHtml +  "<select  type=\"text\" class=\"select\"" + Readonly + " id=\"" + labels[x]['field'] +"\" style=\"pointer-events: none;\">";
							contentHtml = contentHtml +  "<label class=\"layui-form-label\">" + businessKeySpan + "</label><div class=\"layui-input-inline \" style=\"width:"+ width + "\"><select  type=\"text\" class=\"select\"" + Readonly + Required + DataLength  + Pattern + " id=\"" + labels[x]['field'] +"\" name=\"" + labels[x]['field'] +"\">";
							for (var key in labels[x]['options']) {
								// Hidden项非表示
								//alert(key + labels[x]['options'][key]);

								if (isEditFlg) {
									//alert(inputValue);
									if (inputValue == labels[x]['options'][key].id) {
										contentHtml = contentHtml +  "	<option value=\"" + labels[x]['options'][key].id + "\"" + " selected=\"selected\">" + labels[x]['options'][key].text + "</option>";
									} else {
										contentHtml = contentHtml +  "	<option value=\"" + labels[x]['options'][key].id + "\"" + ">" + labels[x]['options'][key].text + "</option>";
									}
								} else {
									// TODO：设置默认下拉框选项（不允许空选项，不然在新追加表列的时候没办法判断是否是新加项。如果不用热添加功能，可以不用设置默认选项）
									if (key == labels[x]['defaultvalue']) {
										jsonBusinessSubkey = jsonBusinessSubkey + labels[x]['field'] + ':"' + labels[x]['options'][key].id + '",';
										contentHtml = contentHtml +  "	<option value=\"" + labels[x]['options'][key].id + "\"" + " selected=\"selected\">" + labels[x]['options'][key].text + "</option>";

									} else {
										contentHtml = contentHtml +  "	<option value=\"" + labels[x]['options'][key].id + "\"" + ">" + labels[x]['options'][key].text + "</option>";
									}
								}
							}
							contentHtml = contentHtml + "</select>";
							break;
						case 'boolean':
							contentHtml = contentHtml +  "<label class=\"layui-form-label\">" + businessKeySpan + "</label><div class=\"layui-input-inline\"><input type=\"checkbox\" class=\"layui-input\" lay-skin=\"switch\" lay-text=\"ON|OFF\" name = \"" + labels[x]['field'] + "\" id = \"" + labels[x]['field'] + "\"";
							if(inputValue=='on'){
								contentHtml = contentHtml +  " checked ";
							}
							contentHtml = contentHtml +  "/>";
							break;
						default:
							// 根据控件类型生成画面
							switch(labels[x]['ComponentType']) {
							case 'dateY':
								contentHtml +=  "<label class=\"layui-form-label\">" + businessKeySpan + "</label><div class=\"layui-input-inline\" style=\"width:"+ width + "\"><input type=\"text\" class=\"layui-input\"" + Readonly + Required + DataLength  + Pattern + " name = \"" + labels[x]['field'] + "\" id = \"" + labels[x]['field'] + "_pop\" value=\"" + inputValue + "\"/>";
								// Date的时候指定类型显示
								dateDetail.push({"id" : labels[x]['field']+"_pop","type" : "year","format" : "yyyy"});	
								break;
							case 'dateYM':
								contentHtml +=  "<label class=\"layui-form-label\">" + businessKeySpan + "</label><div class=\"layui-input-inline\" style=\"width:"+ width + "\"><input type=\"text\" class=\"layui-input\"" + Readonly + Required + DataLength  + Pattern + " name = \"" + labels[x]['field'] + "\" id = \"" + labels[x]['field'] + "_pop\" value=\"" + inputValue + "\"/>";
								// Date的时候指定类型显示
								dateDetail.push({"id" : labels[x]['field']+"_pop","type" : "month","format" : "yyyy-MM"});	
								break;
							case 'dateYMD':
								contentHtml +=  "<label class=\"layui-form-label\">" + businessKeySpan + "</label><div class=\"layui-input-inline\" style=\"width:"+ width + "\"><input type=\"text\" class=\"layui-input\"" + Readonly + Required + DataLength  + Pattern + " name = \"" + labels[x]['field'] + "\" id = \"" + labels[x]['field'] + "_pop\" value=\"" + inputValue + "\"/>";
								// Date的时候指定类型显示
								dateDetail.push({"id" : labels[x]['field']+"_pop","type" : "date","format" : "yyyy-MM-dd"});	
								break;
							case 'dataTime':
								contentHtml +=  "<label class=\"layui-form-label\">" + businessKeySpan + "</label><div class=\"layui-input-inline\" style=\"width:"+ width + "\"><input type=\"text\" class=\"layui-input\"" + Readonly + Required + DataLength  + Pattern + " name = \"" + labels[x]['field'] + "\" id = \"" + labels[x]['field'] + "_pop\" value=\"" + inputValue + "\"/>";
								// Date的时候指定类型显示
								dateDetail.push({"id" : labels[x]['field']+"_pop","type" : "datetime","format" : "yyyy-MM-dd HH:mm:ss"});	
								break;
							case 'time':
								contentHtml +=  "<label class=\"layui-form-label\">" + businessKeySpan + "</label><div class=\"layui-input-inline\" style=\"width:"+ width + "\"><input type=\"text\" class=\"layui-input\"" + Readonly + Required + DataLength  + Pattern + " name = \"" + labels[x]['field'] + "\" id = \"" + labels[x]['field'] + "_pop\" value=\"" + inputValue + "\"/>";
								// Date的时候指定类型显示
								dateDetail.push({"id" : labels[x]['field']+"_pop","type" : "time","format" : "HH:mm:ss"});	
								break;
							case 'textarea':
								contentHtml = contentHtml +  "<label class=\"layui-form-label\">" + businessKeySpan + "</label><div class=\"layui-input-inline\" style=\"width:"+ width + "\"><textarea class=\"layui-textarea\"" + Readonly + Required + DataLength  + Pattern + " id=\"" + labels[x]['field'] + "\" name=\"" + labels[x]['field'] + "\">" + inputValue + "</textarea>";
								break;
							case 'password':
								contentHtml = contentHtml +  "<label class=\"layui-form-label\">" + businessKeySpan + "</label><div class=\"layui-input-inline\" style=\"width:"+ width + "\"><input class=\"layui-input\" type=\"password\"" + Readonly + Required + DataLength  + Pattern + " id=\"" + labels[x]['field'] + "\" name=\"" + labels[x]['field'] + "\" required=\"\"" +
								"autocomplete=\"off\" class=\"layui-input\" value=\"" + inputValue + "\">";
								break;
							default:
								contentHtml = contentHtml +  "<label class=\"layui-form-label\">" + businessKeySpan + "</label><div class=\"layui-input-inline\" style=\"width:"+ width + "\"><input class=\"layui-input\" type=\"text\"" + Required + DataLength  + Pattern + " id=\"" + labels[x]['field'] + "\" name=\"" + labels[x]['field'] + "\" required=\"\"" +
								"autocomplete=\"off\" class=\"layui-input\" value=\"" + inputValue + "\">";
							}
						}
						contentHtml= contentHtml +
						" </input></div>" +
						"</div>";
					}
				}

				insertField = insertField + ']';
				contentHtml = contentHtml + "</div></div>"  + 
				// 按钮右侧对齐
				"<div align=\"right\" style=\"border:15px solid #fff;bottom:0;left:0;\">";
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

		contentHtml = contentHtml +"</div></div>";

		jsonRowData = jsonRowData + '}';


		jsonBusinessSubkey = jsonBusinessSubkey + '}'; 
		//alert(jsonRowData);
		// alert(dataText);
		//字符串转json
		jsonRowData = eval('(' + jsonRowData + ')');


		jsonBusinessSubkey = eval('(' + jsonBusinessSubkey + ')');

		//alert(JSON.stringify(jsonBusinessSubkey));
		layer.open({
			type: 1
			,title: Title //不显示标题栏
			,closeBtn: false
			//,area: 'auto'
			//,area: ['500px', '\'' + hi + 'px\'']
			,area:'500px'
			,maxHeight:600
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
//			// 绑定日期控件
//			lay('.date-item').each(function(){
//			laydate.render({
//			elem: this
//			,min: 1
//			,trigger: 'click'
//			,format: 'yyyyMMdd'
//			});
//			});
//			// TODO 绑定过去日期控件
//			lay('.date-item-past').each(function(){
//			laydate.render({
//			elem: this
//			,max: 0
//			,trigger: 'click'
//			});
//			});
			//alert(JSON.stringify(jsonBusinessSubkey));
			form = layui.form;
			for(var x in dateDetail){
				laydate.render({
					elem: '#'+dateDetail[x].id
					,type: dateDetail[x].type
					,format: dateDetail[x].format
				});
			}
			form.render();

			form.on('switch', function(data) {
				$(data.elem).attr('type', 'hidden').val(this.checked ? 'on':'');

			});
			form.render();

			form.on('select', function(data){
				// 只有更改过的字段才会传到后台
//				if (jsonRowData[data.elem.getAttribute("id")]!=data.value) {
//				jsonBusinessSubkey[data.elem.getAttribute("id")]=data.value;
//				}

				// 传递所有值到后台，参与后台的业务逻辑处理
				jsonBusinessSubkey[data.elem.getAttribute("id")]=data.value;
				//jsonRowData[data.elem.getAttribute("id")]=data.value;
			});

			form.on('submit', function(data){

				$(layero).find("input").each(function(i,v) {					
					// 开关的时候转换一下登陆
					if(RegExp(/switch/).exec($(this).attr("lay-skin")))
					{
						// 传递所有值到后台，参与后台的业务逻辑处理
						jsonBusinessSubkey[this.name]=this.checked ? 'on':'off';
					} else if(RegExp(/layui-input/).exec($(this).attr("class")))
					{
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
					//++++++++++++++++++++++++++++++++个别画面追加前处理 Start++++++++++++++++++++++++++++++++++++
					if (actionId=='insert') {
						if(!preAdd(kayaModelId, jsonBusinessSubkey)) {
							return;
						}
					}
					//++++++++++++++++++++++++++++++++个别画面追加前处理 End++++++++++++++++++++++++++++++++++++++
					//++++++++++++++++++++++++++++++++个别画面默认值设定 Start++++++++++++++++++++++++++++++++++++
					setDefaultValue(kayaModelId, actionId, jsonBusinessSubkey);
					//++++++++++++++++++++++++++++++++个别画面默认值设定 End++++++++++++++++++++++++++++++++++++++		
					inserteOrUpdateRow(kayaModelId,actionId,jsonBusinessSubkey,insertField,Title);
					//++++++++++++++++++++++++++++++++个别画面追加后处理 Start++++++++++++++++++++++++++++++++++++
					postInsertOrUpdate(kayaModelId, actionId, jsonBusinessSubkey);
					//++++++++++++++++++++++++++++++++个别画面追加后处理 End++++++++++++++++++++++++++++++++++++++
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
function inserteOrUpdateRow(kayaModelId,actionId,data,insertField,Title){
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
		//insertOrUpdate = "/workflowhandle";
	}

	var kvParamaterList = [];

	//alert(insertOrUpdate);
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
			//alert("Inserted Success");
			doSearch(kayaModelId,Title,false);
		}
	});
	layer.closeAll();


	// 更新父类页面行变更数据
	// 如果只有一条数据，默认选中状态
}

function deleteRow(kayaModelId,rowsData,title) {
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

		//kvParamaterList.push(rowsData);
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
				doSearch(kayaModelId,title,false);
			}
		});
	}
}
function doWorkflow(parentKayaModelId,kayaModelId,title,parentTitle,grandKayaModelId) {
	var actionId = kayaModelId;

	var form = layui.form;
	// 检索条件设定
	var fromValue = JSON.stringify(form.val('workflowForm_' + parentKayaModelId));
	// 更新
	$.ajax({
		url : "/workflowhandle",
		method : "POST",
		async: false, //改为同步方式
		data : {
			'kvParamaterList' : '[' + fromValue +']',
			'kayaModelId' : parentKayaModelId,
			'actionId' : actionId,
			'orientationkey' : G_Orientationkey.get(parentKayaModelId),
			'businessid' : G_WorkflowBusinessKey.get('businessid'),
			'businesssubid' : G_WorkflowBusinessKey.get('businesssubid')

		},
		success : function(data) {
//			alert("Inserted Success");
//			doSearchWorkflow(parentKayaModelId,parentTitle,false);
		}
	});
	layer.closeAll();
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
		var div = document.getElementById(DivId);
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
			alert("Model Update Completed!");

		}
	});
}

function validation(){//仿照源码写的校验，返回布尔类型
	verify = form.config.verify, stop = null
	,DANGER = 'layui-form-danger', field = {} ,elem = $('.layui-form')

	,verifyElem = elem.find('*[lay-verify]') //获取需要校验的元素
	,formElem = elem //获取当前所在的form元素，如果存在的话
	,fieldElem = elem.find('input,select,textarea') //获取所有表单域
	,filter = '*'; //获取过滤器
	//alert(JSON.stringify(verify));

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
 * 检索
 * @param kayaModelId
 * @returns
 */
function doSearchWorkflow(kayaModelId,title,isDownload) {
	var rows = [];
	var kvParamaterList = [];
	if(G_BusinessKeyMap.has(kayaModelId)) {
		kvParamaterList.push(G_BusinessKeyMap.get(kayaModelId));
	} else {
		kvParamaterList.push(eval('({})'));
	}

	layui.config({base: '../../layui/plug/'})
	.use(['tablePlug', 'laydate'], function () {
		//var tablePlug = layui.tablePlug;
		var table = layui.table;
		var form = layui.form;
		// 检索条件设定
		var fromValue = JSON.stringify(form.val('searchForm_' + kayaModelId));
		// alert(JSON.stringify(kvParamaterList));
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

		//++++++++++++++++++++++++++++++++个别画面检索结果操作按钮（toolbar）设定 Start++++++++++++++++++++++++++++++++++++
		toolbar = getToolbar(kayaModelId,title);
		//++++++++++++++++++++++++++++++++个别画面检索结果操作按钮（toolbar）设定 End++++++++++++++++++++++++++++++++++++++
		//alert(searchValue);
		//var workflowActionId = $(this).attr("actionid");
		$.ajax({
			url : "/workflowselect",
			method : "POST",
			async: false, //同步方式
			data : {
				'kayaModelId' : kayaModelId,
				'searchname' : searchKey,
				'searchvalue' : searchValue,
				'searchUser' : "m1",
				'orientationKey' : G_Orientationkey.get(kayaModelId),
				"searchParamaterList" : '[' + fromValue +']',
				"kvParamaterList" : JSON.stringify(kvParamaterList)
			},
			success : function(data) {
				rows = data.resultDetails;
			}
		});

		if(!isDownload) {
			toolbar = '';
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
				toolbar = '#toolbar1c';
				defaultToolbar = '[]';
			}

			//++++++++++++++++++++++++++++++++个别画面运算处理 Start++++++++++++++++++++++++++++++++
			postSearch(kayaModelId, rows);
			//++++++++++++++++++++++++++++++++个别画面运算处理 End++++++++++++++++++++++++++++++++++
			//+++++++++++++++++++++++++++重新设定TabId Start+++++++++++++++++++++++++++++++++++++++++++++
			kayaModelIdTab = resetKayaModelId(kayaModelId,title);
			//+++++++++++++++++++++++++++重新设定TabId End+++++++++++++++++++++++++++++++++++++++++++++++

			form.render('select');
			table.reload(kayaModelIdTab, {
				data: rows,
				//toolbar:'default',
				toolbar: '',
				defaultToolbar: defaultToolbar,
				//even: true,
				//totalRow:true,
				page: {
					limit: 12
				},
				limits: [2, 5, 12],
				done: function (res, curr, count) {
					// 设置第一行为默认选择状态，更改背景色以及字体颜色
					Layui_SetDataTableRowColor('tablediv_' + kayaModelId, 0, '#2F4056', '#fff');
					// 取得被选择行数据
					G_DataRowListMap.set(kayaModelId,res.data[0]);
					//alert(JSON.stringify(res.data[0]));
				}
			});

			//监听头工具栏事件
			table.on('toolbar('+ kayaModelIdTab + ')', function(obj){
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
					//alert(JSON.stringify(G_BusinessKeyMap.get(kayaModelId)));
					//alert(kayaModelId + ":" +JSON.stringify(G_BusinessKeyListMap.get(kayaModelId)));
					deleteRow(kayaModelId,data,title);
					break;
				case 'download':
					//alert(JSON.stringify(G_BusinessKeyMap.get(kayaModelId)));
					//alert(kayaModelId + ":" +JSON.stringify(G_BusinessKeyListMap.get(kayaModelId)));
					doSearch(kayaModelId,title,true);
					break;
				};
			});

			//监听行单击事件（双击事件为：rowDouble）
			table.on('row(' + kayaModelIdTab +')', function(obj){
				//alert(JSON.stringify(obj.data));
				// 恢复默认行的背景色以及字体颜色
				Layui_SetDataTableRowColor('tablediv_' + kayaModelId, 0, '#fff', '#666');

				// 标注选中样式
				obj.tr.addClass('layui-bg-cyan').siblings().removeClass('layui-bg-cyan');
				// 取得被选择行数据
				G_DataRowListMap.set(kayaModelId,obj.data);

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
 *  WorkFlow详细Tab选项卡追加处理
 * @param index
 * @param parentKayaModelId
 * @param kayaModelId
 * @param title
 * @returns
 */
function EditDetailedInfoWF(parentKayaModelId,kayaModelId,title,parentTitle) {
	// 父类数据为空时不处理
	if (!G_DataRowListMap.has(parentKayaModelId)) {
		//alert("请选择数据");
		return;
	} else {
		if (G_DataRowListMap.has(kayaModelId)) {
			G_DataRowListMap.delete(kayaModelId);
		}
	}
	//++++++++++++++++++++++++++++++++个别画面检索结果操作按钮（toolbar）设定 Start++++++++++++++++++++++++++++++++++++
	toolbar = getToolbar(kayaModelId,title);
	//++++++++++++++++++++++++++++++++个别画面检索结果操作按钮（toolbar）设定 End++++++++++++++++++++++++++++++++++++++
	var column = [];// 列明数组
	var buttonItems=[];// 按钮数组
	var rowData = G_DataRowListMap.get(parentKayaModelId);
	var orientationkey = rowData['orientationkey'];
	var flowcode = rowData['flowcode'];
	var businessKeys = [];
	var rows = [];
	var workflowRows = [];
	var actionItemList = [];

	getInitColumnsWF(kayaModelId,column,buttonItems,businessKeys,orientationkey,parentKayaModelId,rows,workflowRows,actionItemList);
	var isData = false;

	kayaModelIdTab = resetKayaModelId(kayaModelId,title);

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
		//alert($(this).children('a').attr('kayaid'));
		//---------------------------------按钮，表列明编辑---------------------------------------
		businessKeysData= active.tabAddWF("", kayaModelId, title, buttonItems,column,parentKayaModelId,parentTitle,actionItemList);
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
			toolbar:''
		});

	});	
	// -------Table生成列名处理------------------------------------------------------------------------
	active.tabChange(kayaModelId);
	addWFDetail(kayaModelId,title,rows,workflowRows);
}
/**
 * 取得初期的行信息，子表迁移按钮信息
 * @param kayaModelId
 * @param column 表头信息
 * @param buttonItems 子表迁移按钮信息
 * @returns
 */
function getInitColumnsWF(kayaModelId,column,buttonItems,businessKeys,orientationkey,parentKayaModelId,rows,workflowRows,actionItemList){
	var actionItems = ['1','2'];
	var businessid = '';
	var businesssubid = '';
	var rowData = G_DataRowListMap.get(parentKayaModelId);
	var flowcode = rowData['flowcode'];
	$.ajax({
		url : "/kayainitWFDetail",
		method : "POST",
		async: false, //改为同步方式
		data : {
			'kayaModelId' : kayaModelId,
			'orientationKey' : orientationkey,
			'workflowId' :parentKayaModelId,
			'flowcode' :flowcode,
			'actionItems' : JSON.stringify(actionItems)
		},
		success : function(data) {
			_column = data.columnsList;
			_rows = data.resultList;
			_workflowRows = data.workflowList;
			_actionItemList = data.actionItemList; // 取得流程信息
			// 拿到主键
			//businessKeyList = ;
			businessSubKeys = data.businessSubKeyList;
			businessKeys = data.businessKeyList;

			for (x in _column) {
				// Hidden项非表示
				if (_column[x]['role']==false) {
					column.push(_column[x]);
					// 对Role进行显示编辑(子画面Link)
				} else {
					buttonItems.push(_column[x]);
				}
			} 


			// 搜索栏初期表示处理
			for (x in _actionItemList) {
				actionItemList.push(_actionItemList[x]);
			} 
			for (x in _rows) {
				rows.push(_rows[x]);
			} 
			for (x in _workflowRows) {
				workflowRows.push(_workflowRows[x]);
				businessid = workflowRows[x]['businessid'];
				businesssubid = workflowRows[x]['businesssubid'];
			} 
		}
	});

	G_ColumnsMap.set(kayaModelId,column);
	G_BusinessSubkeyListMap.set(kayaModelId,businessSubKeys);
	G_BusinessKeyListMap.set(kayaModelId,businessKeys);
	G_WorkflowBusinessKey.clear();
	G_WorkflowBusinessKey.set('businessid',businessid);
	G_WorkflowBusinessKey.set('businesssubid',businesssubid);


}
/**
 * 检索
 * @param kayaModelId
 * @returns
 */
function addWFDetail(kayaModelId,title,rows,workflowRows) {

	layui.config({base: '../../layui/plug/'})
	.use(['tablePlug', 'laydate'], function () {
		//var tablePlug = layui.tablePlug;
		var table = layui.table;
		var form = layui.form;		
		//++++++++++++++++++++++++++++++++个别画面运算处理 Start++++++++++++++++++++++++++++++++
		postSearch(kayaModelId, rows);
		//++++++++++++++++++++++++++++++++个别画面运算处理 End++++++++++++++++++++++++++++++++++
		//+++++++++++++++++++++++++++重新设定TabId Start+++++++++++++++++++++++++++++++++++++++++++++
		kayaModelIdTab = resetKayaModelId(kayaModelId,title);
		//+++++++++++++++++++++++++++重新设定TabId End+++++++++++++++++++++++++++++++++++++++++++++++

		form.render('select');
		table.reload(kayaModelIdTab, {
			data: rows,
			//toolbar:'default',
			toolbar: '',
			defaultToolbar: defaultToolbar,
			//even: true,
			//totalRow:true,
			page: {
				limit: 12
			},
			limits: [2, 5, 12],
			done: function (res, curr, count) {
				// 设置第一行为默认选择状态，更改背景色以及字体颜色
				Layui_SetDataTableRowColor('tablediv_' + kayaModelId, 0, '#2F4056', '#fff');
				// 取得被选择行数据
				G_DataRowListMap.set(kayaModelId,res.data[0]);
				//alert(JSON.stringify(res.data[0]));
			}
		});

		//监听头工具栏事件
		table.on('toolbar('+ kayaModelIdTab + ')', function(obj){
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
				//alert(JSON.stringify(G_BusinessKeyMap.get(kayaModelId)));
				//alert(kayaModelId + ":" +JSON.stringify(G_BusinessKeyListMap.get(kayaModelId)));
				deleteRow(kayaModelId,data,title);
				break;
			case 'download':
				//alert(JSON.stringify(G_BusinessKeyMap.get(kayaModelId)));
				//alert(kayaModelId + ":" +JSON.stringify(G_BusinessKeyListMap.get(kayaModelId)));
				doSearch(kayaModelId,title,true);
				break;
			};
		});
		var hotNewsHtml = ' ';
		var icon = '';
		for(var i=0;i<workflowRows.length;i++){

			if(i>0) {
				icon = '';
			}
			hotNewsHtml += '  <li class="layui-timeline-item"> '
				+'     <i class="layui-icon layui-timeline-axis"></i>'
				+'     <div class="layui-timeline-content layui-text">'
				+'     <div class="layui-timeline-title">' + workflowRows[i].createdate
				+ ', ' + workflowRows[i].Action + '<br>' + workflowRows[i].Property;

			hotNewsHtml += '  </div>'
				+'  </div>' + '  </li>';
		}
		hotNewsHtml += '  <li class="layui-timeline-item"> '
			+'     <i class="layui-icon layui-timeline-axis">◎</i>'
			+'     <div class="layui-timeline-content layui-text">'
			+'     <div class="layui-timeline-title">' + ' Start';

		hotNewsHtml += '  </div>'
			+'  </div>' + '  </li>';

		$(".detailArea").html(hotNewsHtml);

		//画面控件刷新
		form.render();

		//监听行单击事件（双击事件为：rowDouble）
		table.on('row(' + kayaModelIdTab +')', function(obj){
			//alert(JSON.stringify(obj.data));
			// 恢复默认行的背景色以及字体颜色
			Layui_SetDataTableRowColor('tablediv_' + kayaModelId, 0, '#fff', '#666');

			// 标注选中样式
			obj.tr.addClass('layui-bg-cyan').siblings().removeClass('layui-bg-cyan');
			// 取得被选择行数据
			G_DataRowListMap.set(kayaModelId,obj.data);

		});
	});

}