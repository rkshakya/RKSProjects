<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


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
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <sql:setDataSource
            driver= "<%= MYSQL_CONNECTION_STRING %>"
            url="<%= myDBURL %>"
            user="<%= MYSQL_USERNAME %>"
            password="<%= MYSQL_PASSWORD %>"
            var="dataSource" />
        
        
    </head>
    <script>
		function onCheck(Save)
		{
		  var total = 0;
		  for (var i=0;i < document.compoForm.selectCompoChild.length;i++)
		  if (document.compoForm.selectCompoChild[i].selected)
			total++;
                     if (total > 1) {
                             Save.disabled = false
                     } else {
                             Save.disabled = true
                     }
		}

		

    </script>
    <body>
        <%!
        //declarations here
        %>
        
        <%
        int ruleNo = -1;
        try {
            ruleNo = Integer.parseInt(request.getParameter("ruleNo"));
        }catch(Exception e){}
        
        out.println("Rule No : " + ruleNo);
        
         String cmd = request.getParameter("cmd");
        out.println("CMD: " + cmd);
        
        if(ruleNo == -1) {
        out.println("Please select a rule to modify the tag constraints");
        return;
        }
         
        
        
       
        %>
        <FORM name='compoForm' action='./componentRules.jsp' method='get' target='_self'>
            <INPUT type='hidden' name='ruleNo' value='<%=ruleNo%>' > </INPUT>
            <INPUT type='hidden' name='cmd' value='apply' > </INPUT>
            
            <sql:query var  = "componentsChild"  dataSource="${dataSource}">
                SELECT A.GID, A.MAJOR, A.MINOR, A.BUILD, A.PATCH, B.COMPONENT
                FROM vms_compo_version A, vms_components B
                WHERE A.COMP_GID = B.GID 
                ORDER BY B.COMPONENT
            </sql:query> 
            
            <td><SELECT NAME="selectCompoChild" size="20" MULTIPLE>                                                              
                    
                    <c:forEach var = "rowCompoChild"  items="${componentsChild.rows}">
                        <option value = "<c:out value = "${rowCompoChild.GID}"/>" onclick="onclick='onValidCheck(document.compoForm.Save),onValidCheck(parent.document.frmCompatibility.Save)"> <c:out value = "${rowCompoChild.COMPONENT} ${rowCompoChild.MAJOR}.${rowCompoChild.MINOR}.${rowCompoChild.BUILD}.${rowCompoChild.PATCH}"/>
                    </c:forEach>    
            </SELECT></td>
            
            <%
            //part to show select options
            %>
            
            
            <INPUT type='submit' name='Save' disabled  value='Save' > </INPUT>
            <script>
	document.compoForm.Save.disabled = true;
	document.compoForm.Save.style.visibility = 'hidden';
	parent.document.frmCompatibility.Save.disabled = true;
            </script>
        </FORM>
        
    </body>
</html>
