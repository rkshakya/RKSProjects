
    <%@include file = "layout/vmsCommonBase.jsp"%>

    <tiles:insert definition="vmsHeader" >
	<tiles:put name="title">Version Management System</tiles:put>
    </tiles:insert>
    <script></script>

    <%@include file = "layout/vmsMenuHeader.jsp"%>

    <div class="homeMenu">
            <table width = "100%" align="center">
	                    <tr><td align="center">
	                        <a href="/VMS/VMSCaseComponentAction.do" id="components">Components</a>                                                               
	                    </td></tr>
	                    <tr><td><hr></td></tr>               
	                    <tr><td align="center">
	                        <a href="/VMS/VMSListRuleAction.do?actionType=display" id="lists">Rules</a>                                            
	                    </td></tr>
	                    <tr><td><hr></td></tr>
	                    <tr><td align="center">
	                        <a href="/VMS/VMSCaseAction.do" id="cases">Cases</a>                        
	                    </td></tr>
	                    <tr><td><hr></td></tr>
	                    <tr><td align="center">
	                        <a href="/VMS/VMSServerAction.do" id="servers">Servers</a>                                            
	                    </td></tr>
	                    <tr><td><hr></td></tr>
	                    <tr><td align="center">
	    		                    <a href="/VMS/VMSVersionAction.do" id="servers">Versions</a>                                            
	                    </td></tr>
            </table>
        </div>    
    </body>
</html>
