$(function() {
    $("#buttonSignup").click(function () {
        $.ajax({
            data: {"playerName": $("#inputName").val(), "isComputer": $("#checkboxIsComputer").prop("checked")},
            type: "POST",
            url: "../players",
            timeout: 2000,
            complete: function (jqXHR, textStatus) {
                switch (jqXHR.status) {
                    case 201:
                        setTimeout("window.location='../pages/lobby.html'", 800);
                        break;
                    case 409:
                        var message = jqXHR.responseText;
                        $("#divMainForm").after("<div id=\"divMessage\" class=\"alert alert-dismissible fade show\" role=\"alert\" style=\"width: 500px; margin: 30px auto 0;\">\n" +
                            "        <h3 id=\"textMessage\"></h3>\n" +
                            "        <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\" style=\"margin-top: 8px;\">\n" +
                            "            <span aria-hidden=\"true\">&times;</span>\n" +
                            "        </button>\n" +
                            "    </div>");
                        console.log("Got ajax response - failed to sign up player by name: " + $("#inputName").val());
                        $("#divMessage").addClass("alert-danger");
                        $("#textMessage").html(message);
                        break;
                    default:
                        console.error("Failed to get ajax response");

                }
            }
        })
    });
})
