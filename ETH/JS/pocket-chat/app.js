const express = require('express')
const bodyParser = require('body-parser')
const app = express()
const path = require('path')
const APP_PORT = 3000
const abi = [{"constant":true,"inputs":[{"name":"_index","type":"uint256"}],"name":"getMessageByIndex","outputs":[{"name":"","type":"address"},{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"getTotalMessageCount","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_sender","type":"address"},{"name":"_content","type":"string"}],"name":"sendMessage","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"}]
// Server
const server = app.listen(APP_PORT, () => {
    console.log(`App running on port ${APP_PORT}`)
})
// io
const io = require('socket.io').listen(server)
// Pocket Eth
const EthPackage = require('pocket-js-eth');
const pocketEth = new EthPackage.PocketEth("", [4], 10, 10000);
const smartContractAddress = "0x0b4515fa7e8287f2da3e8776a239a5d6a493b878";
const ethContract = new EthPackage.EthContract(pocketEth.rinkeby, smartContractAddress, abi);
var wallet = null;
// Messages
var messages = []
// Views
app.set('views', path.join(__dirname, 'views'))
app.set('view engine', 'pug')
app.use(express.static('public'))
// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: false }))
// Welcome View
app.get('/', (req, res) => {
    res.render('welcome', {
        // wallet: JSON.stringify(wallet)
    })
})
// Import View
app.get('/import', (req, res) => {
    res.render('import')
})
// Import Wallet Action
app.post('/import', (req, res) => {
    var importedWallet = pocketEth.rinkeby.importWallet(req.body.key);
    if (importedWallet instanceof Error) {
        io.emit('error-update', {
            content: "Failed to import wallet with error: "+importedWallet.message
        })
    }else {
        wallet = importedWallet;
        res.redirect("/chat") 
    }
})
// Create Wallet
app.get('/create', (req, res) => {
    res.render('create')
})
// Chat View
app.get('/chat', (req, res) => {
    res.render('chat', {
        wallet: JSON.stringify(wallet)
    })
})
// On Connection
io.on('connection', (socket) => {
    // Create wallet
    socket.on('create-wallet', () => {
        wallet = pocketEth.rinkeby.createWallet();

        if (wallet instanceof Error) {
            io.emit('error-update', {
                content: "Failed to create wallet with error: "+wallet.message
            })
        }else {
            io.emit('wallet-created', wallet)
        }
    })
    // Move to Chat with imported wallet
    socket.on('show-chat', (wallet) => {
        res.render('chat', {
            wallet: JSON.stringify(wallet)
        })
    })
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
    var msgCount = await ethContract.executeConstantFunction("getTotalMessageCount", [], null, 100000, 10000000000, null)
    // Check for errors
    if (msgCount instanceof Error) {
        if (callback) {
            callback(msgCount);
        }
        io.emit('error-update', {
            content: "Failed to get messages: " + msgCount
        })
        return msgCount;
    }
    // Parse response
    var count = BigInt(msgCount[0]).toString()
    count = parseInt(count, 10)
    // Request messages by index
    while (count > 0) {
        count--;
        var msg = await _getMessagesWithIndex(count)
        if (!msg instanceof Error) {
            if (callback) {
                callback(msg);
            }
            io.emit('error-update', {
                content: "Failed to get messages: " + msg
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
    var result = await ethContract.executeConstantFunction("getMessageByIndex", [index], null, 100000, 10000000000, null)
    if (result instanceof Error) {
        if (callback) {
            callback(result);
        }
        io.emit('error-update', {
            content: "Failed to get messages: " + result
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
        ethContract.executeFunction("sendMessage", wallet, [content.name, content.message], null, 200000, 10000000000, 0, function (error, result) {
            if (error != null) {
                io.emit('status-update', {message: "Failed to send message with error: "+error})
                return
            }
            // Send the success message
            io.emit('status-update', { message: "Message sent, txHash: "+result })
            // Loop for the receipt status of the transaction
            setTimeout(function () {
                pocketEth.rinkeby.eth.getTransactionReceipt(result, function (error, result) {
                    if (error != null) {
                        console.log("Receipt status = " + result.status)
                        if (BigInt(result.status).toString(16) == "1") {
                            io.emit('chatter-update')
                        }
                    } else {
                        io.emit('error-update', {
                            content: "Failed to check message status, please check again later"
                        })
                    }
                })
            }, 15000)
        });
    }else{
        console.log("sendMessage Error: Wallet = "+wallet+", message = "+content)
    }
}