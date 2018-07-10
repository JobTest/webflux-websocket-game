'use strict'

var usernamePage = document.querySelector('#username-page')
var gamePage = document.querySelector('#game-page')
var gameHeader = document.querySelector('#game-header')
var usernameForm = document.querySelector('#usernameForm')
var messageForm = document.querySelector('#messageForm')
var messageInput = document.querySelector('#message')
var username = document.querySelector('#username')
var connectingElement = document.querySelector('.connecting')
var play = document.querySelector('#play')

var eventSource = null
var player = { }
var processingIndicator = ""

function toPlay(player) {
    var jsonPlayer = JSON.stringify(player).replace("{","").replace("}","")
    eventSource = new EventSource("/api/game-round/" + player.id + "?jsonPlayer=" + jsonPlayer)

    eventSource.addEventListener('message', function (e) {
        console.log(e.data)

        processingIndicator = processingIndicator + "."
        play.textContent = processingIndicator
        player = JSON.parse(e.data)

        if (player.state == 'STOP_ROUND') {
            eventSource.close()
            connectingElement.textContent = player.win
            connectingElement.style['color'] = '#00AA00';
            play.textContent = "Start round"
        }
    }, false)

    eventSource.addEventListener('open', function (e) {
        console.log("Connection was opened.")

        connectingElement.textContent = "Connecting..."
        connectingElement.style['color'] = '#777777';
        processingIndicator = ""
    }, false)

    eventSource.onerror = function(e) {
        if (this.readyState == EventSource.CONNECTING) {
            console.log("Connection was closed, reconnection...")

            connectingElement.textContent = "Connection was closed, reconnection..."
            connectingElement.style['color'] = '#ff5652';
        } else {
            console.log("Status error: " + this.readyState)
        }
    }
}

function sendMessage(e) {
    var number = messageInput.value.trim();
    if(number) {
        player.number = number
        player.state = "PLAY"
        toPlay(player)
    }
    e.preventDefault();
}

function connect(e) {
    var playerId = username.value.trim()
    if(playerId) {
        console.log("Player: " + playerId)

        usernamePage.classList.add('hidden')
        gamePage.classList.remove('hidden')
        if (player.id==null) {
            gameHeader.textContent = playerId
            player.id = playerId
            player.state = "CONNECT"
            toPlay(player)
        }
    }
    e.preventDefault();
}

messageForm.addEventListener('submit', sendMessage, true)
usernameForm.addEventListener('submit', connect, true)