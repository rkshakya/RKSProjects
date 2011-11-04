<!-- Hide from JavaScript-Impaired Browsers
function initArray() {
 for(l=0;l<initArray.arguments.length; l++)
  this[l] = initArray.arguments[l];
}
var isnMonths=new initArray("January","February","March","April","May","June","July","August","September","October","November","December");
var isnDays= new initArray("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday");
today=new Date();
hrs=today.getHours();
min=today.getMinutes();
sec=today.getSeconds();
clckh=""+((hrs>12)?hrs-12:hrs); 
clckm=((min<10)?"0":"")+min;
clcks=((sec<10)?"0":"")+sec;
clck=(hrs>=12)?"p.m.":"a.m.";
var stnr="";
var ns="0123456789";
var a="";
 function makeArray() {
      for (l = 0; l<makeArray.arguments.length; l++)
           this[l + 1] = makeArray.arguments[l];
 }
 var dys = new Array('Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday');
 var months = new makeArray('January','February','March','April','May','June','July','August','September','October','November','December');
 var date = new Date();
 var dy = date.getDay();
 var day  = date.getDate();
 var month = date.getMonth() + 1;
 var yy = date.getYear();
 var year = (yy < 1000) ? yy + 1900 : yy;
// End Hiding -->