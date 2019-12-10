var convTime = new Date(inputData['orig_date']).toLocaleString("en-US", {timeZone: inputData['timezone']});
convTime = new Date(convTime);
console.log('Conv time: '+convTime.toLocaleString())
var dateobj = new Date(convTime.toLocaleString());  
var B = dateobj.toISOString();
console.log('ISO: '+B)

output = { outdate: B};
