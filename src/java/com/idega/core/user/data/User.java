package com.idega.core.user.data;

import com.idega.data.*;
import com.idega.core.data.*;
import com.idega.builder.accesscontrol.data.PermissionGroup;
import java.util.List;
import java.sql.*;


/**
 * Title:        User
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class User extends GenericEntity {

    private static String sClassName = "com.idega.core.user.data.User";

    public User(){
            super();
    }

    public User(int id)throws SQLException{
            super(id);
    }


    public String getEntityName(){
            return "ic_user";
    }

    public void initializeAttributes(){
      addAttribute(getIDColumnName());

      addAttribute(getFirstNameColumnName(),"Fornafn",true,true,"java.lang.String");
      addAttribute(getMiddleNameColumnName(),"Mi�nafn",true,true,"java.lang.String");
      addAttribute(getLastNameColumnName(),"Eftirnafn",true,true,"java.lang.String");
      addAttribute(getDisplayNameColumnName(),"Kenninafn",true,true,"java.lang.String");
      addAttribute(getDescriptionColumnName(),"L�sing",true,true,"java.lang.String");
      addAttribute(getDateOfBirthColumnName(),"F��ingardagur",true,true,"java.sql.Date");
      addAttribute(getGenderColumnName(),"Kyn",true,true,"java.lang.String");

    }

    public void setDefaultValues(){

    }

    public String getIDColumnName(){
      return getUserIDColumnName();
    }

    /*  ColumNames begin   */

    public static String getUserIDColumnName(){
      return "ic_user_id";
    }

    public static String getFirstNameColumnName(){
      return "first_name";
    }

    public static String getMiddleNameColumnName(){
      return "middle_name";
    }

    public static String getLastNameColumnName(){
      return "last_name";
    }

    public static String getDisplayNameColumnName(){
      return "display_name";
    }

    public static String getDescriptionColumnName(){
      return "description";
    }

    public static String getDateOfBirthColumnName(){
      return "date_of_birth";
    }

    public static String getGenderColumnName(){
      return "gender";
    }

    /*  ColumNames end   */


    /*  Getters begin   */

    public String getFirstName() {
      return (String) getColumnValue(getFirstNameColumnName());
    }

    public String getMiddleName() {
      return (String) getColumnValue(getMiddleNameColumnName());
    }

    public String getLastName() {
      return (String) getColumnValue(getLastNameColumnName());
    }

    public String getDisplayName() {
      return (String) getColumnValue(getLastNameColumnName());
    }

    public String getDescription() {
      return (String) getColumnValue(getLastNameColumnName());
    }

    public Date getDateOfBirth(){
      return (Date) getColumnValue(getDateOfBirthColumnName());
    }

    public String getGender(){
      return getStringColumnValue(getGenderColumnName());
    }

    public String getName(){
      return this.getFirstName() + " " + this.getMiddleName() + " " + this.getLastName();
    }

    /*  Getters end   */


    /*  Setters begin   */

    public void setFirstName(String fName) {
      setColumn(getFirstNameColumnName(),fName);
    }

    public void setMiddleName(String mName) {
      setColumn(getMiddleNameColumnName(),mName);
    }

    public void setLastName(String lName) {
      setColumn(getLastNameColumnName(),lName);
    }

    public void setDisplayName(String dName) {
      setColumn(getLastNameColumnName(),dName);
    }

    public void setDescription(String description) {
      setColumn(getLastNameColumnName(),description);
    }

    public void setDateOfBirth(Date dateOfBirth){
      setColumn(getDateOfBirthColumnName(),dateOfBirth);
    }

    public void setGender(String gender){
      setColumn(getGenderColumnName(),gender);
    }

    /*  Setters end   */





    public Group[] getGenericGroups()throws SQLException{
      Group group = new Group();
      return (Group[]) findRelated(group);
      //return (Union[])union.findAll("select * from "+union.getEntityName()+" where "+this.getIDColumnName()+"='"+this.getID()+"' ");
    }

    public List getAllGroups() throws SQLException{
      return EntityFinder.findRelated(this,Group.getStaticInstance());
    }

    public List getPermissionGroups() throws SQLException{
      return EntityFinder.findRelated(this,PermissionGroup.getStaticPermissionGroupInstance());
      //return (PermissionGroup[])findResated(new PermissionGroup());
    }

    public static User getStaticInstance(){
      return (User)User.getStaticInstance(sClassName);
    }


}
