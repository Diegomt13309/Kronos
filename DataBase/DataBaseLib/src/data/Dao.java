 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import logic.Accord;
import java.util.Date;
import java.time.LocalTime;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logic.State;
import logic.TempUser;
import logic.Type;
import logic.User;
import logic.User_Role;


/**
 *
 * @author jonathan
 */
public class Dao {

    private DBConnection db;
    private static Dao dao = null;

    private Dao() {

        this.db = new DBConnection();
    }

    public static Dao getDao() {
        if (dao == null) {
            return new Dao();
        } else {
            return dao;
        }

    }

    public void insertAccord(Accord acc) throws Exception {

        this.db.connect();

        CallableStatement statement = this.db.getConnection().prepareCall("{call insertAccord(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

      java.text.DateFormat  format= new SimpleDateFormat("yyyy-dd-MM");
      acc.setIncorporatedTime(LocalTime.now());
      
      int hour=acc.getIncorporatedTime().getHour();
      int minute=acc.getIncorporatedTime().getMinute();
      int second=acc.getIncorporatedTime().getSecond();
        statement.setString(1, acc.getAccNumber());
        statement.setDate(2, new java.sql.Date(acc.getIncorporatedDate().getTime()));
        statement.setTime(3, java.sql.Time.valueOf(acc.getIncorporatedTime()));
        statement.setDate(4, new java.sql.Date(acc.getDeadline().getTime()));
        statement.setDate(5, new java.sql.Date(acc.getSessionDate().getTime()));
        statement.setString(6, String.valueOf(acc.getType()));
        statement.setString(7, acc.getObservations());
        statement.setBoolean(8, acc.isPublished());
        statement.setBoolean(9, acc.isNotified());
        statement.setInt(10, acc.getState());
       // String command="call insertAccord('%s','%s','%s','%s','%c','%s',%b,%b,%d);";
      // String.format(command,acc.getAccNumber(),incordate,deadline,sessDate,acc.getType(),acc.getObservations(),false,false,acc.getState() );
        statement.executeUpdate();
        statement.close();

       // statement.executeUpdate();
       // statement.close();
        for (String item : acc.getURL()) {
            CallableStatement statement2 = this.db.getConnection().prepareCall("{call insertAccPdf(?, ?)}");
            statement2.setString(1, acc.getAccNumber());
            statement2.setString(2, item);
            statement2.executeUpdate();

            statement2.close();
        }

        
                 
//        java.sql.Date date=new java.sql.Date(acc.getIncorporatedDate().getTime());
//       java.sql.Date date2=new java.sql.Date(acc.getDeadline().getTime());
//       java.sql.Date date3= new java.sql.Date(acc.getDeadline().getTime());
//        String aux=String.valueOf(acc.getType());
        this.db.disconnect();
    }

    public void insertTempUser(TempUser tmp) throws Exception {

        this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call insertTempUser(?, ?)}");
        statement.setString(1, tmp.getName());
        statement.setString(2, tmp.getEmail());
        statement.executeUpdate();
        statement.close();
        this.db.disconnect();
    }
    
    public void insertAccordNotification(String accord) throws Exception{
        this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call insertAccNotification(?)}");
        statement.setString(1, accord);
        statement.executeUpdate();
        statement.close();
        this.db.disconnect();
    }

    public void insertUserAccord(TempUser usr, Accord acc) throws Exception {
        this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call insertUserAcc(?, ?)}");
        statement.setString(1, usr.getEmail());
        statement.setString(2, acc.getAccNumber());
        statement.executeUpdate();
        statement.close();
        this.db.disconnect();

    }
    public TempUser getTempUserByEmail(String email) throws Exception{
        this.db.connect();
        
        CallableStatement statement = this.db.getConnection().prepareCall("{call searchTempUser(?)}");
        statement.setString(1,email);
        TempUser temp=null;
         ResultSet rs = statement.executeQuery();
          while (rs.next()) {
              temp= new TempUser();
              temp.setName(rs.getString("NAME"));
              temp.setEmail(rs.getString("EMAIL"));
              
          }
         rs.close();
        this.db.disconnect();
        return temp;
    }
    public List<Accord> searchAccordByType(char type) throws Exception {
        this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call searchAccordType(? )}");
        statement.setString(1,String.valueOf(type));
        ResultSet rs = statement.executeQuery();
        Map<String, Accord> map = new HashMap();

        while (rs.next()) {
            
            String accNumber = rs.getString("ACCNUMBER");
            if (map.isEmpty() || ! map.containsKey(accNumber)) { //if the map is empty or the result isn't
                Accord a = new Accord();                                           //in the map
                a.setAccNumber(accNumber);
                a.setIncorporatedDate(rs.getDate("INCORDATE"));
                a.setDeadline(rs.getDate("DEADLINE"));
                a.setSessionDate(rs.getDate("SESSIONDATE"));
                a.setType(rs.getString("TYPE_ID").charAt(0));
                a.setObservations(rs.getString("OBSERVATIONS"));
                a.setNotified(rs.getBoolean("NOTIFIED"));
                a.setPublished(rs.getBoolean("PUBLIC"));
                a.setState(rs.getInt("STATE"));
                a.getURL().add(rs.getString("URL"));
                map.put(accNumber, a);
            }
            else {
                    //if the result isn't  in the map or the map isn't empty, just add the URL into result
                    map.get(accNumber).getURL().add(rs.getString("URL"));
                
            }
        }
       
        statement.close();
        this.db.disconnect();
       return new ArrayList<Accord>(map.values());
    }

    public Accord getAccordByAccNumber(String AccNumber) throws Exception {
        this.db.connect();
        String search="MSPH-CM-ACUER-";
        search+=AccNumber;
        CallableStatement statement = this.db.getConnection().prepareCall("{call searchAccordNumber(? )}");
        statement.setString(1, AccNumber);
        ResultSet rs = statement.executeQuery();
        List<Accord> list = new ArrayList();
        boolean flag = false;
        Accord a = null;
        while (rs.next()) {
            if (!flag) {
                a = new Accord();
                a.setAccNumber(rs.getString("ACCNUMBER"));
                a.setIncorporatedDate(rs.getDate("INCORDATE"));
                a.setDeadline(rs.getDate("DEADLINE"));
                a.setSessionDate(rs.getDate("SESSIONDATE"));
                a.setType(rs.getString("TYPE_ID").charAt(0));
                a.setObservations(rs.getString("OBSERVATIONS"));
                a.setNotified(rs.getBoolean("NOTIFIED"));
                a.setPublished(rs.getBoolean("PUBLIC"));
                a.setState(rs.getInt("STATE"));
                a.getURL().add(rs.getString("URL"));
            } else {
                String url =rs.getString("URL");
                if(!a.getURL().contains(url))
                    a.getURL().add(url);
                    
            }

            flag = true;
        }

        statement.close();
        this.db.disconnect();   

        return a;
    }
    
    
    
    
    
    public Accord getSearchAccordByAccNumber(String AccNumber) throws Exception {
        this.db.connect();
        String search="MSPH-CM-ACUER-";
        search+=AccNumber;
        CallableStatement statement = this.db.getConnection().prepareCall("{call searchAccordNumber(? )}");
        statement.setString(1, search);
        ResultSet rs = statement.executeQuery();
        List<Accord> list = new ArrayList();
        boolean flag = false;
        Accord a = null;
        while (rs.next()) {
            if (!flag) {
                a = new Accord();
                a.setAccNumber(rs.getString("ACCNUMBER"));
                a.setIncorporatedDate(rs.getDate("INCORDATE"));
                a.setDeadline(rs.getDate("DEADLINE"));
                a.setSessionDate(rs.getDate("SESSIONDATE"));
                a.setType(rs.getString("TYPE_ID").charAt(0));
                a.setObservations(rs.getString("OBSERVATIONS"));
                a.setNotified(rs.getBoolean("NOTIFIED"));
                a.setPublished(rs.getBoolean("PUBLIC"));
                a.setState(rs.getInt("STATE"));
                a.getURL().add(rs.getString("URL"));
            } else {
                String url =rs.getString("URL");
                if(!a.getURL().contains(url))
                    a.getURL().add(url);
                    
            }

            flag = true;
        }

        statement.close();
        this.db.disconnect();   

        return a;
    }
    
    
    

      public List<Accord> searchAccordBySessionDate(Date SessionDate) throws Exception {
        this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call searchAccordsessionDate(? )}");
        statement.setDate(1, new java.sql.Date(SessionDate.getTime()));
        ResultSet rs = statement.executeQuery();
        Map<String, Accord> map = new HashMap();

        while (rs.next()) {
            
            String accNumber = rs.getString("ACCNUMBER");
            if (map.isEmpty() || ! map.containsKey(accNumber)) { //if the map is empty or the result isn't
                Accord a = new Accord();                                                 //in the map
                a.setAccNumber(accNumber);
                a.setIncorporatedDate(rs.getDate("INCORDATE"));
                a.setDeadline(rs.getDate("DEADLINE"));
                a.setSessionDate(rs.getDate("SESSIONDATE"));
                a.setType(rs.getString("TYPE_ID").charAt(0));
                a.setObservations(rs.getString("OBSERVATIONS"));
                a.setNotified(rs.getBoolean("NOTIFIED"));
                a.setPublished(rs.getBoolean("PUBLIC"));
                a.setState(rs.getInt("STATE"));
                a.getURL().add(rs.getString("URL"));
                map.put(accNumber, a);
            }
            else {
                    //if the result isn't  in the map or the map isn't empty, just add the URL into result
                    map.get(accNumber).getURL().add(rs.getString("URL"));
                
            }
        }
       
        statement.close();
        this.db.disconnect();
       return new ArrayList<>(map.values());
    }

    public List<Accord> searchAccordByIcorporatedDate(Date Incorporated) throws Exception {
        this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call searchAccordIncorDate(? )}");
        statement.setDate(1, new java.sql.Date(Incorporated.getTime()));
        ResultSet rs = statement.executeQuery();
        Map<String, Accord> map = new HashMap();

        while (rs.next()) {
       
            String accNumber = rs.getString("ACCNUMBER");
            if (map.isEmpty() || ! map.containsKey(accNumber)) { //if the map is empty or the result isn't
                Accord a = new Accord();                                                 //in the map
                a.setAccNumber(accNumber);
                a.setIncorporatedDate(rs.getDate("INCORDATE"));
                a.setDeadline(rs.getDate("DEADLINE"));
                a.setSessionDate(rs.getDate("SESSIONDATE"));
                a.setType(rs.getString("TYPE_ID").charAt(0));
                a.setObservations(rs.getString("OBSERVATIONS"));
                a.setNotified(rs.getBoolean("NOTIFIED"));
                a.setPublished(rs.getBoolean("PUBLIC"));
                a.setState(rs.getInt("STATE"));
                a.getURL().add(rs.getString("URL"));
                map.put(accNumber, a);
            }
            else {
                    //if the result isn't  in the map or the map isn't empty, just add the URL into result
                    map.get(accNumber).getURL().add(rs.getString("URL"));
                
            }
        }
       
        statement.close();
        this.db.disconnect();
       return new ArrayList<>(map.values());
    }
    
    public List<Accord> searchAllAccords() throws Exception{
         this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call searchAllAccords()}");
        ResultSet rs = statement.executeQuery();
        Map<String, Accord> map = new HashMap();

        while (rs.next()) {
       
            String accNumber = rs.getString("ACCNUMBER");
            if (map.isEmpty() || ! map.containsKey(accNumber)) { //if the map is empty or the result isn't
                Accord a = new Accord();                                                 //in the map
                a.setAccNumber(accNumber);
                a.setIncorporatedDate(rs.getDate("INCORDATE"));
                a.setDeadline(rs.getDate("DEADLINE"));
                a.setSessionDate(rs.getDate("SESSIONDATE")); 
                a.setType(rs.getString("TYPE_ID").charAt(0));
                a.setObservations(rs.getString("OBSERVATIONS"));
                a.setNotified(rs.getBoolean("NOTIFIED"));
                a.setPublished(rs.getBoolean("PUBLIC"));
                a.setState(rs.getInt("STATE"));
                a.getURL().add(rs.getString("URL"));
                map.put(accNumber, a);
            }
            else {
                    //if the result isn't  in the map or the map isn't empty, just add the URL into result
                    map.get(accNumber).getURL().add(rs.getString("URL"));
                
            }
        }
       
        statement.close();
        this.db.disconnect();
       return new ArrayList<>(map.values());
    }
    
    public User getUser(User us) throws Exception{
          this.db.connect();
          
         CallableStatement statement = this.db.getConnection().prepareCall("call getUser(?, ?)");
         statement.setString(1, us.getTempUser());
         statement.setString(1, us.getPassword());
         ResultSet rs=statement.executeQuery();
         
         User result=null;
         while(rs.next()){
             result=new User();
             result.setTempUser(rs.getString("TEMPUSER"));
             result.setPassword(rs.getString("PASSWORD"));
             result.setDeparment(rs.getInt("DEPARTMENT"));
         }
         statement.close();
        this.db.disconnect();
        return result;
         
    }
    
    
     public User_Role getUserRole(User us) throws Exception{
          this.db.connect();
          
         CallableStatement statement = this.db.getConnection().prepareCall("call getUserRole(?, ?)");
         statement.setString(1, us.getTempUser());
         statement.setString(2, us.getPassword());
         ResultSet rs=statement.executeQuery();
         
         User_Role result=null;
         while(rs.next()){
             result=new User_Role();
             result.getUser().setTempUser(rs.getString("USER_NAME"));
             result.getUser().setDeparment(rs.getInt("DEPARTMENT"));
             result.setRole(rs.getString("ROLE_NAME"));
         }
         statement.close();
        this.db.disconnect();
        return result;
         
    }
    
    
    public List<Type> getAllType() throws Exception{
        this.db.connect();
         CallableStatement statement = this.db.getConnection().prepareCall("{call searchAllTypes()}"); 
         ResultSet rs=statement.executeQuery();
         List<Type> result= new ArrayList();
         while(rs.next()){
             Type t=new Type();
             t.setDescription(rs.getString("DESCRIPTION"));
             t.setId(rs.getString("ID").charAt(0));
             result.add(t);
         }
        
         statement.close();
        this.db.disconnect();
        return result;
        
    }
    
    public List<State> getAllState() throws Exception{
        this.db.connect();
         CallableStatement statement = this.db.getConnection().prepareCall("{call searchAllStates()}"); 
         ResultSet rs=statement.executeQuery();
         List<State> result= new ArrayList();
         while(rs.next()){
             State s= new State();
             s.setDescription(rs.getString("DESCRIPTION"));
             s.setId(rs.getInt("ID"));
             result.add(s);
         }
        
         statement.close();
        this.db.disconnect();
        return result;
           
           
           
           
    }
    
    public List<Accord> searchExpiredAccords(Date actual, Date limit) throws Exception{
        this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call searchExpiredAccords(?, ?)}");
        statement.setDate(1, new java.sql.Date(actual.getTime()));
        statement.setDate(2, new java.sql.Date(limit.getTime()));
        
        ResultSet rs = statement.executeQuery();
        Map<String, Accord> map = new HashMap();

        while (rs.next()) {
            
            String accNumber = rs.getString("ACCNUMBER");
            if (map.isEmpty() || ! map.containsKey(accNumber)) { //if the map is empty or the result isn't
                Accord a = new Accord();                                                 //in the map
                a.setAccNumber(accNumber);
                a.setIncorporatedDate(rs.getDate("INCORDATE"));
                a.setDeadline(rs.getDate("DEADLINE"));
                a.setSessionDate(rs.getDate("SESSIONDATE"));
                a.setType(rs.getString("TYPE_ID").charAt(0));
                a.setObservations(rs.getString("OBSERVATIONS"));
                a.setNotified(rs.getBoolean("NOTIFIED"));
                a.setPublished(rs.getBoolean("PUBLIC"));
                a.setState(rs.getInt("STATE"));
                a.getURL().add(rs.getString("URL"));
                map.put(accNumber, a);
            }
            else {
                    //if the result isn't  in the map or the map isn't empty, just add the URL into result
                    map.get(accNumber).getURL().add(rs.getString("URL"));
                
            }
        }
       
        statement.close();
        this.db.disconnect();
       return new ArrayList<>(map.values());
        
        
    }
    
    
    //this function return the accord list that experedby specific date
      public List<Accord> searchAccordByExpiredDate(Date date1, Date limit) throws Exception {
        this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call searchExpiredAccords(? , ?)}");        
        statement.setDate(1, new java.sql.Date(date1.getTime()));
        statement.setDate(2,new java.sql.Date( limit.getTime()));
        ResultSet rs = statement.executeQuery();
        Map<String, Accord> map = new HashMap();

        while (rs.next()) {
            
            String accNumber = rs.getString("ACCNUMBER");
            if (map.isEmpty() || ! map.containsKey(accNumber)) { //if the map is empty or the result isn't
                Accord a = new Accord();                                                 //in the map
                a.setAccNumber(accNumber);
                a.setIncorporatedDate(rs.getDate("INCORDATE"));
                a.setDeadline(rs.getDate("DEADLINE"));
                a.setSessionDate(rs.getDate("SESSIONDATE"));
                a.setType(rs.getString("TYPE_ID").charAt(0));
                a.setObservations(rs.getString("OBSERVATIONS"));
                a.setNotified(rs.getBoolean("NOTIFIED"));
                a.setPublished(rs.getBoolean("PUBLIC"));
                a.setState(rs.getInt("STATE"));
                a.getURL().add(rs.getString("URL"));
                map.put(accNumber, a);
            }
            else {
                    //if the result isn't  in the map or the map isn't empty, just add the URL into result
                    map.get(accNumber).getURL().add(rs.getString("URL"));
                
            }
        }
       
        statement.close();
        this.db.disconnect();
       return new ArrayList<>(map.values());
    }
      
      public void updateAccordSessionDate(String accNumber, Date sessionDate) throws Exception{
           this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call updateAccordSessionDate(? , ?)}");
           statement.setString(1, accNumber);
           statement.setDate(2, new java.sql.Date(sessionDate.getTime()));
           statement.execute();
           statement.close();
           this.db.disconnect();
          
      }
      
         public void updateAccordDeadline(String accNumber, Date deadLine) throws Exception{
           this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call updateAccordDeadline(? , ?)}");
           statement.setString(1, accNumber);
           statement.setDate(2, new java.sql.Date(deadLine.getTime()));
           statement.execute();
           statement.close();
           this.db.disconnect();
          
      }
      
              public void updateAccordType(String accNumber, String type) throws Exception{
           this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call updateAccordType(? , ?)}");
           statement.setString(1, accNumber);
           statement.setString(2, type);
           statement.execute();
           statement.close();
           this.db.disconnect();
          
      }  
         public void updateAccordObservations(String accNumber, String Observations) throws Exception{
           this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call updateAccordObservations(? , ?)}");
           statement.setString(1, accNumber);
           statement.setString(2, Observations);
           statement.execute();
           statement.close();
           this.db.disconnect();
          
      }  
              
             public void updateAccordState(String accNumber, int state) throws Exception{
           this.db.connect();
           CallableStatement statement = this.db.getConnection().prepareCall("{call updateAccordState(? , ?)}");
           statement.setString(1, accNumber);
           statement.setInt(2, state);
           statement.execute();
           statement.close();
           this.db.disconnect();
          
      }      
             
           public void updateAccordUser(String accNumber, String newUser, String oldUser) throws Exception{
           this.db.connect();
           CallableStatement statement = this.db.getConnection().prepareCall("{call updateAccordUser(? , ? ,?)}");
           statement.setString(1, accNumber);
           statement.setString(2, newUser);
           statement.setString(3, oldUser);
           statement.execute();
           statement.close();
           this.db.disconnect();
          
      } 
           public void deletePdf(String accNumber, String pdf) throws Exception{
               this.db.connect();
               CallableStatement statement = this.db.getConnection().prepareCall("{call deletePdf(? ,?)}");
               statement.setString(1, accNumber);
               statement.setString(2, pdf);
               statement.execute();
               statement.close();
               this.db.disconnect();
           }
      
           public void deleteAccord(String accNumber, String user) throws Exception{
               this.db.connect();
               CallableStatement statement = this.db.getConnection().prepareCall("{call deleteAccord(?,?)}");
               statement.setString(1, accNumber);
               statement.setString(2, user);
               statement.execute();
               statement.close();
               this.db.disconnect();
               
               
           }
           public boolean isIncorDate(Date date) throws SQLException{
               this.db.connect();
               CallableStatement statement=this.db.getConnection().prepareCall("{call isActInDB(?)}");
               statement.setDate(1, new java.sql.Date(date.getTime()));
               ResultSet rs= statement.executeQuery();
               boolean isInDB = rs.next();
               
               if(!isInDB){
                 statement=this.db.getConnection().prepareCall("{call insertTempAct(?)}");
                   statement.setDate(1, new java.sql.Date(date.getTime()));
                   int a =statement.executeUpdate();
                   System.out.println(a);
                 
                   
               }
                 statement.close();
               this.db.disconnect();
               return isInDB;
              
               
               
           }
           public List<Accord> emailInfo(Date actual, Date limit) throws Exception{
               this.db.connect();
        CallableStatement statement = this.db.getConnection().prepareCall("{call searchExpiredAccords(? , ?)}");        
        statement.setDate(1, new java.sql.Date(actual.getTime()));
        statement.setDate(2,new java.sql.Date( limit.getTime()));
        ResultSet rs = statement.executeQuery();
        Map<String, Accord> map = new HashMap();

        while (rs.next()) {
            
                String accNumber = rs.getString("ACCNUMBER");
                Accord a = new Accord();                                                 //in the map
                a.setAccNumber(accNumber);
                a.setIncorporatedDate(rs.getDate("INCORDATE"));
               // a.setIncorporatedTime(rs.getTime("INCORTIME").toLocalTime());
               map.put(accNumber, a);
        }
       
        statement.close();
        this.db.disconnect();
       return new ArrayList<>(map.values());
           }
           
}
