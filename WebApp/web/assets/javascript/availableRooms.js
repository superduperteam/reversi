var playerName;

function getRooms() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../availableRooms",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function (json) {
            if(json.rooms.length === 0) {
                console.log("Got ajax response - there are no available rooms");

                if(document.getElementById("noAvailableRooms") == null) {
                    $("#availableRooms").append("<h2 id=\"noAvailableRooms\">There are no rooms available</h2>");
                }
            }
            else {
                console.log("Got ajax response - available rooms: " + json.rooms);

                if(document.getElementById("noAvailableRooms") != null) {
                    $("#noAvailableRooms").remove();
                }

                for(var i = 0; i < json.rooms.length; i++) {
                    var roomID = json.rooms[i].roomName.replace(/ /g, "_");

                    if(document.getElementById(roomID) == null) {
                        $("#availableRooms").append("<div id=\"" + roomID + "\" class=\"list-group\">\n" +
                            "                           <button id=\"" + roomID + "CollapseButton" + "\" class=\"btn btn-primary mb-1 collapsed\" type=\"button\" data-toggle=\"collapse\" data-target=\"#" + roomID + "Collapse\" aria-expanded=\"false\" aria-controls=\"" + roomID + "Collapse\">\n" +
                            "                               <i class=\"fas fa-gamepad mr-2\"></i>" + json.rooms[i].roomName + "\n" +
                            "                               <span id=\"" + roomID + "JoinedPlayers" + "\">" + json.rooms[i].joinedPlayersNum + "</span>" + "/" + json.rooms[i].totalPlayers + "\n" +
                            "                           </button>\n" +
                            "                           <div class=\"collapse\" id=\"" + roomID + "Collapse\" style=\"\">\n" +
                            "                               <div class=\"card card-body\">\n" +
                            "                                   <h5 class=\"card-title\">Uploader: " + json.rooms[i].uploaderName + "</h5>\n" +
                            "                                   <p class=\"card-text\">" +
                            "                                       <b class=\"mr-2\">Variant:</b>" + json.rooms[i].variant +
                            "                                       <b class=\"mr-2 ml-4\">Board:</b>" + json.rooms[i].boardRows + " X " + json.rooms[i].boardCols +
                            "                                   </p>\n" +
                            "                                   <hr>\n" +
                            "                                   <button id=\"" + roomID + "JoinButton" + "\" type=\"button\" class=\"btn btn-primary\">Join Room</button>\n" +
                            "                               </div>\n" +
                            "                           </div>\n" +
                            "                        </div>\n");
                    }
                    else {
                        if($("#" + roomID + "JoinedPlayers").html() === json.rooms[i].totalPlayers || json.rooms[i].isGameActive === true) {
                            $("#" + roomID + "CollapseButton").removeClass("btn-primary");
                            $("#" + roomID + "CollapseButton").addClass("btn-danger");
                            $("#" + roomID + "JoinButton").attr("disabled", "");
                        }
                        else if($("#" + roomID + "JoinedPlayers").html() !== json.rooms[i].joinedPlayersNum) {
                            if ($("#" + roomID + "JoinButton").attr("disabled")) {
                                $("#" + roomID + "JoinButton").removeAttr("disabled");
                                $("#" + roomID + "CollapseButton").removeClass("btn-danger");
                                $("#" + roomID + "CollapseButton").addClass("btn-primary");
                            }

                            $("#" + roomID + "JoinedPlayers").html(json.rooms[i].joinedPlayersNum);
                        }
                    }
                }
            }
        }
    });
}

function getPlayerName() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../lobby",
        timeout: 2000,
        error: function() {
            console.error("Failed to get ajax response");
        },
        success: function(json) {
            console.log("Got ajax response - this player name: " + json);

            playerName = json;
            $("#navBarPlayerName").html(json);
        }
    });
};

$(function() {
    $(document).on("click", "button", function() {
        if(this.id.includes("JoinButton")) {
            var roomNameID = this.id.replace("JoinButton", "");
            var roomName = roomNameID.replace(/_/g, " ");

            $.ajax({
                data: {"roomName":roomName},
                type: "POST",
                url: "../joinRoom",
                timeout: 2000,
                error: function () {
                    console.error("Failed to get ajax response");
                },
                success: function () {
                    console.log(playerName + " joined the room successfully");

                    var joindPlayersNum = $("#" + roomNameID + "JoinedPlayers").val();

                    $("#" + roomNameID + "JoinedPlayers").val(++joindPlayersNum);
                    window.location="../pages/preGameStart.html";
                }
            });
        }
    });
});

window.onload = function() {
    getPlayerName();
    getRooms();
    setInterval(getRooms, 500);
};