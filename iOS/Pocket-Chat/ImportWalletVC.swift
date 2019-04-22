//
//  ImportWalletVC.swift
//  Pocket-Chat
//
//  Created by jeremy beal on 4/18/19.
//  Copyright © 2019 Pocket_Sample. All rights reserved.
//

import Foundation
import UIKit

class ImportWalletVC: UIViewController {
    
    @IBOutlet weak var private_Key_text: UITextField!
    
    
    @IBOutlet weak var maybeError: UILabel!
    
    
    
    
    @IBAction func importButton(_ sender: Any) {
        
        let pkString: String = private_Key_text.text!
        
        UserDefaults.standard.set(pkString, forKey: "privKey")

        UserDefaults.standard.synchronize()
        
        //TODO add import wallet feature here
        
        
        
        performSegue(withIdentifier: "toMessage", sender: self)
        
        
        // address 0xB0fEE26f7671Be33ab5C2eCB82208123535FFAc3
        // pk 5fc239d9f12299cf910786840fc8779d771a12fbd69bd2a036a84b3a79960354
        
        
    }
    
    @IBAction func createWalletButton(_ sender: Any) {
        
        performSegue(withIdentifier: "toWalletVC", sender: self)
        
    }
    
    
    
}
