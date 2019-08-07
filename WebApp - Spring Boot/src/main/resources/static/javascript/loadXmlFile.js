$(function() {
    $("#xmlFile").change(function() {
        var xmlFileName = $("#xmlFile").val().replace(/^.*[\\\/]/, '');

        $("#xmlFileName").val(xmlFileName);

        var xmlFile = this.files[0];
        var formData = new FormData();

        formData.append("xmlFile", xmlFile);

        $.ajax({
            data: formData,
            type: "POST",
            url: "../xmlFileLoader",
            processData: false,
            contentType: false,
            timeout: 4000,
            error: function() {
                console.error("Failed to get ajax response");
            },
            success: function(json) {
                if($("#divMessage") != null) {
                    $("#divMessage").remove();
                }

                if(json.hasOwnProperty("isActionSucceeded")) {
                    console.log("Got ajax response - the xml file is invalid: " + json.message);

                    $("#loadXmlFileDiv").after("<div id=\"divMessage\" class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\">\n" +
                        "        <h3 id=\"textMessage\"></h3>\n" +
                        "        <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\" style=\"margin-top: 8px;\">\n" +
                        "            <span aria-hidden=\"true\">&times;</span>\n" +
                        "        </button>\n" +
                        "    </div>");

                    $("#textMessage").html(json.message);
                    $("#createRoomButton").attr("disabled", "");

                    $("#roomName").val("");
                    $("#variant").val("");
                    $("#boardRows").val("");
                    $("#boardCols").val("");
                    $("#target").val("");
                    $("#totalPlayers").val("");
                }
                else {
                    console.log("Got ajax response - the xml file is valid");

                    $("#roomName").val(json.roomName);
                    $("#variant").val(json.variant);
                    $("#boardRows").val(json.boardRows);
                    $("#boardCols").val(json.boardCols);
                    $("#target").val(json.target);
                    $("#totalPlayers").val(json.totalPlayers);

                    $("#createRoomButton").removeAttr("disabled");
                }

                $("#xmlFile").val("");
            }
        });
    });
});