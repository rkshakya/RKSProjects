var request = require('request');


module.exports = function (location, callback){
		
		if(!location){
			return callback('No location');
		}
		var url = 'http://api.openweathermap.org/data/2.5/weather?q=' + encodeURIComponent(location) + '&units=metric&appid=b1b15e88fa797225412429c1c50c122a';

		request({
		url : url,
		json : true
	}, function(error, response, body){
		if (error){
			callback('Unable to get weather data');
		} else {
			// console.log(JSON.stringify(body, null, 4));
			//It's 77 in Kathmandu.

			callback('It\'s ' + body.main.temp + ' in ' + body.name);
		}
	});

}