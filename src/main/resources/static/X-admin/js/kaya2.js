/**
 * 全局变量
 */
var G_ColumnsMap = new Map();// 表头全局变量
var G_BusinessKeyListMap = new Map();// 各模型主键全局变量
var G_BusinessKeyMap = new Map();// 各画面记录主键全局变量
var G_BusinessSubkeyListMap = new Map();// SubKey
var G_DataRowListMap = new Map();// 主键全局变量

/**
 * 递归实现左侧Menu菜单自动生成处理
 * ul   Div父元素
 * nodes 子节点
 */

$(function () {
	layui.use(['form','element'], function () {
		layer = layui.layer; // 弹出窗口功能模块
		element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
		form = layui.form;
		// Menu动态加载
		$.ajax({
			url : "/kayamenu_html5",
			method : "POST",
			async : false, //改为同步方式
			dataType : 'json',
			success : function(data) {
				var ulHtml = "";
				$('#menutree').html(editTreeHtml(ulHtml, data.menuTree));
				element.init();

			}
		});
		//addVerify(form);
		
		form.verify({
			title: function (value, item) {
				alert(value);
				if (value.length < 5) {
					return '标题至少得5个字符啊';
				}
			}, fname: function (value) {
				if (value.length < 4) {
					return '请输入至少4位的用户名';
				}
			}, contact: function (value) {
				if (value.length < 4) {
					return '内容请输入至少4个字符';
				}
			}
			, phone: [/^1[3|4|5|7|8]\d{9}$/, '手机必须11位，只能是数字！']
			, email: [/^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/, '邮箱格式不对']
		});
		
		//+++++++++++++++++++++++++++++++++++++++++Menu监听-Start++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//当点击有site-demo-active属性的标签时，即左侧菜单栏中内容 ，触发点击事件
		$('.site-demo-active').on('click', function () {
			//alert("有问题");
			var dataid = $(this);
			var kayaModelId = dataid.attr("data-id");
			var url = dataid.attr("data-url");
			var title = dataid.attr("data-title");
			var column = [];
			var businessSubKeys = [];
			var businessKeys = [];
			var buttonItems=[];
			if (G_DataRowListMap.has(kayaModelId)) {
				G_DataRowListMap.delete(kayaModelId);
			}

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
						// alert("有问题");
					}
				});
				if (!isData) {
					active.tabAdd(url, kayaModelId, title, buttonItems, column,"","");
				}
			}


			layui.config({base: '../../layui/plug/'})
			.extend({tablePlug: 'tablePlug/tablePlug'})
			.use(['tablePlug', 'laydate'], function () {
				var table = layui.table;
				var form = layui.form;
				form.render('select');
				table.init(kayaModelId, {
					toolbar:'default'
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
			//alert(currentTabId + ":" + $(this).attr("data-type"));
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
		//+++++++++++++++++++++++++++++++++++++++++form验证-Start++++++++++++++++++++++++++++++++++++++++++++



		//+++++++++++++++++++++++++++++++++++++++++form验证-End++++++++++++++++++++++++++++++++++++++++++++++

		function FrameWH() {
			var h = $(window).height() - 41 - 10 - 60 - 10 - 44 - 10;
			$("iframe").css("height", h + "px");
		}

		$(window).resize(function () {
			FrameWH();
		});
		//+++++++++++++++++++++++++++++++++++++++++TODO：++++++++++++++++++++++++++++++++++++++++++++++++++++++
	});
	var element = layui.element;
	//触发事件
	active = {
			//在这里给active绑定几项事件，后面可通过active调用这些事件
			tabAdd: function(url, kayaModelId, name, buttonItems, column,parentKayaModelId,parentTitle) {
				//新增一个Tab项 传入三个参数，分别是tab页面的地址，还有一个规定的id，对应其标题，是标签中data-id的属性值
				//关于tabAdd的方法所传入的参数可看layui的开发文档中基础方法部分
				var html = '<iframe tab-id="'+kayaModelId+'" frameborder="0" src="'+url+'" scrolling="yes" class="x-iframe"></iframe>';
				var data = "{";
				// KayaModel选项的场合
				if (kayaModelId!=undefined) {
					html = "<div name=\"" + name + "\" tab-id='" + kayaModelId + "' class=\"x-body\"> " +
					"	<div class=\"layui-row\">";
					//alert("ssss");
					// 子表头部处理
					if (parentKayaModelId != "") {
						//+++++++++++++++++++++++++++++++++++++++++++详细信息开始++++++++++++++++++++++++++++++++++++++++++++
						html = html + 
						"		<fieldset id=\"parent_" + kayaModelId + "\"class=\"layui-elem-field\">" +
						"		<legend><span class=\"layui-bg-cyan\"  style=\"font-size:16px;\">" + parentTitle + "</span></legend>" +
						"			<div class=\"layui-collapse\" lay-filter=\"test\">" +
						"				<div class=\"layui-colla-item\"><h2 class=\"layui-colla-title\"><span  style=\"color:#009688\">详细信息</span></h2>" +
						"				<div class=\"layui-colla-content\">" +
						"				<div class=\"layui-field-box\">" +
						"					<table class=\"layui-table\">" +
						"					    <tbody>";
						//alert(parentKayaModelId + ":" + parentTitle);
						var rowData = G_DataRowListMap.get(parentKayaModelId);

						// 取得父表各元素信息
						var columnsMap = G_ColumnsMap.get(parentKayaModelId);


						var displaText = "";
						//var data = '{';
						var businessKeyText = "";
						for (x in columnsMap) {
							html = html + "<tr><th>";
							businessKeyText = rowData[columnsMap[x]['field']];
							// 取得本画面的主键信息
							G_BusinessKeyListMap.get(parentKayaModelId).find(function(value){
								if (value == columnsMap[x]['field']) {
									html = html + "<span style=\"color:red\">*</span>";
									if ('combobox' == columnsMap[x]['editor'].type) {
										for(var items in columnsMap[x]['editor'].options.data){
											// 通过Text查询Id 设置显示内容
											if (rowData[columnsMap[x]['field']] == columnsMap[x]['editor'].options.data[items].text) {

												data = data + columnsMap[x]['field'] + `:"` + columnsMap[x]['editor'].options.data[items].id + `",`;
											}
										}
									} else {
										//displaText = rowData[columnsMap[x]['field']];
										data = data + columnsMap[x]['field'] + `:"` + rowData[columnsMap[x]['field']] + `",`;

									}
									displaText = rowData[columnsMap[x]['field']];

									// 本画面的主键信息
									businessKeyText = "<span style=\"color:red\">" + displaText + "</span>";

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

					//+++++++++++++++++++++++++++++++++++++++++++检索条件开始++++++++++++++++++++++++++++++++++++++++++++
					html = html +
					"		<fieldset class=\"layui-elem-field\">" +
					"		<legend><span class=\"layui-bg-cyan\"  style=\"font-size:16px;\">检索条件</span></legend>" +
					"			<div class=\"layui-form\">" +
					"				<div class=\"layui-input-inline\">" +
					"                	<select id=\"searchkey_" + kayaModelId +"\" lay-verify=\"chapterRender\" style=\"width:80px\">";
					for (x in column) {
						// Hidden项非表示
						html = html +  "	<option value=\"" + column[x]['field'] + "\">" + column[x]['title'] + "</option>";
					}

					html = html +  "	</select>" +
					"</div>" +
					"				<div class=\"layui-input-inline\">" +
					"					<input type=\"text\" id=\"searchvalue_" + kayaModelId +"\" required=\"\" lay-verify=\"required\"" +
					"						autocomplete=\"off\" class=\"layui-input\">" +
					"				</div>" +
					"				<button class=\"layui-btn\"  onclick=\"doSearch(\'" + kayaModelId + "\',\'" + name + "\')\"><i class=\"layui-icon\">&#xe615;</i>检索</button>" +
					"			</div>" +
					"		</fieldset>" +
					"	<div class=\"layui-form-item\"></div>" +
					"</div>" +
					"<xblock id=\"xblock_" + kayaModelId +"\">";
					//+++++++++++++++++++++++++++++++++++++++++++检索条件结束++++++++++++++++++++++++++++++++++++++++++++
					//+++++++++++++++++++++++++++++++++++++++++++子表按钮开始++++++++++++++++++++++++++++++++++++++++++++
					for (x in buttonItems) {
						// TODO:Tab的Index取得
						html = html + 
						"<button onclick=\"EditDetailedInfo('"+ kayaModelId + "','"  + buttonItems[x]['kayamodelid'] + "','" + buttonItems[x]['title']+ "','" + name + "')\" class=\"layui-btn layui-btn-radius\">" + buttonItems[x]['title'] + "</button>";
					}
					//+++++++++++++++++++++++++++++++++++++++++++子表按钮结束++++++++++++++++++++++++++++++++++++++++++++
					//+++++++++++++++++++++++++++++++++++++++++++表格编辑开始++++++++++++++++++++++++++++++++++++++++++++
					html = html + 
					"</xblock>" +
					"<div id=\"tablediv_" + kayaModelId + "\">" +
					"	<table class=\"layui-table\" lay-filter=\""+kayaModelId +"\">" +
					"		<thead>" +
					"		<tr>" +
					// No.和CheckBox列处理
					"			<th lay-data=\"{type:'numbers'}\"  style=\"width:10px\">No.</th><th lay-data=\"{type:'checkbox'}\"  style=\"width:18px\"></th>";

					for (x in column) {

						html = html +  "<th lay-data=\"{field:\'" + column[x]['field'] + "\' }\" style=\"width:80px\">" + column[x]['title'] + "</th>";
						//alert(column[x]['title']);
					} 		
					html = html + "</tr></thead></table></div></div>";
					//alert(html);
				}

				//+++++++++++++++++++++++++++++++++++++++++++表格编辑结束++++++++++++++++++++++++++++++++++++++++++++

				element.tabAdd('home-tabs', {
					title: name 
					,content:html
					,id: kayaModelId
				});


				//通过鼠标mouseover事件  动态将新增的tab下的li标签绑定鼠标右键功能的菜单
				//下面的json.id是动态新增Tab的id，一定要传入这个id,避免重复加载mouseover数据
				$(".layui-tab-title li[lay-id=" + kayaModelId + "]").mouseover(function () {
					CustomRightClick(kayaModelId); //给tab绑定右击事件
					//FrameWH(); //计算ifram层的大小
				});
				element.init();
				return data;
			},
			tabChange: function (kayaModelId) {
				//切换到指定Tab项
				element.tabChange('home-tabs', kayaModelId); //根据传入的id传入到指定的tab项
			},
			tabDelete: function (kayaModelId) {
				element.tabDelete('home-tabs', kayaModelId); //删除
			},
			tabRefresh: function (kayaModelId) { //刷新页面
				$("iframe[data-frameid='" + kayaModelId + "']").attr("src", $("iframe[data-frameid='" + kayaModelId + "']").attr("src")) //刷新框架
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

/**
 * TODO：Menu加载速度慢，未加载完毕点击无效
 * 递归实现左侧Menu菜单自动生成处理
 * ul   Div父元素
 * nodes 子节点
 */
function editTree(ul,nodes) {
	var li = document.createElement("li");
	li.setAttribute("class","layui-nav-item layui-nav-itemed");

	var dl = document.createElement("dl");
	dl.setAttribute("class","layui-nav-child");
	for(var x = 0; x < nodes.length; x++) {

		if(nodes[x]['children'] !=undefined) {
			var _li = document.createElement("li");
			_li.setAttribute("class","layui-nav-item");

			var _a = document.createElement("a");
			_a.setAttribute("href","javascript:void(0);");
			var _i = document.createElement("i");
			_i.setAttribute("class","iconfont");
			_i.setAttribute("style","10px;color:#009688;");
			_i.innerHTML = "&#xe6b4;";

			var span = document.createElement("span");
			span.innerHTML = nodes[x]['text'];

			_a.appendChild(_i);
			_a.appendChild(span);
			_li.appendChild(_a);

			var dl_1 = document.createElement("dl");
			dl_1.setAttribute("class","layui-nav-child layui-nav-itemed");

			editTree(dl_1, nodes[x]['children']);
			_li.appendChild(dl_1);
			dl.appendChild(_li);

		} else {
			var dd = document.createElement("dd");
			var _a = document.createElement("a");
			_a.setAttribute("class", "site-demo-active");
			_a.setAttribute("data-url", "admin-role.html");
			_a.setAttribute("data-id",nodes[x]['id']);
			_a.setAttribute("data-title",nodes[x]['text']);
			_a.setAttribute("data-type", "tabAdd");
			_a.setAttribute("href","javascript:void(0);");
			_a.innerHTML=nodes[x]['text'];
			dd.appendChild(_a);
			dl.appendChild(dd);
		}
	}
	li.appendChild(dl);

	//alert(JSON.stringify(li.innerHTML));
	ul.appendChild(li);
	return ul;
}

function editTreeHtml(ulHtml,nodes) {

	var liHtml="<li class=\"layui-nav-item layui-nav-itemed\">";
	var dlHtml = "<dl clsss=\"layui-nav-child\">";
	for(var x = 0; x < nodes.length; x++) {
		if(nodes[x]['children'] !=undefined) {
			var _liHtml = "<li class=\"layui-nav-item\">";
			var _aHtml = "<a href=\"javascript:void(0);\">";
			var _iHtml = "<i class=\"iconfont\" style=\"10px;color:#009688;\">&#xe6b4;</i>";
			var spanHtml="<span>" + nodes[x]['text'] + "</span>";
			var dl_1Html = "<dl class=\"layui-nav-child layui-nav-itemed\">";
			dl_1Html = editTreeHtml(dl_1Html, nodes[x]['children']) + "</dl>";
			_liHtml =_liHtml + (_aHtml + _iHtml + spanHtml + "</a>" + dl_1Html +"</li>");
			dlHtml = dlHtml + _liHtml;
		} else {
			var _aHtml = "<dd><a class=\"site-demo-active\" data-url=\"admin-role.html\" data-id=\"" + 
			nodes[x]['id'] + "\" data-title=\"" + nodes[x]['text'] + "\"data-type=\"tabAdd\" href=\"javascript:void(0);\">" + nodes[x]['text'] +"</a></dd>";
			dlHtml=dlHtml + _aHtml;
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
function getInitColumns(kayaModelId,column,buttonItems,businessKeys,businessSubKeys){
	$.ajax({
		url : "/kayainit",
		method : "POST",
		async: false, //改为同步方式
		data : {
			'kayaModelId' : kayaModelId
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
				if (_column[x]['searchFlg']!=false) {
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

	var column = [];// 列明数组
	var buttonItems=[];// 按钮数组
	var businessSubKeys =[];
	var businessKeys = [];
	getInitColumns(kayaModelId,column,buttonItems,businessKeys,businessSubKeys);
	var isData = false;
	//这时会判断右侧.layui-tab-title属性下的有lay-id属性的li的数目，即已经打开的tab项数目
	if ($(".layui-tab-title li[lay-id]").length > 0) {
		//初始化一个标志，为false说明未打开该tab项 为true则说明已有
		$.each($(".layui-tab-title li[lay-id]"), function () {
			//如果点击左侧菜单栏所传入的id 在右侧tab项中的lay-id属性可以找到，则说明该tab项已经打开
			if ($(this).attr("lay-id") == kayaModelId) {
				isData = true;
				active.tabChange(kayaModelId);
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
		if(!G_BusinessKeyMap.has(kayaModelId)) {
			G_BusinessKeyMap.push(kayaModelId,businessKeysData);
		}
	}

	// -------Table生成列名处理------------------------------------------------------------------------
	layui.config({base: '../../layui/plug/'})
	.extend({tablePlug: 'tablePlug/tablePlug'})
	.use(['tablePlug', 'laydate'], function () {
		var table = layui.table;
		var form = layui.form;
		form.render('select');
		table.init(kayaModelId, {
			toolbar:'default'
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
function doSearch(kayaModelId,title) {
	var rows = [];
	var kvParamaterList = [];
	if(G_BusinessKeyMap.has(kayaModelId)) {
		kvParamaterList.push(G_BusinessKeyMap.get(kayaModelId));
	} else {
		kvParamaterList.push(eval('({})'));
	}

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

	//alert(searchValue);
	$.ajax({
		url : "/kayaselect",
		method : "POST",
		async: false, //同步方式
		data : {
			'kayaModelId' : kayaModelId,
			'searchname' : searchKey,
			'searchvalue' : searchValue,
			"kvParamaterList" : JSON.stringify(kvParamaterList)
		},
		success : function(data) {
			rows = data.mapList;
		}
	});

	layui.config({base: '../../layui/plug/'})
	.extend({tablePlug: 'tablePlug/tablePlug'})
	.use(['tablePlug', 'laydate'], function () {
		//var tablePlug = layui.tablePlug;
		var table = layui.table;
		var form = layui.form;
		form.render('select');
		table.init(kayaModelId, {
			data: rows,
			toolbar:'default',
			//even: true,
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
				//alert(kayaModelId + ":" +JSON.stringify(G_BusinessKeyListMap.get(kayaModelId)));
				deleteRow(kayaModelId,data,title);
				break;
			};
		});

		//监听行单击事件（双击事件为：rowDouble）
		table.on('row(' + kayaModelId +')', function(obj){
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
			Title = Title + "-[修改]";
		}
		//alert(JSON.stringify(rowData));
	} else {
		// 动态设置题目
		Title = Title + "-[追加]";
	}
	layui.config({base: '../../layui/plug/'})
	.extend({tablePlug: 'tablePlug/tablePlug'})
	.use(['tablePlug','form', 'laydate','layedit'], function () {

		//触发事件
		var actionButton = [];
		var actionButtonId = [];
		//var that = this; 
		var layer = layui.layer;

		var contentHtml = "<div class=\"layui-form gls\"  lay-filter=\"" + kayaModelId+ "\"><form class=\"layui-form layui-form-pane\">";
		var hi = 0;

		var Readonly="";
		//++++++++++++++++++++++++++++++++新规窗口Start++++++++++++++++++++++++++++++++++++

		var jsonRowData="{";
		var jsonBusinessSubkey = "{";
		var insertField = '[';

		//var thisBusinessKeys = new Map();

		var actionItems = ['0','1','2','3'];
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
					var businessKeySpan = labels[x]['label'];
					var inputValue= "";

					Readonly = "";

					// 修正模式的场合
					if (isEditFlg) {
						inputValue = rowData[labels[x]['field']];// 表示值

						// TODO:新增加Cloum处理rowData[labels[x]['field']]
						if (rowData[labels[x]['field']]==undefined) {
							//displayStr = '';
							insertField = insertField + "'" + labels[x]['field'] + "',";

						}else {
							//inputValue = rowData[labels[x]['field']];
						}


						switch(labels[x]['editor']){
						case 'Master':	//alert(eval('(' + JSON.stringify(labels[x]['options']) + ')'));
							//alert(labels[x]['field'] + ":" +rowData[labels[x]['field']]);
							for(var key in labels[x]['options']){

								if (rowData[labels[x]['field']] == undefined) {
									//insertField = insertField + "'" + labels[x]['field'] + "',";
								} else {
									if(labels[x]['options'][key]==rowData[labels[x]['field']]) {
										jsonRowData = jsonRowData + labels[x]['field'] + `:"` + key + `",`;
									}
								}

//								if(labels[x]['options'][key]==rowData[labels[x]['field']]) {
//								jsonRowData = jsonRowData + labels[x]['field'] + `:"` + key + `",`;
//								}
							}
							break;
						default:
							jsonRowData = jsonRowData + labels[x]['field'] + `:"` + rowData[labels[x]['field']] + `",`;
						break;
						}
						// Add模式的场合
					} else {
						Readonly = "";// 只读
					}
					// 取得本画面的主键信息(标签红色处理)
					var businessKeyFlg = false;
					businessKeyList.find(function(val){
						if (val == labels[x]['field'] ) {
							businessKeySpan = "<span style=\"color:red\">*" + labels[x]['label'] + "</span>";
							// 编辑状态只读
							if (isEditFlg){
								Readonly = " disabled=\"disabled\" ";
								switch(labels[x]['editor'])
								{
								case 'Master':	//alert(eval('(' + JSON.stringify(labels[x]['options']) + ')'));
									for(var key in labels[x]['options']){
										if(labels[x]['options'][key]==rowData[labels[x]['field']]) {
											jsonBusinessSubkey = jsonBusinessSubkey + labels[x]['field'] + `:"` + key + `",`;
										}
									}
									break;
								default:
									jsonBusinessSubkey = jsonBusinessSubkey + labels[x]['field'] + `:"` + rowData[labels[x]['field']] + `",`;
								break;
								}
							}
						} 
					});


					contentHtml = contentHtml + "<div class=\"layui-form-item\">" +
					"<label class=\"layui-form-label\">" + businessKeySpan + "</label>" +
					"<div class=\"layui-input-inline\">";

					switch(labels[x]['editor'])
					{
					case 'String':							
						contentHtml = contentHtml +  "<input type=\"text\"" + Readonly + " id=\"" + labels[x]['field']  + "\" lay-verify=\"\"" +
						"autocomplete=\"off\" class=\"layui-input\" value=\"" + inputValue + "\">";
						break;
					case 'Byte':
						break;
					case 'Integer':
						contentHtml = contentHtml +  "<input type=\"text\" id=\"" + labels[x]['field'] + "\" id=\"" + labels[x]['field'] + "\" required=\"\"" +
						"autocomplete=\"off\" class=\"layui-input\" value=\"" + inputValue + "\">";
						break;
					case 'Short':
						break;
					case 'Long':
						break;
					case 'Character':
						break;
					case 'Boolean':
						contentHtml = contentHtml +  "<input type=\"text\" id=\"" + labels[x]['field'] + "\" id=\"" + labels[x]['field'] + "\" required=\"\"" +
						"autocomplete=\"off\" class=\"layui-input\" value=\"" + inputValue + "\">";
						break;
					case 'Date':
						contentHtml = contentHtml +  "<input type=\"text\" id=\"" + labels[x]['field'] + "\" id=\"" + labels[x]['field'] + "\" required=\"\"" +
						"autocomplete=\"off\" class=\"layui-input\" value=\"" + inputValue + "\">";
						break;
					case 'Float':
					case 'Double':
						break;
					case 'BigDecimal':
						break;
					case 'Master':// Master对象
						//contentHtml = contentHtml +  "<select  type=\"text\" class=\"select\"" + Readonly + " id=\"" + labels[x]['field'] +"\" style=\"pointer-events: none;\">";
						contentHtml = contentHtml +  "<select  type=\"text\" class=\"select\"" + Readonly + " id=\"" + labels[x]['field'] +"\">";
						for (var key in labels[x]['options']) {
							// Hidden项非表示
							//alert(key + labels[x]['options'][key]);

							if (isEditFlg) {
								if (inputValue == labels[x]['options'][key]) {
									contentHtml = contentHtml +  "	<option value=\"" + key + "\"" + " selected=\"selected\">" + labels[x]['options'][key] + "</option>";
								} else {
									contentHtml = contentHtml +  "	<option value=\"" + key + "\"" + ">" + labels[x]['options'][key] + "</option>";
								}
							} else {
								// TODO：设置默认下拉框选项（不允许空选项，不然在新追加表列的时候没办法判断是否是新加项。如果不用热添加功能，可以不用设置默认选项）
								if (key == labels[x]['defaultvalue']) {
									jsonBusinessSubkey = jsonBusinessSubkey + labels[x]['field'] + `:"` + key + `",`;
									contentHtml = contentHtml +  "	<option value=\"" + key + "\"" + " selected=\"selected\">" + labels[x]['options'][key] + "</option>";

								} else {
									contentHtml = contentHtml +  "	<option value=\"" + key + "\"" + ">" + labels[x]['options'][key] + "</option>";
								}
							}
						}
						contentHtml = contentHtml + "</select>";
						break;
					default:
						contentHtml = contentHtml +  "<input type=\"text\" id=\"" + labels[x]['field'] + "\" id=\"" + labels[x]['field'] + "\" required=\"\"" +
						"autocomplete=\"off\" class=\"layui-input\" value=\"" + inputValue + "\">";
					}
					contentHtml= contentHtml +
					" </input></div>" +
					"</div>";
				}

				insertField = insertField + ']';
				contentHtml = contentHtml + "</div></div>"  + 
				// 按钮右侧对齐
				"<div align=\"right\" style=\"border:15px solid #fff;bottom:0;left:0;\">";
				// 流程元素处理
				var buttonItems = "";
				var propertyItem= "";
				for (x in workflowList) {

					switch(workflowList[x]['editor'])
					{
					case 'Action':
						buttonItems = buttonItems + "<button class=\"layui-btn\" top=\"50%\" lay-submit=\"\" id=\"" + workflowList[x]['kayamodelid'] + "\">" + workflowList[x]['label'] + "</button>";
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

				// 没有绑定WorkFlow的场合
				if (workflowList.length==0) {
					if (isEditFlg) {
						contentHtml = contentHtml + "<button class=\"layui-btn\" lay-submit id=\"update\">Ok</button>";
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

		contentHtml = contentHtml +"</div></div></div>";

		jsonRowData = jsonRowData + `}`;
		jsonBusinessSubkey = jsonBusinessSubkey + `}`; 
		// alert(dataText);
		//字符串转json
		jsonRowData = eval('(' + jsonRowData + ')');
		jsonBusinessSubkey = eval('(' + jsonBusinessSubkey + ')');
		//alert(JSON.stringify(jsonBusinessSubkey));
		layer.open({
			type: 1
			,title: Title //不显示标题栏
			,closeBtn: false
			,area: ['500px', '\'' + hi + 'px\'']
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

			//alert(JSON.stringify(jsonBusinessSubkey));
			form = layui.form;
			//form.render('select');
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
					//alert($("input[name='name']").val());
					
					
					 //verify = form.config.verify;
					 //alert(JSON.stringify(verify));
					 
					//ver = $(this).attr("lay-verify");
					//alert(ver);
					//if(!jy()){
						//alert('false');
					//}
					switch($(this).attr("class"))
					{
					case 'layui-input':
						// 只有更改过的字段才会传到后台
//						if (jsonRowData[this.id]!=this.value) {
//						jsonBusinessSubkey[this.id]=this.value;
//						}

						// 传递所有值到后台，参与后台的业务逻辑处理
						jsonBusinessSubkey[this.id]=this.value;
						break;
					default:
						//inputCenter = inputCenter + "easyui-textbox";
					}
				});


				form.render();

				var actionId = $(this).attr("id");
				inserteOrUpdateRow(kayaModelId,actionId,jsonBusinessSubkey,insertField,Title);

			});
		}
		});

	});
}


//追加新行
function inserteOrUpdateRow(kayaModelId,actionId,data,insertField,Title){
	//alert("kayaModelId:" + kayaModelId);
	//alert("adtionId:" + actionId);
	//alert("data:" + JSON.stringify(data));
	//alert("insertField:" + insertField);
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

//	if(businessKeysData!=null) {
//	//alert(JSON.stringify(businessKeysData));
//	kvParamaterList.push(businessKeysData);
//	} else {
//	kvParamaterList.push(eval('({})'));
//	}
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
			'insertField': insertField
		},
		success : function(data) {
			//alert("Inserted Success");
			doSearch(kayaModelId,Title);
		}
	});
	layer.closeAll();


	// 更新父类页面行变更数据
	// 如果只有一条数据，默认选中状态
}

function deleteRow(kayaModelId,rowsData,title) {
	if(rowsData.length === 0){
		layer.msg('请选择一行');
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
				'kayaModelId' : kayaModelId
			},
			success : function(data) {
				doSearch(kayaModelId,title);
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
function jy(){//仿照源码写的校验，返回布尔类型
    verify = form.config.verify, stop = null
    ,DANGER = 'layui-form-danger', field = {} ,elem = $('.layui-form')
    
    ,verifyElem = elem.find('*[lay-verify]') //获取需要校验的元素
    ,formElem = elem //获取当前所在的form元素，如果存在的话
    ,fieldElem = elem.find('input,select,textarea') //获取所有表单域
    ,filter = '*'; //获取过滤器
    alert(JSON.stringify(verify));
    //开始校验
    layui.each(verifyElem, function(_, item){
      var othis = $(this), ver = othis.attr('lay-verify'), tips = '';
      var value = othis.val(), isFn = typeof verify[ver] === 'function';
      othis.removeClass(DANGER);
      if(verify[ver] && (isFn ? tips = verify[ver](value, item) : !verify[ver][0].test(value)) ){
        layer.msg(tips || verify[ver][1], {
          icon: 5
          ,shift: 6
        });
        //非移动设备自动定位焦点
        if(!device.android && !device.ios){
          item.focus();
        }
        othis.addClass(DANGER);
        return stop = true;
      }
    });
    
    if(stop) {return false;}
    else{return true;}
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
			alert("模型更新成功！");

		}
	});
}