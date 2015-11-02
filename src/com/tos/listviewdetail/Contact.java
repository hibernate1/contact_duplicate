package com.tos.listviewdetail;
public class Contact {
	 
    //private variables
    int _id;
    String _name;
    String _phone_number;
    String _email;
    String _account;
    boolean is_checked;
 
    public String get_account() {
		return _account;
	}
	public void set_account(String _account) {
		this._account = _account;
	}
	// Empty constructor
    public Contact(){
 
    }
    // constructor
    public Contact(int id, String name, String _phone_number,String _email,String account,boolean checked){
        //this._id = id;
    	this._id=id;
        this._name = name;
        this._phone_number = _phone_number;
        this._email=_email;
        this._account=account;
        this.is_checked=checked;
    }
 
    // constructor
    
    // getting ID
    public int getID(){
        return this._id;
    }
 
    // setting id
    public void setID(int id){
        this._id = id;
    }
 
    // getting name
    public String getName(){
        return this._name;
    }
    
    
    public String getEmail(){
        return this._email;
    }
 
    // setting name
    public void setName(String name){
        this._name = name;
    }
 
    // getting phone number
    public String getPhoneNumber(){
        return this._phone_number;
    }
 
    // setting phone number
    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }
    
    
    public void setEmail(String email){
        this._email = email;
    }
    
    
    public void set_chcked(boolean checked)
    {
    	this.is_checked=checked;
    }
    
    public boolean get_checked()
    {
    	return this.is_checked;
    }
    
    
}