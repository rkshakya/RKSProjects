#!/usr/bin/ruby

=begin
#---------------------------------------------------------------------------------------------------------
# euler12.rb- program to indicate the triangle number whose number of factors are just greater than the given threshold provided in the input file
# i/p - an ASCII txt file with a single number as threshold value (may contain multiple spaces and newline)
# o/p - the triangle number whose count of factors is > given number terminated by a single newline character
# author - Ravi Kishor Shakya
# Ruby version 1.8.7
#----------------------------------------------------------------------------------------------------------
=end

# for a tri no, generate its factors
def getfactors (initnum)
   triangle = initnum;
   tempfactors = Array.new;
   
   counter = 1;
   while counter <= triangle
		remainder = triangle % counter;
		
		if remainder == 0
			tempfactors.push(counter);
		end
		
		counter += 1;
   end
   
return tempfactors;
end

if ARGV.length != 1
	puts "Usage: euler12.rb <infile>";
	exit;
end


$ifile = ARGV[0];
$threshold = 0;

#read input file , trim spaces, new lines
File.open($ifile, "r")  do |thisFile|
		while line = thisFile.gets
		    line = line.chomp;
			line = line.strip;
			$threshold = line;
			#puts "#{$threshold}";	

			#bailout after non-empty line
			if line.length != 0
				break;
			end	
		end	
	end
	
#iterate and generate triangle nos
flag = 0;    #indicates success if true
count = 1;

#puts "#$threshold";

while flag == 0
	num = 0;
	factors = Array.new;
	
	#generate tri nos
	itr = 1;
	while itr <= count
		num += itr;
		itr += 1;
	end
	
	#get n chk factors of a triangle number
	factors = getfactors(num);
	
	#chk condition, set flag
	if factors.length > $threshold.to_i
		puts "#{num}";
		flag = 1;
	end
	
	count += 1;	
end



	
