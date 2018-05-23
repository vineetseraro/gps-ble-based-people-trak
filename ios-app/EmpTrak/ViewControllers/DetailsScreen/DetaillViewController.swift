
import UIKit

import Crashlytics
import AKProximity
// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l < r
  case (nil, _?):
    return true
  default:
    return false
  }
}

// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func > <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l > r
  default:
    return rhs < lhs
  }
}


class DetaillViewController: UIViewController,UIWebViewDelegate,STRNoDataFoundDelegate, UITableViewDelegate, UITableViewDataSource{
    @IBOutlet var vwBase: UIView!
    @IBOutlet var lblNavigation: UILabel!
    var locationID : String!
    var cellTapped:Bool = true
    var currentRow : Int?
    var dateString : String!
    var responseData :((Dictionary<String,AnyObject>)->())?
    var selectedImage: UIImage?
    var isEditEnable : Bool = false
    
    var isSelectedEnable : Bool = false
    
     @IBOutlet var bottonHeight: NSLayoutConstraint!
    @IBOutlet var buttonTask: UIButton!
    var arrayHeightObj = [CGFloat]()
    
    @IBAction func btnSideMenu(_ sender: AnyObject) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func btnSearch(_ sender: AnyObject) {
//        let VW = STRSearchViewController(nibName: "STRSearchViewController", bundle: nil)
//        self.navigationController?.pushViewController(VW, animated: true)
    }
    
    @IBAction func btnTask(_ sender: AnyObject) {
        
    }
    
    @IBAction func btnEdit(_ sender: UIButton) {
        var isStatus : Bool = false
        self.arrayOfSelectedIndex?.removeAll()
        
        let aStr = String(format: "%d Selected", (self.arrayOfSelectedIndex?.count)!)
        self.lblSelectedCount.text = aStr
        for (index,dict) in (self.arrayData?.enumerated())!{
            let status = dict["shipStatus"] as? Int
            if(status == 10)
            {
                isStatus = true
                
            }
            
            if (status == 20)
            {
                isStatus = true
            }
            
            
        }
        
        if isStatus {
            isEditEnable  = true
            vwBase2.isHidden =  false
            self.setUpSelectionUI()
            currentRow = nil
            self.tblView.reloadData()
        }else{
             utility.createAlert(TextMessage.alert.rawValue, alertMessage: TextMessage.noNewShipment.rawValue, alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
        }
       
    }
    
    @IBAction func btnCancel(_ sender: UIButton) {
        self.arrayOfSelectedIndex?.removeAll()
        vwBase2.isHidden =  true
        isEditEnable  = false
        self.tblView.reloadData()
    }
    
    
    
    @IBAction func btnSelectAll(_ sender: AnyObject) {
        if(self.arrayOfSelectedIndex?.count > 0)
        {
            self.arrayOfSelectedIndex?.removeAll()
        }
        else{
            for (index,dict) in (self.arrayData?.enumerated())!{
                let status = dict["shipStatus"] as? Int
              if(status == 10)
              {
               let path = IndexPath(item: index, section: 0)
                self.arrayOfSelectedIndex?.append(path)
              }
             if(status == 20)
                {
                    let path = IndexPath(item: index, section: 0)
                    self.arrayOfSelectedIndex?.append(path)
                }
            }
        }
        self.setUpSelectionUI()
        self.tblView.reloadData()
    }
    @IBOutlet var vwTop: UIView!
    //MARK: data stores
    var arrayData: [Dictionary<String,AnyObject>]?
    var mapData: Dictionary<String,AnyObject>?
    var dateSelected:Date?
    var selectedStateChangeIndex: IndexPath?
    var selectedIndex: IndexPath?
    
    var editMode:Bool?
    var arrayOfSelectedIndex:[IndexPath]?
    @IBOutlet var webViewMap: UIWebView!
    @IBOutlet var tblView: UITableView!
    @IBOutlet var lblTitle: UILabel!
    @IBOutlet var lblSubTitle: UILabel!
    
    @IBOutlet var vwBase2: UIView!
    
    @IBOutlet var lblSelectedCount: UILabel!
    
    @IBOutlet var btnStart: UIButton!
    
    @IBOutlet var lblStart: UILabel!
    
    @IBOutlet var imgSelect: UIImageView!
    
    
    
    
    @IBAction func btnStart(_ sender: AnyObject) {
        self.changeStateBulk()
    }
    
    
    override var preferredStatusBarStyle : UIStatusBarStyle {
        
        
        return UIStatusBarStyle.lightContent
        
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        addKeyboardNotifications()
        
         NotificationCenter.default.post(Notification(name: Notification.Name(rawValue: "UPDATEPROFILENOTIFICATION"), object: nil))
        
        
        arrayOfSelectedIndex = [IndexPath]()
        self.editMode = false
        self.navigationController?.navigationBar.isHidden = true
       // dateSelected = Date()
       
        let order = (Calendar.current as NSCalendar).compare(Date(), to: dateSelected!,
                                                             toUnitGranularity: .day)
        if(order == .orderedSame)
        {
            lblTitle.text="TODAY"
        }
        else
        {
            lblTitle.text=returnDisplayFormatedDate()
        }
        let nib = UINib(nibName: "DetailTableViewCell", bundle: nil)
        tblView.register(nib, forCellReuseIdentifier: "detailTableViewCell")
        self.revealViewController().panGestureRecognizer().isEnabled = true
        self.view .addGestureRecognizer(self.revealViewController().tapGestureRecognizer())
        

        
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        arrayOfSelectedIndex?.removeAll()
        self.navigationController?.navigationBar.isHidden = true
        cellTapped = true
        
            getHomeData()
       
    }
    
    func refresh(_ refreshControl:UIRefreshControl){
        refreshControl.endRefreshing()
        cellTapped = true
        self.getHomeData()
    }
    func setUpFont(){
        self.lblNavigation.font =  UIFont(name: "Roboto-Light", size: 20)!
        self.lblTitle.font =  UIFont(name: "SourceSansPro-Semibold", size:22)
        self.lblSelectedCount.font = UIFont(name: "SourceSansPro-Semibold", size: 10.0);
        self.lblStart.font = UIFont(name: "SourceSansPro-Semibold", size: 12.0);
        
        
    }
    func poptoPreviousScreen(){
        self.navigationController?.popViewController(animated: true)
    }
    
    func customizeNavigation(_ ref : UIViewController) {
        
        ref.navigationController?.navigationBar.isTranslucent = false
        let button: UIButton = UIButton.init()
        
        button.setImage(UIImage(named: "back"), for: UIControlState())
        button.addTarget(ref, action: #selector(DetaillViewController.poptoPreviousScreen), for: UIControlEvents.touchUpInside)
        button.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        
        let barButton = UIBarButtonItem(customView: button)
        ref.navigationItem.leftBarButtonItem = barButton
        
        
        let buttonSearch: UIButton = UIButton.init()
        buttonSearch .setTitle("Edit", for: UIControlState())
        buttonSearch.titleLabel!.font =  UIFont(name: "SourceSansPro-Regular", size: 10)
      
        buttonSearch.addTarget(ref, action: #selector(DetaillViewController.barButtonItemClicked(_:)), for: UIControlEvents.touchUpInside)
        //set frame
        buttonSearch.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        
        let buttonSort: UIButton = UIButton.init()
        
        buttonSort.setImage(UIImage(named: ""), for: UIControlState())
        //add function for button
        buttonSort.addTarget(ref, action: #selector(DetaillViewController.sortButtonClicked(_:)), for: UIControlEvents.touchUpInside)
        buttonSort.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        
        
        
        let barButtonSearch = UIBarButtonItem(customView: buttonSearch)
        let barButtonSort = UIBarButtonItem(customView: buttonSort)
        //assign button to navigationbar
        ref.navigationItem.rightBarButtonItems = [barButtonSearch , barButtonSort]
        
        
        
        
        
            }
    func barButtonItemClicked(_ sender : AnyObject){
      
    }
    
    func sortButtonClicked(_ sender : AnyObject){
        
        
        
//        let VW = STRSearchViewController(nibName: "STRSearchViewController", bundle: nil)
//        self.navigationController?.pushViewController(VW, animated: true)

    }
    func toggleSideMenu(_ sender: AnyObject) {
        
        self.revealViewController().revealToggle(animated: true)
        
    }
    
    var firstNameStr : String!
    var lastNameStr : String!
    var phoneNumberSTr : String!
    var countryCodeSTr : String!
    //MARK: table view delegate
   
    func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {

        if(arrayData != nil && arrayData?.count != 0){
        return (arrayData?.count)!
        }
        return 0
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: DetailTableViewCell = self.tblView.dequeueReusableCell(withIdentifier: "detailTableViewCell") as! DetailTableViewCell
        
        cell.setUpCell(arrayData?[indexPath.row] ,historyCell: false)
       
        
        cell.selectionStyle  = UITableViewCellSelectionStyle.none
        
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
       
     
        return  80
    }

   
    
//    func tableViewScrollToBottom(_ animated: Bool) {
//
//        let delay = 0.1 * Double(NSEC_PER_SEC)
//        let time = DispatchTime.now() + Double(Int64(delay)) / Double(NSEC_PER_SEC)
//
//        DispatchQueue.main.asyncAfter(deadline: time, execute: {
//
//            let numberOfSections = self.tblView.numberOfSections
//            let numberOfRows = self.tblView.numberOfRows(inSection: numberOfSections-1)
//
//            if numberOfRows > 0 {
//                let indexPath = IndexPath(row: numberOfRows-1, section: (numberOfSections-1))
//                self.tblView.scrollToRow(at: indexPath, at: UITableViewScrollPosition.bottom, animated: animated)
//            }
//
//        })
//    }
    
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        
    }
    
    func setUpSelectionUI(){
        
        btnStart.titleLabel?.font = UIFont(name: "SourceSansPro-Semibold", size: 15.0);
        btnStart!.layer.cornerRadius = 10
        btnStart!.clipsToBounds = true

    }
    
    //MARK:  API Fetch 
    
    func getHomeData()->(){
        currentRow = nil
        var loadingNotification = MBProgressHUD.showAdded(to: self.view, animated: true)
            loadingNotification?.mode = MBProgressHUDMode.indeterminate
            loadingNotification?.labelText = "Loading"
        let generalApiobj = GeneralAPI()
        let someDict:[String:String] = ["date":returnFormattedDate(), "location": self.locationID]//
        generalApiobj.hitApiwith(someDict as Dictionary<String, AnyObject>, serviceType: .strGetHomeDetails, success: { (response) in
            DispatchQueue.main.async {
                print(response)
                if(response["code"] as! Int !=  200)
                {
                    MBProgressHUD.hideAllHUDs(for: self.view, animated: true)
                    loadingNotification = nil
                    utility.createAlert(TextMessage.alert.rawValue, alertMessage: "\(response["message"] as! String)", alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
                    return
                }
                guard let data = response["data"] as? [[String:AnyObject]] else{
                    MBProgressHUD.hideAllHUDs(for: self.view, animated: true)
                    utility.createAlert(TextMessage.alert.rawValue, alertMessage: TextMessage.tryAgain.rawValue, alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
                    return
                }
                print(data)
                self.arrayData = data
             
                if self.arrayData!.count == 0{
                        self.addNodata()
                }
               
                
                self.tblView.reloadData()

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
    
    func increaseTableRowHeight(_ indexPath:IndexPath) {
        
        var selectedRowIndex = indexPath
        currentRow = selectedRowIndex.row
         cellTapped = false
        
        self.arrayHeightObj .insert(250, at: currentRow!)
        

        tblView.beginUpdates()
        
        tblView.endUpdates()
    }
    
    func makeHeightArray() -> NSArray{
        
        var arrayHeight = [CGFloat]()
        
        
        for items in arrayData! {
            arrayHeight .append(120)
        }
        print(arrayHeight)
        return arrayHeight as NSArray
    }
    
    
    func decreaseTableRowHeight(_ indexPath:IndexPath) {
        
        var selectedRowIndex = indexPath
        currentRow = selectedRowIndex.row
        
        self.arrayHeightObj .insert(120, at: currentRow!)
        
        currentRow = nil
       
        if selectedRowIndex != indexPath {
            cellTapped = true
        }
        else {
            // there is no cell selected anymore
            cellTapped = false
        }
        tblView.beginUpdates()
        
        tblView.endUpdates()
    }
    
    
    
    func changeStateOne(_ indexPath:IndexPath){
        var ar = [Dictionary<String,AnyObject>]()
        let data = arrayData![indexPath.row]
        ar.append(["caseNo":(data["caseNo"] as? String)! as AnyObject,"shipmentNo":(data["id"] as? String)! as AnyObject])
        changeState(ar)
    }
    
    
    func changeStateBulk(){
        if(self.arrayOfSelectedIndex?.count == 0)
        {
             utility.createAlert(TextMessage.alert.rawValue, alertMessage: TextMessage.noshipment.rawValue, alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
            
            return
        }
        var ar = [Dictionary<String,AnyObject>]()
        for (_,element) in (self.arrayOfSelectedIndex?.enumerated())!{
           let data = arrayData![element.row]
            ar.append(["caseNo":(data["caseNo"] as? String)! as AnyObject,"shipmentNo":(data["id"] as? String)! as AnyObject])
        }
        changeState(ar)
    }
    
    
    func changeState(_ array:[Dictionary<String,AnyObject>]){
        var loadingNotification = MBProgressHUD.showAdded(to: self.view, animated: true)
        loadingNotification?.mode = MBProgressHUDMode.indeterminate
        loadingNotification?.labelText = "Loading"
        let generalApiobj = GeneralAPI()
        let someDict  = ["shipments":array]
        generalApiobj.hitApiwith(someDict as Dictionary<String, AnyObject>, serviceType: .strPickShipment, success: { (response) in
            DispatchQueue.main.async {
                print(response)
                if(response["status"]?.intValue != 1)
                {
                    MBProgressHUD.hideAllHUDs(for: self.view, animated: true)
                    loadingNotification = nil
                    utility.createAlert(TextMessage.alert.rawValue, alertMessage: "\(response["message"] as! String)", alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
                    self.homeScreenLog(false,event: "Pick Shipment")
                    return
                }
                guard let data = response["data"] as? [String:AnyObject],let _ = data["ReaderPickShipmentResponse"] as? [String:AnyObject] else{
                    MBProgressHUD.hideAllHUDs(for: self.view, animated: true)
                    utility.createAlert(TextMessage.alert.rawValue, alertMessage: TextMessage.tryAgain.rawValue, alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
                    self.homeScreenLog(false,event: "Pick Shipment")

                    return
                }
                self.homeScreenLog(true,event: "Pick Shipment")
                self.getHomeData()
                MBProgressHUD.hideAllHUDs(for: self.view, animated: true)
            }
        }) { (err) in
            DispatchQueue.main.async {
                MBProgressHUD.hideAllHUDs(for: self.view, animated: true)
                utility.createAlert(TextMessage.alert.rawValue, alertMessage: TextMessage.tryAgain.rawValue, alertCancelTitle: TextMessage.Ok.rawValue ,view: self)
                self.homeScreenLog(false,event: "Pick Shipment")
                NSLog(" %@", err)
            }
        }

    }
    
    
    
    
    
    func returnFormattedDate()->String{
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        let str = formatter.string(from: dateSelected!)
        return str
    }
    
    func returnDisplayFormatedDate()->String{
        let formatter = DateFormatter()
        formatter.dateFormat = "dd MMM yy"
        let str = formatter.string(from: dateSelected!)
        return str
    }
    
    
    func webView(_ webView: UIWebView, didFailLoadWithError error: Error)
    {
        let path = Bundle.main.path(forResource: "index", ofType:"html" , inDirectory: "HTML")
       let html = try! String(contentsOfFile: path!, encoding:String.Encoding.utf8)
        
       self.webViewMap.loadHTMLString(html, baseURL: Bundle.main.bundleURL)
       self.webViewMap.delegate = nil
    }
    
    //MARK: IBActions
    @IBAction func btnCalenderNew(_ sender: AnyObject) {
     let cal =   STRCalanderViewController(nibName: "STRCalanderViewController", bundle: nil)
        self.homeScreenLog(true,event: "Calendar Screen")
        cal.closure = {(date) in
        
            self.dateSelected = date
            let  order = (Calendar.current as NSCalendar).compare(Date(), to: self.dateSelected!,toUnitGranularity: .day)
              if(order == .orderedSame)
                {
                self.lblTitle.text="TODAY"
                }
              else
              {
                self.lblTitle.text=self.returnDisplayFormatedDate()
              }

            self.getHomeData()
        }
     let nav = UINavigationController(rootViewController: cal)
        cal.dateAlreadySelected = self.dateSelected!
     self.navigationController?.present(nav, animated: false, completion: {
            
        })
    }
    
    @IBAction func btnCalender(_ sender: AnyObject) {
       
      var  order = (Calendar.current as NSCalendar).compare(Date(), to: dateSelected!,
                                                         toUnitGranularity: .day)
        dateSelected = dateSelected!.addingTimeInterval(-(24*60*60))
        order = (Calendar.current as NSCalendar).compare(Date(), to: dateSelected!,
                                                         toUnitGranularity: .day)
        if(order == .orderedSame)
        {
           lblTitle.text="TODAY"
        }
        else
        {
            lblTitle.text=returnDisplayFormatedDate()
        }
         self.getHomeData()
    }
    

   
    @IBAction func btnNext(_ sender: AnyObject) {
        dateSelected = dateSelected!.addingTimeInterval(24*60*60)
        let order = (Calendar.current as NSCalendar).compare(Date(), to: dateSelected!,
                                                         toUnitGranularity: .day)
        if(order == .orderedSame)
        {
            lblTitle.text="TODAY"
        }
        else
        {
            lblTitle.text=returnDisplayFormatedDate()
        }

        self.getHomeData()
    }
    @IBAction func btnList(_ sender: AnyObject) {
        self.webViewMap.isHidden=true;
        self.view.bringSubview(toFront: tblView)
        
    }
    @IBAction func btnMap(_ sender: AnyObject) {
        self.webViewMap.isHidden=false;
        self.view.bringSubview(toFront: webViewMap)
    }
    
    
    
    func UploadRequest(_ indexPath:IndexPath)
    {
        
        
        
        let data = self.arrayData![indexPath.row]
        let caseNo = data["caseNo"] as! String
        let shipmentNo = data["shipmentNo"] as! String
        
        print("first \(firstNameStr) last \(lastNameStr) phone \(phoneNumberSTr)")
        
        let url = URL(string: String(format: "%@%@", Kbase_url, "/reader/deliverShipment" ))
        
        let request = NSMutableURLRequest(url: url!)
        request.httpMethod = "POST"
        
        let boundary = generateBoundaryString()
        
        //define the multipart request type
        
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        request.setValue("application/json", forHTTPHeaderField: "Accept")
        request.setValue(utility.getDevice(), forHTTPHeaderField:"deviceId")
        request.setValue("traquer", forHTTPHeaderField:"AppType")
        request.setValue(utility.getUserToken(), forHTTPHeaderField:"sid")
        
        
        
        
        let body = NSMutableData()
        
        let fname = "test.png"
        let mimetype = "image/png"
        body.append("--\(boundary)\r\n".data(using: String.Encoding.utf8)!)
        body.append("Content-Disposition:form-data; name=\"caseNo\"\r\n\r\n".data(using: String.Encoding.utf8)!)
        body.append("\(caseNo)\r\n".data(using: String.Encoding.utf8)!)
        
        body.append("--\(boundary)\r\n".data(using: String.Encoding.utf8)!)
        body.append("Content-Disposition:form-data; name=\"shipmentNo\"\r\n\r\n".data(using: String.Encoding.utf8)!)
        body.append("\(shipmentNo)\r\n".data(using: String.Encoding.utf8)!)
        
        body.append("--\(boundary)\r\n".data(using: String.Encoding.utf8)!)
        body.append("Content-Disposition:form-data; name=\"recipientFirstName\"\r\n\r\n".data(using: String.Encoding.utf8)!)
        body.append("\(firstNameStr!)\r\n".data(using: String.Encoding.utf8)!)
        
        body.append("--\(boundary)\r\n".data(using: String.Encoding.utf8)!)
        body.append("Content-Disposition:form-data; name=\" recipientLastName\"\r\n\r\n".data(using: String.Encoding.utf8)!)
        body.append("\(lastNameStr!)\r\n".data(using: String.Encoding.utf8)!)
        
        body.append("--\(boundary)\r\n".data(using: String.Encoding.utf8)!)
        body.append("Content-Disposition:form-data; name=\" recipientMobile\"\r\n\r\n".data(using: String.Encoding.utf8)!)
        body.append("\(phoneNumberSTr!)\r\n".data(using: String.Encoding.utf8)!)
        
        body.append("--\(boundary)\r\n".data(using: String.Encoding.utf8)!)
        body.append("Content-Disposition:form-data; name=\" recipientCountryCode\"\r\n\r\n".data(using: String.Encoding.utf8)!)
        body.append("\(countryCodeSTr)\r\n".data(using: String.Encoding.utf8)!)
        if(selectedImage != nil)
        {
            let image_data = UIImageJPEGRepresentation(selectedImage!, 0.0)
            body.append("--\(boundary)\r\n".data(using: String.Encoding.utf8)!)
            body.append("Content-Disposition:form-data; name=\"images[]\"; filename=\"\(fname)\"\r\n".data(using: String.Encoding.utf8)!)
            body.append("Content-Type: \(mimetype)\r\n\r\n".data(using: String.Encoding.utf8)!)
            body.append(image_data!)
            body.append("\r\n".data(using: String.Encoding.utf8)!)
            
        }
        body.append("--\(boundary)--\r\n".data(using: String.Encoding.utf8)!)
        request.httpBody = body as Data
        
        
        
        let session = URLSession.shared
        
        
        let task = session.dataTask(with: request as URLRequest, completionHandler: {
            (
             data,  response, error) in
            
            guard let _:Data = data, let _:URLResponse = response, error == nil else {
                
                print("error")
                self.homeScreenLog(false,event: "Mark Delivered")

                return
            }
            
            let dict = try! JSONSerialization.jsonObject(with: data!, options: .mutableLeaves);
            print(dict as! Dictionary<String, AnyObject>)
            self.homeScreenLog(true,event: "Mark Delivered")

            DispatchQueue.main.async {
                self.getHomeData()
            }
            
            
        }) 
        
        task.resume()
        
        
    }
    
    
    func generateBoundaryString() -> String
    {
        return "Boundary-\(UUID().uuidString)"
    }
    
    func addNodata(){
        let noData = Bundle.main.loadNibNamed("STRNoDataFound", owner: nil, options: nil)!.last as! STRNoDataFound
        noData.tag = 10002
        noData.showViewRetry()
        noData.delegate = self

        self.view.addSubview(noData)
        noData.translatesAutoresizingMaskIntoConstraints = false
        self.view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "V:|-(120)-[noData]-(0)-|", options: NSLayoutFormatOptions(rawValue: 0), metrics: nil, views: ["noData" : noData]))
        self.view.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "H:|-(0)-[noData]-(0)-|", options: NSLayoutFormatOptions(rawValue: 0), metrics: nil, views: ["noData" : noData]))
        
    }
    func retryPressed() {
        getHomeData()
    }
    
    func addKeyboardNotifications() {
        NotificationCenter.default.addObserver(self, selector: #selector(EditViewController.keyboardWillShow(_:)), name:NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(EditViewController.keyboardWillHide(_:)), name:NSNotification.Name.UIKeyboardWillHide, object: nil)
        
    }
    func keyboardWillShow(_ notification: Notification) {
        var info = notification.userInfo!
        let keyboardFrame: CGRect = (info[UIKeyboardFrameEndUserInfoKey] as! NSValue).cgRectValue
        UIView.animate(withDuration: 0, animations: { () -> Void in
           // self.bottonHeight.constant = keyboardFrame.size.height
        }, completion: { (completed: Bool) -> Void in
            
        }) 
    }
    
    func keyboardWillHide(_ notification: Notification) {
        UIView.animate(withDuration: 0, animations: { () -> Void in
          //  self.bottonHeight.constant = 0.0
        }, completion: { (completed: Bool) -> Void in
            
        }) 
    }
    
    /*Fabric event*/
    
    func homeScreenLog(_ success:Bool,event: String){
        var didSuceed:String?
        if success{
            didSuceed = "YES"
        }
        else{
            didSuceed = "NO"
        }
        Answers.logCustomEvent(withName: "HOME SCREEN", customAttributes: ["Success":didSuceed!,"event type":event])
    }

    
}





