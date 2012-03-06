class EmptyArrayError < StandardError; end

def combine_anagrams(words)
	#raise EmptyArrayError unless words.length > 0
	# for each i/p element, sort and populate in hash (key : sorted element, value : array of member elements)
	
	ana_hash = Hash.new	
	words.each do |word|

			newword = word.downcase.split( // ).sort.join
			#puts newword
		
		if ana_hash.key?(newword)
			#fetch value - array and add this word
			ana_hash[newword].push(word)
		else
			#new value, add with empty array as value
			ana_hash[newword] = [word]
		end
		
	end
	# return the values in an array
	return ana_hash.values
end

#print combine_anagrams(['cars', 'for', 'potatoes', 'racs', 'four', 'scar', 'creams', 'scream'])

#print combine_anagrams([])
