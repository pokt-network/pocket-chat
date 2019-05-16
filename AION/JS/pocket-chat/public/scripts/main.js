
function formatUsername(address){
    if (address != null && address != "") {
        var firstFour = address.substring(0, 6)
        var secondFour = address.substring(address.length - 4 , address.length)
        return firstFour+"..."+secondFour
    }else {
        return "No address"
    }
}

function cleanUpChat() {
    $('#chat-messages').empty()
}