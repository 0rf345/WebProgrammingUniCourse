
function toDecimal(number) {
    "use strict";
    return number[0].numerator + number[1].numerator /
            (60 * number[1].denominator) + number[2].numerator / (3600 * number[2].denominator);
}

function initMap(latitude, longtitude) {
    "use strict";
    var uluru = {lat: latitude, lng: longtitude};
    var map = new google.maps.Map(document.getElementById("map"), {
        zoom: 4,
        center: uluru
    });
    var marker = new google.maps.Marker({
        position: uluru,
        map: map
    });
}

var TIV3285 = (function () {
    "use strict";
    var files = {
        loadedImages: {},
        loadedUrls: {},
        fullscreen: 0
    };

    function render(file, elemID, index) {
        if (!file.name.match(/\.(jpg|jpeg|png|gif)$/)) {
            alert(file.name + " not an image");
        } else {
            var reader = new FileReader();
            var span;
            var temp;
            reader.onload = (function (file) {
                return function (e) {
                    // Render thumbnail.
                    span = document.createElement("span");
                    temp = ["<div class=\"tile\" id=\"image", index, "\"", // create handle for clicking
                            " onclick=\"TIV3285.expand(", index, ")\"",    // onclick event handler
                            ">", "<img id=\"image0", index, "\"", "src=\"", e.target.result, // handle for getting url NOT USED
                            "\" title=\"", encodeURI(file.name), "\">", "</div>"];
                    files.loadedUrls[index] = e.target.result;
                    span.innerHTML = temp.join("");
                    document.getElementById(elemID).insertBefore(span, null);
                };
            }(file));
            // Read in the image file as a data URL.
            reader.readAsDataURL(file);
        }
    }

    function showExif(index, elemID) {
        if (files.loadedImages.length === undefined) {
            // Unpopulated image array
            var ref2 = document.getElementById(elemID);
            var res = ref2.innerHTML;
            res = "<p>No Images have been loaded.</p>";
            res = res + "<p>Press \"Choose Files\" to add some files first.</p>";
            ref2.innerHTML = res;
            ref2.style.fontFamily = "Impact";
            ref2.style.fontSize = "xx-large";
            ref2.style.color = "red";
            ref2.style.textAlign = "center";
        } else {
            if (index < files.loadedImages.length) {
                // Everything is ok
                document.getElementById(elemID).innerHTML = "";
                EXIF.getData(files.loadedImages[index], function () {
                    var allMetaData = EXIF.getAllTags(this);
                    var allMetaDataSpan = document.getElementById(elemID);
                    allMetaDataSpan.innerHTML = JSON.stringify(allMetaData, null, "\t");
                    allMetaDataSpan.style.fontFamily = "\"Times New Roman\", Georgia, Serif;";
                    allMetaDataSpan.style.color = "black";
                    allMetaDataSpan.style.fontSize = "small";
                });
            } else {
                // Index out of bounds
                var ref3 = document.getElementById(elemID);
                ref3.innerHTML = "<p>Index was not in loadedImages.</p>";
                ref3.style.fontFamily = "Impact";
                ref3.style.fontSize = "xx-large";
                ref3.style.color = "red";
                ref3.style.textAlign = "center";
            }
        }
    }
    
    // Assumes index and elemID to be valid
    function showMap (index, elemID) {
        var lat = 0;
        var lon = 0;
        EXIF.getData(files.loadedImages[index], function () {
            var allMetaData = EXIF.getAllTags(this);
            var allMetaDataSpan = document.getElementById(elemID);
            if(EXIF.getTag(this, "GPSLatitude")) {
                lat = toDecimal(EXIF.getTag(this, "GPSLatitude"));
            }
            if(EXIF.getTag(this, "GPSLongitude")) {
                lon = toDecimal(EXIF.getTag(this, "GPSLongitude"));
            }
            if (lon !== 0 && lat !== 0) {
                allMetaDataSpan.innerHTML += "<div id=\"map\"></div>";
                initMap(lat, lon);
            }
        });
    }

    return {
        loadImages: function () {
            files.loadedImages = document.getElementById("images").files;
        },
        showImages: function (elemID) {
            if (files.loadedImages.length === undefined) {
                var ref = document.getElementById(elemID);
                var res = ref.innerHTML;
                res = "<p>No Images have been loaded.</p>";
                res = res + "<p>Press \"Choose Files\" to add some files first.</p>";
                ref.innerHTML = res;
                ref.style.fontFamily = "Impact";
                ref.style.fontSize = "xx-large";
                ref.style.color = "red";
                ref.style.textAlign = "center";
            } else {
                document.getElementById(elemID).innerHTML = "";
                var i;
                for (i = 0; i < files.loadedImages.length; i = i + 1) {
                    render(files.loadedImages[i], elemID, i);
                }
            }
        },
        showImage: function (index, elemID) {
            if (files.loadedImages.length === undefined) {
                // Unpopulated image array
                var ref2 = document.getElementById(elemID);
                var res = ref2.innerHTML;
                res = "<p>No Images have been loaded.</p>";
                res = res + "<p>Press \"Choose Files\" to add some files first.</p>";
                ref2.innerHTML = res;
                ref2.style.fontFamily = "Impact";
                ref2.style.fontSize = "xx-large";
                ref2.style.color = "red";
                ref2.style.textAlign = "center";
            } else {
                if (index < files.loadedImages.length) {
                    // Everything is ok
                    document.getElementById(elemID).innerHTML = "";
                    render(files.loadedImages[index], elemID, index);
                } else {
                    // Index out of bounds
                    var ref3 = document.getElementById(elemID);
                    ref3.innerHTML = "<p>Index was not in loadedImages.</p>";
                    ref3.style.fontFamily = "Impact";
                    ref3.style.fontSize = "xx-large";
                    ref3.style.color = "red";
                    ref3.style.textAlign = "center";
                }
            }
        },
        showImageDetailedExifInfo: function (index, elemID) {
            showExif(index, elemID);
        },
        showImageDetailedExifWithMap: function (index, elemID) {
            if (index < files.loadedImages.length) {
                showExif(index, elemID);
                showMap(index, elemID);
            }
        },
        expand: function (which) {
            var index = which;
            if (files.fullscreen === 0) {
                document.getElementById("image" + index).style.maxWidth = "90%";
                document.getElementById("image" + index).style.left = "5%";
                document.getElementById("image" + index).className = "";

                showExif(index, "info");
                showMap(index, "info");
    
                var i;
                for(i = 0; i < files.loadedImages.length; i += 1) {
                    if(i != index) {
                        document.getElementById("image" + i).style.display = "none";
                    }
                }
                document.getElementById("urlSpace").innerHTML = 
                    "<div id=\"urlHolder\">" + "URL: " + files.loadedUrls[index] + "</div>";
                files.fullscreen = 1;
            } else {
                for(i = 0; i < files.loadedImages.length; i += 1) {
                    if(i != index) {
                        document.getElementById("image" + i).style.display = "inline-block";
                    }
                }
                document.getElementById("urlHolder").innerHTML = "";
                document.getElementById("image" + index).style.maxWidth = "";
                document.getElementById("image" + index).style.width = "";
                document.getElementById("image" + index).className = "tile";
                document.getElementById("info").innerHTML = "";
                files.fullscreen = 0;
            }
        },
        getLoadedImages: function () {
            return files.loadedImages;
        }
    };
}());
