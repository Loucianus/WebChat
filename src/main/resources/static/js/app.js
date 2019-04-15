/**
 * Const
 *
 * @type {string}
 */
const METHOD_GET = "GET";
const METHOD_POST = "POST";
const METHOD_DELETE = "DELETE";
const METHOD_PUT = "PUT";

const HEADERS_JSON = new Headers({
    "Content-Type":"application/json;charset=UTF-8"
});


// 选中
// document.querySelector('.chat[data-chat=bulletin]').classList.add('active-chat');
// document.querySelector('.person[data-chat=bulletin]').classList.add('active');

let friends = {
        list: document.querySelector('ul.people'),
        all: document.querySelectorAll('.left .person'),
        name: '' },

    chat = {
        container: document.querySelector('.container .right'),
        current: null,
        person: null,
        name: document.querySelector('.container .right .top .name') };

friends.all.forEach(function (f) {
    f.addEventListener('mousedown', function () {
        f.classList.contains('active') || setActiveChat(f);
    });
});

function setActiveChat(f) {
    friends.list.querySelector('.active').classList.remove('active');
    f.classList.add('active');
    chat.current = chat.container.querySelector('.active-chat');
    chat.person = f.getAttribute('data-chat');
    chat.current.classList.remove('active-chat');
    chat.container.querySelector('[data-chat="' + chat.person + '"]').classList.add('active-chat');
    friends.name = f.querySelector('.name').innerText;
    chat.name.innerHTML = friends.name;
}

/**
 * File-Show
 */
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
            setFileList(result.data.list);
            setFileNav(result.data);
        })
        .catch(error => console.log(error));

}

function setFileList(data) {
    let html = '';
    for (let i=0, len = data.length;i < len; i++) {
        /** @namespace data.filepath */
        html += "<button type='button' class='list-group-item list-group-item-action' data-toggle='modal' data-target='#fileModalLong' onclick='downloadFile(" + JSON.stringify(data[i]) + ")'>"
            + data[i].filename
            + "<span style='float: right;'>" + new Date(data[i].uploadDate).Format("yyyy-MM-dd") + "</span>"
            + "</button>"
    }
    $("#file-list").html( html );
}

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

function downloadFile(fid) {
    window.location.href = "file?fullPath=" + fid.filepath
}

function uploadFile() {

    let file = $('#upload-file-id').prop("files");
    let data = new FormData();
    data.append('file',file[0]);

    $.ajax({
        type: 'POST',
        url: "file/" + $("#worker-uid").html(),
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
 * Bulletin-Show
 */
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

function publishBulletinDetails() {
    const url_bulletin = 'bulletin';
    let id = '0';
    let title = $("#edict-bulletin-title").val();
    let date = new Date().Format("yyyy-MM-dd");
    let details = $("#edict-bulletin-details").val();
    let publishWorker = $("#worker-name").text().trim();
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
 * 时间格式化
 * @param date
 * @returns {string}
 */
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

//时间格式化问题
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

/**
 * 注销
 */
// $("#logout").click(function () {
//
//     console.log("logout");
//
//     const url = "token";
//
//     const general = {
//         method: METHOD_DELETE
//     };
//
//     fetch(url, general)
//         .then(response => { return response.json() })
//         .then(function (result) {
//             if (result.meta.status === 200) {
//                 window.location.href = '/'
//             }
//         })
//         .catch(error => console.log(error))
//
// });