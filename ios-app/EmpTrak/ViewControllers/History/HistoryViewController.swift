//
//  HistoryViewController.swift
//  EmpTrak
//
//  Created by Amarendra on 12/4/17.
//  Copyright © 2017 Akwa. All rights reserved.
//

import UIKit

class HistoryViewController: UIViewController {
    
    @IBAction func btnGo(sender: Any) {
        fetchHistory()
    }
    @IBOutlet weak var vwSave: UIView!
    @IBOutlet weak var txtTo: UITextField!
    
    @IBOutlet weak var tblHistory: UITableView!
    @IBOutlet weak var txtFrom: UITextField!
    var datePickerObject = UIDatePicker()
    var textFieldSelected : UITextField!
    var arrayData:[Dictionary<String,AnyObject>]?
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = TitleName.History.rawValue
        customizeNavigationforAll(self)
        self.navigationController?.navigationBar.isTranslucent = false
        // Do any additional setup after loading the view.
        let nib = UINib(nibName: "DetailTableViewCell", bundle: nil)
        tblHistory.register(nib, forCellReuseIdentifier: "detailTableViewCell")
        NotificationCenter.default.addObserver(self, selector: #selector(SettingViewController.keyboardWillShow), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(SettingViewController.keyboardWillHide), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
        let tap =  UITapGestureRecognizer(target: self, action: #selector(hidePicker))
        tap.numberOfTapsRequired = 1
        self.view.addGestureRecognizer(tap)
        arrayData = [Dictionary<String,AnyObject>]()
        setUpPicker()
        self.vwSave.layer.cornerRadius = 5.0
    }
    
    func setUpPicker(){
        self.datePickerObject.datePickerMode = .date
        self.datePickerObject.backgroundColor = UIColor.white
        self.txtFrom.inputView = self.datePickerObject
        self.txtTo.inputView = self.datePickerObject
        self.datePickerObject.addTarget(self, action: #selector(handleDatePicker(_:)), for: UIControlEvents.valueChanged)
    }
    func handleDatePicker(_ sender: UIDatePicker) {
        datePickerObject = sender
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "YYYY-MM-dd"
        textFieldSelected.text = dateFormatter.string(from: sender.date)
    }
    @objc func hidePicker(){
        self.textFieldSelected?.resignFirstResponder()
    }
    
    func keyboardWillShow(notification: NSNotification) {
        //  let keyboardSize = (notification.userInfo![UIKeyboardFrameEndUserInfoKey] as! NSValue).cgRectValue.size
        //        if self.view.frame.origin.y == 64{
        //            self.view.frame.origin.y -= keyboardSize.height
        //        }
        
    }
    
    func keyboardWillHide(notification: NSNotification) {
        //     let keyboardSize = (notification.userInfo![UIKeyboardFrameEndUserInfoKey] as! NSValue).cgRectValue.size
        //        if self.view.frame.origin.y != 64{
        //            self.view.frame.origin.y += keyboardSize.height
        //        }
        
    }
    func sortButtonClicked(_ sender : AnyObject){
        
        //        let VW = STRSearchViewController(nibName: "STRSearchViewController", bundle: nil)//        self.navigationController?.pushViewController(VW, animated: true)
    }
    func toggleSideMenu(_ sender: AnyObject) {
        self.revealViewController().revealToggle(animated: true)
        
        
    }
    func backToDashbaord(_ sender: AnyObject) {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        appDelegate.initSideBarMenu()
        
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func getData()->(){
        var loadingNotification = MBProgressHUD.showAdded(to: self.view, animated: true)
        loadingNotification?.mode = MBProgressHUDMode.indeterminate
        loadingNotification?.labelText = "Loading"
        let generalApiobj = GeneralAPI()
        generalApiobj.hitApiwith(["to":txtTo.text as AnyObject,"from":txtFrom.text as AnyObject], serviceType: .strGetInOutHistory, success: { (response) in
            DispatchQueue.main.async {
                print(response)
                if(response["code"] as! NSInteger !=  200)
                {
                    MBProgressHUD.hideAllHUDs(for: self.view, animated: true)
                    loadingNotification = nil
                    utility.createAlert(TextMessage.alert.rawValue, alertMessage: "\(response["message"] as! String)", alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
                    return
                }
                guard let data = response["data"] as? [Dictionary<String,AnyObject>]else{
                    MBProgressHUD.hideAllHUDs(for: self.view, animated: true)
                    utility.createAlert(TextMessage.alert.rawValue, alertMessage: TextMessage.tryAgain.rawValue, alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
                    return
                }
                self.arrayData?.removeAll()
                self.arrayData?.append(contentsOf: data)
                self.tblHistory.reloadData()
                self.hidePicker()
                MBProgressHUD.hideAllHUDs(for: self.view, animated: true)
                
            }
        }) { (err) in
            DispatchQueue.main.async {
                MBProgressHUD.hideAllHUDs(for: self.view, animated: true)
                utility.createAlert(TextMessage.alert.rawValue, alertMessage: TextMessage.tryAgain.rawValue, alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
                NSLog(" %@", err)
            }
        }
        
    }
    func addNodata(){
        let noData = Bundle.main.loadNibNamed("STRNoDataFound", owner: nil, options: nil)!.last as! STRNoDataFound
        noData.tag = 10002
        self.view.addSubview(noData)
        noData.translatesAutoresizingMaskIntoConstraints = false
        self.view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "V:|-(160)-[noData]-(0)-|", options: NSLayoutFormatOptions(rawValue: 0), metrics: nil, views: ["noData" : noData]))
        self.view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "H:|-(0)-[noData]-(0)-|", options: NSLayoutFormatOptions(rawValue: 0), metrics: nil, views: ["noData" : noData]))
        
    }
    
    func fetchHistory(){
        if validate(){
            getData()
        }
        
    }
    func validate()->Bool{
        
        if(txtTo.text == "" && txtFrom.text == "" )
        {
            utility.createAlert(TextMessage.alert.rawValue, alertMessage: "Please enter valid To date and From date", alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
            return false
            
        }
        if(txtTo.text == "")
        {
            utility.createAlert(TextMessage.alert.rawValue, alertMessage: "Please enter valid To date", alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
            return false

        }
        if(txtFrom.text == "")
        {
            utility.createAlert(TextMessage.alert.rawValue, alertMessage: "Please enter valid From date", alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
            return false
            
        }
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "YYYY-MM-dd"
        
        let dateTo = dateFormatter.date(from: txtTo.text!)
        let dateFrom  = dateFormatter.date(from: txtFrom.text!)
        if dateTo?.compare(dateFrom!) == .orderedAscending{
              utility.createAlert(TextMessage.alert.rawValue, alertMessage: "To date should be greater than from date", alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
            return false
        }
        
        return true
    }
    
}

extension HistoryViewController:UITextFieldDelegate{
    public func textFieldDidBeginEditing(_ textField: UITextField)
    {
        if textField == txtFrom {
            textFieldSelected = txtFrom
        }
        if textField == txtTo {
            textFieldSelected = txtTo
        }
    }
    
}

extension HistoryViewController : UITableViewDataSource{
    func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if arrayData == nil || arrayData?.count == 0 {
            addNodata()
            return 0
        }
        for view in self.view.subviews{
            if(view.tag == 10002)
            {
                view.removeFromSuperview()
            }
            self.view.viewWithTag(10002)?.removeFromSuperview()
        }
            return (arrayData?.count)!
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: DetailTableViewCell = self.tblHistory.dequeueReusableCell(withIdentifier: "detailTableViewCell") as! DetailTableViewCell
        
        cell.setUpCell(arrayData?[indexPath.row],historyCell: true)
        
        
        cell.selectionStyle  = UITableViewCellSelectionStyle.none
        
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        
        
        return  80
    }
}







