pragma solidity >=0.5.0;

contract notsoSneakyMessages{
    
    mapping (address => text[]) mytext;
    mapping (uint => address) addressIndex;
    uint addressCount;
    
    
    struct text {
        address toAddress;
        string message;
    }
    
    function  iteratetext() public{
        for(uint i=0;i<addressCount;i++)
    {
        mytext[addressIndex[i]];
    }
}
    
    function  appendText(address _toAddress, string memory _toText) public {
        text memory messageInfo = text(_toAddress, _toText);
        mytext[_toAddress].push(messageInfo);
    }
 
    function readMessage(address _toAddress)public view returns(bytes32[] memory messageInfo) {
        
        uint length = mytext[_toAddress].length;
        messageInfo = new bytes32[](length);

        for(uint i = 0; i < length; i++)
        {
            messageInfo[i] = stringToBytes32(mytext[_toAddress][i].message);
        }   
        //return  text[msg.sender];
  }
  
      function stringToBytes32(string memory source) private pure returns (bytes32 result) {
        bytes memory tempEmptyStringTest = bytes(source);
        if (tempEmptyStringTest.length == 0) {
            return 0x0;
        }

        assembly {
            result := mload(add(source, 32))
        }
    }
    
}
