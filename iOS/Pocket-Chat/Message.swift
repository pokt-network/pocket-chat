//
//  Message.swift
//  Pocket-Chat
//
//  Created by jeremy beal on 4/18/19.
//  Copyright © 2019 Pocket_Sample. All rights reserved.
//

import Foundation
import UIKit
import MessageKit
import PocketSwift

// store the messages you want to display
// create the models that will store our messages
// not really needed, just used to test UI

struct Member {
    let address : String
}

struct Message {
    let member: Member
    let textMessage: String
    let messageId: String
}



extension Message: MessageType {
    var sender: Sender {
        return Sender(id: member.address, displayName: member.address)
    }
    
    var sentDate: Date {
        return Date()
    }
    
    var kind: MessageKind {
        return .text(textMessage)
    }

}
