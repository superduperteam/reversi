$(function() {
    $("#createRoomButton").click(function() {
        $.ajax({
            data: {"roomName":$("#roomName").val(), "variant":$("#variant").val(), "boardRows":$("#boardRows").val(),
                "boardCols":$("#boardCols").val(), "target":$("#target").val(), "totalPlayers":$("#totalPlayers").val()},
            type: "POST",
            url: "../createRoom",
            timeout: 2000,
            complete: function (jqXHR, textStatus) {
                if (jqXHR.status === 201) {
                    console.log("Got ajax response - the room " + $("#roomsName").html() + " was created successfully");
                    $("#modalFooter").after("<div id=\"divMessage\" class=\"alert alert-success alert-dismissible fade show\" role=\"alert\">\n" +
                        "        <h3 id=\"textMessage\"></h3>\n" +
                        "        <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\" style=\"margin-top: 8px;\">\n" +
                        "            <span aria-hidden=\"true\">&times;</span>\n" +
                        "        </button>\n" +
                        "    </div>");
                    $("#textMessage").html("Room created!");
                    setTimeout("clearModal()", 800);
                } else {
                    console.error("Failed to get ajax response");
                }
            }
        });
    });
});


function clearModal() {
    $("#xmlFileName").val("");
    $("#roomName").val("");
    $("#variant").val("");
    $("#boardRows").val("");
    $("#boardCols").val("");
    $("#target").val("");
    $("#totalPlayers").val("");

    $("#divMessage").remove();

    $("#newRoomModal").modal("toggle");
    $("#createRoomButton").attr("disabled", "");
}