package com.idega.core.user.data;

import com.idega.data.*;
import com.idega.core.data.*;
import com.idega.core.accesscontrol.data.PermissionGroup;
import java.util.List;
import java.sql.*;


/**
 * Title:        User
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class UserBMPBean extends com.idega.data.GenericEntity implements com.idega.core.user.data.User {

    private static String sClassName = User.class.getName();

    public UserBMPBean(){
      super();
    }

    public UserBMPBean(int id)throws SQLException{
      super(id);
    }


    public String getEntityName(){
            return "ic_user";
    }

    public void initializeAttributes(){
      addAttribute(getIDColumnName());

      addAttribute(getColumnNameFirstName(),"First name",true,true,java.lang.String.class);
      addAttribute(getColumnNameMiddleName(),"Middle name",true,true,java.lang.String.class);
      addAttribute(getColumnNameLastName(),"Last name",true,true,java.lang.String.class);
      addAttribute(getColumnNameDisplayName(),"Display name",true,true,java.lang.String.class);
      addAttribute(getColumnNameDescription(),"Description",true,true,java.lang.String.class);
      addAttribute(getColumnNameDateOfBirth(),"Birth date",true,true,java.sql.Date.class);
      addManyToOneRelationship(getColumnNameGender(),"Gender",com.idega.core.user.data.Gender.class);
      addOneToOneRelationship(getColumnNameSystemImage(),"Image",com.idega.core.data.ICFile.class);
      addOneToOneRelationship(_COLUMNNAME_USER_GROUP_ID,"User",GenericGroup.class);
      addOneToOneRelationship(_COLUMNNAME_PRIMARY_GROUP_ID,"Primary group",GenericGroup.class);
      this.addManyToManyRelationShip(Address.class,"ic_user_address");
      this.addManyToManyRelationShip(Phone.class,"ic_user_phone");
      this.addManyToManyRelationShip(Email.class,"ic_user_email");
      this.setNullable(getColumnNameSystemImage(),true);
      this.setNullable(_COLUMNNAME_PRIMARY_GROUP_ID,true);
      //temp
      this.addManyToManyRelationShip(GenericGroup.class,"ic_group_user");
    }

    public void setDefaultValues(){
    }

    public void insertStartData() throws SQLException {

    }

    public String getIDColumnName(){
      return getColumnNameUserID();
    }

    public static User getStaticInstance(){
      return (User)com.idega.core.user.data.UserBMPBean.getStaticInstance(sClassName);
    }

    public static String getAdminDefaultName(){
      return "Administrator";
    }




    /*  ColumNames begin   */

    public static String getColumnNameUserID(){return "ic_user_id";}
    public static String getColumnNameFirstName(){return "first_name";}
    public static String getColumnNameMiddleName(){return "middle_name";}
    public static String getColumnNameLastName(){return "last_name";}
    public static String getColumnNameDisplayName(){return "display_name";}
    public static String getColumnNameDescription(){return "description";}
    public static String getColumnNameDateOfBirth(){return "date_of_birth";}
    public static String getColumnNameGender(){return "ic_gender_id";}
    public static String getColumnNameSystemImage(){return "system_image_id";}
    public static final String _COLUMNNAME_USER_GROUP_ID = "user_representative";
    public static final String _COLUMNNAME_PRIMARY_GROUP_ID = "primary_group";
    /*  ColumNames end   */


    /*  Getters begin   */

    public String getFirstName() {
      return (String) getColumnValue(getColumnNameFirstName());
    }

    public String getMiddleName() {
      return (String) getColumnValue(getColumnNameMiddleName());
    }

    public String getLastName() {
      return (String) getColumnValue(getColumnNameLastName());
    }

    public String getDisplayName() {
      return (String) getColumnValue(getColumnNameDisplayName());
    }

    public String getDescription() {
      return (String) getColumnValue(getColumnNameDescription());
    }

    public Date getDateOfBirth(){
      return (Date) getColumnValue(getColumnNameDateOfBirth());
    }

    public int getGenderID(){
      return getIntColumnValue(getColumnNameGender());
    }

    public int getSystemImageID(){
      return getIntColumnValue(getColumnNameSystemImage());
    }

    public int getGroupID(){
      return getIntColumnValue(_COLUMNNAME_USER_GROUP_ID);
    }

    public int getPrimaryGroupID(){
      return getIntColumnValue(_COLUMNNAME_PRIMARY_GROUP_ID);
    }

    public String getName(){
	  String firstName=this.getFirstName();
	  String middleName=this.getMiddleName();
	  String lastName = this.getLastName();

	  if(firstName == null){
	    firstName="";
	  }

	  if(middleName == null){
	  	middleName="";
	  }else{
	    middleName = " "+middleName;
	  }

	  if(lastName == null){
	  	lastName="";
	  }else{
  		lastName = " " + lastName;
  	  }
      return firstName + middleName + lastName;
    }

    /*  Getters end   */


    /*  Setters begin   */

    public void setFirstName(String fName) {
      if(!com.idega.core.accesscontrol.business.AccessControl.isValidUsersFirstName(fName)){
        fName = "Invalid firstname";
      }
      if(com.idega.core.accesscontrol.business.AccessControl.isValidUsersFirstName(this.getFirstName())){ // if not Administrator
        setColumn(getColumnNameFirstName(),fName);
      }

    }

    public void setMiddleName(String mName) {
      setColumn(getColumnNameMiddleName(),mName);
    }

    public void setLastName(String lName) {
      setColumn(getColumnNameLastName(),lName);
    }

    public void setDisplayName(String dName) {
      setColumn(getColumnNameDisplayName(),dName);
    }

    public void setDescription(String description) {
      setColumn(getColumnNameDescription(),description);
    }

    public void setDateOfBirth(Date dateOfBirth){
      setColumn(getColumnNameDateOfBirth(),dateOfBirth);
    }

    public void setGender(Integer gender){
      setColumn(getColumnNameGender(),gender);
    }

    public void setGender(int gender){
      setColumn(getColumnNameGender(),gender);
    }

    public void setSystemImageID(Integer fileID){
      setColumn(getColumnNameSystemImage(),fileID);
    }

    public void setSystemImageID(int fileID){
      setColumn(getColumnNameSystemImage(),fileID);
    }

    public void setGroupID(int icGroupId){
      setColumn(_COLUMNNAME_USER_GROUP_ID,icGroupId);
    }

    public void setPrimaryGroupID(int icGroupId){
      setColumn(_COLUMNNAME_PRIMARY_GROUP_ID,icGroupId);
    }

    public void setPrimaryGroupID(Integer icGroupId){
      setColumn(_COLUMNNAME_PRIMARY_GROUP_ID,icGroupId);
    }


    /*  Setters end   */
    public Integer ejbFindUserFromEmail(String emailAddress)throws javax.ejb.FinderException{
      StringBuffer sql = new StringBuffer("select iu.* ");
      sql.append("from ic_email ie,ic_user_email iue,ic_user iu ");
      sql.append("where ie.ic_email_id = iue.ic_email_address ");
      sql.append("and iue.ic_user_id = iu.ic_user_id " );
      sql.append(" and ie.address = '");
      sql.append(emailAddress);
      sql.append("'");
      java.util.Collection coll =  super.idoFindIDsBySQL(sql.toString());
      if(!coll.isEmpty())
        return (Integer)coll.iterator().next();
      else
        throw new javax.ejb.FinderException("No user found");
    }


}