var repeater;

window.onload = function() {
    repeater = setInterval(checkIfGameCanBeActive, 500);
};

function getCurrentRoomNumber(){
    var url = document.URL;
    return url.substring( url.lastIndexOf("/")+1, url.length);
}

function checkIfGameCanBeActive() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../rooms/" + getCurrentRoomNumber() + "/status",
        timeout: 2000,
        error: function() {
            console.error("Failed to get ajax response");
        },
        success: function(json) {
            var isTotalPlayersJoinedTheRoom = json.joinedPlayersNum === json.totalPlayersNum;
            console.log("Got ajax response - is the game can be started: " + isTotalPlayersJoinedTheRoom);
            var roomID = getCurrentRoomNumber();
            var dest = "../rooms/"+roomID+"/game";

            if(isTotalPlayersJoinedTheRoom) {
                clearInterval(repeater);

                if($("#roomJoinedPlayersModal").hasClass("show")) {
                    $("#modalMessage").html("Waiting for all players to join the room - " + json.joinedPlayersNum + "/" + json.totalPlayersNum);
                    $("#leaveRoomButton").attr("disabled", "");

                }

                window.location=dest;
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
            type: "DELETE",
            url: "../rooms/" + getCurrentRoomNumber(),
            timeout: 2000,
            error: function () {
                console.error("Failed to get ajax response");
            },
            success: function () {
                console.log("Player left the room successfully");

                window.location="../pages/rooms.html";
            }
        });
    });
});