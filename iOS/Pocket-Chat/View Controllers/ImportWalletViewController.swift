//
//  ImportWalletViewController.swift
//  Pocket-Chat
//
//  Created by jeremy beal on 4/18/19.
//  Copyright © 2019 Pocket_Sample. All rights reserved.
//

import Foundation
import UIKit
import PocketSwift

class ImportWalletViewController: UIViewController {
    var wallet: Wallet?
    var pocketAion: PocketAion?
    
    @IBOutlet weak var privateKeyTextView: UITextView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    func saveWallet() {
        let alertController = self.requestPassphrase { (passphrase, error) in
            if passphrase != nil {
                do {
                    let isSaved = try self.wallet?.save(passphrase: passphrase!)
                    if isSaved ?? false {
                        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                        let vc = storyboard.instantiateViewController(withIdentifier: "messageViewControllerID") as? MessageViewController
                        vc?.pocketAion = self.pocketAion
                        vc?.wallet = self.wallet
                        self.present(vc!, animated: true, completion: nil)
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
    
    @IBAction func importButton(_ sender: Any) {
        if !privateKeyTextView.text.isEmpty {
            do {
                self.wallet = try pocketAion?.mastery?.importWallet(privateKey: privateKeyTextView.text)
                if self.wallet != nil {
                    let saveAlertController = UIAlertController(title: "Account", message: "Wallet import was a sucess, do you want to SAVE the account?", preferredStyle: .alert)
                    
                    let saveAction1 = UIAlertAction(title: "Yes", style: .default) { (UIAlertAction) in
                        self.saveWallet()
                    }
                    let saveAction2 = UIAlertAction(title: "No", style: .destructive) { (UIAlertAction) in
                        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                        let vc = storyboard.instantiateViewController(withIdentifier: "messageViewControllerID") as? MessageViewController
                        vc?.pocketAion = self.pocketAion
                        vc?.wallet = self.wallet
                        self.present(vc!, animated: true, completion: nil)
                    }
                    saveAlertController.addAction(saveAction1)
                    saveAlertController.addAction(saveAction2)
                    
                    self.present(saveAlertController, animated: false, completion: nil)
                }else {
                    let alertController = UIAlertController(title: "Error", message: "Failed to import wallet, please verify the private key", preferredStyle: .alert)
                    let action = UIAlertAction(title: "Ok", style: .default, handler: nil)
                    alertController.addAction(action)
                    self.present(alertController, animated: true, completion: nil)
                }
            } catch {
                let alertController = UIAlertController(title: "Error", message: "Invalid private key provided.", preferredStyle: .alert)
                let action = UIAlertAction(title: "Ok", style: .default, handler: nil)
                alertController.addAction(action)
                self.present(alertController, animated: true, completion: nil)
            }
        }else {
            let alertController = UIAlertController(title: "Warning", message: "No private key was provided", preferredStyle: .alert)
            let action = UIAlertAction(title: "Ok", style: .default, handler: nil)
            alertController.addAction(action)
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    @IBAction func createWalletButton(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "createWalletViewControllerID")
        self.present(vc, animated: true, completion: nil)
    }
}
