<%@include file = "../layout/vmsCommonBase.jsp"%>

<tiles:insert definition="vmsHeader" >
    <tiles:put name="title">Version Management System :: Add Rule</tiles:put>
</tiles:insert>
<script  type="text/javascript"> 
		 function sortFuncAsc(record1, record2) {
		 		            var value1 = record1.optText.toLowerCase();
		 		            var value2 = record2.optText.toLowerCase();
		 		            if (value1 > value2) return(1);
		 		            if (value1 < value2) return(-1);
		 		            return(0);
		 		        }
		 				       	
		 		function sortSelect(selectToSort, ascendingOrder) {
		 		            if (arguments.length == 1) ascendingOrder = true;    // default to ascending sort
		 		
		 		            // copy options into an array
		 		            var myOptions = [];
		 		            for (var loop=0; loop<selectToSort.options.length; loop++) {
		 		                myOptions[loop] = { optText:selectToSort.options[loop].text, optValue:selectToSort.options[loop].value };
		 		            }
		 		
		 		            // sort array
		 		            if (ascendingOrder) {
		 		                myOptions.sort(sortFuncAsc);
		 		            } else {
		 		                myOptions.sort(sortFuncDesc);
		 		            }
		 		
		 		            // copy sorted options from array back to select box
		 		            selectToSort.options.length = 0;
		 		            for (var loop=0; loop<myOptions.length; loop++) {
		 		                var optObj = document.createElement('option');
		 		                optObj.text = myOptions[loop].optText;
		 		                optObj.value = myOptions[loop].optValue;
		 		                selectToSort.options.add(optObj);
		 		            }
        		}
        
        
            function cleanList(){
            document.forms[0].ruleName.value = "";
            document.forms[0].ruleDescription.value = "";
            //cleans up selectList- activated by Clear btn
                var x = document.getElementById("selectList");
	        var y = document.getElementById("allList");
	        var i = 0;
	        for(i = x.length - 1; i >= 0; i--){	             
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
	        sortSelect(document.getElementById("allList"), true);
                               
               
            }
            
            function add(){
            var rulName = document.forms[0].ruleName.value;            
            
            var z = document.getElementById("selectList");
            if(document.forms[0].selectList.length >= 2){ 
                var m = 0;
                 for(m = z.length - 1; m >= 0; m--){
                    z.options[m].selected = 'true';
                }  
                
                if(rulName != ""){
           	document.VMSAddRuleForm.submit();
           	}else{
           		alert("Enter rule name.");
           	}
            }else{
            	alert("Select minimum 2 components.");                
            }           
        }
            function removeElement(){            
            var x = document.getElementById("selectList");
            var y = document.getElementById("allList");
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
                 
               
            };
                              
            function addElement(){
            var x = document.getElementById("allList");
            var y = document.getElementById("selectList");
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
                 
                
            };
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
    <form action = "VMSAddRuleAction.do" method = "get" name = "VMSAddRuleForm">
        <html:hidden property = "actionType" value = "add" /> 
        <table align = 'left'>
        <tr><td>
                <div align = 'left'>
                    <h2>&nbsp;&nbsp;Add new rule:</h2>
                    <table align = "left" width = '70%'>
                        <tr><td>Rule Name*:</td><td><input type = "text"name = "ruleName" size = "43"/></td></tr>
                        <tr><td>Description : </td><td><textarea name = "ruleDescription" rows = "3" cols = "40"></textarea></td></tr>
                        <tr><td>&nbsp;</td><td><i>(* indicates mandatory field)</i></td></tr>
                    </table>
                </div>
                <br>                    
        </td></tr>
        <tr><td>
                <table class = 'formTable' align ='center' width = '70%'>
                    <tr><th>Selected Component(s)</th><th></th><th>All Components</th>
                    <tr>
                    <td>
                        <select name = "selectList" id = "selectList" size = 20 multiple style="width:250px"> 
                        </select>
                    </td>
                    <td>
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
                    <td>
                        <select name = "allList" id = "allList" size = "20" multiple>
                            <logic:iterate id="component" name="COMPOBEANS" scope="request">
                                <option value = "<bean:write name='component' property='compoVerGID'/>"> <bean:write name="component" property="completeCompoName" /></option>
                            </logic:iterate>
                        </select>
                    </td>
                </table> 
        </td></tr>
        <tr><td>
                <table width = '70%' align = 'left' border="0">
                    <tr><td>
                            <div class = 'buttons'>
                                <input type = "button" name = "Submit" value ='Add' class = 'button' onClick = 'add();'/>                    
                                <input type = 'button' name = 'Clear' value = 'Clear' class = 'button' onClick = 'cleanList();' />
                    </div></td></tr>
                </table>
        </td></tr>
    </form>
</body>
</html>
