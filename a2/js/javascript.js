
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
        getLoadedImages: function () {
            return files.loadedImages;
        }
    };
}());


