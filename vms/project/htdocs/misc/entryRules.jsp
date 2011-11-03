<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import = "java.util.*" %>
<%@page import = "java.io.*" %>
<%@page import = "java.sql.*" %>
<%@page import = "java.text.*" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%> 

<%!
//MySQL server related constants
final String MYSQL_DB_SERVER = "sos.sdp.stratify.com";
final String MYSQL_DATABASE = "str_version";
final String MYSQL_CONNECTION_STRING  = "com.mysql.jdbc.Driver";
final String MYSQL_DB_URL_PREFIX = "jdbc:mysql://";
final int MYSQL_PORT = 3307;
final String MYSQL_USERNAME = "vms";
final String MYSQL_PASSWORD = "pokhran";
String myDBURL = MYSQL_DB_URL_PREFIX + MYSQL_DB_SERVER + ":" + MYSQL_PORT + "/" + MYSQL_DATABASE;

final String GET_RULES = "SELECT DISTINCT RULE FROM vms_compatibility_rules ORDER BY RULE";
final String GET_RULES_COMPO = "SELECT A.RULE, C.COMPONENT, B.MAJOR ,B.MINOR, B.BUILD, B.PATCH "
        + " FROM vms_compatibility_rules A, vms_compo_version B, vms_components C "
        + " WHERE A.COMP_VER_GID = B.GID AND B.COMP_GID = C.GID "
        + " ORDER BY A.RULE";

java.sql.Connection getMySQLDBConnection(String conString, String dbURL, String uName, String passWd) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
// TODO Auto-generated method stub
    java.sql.Connection con = null;
    
//System.out.println(conString + " " + dbURL + " " + uName + " " + passWd );
    Class.forName(conString).newInstance();
    
    con = DriverManager.getConnection(dbURL, uName, passWd);
    
    return con;
}


void cleanUp(PreparedStatement st, ResultSet rs) throws Exception {
    if(rs != null){
        rs.close();
        rs = null;
    }
    if(st != null){
        st.close();
        st = null;
    }
}

void freeUp(Statement st, ResultSet rs) throws Exception{
    rs.close();
    rs = null;
    st.close();
    st = null;
}

String join(Collection s, String delimiter) {
    StringBuffer buffer = new StringBuffer();
    Iterator iter = s.iterator();
    while (iter.hasNext()) {
        buffer.append(iter.next());
        if (iter.hasNext()) {
            buffer.append(delimiter);
        }
    }
    return buffer.toString();
}

Vector getDistinctRules(Connection con) throws Exception{
    Vector ret = new Vector();
    PreparedStatement pstmtGetRules = con.prepareStatement(GET_RULES);
    ResultSet rsGetRules = pstmtGetRules.executeQuery();
    
    while(rsGetRules.next()){
        int val = 0;
        val = rsGetRules.getInt("RULE");
        ret.add(new Integer(val));
    }
    
    cleanUp(pstmtGetRules, rsGetRules);
    return ret;
}

TreeMap getRuleComponents(Connection con) throws Exception{
    TreeMap temp = new TreeMap();
//this map will holds rule no as key and ArrayList with component name + version as values
    PreparedStatement pstmtGetRuleCompo = con.prepareStatement(GET_RULES_COMPO);
    ResultSet rsGetRuleCompo = pstmtGetRuleCompo.executeQuery();
    while(rsGetRuleCompo.next()){
        int rule = 0;
        String compo = "";
        int maj = 0;
        int min = 0;
        int bld = 0;
        int pat = 0;
        
        rule = rsGetRuleCompo.getInt("RULE");
        compo = rsGetRuleCompo.getString("COMPONENT");
        maj = rsGetRuleCompo.getInt("MAJOR");
        min = rsGetRuleCompo.getInt("MINOR");
        bld = rsGetRuleCompo.getInt("BUILD");
        pat = rsGetRuleCompo.getInt("PATCH");
        
        if(temp.containsKey(rule)){
            ArrayList inter = null;
            inter = (ArrayList)temp.get(rule);
            inter.add(compo + " " + maj + "." + min + "." + bld + "." + pat);
            temp.put(new Integer(rule), inter);
            
        }else{
            ArrayList compos = new ArrayList();
            compos.add(compo + " " + maj + "." + min + "." + bld + "." + pat);
            temp.put(new Integer(rule), compos);
        }
    }
    
    cleanUp(pstmtGetRuleCompo, rsGetRuleCompo);
    return temp;
}


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Stratify Version Mgmt System(VMS):: Entry Compatibility rules</title>
        
        
    </head>
    
    
    <body>
        <%
        TreeMap ruleCompo = new TreeMap();
        
        //get connection to MySQL db
        java.sql.Connection mysqlCon = null;
        try {
            //get connection to MySQL server - do it once to minimise overhead
            mysqlCon = getMySQLDBConnection(MYSQL_CONNECTION_STRING, myDBURL, MYSQL_USERNAME, MYSQL_PASSWORD);
            
        } catch (ClassNotFoundException e1) {
            
            System.out.println("Driver Class not found for MySQL");
        } catch (SQLException e1) {
            
            System.out.println("Can not connect to MySQL.");
        } catch (InstantiationException e) {
            
            System.out.println("Could not instantiate Driver Class.");
        } catch (IllegalAccessException e) {
            
            e.printStackTrace();
        }//end try-catch
        
        
        ruleCompo = getRuleComponents(mysqlCon);
        
        if(ruleCompo != null){
            out.println("ruleCompo existis");
            
          
        }
        
        %>
        <h1>Compatibility Rules</h1>
        
        <a href = './addRule.jsp'>Click here to enter new rules!</a>
        
        <table border = "1">
            <th>Rule No.</th><th>Components</th><th>Options</th>
       <%     
              Set myset = ruleCompo.entrySet();
            Iterator itr = myset.iterator();
            
            while(itr.hasNext()){
                Map.Entry me = (Map.Entry)itr.next();
                int key = 0;
                String compValues = "";
                ArrayList val = null;
                key = ((Integer)me.getKey()).intValue();                
                val = (ArrayList)me.getValue();
                compValues = join(val, ",");
                //out.println(key + ":" + compValues);
          %>
           <tr><td><%= key %></td>
	                <td><%= compValues %></td>
	                <td><a href = "">Edit</a>/<a href = "">Delete</a></td>
	            </tr>
          <%
            }
         %>   
            
        </table>
        
        
        
    </body>
</html>
