
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
        loadedImages: {}
    };

    function render(file, elemID) {
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
                    temp = ["<div class=\"tile\">", "<img src=\"", e.target.result,
                            "\" title=\"", encodeURI(file.name), "\">", "</div>"];
                    span.innerHTML = temp.join("");
                    document.getElementById(elemID).insertBefore(span, null);
                };
            }(file));
            // Read in the image file as a data URL.
            reader.readAsDataURL(file);
        }
    }
    
    function showExif(index, elemID) {
        document.getElementById(elemID).innerHTML = "";
            EXIF.getData(files.loadedImages[index], function () {
                var allMetaData = EXIF.getAllTags(this);
                var allMetaDataSpan = document.getElementById(elemID);
                allMetaDataSpan.innerHTML = JSON.stringify(allMetaData, null, "\t");
                allMetaDataSpan.style.fontFamily = "\"Times New Roman\", Georgia, Serif;";
                allMetaDataSpan.style.color = "black";
                allMetaDataSpan.style.fontSize = "small";
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
                    render(files.loadedImages[i], elemID);
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
                if (index < files.loadedImages.length - 1) {
                    // Everything is ok
                    document.getElementById(elemID).innerHTML = "";
                    render(files.loadedImages[index], elemID);
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
                if (index < files.loadedImages.length - 1) {
                    // Everything is ok
                    showExif(index, elemID);
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
        showImageDetailedExifWithMap: function (index, elemID) {
            if (files.loadedImages.length === undefined) {
                alert("No image loaded yet");
            } else if (index < files.loadedImages.length - 1) {
                showExif(index, elemID);
                var lat = 0;
                var lon = 0;
                EXIF.getData(files.loadedImages[index], function () {
                    var allMetaData = EXIF.getAllTags(this);
                    var allMetaDataSpan = document.getElementById(elemID);
                    lat = toDecimal(EXIF.getTag(this, "GPSLatitude"));
                    lon = toDecimal(EXIF.getTag(this, "GPSLongitude"));
                    if (lon !== undefined && lat !== undefined) {
                        allMetaDataSpan.innerHTML += "<div id=\"map\"></div>";
                        initMap(lat, lon);
                    }
                });
            } else {
                alert("Index out of bounds, please use a smaller index or load more photos.");
            }
        },
        getLoadedImages: function () {
            return files.loadedImages;
        }
    };
}());
