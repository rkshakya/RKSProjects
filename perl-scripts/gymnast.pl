#!/usr/bin/perl

my %counts = (); #hash key -search term, value = count
my @firsttokens = ();
my @secondtokens = ();
if(@ARGV != 1){
print "Usage : perl gymnast.pl <infile>\n\n";
die;
}
my $infile = $ARGV[0];
open(IN, "$infile") || die "Can not open input file : $!\n";
my $line;
my $count = 1;
while($line = <IN>){
    chomp($line);
    @firsttokens = split(/\)\s\(/, $line);
    $firsttokens[0] =~ s/\(//;
	$firsttokens[scalar(@firsttokens) - 1] =~ s/\)//;	
	foreach my $tok (@firsttokens){
		@secondtokens = split(/,/, $tok);
		$secondtokens[0] =~ s/^\s+ | \s+$//;
		$secondtokens[1] =~ s/^\s+ | \s+$//;
		$counts{$secondtokens[0]} = $secondtokens[1];
	}	
}
	my @sortedkeys = sort { ($b + $counts{$b}) <=> ($a + $counts{$a}) } keys %counts;
	my $thisht = $sortedkeys[0]; 
	my $thiswt = $counts{$thisht};	
	foreach my $key (@sortedkeys) {
		if( ($key < $thisht) && ($counts{$key} < $thiswt) ){
			$count++;
			$thisht = $key;
			$thiswt = $counts{$key};			
		}   		
	}
print "$count\n";
close(IN) || die " Error closing input file : $! \n";