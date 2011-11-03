<%@include file = "../layout/vmsCommonBase.jsp"%>

<tiles:insert definition="vmsHeader" >
    <tiles:put name="title">Version Management System :: Components Serverwise</tiles:put>
</tiles:insert>
<script  type="text/javascript">  
        function displayServer(){                                   
       	//alert(document.VMSServerActionForm.serverGid.value);   
       	//alert(document.ServerActionForm.actionType.value);   
       	document.VMSServerActionForm.actionType.value = 'server';
        document.VMSServerActionForm.submit();
        return false;
        };
        
        function showCase(casegid){
        document.CaseForm.actionType.value = 'case';
        document.CaseForm.caseGid.value = casegid;        
        document.CaseForm.submit();
        }
</script>

<%@include file = "../layout/vmsMenuHeader.jsp"%>

</head>
<body> 
<script type='text/javascript' src='htdocs/js/common.js'></script>
<script type='text/javascript' src='htdocs/js/css.js'></script>
<script type='text/javascript' src='htdocs/js/standardista-table-sorting.js'></script>
<html:errors/>
<br/>
<div >
    <h4>Server(s) List:</h4>
</div>
<br/>  
<html:form action = "VMSServerAction">
    <logic:present scope="request" name = "SERVERS">                   
        <logic:present scope="request" name = "LASTSERVER">                
            <html:hidden property="actionType" value="server"/>  
            <%                
            int lastServer = ((Integer)request.getAttribute("LASTSERVER")).intValue();               
            %>                                               
            <table class = "formTable" align = 'left'>
                <tr><td>                           
                        <html:select property="serverGid" onchange="displayServer();">
                            <option value = '0'>--SELECT SERVER--</option>
                            <logic:iterate id = "servers" name = "SERVERS" >
                                <logic:equal name = "servers" property="gid" value = "<%= Integer.toString(lastServer) %>"> 
                                    <option value = '<bean:write name = "servers" property = "gid" />' selected><bean:write name = "servers" property= "name"/></option>
                                </logic:equal>
                                <logic:notEqual name = "servers" property="gid" value = "<%= Integer.toString(lastServer) %>">
                                    <option value = '<bean:write name = "servers" property = "gid" />' ><bean:write name = "servers" property= "name"/></option>
                                </logic:notEqual>
                            </logic:iterate> 
                        </html:select>                            
                </td></tr>
            </table>
        </logic:present>
        <logic:notPresent scope="request" name = "LASTSERVER">
            <html:hidden property="actionType" value="server"/>                
            <table class = "formTable" align = 'left'>
                <tr><td>
                        <html:select property="serverGid" onchange="displayServer();"> 
                            <option value = '0'>--SELECT SERVER--</option>
                            <html:options collection="SERVERS" property="gid" labelProperty="name"/>                                
                        </html:select>
                </td></tr>
            </table>
        </logic:notPresent> 
    </logic:present>                   
</html:form>     
<logic:present scope="request" name = "SERVERS">        
    <logic:present scope= "request" name = "LASTSERVER">           
        <logic:present scope = "request" name= "CASECOMPONENTS">
            <br/>
            <div >
                <br/><br/><h4>Components installed on the selected machine:</h4>
            </div>
            
            <br/>
            
            <table id = 'fullheight' width = '100%' align = 'left'  >                
                <tr colspan = '2'> 
                    <td align = 'right'>
                    &nbsp;</td>
                    <td align = 'right'><logic:present scope="request" name = "CASECOUNT">
                            No of unique cases : <bean:write name = "CASECOUNT"/>
                        </logic:present>
                    </td>               
               </tr>
                <tr><td>
                    <table class="dataTable" align = 'right' >
                        <tr><th>SNo</th></tr>
                        <%  int sno = 0; %>                 
                        <logic:iterate id="components" name="CASECOMPONENTS">
                            <tr>
                                <td><%= ++sno %></td>			                            
                            </tr>
                        </logic:iterate>
                    </table>
                </td>
                <td>
                <table class="dataTable sortable" align = 'left' width = '99%'>
                <thead>
                    <tr>
                        <th>Case</th><th>Component</th><th>Version</th><th>Port/Details</th>
                    </tr>
                </thead>
                <tbody>                   
                    <logic:iterate id="components" name="CASECOMPONENTS">
                        <tr>                            
                            <td><a href="javascript: showCase(<bean:write name='components' property = 'caseGid'/>)">
                                    <bean:write name = "components" property = "caseName"/>                            
                            </a></td>  
                            <td><bean:write name = "components" property = "component"/></td>  
                            <td><bean:write name = "components" property = "version"/></td> 
                            <td><bean:write name = "components" property = "port"/></td>  
                        </tr>
                    </logic:iterate>
                </tbody>
            </logic:present>
        </logic:present>
    </logic:present>      
</table> 
</td>
</table>
<form name = "CaseForm" method = "get" action= "VMSCaseAction.do">
    <input type = "hidden" name = "actionType">
    <input type = "hidden" name = "caseGid">
</form>
</body>
</html>
