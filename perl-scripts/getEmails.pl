 #use strict;
 
 use File::Find;
 use File::Basename;
 use Spreadsheet::ParseExcel;
 
 my $args = @ARGV;
 
 if($args < 1){
 	die "ERROR : Argument missing. Please specify the abs path of top level directory\n";
 }
 
    $testloc = $ARGV[0];
    
    
    
    open (OUT, ">results.txt") or die "cannot open file for writing results: $!";
 
    #@ARGV = ('.') unless @ARGV;
    
    my @excels = ();
    $filename = "";
    
    
    sub get_excels {
    
    $filename = $File::Find::name;
    
    if(-f $filename){
    my($fname, undef, $ftype) = fileparse($filename,qr"\..*");
    	#print "\n$fname->$ftype";
    	if( ($fname =~ /Manual/i) && ($ftype =~ /.xls/i)){
    		#print "\n$filename";
    		push(@excels, $filename);
    	}
    }
    
   
    }
    
    find(\&get_excels, @ARGV);
    
    for $elem(@excels){
    	print OUT "\n$elem\n";  
 	my $file = $elem;

    my $excel = Spreadsheet::ParseExcel::Workbook->Parse($file);
    foreach my $sheet (@{$excel->{Worksheet}}) {
        #print OUT "Sheet:  $sheet->{Name}\n";
        $sheet->{MaxRow} ||= $sheet->{MinRow};
        foreach my $row ($sheet->{MinRow} .. $sheet->{MaxRow}) {
            $sheet->{MaxCol} ||= $sheet->{MinCol};
            foreach my $col ($sheet->{MinCol} ..  $sheet->{MaxCol}) {
                my $cell = $sheet->{Cells}[$row][$col];
                if ( ($cell->{Val} =~ /^[\w-]+(?:\.[\w-]+)*@(?:[\w-]+\.)+[a-zA-Z]{2,7}$/) && ($cell->{Val} !~ /stratify/i) ) {
                    #printf("( %s , %s ) => %s\n", $row, $col, $cell->{Val});
                    print OUT "$cell->{Val}\n";
                }
            }
        }
    }
}

close(OUT);
    