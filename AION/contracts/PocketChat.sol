pragma solidity ^0.4.15;

contract PocketChat {

    // Attributes
    // Stores msg index + sender
    mapping(uint => address) internal sendersPerIndex;
    // Stores msg index + msg content
    mapping(uint => string) internal contentsPerIndex;
    // Stores all the msg indexes
    uint[] internal msgIndices;

    function sendMessage(address _sender, string _content) public {
        // Calculate the next message index
        uint msgIndex = msgIndices.length;

        // Store the messages
        sendersPerIndex[msgIndex] = _sender;
        contentsPerIndex[msgIndex] = _content;
        msgIndices.push(msgIndex);
    }

    function getMessageByIndex(uint _index) public constant returns(address, string) {
        return (sendersPerIndex[_index], contentsPerIndex[_index]);
    }

    function getTotalMessageCount() public constant returns(uint) {
        return msgIndices.length;
    }

}