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
   
    //call wallet and pocketaion variable
    var wallet: Wallet?
    var pocketEth: PocketEth?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //create PocketAion instance.
        do{
            pocketEth = try PocketEth.init(devID: DeveloperConfig.devID, netIds: [PocketEth.Networks.Rinkeby.netID,PocketEth.Networks.Mainnet.netID])
            
        } catch{
            print(error)
        }
        
    }
    
    @IBAction func getStarted(_ sender: Any) {
        
        //get account from recordKey
       let keys = Wallet.getWalletsRecordKeys()
        
        if keys.count > 0 {
            let address = keys.last?.split(separator: "/").last
            // Request passphrase for saved wallet
            let alertController = self.requestPassphrase { (passphrase, error) in
                if passphrase != nil {
                    do {
                        //Retrieve wallet
                        self.wallet = try Wallet.retrieveWallet(network: PocketEth.NETWORK, netID: (self.pocketEth?.rinkeby!.netID)!, address: String(address!), passphrase: String(passphrase!))
                        
                        // Instantiate MessageViewController
                        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                        let vc = storyboard.instantiateViewController(withIdentifier: "messageViewControllerID") as? MessageViewController
                        vc?.pocketEth = self.pocketEth
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
                    vc?.pocketEth = self.pocketEth
                    self.present(vc!, animated: true, completion: nil)
                }
            }
            self.present(alertController, animated: false, completion: nil)
        }else {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let vc = storyboard.instantiateViewController(withIdentifier: "importWalletViewControllerID") as? ImportWalletViewController
            vc?.pocketEth = self.pocketEth
            self.present(vc!, animated: true, completion: nil)
        }
    }
}

