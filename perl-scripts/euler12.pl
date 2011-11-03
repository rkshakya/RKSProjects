#!/usr/bin/perl

use strict;

#---------------------------------------------------------------------------------------------------------
# euler12.pl - program to indicate the triangle number whose  number of factors are just greater than the given threshold provided in the input file
# i/p -  an ASCII txt file with a single number as threshold value (may contain multiple spaces and newline)
# o/p - the triangle number whose count of factors is > given number terminated by a single newline character
# author - Ravi Kishor Shakya
# Perl version 5.10.1
#----------------------------------------------------------------------------------------------------------

if(@ARGV != 1){
print "Usage : perl euler12.pl <infile>\n\n";
die;
}

my $infile = $ARGV[0];

#read input file, trim spaces, new lines
open(IN, "$infile") || die "Can not open input file : $!\n";
my $line;
while($line = <IN>){	
	#print "\n B4 Line : ".$line;
	chomp($line);	
	$line = trim($line);
	#print "\n Inter Line :".$line;
	if(length($line) != 0) {
		last;
		}
	}	
	
	#print "\n Threshold: ".$line;
close(IN) || die " Error closing input file : $! \n";

#itereate and generate triangle numbers
my $flag = 0;  #flag to indicate success
my $count = 1;

while($flag == 0){
	my $num = 0;
	my @factors = ();
	#generate triangle numbers
	for(my $itr = 1; $itr <= $count; $itr++){
		$num += $itr;
	}
	
	#print "\n".$count.": triangle number ".$num;
	
	#get and check factors of the triangle number	
	#get the factors in a list
	@factors = getfactors($num);
	
	#print "\n Final factors: ".join "-", @factors;
	#check the condition -set the flag
	if(scalar(@factors) >  $line){
		print "$num\n";
		$flag = 1;
	}
	
	$count++;
}

#for each tri number, iterate and generate factors
sub getfactors($){
	my $cnt;
	my $rem;
	my $triangle = shift;
	my @tempfactors = ();
	
	for ($cnt = 1;$cnt <= $triangle;$cnt++){
        $rem = $triangle % $cnt;
        if ($rem == 0) {
	        #print "\n Factor ".$cnt;
			push(@tempfactors, $cnt);
		}
    }
	
	return @tempfactors;
}

#function to trim spaces, tabs
sub trim($){
	$_ = shift;
	s/^\s+//;
	s/\s+$//;	
	return $_;
}
