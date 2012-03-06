def count_words(string)
	initstring = string
	#lowercase, split on word boundary with non words in betn
	words = initstring.downcase.split(/\b\W+\b/)
	freq = Hash.new(0)
	words.each { |word| word.lstrip.rstrip; puts word; freq[word] += 1;}
	return freq
end

has = count_words("Doo bee doo bee   doo")
#has = count_words("A man, a plan, a canal -- Panama")
puts has
