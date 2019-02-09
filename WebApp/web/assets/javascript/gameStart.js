var boardCols;
var passivePlayerRepeater;
var synchronizeEndTurnRepeater;

var playerName;
var isPlayerComputer;
var playerDiscColor;

var lastTurnPlayerName;
var lastTurnPlayerDiscColor;
var lastTurnPlayerTurnsPlayed;
var isLastMoveExecuted;

(function($) {
    $.fn.hasHorizontalScrollBar = function() {
        return this.get(0) ? this.get(0).scrollWidth > this.innerWidth() : false;
    }
})(jQuery);

window.onload = function() {
    initializeGame();
};

function initializeGame() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../initializeGame",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function (json) {
            console.log("Got ajax response - initializing the game");
            console.log(json);
            if(json.variant === "Regular") {
                $("#popoutVariant").css("display", "inline");
            }

            for(var i = 0; i < json.playersList.length; i++) {
                var currentPlayerName = json.playersList[i].name;
                var isHuman = json.playersList[i].isHuman ? "Human" : "Computer";
                var currentPlayerDiscType = json.playersList[i].discType;

                $("#players").append("<div id=\"" + isHuman + "Card" + "\" class=\"card\" style=\"width: 18rem; float: none; margin-right: auto; margin-left: auto;\">\n" +
                    "                <div class=\"card-body\">\n" +
                    "                    <h5 class=\"card-title\">" + currentPlayerName + "</h5>\n" +
                    "                    <h6 class=\"card-subtitle mb-2 text-muted\">" + isHuman + "</h6>\n" +
                    "                    <br>\n" +
                    "                    <svg height=\"20\" width=\"80\">\n" +
                    "                        <rect width=\"80\", height=\"20\" style=\"fill:" + currentPlayerDiscType + "\"></rect>\n" +
                    "                    </svg>\n" +
                    "                    <br><br>\n" +
                    "                    <p class=\"card-text\"> Turns Played:\n" +
                    "                        <span id=\"" + currentPlayerName + "TurnsPlayed" + "\">0</span>\n" +
                    "                    </p>\n" +
                    "                </div>\n" +
                    "            </div>");
            }

            for(var i = 0; i < json.board.width; i++) {
                var colID = "boardCol-" + i;

                $("#board").append("<div id=\"" + colID + "\"></div>");

                for(var j = 0; j < json.board.height; j++) {
                    var rowID = "boardRow-" + j;

                    $("#" + colID).append("<div>\n" +
                        "                       <svg height=\"100\" width=\"100\">\n" +
                        "                           <rect width=\"100\", height=\"100\" style=\"fill: white;stroke:black;stroke-width:5\"></rect>\n" +
                        "                       </svg>\n" +
                        "                  </div>\n");
                }
            }

            if($("#boardJumbotron").hasHorizontalScrollBar()){
                $("#boardJumbotron").css("display", "");
            }

            $("#startGameModal").modal({show: true});

            boardCols = $("[id^=\"boardCol-\"]");

            getThisPlayer();
        }
    });
};

function getThisPlayer() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../thisPlayer",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function (json) {
            console.log("Got ajax response - this player is: " + json.name);

            playerName = json.name;
            //playerDiscColor = json.discType;

            if(json.isHuman === true) {
                isPlayerComputer = false;
            }
            else {
                isPlayerComputer = true;
                $("#quitButton").css("display", "none");
                $("#popoutVariant").css("display", "none");
            }

            getCurrentPlayerTurn();
        }
    });
}

function getCurrentPlayerTurn() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../currentPlayerTurn",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function (json) {
            console.log("Got ajax response - current player name is: " + json.name);

            isLastMoveExecuted = true;
            lastTurnPlayerName = json.name;
            //lastTurnPlayerDiscColor = json.discType;
            //lastTurnPlayerTurnsPlayed = json.turnsPlayedNum;

            if(playerName === json.name) {
                $("#turn").html(playerName + ", It's your turn");
                $("#quitButton").removeAttr("disabled");

                if(isPlayerComputer === false) {
                    setHighlightClickableBoardCols();
                }
                else {
                    computerMove();
                }
            }
            else {
                $("#turn").html("It's " + json.name + "'s turn. Stand by...");
                $("#quitButton").attr("disabled", "");

                for(var i = 0; i < boardCols.length; i++) {
                    boardCols.eq(i).removeClass("highlight");
                }

                passivePlayerRepeater = setInterval(updateBoard, 500);
            }
        }
    });
}

$(function() {
    $("#regularMove").click(function() {
        if(playerName === lastTurnPlayerName) {
            setHighlightClickableBoardCols();
        }
    });
});

$(function() {
    $("#popoutMove").click(function() {
        if(playerName === lastTurnPlayerName) {
            setHighlightClickableBoardCols();
        }
    });
});

function setHighlightClickableBoardCols() {
    var isPopoutMove = false;

    if($("#popoutVariant").css("display") === "inline") {
        if($("#popoutMove").is(":checked")) {
            isPopoutMove = true;
        }
    }

    $.ajax({
        data: {"isPopoutMove":isPopoutMove},
        type: "POST",
        url: "../clickableBoardCols",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function (json) {
            console.log("Got ajax response - clickable board cols: " + json.clickableBoardColsNum);

            for(var i = 0; i < boardCols.length; i++) {
                if(json.clickableBoardColsNum.includes(i)) {
                    boardCols.eq(i).addClass("highlight");
                }
                else {
                    boardCols.eq(i).removeClass("highlight");
                }
            }
        }
    });
}

$(function() {
    $(document).on("click", "[id^=boardCol-]", function() {
        var destinationCol = this.id.replace("boardCol-", "");

        if(this.classList.contains("highlight")) {
            if (isPlayerComputer === false && playerName === lastTurnPlayerName) {
                if (isLastMoveExecuted) {
                    isLastMoveExecuted = false;

                    var isPopoutMove = false;

                    if ($("#popoutVariant").css("display") === "inline") {
                        if ($("#popoutMove").is(":checked")) {
                            isPopoutMove = true;
                        }
                    }

                    $.ajax({
                        data: {"destinationCol": destinationCol, "isPopoutMove": isPopoutMove},
                        type: "POST",
                        url: "../executeMove",
                        timeout: 2000,
                        error: function () {
                            console.error("Failed to get ajax response");
                        },
                        success: function () {
                            console.log("Got ajax response - " + playerName + "'s move executed");

                            updateBoard();
                        }
                    });
                }
            }
        }
    });
});

$(function() {
    $("#quitButton").click(function() {
        $.ajax({
            data: "",
            type: "GET",
            url: "../playerQuit",
            timeout: 2000,
            error: function () {
                console.error("Failed to get ajax response");
            },
            success: function () {
                console.log("Got ajax response - " + playerName + " quit from the game");

                window.location = "../pages/availableRooms.html";
            }
        });
    });
});

function computerMove() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../executeMove",
        timeout: 4000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function () {
            console.log("Got ajax response - " + playerName + "'s move executed");

            updateBoard();
        }
    });
}

function updateBoard() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../boardUpdater",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function (json) {
            console.log("Got ajax response - last move changes: " + json.lastMoveChanges + " ; " + "is player quit: " + json.isCurrentPlayerQuit);

            if(passivePlayerRepeater != null) {
                if(json.lastMoveChanges.length > 0) {
                    clearInterval(passivePlayerRepeater);
                    passivePlayerRepeater = null;

                    if(json.isCurrentPlayerQuit === true) {
                        $("#" + lastTurnPlayerName + "Card").css("opacity", "0.3");
                    }
                    else {
                        $("#" + lastTurnPlayerName + "TurnsPlayed").html(++lastTurnPlayerTurnsPlayed);
                    }

                    for(var i = 0; i < json.lastMoveChanges.length; i++) {
                        document.getElementById("boardCol-" + json.lastMoveChanges[i].col).querySelector("#boardRow-" + json.lastMoveChanges[i].row).style.fill = json.lastMoveChanges[i].discType;
                    }

                    checkGameOver();
                }
            }
            else {
                for(var i = 0; i < json.lastMoveChanges.length; i++) {
                    document.getElementById("boardCol-" + json.lastMoveChanges[i].col).querySelector("#boardRow-" + json.lastMoveChanges[i].row).style.fill = json.lastMoveChanges[i].discType;
                }

                $("#" + lastTurnPlayerName + "TurnsPlayed").html(++lastTurnPlayerTurnsPlayed);

                checkGameOver();
            }
        }
    });
}

function checkGameOver() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../gameOver",
        timeout: 4000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function (json) {
            var isWinner = false;

            if(json.isGameOver === true) {
                console.log("Got ajax response - the game is over");

                $("#endGameModal").modal({show: true, backdrop: "static", keyBoard: false});

                if(json.endGameType === "VICTORY") {
                    for (var i = 0; i < json.winnersNames.length; i++) {
                        if (playerName === json.winnersNames[i]) {
                            isWinner = true;
                            break;
                        }
                    }

                    if(json.winnersNames.length === 1) {
                        $("#modalPublicMessage").html("The winner is " + json.winnersNames);
                    }
                    else {
                        $("#modalPublicMessage").html("The winners are " + json.winnersNames);
                    }

                    if(isWinner) {
                        $("#modalPrivateMessage").html("You Win! Congratulations!");
                    }
                    else {
                        $("#modalPrivateMessage").html("You Lose! Maybe next time...");
                    }
                }
                else if(json.endGameType === "TIE") {
                    $("#modalPublicMessage").html("It's a TIE");
                }
                else {
                    $("#modalPublicMessage").html("The technically winner is " + json.winnersNames);
                    $("#modalPrivateMessage").html("You the only player left in the room");
                }

                endGameLeaveRoom();
            }
            else {
                console.log("Got ajax response - the game continues");
                changeTurn();
            }
        }
    });
}

function endGameLeaveRoom() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../endGameLeave",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function () {
            console.log(playerName + " left the room - END GAME");

            setTimeout("window.location='../pages/availableRooms.html'", 2000);
        }
    });
}

function changeTurn() {
    $.ajax({
        data: {"lastTurnPlayerName":lastTurnPlayerName},
        type: "POST",
        url: "../changeTurn",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function () {
            console.log("Turn changed successfully");

            synchronizeEndTurnRepeater = setInterval(synchronizeEndTurn, 200);
        }
    });
}

function synchronizeEndTurn() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../synchronizeEndTurn",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function (json) {
            console.log("Got ajax response - are all players synchronized after complete turn of the game: " + json.isActionSucceeded);

            if(json.isActionSucceeded === true) {
                clearInterval(synchronizeEndTurnRepeater);
                synchronizeEndTurnRepeater = null;
                getCurrentPlayerTurn();
            }
        }
    });
}