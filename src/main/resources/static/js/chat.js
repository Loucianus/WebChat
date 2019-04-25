const uid = $("#worker-uid");

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
 * WebSocket
 */
let stompClient = null;
let sessionId = null;
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

/**
 * Onload
 */
/**
 * 加载页面信息，获取联系人列表
 */
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
            setContactList(result.data)
        })
        .catch(error => console.log(error));

    connect();

});

/**
 * 显示人员列表
 * @param data
 */
function setContactList(data) {
    let html = '';
    for (let i=0, len = data.length;i < len; i++) {
        /** @namespace data.portrait */
        html += "<div id='person" + data[i].id +"' class='person' data-chat='person" + data[i].id +"' onclick='chatTO(" + JSON.stringify(data[i]) + ")'>" +
            "<img src='" + data[i].portrait + "' alt='' />" +
            "<span class='name'>" + data[i].name + "</span>" +
            "<span id='badge" + data[i].id + "' class='time badge pull-right'></span>" +
            "<span class='preview'>" + data[i].role + "</span>" +
            "</div>";
    }
    $("#contact-list").append( html );
}

/**
 * 选择聊天对象
 * @param data
 */
/** @namespace data.email */
function chatTO(data) {
    // 清除聊天信息
    $('#output').html("");
    // 设置聊天对象信息
    let chat_name =$("#chat-name-top");
    chat_name.html(data.name);
    let id = $("#chat-to-id-hide");

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

    getChatFile(parseInt(id.html()), parseInt(uid.text().trim()));
}

/**
 * 获取聊天记录
 * @param id
 * @param uid
 */
function getChatFile(id, uid) {
    const url = 'chatfile?id=' + id + "&uid=" + uid;

    const general = {
        headers: HEADERS_JSON,
        method: METHOD_GET
    };

    fetch(url, general)
        .then(response => { return response.json() })
        .then(function (result) {
            console.log("file get::" + result.meta.status);
            if (result.meta.status === 200) {
                for (let i = result.data.length-1; i >= 0; i--) {
                    showChatContent(JSON.stringify(result.data[i]), uid);
                }
            } else if (result.meta.status === 400) {
                console.log("Had not chatted with him.")
            }
        })
        .catch(error => console.log(error));
}


/**
 * 处理发送信息
 */
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

    $("#output").append("<div class='bubble me'>" + _data.val() + "</div>");
    scroll();
    if (parseInt(to_id.html()) !== 0) {
        stompClient.send("/app/private", {}, JSON.stringify(message));
    } else {
        stompClient.send("/app/group", {}, JSON.stringify(message));
    }
    _data.val("");
};

//发送文件
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

            $("#output").append("<a href='#' class='bubble me' onclick='downloadFile("+ ret.data.id +")'>文件:[" + ret.data.filename + "]</a>");
            scroll();
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

/**
 * 私聊收到消息展示
 * @param message
 */
function handlePrivate(message) {
    let to_id = $("#chat-to-id-hide");
    let msg = JSON.parse(message.body);
    if (parseInt(to_id.html()) === msg.from_id) {
        if (msg.type === "s") {
            $("#output").append(
                "<div class='bubble you'>" + msg.content + "</div>"
            );
        } else if (msg.type === "f") {
            $("#output").append(
                "<a href='#' class='bubble you' onclick='downloadFile("+ msg.content +")'>文件:[" + msg.filename + "]</a>"
            );
        }
    }
    scroll();
}

/**
 * 群聊收到消息显示
 */
function handleNotification(message) {
    let msg = JSON.parse(message.body);
    let to_id = $("#chat-to-id-hide");
    /** @namespace msg.data.fromID */
    if (0 === parseInt(to_id.html()) && parseInt(msg.data.fromId) !== parseInt(uid.html())) {
        $('#output').append(
            "<div class='bubble you'>" +"[" + msg.data.name + "]:    " +msg.data.content + "</div>"
        );
    }
    scroll();
}

/**
 * 展示消息
 * @param message
 * @param uid
 */
function showChatContent(message, uid) {
    let msg = JSON.parse(message);
    /** @namespace msg.fromId */
    if (msg.fromId === uid){
        // $('#output').append("<div class='bubble me'>" + msg.content + "</div>");

        if (msg.type === "s") {
            $("#output").append(
                "<div class='bubble me'>" + msg.content + "</div>"
            );
        } else if (msg.type === "f") {
            $("#output").append(
                "<a href='#' class='bubble me' onclick='downloadFile("+ msg.content +")'>文件:[" + msg.filename + "]</a>"
            );
        }
    } else {
    /** @namespace msg.toId */
        if (typeof (msg.toId) === 'undefined') {

            $('#output').append("<div class='bubble you'>" +"[" + msg.name + "]:    " +msg.content + "</div>")

        } else {
            // $('#output').append("<div class='bubble you'>" + msg.content + "</div>");
            if (msg.type === "s") {
                $("#output").append(
                    "<div class='bubble you'>" + msg.content + "</div>"
                );
            } else if (msg.type === "f") {
                $("#output").append(
                    "<a href='#' class='bubble you' onclick='downloadFile("+ msg.content +")'>文件:[" + msg.filename + "]</a>"
                );
            }
        }
    }
    scroll()
}

/**
 * 滚动
 */
function scroll() {
    let div = $("#output");
    const top = 500000;
    div.scrollTop(top);
}

function getHistory() {
    $("#history").html("");
    let to_id = parseInt($("#chat-to-id-hide").html());
    let id = parseInt(uid.text().trim());
    let url = "message/history?id=" + to_id + "&uid=" + id;

    const general = {
        headers: HEADERS_JSON,
        method: METHOD_GET
    };

    fetch(url, general)
        .then(response => { return response.json() })
        .then(function (result) {
            console.log("file get::" + result.meta.status);
            if (result.meta.status === 200) {

                for (let i = 0; i < result.data.length; i++) {
                    // console.log("result:" + JSON.stringify(result.data[i]));
                    showHistoryContent(JSON.stringify(result.data[i]), id);
                }
            } else if (result.meta.status === 400) {
                console.log("Had not chatted with him.")
            }
        })
        .catch(error => console.log(error));
}

function showHistoryContent(message, uid) {

    let msg = JSON.parse(message);
    /** @namespace msg.fromId */
    if (msg.fromId === uid){
        // $('#output').append("<div class='bubble me'>" + msg.content + "</div>");

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
            // $('#output').append("<div class='bubble you'>" + msg.content + "</div>");
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