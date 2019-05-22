//
//  MessageViewController.swift
//  Pocket-Chat
//
//  Created by jeremy beal on 4/18/19.
//  Copyright © 2019 Pocket_Sample. All rights reserved.
//

import UIKit
import MessageKit
import InputBarAccessoryView
import PocketSwift
import BigInt

class MessageViewController: MessagesViewController, MessagesDataSource, MessagesLayoutDelegate, MessagesDisplayDelegate {
    
   
    
    var pocketEth: PocketEth?
    var ethContract:EthContract?
    var wallet:Wallet?
    var messageList = [Message]()
    var currentUser: User?
    let refreshControl = UIRefreshControl()
    let formatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        return formatter
    }()
    var activityIndicator = UIActivityIndicatorView()
    
    // Views
    override func viewDidLoad() {
        super.viewDidLoad()
        // Set current user
        currentUser = User(senderId: UUID().uuidString, displayName: wallet!.address)
        
        // MessageKit setup
        configureMessageCollectionView()
        configureMessageInputBar()
        
        
        // Create refresh button
        let refreshImage = UIImage(named: "refresh")
        let refreshItem = InputBarButtonItem(type: .system)
        refreshItem.configure { (InputBarButtonItem) in
            InputBarButtonItem.setSize(CGSize(width: 60, height: 30), animated: false)
            InputBarButtonItem.image = refreshImage
            InputBarButtonItem.addTarget(self, action: #selector(self.refreshScreen(_:)), for: .touchUpInside)
        }
        let inputItems = [refreshItem]
        // Message Input bar config
        messageInputBar.leftStackView.alignment = .center
        messageInputBar.setLeftStackViewWidthConstant(to: 30, animated: false)
        messageInputBar.setStackViewItems(inputItems as [InputItem] , forStack: .left, animated: false)
        
        // Additional setup
        activityIndicator = UIActivityIndicatorView(style: .gray)
//        activityIndicator.frame = CGRect(x: 0, y: 0, width: 200, height: 200)
        activityIndicator.transform = CGAffineTransform(scaleX: 1.5, y: 1.5);
        activityIndicator.color = UIColor.blue;
        activityIndicator.center = self.view.center
        self.view.addSubview(activityIndicator)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        activityIndicator.startAnimating()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        // Load the first messages
        loadFirstMessages()
        
        
    }
    
    // Retrieve messages
    @objc func refreshScreen (_ sender:UIButton?){
        self.messageList.removeAll()

        do {
            //Create an aionContract Instance
            ethContract = try? EthContract.init(ethNetwork: pocketEth!.rinkeby!, address: AppConfig.smartContractAddress, abiDefinition: AppConfig.abiDefinition)
            
            //exececuteConstantFunciton
            try ethContract?.executeConstantFunction(functionName: "getTotalMessageCount", fromAddress: nil, gas: BigUInt(100000), gasPrice: BigUInt(10000000000), value: nil, blockTag: nil, callback: { (error, count) in
                
                if error != nil {
                    print("Failed to retrieve total message count with error: \(error!)")
                    return
                }
                // Parse response
                var msgCount = Int("\(count?.first ?? "0")") ?? 0
              
                do {
                    while msgCount > 0 {
                        msgCount = msgCount - 1
                        
                        var indexNum:[AnyObject] = []
                        indexNum.append(msgCount as AnyObject)


                        // executeConstantFunction can call getMessageByIndex
                        try self.ethContract?.executeConstantFunction(functionName: "getMessageByIndex", functionParams: indexNum, fromAddress: nil, gas: BigUInt(100000), gasPrice: BigUInt(10000000000), value: nil, blockTag: EthBlockTag.latest, callback: { (error, result) in
                           
                            if error != nil {
                                print("Failed to retrieve total message with error: \(error!)")
                                return
                            }
                            if result?.last != nil {
                                let message = Message.init(messageId: UUID().uuidString, user: self.currentUser!, textMessage: "\(result?.last! ?? "none")")
                                self.messageList.append(message)
                            }
                        })
                    }
                    
                    DispatchQueue.main.async {
                        self.messageList = self.messageList.reversed()
                        self.messagesCollectionView.reloadData()
                        self.activityIndicator.stopAnimating()
                        self.refreshControl.endRefreshing()
                    }
                } catch {
                    print("Failed with error: \(error)")
                    self.activityIndicator.stopAnimating()
                }
                
            })
        } catch {
            print("Failed with error: \(error)")
            self.activityIndicator.stopAnimating()
        }
    }
    
    func loadFirstMessages() {
        self.refreshScreen(nil)
    }
    
    @objc
    func loadMoreMessages() {
        self.loadFirstMessages()
    }
    
    func configureMessageCollectionView() {
        messagesCollectionView.messagesDataSource = self
        //messagesCollectionView.messageCellDelegate = self
        messagesCollectionView.messagesLayoutDelegate = self
        messagesCollectionView.messagesDisplayDelegate = self
        scrollsToBottomOnKeyboardBeginsEditing = true // default false
        maintainPositionOnKeyboardFrameChanged = true // default false
        
        messagesCollectionView.addSubview(refreshControl)
        refreshControl.addTarget(self, action: #selector(loadMoreMessages), for: .valueChanged)
    }
    
    func configureMessageInputBar() {
        messageInputBar.delegate = self
        messageInputBar.inputTextView.tintColor = UIColor.blue
        messageInputBar.sendButton.setTitleColor(UIColor.blue.withAlphaComponent(0.7), for: .normal)
        messageInputBar.sendButton.setTitleColor(
            UIColor.blue.withAlphaComponent(0.3),
            for: .highlighted
        )
    }
    
    // MARK: - Helpers
    
    func insertMessage(_ message: Message) {
        messageList.append(message)
        // uad last section to update header/footer labels and insert a new one
        messagesCollectionView.performBatchUpdates({
            messagesCollectionView.insertSections([messageList.count - 1])
            if messageList.count >= 2 {
                messagesCollectionView.reloadSections([messageList.count - 2])
            }
        }, completion: { [weak self] _ in
            if self?.isLastSectionVisible() == true {
                self?.messagesCollectionView.scrollToBottom(animated: true)
            }
        })
    }
    
    func isLastSectionVisible() -> Bool {
        
        guard !messageList.isEmpty else { return false }
        
        let lastIndexPath = IndexPath(item: 0, section: messageList.count - 1)
        
        return messagesCollectionView.indexPathsForVisibleItems.contains(lastIndexPath)
    }
    
    // MARK: - MessagesDataSource
    
    func currentSender() -> SenderType {
        return self.currentUser!
    }
    
    func numberOfSections(in messagesCollectionView: MessagesCollectionView) -> Int {
        return messageList.count
    }
    
    func messageForItem(at indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> MessageType {
        return messageList[indexPath.section]
    }
    
    func cellTopLabelAttributedText(for message: MessageType, at indexPath: IndexPath) -> NSAttributedString? {
        if indexPath.section % 3 == 0 {
            return NSAttributedString(string: MessageKitDateFormatter.shared.string(from: message.sentDate), attributes: [NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: 12), NSAttributedString.Key.foregroundColor: UIColor.darkGray])
        }
        return nil
    }
    
    func cellBottomLabelAttributedText(for message: MessageType, at indexPath: IndexPath) -> NSAttributedString? {
        
        return NSAttributedString(string: "Read", attributes: [NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: 10), NSAttributedString.Key.foregroundColor: UIColor.darkGray])
    }
    
    func messageTopLabelAttributedText(for message: MessageType, at indexPath: IndexPath) -> NSAttributedString? {
        let name = message.sender.displayName
        return NSAttributedString(string: name, attributes: [NSAttributedString.Key.font: UIFont.preferredFont(forTextStyle: .caption1)])
    }

    
}


// MARK: - MessageInputBarDelegate
extension MessageViewController: InputBarAccessoryViewDelegate {
    
    override var canBecomeFirstResponder: Bool {
        get {
            return true
        }
    }
    
    func inputBar(_ inputBar: InputBarAccessoryView, didPressSendButtonWith text: String) {
        
        // Message
        let message = Message.init(messageId: UUID().uuidString, user: self.currentUser!, textMessage: text)
        let address = self.wallet?.address
        // Clear input text and insert message
        inputBar.inputTextView.text = String()
        self.insertMessage(message)
        self.messagesCollectionView.scrollToBottom(animated: true)
        
        let alertController = UIAlertController(title: "Wait", message: "Do you want to send the message?", preferredStyle: .alert)
        let action = UIAlertAction(title: "Yes", style: .default) { (UIAlertAction) in
            do {
       
                
                // Params
                var params: [AnyObject] = []
                params.append(address as AnyObject)
                params.append(String(text) as AnyObject)
                
                //executeFunction class
                try self.ethContract?.executeFunction(functionName: "sendMessage", wallet: self.wallet!, functionParams: params, nonce: nil, gas: BigUInt(200000), gasPrice: BigUInt(10000000000), value: 0, callback: { (error, result) in
                    if error != nil {
                        print("Failed to send message with error: \(error!)")
                        return
                    }
                    print("\(result ?? "none")")
                })
            } catch {
                print(error)
            }
        }
        let cancelAction = UIAlertAction(title: "Cancel", style: .destructive, handler: nil)
        alertController.addAction(action)
        alertController.addAction(cancelAction)
        self.present(alertController, animated: true, completion: nil)
        
    }
}
