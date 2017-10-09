/**
 * Created by wangzhuoya on 2017/7/7.
 */
G2.Global.colors['default'] = ['#61A5E8','#7ECF51','#EECB5F','#9570E5','#E3935D'];
/**折线图**/
function lineChartModal(data,id,x,y,z,xTtile,yTtile,chartHeight,defs){
    $("#"+id).html("");
    $("#"+id+"_slider").remove();
    var chart = new G2.Chart({
        id: id,
        forceFit: true,
        height: chartHeight,
        plotCfg: {
            margin: [20, 45, 65, 50] // 上，右，下，左
          }
    });
    chart.source(data,defs);
    chart.legend({
        position: 'top', // 设置图例的显示位置
        itemWrap: true,
        dy: -20 // 整个图例的垂直偏移距离
    });
    if(isEmpty(xTtile)){
        chart.axis(x, {
            title: null
        });
    }
    if(isEmpty(yTtile)){
        chart.axis(y, {
            title: null
        });
    }
    chart.line().position(x+'*'+y).color(z).style({
    	lineWidth: 2
    });
//    chart.render();
    $('<div id="'+id+'_slider"></div>').insertAfter("#"+id);
    var day1 = new Date();
    day1.setTime(day1.getTime()-24*60*60*1000);
    var s1 = day1.getFullYear()+"-" + (day1.getMonth()+1) + "-" + day1.getDate();
    var slider = new G2.Plugin.slider({
        domId: id+'_slider', //DOM id
        height: 25,
        width: 800, 
        xDim: x,
        yDim: y,
        charts: chart,
        end : s1
    });
    slider.render();
    download(data,id,chart);
    if(data == null || data.length == 0){
        tips(id,chartHeight);
    }
    return chart;
}
/**折线图**/
function lineChartNoSpider(data,id,x,y,z,xTtile,yTtile,chartHeight,defs){
    $("#"+id).html("");
    var chart = new G2.Chart({
        id: id,
        forceFit: true,
        height: chartHeight,
        plotCfg: {
            margin: [20, 40, 65, 50] // 上，右，下，左
          }
    });
    chart.source(data,defs);
    chart.legend({
        position: 'top', // 设置图例的显示位置
        itemWrap: true,
        dy: -20 // 整个图例的垂直偏移距离
    });
    if(isEmpty(xTtile)){
        chart.axis(x, {
            title: null
        });
    }
    if(isEmpty(yTtile)){
        chart.axis(y, {
            title: null
        });
    }
    chart.line().position(x+'*'+y).color(z);
    chart.render();
    download(data,id,chart);
    if(data == null || data.length == 0){
        tips(id,chartHeight);
    }
    return chart;
}

/** 柱状图 **/
function histogramModal(data,id,x,y,xTtile,yTtile,chartHeight,defs){
    $("#"+id).html("");
    // Step 1: 创建 Chart 对象
    var chart = new G2.Chart({
        id: id, // 指定图表容器 ID
        forceFit: true,
        height : chartHeight // 指定图表高度
    });
    // Step 2: 载入数据源,定义列信息
    chart.source(data,defs);
    if(isEmpty(xTtile)){
        chart.axis(x, {
            title: null
        });
    }
    if(isEmpty(yTtile)){
        chart.axis(y, {
            title: null
        });
    }
    chart.tooltip(true, {
  	  title: null // 默认标题不显示
  	});
    chart.legend(false);
    // Step 3：创建图形语法，绘制柱状图，由 x 和 y 两个属性决定图形位置，x 映射至 x 轴，y 映射至 y 轴
    chart.interval().position(x+'*'+y).color(x).label(y).size(25);
    // Step 4: 渲染图表
    chart.render();
    download(data,id,chart);
    if(data == null || data.length == 0){
        tips(id,chartHeight);
    }
    return chart;
}

/** 饼图 **/
function pieChartModal(data,id,x,y,chartHeight){
    $("#"+id).html("");
    var Stat = G2.Stat;
    var chart = new G2.Chart({
        id: id,
        forceFit: true,
        height: chartHeight
    });
    chart.source(data);
    // 重要：绘制饼图时，必须声明 theta 坐标系
    chart.coord('theta', {
        radius: 0.8 // 设置饼图的大小
    });
    chart.legend(x, {
        position: 'bottom',
        itemWrap: true
    });
    chart.intervalStack()
        .position(Stat.summary.percent(y))
        .color(x)
        .label(x, {
            renderer: function(text, item, index) {
                var point = item.point; // 每个弧度对应的点
                var percent = point['..percent']; // ..percent 字段由 Stat.summary.percent 统计函数生成
                percent = (percent * 100).toFixed(2) + '%';
                return text + ' ' + percent;
            }
        });
    chart.render();
    download(data,id,chart);
    if(data == null || data.length == 0){
        tips(id,chartHeight);
    }
    return chart;
}

/** 横向柱状图 **/
function horizontalHistogramModal(data,id,x,y,chartHeight){
    $("#"+id).html("");
    var Frame = G2.Frame;
    var frame = new Frame(data);
    frame = Frame.sort(frame, y); // 将数据按照y进行排序，由大到小
    var chart = new G2.Chart({
        id : id,
        forceFit: true,
        height: chartHeight,
        plotCfg: {
            margin: [20, 60, 20, 120]
        }
    });
    if(data == null || data.length == 0){
        tips(id,chartHeight);
        return;
    }
    chart.source(frame);
    chart.axis(x,{
        title: null
    });
    chart.tooltip(true, {
  	  title: null // 默认标题不显示
  	});
    chart.legend(false);
    chart.coord('rect').transpose();
    chart.interval().position(x+'*'+y).color(x).label(y).size(15);
    chart.render();
    download(data,id,chart);
    if(data == null || data.length == 0){
        tips(id,chartHeight);
    }
    return chart;
}


/** 漏斗图 **/
function funnelModal(data,id,x,y,type,chartHeight){
    $("#"+id).html("");
    var chart = new G2.Chart({
        id: id,
        forceFit: true,
        height: chartHeight
    });
    if(data == null || data.length == 0){
        tips(id,chartHeight);
        return chart;
    }
    chart.source(data);
    chart.legend(false);
    chart.coord('rect').transpose().scale(1,-1);
    chart.axis(false);
    var jsonData = data.sort(function(a, b){return (a.value < b.value) ? 1 : -1});
    chart.intervalSymmetric().position(x+'*'+y).color(x).shape('pyramid').label(x, {
        offset:5,
        renderer: function(text, item, index) {
            var point = item.point; // 每个弧度对应的点
            $.each(jsonData,function(idx){
                if(jsonData[idx].value == point.value && jsonData[idx].name == text){
                    funnelIndex = idx;//idx为数组下标
                }
            });
            if(point.value == 0 || funnelIndex == 0){
                return text + '\n'+type+"总量"+point.value;
            }else{
                var ratio = point.value / jsonData[funnelIndex-1].value * 100;
                return text + '\n'+type+"总量"+point.value+"\n占比" + ratio.toFixed(2) +"%";
            }
        }
    }).animate({
        appear:{
            animation:'zoomIn'
        },
        enter:{
            animation:'fadeIn',
            easing:'easeInQuart'
        }
    });
    chart.render();
    chart.on('tooltipchange', function(ev) {
        var items = ev.items; // tooltip显示的项
        var origin = items[0]; // 将一条数据改成多条数据
        var proportion;
        $.each(yDataAmonut1,function(idx){
            if(yDataAmonut1[idx].value == origin.value && yDataAmonut1[idx].name == origin.name){
                funnelIndex = idx;//idx为数组下标
            }
        });
        if(origin.value != 0 && funnelIndex != 0){
            var ratio = origin.value / yDataAmonut1[funnelIndex-1].value * 100;
            proportion = ratio.toFixed(2) +"%";
        }
        items.splice(0); // 清空
        items.push({
            name: type+'总量',
            title: origin.title,
            marker: true,
            color: origin.color,
            value: origin.value
        });
        if(proportion != undefined){
            items.push({
                name: '占比',
                marker: true,
                title: origin.title,
                color: origin.color,
                value:proportion
            });
        }
    });
    download(data,id,chart);
    return chart;
}

function isEmpty(m) {
    if (null == m || m.trim() == "") {
        return true;
    } else {
        return false;
    }
}

function download(data,id,chart){
    if(data!="" && data!=null){
        if($("#download_"+id).length <= 0){
            var html = "<label id='download_"+id+"' style='float: right;font-size:20px!important;padding-right: 5px;color: #669fc7;'>"+
                "<i class='ace-icon fa fa-cloud-download'></i>"+
                "</label>";
            $("#"+id).parent().parent().children().children("h4").append(html);
        }
        $("#download_"+id).unbind("click");
        $("#download_"+id).on("click",function(){
            chart.downloadImage();
        })
    }else{
        if($("#download_"+id).length > 0){
            $("#download_"+id).remove();
        }
    }
}

function tips(id,chartHeight){
   var str =  "<div style='top: 50%;left: 50%;position: absolute;transform: translate(-50%, -50%);'><img style='width:18.89px;height:18.89px' src='../images/sad-big.png' /><span style='color:#333333;padding-left: 2px'>暂无数据</span></div>";
    $("#"+id).html("");
    $("#"+id).css("height",chartHeight);
    $("#"+id).append(str);
}