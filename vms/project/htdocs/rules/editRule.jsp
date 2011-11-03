<%@include file = "../layout/vmsCommonBase.jsp"%>

<tiles:insert definition="vmsHeader" >
    <tiles:put name="title">Version Management System :: Edit Rule</tiles:put>
</tiles:insert>
<script  type="text/javascript">  

	function saveRule(){
		
		if (!validateEntry()) return false;
	
		var z = document.getElementById("thisComponent");
		var m = 0;
		for(m = z.length - 1; m >= 0; m--){
			z.options[m].selected = 'true';
		}

		document.RuleEditForm.editType.value = 'save';
		document.RuleEditForm.submit();
		return false;
        }

	function validateEntry() {
		var z = document.getElementById("thisComponent");
                var ruleName = document.forms[0].ruleName.value;
	
		if (z.options.length < 2) {
			alert('Please select a minimum of two components to form a compatibility rule');
			return false;
		}
                
                if(ruleName == ""){
                    alert('Rule Name field cannot be empty.');
			return false;
		}                
                                		
		return true;
	}

	function removeElement(){
		var x = document.getElementById("thisComponent");
		var y = document.getElementById("restComponent");
		var i = 0;
                for(i = x.length - 1; i >= 0; i--){
			if(x.options[i].selected){
				var newOption = document.createElement('option');
				newOption.text = x.options[i].text;
				newOption.value = x.options[i].value;

				try{
					y.add(newOption, null); //for compliant browsers
				}catch(ex){
					y.add(newOption);   //for IE
				}                                                                      

	                        x.remove(i);
			}
		}              
	}
                              
	function addElement(){
		var x = document.getElementById("restComponent");
		var y = document.getElementById("thisComponent");
		var i = 0;
                for(i = x.length - 1; i >= 0; i--){
			if(x.options[i].selected){
	                        var newOption = document.createElement('option');
	                        newOption.text = x.options[i].text;
	                        newOption.value = x.options[i].value;
	                        try{
		                        y.add(newOption, null); //for compliant browsers
	                        }catch(ex){
		                        y.add(newOption);   //for IE
	                        }                                                                      
	                        x.remove(i);
			}
		}                
	}
	
	function refreshPage() {
		document.forms[0].editType.value = '';
		document.forms[0].submit();
	}
	
</script>       

</head>
<body>
    <%@include file = "../layout/vmsMenuHeader.jsp"%>
    <html:errors/>
    <form action = "VMSEditRuleAction.do" method ="get" name = "RuleEditForm">
        <input type = "hidden" name="editType" value = ""/>
        <div align = 'left'>
            <h2>&nbsp;&nbsp;Edit Rule</h2>
        </div>
        <br>
            <table align = 'left'><tr><td>	
                    <logic:present name = "MESSAGE" scope = "request">
                        <div class="info" align="left">
                            &nbsp;&nbsp;<bean:write name = "MESSAGE"/>
                        </div>	
                        <br><br>
                    </logic:present>
                    <table align = "left">
                        <tr>                                                  
                            <td>Rule Name* :</td><td> <html:text name = "LASTRULEOBJ" property= "ruleName" size = "40"/></td>
                            <logic:present name = "LASTRULE" scope = "request">
                                <input type = "hidden" name = "ruleNum" value = '<bean:write name = "LASTRULE"/>'/>
                            </logic:present> 
                            <logic:notPresent name = "LASTRULE" scope = "request">
                                <input type = "hidden" name = "ruleNum" value = '0'/>
                            </logic:notPresent> 
                            
                        </tr>
                        <tr>
                            <td>Rule Description :</td><td> <html:textarea name = "LASTRULEOBJ" property= "ruleDescription" rows="3" cols= "40"/></td>
                        </tr>
                        <tr><td>&nbsp;</td><td><i>(Field marked * is mandatory.)</i></td></tr>
                    </table>
            </td></tr>
            <tr><td>
                    <table class = 'formTable' align = 'center' width="70%">            
                        <tr><th>Selected Component(s)</th><th></th><th>Other Components</th>
                        <tr>
                            <td width="40%">                                            
                                <select name = "rcomponents" multiple size = 15 id = "thisComponent">                                    
                                    <logic:present name = "THISCOMPONENTS" scope = "request">
                                        <logic:iterate id = "thiscomponents" name = "THISCOMPONENTS" scope = "request">                                                                                        
                                               <option value = "<bean:write name = 'thiscomponents' property = 'compoVerGID' />"><bean:write name = 'thiscomponents' property = 'completeCompoName' /></option>                                                                                       
                                        </logic:iterate>            
                                    </logic:present>
                                </select>
                            </td>
                            <td width="20%">
                                <table>
                                    <tr><td>
                                            <div class = 'buttons'>
                                                <input type = "button" class = 'addButton' value = ">>" onclick = "removeElement();">
                                            </div>    
                                    </td></tr>
                                    <tr><td>
                                            <div class = 'buttons'>
                                                <input type = "button" class= 'addButton' value = "<<" onclick= "addElement();">
                                            </div>
                                    </td></tr>
                                </table>
                            </td>
                            <td width="40%">                               
                                <select name = "restComponents" multiple size = 15 id = "restComponent" >
                                    <logic:present name = "RESTCOMPONENTS" scope = "request">
                                        <logic:iterate id = "restcomponents" name = "RESTCOMPONENTS" scope = "request">                                                                                        
                                                <option value = '<bean:write name = "restcomponents" property = "compoVerGID" />'><bean:write name = 'restcomponents' property = 'completeCompoName' /></option>                                            
                                        </logic:iterate>            
                                    </logic:present>
                                </select>
                            </td>
                        </tr>
                    </table>
            </td></tr>
        </br>
        <tr><td>
                <table width = '70%' align = 'left' border="0">
                    <tr><td><div class = 'buttons'>
                                <input type = "button" class = 'button' value = "Save" align="center" onclick = 'saveRule();'>
                                <input type = "button" class = 'button' value = "Reset" align="center" onclick = 'refreshPage();'>
                    </div></td></tr>
                </table>
        </td></tr>        
    </form>
    
</body>
</html>
