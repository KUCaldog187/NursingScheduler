<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Schedule</title>
</head>
<body>
	MONTH:
	<select id="month">
	  <option value="0">January</option>
	  <option value="1">February</option>
	  <option value="2">March</option>
	  <option value="3">April</option>
  	  <option value="4">May</option>
	  <option value="5">June</option>
	  <option value="6">July</option>
	  <option value="7">August</option>
  	  <option value="8">September</option>
	  <option value="9">October</option>
	  <option value="10">November</option>
	  <option value="11">December</option>
	</select>
 YEAR:
 	<select id="year">
	  <option value="2015">2015</option>
	  <option value="2016">2016</option>
	</select>
	<input type="button" value="GO" onclick="getEmployeeSchedule()"/>
	<input type="button" value="GENERATE" onclick="generateEmployeeSchedule();getEmployeeSchedule()"/> <input id="generateId" size="1"/><p id="success"></div>
	<br>
	
<div id="dateresult">
	<table border="1">
		<tr>
			<td>NAME</td>
		</tr>
		<tr>
			<td>DATE</td>
		</tr>
	</table>
</div>


	<script type="text/javascript">
	
		function getEmployeeSchedule(){
			$.get("/NursingScheduler/schedule/"+$("#son").val()+"/"+$("#month").val()+"/"+$("#year").val(), showEmployeeSchedule);
		}
		
		function generateEmployeeSchedule(){
			$.get("/NursingScheduler/schedule/generate/"+$("#generateId").val()+"/"+$("#month").val()+"/"+$("#year").val(), showSuccess);
		}
		
		function showEmployeeSchedule(retVal){
			$("#dateresult").html(retVal);
		}
		
		function showSuccess(retVal){
			$("#success").html(retVal);
		}
	
	</script>
</body>
</html>