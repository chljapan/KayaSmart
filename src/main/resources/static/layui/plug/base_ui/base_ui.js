layui.define(["jquery"],function (exports) {
	var obj = {

			/**
			 * 信息详细的分组处理（Group）s
			 * @param columns 后台表元素信息
			 * @param rowData 选择行信息
			 * @param contentHtml 动态编辑UI信息
			 * @param businessKeys_Json 主键信息
			 * @returns
			 */
			editeDetailedInfo : function (columns,rowData,contentHtml,businessKeys_Json) {
				for (x in columns) {
					if (columns[x].uniquekey) {
						// 本画面的主键信息
						//var arr = {columns[x]['field'].val(): rowData[columns[x]['field']].val()};
						//label = "<span style=\"color:red\">" + columns[x].title + "</span>";
						businessKeys_Json[columns[x]['field']] = rowData[columns[x]['field']];
					}
					contentHtml = contentHtml + "<tr><td>";
					var businessKeyText = rowData[columns[x]['field']];
					// Master名称显示处理（可根据需求更改）
					if(columns[x].modeltype=='MasterReference') {
						for(var items in columns[x]['editor'].options.data){
							// 通过Text查询Id 设置显示内容
							if (rowData[columns[x]['field']] == columns[x]['editor'].options.data[items].id && rowData[columns[x]['field']]!='') {
								businessKeyText = rowData[columns[x]['field']] + ':' + columns[x]['editor'].options.data[items].text;
								contentHtml = contentHtml + columns[x]['title'] + "</td><td>" + businessKeyText + "</td>";
								break;
							}
						}
						// Group 组件参数处理
					}else if(columns[x].modeltype=='Group') {
						//contentHtml = contentHtml + columns[x]['title'] + "</td><td>" + '' + "</td>";
						// 递归处理Group的子项内容
						var _contentHtml = "<span style=\"font-size:16px;font-style:italic;font-weight:bold;\">" + columns[x]['title'] +"</span></td><td>" + '' + "</td>";

						//_contentHtml = _contentHtml + "<fieldset class=\"layui-elem-field\"><legend><span style=\"font-size:16px;font-style:italic;font-weight:bold;\">" + label +"</span></legend>";
						// 递归处理Group的子项内容
						_contentHtml = this.editeDetailedInfo(columns[x].editor.options.group,rowData,_contentHtml,businessKeys_Json);

						contentHtml = contentHtml + _contentHtml;

						// 其他组件参数通用处理
					} else {
						contentHtml = contentHtml + columns[x]['title'] + "</td><td>" + businessKeyText + "</td>";
					}
					
					
				}
				return contentHtml;
			},

			/**
			 * 表头Columns的分组处理（Group）s
			 * @param columns 后台表头信息
			 * @param levelMap 分层存储变量
			 * @returns
			 */
			editeTableColumns : function (columns,levelMap) {

				for (x in columns) {
					var columnStyle = '';
					// Master名称显示处理（可根据需求更改）
					if(columns[x].modeltype=='MasterReference') {
						columnStyle = "<th lay-data=\"{sort:true,field:\'" + columns[x].field + "_NM\'}\"" +  " rowspan=\"" +columns[x].rowspan + "\">" + columns[x].title + "</th>";
						// Group 组件参数处理
					}else if(columns[x].modeltype=='Group' || columns[x].modeltype=='WorkFlow') {
						columnStyle = "<th lay-data=\"{align:\'center\',field:\'" + columns[x].field + "\'}\"" +  " colspan=\"" +columns[x].colspan + "\">" + columns[x].title + "</th>";
						this.editeTableColumns(columns[x]['editor'].options.group,levelMap);
						// 其他组件参数通用处理
//					} else if(columns[x].modeltype=='WorkFlow') {
//						columnStyle = "<th lay-data=\"{align:\'center\',field:\'" + columns[x].field + "\'}\"" +  " colspan=\"" +columns[x].colspan + "\">" + columns[x].title + "</th>";
//						this.editeTableColumns(columns[x]['editor'].options.group,levelMap);
						// 其他组件参数通用处理
					} else {
						columnStyle = "<th lay-data=\"{sort:true,field:\'" + columns[x].field + "\'}\"" +  " rowspan=\"" +columns[x].rowspan + "\">" + columns[x].title + "</th>";
					}

					// 最新的层信息更新（存在则替换）
					if (levelMap.has(columns[x].rowspan)) {
						levelMap.set(columns[x].rowspan,levelMap.get(columns[x].rowspan) + columnStyle);

					} else {
						levelMap.set(columns[x].rowspan,columnStyle);
					}
				}
			},
			/**
			 * 登陆更新Column的分组处理（Group）
			 * @param column 后台表头信息
			 * @param levelMap 分层存储变量
			 * @param isEditFlg 编辑Flg
			 * @param rowData 编辑数据
			 * @param jsonBusinessSubkey 主键信息
			 * @returns
			 */
			editAddRows : function (columns, contentHtml,isEditFlg,rowData,jsonBusinessSubkey) {

				for (x in columns) {
					var inputValue= "";
					var Readonly = "";
					if (isEditFlg) {
						inputValue = rowData[columns[x].field];
						
						if (columns[x].uniquekey) {
							// 本画面的主键信息
							Readonly = " disabled=\"disabled\" ";
							jsonBusinessSubkey[columns[x].field] = rowData[columns[x].field];
						}
					}
					var columnsStyle = '';
					// Master名称显示处理（可根据需求更改）
					//alert(columns[x].uniquekey + ':' + columns[x].title);
					var label = "";

					if (columns[x].uniquekey) {
						// 本画面的主键信息
						label = "<span style=\"color:red\">" + columns[x].title + "</span>";
					} else {
						label = columns[x].title;
					}

					switch(columns[x].modeltype) {
					case 'MasterReference':
						contentHtml = contentHtml + "<div class=\"layui-form-item\"  style=\"margin-top: 5px;\">";
						//contentHtml = contentHtml +  "<label class=\"layui-form-label\">" + label + "</label><div class=\"layui-input-inline \" style=\"width:"+ "140" + "\"><select  type=\"text\" class=\"select\""  + " lay-filter=\"" + columns[x].field + "\" id=\"" + columns[x].field +"\" name=\"" + columns[x].field + "\">";
						contentHtml = contentHtml +  "<label class=\"layui-form-label\">" + label + "</label><div class=\"layui-input-block \" style=\"width:"+ "140" + "\"><select type=\"text\" class=\"layui-input\""  + Readonly + " lay-filter=\"" + columns[x].field + "\" id=\"" + columns[x].field +"\" name=\"" + columns[x].field + "\">";
						for (var key in columns[x].editor.options.data) {

//							alert(columns[x]['editor'].options.data[key].id);
							if (isEditFlg) {
								//alert(inputValue);
								if (inputValue == columns[x].editor.options.data[key].id) {
									contentHtml = contentHtml + "<option value=\"" + columns[x].editor.options.data[key].id + "\"" + " selected=\"selected\">" + columns[x].editor.options.data[key].text + "</option>";
								} else {
									contentHtml = contentHtml +  "<option value=\"" + columns[x].editor.options.data[key].id + "\"" + ">" + columns[x].editor.options.data[key].text + "</option>";
								}
							} else {
								// TODO：设置默认下拉框选项（不允许空选项，不然在新追加表列的时候没办法判断是否是新加项。如果不用热添加功能，可以不用设置默认选项）
								if (key == columns[x]['defaultvalue']) {
									contentHtml = contentHtml +  "<option value=\"" + columns[x].editor.options.data[key].id + "\"" + " selected=\"selected\">" + columns[x].editor.options.data[key].text + "</option>";

								} else {
									//contentHtml = contentHtml +  "<option value=\"" + columns[x].editor.options.data[key].id + "\"" + ">" + columns[x].editor.options.data[key].text + "</option>";
									contentHtml = contentHtml +  "<option value=\"" + columns[x].editor.options.data[key].id + "\""+ " name=\"" + columns[x].field + "_NM\"" + ">" + columns[x].editor.options.data[key].text + "</option>";
								}
								//alert(contentHtml);
							}
						}
						contentHtml = contentHtml + "</select></input></div></div>";
						break;
					case 'Group':
						var _contentHtml ="<div class=\"layui-form-item\"  style=\"margin-top: 5px;\">";

						_contentHtml = _contentHtml + "<fieldset class=\"layui-elem-field\"><legend><span style=\"font-size:16px;font-style:italic;font-weight:bold;\">" + label +"</span></legend>";
						// 递归处理Group的子项内容
						_contentHtml = this.editAddRows(columns[x].editor.options.group,_contentHtml,isEditFlg,rowData,jsonBusinessSubkey)+ "</fieldset>";

						contentHtml = contentHtml + _contentHtml+ "</div>";;

						break;
					case 'Role':
						//continue;
						break;
					default:
						contentHtml = contentHtml + "<div class=\"layui-form-item\"  style=\"margin-top: 5px;\">";
						switch(columns[x].datatype){
						case 'Auto':
							if (isEditFlg){
								contentHtml = contentHtml +  "<label class=\"layui-form-label\">" + label + "</label><div class=\"layui-input-inline\" style=\"width:"+ "140" + "\"><input hidden = 'true' type=\"text\"" + Readonly + " id=\"" + columns[x].field + "\" name=\"" + columns[x].field + "\" required=\"\"" +
								"autocomplete=\"off\" class=\"layui-input\" value=\"" + inputValue + "\"></input></div></div>";
							} else {
								contentHtml = contentHtml +  "<div class=\"layui-input-inline\" style=\"width:"+ "140" + "\"><input style=\"display: none\" type=\"text\"" + Readonly + " id=\"" + columns[x].field + "\" name=\"" + columns[x].field + "\"" +
								"autocomplete=\"off\" class=\"layui-input\" value=\"" + inputValue + "\"></input></div></div>";
							}
							break;
						case 'boolean':
							contentHtml = contentHtml +  "<label class=\"layui-form-label\">" + label + "</label><div class=\"layui-input-inline\"><input type=\"checkbox\" class=\"layui-input\"" + Readonly +  "lay-skin=\"switch\" lay-text=\"ON|OFF\" name = \"" + columns[x].field + "\" id = \"" + columns[x].field + "\"";
							if(inputValue=='on'){
								contentHtml = contentHtml +  " checked ";
							}
							contentHtml = contentHtml +  "/></input></div></div>";
							break;
						default:
							contentHtml = contentHtml +  "<label class=\"layui-form-label\">" + label + "</label><div class=\"layui-input-block\" style=\"width:"+ "140" + "\"><input class=\"layui-input\" type=\"text\"" + Readonly + " id=\"" + columns[x].field + "\" name=\"" + columns[x].field + "\" required=\"\"" +
							"autocomplete=\"off\" value=\"" + inputValue + "\"></input></div></div>";
						} 
					}
				}
				return contentHtml;
			}
	}
	exports("base_ui",obj);
});