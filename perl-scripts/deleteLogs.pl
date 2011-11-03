my $args = @ARGV;

if($args < 1){
	die "ERROR : Argument missing. Please specify the abs path of Stratify directory\n";
}

$testloc = $ARGV[0];

print "Location of the folder: $testloc";

open(FILEHANDLE, ">log.txt") or die "cannot open file for logging: $!";

if(-d $testloc){
	#dir exists -process
	opendir(DIRHANDLE, $testloc) || die "Cannot opendir : $!";
	
	  foreach $name (sort readdir(DIRHANDLE)) {
	    print "found file: $name\n";
	   
	   
	   
	    if($name =~ /\bES_/){
	    	#if starts with ES_
	    	#go to ExtractionServer\logs
	    	
	    	$location = $testloc."/".$name."/ExtractionServer/logs";
	    	
	    	print " Location1 : $location\n";
	    	
	    	
	    	
	    	if(-d $location){
	    		
	    		
	    		
	    		opendir(DIRHANDLE1, $location) || die "Cannot opendir : $!";
	    		
	    		foreach $files(readdir(DIRHANDLE1)){
	    			$age = -M $location."/".$files;
	    			
	    			print " FILES: $files\n";
	    			print " AGE: $age\n";
	    			
	    			if((-f $location."/".$files) &&  ($age > 3.0)){
	    				unlink($location."/".$files);
	    				
	    				print FILEHANDLE "File deleted : $location./.$files\n";
	    			}
	    			
	    		}
	    		
	    		closedir(DIRHANDLE1);
	    	
	    	} else{
	    		print FILEHANDLE "Directory $location does not exist.\n";
	    	}
	    }elsif($name =~ /\bGalileo_/ ){
	    
	    #starts with Galileo
	    	#goto logs folder
	    	
	    	$location = $testloc."/".$name."/logs";
	    	
	    	print " Location2 : $location\n";
	    	
	    	
	    	
	    	if(-d $location){
	    		
			opendir(DIRHANDLE2, $location) || die "Cannot opendir : $!";
	    		
	    		foreach $files(readdir(DIRHANDLE2)){
	    		
	    			
	    				
	    			$age = -M $location."/".$files;
	    			
	    			print " FILES: $files\n";
	    			print " AGE: $age\n";
	    			
	    			if((-f $location."/".$files) && ($age > 3.0)){
	    				unlink($location."/".$files);
	    				
	    				
	    				print FILEHANDLE "File deleted : $location./.$files\n";
	    			}
	    			
	    		}
	    		
	    		closedir(DIRHANDLE2);	    		
	    		
	    	}else{
	    		print FILEHANDLE " Directory $location does not exist.\n";
	    	}
	    
	    }
	  }
  	closedir(DIRHANDLE);
}else{
	print "Dir does not exists.";
	print FILEHANDLE "Directory $testloc does not exist. \n";
	
}

close(FILEHANDLE);

