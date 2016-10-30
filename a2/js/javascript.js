
var TIV3285 = (function () {
    "use strict";
    var files = {
        loadedImages: {}
    };
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
            }
            var file;
            var reader;
            var span;
            var x;
            var temp;
            for (x in files.loadedImages) {
                file = files.loadedImages[x];
                if (!file.name.match(/\.(jpg|jpeg|png|gif)$/)) {
                    alert(file.name + " not an image");
                } else {
                    reader = new FileReader();
                    // Closure to capture the file information.
                    reader.onload = (function (file) {
                        return function (e) {
                            // Render thumbnail.
                            span = document.createElement("span");
                            temp = ["<img src=\"", e.target.result,
                                    "\" title=\"", escape(file.name), "\">"];
                            span.innerHTML = temp.join("");
                            document.getElementById("list").insertBefore(span, null);
                        };
                    }(file));
                    // Read in the image file as a data URL.
                    reader.readAsDataURL(file);
                }
            }
        },
        getLoadedImages: function () {
            return files.loadedImages;
        }
    };
}());


