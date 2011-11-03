#!/usr/bin/perl
use strict;

#---------------------------------------------------------------------------------------------------------
# bwtreverse.pl - program to generate original string used in BWT (implementation of reverse BWT)
# i/p -  an ASCII txt file with row number <space> BWT scrambled output
# o/p - original string used for BWT
# author - Ravi Kishor Shakya
# Perl version 5.10.1
#----------------------------------------------------------------------------------------------------------

my @bwtparams = (); 	#read from input file
my @chars = ();			#holds BWT scrambled input, corresp to the last col of the matrix 
my @sortedchars = ();	#holds the sorted chars, corresp to the first col of the matrix
my $finalstring = ''; 	#collect final result
my $length = 0;  		#length of the string
my %lookup = ();        #lookup hash - key(char_countoccur), value - index in array
my %inter = (); 		#hash for populating lookup hash

#procees the i/p file
if(@ARGV != 1){
print "Usage : perl bwtreverse.pl <infile>\n\n";
die;
}

my $infile = $ARGV[0];

#read input file, trim spaces, new lines
open(IN, "$infile") || die "Can not open input file : $!\n";
my $linecounter = 1;
my $line;
while($line = <IN>){	
	chomp($line);	
	
	#remove preceding/ending spaces
	$line =~ s/^\s+//;
	$line =~ s/\s+$//;
		
	#get the BWT values now, 0-index, 1-string
	@bwtparams = split /\s/, $line;
	
	#break string to chars
	@chars = split(//, $bwtparams[1]);
	@sortedchars = sort(@chars);
	$length = scalar @chars;
	
	#get the lookup hash ready
	my $ch;
	my $indx = 0;
	foreach $ch(@chars){
		if (exists $inter{$ch}){
			$inter{$ch}++;
			$lookup{$ch.'-'.$inter{$ch}} = $indx;
		}
		if(!exists $inter{$ch}){
			$inter{$ch} = 1;
			$lookup{$ch.'-'.1} = $indx;
		}
		
		$indx++;
	}	
		
	#first char of the final string- we already know
	$finalstring .= $sortedchars[$bwtparams[0]];	
	
	#get the intermediate chars - meat of the program
	#start with the first char c, given row number say R
	my $schar = $sortedchars[$bwtparams[0]];
	my $row = $bwtparams[0];
	my $counter = 1; #track number of chars, iteration of loop	
		
	#loop till all chars are processed
	while($counter < $length - 1){	
		#count how many times this char comes before current row R, say i times				
		my $result = 0;		
		
		for(my $cnt = 0; $cnt < $row; $cnt++){
			if ($sortedchars[$cnt] eq $schar){
				$result++;
			}	
		}	
				
		$row = $lookup{$schar.'-'.($result + 1)};	
			
			
		#get the corresp first char in the row - this char is the next char in the sequence	
		#append in the final string
		$finalstring .= $sortedchars[$row];
		$schar = $sortedchars[$row];
		
		$counter++;
	}
	
	#last char - also
	if(scalar @chars != 1){
		$finalstring .= $chars[$bwtparams[0]];
	}
	 
	print $finalstring."\n";
	
	#reset the global vars and arrays
	@bwtparams = ();
	@chars = ();
	@sortedchars = ();
	$finalstring = '';
	$length = 0;	
	%lookup = ();
	%inter = ();
	}	
	
close(IN) || die " Error closing input file : $! \n";