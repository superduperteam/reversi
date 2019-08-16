var passivePlayerRepeater;
var synchronizeEndTurnRepeater;
var getCurrentTurnRepeater;
var gameoverRepeater;
var stompClient = null;

var boardHeight
var boardWidth;

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
    connect();
    initializeGame();
};


function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe("/topic/rooms/" + getRoomID() +"/game/getWhoseTurn", function (json) {
            onGetCurrentPlayerTurn(JSON.parse(json.body).content)
        });
    });
}

function getRoomID(){
    return document.URL.substring(document.URL.indexOf("/rooms")+7, document.URL.lastIndexOf("/game"));
}

function initializeGame() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../"+getRoomID()+"/gameStart",
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
                $("#players").append("<div id=\"" + isHuman + "Card" + "\" class=\"cardBody\" style=\"height: 12rem; width: 12rem; float: none; margin-right: auto; margin-left: auto;\">\n" +
                    "                <div class=\"cardBody\">\n" +
                    "                    <h5 class=\"card-title\">" + currentPlayerName + "</h5>\n" +
                    "                    <h6 class=\"card-subtitle mb-2 text-muted\">" + isHuman + "</h6>\n" +
                    "                    <br>\n" +
                    "                    <svg height=\"20\" width=\"80\">\n" +
                    "                        <rect width=\"80\", height=\"20\" style=\"fill:" + currentPlayerDiscType + "\"></rect>\n" +
                    "                    </svg>\n" +
                    "                    <br><br>\n" +
                    "                    <div class=\"card-text\"> Score:\n" +
                    "                        <span id=\"" + currentPlayerName + "Score" + "\">" + score + "</span>\n" +
                    "                    </div>\n" +
                    "                    <div class=\"card-text\"> Average of flips:\n" +
                    "                        <span id=\"" + currentPlayerName + "AverageOfFlips" + "\">0</span>\n" +
                    "                    </div>\n" +
                    "                    <div class=\"card-text\"> Turns Played:\n" +
                    "                        <span id=\"" + currentPlayerName + "TurnsPlayed" + "\">0</span>\n" +
                    "                    </div>\n" +
                    "                </div>\n" +
                    "            </div>");
            }

            for(var colIndex = 0; colIndex < json.board.width; colIndex++) {
                var colID = "boardCol-" + colIndex;
                var numOfPossibleMoves = "";
                $("#board").append("<div id=\"" + colID + "\"></div>");
                console.log(json);
                for(var rowIndex = 0; rowIndex < json.board.height; rowIndex++) {
                    var rowID = "boardRow-" + rowIndex;
                    var fill1;

                    if(json.board.gameboard[rowIndex][colIndex].disc !== undefined && json.board.gameboard[rowIndex][colIndex].disc !== null){
                        fill1 = json.board.gameboard[rowIndex][colIndex].disc.type;
                    }
                    else{
                        fill1 = 'lightgreen';
                        //if(json.board.gameboard[rowIndex][colIndex].flipPotential !== 0 && json.board.gameboard[rowIndex][colIndex].flipPotential !== '0'){
                        //}
                    }

                    if(json.board.gameboard[rowIndex][colIndex].countOfFlipsPotential !== 0 && json.board.gameboard[rowIndex][colIndex].countOfFlipsPotential !== '0'){
                        numOfPossibleMoves = json.board.gameboard[rowIndex][colIndex].countOfFlipsPotential;
                    }
                    //numOfPossibleMoves = json.board.gameboard[rowIndex][colIndex].countOfFlipsPotential;

                    var id1 = rowID + "," + colID;
                    $("#" + colID).append("<div id=\"" + id1 + "\"> \n" +
                        "                       <svg height=\"80\" width=\"80\">\n" +
                        "                           <rect width=\"80\" height=\"80\" style=\"fill: lightgreen;stroke:black;stroke-width:5\"></rect>\n" +
                        "                           <circle id=\"Circle" +id1 +"\" cx=\"40\" cy=\"40\" r=\"32\" stroke=\"lightgreen\" stroke-width=\"1\" fill=\"" + fill1+ "\" />\n" +
                        "                           <text style=\"display: none\" id=\"text" +id1 +"\" x=\"50%\" y=\"50%\" stroke=\"#51c5cf\" stroke-width=\"2px\" dy=\".3em\">"+ numOfPossibleMoves + " </text>\n" +
                        "                       </svg>\n" +
                        "                  </div>\n");
                }
            }

            if($("#boardJumbotron").hasHorizontalScrollBar()){
                $("#boardJumbotron").css("display", "");
            }

            $("#startGameModal").modal({show: true});

            boardWidth = json.board.width;
            boardHeight = json.board.height;

            getThisPlayer();
        }
    });
};

function getThisPlayer() {
    $.ajax({
        data: "",
        type: "GET",
        url: "../../players/playerName",
        timeout: 2000,
        error: function() {
            console.error("Failed to get ajax response");
        },
        success: function(json) {
            console.log("Got ajax response - this player name: " + json);

            playerName = json;
        }
    });
}

function onGetCurrentPlayerTurn() {
    console.log("Got ajax response - current player name is: " + json.name);
    if (playerName === json.name) {
        isItMyTurn = true;
        $("#turn").html(playerName + ", It's your turn");
        $("#quitButton").removeAttr("disabled");

        if (isPlayerComputer === true) {
            computerMove();
        }
    } else {
        isItMyTurn = false;

        $("#turn").html("It's " + json.name + "'s turn. Stand by...");
        $("#quitButton").attr("disabled", "");
    }
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
            if (json !== "false" && json !== false) {
                console.log("Got ajax response - current player name is: " + json.activePlayer.name);
                clearInterval(getCurrentTurnRepeater);
                getCurrentTurnRepeater = null;


                // updateUI(json);
                //isLastMoveExecuted = true;
                //lastTurnPlayerDiscColor = json.discType;
                //lastTurnPlayerTurnsPlayed = json.turnsPlayedNum;

                if (playerName === json.activePlayer.name) {
                    isItMyTurn = true;
                    passivePlayerRepeater = null;
                    $("#turn").html(playerName + ", It's your turn");
                    $("#quitButton").removeAttr("disabled");

                    updateUI(json);

                    if (isPlayerComputer === true) {
                        computerMove();
                    }
                } else {
                    isItMyTurn = false;

                    updateUI(json);
                    $("#turn").html("It's " + json.activePlayer.name + "'s turn. Stand by...");
                    $("#quitButton").attr("disabled", "");

                    // for(var i = 0; i < boardCols.length; i++) {
                    //     boardCols.eq(i).removeClass("highlight");
                    // }
                    //gameoverRepeater = setInterval(checkGameOver, 3000); // Saar: The game doesn't change turns if you add this (normal game without quiting).
                    passivePlayerRepeater = setInterval(updateGame, 500);
                }
            }
        }
    });
}

function computerMove() {
    $.ajax({
        data: {"activePlayer": isItMyTurn, // for debug
            "myName": playerName // for debug
        },
        type: "GET",
        url: "../executeMove",
        timeout: 4000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function () {
            console.log("Got ajax response - " + playerName + "was move executed");

            updateGame();
        }
    });
}

$(function() {
    // $('#textbox1').val(this.checked);
    $("#tutorialModeCheckBox").change(function() {
        if(boardHeight !== undefined && boardWidth !== undefined){
            if(this.checked) {
                for (var i = 0; i < boardHeight; i++) {
                    for (var j = 0; j < boardWidth; j++){
                         if(isItMyTurn){
                            document.getElementById("textboardRow-" + i + "," + "boardCol-" + j).style.display = "";
                         }
                    }
                }
            }
            else{
                for (var k = 0; k < boardHeight; k++) {
                    for (var m = 0; m < boardWidth; m++){
                        document.getElementById("textboardRow-" + k + "," + "boardCol-" + m).style.display = "none";
                    }
                }
            }
        }
    });
});

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
                        updateGame();
                    }
                    else if(json !== "" && json !== undefined && json !== false && json !== "false" && json !== "again" && json !== "\"again\"") {
                        alert(json);
                    }
                    else if(json === "again" || json === "\"again\""){
                        $( "#boardRow-" + destinationRow +"," + "boardCol" + destinationCol).click();
                    }
                }
            });
        }
    });
});

function updateUI(gameManager){

    var numOfPossibleMoves;
    if (passivePlayerRepeater != null) { // user got an updated board, we can stop asking for it.
        clearInterval(passivePlayerRepeater);
        passivePlayerRepeater = null;
    }

    for(var k = 0; k< gameManager.playersList.length; k++){
        var avgOfFlips;
        var currPlayer = gameManager.playersList[k];
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
    for (var i = 0; i < gameManager.board.height; i++) {
        for (var j = 0; j < gameManager.board.width; j++){
            numOfPossibleMoves = "";
            var currDisc = gameManager.board.gameboard[i][j].disc;

            // document.getElementById("boardCol-" + j).querySelector("#boardRow-" + i).style.fill = gameManager.gameboard[i][j].disc.discType1;
            if (currDisc !== undefined && currDisc !== null) {
                document.getElementById("CircleboardRow-" + i + "," + "boardCol-" + j).style.fill = gameManager.board.gameboard[i][j].disc.type;
            }
            else {
                document.getElementById("CircleboardRow-" + i + "," + "boardCol-" + j).style.fill = 'lightgreen';
            }

            if(gameManager.board.gameboard[i][j].countOfFlipsPotential !== 0 && gameManager.board.gameboard[i][j].countOfFlipsPotential !== '0'){
                numOfPossibleMoves = gameManager.board.gameboard[i][j].countOfFlipsPotential;
            }

            document.getElementById("textboardRow-" + i + "," + "boardCol-" + j).textContent = numOfPossibleMoves;

            var isTutorialChecked =  $("#tutorialModeCheckBox").prop('checked');

            // if(playerName === gameManager.activePlayer.name){
            //     alert("my board my turn");
            // }

            //if(playerName === gameManager.activePlayer.name && isTutorialChecked === true){
            if(playerName === gameManager.activePlayer.name && isTutorialChecked === true){
                document.getElementById("textboardRow-" + i + "," + "boardCol-" + j).style.display = "";
            }
            else{
                document.getElementById("textboardRow-" + i + "," + "boardCol-" + j).style.display = "none";
            }
        }
    }
}

function updateGame() {
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
                updateUI(json)
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
                            break;}}
                    if(json.winnersNames.length === 1) {
                        $("#modalPublicMessage").html("The winner is " + json.winnersNames);}
                    else {
                        $("#modalPublicMessage").html("It's a tie!");}
                    if(isWinner) {
                        $("#modalPrivateMessage").html("You Win!");}
                    else {
                        $("#modalPrivateMessage").html("You Lose");}}
                else if(json.endGameType === "TIE") {
                    $("#modalPublicMessage").html("It's a tie!");}
                else {
                    $("#modalPublicMessage").html("The technically winner is " + json.winnersNames);
                    $("#modalPrivateMessage").html("You the only player left in the room");}

                endGameLeaveRoom();
            }
            else {
                console.log("Got ajax response - the game is not over yet");
                synchronizeEndTurnRepeater = setInterval(synchronizeEndTurn, 200); // ask server to go to next turn.
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
                //getCurrentPlayerTurn();

                getCurrentTurnRepeater = setInterval(getCurrentPlayerTurn, 200);
            }
        }
    });
}

function endGameLeaveRoom() {
    $.ajax({
        data: {"myName": playerName // for debug
        },
        type: "GET",
        url: "../endGameLeave",
        timeout: 2000,
        error: function () {
            console.error("Failed to get ajax response");
        },
        success: function (json) {
            // clearInterval(gameoverRepeater);
            // gameoverRepeater = null;
            // setTimeout("window.location='../pages/rooms.html'", 2000);
            console.log(playerName + " left the room - game is ended");

            setTimeout("window.location='../pages/rooms.html'", 2000);
        }
    });
}

$(function() {
    $("#quitButton").click(function() {
        $.ajax({
            data: {"myName": playerName // for debug
            },
            type: "GET",
            url: "../playerQuit",
            timeout: 2000,
            error: function () {
                console.error("Failed to get ajax response");
            },
            success: function () {
                console.log("Got ajax response - " + playerName + " quit from the game");

                window.location = "../pages/rooms.html";
            }
        });
    });
});
