//
//  User.swift
//  Pocket-Chat
//
//  Created by Pabel Nunez Landestoy on 4/23/19.
//  Copyright © 2019 Pocket_Sample. All rights reserved.
//

import Foundation
import MessageKit

struct User: SenderType, Equatable {
    var senderId: String
    var displayName: String
    
    init(senderId: String, displayName: String) {
        self.senderId = senderId
        self.displayName = String(displayName.prefix(4)) + "..." + String(displayName.suffix(4))
    }
}
