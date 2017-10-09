function iFrameHeight(id) {
    var ifm = document.getElementById("mainFrame" + id);
    var subWeb = document.frames ? document.frames["mainFrame"].document : ifm.contentDocument;
    if (ifm != null && subWeb != null) {
        var docHeight = document.documentElement.clientHeight;
        var navbarHeight = document.getElementById("navbar").offsetHeight;
        var breadcrumbsHeight = document.getElementById("breadcrumbs" + id).offsetHeight;
        var footerHeight = 80;
        var ifmMinHeight = docHeight - navbarHeight - breadcrumbsHeight - footerHeight;
        ifm.height = subWeb.body.scrollHeight > ifmMinHeight ? subWeb.body.scrollHeight : ifmMinHeight;
    }
}
/**
 * 高亮选中的菜单项
 * @return {[type]} [description]
 */
function activeMenu(obj, url) {
	//$("#mainFrame").attr('src',url)
	//移除所以li的active状态
	if(url=="/rhino"){
		return;
	}
	$(".submenu li").removeClass('active');
	$(".nav-list li").removeClass('active');
	//高亮当前选择菜单和父菜单
	$(obj).parent().addClass('active');
	$(obj).parent().parent().parent().addClass('active');
	//更新面包屑
	var options = new Object();
	options.url = $(obj).parent().attr("url");
	if($(obj).parent().attr("menuId")!=undefined){
		options.id = $(obj).parent().attr("menuId");
		options.title = $(obj).parent().attr("title");
		options.stBreadcrumb = $(obj).find(".menu-text").text();
	}else if($(obj).parent().attr("menu2Id")!=undefined){
		options.id = $(obj).parent().attr("menu2Id");
		options.title = $(obj).parent().attr("title");
		options.stBreadcrumb = $(obj).parent().parent().parent().find(".menu-text").text();
	}else{
		options.id = $(obj).parent().attr("menu3Id");
		options.title = $(obj).parent().attr("title");
		options.title0 = $(obj).parent().parent().parent().attr("title");
		options.stBreadcrumb = $(obj).parent().parent().parent().parent().parent().find(".menu-text").text();
	}
	options.close = true;
	if(options.url!=""){
	addTabs(options);
	}
}



