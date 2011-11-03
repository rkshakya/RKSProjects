<%@include file = "../layout/vmsCommonBase.jsp"%>

<tiles:insert definition="vmsHeader" >
    <tiles:put name="title">Version Management System :: List Rules</tiles:put>
</tiles:insert>     
<script  type="text/javascript">
        function displayComponents(){
		document.ListForm.deleteAction.value = 'test';
		document.ListForm.submit();        
        }
        
        function addRule(){        
            document.ListForm.deleteAction.value = 'add';
            document.ListForm.submit(); 
        }
         
        function deleteRule(){        
	       	if(document.forms[0].ruleNum.options.selectedIndex == -1){
	       		alert("Please select a rule to delete.");
	       		return false;
	       	}else{
	            document.ListForm.deleteAction.value = 'delete';
	            document.ListForm.submit();
		}
        }
        
        function editRule(){        
       	 if(document.forms[0].ruleNum.options.selectedIndex == -1){	 	
            	alert("Please select a rule to edit.");
            	return false;
            }else{
            	document.ListForm.deleteAction.value = 'edit';
            	document.ListForm.submit();
            }
        }
</script>
</head>         

<body>  
<%@include file = "../layout/vmsMenuHeader.jsp"%>
<html:errors/>
<logic:present name = "MESSAGE" scope = "request">
    <div class="info" align="left"> 
        &nbsp;&nbsp;<bean:write name = "MESSAGE"/>
    </div>
    <br><br>
</logic:present>
<form action = "VMSListRuleAction.do" method = "get" name = "ListForm">
<html:hidden property="actionType" value = "display"/>
<html:hidden property="deleteAction" value="test"/>
</br>
<table class = 'formTable' width = '70%' align = 'center'>
<tr><th>Rule Name</th><th>Components</th></tr>
<tr><td> 
    <%	        
	        int lastRule = 0;	        
	        if(null != request.getAttribute("LASTRULE")){
	            lastRule = ((Integer)request.getAttribute("LASTRULE")).intValue();
        }
    %>     
    <select name = "ruleNum" size="20" style="width:250px" onchange="displayComponents();" id = "ruleNum">              
        <logic:present name = "RULES" scope = "request">
            <logic:iterate id = "rules" name = "RULES" scope = "request">
                <logic:equal name = "rules" property="rule" value = "<%= new Integer(lastRule).toString() %>">
                    <option value = '<bean:write name = "rules" property = "rule" />' selected><bean:write name = "rules" property= "ruleName"/></option>
                </logic:equal>
                <logic:notEqual name = "rules" property="rule" value = "<%= new Integer(lastRule).toString() %>">
                    <option value = '<bean:write name = "rules" property = "rule" />'><bean:write name = "rules" property= "ruleName"/></option>
                </logic:notEqual>
            </logic:iterate>            
        </logic:present>		
        
    </select>     
    <td>
        <select name = "rcomponents" size = 20 style="width:250px">
            <logic:present name = "RCOMPOBEAN" scope = "request">
                <logic:iterate id="rcomponent" name="RCOMPOBEAN" scope="request">
                    <option value = "<bean:write name='rcomponent' property='compoVerGID'/>"> <bean:write name="rcomponent" property="completeCompoName" /></option>
                </logic:iterate>                        	
            </logic:present>
        </select>
    </td>
    <tr><th colspan="2">Rule Description</th></tr>
    <tr><td> <logic:present name = "LASTRULEOBJ" scope = "request">                
                &nbsp;&nbsp;<bean:write name = "LASTRULEOBJ" property="ruleDescription" />                               
            </logic:present>
            <logic:notPresent name = "LASTRULEOBJ" scope = "request">                
                NA                              
        </logic:notPresent></td></th>
</td></tr>

</tr>
</table>
<br>
<table width = '70%' align = 'left' border="0">
    <tr><td>
            <div class = 'buttons' align="left">
                <% if (lastRule <= 0) {%>                
                <input type = 'button' name = 'Edit' value = 'Edit' onclick="editRule();" class = 'button' disabled/>
                <input type = 'button' name = 'Delete' value = 'Delete' onclick="deleteRule();" class = 'button' disabled/>                
                <%}else{%> 
                <input type = 'button' name = 'Edit' value = 'Edit' onclick="editRule();" class = 'button'/>
                <input type = 'button' name = 'Delete' value = 'Delete' onclick="deleteRule();" class = 'button'/>
                <%}%>
                <input type = 'button' name = 'Add New Rule' value= "Add new rule" onclick = "addRule();" class = 'button'/>
            </div>
    </td></tr>
</table>

</form>
</body>
</html>
