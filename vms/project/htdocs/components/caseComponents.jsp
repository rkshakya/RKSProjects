<%@include file = "../layout/vmsCommonBase.jsp"%>
<tiles:insert definition="vmsHeader" >
    <tiles:put name="title">Version Management System :: Case Component Combination</tiles:put>
</tiles:insert>
<%@include file = "../layout/vmsMenuHeader.jsp"%>
</head>
<body> 
    <script type='text/javascript' src='htdocs/js/common.js'></script>
    <script type='text/javascript' src='htdocs/js/css.js'></script>
    <script type='text/javascript' src='htdocs/js/standardista-table-sorting.js'></script>
    <html:errors/>
    </br>
    <!--div align="left">
        <h4><a href = "VMSListComponentsAction.do">Add components</a></h4>
    </div-->
    <div>        
        <h4>Case/Component List:</h4><br/>
    </div>
    </br>
    <html:form action = "VMSCaseComponentAction">
        <html:hidden property = "actionType" value = "casecomponent"/>
        <table align = 'left'>
            <tr><td>
                    <logic:present scope = "request" name = "CASES">
                        <logic:present scope = "request" name = "LASTCASE">
                            <%                
                            int lastCase = 0;
                            lastCase = ((Integer)request.getAttribute("LASTCASE")).intValue();               
                            %>    
                            <html:select property="caseGid">	
                                <option value = '0'>--SELECT CASE--</option>
                                <logic:iterate id = "cases" name = "CASES" >                                
                                    <logic:equal name = "cases" property="caseGid" value = "<%= Integer.toString(lastCase) %>"> 
                                        <option value = '<bean:write name = "cases" property = "caseGid" />' selected><bean:write name = "cases" property= "caseName"/></option>
                                    </logic:equal>
                                    <logic:notEqual name = "cases" property="caseGid" value = "<%= Integer.toString(lastCase) %>">
                                        <option value = '<bean:write name = "cases" property = "caseGid" />' ><bean:write name = "cases" property= "caseName"/></option>
                                    </logic:notEqual>
                                </logic:iterate> 
                            </html:select>  
                        </logic:present>                         
                        <logic:notPresent scope = "request" name = "LASTCASE">
                            <html:select property="caseGid"> 
                                <option value = '0'>--SELECT CASE--</option>
                                <html:options collection="CASES" property="caseGid" labelProperty="caseName"/>                                
                            </html:select>
                        </logic:notPresent>
                    </logic:present>
                </td><td>&nbsp;</td><td>
                    <logic:present scope = "request" name = "COMPONENTS">
                        <logic:present scope = "request" name = "LASTCOMPONENT">
                            <%                
                            int lastComponent = 0;
                            lastComponent = ((Integer)request.getAttribute("LASTCOMPONENT")).intValue();               
                            %>    
                            <html:select property="componentGid">
                                <option value = '0'>--SELECT COMPONENT--</option>
                                <logic:iterate id = "components" name = "COMPONENTS" >
                                    <logic:equal name = "components" property="gid" value = "<%= Integer.toString(lastComponent) %>"> 
                                        <option value = '<bean:write name = "components" property = "gid" />' selected><bean:write name = "components" property= "componentName"/></option>
                                    </logic:equal>
                                    <logic:notEqual name = "components" property="gid" value = "<%= Integer.toString(lastComponent) %>">
                                        <option value = '<bean:write name = "components" property = "gid" />' ><bean:write name = "components" property= "componentName"/></option>
                                    </logic:notEqual>
                                </logic:iterate> 
                            </html:select>  
                        </logic:present> 
                        
                        <logic:notPresent scope = "request" name = "LASTCASE">
                            <html:select property="componentGid"> 
                                <option value = '0'>--SELECT COMPONENT--</option>
                                <html:options collection="COMPONENTS" property="gid" labelProperty="componentName"/>                                
                            </html:select>
                        </logic:notPresent>
                    </logic:present>                                        
                </td>
                <td>
                    <html:submit value = "Go"/>                    
                </td> 
            </tr>    
        </table>
    </html:form> 
    <logic:present scope = "request" name= "CASECOMPONENTS">    
        </br><br><br>
        <div align = 'left'>
            <h4>Information for the selected case and component:</h4>
        </div>
        
        <table align = 'left' width = '80%' >
            <tr colspan = '2'> 
                <td align = 'right'>
                &nbsp;</td><td align = 'right'> <logic:present scope="request" name="CASECOUNT"> 
                        No of unique cases : <bean:write name = "CASECOUNT"/>
            </logic:present></td> </tr>
            <tr><td>
                <table class="dataTable" align = 'right' border = '1'>
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
                <table class = "dataTable sortable" width = "98%" align = 'left'>
                    <thead>
                        <tr>
                            <th>Case</th><th>Server</th><th>Component</th><th>Version</th><th>Domain</th><th>Port/Details</th>                 
                        </tr>
                    </thead>
                    <tbody>
                        <logic:iterate id="components" name= "CASECOMPONENTS">
                            <tr><td ><bean:write name = "components" property = "casename"/> </td>
                                <td nowrap><bean:write name = "components" property = "servername"/></td>
                                <td nowrap><bean:write name = "components" property = "componentname"/></td>
                                <td><bean:write name = "components" property = "version"/></td>
                                <td><bean:write name = "components" property = "domain"/></td>
                            <td><bean:write name = "components" property = "port"/></td></tr>
                        </logic:iterate>
                    </tbody>
                </table>
            </td>
        </table>
    </logic:present>     
</body>
</html>  
