/**
 * WebSocket
 */

let stompClient = null;
let sessionId = null;
const uid = $("#worker-uid");
function connect() {
    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        sessionId = /\/([^\/]+)\/websocket/.exec(socket._transport.url)[1];
        // 群聊
        stompClient.subscribe('/topic/greetings', function (greeting) {
            // showGreeting(JSON.parse(greeting.body).content);
            console.log("public");
            // handleNotification(greeting)
        });
        // 私信
        stompClient.subscribe('/user/topic/private', function (greeting) {
            handlePrivate(greeting);
            console.log("private")
        });

        // 在线用户列表
        stompClient.subscribe('/topic/userlist', function (greeting) {
            //     var parse = JSON.parse(greeting.body);
            //     if (parse.online) {
            //         showUser(parse.name, parse.id);
            //     } else {
            //         removeUser(parse.id);
            //     }
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

    const url = 'contacts/' + uid.text().trim();

    const general = {
        headers: HEADERS_JSON,
        method: METHOD_GET
    };

    fetch(url, general)
        .then(result => { return result.json() })
        .then(function (result) {
            console.log("result:" + (result.data));
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
    console.log("ContactList:" + JSON.stringify(data));
    let html = '';
    for (let i=0, len = data.length;i < len; i++) {
        /** @namespace data.portrait */
        html += "<div class='person' data-chat='person' onclick='chatTO(" + JSON.stringify(data[i]) + ")'>" +
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
function chatTO(data) {
    // 清除聊天信息
    $('#output').html("");
    // 设置聊天对象信息
    $("#chat-name").html(data.name);
    /** @namespace data.email */
    $("#chat-email").html(data.email);

    // uid.html(data.uid);
    let id = $("#chat-to-id");
    id.html(data.id);

    getChatFile(parseInt(id.html()), parseInt(uid.html()));
}

/**
 * 处理发送信息
 */
$('#send-text').click(function () {

    let _data = $('#input-text');

    if ($.trim(_data.val()) === "") {
        alert("Please input message and send.");
        console.log("Please not input empty message!!");
        return
    }

    let _to_email = $("#chat-email").html();

    if (_to_email === "" || _to_email === null) {
        alert("The chat partner not selected.");
        console.log("Please choose the user to chat.");
        return;
    }

    let message = {
        "to_id" : parseInt($("#chat-to-id").html()),
        "from_id": parseInt($("#chat-uid").html()),
        "to_email": _to_email,
        "data" : _data.val(),
        "type": 's',
        "name": $("#chat-name").html()
    };

    $("#output").append("<div class='bubble me'>" + _data.val() + "</div>");
    scroll();
    stompClient.send("/app/private",{},JSON.stringify(message));
    _data.val("");

});

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
                for (let i = 0, len = result.data.length; i < len; i++) {
                    showChatContent(JSON.stringify(result.data[i]), uid);
                }
            } else if (result.meta.status === 400) {
                console.log("Had not chatted with him.")
            }
        })
        .catch(error => console.log(error));
}

/**
 * 私聊收到消息展示
 * @param message
 */
function handlePrivate(message) {
    let msg = JSON.parse(message.body);
    console.log("message.body" + msg);
    $('#output').append(
        "<div class='bubble you'>" + msg.data + "</div>"
    );
    scroll();
}

/**
 * 群聊收到消息显示
 */
// function handleNotification(message) {
//     let msg = JSON.parse(message.body);
//     console.log("message.body" + msg);
//     $('#output').append(
//         "<div class='bubble you'>" + msg.data + "</div>"
//     );
// }

/**
 * 展示消息
 * @param message
 * @param uid
 */
function showChatContent(message, uid) {
    let msg = JSON.parse(message);
    console.log(msg);
    /** @namespace msg.fromId */
    if (msg.fromId === uid){
        $('#output').append("<div class='bubble me'>"+ msg.content +"</div>");
    } else {
        $('#output').append("<div class='bubble you'>"+ msg.content +"</div>");
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

/**
 * 查看用户资料
 */
$("#chat-user-model").click(function () {
    const url = 'worker/id=' + $("#chat-to-id").text();

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

            console.log("file get::" + result.meta.status);
            if (result.meta.status === 200) {
                console.log(result.toString())
               // let data = result.data;
                // $("#chat-user-model-title").html(data.name);
                // $("#chat-user-model-email").html("Email    :    "+data.username);
                // $("#chat-user-model-role").html("Role     :    "+data.role)

            } else  {
                console.log("code::" + result.meta.status)
            }

        })
        .catch(error => console.log(error));
});


