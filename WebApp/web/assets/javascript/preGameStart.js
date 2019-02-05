var repeater;

window.onload = function() {
    checkIfGameCanBeActive();
    repeater = setInterval(checkIfGameCanBeActive, 500);
};

function checkIfGameCanBeActive() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../roomJoinedPlayers",
        timeout: 2000,
        error: function() {
            console.error("Failed to get ajax response");
        },
        success: function(json) {
            console.log("Got ajax response - is the game can be started: " + json.isTotalPlayersJoinedTheRoom);

            if(json.isTotalPlayersJoinedTheRoom) {
                clearInterval(repeater);

                if($("#roomJoinedPlayersModal").hasClass("show")) {
                    $("#modalMessage").html("Waiting for all players to join the room - " + json.joinedPlayersNum + "/" + json.totalPlayersNum);
                    $("#leaveRoomButton").attr("disabled", "");

                    setTimeout("window.location='../pages/gameStart.html'", 500);
                }
                else {
                    window.location="../pages/gameStart.html";
                }
            }
            else {
                if($("#roomJoinedPlayersModal").hasClass("show")) {
                    $("#modalMessage").html("Waiting for all players to join the room - " + json.joinedPlayersNum + "/" + json.totalPlayersNum);
                }
                else {
                    $("#roomJoinedPlayersModal").modal({show: true, backdrop: "static", keyBoard: false});
                    $("#modalMessage").html("Waiting for all players to join the room - " + json.joinedPlayersNum + "/" + json.totalPlayersNum);
                }
            }
        }
    });
}

$(function() {
    $("#leaveRoomButton").click(function() {
        $.ajax({
            data: "",
            type: "GET",
            url: "../leaveRoom",
            timeout: 2000,
            error: function () {
                console.error("Failed to get ajax response");
            },
            success: function () {
                console.log("Player left the room successfully");

                window.location="../pages/availableRooms.html";
            }
        });
    });
});