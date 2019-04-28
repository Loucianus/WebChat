const role = $("#worker-role-hide").html().toString();
const permission = $("#worker-permission-hide").html().toString();

/**
 * Const
 */
const METHOD_GET = "GET";
const METHOD_POST = "POST";
const METHOD_DELETE = "DELETE";
const METHOD_PUT = "PUT";
const HEADERS_JSON = new Headers({
    "Content-Type":"application/json;charset=UTF-8"
});

/**
 * File
 */
// 获取文件列表资源
function viewFile() {
    console.log("Get Files...");

    const url = '/file/all/' + parseInt($("#worker-uid").html());

    const general = {
        headers: HEADERS_JSON,
        method: METHOD_GET
    };

    fetch(url, general)
        .then(response => { return response.json() })
        .then(function (result) {
            if (result.data.total !== 0) {
                setFileList(result.data);
                setFileNav(result.data, "")
            } else if (result.data.total === 0) {
                $("#file-list").html(  "" );
                alert("没有搜索到相关文件")
            }
        })
        .catch(error => console.log(error));

}
// 显示文件列表
function setFileList(data) {
    if (role === "" || typeof role === "undefined" ) {
        if (role !== "worker" && role !== 'manager')
        alert("Role: " + role + " Has No PERMISSION To Download File.");
        return;
    }
    console.log(data);
    let list = data.list;
    let html = '';
    for (let i=0, len = list.length;i < len; i++) {
        /** @namespace data.pageSize */
        let num = data.pageSize * (data.pageNum - 1) + i + 1;
        html +=
            "<tr><row>" +
            "<th class='text-left' scope='row'>" + num + "</th>" +
            "<td class='text-left'>" + list[i].filename + "</td>" +
            "<td class='text-center'>" + new Date(list[i].uploadDate).Format("yyyy-MM-dd") + "</td>" +
            "<td class='text-center'>" + list[i].name +"</td>" ;
            /** @namespace data.downloadTimes */
            // "<td class='text-center'>" + list[i].downloadTimes +"</td>";
        if (role === 'worker') {
            /** @namespace data.uploaderId */
            if (list[i].email === $("#worker-email-hide").html()) {
                /** @namespace list.fileId */
                html += "<td><row><button type='button' class='btn btn-outline-primary btn-sm col-sm-6' onclick='downloadFile(" + list[i].fileId + ")'>Download</button><button type='button' class='btn btn-outline-danger btn-sm col-sm-6' onclick='deleteFile(" + list[i].fileId + ")'>Delete</button></row></td>"
            } else {
                html += "<td><row><button type='button' class='btn btn-outline-primary btn-sm' onclick='downloadFile(" + list[i].fileId + ")'>Download</button></td>"
            }
        } else if (role === 'manager') {
            html += "<td><row><button type='button' class='btn btn-outline-primary btn-sm col-sm-6' onclick='downloadFile(" + list[i].fileId + ")'>Download</button><button type='button' class='btn btn-outline-danger btn-sm col-sm-6' onclick='deleteFile(" + list[i].fileId + ")'>Delete</button></row></td>"
        }
        html += "</row></tr>";
        $("#file-list").html( html );
    }

}
// 文件的翻页
function setFileNav(data,filename) {

    console.log("翻页::filename::"+filename);

    if (typeof (filename) === "undefined") {
        let html = "";
        /** @namespace data.prePage */
        if (data.prePage > 0) {
            html += "<li id='prePage' class='page-item'><a class='page-link' onclick='jump("+ data.prePage+ "," + '' + ")'>Previous</a></li>";
        }

        /** @namespace data.pageNum */
        html += "<li class='page-item'><a class='page-link'>" + data.pageNum  + "</a></li>";

        /** @namespace data.nextPage */
        /** @namespace data.navigatePages */
        if (parseInt(data.nextPage) !== 0) {
            html += "<li id='nextPage' class='page-item'><a class='page-link' onclick='jump(" + data.nextPage +"," + '' + ")'>Next</a></li>";
        }

        $("#file-nav").html( html );
    } else {
        let html = "";
        /** @namespace data.prePage */
        if (data.prePage > 0) {
            html += "<li id='prePage' class='page-item'><a class='page-link' onclick='jump("+ data.prePage+ "," + filename + ")'>Previous</a></li>";
        }

        /** @namespace data.pageNum */
        html += "<li class='page-item'><a class='page-link'>" + data.pageNum  + "</a></li>";

        /** @namespace data.nextPage */
        /** @namespace data.navigatePages */
        if (parseInt(data.nextPage) !== 0) {
            html += "<li id='nextPage' class='page-item'><a class='page-link' onclick='jump(" + data.nextPage +"," + filename + ")'>Next</a></li>";
        }

        $("#file-nav").html( html );
    }


}
// 文件页面的跳转
function jump(data, filename) {

    console.log("jump::"+data);
    console.log("跳转::filename::"+filename);
    if (typeof (filename) === "undefined") {
        const url = "/file/all/" + parseInt($("#worker-uid").html()) + "?pageNo=" + data;

        const general = {
            headers: HEADERS_JSON,
            method: METHOD_GET
        };

        fetch(url, general)
            .then(response => { return response.json() })
            .then(function (result) {
                setFileList(result.data);
                setFileNav(result.data, filename)
            })
            .catch(error => console.log(error));

    } else {
        const url = "/file/all/" + parseInt($("#worker-uid").html()) + "?pageNo=" + data + "&filename=" + filename;

        const general = {
            headers: HEADERS_JSON,
            method: METHOD_GET
        };

        fetch(url, general)
            .then(response => { return response.json() })
            .then(function (result) {
                setFileList(result.data);
                setFileNav(result.data, filename)
            })
            .catch(error => console.log(error));

    }

}
// 文件的下载
function downloadFile(fid) {
    window.location.href = 'file?file_id=' + parseInt(fid);
}
// 文件的删除
function deleteFile(id) {
    const url = 'file?file_id=' + id;

    const general = {
        headers: HEADERS_JSON,
        method: METHOD_DELETE
    };

    fetch(url, general)
        .then(response => { return response.json() })
        .then(function (result) {

            if (result.meta.status === 204) {
                alert(result.data);
                window.reload()
            } else {
                alert(result.data)
            }
        })
        .catch(error => console.log(error))
}
// 文件的上传
function uploadFile() {

    let file = $('#upload-file-id').prop("files");
    let data = new FormData();
    data.append('file',file[0]);

    console.log(file);
    console.log(data);

    if (file.length === 0) {
        alert("请选择要上传的文件");
        return;
    }

    $.ajax({
        type: METHOD_POST,
        url: "file?uploader_id=" + $("#worker-uid").html() + "&to_id=0",
        data: data,
        cache: false,
        processData: false,
        contentType: false,
        success: function (result) {
            if (result.meta.status === 405) {
                alert(result.data)
            } else if (result.meta.status === 200) {
                alert("上传完成..");
            } else {
                console.log(result.data);
                alert("发生未知错误!!")
            }
        }
    });

}
// 文件搜索
function searchFile() {
    console.log("Get Files...");

    let search = $("#search-file").val();

    const url = '/file/all/' + parseInt($("#worker-uid").html()) + "?filename=" + search;

    const general = {
        headers: HEADERS_JSON,
        method: METHOD_GET
    };

    fetch(url, general)
        .then(response => { return response.json() })
        .then(function (result) {
            console.log("result::" + JSON.stringify(result));
            if (result.data.total !== 0) {
                setFileList(result.data);
                setFileNav(result.data, search)
            } else if (result.data.total === 0) {
                $("#file-list").html(  "" );
                alert("没有搜索到相关文件")
            }
        })
        .catch(error => console.log(error));
}

/**
 * Bulletin
 */
// 获取数据并展示
function viewBulletin() {
    const url_bulletin = 'bulletin';

    const general_bulletin = {
        headers: HEADERS_JSON,
        method: METHOD_GET
    };

    fetch(url_bulletin, general_bulletin)
        .then(response => { return response.json() })
        .then(function (result) {
            let bulletin = result.data;
            console.log(bulletin);
            /** @namespace bulletin.details */
            $("#view-bulletin-details").html( marked(bulletin.details) );
            /** @namespace bulletin.date */
            $("#view-bulletin-date").html( new Date(bulletin.date).Format("yyyy-MM-dd"));
            /** @namespace bulletin.publishWorker */
            $("#view-bulletin-publisher").html(bulletin.publishWorker);
            $("#view-bulletin-title").html(bulletin.title)
        })
        .catch(error => console.log(error))
}
// 更新公告
function publishBulletinDetails() {
    const url_bulletin = 'bulletin';
    let id = '0';
    let title = $("#edict-bulletin-title").val();
    let date = new Date().Format("yyyy-MM-dd");
    let details = $("#edict-bulletin-details").val();
    let publishWorker = $("#worker-name-hide").text().trim();
    let publishWorkerId = $("#worker-uid").text().trim();

    const body = {
        "id": id,
        "title": title,
        "date": date,
        "details": details,
        "publishWorker": publishWorker,
        "publishWorkerId": publishWorkerId
    };

    const general_bulletin = {
        headers: HEADERS_JSON,
        method: METHOD_POST,
        body: JSON.stringify(body)
    };

    fetch(url_bulletin, general_bulletin)
        .then(response => { return response.json() })
        .then(function (result) {
            if (result.meta.status === 200) {
                alert(" success to update Bulletin!!");
                $('#edict-bulletin').modal('hide')
            } else {
                alert(" Not Update Bulletin.Please try again!!")
            }
        })
        .catch(error => console.log(error))
}

/**
 * Worker
 */
// 获取worker的信息
function getWorkerInfo() {
    let id =parseInt($("#chat-to-id-hide").html().toString());
    if (id === 0) {
        return
    }

    const url = "worker/info/" + id;
    const general = {
        headers: HEADERS_JSON,
        method: METHOD_GET
    };

    /** @namespace data.gender */
    fetch(url, general)
        .then(response => { return response.json() })
        .then(function (result) {
            console.log(result);
            if (result.meta.status === 200) {
                let data = result.data;
                let email =$("#chat-email");
                email.html("邮箱&nbsp;&nbsp;:&nbsp;&nbsp;" + data.email);
                email.attr("href", "mailto:" + data.email);
                if (data.gender === "f")
                    $("#chat-gender").html("性别&nbsp;&nbsp;:&nbsp;&nbsp;女性");
                else if (data.gender === "m")
                    $("#chat-gender").html("性别&nbsp;&nbsp;:&nbsp;&nbsp;男性");
                else $("#chat-gender").html("性别&nbsp;&nbsp;:&nbsp;&nbsp;未知");
                $("#chat-role").html("权限&nbsp;&nbsp;:&nbsp;&nbsp;" + data.role);
                $("#chat-name").html("姓名&nbsp;&nbsp;:&nbsp;&nbsp;" + data.name);
                if (data.status === "i") {
                    $("#chat-status").html("状态&nbsp;&nbsp;:&nbsp;&nbsp;在职")
                } else if (data.status === "q") {
                    $("#chat-status").html("状态&nbsp;&nbsp;&nbsp;&nbsp;离职")
                } else if (data.status === "v") {
                    $("#chat-status").html("状态&nbsp;&nbsp;:&nbsp;&nbsp;休假")
                } else {
                    $("#chat-status").html("状态&nbsp;&nbsp;:&nbsp;&nbsp;未知")
                }
            }
        })
        .catch(error => console.log(error));

}
let add_role_select = $("#add-worker-role-select");
add_role_select.change(function () {
    if (add_role_select.val() === "manager") {
        $("#add-worker-permission-form").show();
    } else {
        $("#add-worker-permission-form").hide();
    }
});

// 添加worker
function addWorker() {
    let name = $("#add-worker-name-input").val();
    let email = $("#add-worker-email-input").val();
    let gender = $("#add-worker-gender-select").val();
    let role = add_role_select.val();
    let _permission = $("#add-worker-permission-select").val();

    if (_permission === "view#edict#download#upload#update#delete" && permission === "view#edict#download#upload#update") {
        alert("没有权限去创建超级管理员");
        return;
    }

    const url = "worker";
    let body = {};

    if (role === "manager") {
        body = {
            "name" : name,
            "email" : email,
            "role" : role,
            "permission" : _permission,
            "gender": gender
        };
    } else if (role === "worker") {
        body = {
            "name" : name,
            "email" : email,
            "role" : role,
            "gender": gender,
            "permission": ""
        };
    }

    console.log("add worker body::" + JSON.stringify(body));

    const general = {
        headers: HEADERS_JSON,
        body: JSON.stringify(body),
        method: METHOD_POST
    };

    fetch(url, general)
        .then(result => {return result.json()})
        .then(function (result) {
            if (result.meta.status === 200) {
                if ( result.data) {
                    alert("Succeed to add the worker.")
                } else {
                    alert("Failed to add the worker.")
                }
            } else {
                alert("code: " + result.meta.status + ".Please try again or call the manager." )
            }
        })
        .catch(err => console.log(err))
}

// 修改用户权限
function edictWorkerPermission() {
    let id =parseInt($("#chat-to-id-hide").html().toString());
    if (id === 0) {
        return
    }
    let status = $("#worker-permission-status-select").val();
    let role = $("#worker-permission-role-select").val();
    let _permission = $("#worker-permission-permission-select").val();

    if (_permission === "view#edict#download#upload#update#delete" && permission === "view#edict#download#upload#update") {
        alert("没有权限更改用户为超级管理员");
        return;
    }

    let body = {};
    if (role === "manager") {
        body = {
            "id":id,
            "status": status,
            "role" : role,
            "permission" : _permission,
        };
    } else if (role === "worker") {
        body = {
            "id": id,
            "status": status,
            "role" : role,
            "permission": "",
        };
    }

    const url = "worker/permission";

    const general = {
        headers: HEADERS_JSON,
        method: METHOD_POST,
        body: JSON.stringify(body)
    };

    fetch(url, general)
        .then(response => { return response.json() })
        .then(function (result) {
            if (result.data === true) {
                alert("修改成功!")
            } else {
                alert("没有权限修改此用户.")
            }

        })
        .catch(error => console.log(error));

}
let worker_permission_role_select = $("#worker-permission-role-select");
worker_permission_role_select.change(function () {
    if (worker_permission_role_select.val() === "manager") {
        $("#worker-permission-permission-form").show();
    } else {
        $("#worker-permission-permission-form").hide();
    }
});

//修改用户信息
function edictWorkerInfo() {
    let name = $("#updateName").val();
    let gender = $("#updateGender").val();


    let file = $('#upload-portrait-id').prop("files");
    console.log(file);
    let data = new FormData();
    data.append('file',file[0]);

    $.ajax({
        type: METHOD_PUT,
        url: "worker/info?uid=" + parseInt($("#worker-uid").html()) + "&name=" + name + "&gender=" + gender,
        data: data,
        cache: false,
        processData: false,
        contentType: false,
        success: function (result) {
            console.log(result);
            if (result.meta.status === 200) {
                alert(result.data);
            } else if (result.meta.status === 200) {
                alert(result.data)
            }
        }
    });
}

// 时间处理，与现在日期对比，返回显示的格式
function dateFormat(date) {

    let pre = date.toString();
    let pre_y = pre.substring(0,4);
    let pre_m = pre.substring(5,7);
    let pre_d = pre.substring(8,10);
    let pre_h = pre.substring(11,13);
    let pre_s = pre.substring(14,16);

    let now = new Date().Format('yyyy-MM-dd HH:ss');
    let now_d = now.substring(8,10);

    if (pre_d === now_d) {
        return pre_h + ":" + pre_s
    } else if (now_d - pre_d === 1){
        return "Yesterday"
    } else {
        return pre_y + "-" + pre_m + "-" + pre_d
    }
}

// 时间格式化
Date.prototype.Format = function (fmt) {
    let o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (let k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};