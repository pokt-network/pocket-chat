//
//  AppConfig.swift
//  Pocket-Chat
//
//  Created by Pabel Nunez Landestoy on 4/24/19.
//  Copyright © 2019 Pocket_Sample. All rights reserved.
//

import Foundation

public class AppConfig: NSObject {
    
    public static let smartContractAddress = "0x0b4515fa7e8287f2da3e8776a239a5d6a493b878"
    public static let abiDefinition = "[{\"constant\":true,\"inputs\":[{\"name\":\"_index\",\"type\":\"uint256\"}],\"name\":\"getMessageByIndex\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"getTotalMessageCount\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_sender\",\"type\":\"address\"},{\"name\":\"_content\",\"type\":\"string\"}],\"name\":\"sendMessage\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"
}
