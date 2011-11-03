<%@include file = "../layout/vmsCommonBase.jsp"%>
<%@page import="java.util.TreeMap,java.util.Iterator,java.util.Set,java.util.Map"%>
<tiles:insert definition="vmsHeader" >
    <tiles:put name="title">Version Management System :: Components Casewise</tiles:put>
</tiles:insert>
<script  type="text/javascript">  
        function displayComponents(){                                   
       // alert(document.SelectCaseForm.serverGid.value);   
        document.SelectCaseForm.actionType.value = 'case';
        document.SelectCaseForm.submit();
        return false;
        };
        
        function showServer(servergid){
	        document.ServerForm.actionType.value = 'server';
	        document.ServerForm.serverGid.value = servergid;        
	        document.ServerForm.submit();
        }
</script>
<%@include file = "../layout/vmsMenuHeader.jsp"%>
</head>
<body>
    <html:errors/>
    <script type='text/javascript' src='htdocs/js/common.js'></script>
    <script type='text/javascript' src='htdocs/js/css.js'></script>
    <script type='text/javascript' src='htdocs/js/standardista-table-sorting.js'></script>
    <% try { %>
    <br/>
    <h4>  Case(s) List:</h4>
    <br/>
    <table >
            <tr><td>
    <form name = "SelectCaseForm" method = "post" action = "VMSCaseAction.do">
        <input type = "hidden" name = "actionType" value = "case"/>                 
        <bean:define id="foundCase" value="false" />
        
        
                    <select name = "caseGid"  multiple size="8" style="width:250px">                        
                        <logic:present scope = "request" name = "CASEBEANS">
                            <logic:present scope = "request" name = "LASTCASES">
                                <logic:iterate id = "lastCase" name = "LASTCASES">
                                    <logic:equal name = "lastCase" value = "-1">
                                        <bean:define id="foundCase" value="true" />
                                    </logic:equal>
                                </logic:iterate>			    	    
                            </logic:present>
                            <logic:present name="foundCase" >
                                <logic:equal name="foundCase" value="true">
                                    <option value = "-1" selected>--ALL CASES--</option>
                                </logic:equal>
                                <logic:notEqual name="foundCase" value="true">
                                    <option value = "-1">--ALL CASES--</option>
                                </logic:notEqual>
                            </logic:present>
                            <logic:notPresent name="foundCase" >
                                <option value = "-1" >--ALL CASES--</option>
                            </logic:notPresent>
                            <bean:define id="foundCase" value="false" />
                            <logic:iterate id = "oneCase" name = "CASEBEANS">
                                <logic:present scope = "request" name = "LASTCASES">
                                    <logic:iterate id = "lastCase" name = "LASTCASES">
                                        <logic:equal name = "oneCase" property = "caseGid" value = "<%=lastCase.toString()%>">
                                            <bean:define id="foundCase" value="true" />
                                        </logic:equal>
                                    </logic:iterate>                                    
                                </logic:present>
                                <logic:equal name="foundCase" value="true">
                                    <option value = '<bean:write name = "oneCase" property= "caseGid"/>' selected><bean:write name = "oneCase" property= "caseName"/></option>   
                                </logic:equal>
                                <logic:notEqual name="foundCase" value="true">
                                    <option value = '<bean:write name = "oneCase" property= "caseGid"/>'><bean:write name = "oneCase" property= "caseName"/></option>
                                </logic:notEqual>
                                <bean:define id="foundCase" value="false" />
                            </logic:iterate>                                                        
                        </logic:present>                        
                    </select>
                </td><td>
                    <input type="button" name="Go" value="Go" onclick="displayComponents();">            
    </form>
    </td></tr>
        </table>    
        <table align = 'left' width = '100%'>
        
            <tr>
                <td bgcolor="#999999"></td><td>: indicates incompatible set of components.</td>
            </tr>
       
        <%
        TreeMap serverComponents = null;
        TreeMap outCastes = null;
        if(null != request.getAttribute("SERVERCOMPONENTS")){
            serverComponents = (TreeMap) request.getAttribute("SERVERCOMPONENTS");
            
            
        }else{
            
        }
        
        if(request.getAttribute("OUTCASTES") != null) {
            outCastes = (TreeMap)request.getAttribute("OUTCASTES");
            
        }else{
            
        }
        
        if(request.getAttribute("OUTCASTES") != null) {
            Set caseSet = outCastes.entrySet();
            Iterator itr = caseSet.iterator();
            
            while(itr.hasNext()){
                String caseName = null;
                ArrayList serverComponentsCase = null;
                ArrayList caseOutCastes = null;
                int compoCount = 0;
                VMSServerComponent testCompo = null;
                
                
                Map.Entry me = (Map.Entry)itr.next();
                caseName = (String) me.getKey();
                caseOutCastes = (ArrayList)me.getValue();
                serverComponentsCase = (ArrayList)serverComponents.get(caseName);
                compoCount = caseOutCastes.size() + serverComponentsCase.size();
                if(0 != caseOutCastes.size()){
                testCompo = (VMSServerComponent)caseOutCastes.get(0);
                }else{
                testCompo = (VMSServerComponent)serverComponentsCase.get(0);
                }   
                
        %>         
   
    
         <tr>
            <td align = 'left' colspan = '2'> <h4><%= caseName %></h4></td>
        </tr>
         <tr>
	             <td align = 'left' colspan = '2'> <h5>HIERARCHY GID : <%= testCompo.getHierarchyGid() %></h5></td>
	         </tr>
         
        
        <tr>         
            <td align = 'right' colspan = '2'>
                No of components for <%= caseName %> : <%= compoCount %>     
            </td>               
        </tr>
    
    
    <tr>
    <td colspan = '2'>
    <table class = "dataTable sortable" width = "100%" align = 'left'>                   
        <thead>
            <tr>
                <th>Case</th><th>URL</th><th>Server</th><th>Component</th><th>Version</th><th>Port/Details</th>
            </tr>
        </thead>
        <tbody>
            <%
            for(int i = 0; i < caseOutCastes.size(); i++){
                    VMSServerComponent sc = (VMSServerComponent)caseOutCastes.get(i);                
            %>                                                                   
            <tr><td bgcolor="#999999"><%=sc.getCaseName()%></td><td bgcolor="#999999"><a href= "<%=sc.getUrl()%>" target = "_blank"><%=sc.getUrl()%></a></td>                        
                <td bgcolor="#999999"><a href= "javascript:showServer(<%=sc.getServerGid()%>)"><%=sc.getServerName()%></a></td>                        
            <td bgcolor="#999999"><%=sc.getComponent()%></td><td  bgcolor="#999999"><%=sc.getVersion()%></td><td bgcolor="#999999" ><%=sc.getPort()%></td></tr>                                                            
            <%        
            }
                
                
                if(null != serverComponentsCase){
                    for(int j = 0 ; j < serverComponentsCase.size(); j++){
                        VMSServerComponent valids = (VMSServerComponent)serverComponentsCase.get(j);
            %>                                                                          
            <tr><td  ><%=valids.getCaseName()%></td><td><a href= "<%=valids.getUrl()%>" target = "_blank"><%=valids.getUrl()%></a></td>                                               
                <td ><a href= "javascript:showServer(<%=valids.getServerGid()%>)"><%=valids.getServerName()%></a></td>                       
            <td ><%=valids.getComponent()%></td><td><%=valids.getVersion()%></td><td ><%=valids.getPort()%></td></tr>                                                                    
            <%
                    }
                }
            %>
        </tbody>                   
    </table>
    </td>
    </tr>    
    <%
            }
    %>
    
        <tr>
            <td bgcolor="#999999"></td><td>: indicates incompatible set of components.</td>
        </tr>    	
    
    </table>
    <%
        }
    %>
    
    <form name = "ServerForm" method = "get" action= "VMSServerAction.do">
        <input type = "hidden" name = "actionType">
        <input type = "hidden" name = "serverGid">
    </form>
    <% } catch (Throwable t) {
        out.println(t.getMessage());
        t.printStackTrace();
        return;
    } %>
</body>
</html>
