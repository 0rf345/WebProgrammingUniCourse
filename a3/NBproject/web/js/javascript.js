// These help with coding the page
var p = "<p>";
var ep= "</p>";
var l = "<label>";
var el= "</label>";

function validatePassword(){
  
  if($("#userp").valid()) {
      $("#userpp").pattern = $("#userp").pattern;
  }
  
}

function loginFormCreate() {
    $("#form").html("");
    $("#form").append(p+l+"Username"+el+"<input type='text' id='usern' placeholder='username' />"+ep);
    $("#form").append(p+l+"Password"+el+"<input type='password' id='userp' placeholder='password' />"+ep);
    $("#form").append("<p><input type='submit' value='Login' onclick='loginPOST();' /></p>");
}

function registerFormCreate() {
    $("#form").html("");
    $("#form").append(p+l+"Username"+el+"<input type='text' id='usern' placeholder='username' pattern='[a-zA-Z0-9]{8,}' required title='8 or more characters' />"+ep);
    $("#form").append(p+l+"e-mail"+el+"<input type='email' id='email' placeholder='johndoe@gmail.com' pattern='[a-z0-9._%+-]+@[a-z0-9.-]+[.]+\.[a-z]{0,}$' required title='text(@)text(.)text Can have more (.)' />"+ep);
    $("#form").append(p+l+"Password"+el+"<input type='password' id='userp' placeholder='password' required title='6-10 characters, must contain 1 latin character, a number and a special symbol'/>"+ep);
    $("#form").append(p+l+"Confirm Password"+el+"<input type='password' id='userpp' placeholder='password' required />"+ep);
    $("#form").append(p+l+"FirstName"+el+"<input type='text' id='fname' placeholder='John' />"+ep);
    $("#form").append(p+l+"LastName"+el+"<input type='password' id='lname' placeholder='Doe' />"+ep);
    $("#form").append(p+l+"Birthday"+el+"<input type='date' id='bdate' max='2001-01-01'/>"+ep); // Going with year alone on defining age. The UI format is based on the user's locale.
    $("#form").append(p+"<input type='radio' name='sex' value='1' />Not applicable"+ep);
    $("#form").append(p+"<input type='radio' name='sex' value='2'>Male"+ep);
    $("#form").append(p+"<input type='radio' name='sex' value='3'>Female"+ep);
    $("#form").append(p+l+"Town"+el+"<input type='text' id='town' placeholder='Honolulu' />"+ep);
    $("#form").append(p+"<input type='submit' value='Register' onclick='registerPOST();' />"+ep);
    $("#userp").on("change", validatePassword);
    
    
    password = $("#userp");
    confirm_password = $("#userpp");
}

function loginPOST() {
    var usern = $("#usern").val();
    var userp = $("#userp").val();
    var xhr = new XMLHttpRequest();
    
    xhr.open('POST', 'NewServlet?usern=' + usern + '&userp=' + userp);
    xhr.onload = function() {
        if(xhr.readyState === 4 && xhr.status === 200) {
            //Response was OK
            $("#loginForm").after(xhr.responseText);
        }else if(xhr.status !== 200) {
            alert('Request failed with code: '+xhr.status);
        }
    };
    
    xhr.setRequestHeader('ContentType','application/x-www-form-urlencoded');
    xhr.send();
}   

function registerPOST() {
    
}