function showColorValue() {
    var clr = document.getElementById("clr");
    hexClr.innerHTML = clr.value;
    var R = parseInt((clr.value.substring(1)).substring(0,2),16);
    var G = parseInt((clr.value.substring(1)).substring(2,4),16);
    var B = parseInt((clr.value.substring(1)).substring(4,6),16);
    var rgb = R + "," + G + "," + B;
    rgbClr.innerHTML = rgb;
}
