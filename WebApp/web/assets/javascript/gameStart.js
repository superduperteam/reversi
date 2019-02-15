var boardCols;
var passivePlayerRepeater;
var synchronizeEndTurnRepeater;
var gameoverRepeater;

var playerName;
var isPlayerComputer;
var playerDiscColor;
var isItMyTurn;
var lastTurnPlayerDiscColor;
var lastTurnPlayerTurnsPlayed;
var isLastMoveExecuted;
var currentPlayerDiscType;

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

            $("#gameModeType").text(json.gameMode);


            for(var i = 0; i < json.playersList.length; i++) {
                var currentPlayerName = json.playersList[i].name;
                var isHuman = json.playersList[i].isHuman ? "Human" : "Computer";
                currentPlayerDiscType = json.playersList[i].discType;
                var score = json.playersList[i].statistics.score;

                console.table(json.playersList);
                $("#players").append("<div id=\"" + isHuman + "Card" + "\" class=\"cardBody\" style=\"width: 18rem; float: none; margin-right: auto; margin-left: auto;\">\n" +
                    "                <div class=\"cardBody\">\n" +
                    "                    <h5 class=\"card-title\">" + currentPlayerName + "</h5>\n" +
                    "                    <h6 class=\"card-subtitle mb-2 text-muted\">" + isHuman + "</h6>\n" +
                    "                    <br>\n" +
                    "                    <svg height=\"20\" width=\"80\">\n" +
                    "                        <rect width=\"80\", height=\"20\" style=\"fill:" + currentPlayerDiscType + "\"></rect>\n" +
                    "                    </svg>\n" +
                    "                    <br><br>\n" +
                    "                    <p class=\"card-text\"> Score:\n" +
                    "                        <span id=\"" + currentPlayerName + "Score" + "\">" + score + "</span>\n" +
                    "                    </p>\n" +
                    "                    <p class=\"card-text\"> Average of flips:\n" +
                    "                        <span id=\"" + currentPlayerName + "AverageOfFlips" + "\">0</span>\n" +
                    "                    </p>\n" +
                    "                    <p class=\"card-text\"> Turns Played:\n" +
                    "                        <span id=\"" + currentPlayerName + "TurnsPlayed" + "\">0</span>\n" +
                    "                    </p>\n" +
                    "                </div>\n" +
                    "            </div>");
            }

            // for(var i = 0; i < json.board.height; i++) {
            //             //     var colID = "boardCol-" + i;
            //             //
            //             //     $("#board").append("<div id=\"" + colID + "\"></div>");
            //             //     console.log(json);
            //             //     for(var j = 0; j < json.board.width; j++) {
            //             //         var rowID = "boardRow-" + j;
            //             //         var fill1;
            //             //
            //             //         if(json.board.gameboard[i][j].disc !== undefined){
            //             //             fill1 = 'black';
            //             //         }
            //             //         else{
            //             //             fill1 = 'lightblue';
            //             //         }
            //             //
            //             //         $("#" + colID).append("<div>\n" +
            //             //             "                       <svg height=\"100\" width=\"100\">\n" +
            //             //             "                           <rect width=\"100\" height=\"100\" style=\"fill: lightblue;stroke:black;stroke-width:5\"></rect>\n" +
            //             //             "                           <circle cx=\"50\" cy=\"50\" r=\"40\" stroke=\"lightblue\" stroke-width=\"1\" fill=\"" + fill1+ "\" />\n" +
            //             //             "                       </svg>\n" +
            //             //             "                  </div>\n");
            //             //     }
            //             // }

            for(var colIndex = 0; colIndex < json.board.width; colIndex++) {
                var colID = "boardCol-" + colIndex;
                var numOfPossibleMoves = "";
                $("#board").append("<div id=\"" + colID + "\"></div>");
                console.log(json);
                for(var rowIndex = 0; rowIndex < json.board.height; rowIndex++) {
                    var rowID = "boardRow-" + rowIndex;
                    var fill1;

                    if(json.board.gameboard[rowIndex][colIndex].disc !== undefined){
                        fill1 = json.board.gameboard[rowIndex][colIndex].disc.type;
                    }
                    else{
                        fill1 = 'lightgreen';
                        numOfPossibleMoves = json.board.gameboard[rowIndex][colIndex].flipPotential;
                    }

                    var id1 = rowID + "," + colID;
                    $("#" + colID).append("<div id=\"" + id1 + "\"> \n" +
                        "                       <svg height=\"100\" width=\"100\">\n" +
                        "                           <rect width=\"100\" height=\"100\" style=\"fill: lightgreen;stroke:black;stroke-width:5\"></rect>\n" +
                        "                           <circle id=\"" +id1 +"Circle" +"\" cx=\"50\" cy=\"50\" r=\"40\" stroke=\"lightgreen\" stroke-width=\"1\" fill=\"" + fill1+ "\" />\n" +
                        "                           <text id=\"" +id1 +"text" +"\" x=\"50%\" y=\"50%\" stroke=\"#51c5cf\" stroke-width=\"2px\" dy=\".3em\">"+ numOfPossibleMoves + " </text>\n" +

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

           //isLastMoveExecuted = true;
            //lastTurnPlayerDiscColor = json.discType;
            //lastTurnPlayerTurnsPlayed = json.turnsPlayedNum;

            if(playerName === json.name) {
                isItMyTurn = true;
                passivePlayerRepeater = null;
                $("#turn").html(playerName + ", It's your turn");
                $("#quitButton").removeAttr("disabled");

                if(isPlayerComputer === false) {
                    //setHighlightClickableBoardCols();

                }
                else {
                    computerMove();
                }
            }
            else {
                isItMyTurn = false;
                $("#turn").html("It's " + json.name + "'s turn. Stand by...");
                $("#quitButton").attr("disabled", "");

                // for(var i = 0; i < boardCols.length; i++) {
                //     boardCols.eq(i).removeClass("highlight");
                // }
                //gameoverRepeater = setInterval(checkGameOver, 3000); // Saar: The game doesn't change turns if you add this (normal game without quiting).
                passivePlayerRepeater = setInterval(updateBoard, 500);
            }
        }
    });
}

$(function() {
    $(document).on("click", "[id^=boardRow-]", function () {
        var destination = this.id.replace("boardCol-", "");
        destination = destination.replace("boardRow-", "");

        if(destination.indexOf("Circle") >= 0){
            destination = destination.replace("Circle", "");
        }
        if(destination.indexOf("text") >= 0){ // Saar: without this, the request could make the server throws exception
            destination = destination.replace("text", "");
        }

        var commaIndex = destination.indexOf(",");
        var destinationRow = destination.substr(0, commaIndex);
        var destinationCol = destination.substr(commaIndex + 1, 999); // 999.. (===infinity)

        if (isPlayerComputer === false && isItMyTurn === true) {
            $.ajax({
                data: {
                    "myName": playerName,
                    "destinationCol": destinationCol,
                    "destinationRow": destinationRow
                },
                type: "POST",
                url: "../executeMove",
                timeout: 2000,
                error: function () {
                    console.error("Failed to get ajax response");
                },
                success: function (json) {
                    var isActionSucceeded = json;
                    //console.log("Got ajax response - " + playerName + "'s move executed");
                    console.log(isActionSucceeded);

                    if (isActionSucceeded === "true" || isActionSucceeded === true) {
                        updateBoard();
                    }
                    else if(json !== "" && json !== undefined) {
                        alert(json);
                    }
                }
            });
        }
    });
});


    //     if (isPlayerComputer === false && playerName === lastTurnPlayerName) {
    //         if (isLastMoveExecuted) {
    //             isLastMoveExecuted = false;
    //
    //             var isPopoutMove = false;
    //
    //             if ($("#popoutVariant").css("display") === "inline") {
    //                 if ($("#popoutMove").is(":checked")) {
    //                     isPopoutMove = true;
    //                 }
    //             }
    //
    //             $.ajax({
    //                 data: {
    //                     "destinationCol": destinationCol,
    //                     "destinationRow": destinationRow,
    //                     "isPopoutMove": isPopoutMove,
    //                     "discType": currentPlayerDiscType
    //                 },
    //                 type: "POST",
    //                 url: "../executeMove",
    //                 timeout: 2000,
    //                 error: function () {
    //                     console.error("Failed to get ajax response");
    //                 },
    //                 success: function () {
    //                     console.log("Got ajax response - " + playerName + "'s move executed");
    //
    //                     updateBoard();
    //                 }
    //             });
    //         }
    //     }


function updateBoard() {
    $.ajax({
        data: {"activePlayer": isItMyTurn, // for debug
                "myName": playerName // for debug
        },
        type: "GET",
        url: "../boardUpdater",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function (json) {
            //console.log("Got ajax response - last move changes: " + json.lastMoveChanges + " ; " + "is player quit: " + json.isCurrentPlayerQuit);


            // if(json.isCurrentPlayerQuit === true) {
            //     $("#" + lastTurnPlayerName + "Card").css("opacity", "0.3");
            // }
            // else {
            //     $("#" + lastTurnPlayerName + "TurnsPlayed").html(++lastTurnPlayerTurnsPlayed);
            // }

            if (json !== false && json !== "false") { // false === active player didn't make his move yet

                if (passivePlayerRepeater != null) { // user got an updated board, we can stop asking for it.
                    clearInterval(passivePlayerRepeater);
                    passivePlayerRepeater = null;
                }

                for(var k = 0; k< json.playersList.length; k++){
                    var avgOfFlips;
                    var currPlayer = json.playersList[k];
                    $("#"+ currPlayer.name + "Score").text(currPlayer.statistics.score);
                    if(currPlayer.statistics.countOfPlayedTurns !== 0){
                        avgOfFlips = currPlayer.statistics.totalNumOfFlips/currPlayer.statistics.countOfPlayedTurns;
                    }
                    else{
                        avgOfFlips = 0;
                    }

                    $("#"+ currPlayer.name + "AverageOfFlips").text(Number.parseFloat(avgOfFlips).toFixed(2));
                    $("#"+ currPlayer.name + "TurnsPlayed").text(currPlayer.statistics.countOfPlayedTurns);
                }

                // every user get the new board and updates his UI board according to the logic board.
                for (var i = 0; i < json.board.height; i++) {
                    for (var j = 0; j < json.board.width; j++)
                        // document.getElementById("boardCol-" + j).querySelector("#boardRow-" + i).style.fill = json.gameboard[i][j].disc.discType1;
                        if (json.board.gameboard[i][j].disc !== undefined) {
                            document.getElementById("boardRow-" + i + "," + "boardCol-" + j + "Circle").style.fill = json.board.gameboard[i][j].disc.type;
                        }
                        else {
                            document.getElementById("boardRow-" + i + "," + "boardCol-" + j + "Circle").style.fill = 'lightgreen';
                        }
                }


                checkGameOver();
            }

        }
    });
}

function synchronizeEndTurn() {

    $.ajax({
        data: {"activePlayer": isItMyTurn, // for debug
                "myName": playerName // for debug
        },
        type: "GET",
        url: "../synchronizeEndTurn",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function (json) {
            console.log("Got ajax response - are all players synchronized after complete turn of the game: " + json);

            if(json === true || json === "true") {
                clearInterval(synchronizeEndTurnRepeater);
                synchronizeEndTurnRepeater = null;
                getCurrentPlayerTurn();
            }
        }
    });
}






// $(function() {
//     $("#regularMove").click(function() {
//         if(playerName === lastTurnPlayerName) {
//             setHighlightClickableBoardCols();
//         }
//     });
// });
//
// $(function() {
//     $("#popoutMove").click(function() {
//         if(playerName === lastTurnPlayerName) {
//             setHighlightClickableBoardCols();
//         }
//     });
// });
//
// function setHighlightClickableBoardCols() {
//     var isPopoutMove = false;
//
//     if($("#popoutVariant").css("display") === "inline") {
//         if($("#popoutMove").is(":checked")) {
//             isPopoutMove = true;
//         }
//     }
//
//     $.ajax({
//         data: {"isPopoutMove":isPopoutMove},
//         type: "POST",
//         url: "../clickableBoardCols",
//         timeout: 2000,
//         error: function () {
//             console.error("Failed to get ajax response");
//         },
//         success: function (json) {
//             console.log("Got ajax response - clickable board cols: " + json.clickableBoardColsNum);
//
//             for(var i = 0; i < boardCols.length; i++) {
//                 if(json.clickableBoardColsNum.includes(i)) {
//                     boardCols.eq(i).addClass("highlight");
//                 }
//                 else {
//                     boardCols.eq(i).removeClass("highlight");
//                 }
//             }
//         }
//     });
// }
//
// $(function() {
//     $(document).on("click", "[id^=boardCol-]", function() {
//         var destinationCol = this.id.replace("boardCol-", "");
//         var destinationRow = this.id.replace("boardRow-", "");
//         if(this.classList.contains("highlight")) {
//             if (isPlayerComputer === false && playerName === lastTurnPlayerName) {
//                 if (isLastMoveExecuted) {
//                     isLastMoveExecuted = false;
//
//                     var isPopoutMove = false;
//
//                     if ($("#popoutVariant").css("display") === "inline") {
//                         if ($("#popoutMove").is(":checked")) {
//                             isPopoutMove = true;
//                         }
//                     }
//
//                     $.ajax({
//                         data: {"destinationCol": destinationCol, "destinationRow": destinationRow ,"isPopoutMove": isPopoutMove, "discType": currentPlayerDiscType},
//                         type: "POST",
//                         url: "../executeMove",
//                         timeout: 2000,
//                         error: function () {
//                             console.error("Failed to get ajax response");
//                         },
//                         success: function () {
//                             console.log("Got ajax response - " + playerName + "'s move executed");
//
//                             updateBoard();
//                         }
//                     });
//                 }
//             }
//         }
//     });
// });
//
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
//
// function computerMove() {
//     $.ajax({
//         data: "",
//         type: "GET",
//         url: "../executeMove",
//         timeout: 4000,
//         error: function () {
//             console.error("Failed to get ajax response");
//         },
//         success: function () {
//             console.log("Got ajax response - " + playerName + "'s move executed");
//
//             updateBoard();
//         }
//     });
// }
//
// function updateBoard() {
//     $.ajax({
//         data: "",
//         type: "GET",
//         url: "../boardUpdater",
//         timeout: 2000,
//         error: function () {
//             console.error("Failed to get ajax response");
//         },
//         success: function (json) {
//             console.log("Got ajax response - last move changes: " + json.lastMoveChanges + " ; " + "is player quit: " + json.isCurrentPlayerQuit);
//
//             if(passivePlayerRepeater != null) {
//                 if(json.gameboard.length > 0) {
//                     clearInterval(passivePlayerRepeater);
//                     passivePlayerRepeater = null;
//
//                     if(json.isCurrentPlayerQuit === true) {
//                         $("#" + lastTurnPlayerName + "Card").css("opacity", "0.3");
//                     }
//                     else {
//                         $("#" + lastTurnPlayerName + "TurnsPlayed").html(++lastTurnPlayerTurnsPlayed);
//                     }
//
//                     for(var i = 0; i < json.height; i++) {
//                         for(var j = 0; j < json.width; j++)
//                             document.getElementById("boardCol-" + j).querySelector("#boardRow-" + i).style.fill = json.gameboard[i][j].disc.discType1;
//                     }
//
//                     checkGameOver();
//                 }
//             }
//             else {
//                 for(var i = 0; i < json.lastMoveChanges.length; i++) {
//                     document.getElementById("boardCol-" + json.lastMoveChanges[i].col).querySelector("#boardRow-" + json.lastMoveChanges[i].row).style.fill = json.lastMoveChanges[i].discType;
//                 }
//
//                 $("#" + lastTurnPlayerName + "TurnsPlayed").html(++lastTurnPlayerTurnsPlayed);
//
//                 checkGameOver();
//             }
//         }
//     });
// }
//
function checkGameOver() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../gameOver",
        timeout: 4000,
        error: function () {
            console.error("Failed to get ajax response - on check game over");
        },
        success: function (json) {
            var isWinner = false;

            if(json.isGameOver === true) {
                console.log("Got ajax response - game is over now");

                $("#endGameModal").modal({show: true, backdrop: "static", keyBoard: false});

                if(json.isGameOver === true) {
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
                        $("#modalPublicMessage").html("It's a tie!");
                    }

                    if(isWinner) {
                        $("#modalPrivateMessage").html("You Win!");
                    }
                    else {
                        $("#modalPrivateMessage").html("You Lose");
                    }
                }
                else if(json.endGameType === "TIE") {
                    $("#modalPublicMessage").html("It's a tie!");
                }
                else {
                    $("#modalPublicMessage").html("The technically winner is " + json.winnersNames);
                    $("#modalPrivateMessage").html("You the only player left in the room");
                }

                endGameLeaveRoom();
            }
            else {
                console.log("Got ajax response - the game is not over yet");

                synchronizeEndTurnRepeater = setInterval(synchronizeEndTurn, 200); // ask server to go to next turn.
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
        success: function (json) {
            // clearInterval(gameoverRepeater);
            // gameoverRepeater = null;
            // setTimeout("window.location='../pages/availableRooms.html'", 2000);
            console.log(playerName + " left the room - game is ended");

            setTimeout("window.location='../pages/availableRooms.html'", 2000);
        }
    });
}
//
// function changeTurn() {
//     $.ajax({
//         data: {"lastTurnPlayerName":lastTurnPlayerName},
//         type: "POST",
//         url: "../changeTurn",
//         timeout: 2000,
//         error: function () {
//             console.error("Failed to get ajax response");
//         },
//         success: function () {
//             console.log("Turn changed successfully");
//
//             synchronizeEndTurnRepeater = setInterval(synchronizeEndTurn, 200);
//         }
//     });
// }
//
// function synchronizeEndTurn() {
//     $.ajax({
//         data: "",
//         type: "GET",
//         url: "../synchronizeEndTurn",
//         timeout: 2000,
//         error: function () {
//             console.error("Failed to get ajax response");
//         },
//         success: function (json) {
//             console.log("Got ajax response - are all players synchronized after complete turn of the game: " + json.isActionSucceeded);
//
//             if(json.isActionSucceeded === true) {
//                 clearInterval(synchronizeEndTurnRepeater);
//                 synchronizeEndTurnRepeater = null;
//                 getCurrentPlayerTurn();
//             }
//         }
//     });
// }