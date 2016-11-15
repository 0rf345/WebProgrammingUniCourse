function loginFormCreate() {
    var usern = "<input type='text' id='usern' value='username' />";
    var userp = "<input type='password' id='userp' value='password' />";
    $("#loginForm").html("");
    $("#loginForm").append("<p>Username "+usern+"</p>");
    $("#loginForm").append("<p>Password "+userp+"</p>");
    $("#loginForm").append("<p><input type='submit' value='Login' onclick='loginPOST();' /></p>");
}

function loginPOST() {
    var usern = $("#usern").val();
    var userp = $("#userp").val();
}