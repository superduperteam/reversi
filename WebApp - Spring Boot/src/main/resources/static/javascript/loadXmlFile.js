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
            complete: function (jqXHR, textStatus) {
                var json = JSON.parse(jqXHR.responseText);
                console.log(json);
                console.log(textStatus);

                switch (jqXHR.status) {
                    case 202:
                        console.log("Got ajax response - the xml file is valid");
                        $("#roomName").val(json.roomName);
                        $("#variant").val(json.gameManager.gameMode);
                        $("#boardRows").val(json.gameManager.board.height);
                        $("#boardCols").val(json.gameManager.board.width);
                        $("#target").val(json.target);
                        $("#totalPlayers").val(json.gameManager.totalNumOfPlayers);
                        $("#createRoomButton").removeAttr("disabled");

                        $("#xmlFile").val("");
                        break;
                    case 409:
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
                    default:
                        console.error("Failed to get ajax response");
                        $("#xmlFile").val("");
                        break;
                }
            }
        });
    });
});