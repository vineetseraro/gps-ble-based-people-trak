//
//  DetailTableViewCell.swift
//  EmpTrak
//
//  Created by Nitin Singh on 27/11/17.
//  Copyright © 2017 Akwa. All rights reserved.
//

import UIKit

class DetailTableViewCell: UITableViewCell {

    @IBOutlet weak var imageVw: UIImageView!
    @IBOutlet weak var outLabel: UILabel!
    @IBOutlet weak var intimeLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        setupFont()
    }
    func setupFont(){
        nameLabel.font = UIFont(name: "SourceSansPro-Semibold", size: 18.0);
        intimeLabel.font =  UIFont(name: "SourceSansPro-Regular", size: 12.0);
        outLabel.font = UIFont(name: "SourceSansPro-Regular", size: 10.0);
        
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func setUpCell(_ dict:Dictionary<String,AnyObject>? ,historyCell:Bool) {
        if historyCell{
            nameLabel.text = dict!["date"] as? String
            intimeLabel.text = "In:" + " \(dict!["firstIn"] as! String)"
            outLabel.text = "Out:" + " \(dict!["lastOut"] as! String)"
        }
        else{
            nameLabel.text = dict!["name"] as? String
            intimeLabel.text = "In:" + " \(dict!["entryTime"] as! String)"
            outLabel.text = "Out:" + " \(dict!["exitTime"] as! String)"
        }
    }
}
