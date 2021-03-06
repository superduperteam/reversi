var playerName;

function getOnlinePlayers() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../players",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function (json) {
            console.log("Got ajax response - online players: " + json.onlinePlayers);

            $("#onlinePlayers").empty();

            for(var i = 0; i < json.onlinePlayers.length; i++) {
                if(json.onlinePlayers[i].name === playerName) {
                    $("#onlinePlayers").append("<li class=\"list-group-item mb-2\" style=\"background-color: dodgerblue; border-radius: 32px; color: white;\">\n" +
                        "                           <i class=\"fas fa-user mr-2\" style=\"background-color: dodgerblue; border-radius: 32px; color: white;\"></i>" + json.onlinePlayers[i].name + "\n" +
                        "                       </li>");
                }
                else {
                    $("#onlinePlayers").append("<li class=\"list-group-item mb-2\" style=\"background-color: dodgerblue; border-radius: 32px; color: white;\">\n" +
                        "                           <i class=\"fas fa-user mr-2\" style=\"background-color: dodgerblue; border-radius: 32px; color: white;\"></i>" + json.onlinePlayers[i].name + "\n" +
                        "                       </li>");
                }
            }
        }
    });
}

function getPlayerName() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../players/playerName",
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

window.onload = function() {
    getPlayerName();
    getOnlinePlayers();
    setInterval(getOnlinePlayers, 1000);
};