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
                    console.log("Joined the room successfully");

                    var joindPlayersNum = $("#" + roomNameID + "JoinedPlayers").val();

                    $("#" + roomNameID + "JoinedPlayers").val(++joindPlayersNum);
                    window.location="../../pages/preGameStart.html";
                }
            });
        }
    });
});