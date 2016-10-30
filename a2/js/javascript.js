<script>

var TIV3285 = function() {
	var files = {
		loadedImages: {}
	};
	return {
		loadImages: function() {
			files.loadedImages = document.getElementById("images").files;
		},
		showloadedImages: function(elemID) {
			if(typeof files.loadedImages.length == "undefined") {
				var ref = document.getElementById(elemID);
				var res = ref.innerHTML;
				res = "<p>No Images have been loaded.</p>";
				res = res + "<p>Press 'Choose Files' to add some files first.</p>";
				ref.innerHTML = res;
				value = ref.value;
				ref.style.fontFamily = 'Impact';
				ref.style.fontSize = 'xx-large';
				ref.style.color = 'red';
				ref.style.textAlign = 'center';
			}else{
				document.getElementById(elemID).innerHTML = '';
			}
			for (var i = 0; i < files.loadedImages.length; i++) {
				 var file = files.loadedImages[i];
				 if (!file.name.match(/\.(jpg|jpeg|png|gif)$/))
					 alert(file.name + ' not an image');
				 else {
					 var reader = new FileReader();
					 // Closure to capture the file information.
					 reader.onload = (function(file) {
					 return function(e) {
					 // Render thumbnail.
					 var span = document.createElement('span');
					 span.innerHTML = ['<img src="', e.target.result,
					 '" title="', escape(file.name), '">'].join('');
					 document.getElementById('list').insertBefore(span, null);
					 };
					 })(file);
					 // Read in the image file as a data URL.
					 reader.readAsDataURL(file);
				 }
			}
		},
		getLoadedImages: function() {
			return files.loadedImages;
		}
	};
}();

</script>