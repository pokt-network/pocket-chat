//
//  CreateWalletViewController.swift
//  Pocket-Chat
//
//  Created by jeremy beal on 4/18/19.
//  Copyright © 2019 Pocket_Sample. All rights reserved.
//

import Foundation
import PocketSwift


class CreateWalletViewController: UIViewController {
    
    // declate wallet and PocketAion
    var wallet:Wallet?
    var pocketEth:PocketEth?
    
    
    
    @IBOutlet weak var publicKeyLabel: UILabel!
    @IBOutlet weak var privateKetyTextView: UITextView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        do{
            pocketEth = try PocketEth.init(devID: DeveloperConfig.devID, netIds: [PocketEth.Networks.Rinkeby.netID,PocketEth.Networks.Mainnet.netID])
            
            
        } catch{
            print(error)
        }
     
        
    }
    
    @IBAction func createWallet(_ sender: Any) {
        
        //ensure the labels are empty
        if publicKeyLabel.text?.isEmpty ?? true || privateKetyTextView.text?.isEmpty ?? true {
            
            //generate the public and private keys
            do {
                // TODO: call Create Wallet class
                 wallet = try pocketEth?.rinkeby?.createWallet()

                //TODO: display wallet address and private key
                self.publicKeyLabel.text = wallet?.address
                
                self.privateKetyTextView.text = wallet?.privateKey
                
            } catch {
                let alertController = UIAlertController(title: "Error", message: "Failed to create a wallet, please try again later.", preferredStyle: .alert)
                self.present(alertController, animated: true, completion: nil)
            }
        }else {
            let alertController = UIAlertController(title: "Wait", message: "Do you want to create a new wallet besides the one already presented?", preferredStyle: .alert)
            let action1 = UIAlertAction(title: "Yes", style: .default) { (action:UIAlertAction) in
                self.publicKeyLabel.text = ""
                self.privateKetyTextView.text = ""
                self.createWallet(self)
                self.dismiss(animated: false, completion: nil)
            }
            
            let action2 = UIAlertAction(title: "Cancel", style: .destructive) { (action:UIAlertAction) in
                self.dismiss(animated: false, completion: nil)
            }
            
            alertController.addAction(action1)
            alertController.addAction(action2)
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    @IBAction func continueToMVC(_ sender: Any) {
        let alertController = self.requestPassphrase { (passphrase, error) in
            if passphrase != nil {
                do {
                    let isSaved = try self.wallet?.save(passphrase: passphrase!)
                    if isSaved ?? false {
                        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                        let vc = storyboard.instantiateViewController(withIdentifier: "messageViewControllerID")
                        self.present(vc, animated: true, completion: nil)
                    }
                } catch {
                    print(error)
                }
            }else if error != nil{
                let alertController = UIAlertController(title: "Error", message: "Failed to save wallet, please try again later", preferredStyle: .alert)
                self.present(alertController, animated: true, completion: nil)
            }else{
                let storyboard = UIStoryboard(name: "Main", bundle: nil)
                let vc = storyboard.instantiateViewController(withIdentifier: "messageViewControllerID")
                self.present(vc, animated: true, completion: nil)
            }
        }
        self.present(alertController, animated: false, completion: nil)
    }
    
}
