//idega 2000 - eiki

package com.idega.core.user.data;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class Phone extends GenericEntity{

    public Phone(){
      super();
    }

    public Phone(int id)throws SQLException{
      super(id);
    }

    public void initializeAttributes(){
      addAttribute(getIDColumnName());
      addAttribute("phone_number","N�mer",true,true,"java.lang.String");
      addAttribute("ic_country_code_id","Landsn�mer",true,true,"java.lang.Integer");
      addAttribute("ic_area_code_id","Sv��isn�mer",true,true,"java.lang.Integer");
      addAttribute("phone_attribute","Tegund",true,true,"java.lang.String");
      addAttribute("phone_attribute_value","Gildi",true,true,"java.lang.String");
    }

    public String getEntityName(){
      return "ic_phone";
    }

    public void setDefaultValues() {
      setColumn("ic_country_code_id",-1);
      setColumn("ic_area_code_id",-1);
    }

    public String getNumber(){
      return (String)getColumnValue("phone_number");
    }

    public void setNumber(String number){
      setColumn("phone_number", number);
    }


    public String getPhoneType(){
      return (String) getColumnValue("phone_type");
    }

    public void setPhoneType(String phone_type){
      setColumn("phone_type", phone_type);
    }

}
