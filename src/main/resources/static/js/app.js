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

    const url = '/file/all';

    const headers = new Headers({
        "Content-Type":"application/json;charset=UTF-8"
    });

    const method = "GET";

    const general = {
        headers: headers,
        method: method
    };

    fetch(url, general)
        .then(response => { return response.json() })
        .then(function (result) {
            console.log(result.data);
            setFileList(result.data);
            setFileNav(result.data);
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
            "<td class='text-center'>" + list[i].name +"</td>" +
            /** @namespace data.downloadTimes */
            "<td class='text-center'>" + list[i].downloadTimes +"</td>";
        if (role === 'worker') {
            /** @namespace data.uploaderId */
            if (list[i].email === $("#worker-email-hide").html()) {
                /** @namespace list.fileId */
                html += "<td><row><button type='button' class='btn btn-outline-primary btn-sm col-sm-6' onclick='downloadFile(" + JSON.stringify(list[i]) + ")'>Download</button><button type='button' class='btn btn-outline-danger btn-sm col-sm-6' onclick='deleteFile(" + list[i].fileId + ")'>Delete</button></row></td>"
            } else {
                html += "<td><row><div class='com-sm-6'></div><button type='button' class='btn btn-outline-primary btn-sm col-sm-6' onclick='downloadFile(" + JSON.stringify(list[i]) + ")'>Download</button></td>"
            }
        } else if (role === 'manager') {
            html += "<td><row><button type='button' class='btn btn-outline-primary btn-sm col-sm-6' onclick='downloadFile(" + JSON.stringify(list[i]) + ")'>Download</button><button type='button' class='btn btn-outline-danger btn-sm col-sm-6' onclick='deleteFile(" + list[i].fileId + ")'>Delete</button></row></td>"
        }
        html += "</row></tr>";
        $("#file-list").html( html );
    }


}
// 文件的翻页
function setFileNav(data) {

    console.log("nav pre:" + data.prePage);
    console.log("nav next:" + data.nextPage);

    let html = "";
    /** @namespace data.prePage */
    if (data.prePage > 0) {
        html += "<li id='prePage' class='page-item'><a class='page-link' onclick='jump("+ data.prePage + ")'>Previous</a></li>";
    }

    /** @namespace data.pageNum */
    html += "<li class='page-item'><a class='page-link'>" + data.pageNum  + "</a></li>";

    /** @namespace data.nextPage */
    /** @namespace data.navigatePages */
    if (data.nextPage !== 0) {
        html += "<li id='nextPage' class='page-item'><a class='page-link' onclick='jump(" + data.nextPage + ")'>Next</a></li>";
    }

    $("#file-nav").html( html );
}
// 文件页面的跳转
function jump(data) {

    if (data.prePage === 0 || data.nextPage === data.navigatePages + 1) {
        return '';
    } else {
        console.log("Get Files...");

        const url = '/file/all?pageNo=' + data;

        const headers = new Headers({
            "Content-Type":"application/json;charset=UTF-8"
        });

        const method = "GET";

        const general = {
            headers: headers,
            method: method
        };

        fetch(url, general)
            .then(response => { return response.json() })
            .then(function (result) {
                console.log(result.data);
                setFileList(result.data.list);
                setFileNav(result.data)
            })
            .catch(error => console.log(error));

        console.log("Web loaded!!");
    }
}
// 文件的下载
function downloadFile(file) {
    /** @namespace file.fullPath */
    window.location.href = "file?fullPath=" + file.fullPath
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

    $.ajax({
        type: 'POST',
        url: "file?uploader_id=" + $("#worker-uid").html(),
        data: data,
        cache: false,
        processData: false,
        contentType: false,
        success: function (ret) {
            alert(ret);
        }
    });

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
    console.log("url" + url);
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
                email.html("Email&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;" + data.email);
                email.attr("href", "mailto:" + data.email);
                if (data.gender === "f")
                    $("#chat-gender").html("Gender&nbsp;&nbsp;:&nbsp;&nbsp;Female");
                else if (data.gender === "m")
                    $("#chat-gender").html("Gender&nbsp;&nbsp;:&nbsp;&nbsp;Male");
                else $("#chat-gender").html("Gender : Unknow");
                $("#chat-role").html("Role&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;" + data.role);
                $("#chat-name").html("Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;" + data.name);
                if (data.status === "i") {
                    $("#chat-status").html("Status&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;Incumbency")
                } else if (data.status === "q") {
                    $("#chat-status").html("Status&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Quit")
                } else if (data.status === "v") {
                    $("#chat-status").html("Status&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;Vacation")
                } else {
                    $("#chat-status").html("Status&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;Unknow")
                }
            }
        })
        .catch(error => console.log(error));
}
// 添加用户的联级selector的实现
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
    let role = add_role_select.val();
    let permission = $("#add-worker-permission-select").val();

    const url = "worker";
    let body = {};
    if (role === "manager") {
       body = {
            "name" : name,
            "email" : email,
            "role" : role,
            "permission" : permission
        };
    } else if (role === "worker") {
        body = {
            "name" : name,
            "email" : email,
            "role" : role
        };
    }

    console.log("add worker body::" + JSON.stringify(body));

    const general = {
        headers: HEADERS_JSON,
        body: body,
        method: METHOD_POST
    };

    fetch(url, general)
        .then(result => {return result.json()})
        .then(function (result) {
            if (result.meta.status === 200) {
                alert(result.data)
            } else {
                alert(result.data)
            }
        })
        .catch(err => console.log(err))
}

/**
 * 时间格式化工具
 */
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