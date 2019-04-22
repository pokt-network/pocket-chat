//
//  RandomMessage.swift
//  Pocket-Chat
//
//  Created by jeremy beal on 4/19/19.
//  Copyright © 2019 Pocket_Sample. All rights reserved.
//

import Foundation
import MessageKit

// store the messages you want to display
// create the models that will store our messages
// not really needed, just used to test UI

struct RandomMessage {
    let textMessage: String
    let messageId: String
}

extension RandomMessage: MessageType {
    var sender: Sender {
        return Sender(id: messageId, displayName: messageId)
    }
    
    var sentDate: Date {
        return Date()
    }
    
    var kind: MessageKind {
        return .text(textMessage)
    }
    
}
