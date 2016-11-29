"use strict";
// These help with coding the page
var p = "<p>";
var ep= "</p>";
var l = "<label>";
var el= "</label>";
var exclude = "";


/*
 * In order to facilitate an XSS attack I inputed a script in any of the fields
 * that after registration are posted on the browser and have no limitation
 * on what kind of characters they get. Namely: username, town, extra-info.
 * In order to prevent it from hapenning we only
 * need to disallow the user from inputing <script>, etc 
 */

/*
 * Checks that the user passwords on registration match by make the pattern
 * of the second one the same as the value of the first one when the first one
 * is valid.
 */
function validatePassword(){
  if(document.getElementById("userp").checkValidity()) {
      document.getElementById("userpp").pattern = document.getElementById("userp").value;
  } 
}

/*
 * Checks town for XSS attack
 */
function validateTown() {
    if(document.getElementById("town").checkValidity()) {
        if(document.getElementById("town").value.includes("<script>")||
           document.getElementById("town").value.includes("<style>")) {    
            alert("You are not playing fair. Please do not try to exploit XSS");
            document.getElementById("town").value = "";
        }
    }
}

/*
 * Checks extra for XSS attack
 */
function validateExtra() {
    if(document.getElementById("extraInfo").checkValidity()) {
        if(document.getElementById("extraInfo").value.includes("<script>")||
           document.getElementById("extraInfo").value.includes("<style>")) {    
            alert("You are not playing fair. Please do not try to exploit XSS");
            document.getElementById("extraInfo").value = "";
        }
    }
}

var hid = 1;
function togglePassword() {
    if(hid === 1) {
        document.getElementById("userp").type = "text";
        $("#toggler").html("Hide");
        hid = 0;
    }else{
        document.getElementById("userp").type = "password";
        $("#toggler").html("Show");
        hid = 1;
    }
}

/*
 * Checks username using the servlet to see if it has been registered before
 */
function validateUsername() {
    if(document.getElementById("usern").checkValidity()) {
        if(document.getElementById("usern").value.includes("<script>")||
           document.getElementById("usern").value.includes("<style>")) {    
            alert("You are not playing fair. Please do not try to exploit XSS");
            document.getElementById("usern").value = "";
        }else{
            var usern = $("#usern").val();
            var xhr = new XMLHttpRequest();
            xhr.open('POST', 'NewServlet?login=2&usern='+usern);
            xhr.onload = function() {
                if(xhr.readyState === 4 && xhr.status === 200) {
                    //Response was OK
                    if(xhr.responseText === "0") {
                        alert("Username: "+$("#usern").val()+" is taken, please choose another.");
                        document.getElementById("usern").value = "";
                    }
                }else if(xhr.status !== 200) {
                    alert('Request failed with code: '+xhr.status);
                }
            };
            xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
            xhr.send();
        }
    }
}

/*
 * Checks email using the servlet to see if it has been registered before
 */
function validateEmail() {
    if(document.getElementById("email").checkValidity()) {
        var email = $("#email").val();
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'NewServlet?login=3&email='+email);
        xhr.onload = function() {
            if(xhr.readyState === 4 && xhr.status === 200) {
                if(xhr.responseText === "0") {
                    alert('Email: '+$("#email").val()+' already in database.');
                    document.getElementById("email").value = "";
                }
            }else if(xhr.status !== 200) {
                alert('Request failed with code: '+xhr.status);
            }
        };
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.send();
    }
}

/*
 * Creates the form so the users can login
 */
function loginFormCreate() {
    $("#form").html("");
    $("#form").append(p+l+"Username"+el+"<input type='text' id='usern' placeholder='username' />"+ep);
    $("#form").append(p+l+"Password"+el+"<input type='password' id='userp' placeholder='password' />"+ep);
    $("#form").append("<p><input type='button' value='Login' onclick='loginPOST();' /></p>");
}

/*
 * Creates the form so the users can register
 */
function registerFormCreate() {
    var ppattern = "pattern='(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?\\x26])[A-Za-z\\d$@$!%*#?\\x26]{6,10}$' ";
    var epattern = "pattern='([a-zA-Z0-9]{1,}(\\.*[a-zA-Z0-9]){0,}@[a-zA-Z0-9]{1,}(\\.{1}[a-zA-Z0-9]{1,}){1,})' ";
    $("#form").html("");
    // This is the only XSS vulnerable point of code, easily preventable with a better pattern...
    $("#form").append(p+l+"*Username"+el+"<input type='text' id='usern' placeholder='username' pattern='.{8,}' required title='8 or more latin characters' />"+ep);
    $("#form").append(p+l+"*E-mail"+el+"<input type='email' id='email' placeholder='johndoes@gmail.com' "+epattern+"required title='text(@)text(.)text Can have more (.)' />"+ep);
    $("#form").append(p+l+"*Password"+el+"<input type='password' id='userp' placeholder='password' "+ppattern+"required title='6-10 characters, must contain 1 latin character, a number and a special symbol'/>"+ep);
    $("#form").append(p+l+"*Confirm Password"+el+"<input type='password' id='userpp' placeholder='password' "+ppattern+"required title='match the previous password' />"+ep);
    $("#form").append(p+l+"*FirstName"+el+"<input type='text' id='fname' placeholder='John' pattern='[a-zA-Z]{3,20}' required title='3-20 latin characters' />"+ep);
    $("#form").append(p+l+"*LastName"+el+"<input type='text' id='lname' placeholder='Does' pattern='[a-zA-Z]{4,20}' required title='4-20 latin characters' />"+ep);
    $("#form").append(p+l+"*Birthday"+el+"<input type='date' id='bdate' max='2001-01-01' required />"+ep); // Going with year alone on defining age. The UI format is based on the user's locale.
    $("#form").append(p+"<input type='radio' name='sex' value='NotApplicable' />Not applicable"+ep);
    $("#form").append(p+"<input type='radio' name='sex' value='Male'>Male"+ep);
    $("#form").append(p+"<input type='radio' name='sex' value='Female'>Female"+ep);
    populateCountries("#form");
    $("#form").append(ep);
    $("#form").append(p+l+"*Town"+el+"<input type='text' id='town' placeholder='Honolulu' required pattern='.{2,50}' title='Must be 2-50 characters long' />"+ep);
    $("#form").append(p+l+"Extra Info"+el+"<textarea rows='10' cols='50' id='extraInfo' placeholder='Lorem ipsum...' maxlength=500 title='Up to 500 characters'></textarea>"+ep);
    $("#form").append(p+"<input type='submit' value='Register' onclick='checkFields();' />"+ep);
    $("#userp").on("change", validatePassword);
    $("#usern").on("change", validateUsername);
    $("#email").on("change", validateEmail);
    $("#town").on("change", validateTown);
    $("#extraInfo").on("change", validateExtra);
}

/*
 * Checks thall all register fields are valid to begin the POST to servlet
 */
function checkFields() {
    if(document.getElementById("usern").checkValidity() && 
            document.getElementById("userp").checkValidity()&&
            document.getElementById("email").checkValidity() && 
            document.getElementById("fname").checkValidity()&&
            document.getElementById("lname").checkValidity() && 
            document.getElementById("town").checkValidity()&&
            document.getElementById("country").checkValidity() && 
            document.getElementById("bdate").checkValidity()) {
        registerPOST();
    }
}

/*
 * Asks servlet for all registered users
 */
function showUsers() {
    $("#welcome").html("");
    $("#logged").html("");
    
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'NewServlet?login=4');
    xhr.onload = function() {
        if(xhr.readyState === 4 && xhr.status === 200) {
            //Response was OK
            $("#logged").append(xhr.responseText);
        }else if(xhr.status !== 200) {
            alert('Request failed with code: '+xhr.status);
        }
    };
    
    xhr.setRequestHeader('ContentType','application/x-www-form-urlencoded');
    xhr.send();    
}

/*
 * Asks servlet for logged in user's info
 */
function showInfo() {
    $("#welcome").html("");
    $("#logged").html("");
    
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'NewServlet?login=5');
    xhr.onload = function() {
        if(xhr.readyState === 4 && xhr.status === 200) {
            //Response was OK
            $("#logged").append(xhr.responseText);
        }else if(xhr.status !== 200) {
            alert('Request failed with code: '+xhr.status);
        }
    };
    
    xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
    xhr.send(); 
}

/*
 * Sends the inputed username and password to the server in order to attempt
 * a login. Logs the user in if successfull, alerts of what hapenned otherwise.
 */
function loginPOST() {
    var usern = $("#usern").val();
    var userp = $("#userp").val();
    var xhr = new XMLHttpRequest();
    
    xhr.open('POST', 'NewServlet?login=1&usern=' + usern + '&userp=' + userp);
    xhr.onload = function() {
        if(xhr.readyState === 4 && xhr.status === 200) {
            //Response was OK
            if(xhr.responseText === "1") {
                // Successfully login
                $("#header").html("");
                $("#form").html("");
                $("#form").append("<br><p ><h1 id='welcome'>You have successfully signed in!</h1></p>");
                $("#form").append("<p><input type='button' value='MyInfo' onclick='showInfo();' />\
                <input type='button' value='Usernames' onclick='showUsers();' /> \
                <form name=\"uploadForm\"> \
                <input id=\"images\" type=\"file\" webkitdirectory mozdirectory directory name=\"myFiles\" \
                onchange=\"TIV3285.loadImages();\" multiple/> \
                </form> \
                <button id=\"buttonShow\" onclick=\"TIV3285.showImages('list');\">Show me pictures</button></p>");
                $("#header").append("<input type='button' value='Log Out' onclick='location.reload();' />");
            }else if(xhr.responseText === "2") {
                // Wrong Password
                alert('Wrond username - password combination');
                document.getElementById("usern").value = "";
                document.getElementById("userp").value = "";
            }else if(xhr.responseText === "3") {
                // Wrong Username
                alert('Username not found');
                document.getElementById("usern").value = "";
                document.getElementById("userp").value = "";
            }
        }else if(xhr.status !== 200) {
            alert('Request failed with code: '+xhr.status);
        }
    };
    
    xhr.setRequestHeader('ContentType','application/x-www-form-urlencoded');
    xhr.send();
}   

/*
 * Gives all the info to the servlet to register a new user
 */
function registerPOST() {
    var usern   = $("#usern").val();
    var userp   = $("#userp").val();
    var email   = $("#email").val();
    var fname   = $("#fname").val();
    var lname   = $("#lname").val();
    var date    = $("#bdate").val();
    var sex     = $('input[name="sex"]:checked').val();
    var country = $("#country").val();
    var town    = $("#town").val();
    var extra   = $("#extraInfo").val();
    
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'NewServlet?login=0&usern='+usern+'&userp='+userp+'&email='+
            email+'&fname='+fname+'&lname='+lname+'&date='+date+'&sex='+
            sex+'&country='+country+'&town='+town+'&extra='+extra);
    xhr.onload = function() {
        if(xhr.readyState === 4 && xhr.status === 200) {
            //Response was OK
            $("#form").html(xhr.responseText);
        }else if(xhr.status !== 200) {
            alert('Request failed with code: '+xhr.status);
        }
    };
    
    xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
    xhr.send();
    
}

/*
 * Sends new info of user to the servlet so it can update the user's info.
 */
function saveChanges() {
    
    if(!document.getElementById("usern").checkValidity()) {
        alert("Username invalid");
        return;
    }
    if(!document.getElementById("userp").checkValidity()) {
        alert("Password invalid");
        return;
    }
    if(!document.getElementById("email").checkValidity()) {
        alert("Email invalid");
        return;
    }
    if(!document.getElementById("fname").checkValidity()) {
        alert("First name invalid");
        return;
    }
    if(!document.getElementById("lname").checkValidity()) {
        alert("Last name invalid");
        return;
    }
    if(!document.getElementById("bdate").checkValidity()) {
        alert("Birthday invalid");
        return;
    }
    if(!document.getElementById("country").checkValidity()) {
        alert("Country invalid");
        return;
    }
    if(!document.getElementById("town").checkValidity()) {
        alert("Town invalid");
        return;
    }
    if(!document.getElementById("extraInfo").checkValidity()) {
        alert("Extra info invalid");
        return;
    }
    
    var usern   = $("#usern").val();
    var userp   = $("#userp").val();
    var email   = $("#email").val();
    var fname   = $("#fname").val();
    var lname   = $("#lname").val();
    var date    = $("#bdate").val();
    var sex     = $('input[name="sex"]:checked').val();
    var country = $("#country").val();
    var town    = $("#town").val();
    var extra   = $("#extraInfo").val();
    
    var usernn = "";
    var userpp = "";
    var emaill = "";
    var fnamee = "";
    var lnamee = "";
    var datee = "";
    var sexx = "";
    var countryy = "";
    var townn = "";
    var extraa = "";
    
    if(usern !== "") usernn = "&usern=";
    if(userp !== "") userpp = "&userp=";
    if(email !== "") emaill = "&email=";
    if(fname !== "") fnamee = "&fname=";
    if(lname !== "") lnamee = "&lname=";
    if(date  !== "") datee  = "&date=";
    if(sex   !== "") sexx   = "&sex=";
    if(country !== "") countryy= "&country=";
    if(town  !== "") townn  = "&town=";
    if(extra !== "") extraa = "&extra=";
    
    
    
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'NewServlet?login=6'+usernn+usern+userpp+userp+emaill+
            email+fnamee+fname+lnamee+lname+datee+date+sexx+
            sex+countryy+country+townn+town+extraa+extra);
        xhr.onload = function() {
        if(xhr.readyState === 4 && xhr.status === 200) {
            //Response was OK
            $("#logged").html(xhr.responseText);
        }else if(xhr.status !== 200) {
            alert('Request failed with code: '+xhr.status);
        }
    };
    
    xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
    xhr.send();
}

/*
 * Creates a button for saving user info changes
 */
function populateButton() {
    $("#logged").append("<input type='button' value='Save Changes' onclick='saveChanges();'/>");
}

/*
 * Populates 'where' with a select form of countries.
 */
function populateCountries(where) {
    var tmp = "";
    if(where === "#form") tmp = "*";
    $(where).append(p+l+tmp+"Country"+el+"<select id='country'>"+
"<option value='Greece'>Greece</option>"+
"<option value='Afganistan'>Afghanistan</option>"+
"<option value='Albania'>Albania</option>"+
"<option value='Algeria'>Algeria</option>"+
"<option value='American Samoa'>American Samoa</option>"+
"<option value='Andorra'>Andorra</option>"+
"<option value='Angola'>Angola</option>"+
"<option value='Anguilla'>Anguilla</option>"+
"<option value='Antigua &amp; Barbuda'>Antigua &amp; Barbuda</option>"+
"<option value='Argentina'>Argentina</option>"+
"<option value='Armenia'>Armenia</option>"+
"<option value='Aruba'>Aruba</option>"+
"<option value='Australia'>Australia</option>"+
"<option value='Austria'>Austria</option>"+
"<option value='Azerbaijan'>Azerbaijan</option>"+
"<option value='Bahamas'>Bahamas</option>"+
"<option value='Bahrain'>Bahrain</option>"+
"<option value='Bangladesh'>Bangladesh</option>"+
"<option value='Barbados'>Barbados</option>"+
"<option value='Belarus'>Belarus</option>"+
"<option value='Belgium'>Belgium</option>"+
"<option value='Belize'>Belize</option>"+
"<option value='Benin'>Benin</option>"+
"<option value='Bermuda'>Bermuda</option>"+
"<option value='Bhutan'>Bhutan</option>"+
"<option value='Bolivia'>Bolivia</option>"+
"<option value='Bonaire'>Bonaire</option>"+
"<option value='Bosnia &amp; Herzegovina'>Bosnia &amp; Herzegovina</option>"+
"<option value='Botswana'>Botswana</option>"+
"<option value='Brazil'>Brazil</option>"+
"<option value='British Indian Ocean Ter'>British Indian Ocean Ter</option>"+
"<option value='Brunei'>Brunei</option>"+
"<option value='Bulgaria'>Bulgaria</option>"+
"<option value='Burkina Faso'>Burkina Faso</option>"+
"<option value='Burundi'>Burundi</option>"+
"<option value='Cambodia'>Cambodia</option>"+
"<option value='Cameroon'>Cameroon</option>"+
"<option value='Canada'>Canada</option>"+
"<option value='Canary Islands'>Canary Islands</option>"+
"<option value='Cape Verde'>Cape Verde</option>"+
"<option value='Cayman Islands'>Cayman Islands</option>"+
"<option value='Central African Republic'>Central African Republic</option>"+
"<option value='Chad'>Chad</option>"+
"<option value='Channel Islands'>Channel Islands</option>"+
"<option value='Chile'>Chile</option>"+
"<option value='China'>China</option>"+
"<option value='Christmas Island'>Christmas Island</option>"+
"<option value='Cocos Island'>Cocos Island</option>"+
"<option value='Colombia'>Colombia</option>"+
"<option value='Comoros'>Comoros</option>"+
"<option value='Congo'>Congo</option>"+
"<option value='Cook Islands'>Cook Islands</option>"+
"<option value='Costa Rica'>Costa Rica</option>"+
"<option value='Cote DIvoire'>Cote D'Ivoire</option>"+
"<option value='Croatia'>Croatia</option>"+
"<option value='Cuba'>Cuba</option>"+
"<option value='Curaco'>Curacao</option>"+
"<option value='Cyprus'>Cyprus</option>"+
"<option value='Czech Republic'>Czech Republic</option>"+
"<option value='Denmark'>Denmark</option>"+
"<option value='Djibouti'>Djibouti</option>"+
"<option value='Dominica'>Dominica</option>"+
"<option value='Dominican Republic'>Dominican Republic</option>"+
"<option value='East Timor'>East Timor</option>"+
"<option value='Ecuador'>Ecuador</option>"+
"<option value='Egypt'>Egypt</option>"+
"<option value='El Salvador'>El Salvador</option>"+
"<option value='Equatorial Guinea'>Equatorial Guinea</option>"+
"<option value='Eritrea'>Eritrea</option>"+
"<option value='Estonia'>Estonia</option>"+
"<option value='Ethiopia'>Ethiopia</option>"+
"<option value='Falkland Islands'>Falkland Islands</option>"+
"<option value='Faroe Islands'>Faroe Islands</option>"+
"<option value='Fiji'>Fiji</option>"+
"<option value='Finland'>Finland</option>"+
"<option value='France'>France</option>"+
"<option value='French Guiana'>French Guiana</option>"+
"<option value='French Polynesia'>French Polynesia</option>"+
"<option value='French Southern Ter'>French Southern Ter</option>"+
"<option value='Gabon'>Gabon</option>"+
"<option value='Gambia'>Gambia</option>"+
"<option value='Georgia'>Georgia</option>"+
"<option value='Germany'>Germany</option>"+
"<option value='Ghana'>Ghana</option>"+
"<option value='Gibraltar'>Gibraltar</option>"+
"<option value='Great Britain'>Great Britain</option>"+
"<option value='Greenland'>Greenland</option>"+
"<option value='Grenada'>Grenada</option>"+
"<option value='Guadeloupe'>Guadeloupe</option>"+
"<option value='Guam'>Guam</option>"+
"<option value='Guatemala'>Guatemala</option>"+
"<option value='Guinea'>Guinea</option>"+
"<option value='Guyana'>Guyana</option>"+
"<option value='Haiti'>Haiti</option>"+
"<option value='Hawaii'>Hawaii</option>"+
"<option value='Honduras'>Honduras</option>"+
"<option value='Hong Kong'>Hong Kong</option>"+
"<option value='Hungary'>Hungary</option>"+
"<option value='Iceland'>Iceland</option>"+
"<option value='India'>India</option>"+
"<option value='Indonesia'>Indonesia</option>"+
"<option value='Iran'>Iran</option>"+
"<option value='Iraq'>Iraq</option>"+
"<option value='Ireland'>Ireland</option>"+
"<option value='Isle of Man'>Isle of Man</option>"+
"<option value='Israel'>Israel</option>"+
"<option value='Italy'>Italy</option>"+
"<option value='Jamaica'>Jamaica</option>"+
"<option value='Japan'>Japan</option>"+
"<option value='Jordan'>Jordan</option>"+
"<option value='Kazakhstan'>Kazakhstan</option>"+
"<option value='Kenya'>Kenya</option>"+
"<option value='Kiribati'>Kiribati</option>"+
"<option value='Korea North'>Korea North</option>"+
"<option value='Korea Sout'>Korea South</option>"+
"<option value='Kuwait'>Kuwait</option>"+
"<option value='Kyrgyzstan'>Kyrgyzstan</option>"+
"<option value='Laos'>Laos</option>"+
"<option value='Latvia'>Latvia</option>"+
"<option value='Lebanon'>Lebanon</option>"+
"<option value='Lesotho'>Lesotho</option>"+
"<option value='Liberia'>Liberia</option>"+
"<option value='Libya'>Libya</option>"+
"<option value='Liechtenstein'>Liechtenstein</option>"+
"<option value='Lithuania'>Lithuania</option>"+
"<option value='Luxembourg'>Luxembourg</option>"+
"<option value='Macau'>Macau</option>"+
"<option value='Macedonia'>Macedonia</option>"+
"<option value='Madagascar'>Madagascar</option>"+
"<option value='Malaysia'>Malaysia</option>"+
"<option value='Malawi'>Malawi</option>"+
"<option value='Maldives'>Maldives</option>"+
"<option value='Mali'>Mali</option>"+
"<option value='Malta'>Malta</option>"+
"<option value='Marshall Islands'>Marshall Islands</option>"+
"<option value='Martinique'>Martinique</option>"+
"<option value='Mauritania'>Mauritania</option>"+
"<option value='Mauritius'>Mauritius</option>"+
"<option value='Mayotte'>Mayotte</option>"+
"<option value='Mexico'>Mexico</option>"+
"<option value='Midway Islands'>Midway Islands</option>"+
"<option value='Moldova'>Moldova</option>"+
"<option value='Monaco'>Monaco</option>"+
"<option value='Mongolia'>Mongolia</option>"+
"<option value='Montserrat'>Montserrat</option>"+
"<option value='Morocco'>Morocco</option>"+
"<option value='Mozambique'>Mozambique</option>"+
"<option value='Myanmar'>Myanmar</option>"+
"<option value='Nambia'>Nambia</option>"+
"<option value='Nauru'>Nauru</option>"+
"<option value='Nepal'>Nepal</option>"+
"<option value='Netherland Antilles'>Netherland Antilles</option>"+
"<option value='Netherlands'>Netherlands (Holland, Europe)</option>"+
"<option value='Nevis'>Nevis</option>"+
"<option value='New Caledonia'>New Caledonia</option>"+
"<option value='New Zealand'>New Zealand</option>"+
"<option value='Nicaragua'>Nicaragua</option>"+
"<option value='Niger'>Niger</option>"+
"<option value='Nigeria'>Nigeria</option>"+
"<option value='Niue'>Niue</option>"+
"<option value='Norfolk Island'>Norfolk Island</option>"+
"<option value='Norway'>Norway</option>"+
"<option value='Oman'>Oman</option>"+
"<option value='Pakistan'>Pakistan</option>"+
"<option value='Palau Island'>Palau Island</option>"+
"<option value='Palestine'>Palestine</option>"+
"<option value='Panama'>Panama</option>"+
"<option value='Papua New Guinea'>Papua New Guinea</option>"+
"<option value='Paraguay'>Paraguay</option>"+
"<option value='Peru'>Peru</option>"+
"<option value='Phillipines'>Philippines</option>"+
"<option value='Pitcairn Island'>Pitcairn Island</option>"+
"<option value='Poland'>Poland</option>"+
"<option value='Portugal'>Portugal</option>"+
"<option value='Puerto Rico'>Puerto Rico</option>"+
"<option value='Qatar'>Qatar</option>"+
"<option value='Republic of Montenegro'>Republic of Montenegro</option>"+
"<option value='Republic of Serbia'>Republic of Serbia</option>"+
"<option value='Reunion'>Reunion</option>"+
"<option value='Romania'>Romania</option>"+
"<option value='Russia'>Russia</option>"+
"<option value='Rwanda'>Rwanda</option>"+
"<option value='St Barthelemy'>St Barthelemy</option>"+
"<option value='St Eustatius'>St Eustatius</option>"+
"<option value='St Helena'>St Helena</option>"+
"<option value='St Kitts-Nevis'>St Kitts-Nevis</option>"+
"<option value='St Lucia'>St Lucia</option>"+
"<option value='St Maarten'>St Maarten</option>"+
"<option value='St Pierre &amp; Miquelon'>St Pierre &amp; Miquelon</option>"+
"<option value='St Vincent &amp; Grenadines'>St Vincent &amp; Grenadines</option>"+
"<option value='Saipan'>Saipan</option>"+
"<option value='Samoa'>Samoa</option>"+
"<option value='Samoa American'>Samoa American</option>"+
"<option value='San Marino'>San Marino</option>"+
"<option value='Sao Tome &amp; Principe'>Sao Tome &amp; Principe</option>"+
"<option value='Saudi Arabia'>Saudi Arabia</option>"+
"<option value='Senegal'>Senegal</option>"+
"<option value='Serbia'>Serbia</option>"+
"<option value='Seychelles'>Seychelles</option>"+
"<option value='Sierra Leone'>Sierra Leone</option>"+
"<option value='Singapore'>Singapore</option>"+
"<option value='Slovakia'>Slovakia</option>"+
"<option value='Slovenia'>Slovenia</option>"+
"<option value='Solomon Islands'>Solomon Islands</option>"+
"<option value='Somalia'>Somalia</option>"+
"<option value='South Africa'>South Africa</option>"+
"<option value='Spain'>Spain</option>"+
"<option value='Sri Lanka'>Sri Lanka</option>"+
"<option value='Sudan'>Sudan</option>"+
"<option value='Suriname'>Suriname</option>"+
"<option value='Swaziland'>Swaziland</option>"+
"<option value='Sweden'>Sweden</option>"+
"<option value='Switzerland'>Switzerland</option>"+
"<option value='Syria'>Syria</option>"+
"<option value='Tahiti'>Tahiti</option>"+
"<option value='Taiwan'>Taiwan</option>"+
"<option value='Tajikistan'>Tajikistan</option>"+
"<option value='Tanzania'>Tanzania</option>"+
"<option value='Thailand'>Thailand</option>"+
"<option value='Togo'>Togo</option>"+
"<option value='Tokelau'>Tokelau</option>"+
"<option value='Tonga'>Tonga</option>"+
"<option value='Trinidad &amp; Tobago'>Trinidad &amp; Tobago</option>"+
"<option value='Tunisia'>Tunisia</option>"+
"<option value='Turkey'>Turkey</option>"+
"<option value='Turkmenistan'>Turkmenistan</option>"+
"<option value='Turks &amp; Caicos Is'>Turks &amp; Caicos Is</option>"+
"<option value='Tuvalu'>Tuvalu</option>"+
"<option value='Uganda'>Uganda</option>"+
"<option value='Ukraine'>Ukraine</option>"+
"<option value='United Arab Erimates'>United Arab Emirates</option>"+
"<option value='United Kingdom'>United Kingdom</option>"+
"<option value='United States of America'>United States of America</option>"+
"<option value='Uraguay'>Uruguay</option>"+
"<option value='Uzbekistan'>Uzbekistan</option>"+
"<option value='Vanuatu'>Vanuatu</option>"+
"<option value='Vatican City State'>Vatican City State</option>"+
"<option value='Venezuela'>Venezuela</option>"+
"<option value='Vietnam'>Vietnam</option>"+
"<option value='Virgin Islands (Brit)'>Virgin Islands (Brit)</option>"+
"<option value='Virgin Islands (USA)'>Virgin Islands (USA)</option>"+
"<option value='Wake Island'>Wake Island</option>"+
"<option value='Wallis &amp; Futana Is'>Wallis &amp; Futana Is</option>"+
"<option value='Yemen'>Yemen</option>"+
"<option value='Zaire'>Zaire</option>"+
"<option value='Zambia'>Zambia</option>"+
"<option value='Zimbabwe'>Zimbabwe</option>"+
"</select>"+ep);
}