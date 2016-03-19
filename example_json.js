var person = {
	name : 'Ravi',
	age : 36
};

var personJSON = JSON.stringify(person);

console.log(personJSON);
console.log(typeof personJSON);

var personObj = JSON.parse(personJSON);

console.log(personObj.name);
console.log(typeof personObj);

console.log("Challenge area");

var animal = '{"name": "Mulley"}';

var animalObj = JSON.parse(animal);

animalObj.age = 10;

var animalJSON = JSON.stringify(animalObj);

console.log(animalJSON);