const uid = $("#worker-uid");
let lastId = 0;
let nowId = 0;

let stompClient = null;
let sessionId = null;

/**
 * WebSocket
 */
function connect() {

    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        sessionId = /\/([^\/]+)\/websocket/.exec(socket._transport.url)[1];
        // 群聊
        stompClient.subscribe('/topic/group', function (message) {
            handleNotification(message)
        });
        // 私信
        stompClient.subscribe('/user/topic/private', function (message) {
            handlePrivate(message);
        });
        // 在线用户列表
        stompClient.subscribe('/topic/contacts', function (message) {
        });
    });
}
// 私聊
function handlePrivate(message) {
    let to_id = $("#chat-to-id-hide");
    let msg = JSON.parse(message.body);

    console.log(msg);
    if (parseInt(to_id.html()) === msg.from_id) {
        if (msg.type === "s") {
            $("#output" + msg.from_id +"").append(
                "<div class='bubble you'>" + msg.content + "</div>"
            );
        } else if (msg.type === "f") {
            $("#output" + msg.from_id +"").append(
                "<a href='#' class='bubble you' onclick='downloadFile("+ msg.content +")'>文件:[" + msg.filename + "]</a>"
            );
        }

        let url = "message?uid=" + parseInt($("#worker-uid").html()) + "&id=" + parseInt(to_id.html());
        $.get(url)
    } else {
        let badge = $("#badge" + msg.from_id +"");
        console.log(badge);
        if (badge.html() === "") {
            badge.html(1)
        } else {
            let tmp = parseInt(badge.html());
            badge.html(tmp + 1)
        }
        if (msg.type === "s") {
            $("#output" + msg.from_id +"").append(
                "<div class='bubble you'>" + msg.content + "</div>"
            );
        } else if (msg.type === "f") {
            $("#output" + msg.from_id +"").append(
                "<a href='#' class='bubble you' onclick='downloadFile("+ msg.content +")'>文件:[" + msg.filename + "]</a>"
            );
        }
    }
    scroll(parseInt(to_id.html()));
}

// 群聊
//TODO 改
function handleNotification(message) {
    let msg = JSON.parse(message.body);
    let to_id = $("#chat-to-id-hide");
    console.log("群聊:" + msg);
    /** @namespace msg.data.fromID */

    if (parseInt(to_id.html()) === 0) {
        if (parseInt(msg.data.fromId) !== parseInt(uid.html())) {

            $("#output0").append(
                "<div class='bubble you'>" + "[" + msg.data.name + "]:    " + msg.data.content + "</div>"
            );
        }
    }
    else {
        let badge = $("#badge0");
        console.log(badge);
        if (badge.html() === "") {
            badge.html(1)
        } else {
            let tmp = parseInt(badge.html());
            badge.html(tmp + 1)
        }
        $("#output0").append(
            "<div class='bubble you'>" +"[" + msg.data.name + "]:    " +msg.data.content + "</div>"
        );
    }
    scroll(parseInt(to_id.html()));
}

// 消息展示
function showChatContent(message, uid) {

    let msg = JSON.parse(message);
    // console.log(msg);

    if (msg.fromId === uid) {
        nowId = msg.toId
    } else {
        nowId = msg.fromId
    }

    if (typeof (msg.toId) === "undefined") {
        nowId = 0
    }

    let badge = $("#badge" + parseInt(msg.fromId) +"");
    if (badge.html() === "") {
        badge.html(1)
    } else {
        let tmp = parseInt(badge.html());
        badge.html(tmp + 1)
    }

    /** @namespace msg.fromId */
    if (msg.fromId === uid){

        if (msg.type === "s") {
            $("#output" + nowId +"").append(
                "<div class='bubble me'>" + msg.content + "</div>"
            );
        } else if (msg.type === "f") {
            $("#output" + nowId +"").append(
                "<a href='#' class='bubble me' onclick='downloadFile("+ msg.content +")'>文件:[" + msg.filename + "]</a>"
            );
        }
    } else {
        /** @namespace msg.toId */
        if (typeof (msg.toId) === 'undefined') {

            $("#output" + nowId +"").append("<div class='bubble you'>" +"[" + msg.name + "]:    " +msg.content + "</div>")

        } else {
            if (msg.type === "s") {
                $("#output" + nowId +"").append(
                    "<div class='bubble you'>" + msg.content + "</div>"
                );
            } else if (msg.type === "f") {
                $("#output" + nowId +"").append(
                    "<a href='#' class='bubble you' onclick='downloadFile("+ msg.content +")'>文件:[" + msg.filename + "]</a>"
                );
            }
        }
    }
}

// 加载页面时获取联系人信息
$().ready(function () {

    console.log("Get Contacts...");

    const url = 'worker/contacts/';

    const general = {
        headers: HEADERS_JSON,
        method: METHOD_GET
    };

    fetch(url, general)
        .then(result => { return result.json() })
        .then(function (result) {
            setContactList(result.data);
        })
        .catch(error => console.log(error));

    connect();

});

// 显示联系人列表
function setContactList(data) {
    let html = '';
    for (let i=0, len = data.length;i < len; i++) {
        /** @namespace data.portrait */
        let body = {
            "id":data[i].id,
            "name": data[i].name,
            "email": data[i].email
        };
        html += "<div class='person' data-chat='person" + data[i].id +"' onclick='chatTO(" + JSON.stringify(body) + ")'>" +
            "<img src='" + data[i].portrait + "' alt='' />" +
            "<span class='name'>" + data[i].name + "</span>" +
            "<span id='badge" + data[i].id + "' class='time badge pull-right'></span>" +
            "<span class='preview'>" + data[i].role + "</span>" +
            "</div>";
        $("#message-body").append("<div id='output" + data[i].id +"' class='scroll scroll-chat'></div>");
        $("#output" + data[i].id +"").hide();
        // console.log(data[i].id);
        // here
        getChatFile(parseInt(data[i].id), parseInt(uid.text().trim()));
    }
    $("#contact-list").append( html );
}

// 选择聊天对象
function chatTO(data) {

    // 设置聊天对象信息
    let chat_name =$("#chat-name-top");
    chat_name.html(data.name);
    let id = $("#chat-to-id-hide");

    let badge = $("#badge" + parseInt(data.id) +"");
    console.log(badge.html());
    if (badge.html() !== "") {
        let url = "message?uid=" + parseInt($("#worker-uid").html()) + "&id=" +parseInt(data.id);
        $.get(url);
    }

    if (parseInt(data.id) === 0) {
        $("#send-file-icon").hide();
        chat_name.removeAttr("onclick");
        $("#info-dropdown-menu").attr("style", "display:none")
    } else {
        $("#send-file-icon").show();
        chat_name.attr("onclick", "getWorkerInfo()");
        $("#info-dropdown-menu").removeAttr("style");
    }

    $("#chat-email-hide").html(data.email);
    id.html(data.id);
    $("#output"+lastId+"").hide();
    $("#output"+data.id+"").show();
    lastId = parseInt(data.id);
    badge.html("");
    scroll(parseInt(id.html()))
}

// 获取离线消息
function getChatFile(id, uid) {
    const url = 'message/recent?id=' + id + "&uid=" + uid;

    const general = {
        headers: HEADERS_JSON,
        method: METHOD_GET
    };

    fetch(url, general)
        .then(response => { return response.json() })
        .then(function (result) {
            // console.log("file get::" + result.meta.status);
            if (result.meta.status === 200) {
                console.log("outline::" + JSON.stringify(result.data));
                for (let i = result.data.length-1; i >= 0; i--) {
                    showChatContent(JSON.stringify(result.data[i]), uid);
                }
            } else if (result.meta.status === 400) {
                console.log("Had not chatted with him.")
            }
        })
        .catch(error => console.log(error));
}

// 发送文本信息
function sendText () {

    let _data = $('#input-text');

    if ($.trim(_data.val()) === "") {
        alert("Please input message and send.");
        console.log("Please not input empty message!!");
        return
    }

    let _to_email = $("#chat-email-hide").html();
    if (_to_email === "" || _to_email === null) {
        alert("The chat partner not selected.");
        console.log("Please choose the user to chat.");
        return;
    }

    let to_id = $("#chat-to-id-hide");
    let message = {
        "to_id" : parseInt(to_id.html()),
        "from_id": parseInt(uid.text().trim()),
        "to_email": _to_email,
        "data" : _data.val(),
        "type": 's',
        "name": $("#worker-name-hide").html(),
        "filename": "#"
    };

    $("#output" + parseInt(to_id.html()) + "").append("<div class='bubble me'>" + _data.val() + "</div>");
    scroll(parseInt(to_id.html()));
    if (parseInt(to_id.html()) !== 0) {
        stompClient.send("/app/private", {}, JSON.stringify(message));
    } else {
        stompClient.send("/app/group", {}, JSON.stringify(message));
    }
    _data.val("");
}

// 发送文件信息
$("#send-file-btn").click(function () {
    let _to_email = $("#chat-email-hide").html();
    if (_to_email === "" || _to_email === null) {
        alert("The chat partner not selected.");
        console.log("Please choose the user to chat.");
        return;
    }
    let to_id = $("#chat-to-id-hide");
    let file = $('#upload-file-id-send').prop("files");
    let data = new FormData();
    data.append('file',file[0]);

    $.ajax({
        type: METHOD_POST,
        url: "file?uploader_id=" + $("#worker-uid").html() + "&to_id=" + parseInt(to_id.html()),
        data: data,
        cache: false,
        processData: false,
        contentType: false,
        success: function (ret) {
            console.log(ret);


            let message = {
                "to_id" : parseInt(to_id.html()),
                "from_id": parseInt(uid.text().trim()),
                "to_email": _to_email,
                "data" : ret.data.id,
                "type": 'f',
                "name": $("#worker-name-hide").html(),
                "filename" : ret.data.filename
            };

            $("#output" + parseInt(to_id.html()) + "").append("<a href='#' class='bubble me' onclick='downloadFile("+ ret.data.id +")'>文件:[" + ret.data.filename + "]</a>");
            scroll(parseInt(to_id.html()));
            if (parseInt(to_id.html()) !== 0) {
                stompClient.send("/app/private", {}, JSON.stringify(message));
            } else {
                stompClient.send("/app/group", {}, JSON.stringify(message));
            }

        }
    });


});

function keySend(event) {
    if (event.ctrlKey && event.keyCode === 13) {
        sendText()
    }
}

// 聊天内容的滚动条
function scroll(id) {
    let div = $("#output"+ id +"");
    const top = 500000;
    div.scrollTop(top);
}

// 获取历史记录
function getHistory() {
    $("#history").html("");
    let to_id = parseInt($("#chat-to-id-hide").html());
    let id = parseInt(uid.text().trim());

    console.log("to id" + to_id);
    console.log("uid" + id);

    let search = $("#search-msg").val();

    let url = "";
    if (search === "") {
        url = "message/history?id=" + to_id + "&uid=" + id;
    } else if (typeof (search) === "undefined") {
        url = "message/history?id=" + to_id + "&uid=" + id;
    } else {
        url = "message/history?id=" + to_id + "&uid=" + id + "&msg=" + search;
    }

    const general = {
        headers: HEADERS_JSON,
        method: METHOD_GET
    };

    fetch(url, general)
        .then(response => { return response.json() })
        .then(function (result) {
            if (result.meta.status === 200) {
                let messageList = result.data.list;
                console.log(JSON.stringify(result.data));
                for (let i = 0; i < messageList.length; i++) {
                    showHistoryContent(JSON.stringify(messageList[i]), id);
                }
                setMessageNav(result.data, "")
            } else if (result.meta.status === 400) {
                console.log("Had not chatted with him.");
                alert("你还没和TA有过通讯.")
            }
        })
        .catch(error => console.log(error));
}

// 展示历史记录
function showHistoryContent(message, uid) {

    let msg = JSON.parse(message);
    /** @namespace msg.fromId */
    if (msg.fromId === uid){
        if (msg.type === "s") {
            $("#history").append(
                "<div class='bubble me'>" + msg.content + "</div>"
            );
        } else if (msg.type === "f") {
            $("#history").append(
                "<a href='#' class='bubble me' onclick='downloadFile("+ msg.content +")'>文件:[" + msg.filename + "]</a>"
            );
        }
    } else {
        /** @namespace msg.toId */
        if (typeof (msg.toId) === 'undefined') {

            $('#history').append("<div class='bubble you'>" +"[" + msg.name + "]:    " +msg.content + "</div>")

        } else {
            if (msg.type === "s") {
                $("#history").append(
                    "<div class='bubble you'>" + msg.content + "</div>"
                );
            } else if (msg.type === "f") {
                $("#history").append(
                    "<a href='#' class='bubble you' onclick='downloadFile("+ msg.content +")'>文件:[" + msg.filename + "]</a>"
                );
            }
        }
    }
}

// 历史记录的的翻页
function setMessageNav(data,msg) {
    console.log("历史消息翻页::data" + JSON.stringify(data));

    let html = "";
    /** @namespace data.prePage */
    if (data.prePage > 0) {
        html += "<li id='prePage' class='page-item'><a class='page-link' onclick='jumpMsg("+ data.prePage+ "," + msg + ")'>Previous</a></li>";
    }

    /** @namespace data.pageNum */
    html += "<li class='page-item'><a class='page-link'>" + parseInt(data.pageNum)  + "</a></li>";

    /** @namespace data.nextPage */
    /** @namespace data.navigatePages */
    if (parseInt(data.nextPage) !== 0) {
        html += "<li id='nextPage' class='page-item'><a class='page-link' onclick='jumpMsg(" + data.nextPage +"," + msg + ")'>Next</a></li>";
    }

    $("#msg-nav").html( html );
}
// 文件页面的跳转
function jumpMsg(data, msg,) {

    let to_id = parseInt($("#chat-to-id-hide").html());
    let id = parseInt(uid.text().trim());


    let url = "";
    if (msg === "") {
        url = "message/history?id=" + to_id + "&uid=" + id+ "&pageNo=" + data;
    } else if (typeof (msg) === "undefined") {
        url = "message/history?id=" + to_id + "&uid=" + id+ "&pageNo=" + data;
    } else {
        url = "message/history?id=" + to_id + "&uid=" + id + "&msg=" + msg+ "&pageNo=" + data;
    }




    const general = {
        headers: HEADERS_JSON,
        method: METHOD_GET
    };
    $("#history").html("");
    fetch(url, general)
        .then(response => { return response.json() })
        .then(function (result) {
            let messageList = result.data.list;
            for (let i = 0; i < messageList.length; i++) {
                showHistoryContent(JSON.stringify(messageList[i]), id);
            }
            setMessageNav(result.data, msg)
        })
        .catch(error => console.log(error));

}