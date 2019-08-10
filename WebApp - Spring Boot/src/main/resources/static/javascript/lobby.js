window.onload = function() {
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

            $("#navBarPlayerName").html(json);
            $("#mainPlayerName").html(json);
        }
    });
};
