//
//  ViewController.swift
//  Pocket-Chat
//
//  Created by jeremy beal on 4/18/19.
//  Copyright © 2019 Pocket_Sample. All rights reserved.
//

import UIKit

class WelcomeVC: UIViewController {

  
    @IBAction func GetStarted(_ sender: Any) {
        
        let savedData = UserDefaults.standard.string(forKey: "privKey")
        if((savedData) != nil){
            performSegue(withIdentifier: "signedIn", sender:self)//here u have decide the which view will show if the user is logged in how. here i used   segue.
            
            let key = UserDefaults.standard.string(forKey: "privKey")
            print(key)
            
        }else{
            performSegue(withIdentifier: "toLogin", sender: self)// this is the main view. just make the object of the class and called it.
        }
        
        // transition to login screen
        //performSegue(withIdentifier: "toLogin", sender: self)
        
    }
    
    


}

