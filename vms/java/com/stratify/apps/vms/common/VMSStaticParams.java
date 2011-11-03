/*
 * VMSStaticParams.java
 *
 * Created on November 21, 2007, 4:30 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common;

/**
 *
 * @author ravikishores
 */
public class VMSStaticParams {
    
    /**
     * Creates a new instance of VMSStaticParams
     */
    public VMSStaticParams() {
    }
    //SQL server related
    public static final String CONNECTION_STRING = "net.sourceforge.jtds.jdbc.Driver";
    public static final String DB_URL_PREFIX = "jdbc:jtds:sqlserver://";
    public static final String MSSQL_BE_USERNAME = "DBADMIN";
    public static final String MSSQL_BE_PASSWORD = "DBADMIN";
    public static final int MSSQL_PORT = 1433;
    
    //domain related
    public static final String FE = "LDS";
    public static final String BE = "SDP";
    public static final String FEALAIS = "FE";
    public static final String BEALIAS = "BE";
    public static final String FESUFFIX = ".lds.asp.stratify.com";
    public static final String BESUFFIX = ".sdp.stratify.com";
    public static final String APAC = "APAC";
    public static final String APACFEALAIS = "APACFE";
    public static final String APACBEALIAS = "APACBE";
    public static final String BESUFFIX_APAC = ".sdp.stratify.com";
    public static final String FESUFFIX_APAC = ".lds.apac.stratify.com";
    
    //table aliases
    public static final String TBL_CASE = "vms_cases";
    public static final String TBL_SERVER = "vms_servers";
    public static final String TBL_COMPONENT = "vms_components";
    public static final String TBL_VERSION = "vms_compo_version";
    public static final String TBL_MAPPING = "vms_component_mapping";
    
    //Entry Server XML folder
    public static final String ENTRY_SERVER_FOLDER = "\\\\uprof01\\EntryConf\\conf";
    //public static final String ENTRY_SERVER_FOLDER = "\\\\apacentry01\\Stratify\\conf";
    //FE WEb Services URL
    public static final String FE_WEB_SERVICE_URL_APAC = "http://ldstools01.lds.asp.stratify.com/vms/services/VMSFEComponentService";
    //public static final String FE_WEB_SERVICE_URL_APAC = "http://192.168.1.224/vms/services/VMSFEComponentService";
    
    //Registry Key Search Strings
    public static final String SEARCH_SERVER_STRING = "PYFTS";
    public static final String REDACTION_SERVER_STRING = "REDACTION_VER";
    public static final String PDF_SERVER_STRING = "PDFCONV_VER";
    public static final String LDSUI_STRING = "LDSAPPSUI_VER";
    
    //messages related
    public static String MSG_INCOMPATIBILITY = "Incompatible components detected.";
    public static String MSG_REDUNDANCY = "Redundant components detected";
    public static String RULE_NOT_ADDED = "Rule could not be added.(Either rule with same name or rule with same components already exists.)";
    
    //CaseShares related
    public static String CASESHARE_PRIMARY = "CaseShare_FE_Primary";
    public static String CASESHARE_SECONDARY = "CaseShare_FE_Secondary";
    public static String CASECONFIG_LOCATION = "\\config\\caseconfig.xml";  //suffix for caseconfig.xml
    public static String CASESHARE_VERSION = "//scripts//common//version.txt";  //suffix for version file in caseshare
    public static String CASESHARE_PRIMARY_BE = "CaseShare_BE_Primary";
    public static String CASESHARE_SECONDARY_BE = "CaseShare_BE_Secondary";
    public static String SEC_CASESHARE_PREFIX = "add-prefix" ;
    
    //LDSUI related
    public static String LDSUI = "LDSUI";
    
    //Search Server related
    public static String SEARCH_SERVER = "SearchServer_FE";
    public static String SEARCH_SERVER_BE = "SearchServer_BE";
    public static String SEARCH_SERVER_ALIAS = "SearchServer";
    
    //User related
    public static int USER_ID = 1; //user id will be redundant afterwards
    
    //RedactionServer related
    public static String REDACT_SERVER_KEY = "redact/redactionSrv";
    public static String REDACTION_SERVER_ALIAS = "RedactionServer";
    
    //PDFServer related
    public static String PDF_SERVER_KEY ="redact/pdfconvertSrv";
    public static String PDF_SERVER_ALIAS = "PDFServer";
    
    //DB server related
    public static String BEDB = "DatabaseServer_BE";
    public static String FEDB = "DatabaseServer_FE";
    
    //Mailer related
    public static String MAILERCONFIGFILE = "conf\\mailerConfig.properties"; //config file for mailer
    public static String MAILERFLAG = "MAILER"; //flag to turn on/off mailer
    
    //terminal server related    
    public static String SITE_TAG = "site";    
    public static String TERMINAL_SERVER_ALIAS = "terminalServer";     
    public static String MAPPING_FILE = "conf\\server_mappings.txt";   //mapping file to resolve ts machine name to IP address
    
    //protocols related
    public static String HTTP = "http://";
    
    //misc 
    public static String NOT_APPLICABLE = "NA";
    public static String START_AFTER_MINS = "startafter-min"; //alias for properties read from web.xml
    public static String PERIOD_MINS = "runinterval-min";
    public static String DEFAULT_START_AFTER_MINS = "0"; //default time in mins for the gatherer to start
    public static String DEFAULT_PERIOD_MINS = "1440";   //period for repetition of gatherer
    public static String EXECUTION_DOMAIN = "execution-domain"; //domain value for execution to be picked from web.xml  
    public static String MASTER = "master";   

    public static String ANNOTATION_SERVER = "AnnotationServer";

    //alaises used in the DS_DATAHUB_PREF for master servers
    public static String MASTER_EXTRACTION = "MasterServer/Extraction";
    public static String MASTER_DOCPROCESSOR = "MasterServer/DocProcessor";
    public static String MASTER_CONCEPT_ORGANIZATION = "MasterServer/ConceptOrganization";

    //alaises used in VMS internal tables for Master servers
    public static String VMS_MASTER_EXTRACTION = "MasterServer_Extraction";
    public static String VMS_MASTER_DOCPROCESSOR = "MasterServer_DocProcessor";
    public static String VMS_MASTER_CONCEPT_ORGANIZATION = "MasterServer_ConceptOrganization";

    public static String SCHEDULE = "schedule";

    public static String DEFAULT_SCHEDULE = "0 0 7 * * ?";  //defaults to 7 am PST everyday

    public static int DB_LOGIN_TIMEOUT = 10; //DB login timeout in secs
         
    public static String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss"; 
    
    //crawl deviation  related messages
    public static String FATAL = "FATAL";
    public static String ERROR = "ERROR";
    public static String CRITICAL = "CRITICAL";
    public static String NONFATAL = "NONFATAL";

    public static int TH_SEVERITY_1 = 1; //threshold severity will be logged in crawler deviation history table
    public static int TH_SEVERITY_2 = 2;
    
    public static int SEVERITY_1 = 1; //placeholders for severities
    public static int SEVERITY_2 = 2;
    public static int SEVERITY_3 = 3;
    
    public static String NA = "NA";
    
}
