//
//  WelcomeViewController.swift
//  Pocket-Chat
//
//  Created by jeremy beal on 4/18/19.
//  Copyright © 2019 Pocket_Sample. All rights reserved.
//

import UIKit
import PocketSwift

class WelcomeViewController: UIViewController {
    var wallet: Wallet?
    var pocketAion: PocketAion?
    override func viewDidLoad() {
        super.viewDidLoad()
        do {
            // Instantiate PocketAion
            pocketAion = try PocketAion.init(devID: "", netIds: ["32","256"], defaultNetID: "32", maxNodes: 5, requestTimeOut: 20000)
        } catch {
            print(error)
        }
    }
    
    @IBAction func getStarted(_ sender: Any) {
        // Try to get account from keystore
        let keys = Wallet.getWalletsRecordKeys()
        
        if keys.count > 0 {
            let address = keys.last?.split(separator: "/").last
            // Request passphrase for saved wallet
            let alertController = self.requestPassphrase { (passphrase, error) in
                if passphrase != nil {
                    do {
                        // Retrieve wallet
                        self.wallet = try Wallet.retrieveWallet(network: "AION", netID: "32", address: String(address!), passphrase: passphrase!)
                        // Instantiate MessageViewController
                        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                        let vc = storyboard.instantiateViewController(withIdentifier: "messageViewControllerID") as? MessageViewController
                        vc?.pocketAion = self.pocketAion
                        vc?.wallet = self.wallet
                        self.present(vc!, animated: true, completion: nil)
                    } catch {
                        let alertController = UIAlertController(title: "Error", message: "Failed to retrieve wallet, please try again later", preferredStyle: .alert)
                        let action = UIAlertAction(title: "Ok", style: .default, handler: nil)
                        alertController.addAction(action)
                        self.present(alertController, animated: true, completion: nil)
                    }
                }else if error != nil {
                    let alertController = UIAlertController(title: "Error", message: "Failed to retrieve passphrase with error: \(error!)", preferredStyle: .alert)
                    let action = UIAlertAction(title: "Ok", style: .default, handler: nil)
                    alertController.addAction(action)
                    self.present(alertController, animated: true, completion: nil)
                }else {
                    let storyboard = UIStoryboard(name: "Main", bundle: nil)
                    let vc = storyboard.instantiateViewController(withIdentifier: "importWalletViewControllerID") as? ImportWalletViewController
                    vc?.pocketAion = self.pocketAion
                    self.present(vc!, animated: true, completion: nil)
                }
            }
            self.present(alertController, animated: false, completion: nil)
        }else {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let vc = storyboard.instantiateViewController(withIdentifier: "importWalletViewControllerID") as? ImportWalletViewController
            vc?.pocketAion = self.pocketAion
            self.present(vc!, animated: true, completion: nil)
        }
    }
}

