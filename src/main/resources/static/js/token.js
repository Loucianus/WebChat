const METHOD_POST = "POST";

const HEADERS_JSON = new Headers({
    "Content-Type":"application/json;charset=UTF-8"
});

/** @namespace result.meta */
let password_flag, username_flag = false;

// 用户名合法验证
function bindUsername() {
    const username_reg =/^\w+((-\w+)|(\.\w+))*@[A-Za-z0-9]+(([.\-])[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
    const username = $("#InputEmail");
    const submit = $("#submit");
    if (!username_reg.test(username.val())){
        username.attr("class", "form-control is-invalid");
        $("#usernameHelpBlock").show();
        username.attr("required");
        username_flag = false
    }
    else {
        username.attr("class", "form-control is-valid");
        $("#usernameHelpBlock").hide();
        username.removeAttr("required");
        username_flag = true
    }

    if (username_flag === true && password_flag === true) {
        submit.removeAttr("disabled")
    } else {
        submit.attr("disabled", "disabled")
    }
}

// 密码合法验证
function bindPassword() {
    const password = $("#InputPassword");
    const submit = $("#submit");
    if (password.val() === "" || password.val().length < 8 || password.val().length > 20) {
        password.attr("class", "form-control is-invalid");
        $("#passwordHelpBlock").show();
        password_flag = false
    } else {
        password.attr("class", "form-control is-valid");
        $("#passwordHelpBlock").hide();
        password_flag = true
    }
    if (username_flag === true && password_flag === true) {
        submit.removeAttr("disabled")
    } else {
        submit.attr("disabled", "disabled")
    }
}

// 登陆
function submitFormLogin() {

    const url = "token";

    if ($("#InputEmail").val() === "" ) {
        alert("请输入邮箱!!");
        $("#InputEmail").focus()
    } else if ($("#InputPassword").val() === "") {
        alert("请输入密码!!");
        $("#InputPassword").focus()
    }

    const data = {
        "username": $("#InputEmail").val(),
        "password": $("#InputPassword").val()
    };

    const headers = new Headers({
            "Content-Type":"application/json;charset=UTF-8"
        });

    const method = "POST";

    const general = {
        headers: headers,
        body: JSON.stringify(data),
        method: method
    };

    fetch(url, general)
        .then(response => { return response.json() })
        .then(function (result) {

            if (result.meta.status === 200) {
                console.log(result.data);
                window.location.href = 'index'
            } else {
                $("#accountHelpBlock").fadeOut();
            }
        })
        .catch(error => console.log(error))
}