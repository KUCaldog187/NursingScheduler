<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<table>
<tr>
	<td valign="top">
		<table>
			<tr>
				<td>Enter Worker Id: <input id="son" type="text" onkeypress="" value="0"/></td>
			</tr>
			<tr>
				<td><jsp:include page="schedule.jsp"></jsp:include></td>
			</tr>
		</table>
	</td>
	<td td valign="top">
		<div id="employees"></div>
	</td>
</tr>
</table>

<script>
$('#son').keypress(function (e) {
	if (e.which == 13) {
	  $.get("/NursingScheduler/nurse/"+$("#son").val(), showEmployees);
	  return false;    //<---- Add this line
	}
});

function showEmployees(retVal){
	$("#employees").html(retVal);
}

</script>
</body>
</html>