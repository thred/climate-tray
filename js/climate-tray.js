$.ajax({
    url: "VERSION",
    success: function (data) {
        var version = data.match(/build\.version\s*=\s*([^\n]*)/)[1];

        $("#download-button").attr("href", "climate-tray-" + version + ".zip");
        $("#version").html(version);
    }
});