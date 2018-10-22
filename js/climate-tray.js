var platform = navigator.platform.toLowerCase();
var extension;

if (platform.indexOf("linux") >= 0) {
    extension = "-bin.tar.gz";
}
else {
    extension = "-bin.zip";
}

$.ajax({
    url: "VERSION",
    success: function (data) {
        var version = data.match(/build\.version\s*=\s*([^\n]*)/)[1];

        $("#download-button").attr("href", "climate-tray-" + version + extension);
        $("#version").html(version);
    }
});