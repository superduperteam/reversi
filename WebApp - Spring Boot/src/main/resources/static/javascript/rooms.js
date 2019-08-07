var playerName;

function getRooms() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../rooms",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function (json) {
            if(json.rooms.length === 0) {
                console.log("Got ajax response - there are no available rooms");

                if(document.getElementById("norooms") == null) {
                    $("#rooms").append("<h2 id=\"norooms\">There are no rooms available</h2>");
                }
            }
            else {
                console.log("Got ajax response - available rooms: " + json.rooms);

                if(document.getElementById("norooms") != null) {
                    $("#norooms").remove();
                }
                for(var i = 0; i < json.rooms.length; i++) {
                    var roomID = json.rooms[i].roomName.replace(/ /g, "_");

                    if(document.getElementById(roomID) == null) {
                        $("#rooms").append("<div id=\"" + roomID + "\" class=\"list-group\">\n" +
                            "                           <button id=\"" + roomID + "CollapseButton" + "\" class=\"btn btn-primary mb-1 collapsed\" style=\"background-color:green; border-radius: 32px\" type=\"button\" data-toggle=\"collapse\" data-target=\"#" + roomID + "Collapse\" aria-expanded=\"false\" aria-controls=\"" + roomID + "Collapse\">\n" +
                            "                               <i></i>" + json.rooms[i].roomName + "\n" +
                            "                               <span id=\"" + roomID + "JoinedPlayers" + "\">" + json.rooms[i].joinedPlayersNum + "</span>" + "/" + json.rooms[i].totalPlayers + "\n" +
                            "                           </button>\n" +
                            "                           <div class=\"collapse popup\" onclick='showBoardOnPopup()'  id=\"" + roomID + "Collapse\" style=\"\">\n" +
                                                            "<div class=\"collapse popup\" style=\"display: flex; justify-content: center\" id=\""+ i +"popupboard" + "\">  </div>\n" +
                            "                               <div class=\"card card-body\">\n" +
                            "                                   <h5 class=\"card-title\">Uploader: " + json.rooms[i].uploaderName + "</h5>\n" +
                            "                                   <p class=\"card-text\">" +
                            "                                       <b class=\"mr-2\">Variant:</b>" + json.rooms[i].variant +
                            "                                       <b class=\"mr-2 ml-4\">Board:</b>" + json.rooms[i].boardRows + " X " + json.rooms[i].boardCols +
                            "                                   </p>\n" +
                            "                                   <hr>\n" +
                            "                                   <button id=\"" + roomID + "JoinButton" + "\" type=\"button\" class=\"btn btn-primary\" style=\"background-color:green; border-radius: 32px\">Join Room</button>\n" +
                            "                               </div>\n" +
                            "                           </div>\n" +
                            "                        </div>\n");
                        showBoardOnPopup(json.rooms[i].gameManager, i);
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

function showBoardOnPopup(json, num) {
    console.log('in my func');


    var originalCellSize = 30;


    for (var colIndex = 0; colIndex < json.board.width; colIndex++) {
        var colID = "boardCol-" + colIndex;
        var uID = num + "popupboard";

        $("#" + uID).append("<div id=\"" + num + colID + "\"></div>");
        console.log(json);
        for (var rowIndex = 0; rowIndex < json.board.height; rowIndex++) {
            var rowID = "boardRow-" + rowIndex;
            var fill1;

            if (json.board.gameboard[rowIndex][colIndex].disc !== undefined) {
                fill1 = json.board.gameboard[rowIndex][colIndex].disc.type;
            }
            else {
                fill1 = 'lightgreen';
            }

            var id1 = rowID + "," + colID;
            var searchCOLID = "" + num + colID;

            $("#" + searchCOLID).append("<div id=\"" + id1 + "\"> \n" +
                "                       <svg height=\"30\" width=\"30\">\n" +
                "                           <rect width=\"30\" height=\"30\" style=\"fill: lightgreen;stroke:black;stroke-width:0.5\"></rect>\n" +
                "                           <circle class=\"popupboard\" id=\"Circle" + id1 + "\" cx=\"15\" cy=\"15\" r=\"11\" stroke=\"lightgreen\" stroke-width=\"1\" fill=\"" + fill1 + "\" />\n" +
                "                       </svg>\n" +
                "                  </div>\n");
        }
    }

    var popup = document.getElementById(num + "popupboard");
    popup.classList.toggle("show");
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