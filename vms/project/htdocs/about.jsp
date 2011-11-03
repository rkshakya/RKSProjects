<%--

 (c) Copyright 1999 - 2007 Stratify, Inc. ( f/k/a PurpleYogi f/k/a Calpurnia ). All rights reserved.

 The foregoing shall not be deemed to indicate that this source has been published. Instead, it remains a trade secret of Stratify, Inc.

--%>

<%@page import="com.stratify.datahub.system.ejb.SoftVersion"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Stratify Processing Console - About</title>
<link type="text/css" rel="stylesheet" href="../htdocs/stylesheets/workbench.css" />
</head>
<body>

<div align="center">
	<img src="../reporting/htdocs/images/stratify_white.gif">
	<h2>Processing Console</h2>
	Release: <%=SoftVersion.getFullVersion()%>
	<br><br><br>
	&#169 2007 Stratify, Inc. All Rights Reserved.
</div>

</body>
</html>
