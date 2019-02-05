$(function() {
    $("#logoutButton").click(function () {
        $.ajax({
            data: "",
            type: "GET",
            url: "../logout",
            timeout: 2000,
            error: function () {
                console.error("Failed to get ajax response");
            },
            success: function () {
                console.log($("#navBarPlayerName").html() + " logged out successfully");

                window.location="../pages/signup.html";
            }
        });
    });
});