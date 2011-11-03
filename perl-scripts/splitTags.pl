if(@ARGV != 1){
print "Usage : perl splitTags.pl <infile>\n\n";
die;
}

$infile = $ARGV[0];
$outfile = "out_".time().".txt";

open(IN, "$infile") || die "Can not open input file : $!\n";
open(OUT, ">$outfile") || die "Can not open output file : $!\n";

while($line = <IN>){
	chomp($line);
	
	@contents = split('\t', $line);
	
	if($contents[1] ne "NULL"){
		trim($contents[1]);
		@tags = split(';', $contents[1]);
		
		# check if tags are repeating
		
		%seen = ();
		@unique = ();
		
		foreach $item(@tags){
		unless($seen{$item}){
				$seen{$item} = 1;
				push(@unique, $item);
			}
		}
		
		for($i = 0 ; $i < @unique; $i++){
			print OUT $contents[0]."\t".trim($contents[1])."\n";
			print OUT $contents[0]."\t".trim($unique[$i])."\n";
				
		}
	}
	
	
	
}

close(OUT) || die " Error closing out file : $! \n";
close(IN) || die " Error closing file : $! \n";

sub trim($)
{
	my $string = shift;
	$string =~ s/^\s+//;
	$string =~ s/\s+$//;
	return $string;
}
