#!/usr/bin/perl

#---------------------------------------------------------------------------------------------------------
# topfive.pl - program to generate top 5 search term listing after parsing web log files
# i/p - a log file (txt format)
# o/p - top 5 search terms in desc order of popularity
# author - Ravi Kishor Shakya
# Perl version 5.10.1
#----------------------------------------------------------------------------------------------------------

my %counts = (); #hash key -search term, value = count

#procees the i/p file
if(@ARGV != 1){
print "Usage : perl topfive.pl <infile>\n\n";
die;
}

my $infile = $ARGV[0];

#read input file, trim spaces, new lines
open(IN, "$infile") || die "Can not open input file : $!\n";

my $indextracker;
my $line;
my $term;

while($line = <IN>){
	#chomp($line);
	
	#extract the search term from each line - 1. use substring fns (2. alternative regex pattern matching)
	$indextracker = index($line, 'query=') + 6;
	$term = substr $line, $indextracker , rindex($line, ']') - $indextracker;
	
	#get the lookup hash ready	
	if (exists $counts{$term}){
	$counts{$term}++;
	}else{
	$counts{$term} = 1;
	}		
}

close(IN) || die " Error closing input file : $! \n";

my $counter = 1;
foreach my $key ( sort hashValueDescending keys(%counts)) {
   		print "$key\n";
		
		if($counter == 5){
			last;
		}		
		$counter++;
	}

sub hashValueDescending {
   $counts{$b} <=> $counts{$a};
}