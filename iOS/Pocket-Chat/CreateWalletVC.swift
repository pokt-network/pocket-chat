//
//  CreateWalletVC.swift
//  Pocket-Chat
//
//  Created by jeremy beal on 4/18/19.
//  Copyright © 2019 Pocket_Sample. All rights reserved.
//

import Foundation
import PocketSwift


class CreateWalletVC: UIViewController {
    
    @IBOutlet weak var pub_KeyL: UILabel!
    
    @IBOutlet weak var priv_KeyL: UILabel!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        let pocketAion = try? PocketAion.init(devID: nil, netIds: ["32","356"], defaultNetID: "32", maxNodes: 5, requestTimeOut: 20000)
        
        
        do {
            
            let wallet = try?pocketAion!.mastery!.createWallet()
            
            DispatchQueue.main.async {
                self.pub_KeyL.text = wallet?.address
                
                self.priv_KeyL.text = wallet?.privateKey
            }
            
        } catch {
            print(error)
        }
        
    }
    
    
    @IBAction func continueToMVC(_ sender: Any) {
        
        performSegue(withIdentifier: "toMessage", sender: self)
        
    }
    
    
    @IBAction func backToImport(_ sender: Any) {
        
        performSegue(withIdentifier: "backToImport", sender: self)
        
    }
    
    
    
}
