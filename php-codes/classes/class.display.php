<?
class class_display
{
	var $conn;	//the database connection
	var $resultset;	//The batched result
	var $navigationbar;
	var $dummy;
	var $currentpage;
	var $noTotalPage;
	var $tmp_present;	//the serial no.
	//These variables define the parameter names in the navigation bar
	//naming these variables may be necessary to avoid conflicts
	var $mainvar;
	var $startvar;
	var $countvar;

	function class_display($conn,$mainvar="main",$startvar="start",$countvar="count")
	{
		$this->conn=$conn;
		
		//initialize navigation bar
		$this->navigationbar="";
		$this->mainvar = $mainvar;
		$this->startvar = $startvar;
		$this->countvar = $countvar;
	}
	
	/**Prints a table of batched output
	 * $sql: the sql query according which records are fetched from the database
	 * $batch_page: name of the batching page in which this function is called
	 * $param: This argument is simply concatenated to the batching page
	 * $per_page: Number of batched pages to display
	 * $max_rec: Number of records in each page
	 * $i, $temp, $main, $start: Do not specify these. These use the default value for internal working
	 **/	
	function sql_batching($sql,$batch_page,$param,$per_page=5,$max_rec=10,$main=0,$start=0) 
	{
		$i=0;
		$temp=0;

		
		$PER_PAGE=$per_page;	//DEFINE HOW MANY PAGES TO DISPLAY PER PAGE.
		$MAX_REC=$max_rec;		//DEFINE HOW MANY ROWS TO DISPALY PER PAGE.

		$cols=count($tbl_heading)+1 ;
		$selectResult = $this->conn->sql($sql);
		if(($selectResult)>0)
			$count=mysql_num_rows($selectResult);
       
		else{	die("No Data Available");	}
		 //print "count is................".$count;
		//variables to display the total number of pages .
		if(($count%$MAX_REC)==0)
		{
			$totalPages=($count/$MAX_REC);
		}
		else 
			if(($count%$MAX_REC)!=0)
			{
				$totalPages=(int)($count/$MAX_REC)+1;
			}


		$page=(int)($start/$MAX_REC)+1;

		

		$batch_sql=$sql." LIMIT ".($start).", ".$MAX_REC;
		

		$selectResult = $this->conn->sql($batch_sql) or die ("Could not select data");

		//the batched result set				    	
		$this->resultset = $selectResult;
		
		
		if(mysql_num_rows($selectResult)>0)
		{
			
			$this->tmp_present=$start+1;
			

			//print "<table cellspacing='1' cellpadding='2' border='0' width='100%'>";
			
			//print "<tr><td colspan='$cols' align='right'>Page $page of $totalPages</td></tr>";
			
			
			$this->currentpage=$page;
			
			
			$this->noTotalpage=$totalPages;
			

			//if($tbl_main_head)
			//	print "<tr><td colspan='$cols' class='$td_color' align=\"center\">$tbl_main_head</td><tr>";
			/*print "<tr>";
			print "	<td class='$td_color' aling='center'>Sno.</td>";
			for($i=0;$i<count($tbl_heading);$i++)
				print " <td class='$td_color' aling='center'>$tbl_heading[$i]</td>";
			print "</tr>";*/
			/*while($row=mysql_fetch_row($selectResult))
			{
				print "<tr>";
				print "<td class='$bg_color'>".$tmp_present++."</td>";
				for($j=0;$j<count($tbl_heading);$j++)
					print "<td class='$bg_color'>$row[$j]</td>";
				print "</tr>";
			}*/
			//print "<tr>";
			//print "<td colspan='$cols' align='center' class='$bg_color'>";

			


				//$page=(int)($start/$MAX_REC)+1;
				
				if($main!=0){		
					
					
					$this->navigationbar .= "<a href='$batch_page?$this->mainvar=".($main-1)."&$this->startvar=".((($main-1)*$MAX_REC*$PER_PAGE)+($PER_PAGE-1)*$MAX_REC)."&$this->countvar=".$count."&$param'><font color='blue'>Previous</font></a>&nbsp;&nbsp;";	
					
					for($i=(1+($main*$PER_PAGE));$i<(1+($main*$PER_PAGE))+$PER_PAGE;$i++){
					
						if($i==$page){
							$this->navigationbar .="<font color='red'><b>".$i."</b></font>&nbsp;&nbsp;";

						}
						else{
							if($i<=$totalPages){
								$this->navigationbar .= "<a href='$batch_page?$this->mainvar=".$main."&$this->startvar=".(($i*$MAX_REC)-$MAX_REC)."&$this->countvar=".$count."&$param'>".$i."</a>&nbsp;&nbsp;";	


								/*if(($start==($count-$MAX_REC))){
									$i=$i+1;
									print "<a href='index.php?main=".$main."&start=".(($i*$MAX_REC)-$MAX_REC)."&count=".$count."'>".$i."</a>&nbsp;&nbsp;";	
									break;
								}*/
						
							}
						}								//end of if($i==$page)
						
						
					}									//end for
				
				
				 } //end  if main
				
				else{
					for($i=(1+($main*3));$i<(1+($main*$PER_PAGE))+$PER_PAGE;$i++){
					 	if($i==$page){
							$this->navigationbar .= "<font color='red'><b>".$i."</b></font>&nbsp;&nbsp;";
							//print $this->navigationbar;
						}
						else{				
							if($i<=$totalPages){
							$this->navigationbar .= "<a href='$batch_page?$this->mainvar=".$main."&$this->startvar=".(($i*$MAX_REC)-$MAX_REC)."&$this->countvar=".$count."&$param'>".$i."</a>&nbsp;&nbsp;";	

									/*if($start==($count-$MAX_REC)){
										$i=$i+1;
										print "<a href='$batch_page?main=".$main."&start=".(($i*$MAX_REC)-$MAX_REC)."&count=".$count."&$param'>".$i."</a>&nbsp;&nbsp;";	
										break;
									}*/
							
							}
						
						}								//end of else ($i==$page)

					 }									//end for
						
				
				
						
				} //end else
				
				
				if(($i<=$totalPages)&&($count!=($start+$MAX_REC))){
						$main=$main+1;
						$this->navigationbar .= "<a href='$batch_page?$this->mainvar=".$main."&$this->startvar=".(($i*$MAX_REC)-$MAX_REC)."&$this->countvar=".$count."&$param'><font color='blue'>Next</font></a>";		

				}
			
			
			//print "</td>";
			//print "</tr>";
			//print "</table>";
		}	

	}
	
	/**
	 * Returns the next record in the batched ouput and false if no more record
	 * The serial number will be returned in the record with associative index "serialno"
	 */
	function getNextRow()
	{
		
		if($record=mysql_fetch_array($this->resultset))
		{
			//fill the serialno also in the record
			$record["serialno"]=$this->tmp_present;
			$this->tmp_present++;
		}
		return $record;
	}

	function getCurrentPage()
	{
		return $this->currentpage;
	}

	function getTotalPages()
	{
		return $this->noTotalpage;
	}
	
	function getNavigationBar()
	{
		return $this->navigationbar;
	}
}
?>