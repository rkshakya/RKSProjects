Steps to run hit report generation script:
1) Copy following files to a folder location:
	a) BooleanHitReport.jar
	b) commons.jar

2) Copy 
	a) Cluster_log.txt, 
	b) BooleanFilterModel.xml,
	c) boolean_summary.xml 
into the same location as in 1) from clustering run(\\TS_server machine\Stratify\TS_Casename\\TaxonomyServer\TaxonomyServerWorkDir\data\ClusteringRuns\XX_working_0) location.

3) Invoke the script from the command prompt as:
java -jar BooleanHitReport.jar BooleanFilterModel.xml clusterer_log.txt boolean_summary.xml