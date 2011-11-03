<%@include file = "../layout/vmsCommonBase.jsp"%>
<%@page import = "java.util.*" %>
<%@page import = "java.io.*" %>
<%@page import = "java.sql.*" %>
<%@page import = "java.text.*" %>


<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%> 
<%@ taglib uri="http://jakarta.apache.org/taglibs/request-1.0" prefix="req" %>

<%!
//MySQL server related constants
final String MYSQL_DB_SERVER = "sos.sdp.stratify.com";
final String MYSQL_DATABASE = "vms_qa";
final String MYSQL_CONNECTION_STRING  = "com.mysql.jdbc.Driver";
final String MYSQL_DB_URL_PREFIX = "jdbc:mysql://";
final int MYSQL_PORT = 3307;
final String MYSQL_USERNAME = "root";
final String MYSQL_PASSWORD = "galapagos";
String myDBURL = MYSQL_DB_URL_PREFIX + MYSQL_DB_SERVER + ":" + MYSQL_PORT + "/" + MYSQL_DATABASE;

final String SEARCH_COMPO = "SELECT GID FROM vms_compo_version WHERE COMP_GID = ? AND MAJOR = ? "
        + "  AND MINOR = ? AND BUILD = ? AND PATCH = ?";
final String INS_COMPO = "INSERT INTO vms_compo_version(COMP_GID, MAJOR, MINOR, BUILD, PATCH) VALUES "
        + " (?,?,?,?,?)";

boolean checkEmpty(String a, String b, String c, String d, String e){
    boolean ret = false;
    if((a.length() != 0)&&(b.length() != 0)&&(c.length() != 0)&&(d.length() != 0)&&(e.length() != 0)
    &&(!a.equals(""))&&(!b.equals(""))&&(!c.equals(""))&&(!d.equals(""))&&(!e.equals(""))){
        ret = true;
    }
    return ret;
}


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

int insNotExists(Connection con, String compoGId, String maj, String min, String bld, String pat) throws Exception{
    int ret = 0;
    PreparedStatement pstmtInsCompo = con.prepareStatement(INS_COMPO) ;
    pstmtInsCompo.setString(1, compoGId);
    pstmtInsCompo.setString(2, maj);
    pstmtInsCompo.setString(3, min);
    pstmtInsCompo.setString(4, bld);
    pstmtInsCompo.setString(5, pat);
    
    
    ret = pstmtInsCompo.executeUpdate();
    
    cleanUp(pstmtInsCompo, null);
    
    return ret;
}

int checkExists(Connection con, String compoGId, String maj, String min, String bld, String pat) throws Exception{
    int ret = 0;
    PreparedStatement pstmtGetCompo = con.prepareStatement(SEARCH_COMPO) ;
    pstmtGetCompo.setString(1, compoGId);
    pstmtGetCompo.setString(2, maj);
    pstmtGetCompo.setString(3, min);
    pstmtGetCompo.setString(4, bld);
    pstmtGetCompo.setString(5, pat);
    
    ResultSet rsGetCompo = pstmtGetCompo.executeQuery();
    
    if(rsGetCompo.next()){
        ret = rsGetCompo.getInt("GID");
    }
    
    cleanUp(pstmtGetCompo, rsGetCompo);
    
    return ret;
}
%>   

<%
if(request.getParameter("Submit")!= null){
    
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
    
    
    for(int i = 0 ; i < 5 ; i++){
        String selA, majorA, minorA, buildA, patchA = "";
        String selB, majorB, minorB, buildB, patchB = "";
        
        selA = request.getParameter("selectA" + (i + 1));
        majorA = request.getParameter("majorA" + (i + 1));
        minorA = request.getParameter("minorA" + (i + 1));
        buildA = request.getParameter("buildA" + (i + 1));
        patchA = request.getParameter("patchA" + (i + 1));
        
        selB = request.getParameter("selectB" + (i + 1));
        majorB = request.getParameter("majorB" + (i + 1));
        minorB = request.getParameter("minorB" + (i + 1));
        buildB = request.getParameter("buildB" + (i + 1));
        patchB = request.getParameter("patchB" + (i + 1));
               
        
        if( checkEmpty(selA, majorA, minorA, buildA, patchA) ){
//insert into DB if not already exists
            int insFlag = 0;
            int existsFlag = 0;
            
            existsFlag = checkExists(mysqlCon, selA, majorA, minorA, buildA, patchA);
            if(existsFlag == 0){
                insFlag = insNotExists(mysqlCon, selA, majorA, minorA, buildA, patchA);
            }else{
                out.println("BE Component already exists in DB!");
            }
            
            if(insFlag > 0){
                out.println("BE Component information added : Major " + majorA + " Minor " + minorA  + " Build " + buildA + " Patch " + patchA );
            }
            
        }
        
        if( checkEmpty(selB, majorB, minorB, buildB, patchB) ){
//insert into DB if not already exists
            int insFlag = 0;
            int existsFlag = 0;
            
            existsFlag = checkExists(mysqlCon, selB, majorB, minorB, buildB, patchB);
            if(existsFlag == 0){
                insFlag = insNotExists(mysqlCon, selB, majorB, minorB, buildB, patchB);
            }else{
                out.println("FE Component already exists in DB!");
            }
            
            if(insFlag > 0){
                out.println("FE Component information added : Major " + majorB + " Minor " + minorB  + " Build " + buildB + " Patch " + patchB );
            }
            
        }
    }
    
    
}
%>            


<tiles:insert definition="vmsHeader" >
    <tiles:put name="title">Version Management System :: Add Components</tiles:put>
</tiles:insert>
<sql:setDataSource
    driver= "<%= MYSQL_CONNECTION_STRING %>"
    url="<%= myDBURL %>"
    user="<%= MYSQL_USERNAME %>"
    password="<%= MYSQL_PASSWORD %>"
    var="dataSource" />

<%@include file = "../layout/vmsMenuHeader.jsp"%>

</head>
<body>
</br>
<div  id="subHeader">
     <h3>Enter the component type and their version :</h3>
</div>
<br/>
<form name="frmComponents" action="VMSListComponentsAction.do" method="GET">
   <table width="100%">
   	<tr>
   	<td>
       <h4>Backend Domain</h4>
       <table width="98%" class="table">
   	<tr>
   		<th colwidth="2">Component</th>
   		<th>Major</th>
   		<th>Minor</th>
   		<th>Build</th>
   		<th>Patch</th>
	</tr> 
    <sql:query var="components"  dataSource="${dataSource}">
	SELECT GID, COMPONENT FROM vms_components WHERE BE = 1 ORDER BY COMPONENT
    </sql:query> 
    <%
    for(int i=0; i<5; i++){
    %>
	<tr>
		<td colwidth="2">
			<select name="selectA<%=i+1%>">
			<option value="0">--SELECT--</option>
	<c:forEach var = "row" items="${components.rows}">
			<option value = "<c:out value = "${row.GID}"/>"> <c:out value = "${row.COMPONENT}"/>
	</c:forEach>    
			</select>
		</td>
		<td><input type="text" name="majorA<%= i+1 %>" size="3"></td>
		<td><input type="text" name="minorA<%= i+1 %>" size="3"></td>
		<td><input type="text" name="buildA<%= i+1 %>" size="3"></td>
		<td><input type="text" name="patchA<%= i+1 %>" size="3"></td>
	</tr>
     <% } %>
                    
	</table>
</td>
<td>
<h4>FrontEnd Domain</h4>
	<table  width="98%" class="table" border="0">
		  
		<tr>
                        <th colwidth="2">Component</th>
                        <th>Major</th>
                        <th>Minor</th>
                        <th>Build</th>
                        <th>Patch</th>
		</tr> 
	<sql:query var  = "componentsB"  dataSource="${dataSource}">
	SELECT GID, COMPONENT FROM vms_components WHERE BE = 0 ORDER BY COMPONENT
	</sql:query> 
	<%
	for(int j = 0 ; j < 5; j++) {
	%>                    
		<tr>
                        <td colwidth="2">
			<select name="selectB<%= j+1 %>">
			<option value="0">--SELECT--</option>
		<c:forEach var = "rowB" items="${componentsB.rows}">
			<option value="<c:out value = '${rowB.GID}'/>"> <c:out value="${rowB.COMPONENT}"/></option>
		</c:forEach>    
			</select>
                        </td>
                        <td><input type="text" name="majorB<%= j+1 %>" size="3"></td>
                        <td><input type="text" name="minorB<%= j+1 %>" size="3"></td>
                        <td><input type="text" name="buildB<%= j+1 %>" size="3"></td>
                        <td><input type="text" name="patchB<%= j+1 %>" size="3"></td>
		</tr>
	<% 
	}
	%>                    
	</table>                
	</td>
    </tr></table>
    <br>
    <div class = 'buttons'>
        <input type="submit" name="Submit" value="Submit" class="button">        
    </div>               
</form>

</body>
</html>
