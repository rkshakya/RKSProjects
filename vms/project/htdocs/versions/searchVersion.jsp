<%@include file = "../layout/vmsCommonBase.jsp"%>

<tiles:insert definition="vmsHeader" >
    <tiles:put name="title">Version Management System :: Components Versionwise</tiles:put>
</tiles:insert>

<%@include file = "../layout/vmsMenuHeader.jsp"%>
<script type='text/javascript'>
        function validate(){
        if( (document.forms[0].major.value == '') && (document.forms[0].minor.value == '') && (document.forms[0].build.value == '') && (document.forms[0].patch.value == '')){	 	
            	alert("Please select an option from either of Major/Minor/Build/Patch dropdowns.");
            	return false;
            }else{                
                return true;               
            }
          }
    </script>

</head>
<body>
    <script type='text/javascript' src='htdocs/js/common.js'></script>
    <script type='text/javascript' src='htdocs/js/css.js'></script>
    <script type='text/javascript' src='htdocs/js/standardista-table-sorting.js'></script>
            
    <html:errors/>
    </br>
    <div align = 'left'>
        <h4>Version information:</h4>  
    </div>
    </br>
    <html:form action = "VMSVersionAction">    
        <html:hidden property="actionType" value = "version"/> 
        <logic:present scope="request" name = "MAJORS"> 
            <table width = '60%'>
                <th>Major</th><th>&nbsp;</th><th>Minor</th><th>&nbsp;</th><th>Build</th><th>&nbsp;</th><th>Patch</th><th>&nbsp;</th><th>Component</th>
                <tr>
                    <td>
                        <html:select property="major">
                            <option value = ''>--MAJOR--</option>
                            <html:options collection="MAJORS" property="version"/>                                
                        </html:select>                        
                    </td>
                    <td>&nbsp;</td>
                    <td>
                        <html:select property="minor"> 
                            <option value = ''>--MINOR--</option>
                            <html:options collection="MINORS" property="version"/>                                
                        </html:select>     
                    </td>
                    <td>&nbsp;</td>
                    <td>
                        <html:select property="build">  
                            <option value = ''>--BUILD--</option>
                            <html:options collection="BUILDS" property="version"/>                                
                        </html:select>    
                    </td>
                    <td>&nbsp;</td>
                    <td>
                        <html:select property="patch"> 
                            <option value = ''>--PATCH--</option>
                            <html:options collection="PATCHS" property="version"/>                                
                        </html:select>    
                    </td>
                    <td>&nbsp;</td>
                    <td>
                        <logic:present scope = "request" name = "COMPONENTS">
                            <logic:present scope = "request" name = "LASTCOMPONENT">
                                <%                
                                int lastComponent = 0;
                                lastComponent = ((Integer)request.getAttribute("LASTCOMPONENT")).intValue();               
                                %>                                   
                                <html:select property="componentGid">
                                    <option value = '0'>--ANY COMPONENT--</option>
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
                            
                            <logic:notPresent scope = "request" name = "LASTCOMPONENT">
                                <html:select property="componentGid"> 
                                    <option value = '0'>--ANY COMPONENT--</option>
                                    <logic:iterate id = "components" name = "COMPONENTS" >
                                        <logic:equal name = "components" property="componentName" value = "DatabaseServer_BE"> 
                                            <option value = '<bean:write name = "components" property = "gid" />' selected><bean:write name = "components" property= "componentName"/></option>
                                        </logic:equal>
                                        <logic:notEqual name = "components" property="componentName" value = "DatabaseServer_BE">
                                            <option value = '<bean:write name = "components" property = "gid" />' ><bean:write name = "components" property= "componentName"/></option>
                                        </logic:notEqual>
                                    </logic:iterate> 
                                </html:select>
                            </logic:notPresent>
                        </logic:present>                                        
                    </td>
                    <td>&nbsp;</td>
                    <td><html:submit value="Go" onclick = "return validate();"/></td>                            
                </tr>                            
            </table>
        </logic:present>
    </html:form>
    <br/>
    <logic:present scope="request" name="VERSIONCOMPONENTS">                   
        <table id = 'fullheight' align = 'left' width = '80%'>
            <tr colspan = '2'> 
                <td align = 'right'>
                &nbsp;</td><td align = 'right'><logic:present scope="request" name="CASECOUNTS"> 
                        No of unique cases : <bean:write name = "CASECOUNTS"/>
            </logic:present></td> </tr>
            <tr><td>
                    <table class="dataTable" align = 'right'>
                        <tr><th>SNo</th></tr>
                        <%  int sno = 0; %>                 
                        <logic:iterate id="components" name="VERSIONCOMPONENTS">
                            <tr>
                                <td><%= ++sno %></td>			                            
                            </tr>
                        </logic:iterate>
                    </table>
                </td>
                <td align = 'left'>        
                    <table class = 'dataTable sortable' width = '98%' align = 'left'>
                        <thead>
                            <tr>
                                <th>Case</th><th>Server</th><th>Component</th><th>Version</th><th>Domain</th><th>Port/Details</th> 
                            </tr> 
                        </thead>
                        <tbody>
                            <logic:iterate id="components" name="VERSIONCOMPONENTS">            
                                <tr><td><bean:write name = "components" property = "casename"/></td>
                                    <td><bean:write name = "components" property = "servername"/></td>
                                    <td><bean:write name = "components" property = "componentname"/></td>
                                    <td><bean:write name = "components" property = "version"/></td>                    
                                    <td><bean:write name = "components" property = "domain"/></td>
                                    <td><bean:write name = "components" property = "port"/></td>                    
                                </tr>
                            </logic:iterate> 
                        </tbody>
                        
                    </table>
                </td>
            </tr>
        </table>
    </logic:present> 
    <br/>
    <br/>
</body>
</html>
