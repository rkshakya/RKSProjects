var crypto = require("crypto-js");

var secretMessage = {
	name : 'Andrew',
	secretName : '007'
};
var secretKey = "Halloa";

var encMessage = crypto.AES.encrypt(JSON.stringify(secretMessage), secretKey);

console.log("Encrypted Message " + encMessage);

var bytes = crypto.AES.decrypt(encMessage, secretKey);
var mesg = bytes.toString(crypto.enc.Utf8);

var finobj = JSON.parse(mesg);

console.log("Decrypted message " + mesg);
console.log(finobj.secretName);
