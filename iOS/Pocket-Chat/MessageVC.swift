//
//  MessageVC.swift
//  Pocket-Chat
//
//  Created by jeremy beal on 4/18/19.
//  Copyright © 2019 Pocket_Sample. All rights reserved.
//

import UIKit
import MessageKit
import MessageInputBar
import PocketSwift
import BigInt



class MessageVC: MessagesViewController {
    
    let pocketAion = try? PocketAion.init(devID: nil, netIds: ["32"], defaultNetID: "32", maxNodes: 5, requestTimeOut: 20000)
    
  
    
    let user1_Pub = "0x8d5Cd515c8696d917139Cd9091c84AC59B766046"
    let user1_Priv = "1390b7268f37df81d76db982961de67646506fe70e28f83b32ca97b8ff02a913"
    
    var messages: [Message] = []
    var member: Member!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //TODO i need to add the wallet ID for identification for "member"
        // from the VC
        messagesCollectionView.messagesDataSource = self
        messagesCollectionView.messagesLayoutDelegate = self as? MessagesLayoutDelegate
        messageInputBar.delegate = self
        messagesCollectionView.messagesDisplayDelegate = self as? MessagesDisplayDelegate
        
        // create refresh button
        let refreshImage = UIImage(named: "refresh")
        let refreshItem = InputBarButtonItem(type: .system)
        refreshItem.setSize(CGSize(width: 60, height: 30), animated: false)
        
        refreshItem.image = refreshImage
        
        refreshItem.addTarget(self, action: #selector(MessageVC.refreshScreen(_:)), for: .touchUpInside)
        
        messageInputBar.leftStackView.alignment = .center
        messageInputBar.setLeftStackViewWidthConstant(to: 30, animated: false)
        
        messageInputBar.setStackViewItems([refreshItem], forStack: .left, animated: false)
        
    }

    
    // TODO display possible new messages
    @objc func refreshScreen (_ sender:UIButton!){
        
        let wallet = try? pocketAion!.importWallet(privateKey: "b274e8cc594cedad360b18791924c5219bc2745969fc2a45701f1f66f2053482083b81d17e633edbddd0aa88b6678ceb5e2dacdb15d7a18e1dbceabddc5d63f6", netID: "32")
        let name = "getMessageByIndex"
        let parameters = ["2"]
        let nrg = BigUInt(150000)
        let nrgPrice = BigUInt(20000000000)
        
       let aionContact = try? AionContract.init(aionNetwork: pocketAion!.mastery!, address: "0xA0dC0a5E880F2ea7fb74eA9fB5319fe9ee98968F0B06bCAC535e7EF0152e8aC9", abiDefinition: "[{\"outputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"string\"}],\"constant\":true,\"payable\":false,\"inputs\":[{\"name\":\"_index\",\"type\":\"uint128\"}],\"name\":\"getMessageByIndex\",\"type\":\"function\"},{\"outputs\":[{\"name\":\"\",\"type\":\"uint128\"}],\"constant\":true,\"payable\":false,\"inputs\":[],\"name\":\"getTotalMessageCount\",\"type\":\"function\"},{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\":[{\"name\":\"_sender\",\"type\":\"address\"},{\"name\":\"_content\",\"type\":\"string\"}],\"name\":\"sendMessage\",\"type\":\"function\"}]")
       
        try? aionContact?.executeConstantFunction(functionName: "getTotalMessageCount", functionParams: [], fromAddress: nil, gas: nrg, gasPrice: nrgPrice, value: nil, blockTag: nil, callback: { (error, count) in
            print("text count is ", count)
            return
            //print(error)
            })
            
        try? aionContact?.executeConstantFunction(functionName: name, functionParams: parameters, fromAddress: "0xa0500bbb5b3d4556a4f93bf536a29d1edca0e4f50d7df3749053e03660c16a91", gas: nrg, gasPrice: nrgPrice, value: nil, blockTag: nil, callback: { (error, textMessage) in
            //print(textMessage)
            
            var address = "\(textMessage![0])"
            let otherUser = "\(textMessage![1])"
            
            print(address)
            let change = Member(address: address)
            
            let theirMessage = Message(
                member: change, textMessage: otherUser,
                messageId: "32")
           
           
            //print(otherUser)
            
            self.messages.append(theirMessage)
            self.messagesCollectionView.reloadData()
            
        })
        
        
        
    }
    
    
   
    // takes the text saved and stored in messages
    func sendText(_ message: String) {
        
        let userMessage = message
        //let getText = userMessage.
        //print(userMessage)
        let wallet = try? pocketAion!.importWallet(privateKey: "b274e8cc594cedad360b18791924c5219bc2745969fc2a45701f1f66f2053482083b81d17e633edbddd0aa88b6678ceb5e2dacdb15d7a18e1dbceabddc5d63f6", netID: "32")
        let name = "sendMessage"
        let parameters = [wallet!.address,userMessage]
        let nrg = BigUInt(1500000)
        let nrgPrice = BigUInt(20000000000)
        
   
        do {

            let aionContract = try? AionContract.init(aionNetwork: pocketAion!.mastery!, address: "0xA0dC0a5E880F2ea7fb74eA9fB5319fe9ee98968F0B06bCAC535e7EF0152e8aC9", abiDefinition: "[{\"outputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"string\"}],\"constant\":true,\"payable\":false,\"inputs\":[{\"name\":\"_index\",\"type\":\"uint128\"}],\"name\":\"getMessageByIndex\",\"type\":\"function\"},{\"outputs\":[{\"name\":\"\",\"type\":\"uint128\"}],\"constant\":true,\"payable\":false,\"inputs\":[],\"name\":\"getTotalMessageCount\",\"type\":\"function\"},{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\":[{\"name\":\"_sender\",\"type\":\"address\"},{\"name\":\"_content\",\"type\":\"string\"}],\"name\":\"sendMessage\",\"type\":\"function\"}]")
            
            //let getAddress = wallet?.address as! String
            
            try? aionContract?.executeFunction(functionName: name, wallet: wallet!, nonce: nil, gas: nrg, gasPrice: nrgPrice, value: nil, callback: { (error, hash) in
                //print(error)
                print("take two", hash)
                
            })
            
        } catch {
            print("is this the error?", error)
        }
        
        
    }
    
}

extension MessageVC: MessagesLayoutDelegate {
    
    
    func heightForLocation(message: MessageType,
                           at indexPath: IndexPath,
                           with maxWidth: CGFloat,
                           in messagesCollectionView: MessagesCollectionView) -> CGFloat {
        
        return 0
    }
}


extension MessageVC: MessagesDisplayDelegate {
    func configureAvatarView(
        _ avatarView: AvatarView,
        for message: MessageType,
        at indexPath: IndexPath,
        in messagesCollectionView: MessagesCollectionView) {
        
    }
    
    func shouldDisplayHeader(for message: MessageType, at indexPath: IndexPath,
                             in messagesCollectionView: MessagesCollectionView) -> Bool {
        
        // 2
        return false
    }
    
    func messageStyle(for message: MessageType, at indexPath: IndexPath,
                      in messagesCollectionView: MessagesCollectionView) -> MessageStyle {
        
        let corner: MessageStyle.TailCorner = isFromCurrentSender(message: message) ? .bottomRight : .bottomLeft
        
        // 3
        return .bubbleTail(corner, .curved)
    }
    
    func footerViewSize(for message: MessageType, at indexPath: IndexPath,
                        in messagesCollectionView: MessagesCollectionView) -> CGSize {
        
        // 2
        return CGSize(width: 0, height: 8)
    }
    
}

extension MessageVC: MessagesDataSource {
    
    func currentSender() -> Sender {
        return Sender(id: "0x1", displayName: "John")
    }
    
    func numberOfSections(in messagesCollectionView: MessagesCollectionView) -> Int {
        return messages.count
    }
    
    func messageForItem(at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> MessageType {
        return messages[indexPath.section]
    }
    
    func messageTopLabelAttributedText(
        for message: MessageType,
        at indexPath: IndexPath) -> NSAttributedString? {
        
        return NSAttributedString(
            string: message.sender.displayName,
            attributes: [.font: UIFont.systemFont(ofSize: 12)])
    }
}

//This allows us to actually send a new message. For now, we'll just append the message onto the array

extension MessageVC: MessageInputBarDelegate {
    func messageInputBar( _ inputBar: MessageInputBar, didPressSendButtonWith text: String) {
        
        let ourMessages = Member(address: "0x1")
        
        let newMessage = Message(
            member: ourMessages, textMessage: text,
            messageId: UUID().uuidString)
        
        //TODO: Store message in Sendtext function and send it
        sendText(text)
        
        //then append the message so it can be displayed
        messages.append(newMessage)
        
        //erase the data in the input bar
        inputBar.inputTextView.text = ""
        
        messagesCollectionView.reloadData()
        messagesCollectionView.scrollToBottom(animated: true)
    }
}


/* if we ever want to send money(add this in diditload)
 
 // creates the money image, button, and binds it to the message input bar.
 
 let moneyImage = UIImage(named: "Btc")
 //let moneyView = UIImageView(image: moneyImage)
 
 let moneyItem = InputBarButtonItem(type: .system)
 moneyItem.setSize(CGSize(width: 10, height: 20), animated: false)
 moneyItem.image = moneyImage
 moneyItem.addTarget(self, action: #selector(MessageVC.sendMoney(_:)), for: .touchUpInside)
 
 
 
 // creates the action for when someone wants to sendMoney (add outside of diditload)
 @objc func sendMoney(_ sender:UIButton!){
 
 let address = "0xa0510dd236472e90f0ff4f6b7b4f70b1d8c5206cf303839f9a4e8fa6af0dd420"
 let privateKey = "0xd0c6208eb958998dcdac23bedf7d58d863c5abe64e250e4e379a4efd3966cd99e5cab1be5be1655abc987ff7321a438581778919b859370cf1faa22346b201fc"
 // let nonce = BigInt.init(13)
 let to =  "0xa0d969df9232b45239b577c3790887081b5a22ffd5a46a8d82584ee560485624"
 
 }
 
 */
