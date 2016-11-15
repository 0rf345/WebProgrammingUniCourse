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
    var xhr = new XMLHttpRequest();
    
    xhr.open('POST', 'NewServlet');
    xhr.onload = function() {
        if(xhr.readyState === 4 && xhr.status === 200) {
            //Response was OK
            alert("SUCCESS");
        }else if(xhr.status !== 200) {
            alert('Request failed with code: '+xhr.status);
        }
    };
    
    xhr.setRequestHeader('ContentType','application/x-www-form-urlencoded');
    xhr.send('usern='+usern+'&userp='+userp);
}   