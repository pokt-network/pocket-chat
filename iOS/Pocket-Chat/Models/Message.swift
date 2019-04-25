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

struct Message: MessageType {

    var messageId: String
    var user: User
    let textMessage: String
    var sender: SenderType {
        return user
    }
    var sentDate: Date {
        return Date()
    }
    var kind: MessageKind {
        return .text(textMessage)
    }
}
