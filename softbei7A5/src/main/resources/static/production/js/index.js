$(document).ready(function() {
	getAllImgNumberAndProcessTime();
	getLastImgProcessInformation();
	showLast7Performance();
});

function getAllImgNumberAndProcessTime() {
	$.ajax({
		url: "/record/get_all_img_number_and_process_time",
		type: "GET",
		dataType: "json",
		cache: false,//设置不缓存
		success: getAllImgNumberAndProcessTimeSuccess,
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			if (XMLHttpRequest.status >= 400 && XMLHttpRequest.status < 500) {
				alert("客户端请求出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
			} else {
				if (XMLHttpRequest.status >= 500 || XMLHttpRequest.status <= 600) {
					alert("服务器出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
				} else {
					if (XMLHttpRequest.status >= 300 || XMLHttpRequest.status < 400) {
						alert("重定向问题！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
					} else {
						alert("抱歉，系统好像出现一些异常！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
					}
				}
			}
		}
	});
}

function getAllImgNumberAndProcessTimeSuccess(data) {
	if (data["status"] == "success") {
		$("#all-img-count").html(data["img_all"]);
		$("#all-process-time").html(data["process_time_all"].toFixed(3));
	} else {
		alert("获取数据失败！");
	}
}

function getLastImgProcessInformation() {
	$.ajax({
		url: "/record/get_last_img_process_information",
		type: "GET",
		dataType: "json",
		cache: false,//设置不缓存
		success: getLastImgProcessInformationSuccess,
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			if (XMLHttpRequest.status >= 400 && XMLHttpRequest.status < 500) {
				alert("客户端请求出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
			} else {
				if (XMLHttpRequest.status >= 500 || XMLHttpRequest.status <= 600) {
					alert("服务器出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
				} else {
					if (XMLHttpRequest.status >= 300 || XMLHttpRequest.status < 400) {
						alert("重定向问题！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
					} else {
						alert("抱歉，系统好像出现一些异常！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
					}
				}
			}
		}
	});
}

function getLastImgProcessInformationSuccess(data) {
	if (data["status"] == "success") {
		$("#last-img-all").html(data["last_img_all"]);
		var compareImgAll = data["compare_img_all"];
		if (compareImgAll["status"] == "up") {
			var str = '比上次<i class="green"><i class="fa fa-sort-asc"></i>' + compareImgAll["number"].toFixed(2) + '%</i>';
			$("#compare-img-all").html(str);
		} else {
			var str = '比上次<i class="red"><i class="fa fa-sort-desc"></i>' + compareImgAll["number"].toFixed(2) + '%</i>';
			$("#compare-img-all").html(str);
		}
		$("#last-process-time").html(data["last_process_time"].toFixed(3));
		var compareProcessTime = data["compare_process_time"];
		if (compareProcessTime["status"] == "up") {
			var str = '比上次<i class="red"><i class="fa fa-sort-asc"></i>' + compareProcessTime["number"].toFixed(2) + '%</i>';
			$("#compare-process-time").html(str);
		} else {
			var str = '比上次<i class="green"><i class="fa fa-sort-desc"></i>' + compareProcessTime["number"].toFixed(2) + '%</i>';
			$("#compare-process-time").html(str);
		}
		$("#last-average-process-time").html(data["last_average_process_time"].toFixed(3));
		var compareAverageProcessTime = data["compare_average_process_time"];
		if (compareAverageProcessTime["status"] == "up") {
			var str = '比上次<i class="red"><i class="fa fa-sort-asc"></i>' + compareAverageProcessTime["number"].toFixed(2) + '%</i>';
			$("#compare-average-process-time").html(str);
		} else {
			var str = '比上次<i class="green"><i class="fa fa-sort-desc"></i>' + compareAverageProcessTime["number"].toFixed(2) + '%</i>';
			$("#compare-average-process-time").html(str);
		}
		$("#last-process-number").html(data["last_process_number"]);
		var compareProcessNumber = data["compare_process_number"];
		if (compareProcessNumber["status"] == "up") {
			var str = '比上次<i class="green"><i class="fa fa-sort-asc"></i>' + compareProcessNumber["number"].toFixed(2) + '%</i>';
			$("#compare-process-number").html(str);
		} else {
			var str = '比上次<i class="red"><i class="fa fa-sort-desc"></i>' + compareProcessNumber["number"].toFixed(2) + '%</i>';
			$("#compare-process-number").html(str);
		}
	} else {
		alert("获取数据失败！");
	}
}

function showLast7Performance() {
	$.ajax({
		url: "/record/get_last_7_process_average_time_and_process_number",
		type: "GET",
		dataType: "json",
		cache: false,//设置不缓存
		success: showLast7PerformanceSuccess,
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			if (XMLHttpRequest.status >= 400 && XMLHttpRequest.status < 500) {
				alert("客户端请求出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
			} else {
				if (XMLHttpRequest.status >= 500 || XMLHttpRequest.status <= 600) {
					alert("服务器出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
				} else {
					if (XMLHttpRequest.status >= 300 || XMLHttpRequest.status < 400) {
						alert("重定向问题！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
					} else {
						alert("抱歉，系统好像出现一些异常！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
					}
				}
			}
		}
	});
}

function showLast7PerformanceSuccess(data) {
	if (data["status"] == "success") {
		drawPerformanceChart("performance_echart", data["average_time"], data["process_number"], data["data"]);
	} else {
		alert("获取数据失败！");
	}
}

function drawPerformanceChart(showAreaId, averageTimeList, processNumberList, dataList) {
	var myChart = echarts.init(document.getElementById(showAreaId));
	var colors = ['#5793f3', '#d14a61'];
	var option = {
		color: colors,
		tooltip: {
			trigger: 'axis',
			axisPointer: {
				type: 'cross'
			}
		},
		grid: {
			right: '20%'
		},
		toolbox: {
			feature: {
				dataView: {show: true, readOnly: false},
				restore: {show: true},
				saveAsImage: {show: true}
			}
		},
		legend: {
			data:['平均处理时间','处理进程数']
		},
		xAxis: [
			{
				type: 'category',
				axisTick: {
					alignWithLabel: true
				},
				data: dataList
			}
		],
		yAxis: [
			{
				type: 'value',
				name: '平均处理时间',
				position: 'right',
				axisLine: {
					lineStyle: {
						color: colors[0]
					}
				},
				axisLabel: {
					formatter: '{value} s'
				}
			},
			{
				type: 'value',
				name: '处理进程数',
				position: 'left',
				axisLine: {
					lineStyle: {
						color: colors[1]
					}
				},
				axisLabel: {
					formatter: '{value} '
				}
			}
		],
		series: [
			{
				name:'平均处理时间',
				type:'line',
				data:averageTimeList
			},
			{
				name:'处理进程数',
				type:'line',
				yAxisIndex: 1,
				data:processNumberList
			}
		]
	};
	// 使用刚指定的配置项和数据显示图表。
	myChart.setOption(option);
}