<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<body>
<input id="lock" type="hidden" value="${success}">
<input id="lockid" type="hidden" value="${workerid}">
<input id="lockday" type="hidden" value="${day}">
</body>    
<script type="text/javascript">
var workerid = $("#lockid").val();
var day = $("#lockday").val();
var lock = $("#lock").val();

$("#lock").remove();
$("#lockid").remove();
$("#lockday").remove();
console.log(lock);
console.log(Boolean(lock));
if(lock == "true"){
	$("[workerid='"+workerid+"'][day='"+day+"']").parent().css("background-color", "red");	
}else{
	$("[workerid='"+workerid+"'][day='"+day+"']").parent().css("background-color", "");
}


</script>