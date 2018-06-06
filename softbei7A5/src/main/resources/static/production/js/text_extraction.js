/**
 * Created by 郭欣光 on 2018/5/27.
 */

function browseFolder(path) {
    try {
        var Message = "\u8bf7\u9009\u62e9\u6587\u4ef6\u5939"; //选择框提示信息
        var Shell = new ActiveXObject("Shell.Application");
        var Folder = Shell.BrowseForFolder(0, Message, 64, 17); //起始目录为：我的电脑
        //var Folder = Shell.BrowseForFolder(0, Message, 0); //起始目录为：桌面
        if (Folder != null) {
            Folder = Folder.items(); // 返回 FolderItems 对象
            Folder = Folder.item(); // 返回 Folderitem 对象
            Folder = Folder.Path; // 返回路径
            if (Folder.charAt(Folder.length - 1) != "\\") {
                Folder = Folder + "\\";
            }
            document.getElementById(path).value = Folder;
            return Folder;
        }
    }
    catch (e) {
        alert(e.message);
    }
}

// var saveSrc;

function startDistinguish() {
    var imgSrc = $('#img-src').val();
    var saveSrc = $('#save-src').val();
    var processNumber = $('#process-number').val();
    if (imgSrc == null || imgSrc == "") {
        alert("请选择要识别图片所在路径！");
    } else {
        if (saveSrc == null || saveSrc == "") {
            alert("请选择要保存的excel路径！");
        } else {
            if (processNumber == null || processNumber == "") {
                alert("请选择进程数！");
            } else {
                var obj = new Object();
                obj["img_src"] = imgSrc;
                obj["save_src"] = saveSrc;
                obj["process_number"] = processNumber;
                $.ajax({
                    url: "/record/upload_process_img_information",
                    type: "POST",
                    dataType: "json",
                    data: obj,
                    cache: false,//设置不缓存
                    success: startDistinguishSuccess,
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
        }
    }
}

var requestProgressInterval;

function startDistinguishSuccess(data) {
    if (data["status"] == "success") {
        $("#img-src-title").html("正在识别");
        showSpeedOfProgress(0);
        requestProgressInterval = setInterval("requestProgress()", 500);
        // for (var i = 0; i <= 100; i += 10) {
        //     setTimeout("showSpeedOfProgress(" + i + ")", 500 * i);
        // }
        // setTimeout(function(){ distinguishFinish(saveSrc);}, 500 * 100 + 200);
    } else {
        alert(data["message"]);
    }
}

function requestProgress() {
    $.ajax({
        url: "/record/get_progress",
        type: "GET",
        dataType: "json",
        cache: false,//设置不缓存
        success: requestProgressSuccess,
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

function requestProgressSuccess(data) {
    if (data["status"] == "success") {
        if (data["img_all"] == -1) {
            showSpeedOfProgress(0);
        } else {
            var imgAll = parseInt(data["img_all"]);
            var imgAlready = parseInt(data["img_already"]);
            var imgError = parseInt(data["img_error"]);
            var speedOfProgress = (((imgAlready + imgError) / imgAll) * 100).toFixed(2);
            showSpeedOfProgress(speedOfProgress);
            if (imgAll == (imgAlready + imgError)) {
                clearInterval(requestProgressInterval);
                getProcessResult();
            }
        }
    } else {
        alert(data["message"]);
    }
}

function getProcessResult() {
    $.ajax({
        url: "/record/get_process_result",
        type: "GET",
        dataType: "json",
        cache: false,//设置不缓存
        success: getProcessResultSuccess,
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

function getProcessResultSuccess(data) {
    if (data["status"] == "success") {
        var record = data["record"];
        distinguishFinish(record);
    } else {
        alert(data['message']);
    }
}

function showSpeedOfProgress(speedOfProgress) {
    var content = "<br/>";
    content += "<p>识别中，请稍后...</p>";
    content += "<div class=\"progress\">";
    content += " <div class=\"progress-bar progress-bar-success progress-bar-striped active\" role=\"progressbar\" aria-valuenow=\"" + speedOfProgress + "\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: " + speedOfProgress + "%;\">";
    content += speedOfProgress + "%";
    content += "</div>";
    content += "</div>";
    $("#img-src-content").html(content);
}

function distinguishFinish(record) {
    alert("识别完成，生成的excel存放在" + record['save_src']);
    $("#img-src-title").html("图片来源");
    var content = "<br />";
    content = "<form id=\"demo-form2\" data-parsley-validate class=\"form-horizontal form-label-left\" onsubmit=\"startDistinguish();return false;\">";
    content += "<div class=\"form-group\">";
    content += " <label class=\"control-label col-md-3 col-sm-3 col-xs-12\" for=\"img-src\">图片所在路径 <span class=\"required\">*</span>";
    content += "</label>";
    content += "<div class=\"col-md-6 col-sm-6 col-xs-12\">";
    content += "<div class=\"input-group\">";
    content += "<span class=\"input-group-btn\">";
    content += "<button type=\"button\" class=\"btn btn-success\" onclick=\"browseFolder('img-src')\">选择</button>";
    content += "</span>";
    content += "<input type=\"text\" class=\"form-control\" id=\"img-src\" disabled=\"disabled\">";
    content += "</div>";
    content += "</div>";
    content += "</div>";
    content += "<div class=\"form-group\">";
    content += "<label class=\"control-label col-md-3 col-sm-3 col-xs-12\" for=\"save-src\">excel保存路径<span class=\"required\">*</span>";
    content += " </label>";
    content += "<div class=\"col-md-6 col-sm-6 col-xs-12\">";
    content += "<div class=\"input-group\">";
    content += "<span class=\"input-group-btn\">";
    content += "<button type=\"button\" class=\"btn btn-success\" onclick=\"browseFolder('save-src')\">选择</button>";
    content += "</span>";
    content += "<input type=\"text\" class=\"form-control\" id=\"save-src\" disabled=\"disabled\">";
    content += " </div>";
    content += " </div>";
    content += " </div>";
    content += "<div class=\"form-group\">";
    content += "<label for=\"process-number\" class=\"control-label col-md-3 col-sm-3 col-xs-12\">使用进程数</label><span class=\"required\">*</span>";
    content += "<div class=\"col-md-6 col-sm-6 col-xs-12\">";
    content += "<select class=\"form-control  col-md-7 col-xs-12\" id=\"process-number\" name=\"process-number\">";
    content += "<option>1</option>";
    content += "<option>2</option>";
    content += "<option>3</option>";
    content += "<option>4</option>";
    content += "<option>5</option>";
    content += "</select>";
    content += "</div>";
    content += "</div>";
    content += " <div class=\"ln_solid\"></div>";
    content += "<div class=\"form-group\">";
    content += "<div class=\"col-md-6 col-sm-6 col-xs-12 col-md-offset-3\">";
    content += "<button class=\"btn btn-primary\" type=\"reset\">重置</button>";
    content += "<button type=\"submit\" class=\"btn btn-success\">开始识别</button>";
    content += "</div>";
    content += "</div>";
    content += "</form>";
    $("#img-src-content").html(content);

    var successContent = "";
    successContent += "<div class=\"col-md-12 col-sm-12 col-xs-12\">";
    successContent += "<div class=\"x_panel\">";
    successContent += "<div class=\"x_title\">";
    successContent += "<h2>性能分析</h2>";
    successContent += ' <ul class="nav navbar-right panel_toolbox">';
    successContent += '<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>';
    successContent += '</li>';
    successContent += ' <li class="dropdown">';
    successContent += '<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>';
    successContent += '<ul class="dropdown-menu" role="menu">';
    successContent += '<li><a href="#">Settings 1</a>';
    successContent += '</li>';
    successContent += '<li><a href="#">Settings 2</a>';
    successContent += ' </li>';
    successContent += '</ul>';
    successContent += '</li>';
    successContent += ' <li><a class="close-link"><i class="fa fa-close"></i></a>';
    successContent += '</li>';
    successContent += '</ul>';
    successContent += '<div class="clearfix"></div>';
    successContent += '</div>';
    successContent += '<div class="x_content">';
    successContent += '<div class="row tile_count">';
    successContent += '<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">';
    successContent += '<span class="count_top"><i class="fa fa-file-image-o"></i> 识别出的图片张数</span>';
    successContent += '<div class="count">' + record['img_already'] + '</div>';
    successContent += '</div>';
    successContent += '<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">';
    successContent += '<span class="count_top"><i class="fa fa-file-image-o"></i> 未能识别的图片张数</span>';
    successContent += '<div class="count">' + record['img_error'] + '</div>';
    successContent += '</div>';
    successContent += '<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">';
    successContent += '<span class="count_top"><i class="fa fa-check-square-o"></i> 识别成功率</span>';
    var imgAlready = parseInt(record['img_already']);
    var imgAll = parseInt(record['img_all']);
    successContent += '<div class="count"> '+ ((imgAlready / imgAll) * 100).toFixed(2) + '%</div>';
    successContent += '</div>';
    successContent += '<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">';
    successContent += '<span class="count_top"><i class="fa fa-clock-o"></i> 识别时间（s）</span>';
    successContent += '<div class="count">' + (record['process_time'] / 1000).toFixed(3) + '</div>';
    successContent += '</div>';
    successContent += '<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">';
    successContent += '<span class="count_top"><i class="fa fa-clock-o"></i> 平均识别时间（s）</span>';
    successContent += '<div class="count">' + ((record['process_time'] / 1000) / imgAll).toFixed(3) + '</div>';
    successContent += '</div>';
    successContent += '<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count">';
    successContent += '<span class="count_top"><i class="fa fa-lightbulb-o"></i> 使用进程数</span>';
    successContent += '<div class="count">' + record['process_number'] + '</div>';
    successContent += '</div>';
    successContent += '</div>';
    successContent += '</div>';
    successContent += '</div>';
    successContent += '</div>';
    $('#success-content').html(successContent);
}
