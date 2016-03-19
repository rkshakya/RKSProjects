console.log('starting password manager');

var crypto = require('crypto-js');

var storage = require('node-persist');
storage.initSync();

var args = require('yargs')
	.command('create', 'create user password', function(yargs){
		yargs.options({
			name: {
				demand: true,
				alias: 'n',
				description: 'Your account name goes here',
				type : 'string'
			},
			username:{
				demand: true,
				alias: 'u',
				description: 'Your user name goes here',
				type : 'string'
			},
			password:{
				demand: true,
				alias: 'p',
				description: 'Your password goes here',
				type : 'string'
			},
			masterPassword:{
				demand: true,
				alias: 'm',
				description: 'Your master password',
				type: 'string'
			}
		}).help('help');
	})
	.command('get', 'get username', function(yargs){
		yargs.options({
			name:{
				demand:true,
				alias: 'na',
				description : 'You account name',
				type : 'string'
			},
			masterPassword:{
				demand: true,
				alias: 'm',
				description: 'Your master password',
				type: 'string'
			}
		})
	})
	.help('help')
	.argv;

var command = args._[0];


//create
//		--name
//		--username
//		--password

//get
//		--name

// storage.setItemSync('name', 'Ravi');

// var name = storage.getItemSync('name');

// storage.setItemSync('accounts', [{ username : 'Ravi', balance : 300 }]);

// var accounts = storage.getItemSync('accounts');

/*accounts.push({username : 'Sony', balance: 100});

storage.setItemSync('accounts', accounts);*/


// console.log(storage.getItemSync('accounts'));

function saveAccounts(accounts, masterPassword){
	var encAccounts = crypto.AES.encrypt(JSON.stringify(accounts), masterPassword);
	storage.setItemSync('accounts', encAccounts.toString());
	return accounts;
}

function getAccounts(masterPassword){
	var encaccounts = storage.getItemSync('accounts');
	var accounts = [];
	if(typeof encaccounts !== 'undefined'){
		var bytes = crypto.AES.decrypt(encaccounts, masterPassword);
		var accounts = JSON.parse(bytes.toString(crypto.enc.Utf8));
	}
	return accounts;

}

function createAccount(account, masterPassword){
	var accounts = getAccounts(masterPassword);

	accounts.push(account);
	
	saveAccounts(accounts, masterPassword)
	return account;
}

function getAccount(accountName, masterPassword){
	var accounts = getAccounts(masterPassword);
	var retaccount;

	accounts.forEach(function (account) {
		if (account.name === accountName){
			retaccount = account;
		}
	})

	return retaccount;
}

if (command === 'create'){
	try{
		var acc = createAccount({
			name: args.name,
			username: args.username,
			password: args.password
		}, args.masterPassword);
		console.log(acc);
	} catch (e){
		console.log(e.message);
	}
	
} else if (command === 'get'){
	try{
		var gacc = getAccount(args.name, args.masterPassword);

		if(typeof gacc === 'undefined'){
			console.log('Account not found');
		}else{
			console.log(gacc);
		}
	} catch (e){
		console.log('Unable to get account');
	}
	
}

// var acc = createAccount({name : 'twitter',
// 	username : 'mam',
// 	password : 'ullu'
// });
// console.log(acc);

// var sacc = getAccount('twitter');
// console.log(sacc);