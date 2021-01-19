function addVerify(form) {
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
}