//
//  ViewController+Extension.swift
//  Pocket-Chat
//
//  Created by Pabel Nunez Landestoy on 4/24/19.
//  Copyright © 2019 Pocket_Sample. All rights reserved.
//

import UIKit

extension UIViewController {
    public typealias passphraseRequestHandler = (_: String?, _: Error?) -> Void

    func requestPassphrase(handler: @escaping passphraseRequestHandler) -> UIAlertController {
        let alert = UIAlertController(title: "Wallet passphrase", message: "Please input a wallet passphrase", preferredStyle: .alert)
        alert.addTextField { (textField) in
            textField.placeholder = "Passphrase"
            textField.isSecureTextEntry = true
        }
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { (UIAlertAction) in
            if let passphraseTextField = alert.textFields?.first {
                handler(passphraseTextField.text, nil)
            }else {
                handler(nil, "Failed to retrieve passphraseTextField" as? Error)
            }
        }))
        
        return alert
    }
    
    func showErrorWith(title: String, message: String) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        let action = UIAlertAction(title: "Ok", style: .default, handler: nil)
        
        alert.addAction(action)
        
        self.present(alert, animated: false, completion: nil)
    }
}
