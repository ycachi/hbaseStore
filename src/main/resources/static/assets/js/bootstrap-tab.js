var addTabs = function (options) {
    //var rand = Math.random().toString();
    //var id = rand.substring(rand.indexOf('.') + 1);
    var url = window.location.protocol + '//' + window.location.host;
    options.url = url + options.url;
    id = "tab_" + options.id;
    $(".main-content .active").removeClass("active");
    //如果TAB不存在，创建一个新的TAB
    if ($("#" + id).length == 0) {
        //固定TAB中IFRAME高度
        //mainHeight = $(document.body).hei ght() - 90;
        //创建新TAB的title
        title = '<li role="presentation" id="tab_' + id + '"><a href="#' + id + '" aria-controls="' + id + '" role="tab" data-toggle="tab">' + options.title;
        //是否允许关闭
        if (options.close) {
            title += ' <i class="glyphicon glyphicon-remove" tabclose="' + id + '"></i>';
        }
        title += '</a></li>';

        //是否指定TAB内容
        if (options.content) {
            content = '<div role="tabpanel" class="tab-pane" id="' + id + '">' + options.content + '</div>';
        } else {//没有内容，使用IFRAME打开链接
            if (options.title0 == undefined) {
                content = '<div role="tabpanel" class="tab-pane" id="' + id + '"><div class="breadcrumbs" id="breadcrumbs' + options.id + '"><script type="text/javascript">try{ace.settings.check("breadcrumbs" , "fixed")}catch(e){}</script>' +
                    '<ul class="breadcrumb"><li><i class="ace-icon fa fa-home home-icon"></i><a id="1stBreadcrumb" href="javascript:void(0)">' + options.stBreadcrumb + '</a></li>' +
                    '<li id="2ndBreadcrumb" class="active">' + options.title + '</li></ul></div><iframe src="' + options.url + '" id="mainFrame' + options.id + '" name="mainFrame' + options.id + '" width="100%" height=""  frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes" onLoad="iFrameHeight(\'' + options.id + '\')"></iframe></div>';
            } else {
                content = '<div role="tabpanel" class="tab-pane" id="' + id + '"><div class="breadcrumbs" id="breadcrumbs' + options.id + '"><script type="text/javascript">try{ace.settings.check("breadcrumbs" , "fixed")}catch(e){}</script>' +
                    '<ul class="breadcrumb"><li><i class="ace-icon fa fa-home home-icon"></i><a id="1stBreadcrumb" href="javascript:void(0)">' + options.stBreadcrumb + '</a></li>' +
                    '<li id="2ndBreadcrumb" class="active">' + options.title0 + '</li>' +
                    '<li id="2ndBreadcrumb" class="active">' + options.title + '</li></ul></div><iframe src="' + options.url + '" id="mainFrame' + options.id + '" name="mainFrame' + options.id + '" width="100%"  frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes" onLoad="iFrameHeight(\'' + options.id + '\')"></iframe></div>';
                //'<li id="2ndBreadcrumb" class="active">'+options.title+'</li></ul></div><iframe src="' + options.url + '" id="mainFrame'+options.id+'" name="mainFrame'+options.id+'" width="100%" height="1200" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes" onLoad="iFrameHeight('+options.id+')"></iframe></div>';
            }
        }
        //加入TABS
        $(".nav-tabs").append(title);
        $(".tab-content").append(content);
    }
    //激活TAB
    $("#tab_" + id).addClass('active');
    $("#" + id).addClass("active");
};
var closeTab = function (id) {
    $(".nav-list .active").removeClass("active");
    //如果关闭的是当前激活的TAB，激活他的前一个TAB
    if ($(".nav-tabs li.active").attr('id') == "tab_" + id) {
        $("#tab_" + id).prev().addClass('active');
        $("#" + id).prev().addClass('active');
        var menu2Id = $("#" + id).prev().attr("id").split("_")[1];
        $(".nav-list .submenu li[menu2id=" + menu2Id + "]").addClass('active');
        $(".nav-list .submenu li[menu2id=" + menu2Id + "]").parent().parent().addClass('active');
    }
    //关闭TAB
    $("#tab_" + id).remove();
    $("#" + id).remove();
};
$(function () {
    $(".nav-tabs").on("click", "[tabclose]", function (e) {
        id = $(this).attr("tabclose");
        closeTab(id);
    });
});
	
