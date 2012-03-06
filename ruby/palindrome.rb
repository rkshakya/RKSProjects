def palindrome? (string)
	retval = false;
	tempstr = string;
	
	#remove non-words
	tempstr = tempstr.gsub(/\W/, '');
	#reverse str
	revstr = tempstr.reverse;
	#compare case insensitive
	flag = tempstr.casecmp(revstr);
	if flag == 0
		retval = true;
		#puts "Success";
	end
	return retval;
end

#palindrome? ("Madam, I'm Adam!")
#palindrome? ("A man, a plan, a canal -- Panama")
#palindrome? ("Abracadabra")

def count_words(string)
	initstring = string
	#lowercase, split on word boundary with non words in betn
	words = initstring.downcase.split(/\b\W+\b/)
	freq = Hash.new(0)
	words.each { |word| word.lstrip.rstrip; puts word; freq[word] += 1;}
	return freq
end

#has = count_words("Doo bee doo bee   doo")
#has = count_words("A man, a plan, a canal -- Panama")
#puts has
