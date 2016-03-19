var args = require('yargs')
	.command('hello', 'greets the user', function(yargs){
		yargs.options({
			name: {
				demand: true,
				alias: 'n',
				description: 'Your first name goes here'
			},
			lastname:{
				demand: true,
				alias: 'l',
				description: 'Your last name goes here'
			}
		}).help('help');
	})
	.help('help')
	.argv;
var greet = args._[0];

console.log(args);

if (greet === 'hello' && typeof args.name !== 'undefined' && typeof args.lastname !== 'undefined'){
	console.log("Hello " + args.name + " " + args.lastname + "!");
} else if (greet === 'hello' && typeof args.name !== 'undefined'){
	console.log("Hello " + args.name + "!");
} else if(greet === 'hello'){
	console.log("Hello world!");
}

// 