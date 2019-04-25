const express = require('express')
const app = express()
const path = require('path')
const APP_PORT = 3000
const abi = [{"outputs":[{"name":"","type":"address"},{"name":"","type":"string"}],"constant":true,"payable":false,"inputs":[{"name":"_index","type":"uint128"}],"name":"getMessageByIndex","type":"function"},{"outputs":[{"name":"","type":"uint128"}],"constant":true,"payable":false,"inputs":[],"name":"getTotalMessageCount","type":"function"},{"outputs":[],"constant":false,"payable":false,"inputs":[{"name":"_sender","type":"address"},{"name":"_content","type":"string"}],"name":"sendMessage","type":"function"}]
// Server
const server = app.listen(APP_PORT, () => {
    console.log(`App running on port ${APP_PORT}`)
})
// io
const io = require('socket.io').listen(server)
// Pocket Aion
const AionPackage = require('pocket-js-aion')
const pocketAion = new AionPackage.PocketAion("", [32], 10, 10000);
const aionContract = new AionPackage.AionContract(pocketAion.mastery, "0xA0dC0a5E880F2ea7fb74eA9fB5319fe9ee98968F0B06bCAC535e7EF0152e8aC9", abi);
// Aion Wallet
const privateKey = ""
const wallet = pocketAion.mastery.importWallet(privateKey)
// Messages
var messages = []
// Views
app.set('views', path.join(__dirname, 'views'))
app.set('view engine', 'pug')
app.use(express.static('public'))

// Home
app.get('/', (req, res) => {
    res.render('index', {
        wallet: JSON.stringify(wallet)
    })
})
// On Connection
io.on('connection', (socket) => {
    // Send message to the client
    socket.on('chatter', (message) => {
        console.log('chatter = message : ', `${message.name} : ${message.message}`)
        io.emit('chatter', message)
    })
    // Send message to the Network
    socket.on('submit-message', (message) => {
        console.log('submit-message = message : ', message)
        sendMessage(message)
    })
    // Update chat list on the client
    socket.on('chatter-update', () => {
        messages = []
        getLatestMessages( function(status) {
            if (status == true) {
                console.log("Messages loaded successfully")
            }
        })
    })
})

// Retrieve a list of messages from the blockchain
async function getLatestMessages(callback) {
    // Get total messages count
    var msgCount = await aionContract.executeConstantFunction("getTotalMessageCount", [], null, 50000, 20000000000, null)
    // Check for errors
    if (msgCount instanceof Error) {
        if (callback) {
            callback(msgCount);
        }
        io.emit('error-update', {
            message: "Failed to get messages: " + msgCount
        })
        return msgCount;
    }

    // Parse response
    var count = BigInt(msgCount[0]).toString()
    count = parseInt(count, 10)
    // Request messages by index
    while (count > 0) {
        count--
        var msg = await _getMessagesWithIndex(count)
        if (!msg instanceof Error) {
            if (callback) {
                callback(msg);
            }
            io.emit('error-update', {
                message: "Failed to get messages: " + msg
            })
        }
    }
    messages = messages.reverse()
    messages.forEach( function(message){
        io.emit('chatter', message)
    })
    io.emit('stop-spinner')
    if (callback) {
        callback(true)
    }
    return true
}
async function _getMessagesWithIndex(index, callback){
    var result = await aionContract.executeConstantFunction("getMessageByIndex", [index], null, 50000, 20000000000, null)
    if (result instanceof Error) {
        if (callback) {
            callback(result);
        }
        io.emit('error-update', {
            message: "Failed to get messages: " + result
        })
        return result
    }
    // Create message object
    var msg = {index: index, name: result[0], content: result[1]}
    _pushUnique(msg)

    return msg
}
function _pushUnique(msg){
    var exists = messages.find(function(element) {
        return element.index == msg.index;
    });
    if (typeof exists == "undefined") {
        messages.push(msg)
    }
}
function _isValidMessage(content) {
    var result = true
    if (content.name == null || content.message == null ) {
        result = false
    }
    return result
}
// Send message to the smart contract
function sendMessage(content) {
    if (wallet != null && _isValidMessage(content)) {        
        aionContract.executeFunction("sendMessage", wallet, [content.name, content.message], null, 150000, 20000000000, 0, function (error, result) {
            if (error != null) {
                io.emit('status-update', {message: "Failed to send message with error: "+error})
                return
            }
            // Send the success message
            io.emit('status-update', { message: "Message sent, txHash: "+result })
            // Loop for the receipt status of the transaction
            setTimeout(function () {
                pocketAion.mastery.eth.getTransactionReceipt(result, function (error, result) {
                    if (error != null) {
                        console.log("Receipt status = " + result.status)
                        if (result.status == 1) {
                            io.emit('chatter-update')
                        }
                    } else {
                        io.emit('error-update', {
                            message: "Failed to check message status, please check again later"
                        })
                    }
                })
            }, 15000)
        });
    }else{
        console.log("sendMessage Error: Wallet = "+wallet+", message = "+content)
    }
}