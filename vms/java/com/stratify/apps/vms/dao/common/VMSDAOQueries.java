/*
 * VMSDAOQueries.java
 *
 * Created on November 19, 2007, 5:34 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.dao.common;

/**
 *
 * @author ravikishores
 */
public class VMSDAOQueries {
    //source MySQL related, get case, server info
//    public static String GET_CASE_SERVERS = "SELECT  CASENAME, DB_VERSION, SERVER_NAME, DOMAIN "
//            + " FROM dbh_cases A, dbh_servers B WHERE "
//            + " A.SERVER_GID = B.GID AND A.IS_DROPPED = 0 AND B.FLAG = 0 ORDER BY A.CASENAME";
    
    public static String GET_CASE_SERVERS = "SELECT  CASENAME, DB_VERSION, SERVER_NAME, DOMAIN "
            + " FROM dbh_cases A, dbh_servers B WHERE "
            + " A.SERVER_GID = B.GID AND A.IS_DROPPED = 0 AND B.FLAG = 0 "
            + " AND B.DOMAIN IN (SELECT DISTINCT DOMAIN FROM dbh_servers WHERE DOMAIN IN (?, ?))"
            + " ORDER BY A.CASENAME";
    
    //dest MySQL related
    //related to synchronization
    public static String GET_CASE_GID = "SELECT GID FROM vms_cases WHERE CASE = ? ";
    public static String GET_SERVER_GID = "SELECT GID FROM vms_servers WHERE SERVER = ? AND "
            + " DOMAIN = ?";
    public static String GET_CASE_SERVER_GID = "SELECT GID FROM vms_caseserver_mapping WHERE CASE_GID = ? AND "
            + " SERVER_GID = ? ";
    public static String INS_CASE = "INSERT INTO vms_cases(GID, CASENAME) VALUES(NULL, ?)";
    public static String INS_SERVER = "INSERT INTO vms_servers(GID, SERVER, DOMAIN) "
            + "VALUES(NULL, ?, ?)";
    public static String INS_CASE_SERVER = "INSERT INTO vms_caseserver_mapping(GID, CASE_GID, SERVER_GID) "
            + " VALUES(NULL, ?, ?)";
    public static String GET_INS_INFO = "SELECT GID AS CASE_GID, 0 AS SERVER_GID, 0 AS CASE_SERVER_GID"
            + " FROM vms_cases WHERE CASENAME = ? UNION "
            + " SELECT 0 AS CASE_GID, GID AS SERVER_GID, 0 AS CASE_SERVER_GID "
            + " FROM vms_servers WHERE SERVER = ? AND DOMAIN = ? UNION "
            + " SELECT CASE_GID AS CASE_GID, SERVER_GID AS SERVER_GID, GID AS CASE_SERVER_GID "
            + " FROM vms_caseserver_mapping "
            + " WHERE CASE_GID = (SELECT GID FROM vms_cases WHERE CASENAME = ?) "
            + " AND SERVER_GID = (SELECT GID FROM vms_servers WHERE SERVER = ? AND DOMAIN = ?)";
    public static String GET_LAST_GID = "SELECT LAST_INSERT_ID()";
    
    public static String GET_COMPO_SERVER_FLAGS = "SELECT GID AS SERVER_GID, 0 AS COMPO_GID, 0 AS CASE_SERVER_GID, 0 AS CASE_GID"
            + " FROM vms_servers WHERE SERVER = ? AND DOMAIN = ? UNION "
            + " SELECT 0 AS SERVER_GID, GID AS COMPO_GID, 0 AS CASE_SERVER_GID, 0 AS CASE_GID "
            + " FROM vms_components WHERE COMPONENT = ? UNION "
            + " SELECT SERVER_GID AS SERVER_GID, 0 AS COMPO_GID, GID AS CASE_SERVER_GID, CASE_GID AS CASE_GID "
            + " FROM vms_caseserver_mapping "
            + " WHERE CASE_GID = (SELECT GID FROM vms_cases WHERE CASENAME = ?) "
            + " AND SERVER_GID = (SELECT GID FROM vms_servers WHERE SERVER = ? AND DOMAIN = ?) UNION "
            + " SELECT 0 AS SERVER_GID, 0 AS COMPO_GID, 0 AS CASE_SERVER_GID, GID AS CASE_GID "
            + " FROM vms_cases WHERE CASENAME = ? ";
    
    public static String GET_BE_CASE_SERVS = "SELECT A.CASENAME , B.SERVER , "
            + " B.DOMAIN, C.GID  FROM "
            + " vms_cases A, vms_servers B, vms_caseserver_mapping C WHERE "
            + " A.GID = C.CASE_GID AND B.GID = C.SERVER_GID AND B.DOMAIN = 'BE' "
            + " A.IS_DELETED = 0"
            +  " AND (B.SERVER LIKE '%dsdb%' OR B.SERVER LIKE '%db%') ORDER BY B.SERVER";
    
    public static String GET_FE_CASE_SERVS = "SELECT A.CASENAME , B.SERVER , "
            + " B.DOMAIN, C.GID  FROM "
            + " vms_cases A, vms_servers B, vms_caseserver_mapping C WHERE "
            + " A.GID = C.CASE_GID AND B.GID = C.SERVER_GID AND B.DOMAIN = 'FE' "
            + " A.IS_DELETED = 0"
            +  " AND B.SERVER LIKE '%db%' ORDER BY B.SERVER";
/* old version
    public static String GET_BEFE_CASE_SERVS = "SELECT BEONLY.CASENAME AS BECASENAME, BEONLY.SERVER AS BESERVER, BEONLY.DOMAIN AS BEDOMAIN,"
            + " IFNULL(BEONLY.GID, 0) AS BECASESERVERGID, FEONLY.CASENAME AS FECASENAME, FEONLY.SERVER AS FESERVER,"
            + " FEONLY.DOMAIN AS FEDOMAIN, FEONLY.GID AS FECASESERVERGID"
            + " FROM"
            + " (SELECT A.CASENAME , B.SERVER,"
            + " B.DOMAIN, C.GID, A.IS_DELETED"
            + " FROM vms_servers B INNER JOIN vms_caseserver_mapping C"
            + " ON B.GID = C.SERVER_GID"
            + " AND B.DOMAIN = ? "
            + " AND (B.SERVER LIKE '%dsdb%' OR B.SERVER LIKE '%db%')"
            + " RIGHT OUTER JOIN vms_cases A"
            + " ON A.GID = C.CASE_GID"
            + " ORDER BY B.SERVER) AS BEONLY,"
            + " (SELECT A.CASENAME , B.SERVER ,"
            + " B.DOMAIN, C.GID, A.IS_DELETED  FROM vms_servers B INNER JOIN vms_caseserver_mapping C"
            + " ON B.GID = C.SERVER_GID"
            + " AND B.DOMAIN = ?"
            + " AND B.SERVER LIKE '%db%'"
            + " RIGHT OUTER JOIN vms_cases A"
            + " ON A.GID = C.CASE_GID "
            + " ORDER BY B.SERVER) AS FEONLY"
            + " WHERE BEONLY.CASENAME = FEONLY.CASENAME"
            + " AND BEONLY.IS_DELETED = 0 AND FEONLY.IS_DELETED = 0"
            + " AND BEONLY.DOMAIN = ? AND FEONLY.DOMAIN = ?"
            + " ORDER BY BEONLY.CASENAME, BEONLY.SERVER DESC, BEONLY.DOMAIN DESC";
 */
    
    
    
    public static String GET_BEFE_CASE_SERVS = "SELECT casename AS BECASENAME, NULLIF(max(be_server), '') AS BESERVER, NULLIF(max(be_domain), '') AS BEDOMAIN,"
            + " max(becsgid) AS BECASESERVERGID, casename AS FECASENAME, NULLIF(max(fe_server), '')  AS FESERVER, NULLIF(max(fe_domain), '') AS FEDOMAIN, "
            + " max(fecsgid) AS FECASESERVERGID "
            + " FROM ( "
            + "  SELECT c.casename, s.server AS be_server, '' AS fe_server, s.domain AS be_domain, '' AS fe_domain,"
            + "  csm.gid AS becsgid, 0 AS fecsgid "
            + "  FROM vms_cases c, vms_servers s, vms_caseserver_mapping csm"
            + "  WHERE c.is_deleted = 0 "
            + "  AND c.gid = csm.case_gid"
            + " AND s.gid = csm.server_gid"
            + " AND s.domain = ? "
            + " AND s.server LIKE '%db%'"
            + " AND ( "
            + " EXISTS (  SELECT 'X' FROM vms_component_mapping cm WHERE cm.case_Server_Gid =csm.gid AND cm.is_deleted = 0) OR"
            + " NOT EXISTS (  SELECT 'X' FROM vms_component_mapping cm WHERE cm.case_Server_Gid =csm.gid AND cm.is_deleted = 1)"
            + " )"
            + " UNION ALL "
            + " SELECT c.casename, '' AS be_server, s.server AS fe_server, '' AS be_domain, s.domain AS fe_domain,"
            + " 0 AS becsgid, csm.gid AS fecsgid "
            + " FROM vms_cases c, vms_servers s, vms_caseserver_mapping csm"
            + " WHERE c.is_deleted = 0"
            + " AND c.gid = csm.case_gid"
            + " AND s.gid = csm.server_gid"
            + " AND s.domain = ? "
            + " AND s.server LIKE '%db%'"
            + " AND s.server NOT LIKE '%root%'"
            + " AND ( "
            + " EXISTS (  SELECT 'X' FROM vms_component_mapping cm WHERE cm.case_Server_Gid =csm.gid AND cm.is_deleted = 0) OR"
            + " NOT EXISTS (  SELECT 'X' FROM vms_component_mapping cm WHERE cm.case_Server_Gid =csm.gid AND cm.is_deleted = 1)"
            + " ) "
            + " ) AS T"
            + " GROUP BY BECASENAME"
            + " ORDER BY BECASENAME, BESERVER, BEDOMAIN";
    
    //sql server related
    public static String GET_BE_COMPONENTS = "SELECT SERVER_TYPE, VERSION, SERVER_URL, ACTIVE FROM DBADMIN.DS_SERVER(NOLOCK)"
            + " WHERE ACTIVE = 1 AND SERVER_TYPE NOT IN ('SearchServer', 'SystemMonitor', 'generic')";
    public static String GET_BE_DTSEARCH = "SELECT VALUE FROM DBADMIN.DS_DATAHUB_PREF(NOLOCK) WHERE NAME = 'lds/ftSearchServer'";
    public static String GET_FE_REDACTPDF = "SELECT VALUE, NAME FROM DBADMIN.DS_DATAHUB_PREF(NOLOCK) WHERE NAME IN ( 'redact/redactionSrv', 'redact/pdfconvertSrv')";
    public static String GET_DB_INFO = "SELECT MAJOR, MINOR, BUILD_NUMBER, PATCH_NUMBER, SCHEMA_NUMBER FROM DBADMIN.DS_DB_PKG_BUILD(NOLOCK)";
    //public static String GET_HIERARCHY = "SELECT GID FROM DS_HIERARCHY WHERE RELEASE_VERSION IS NOT NULL";
    public static String GET_HIERARCHY = "DECLARE @MAJOR INT, @QUERY NVARCHAR(1000), @HGIDOUT INT "
            + " SELECT @MAJOR = MAJOR FROM DS_DB_PKG_BUILD(NOLOCK) "
            + " SELECT @QUERY =  "
            + " CASE @MAJOR "
            + " WHEN  9 THEN  'SELECT  TOP 1 @HGID = GID FROM DS_HIERARCHY(NOLOCK) WHERE TYPE = ''public'' ORDER BY GID' "
	    + "	 ELSE  'SELECT @HGID = GID FROM DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL'"
            + " END "    
            + " EXEC sp_executesql  @QUERY, N'@HGID INT OUTPUT ', @HGIDOUT OUTPUT "
            + " SELECT @HGIDOUT" ;
    public static String USE_DB = "USE ";
    
    //queries for 6.0 and later cases
    public static String GET_BE_CASESHARES_6_ON = "SELECT A.CASESHARE_PATH, MAX(A.IS_PRIMARY) AS IS_PRIMARY "
            + " FROM (SELECT VALUE AS CASESHARE_PATH, "
            + " CASE WHEN NAME = 'caseconfig/Case_config_path' THEN 1 ELSE 0 END AS IS_PRIMARY "
            + " FROM DS_DATAHUB_PREF(NOLOCK) WHERE NAME LIKE 'caseconfig/Case_config%' AND VALUE LIKE '\\%' "
            + " UNION "
            + " SELECT CASESHARE_PATH, IS_PRIMARY "
            + " FROM WB_CASESHARE(NOLOCK) "
            + " WHERE CASESHARE_TYPE = 'backend'"
            + " ) AS A"
            + " GROUP BY A.CASESHARE_PATH";
    
    public static String GET_BE_CASESHARES_9_ON = "SELECT CASESHARE_PATH, IS_PRIMARY "
            + " FROM DS_CASESHARE(NOLOCK) WHERE CASESHARE_TYPE = 'backend'";
    
    public static String GET_FE_CASESHARES_9_ON = "SELECT CASESHARE_PATH, IS_PRIMARY "
            + " FROM DS_CASESHARE(NOLOCK) WHERE CASESHARE_TYPE = 'frontend'";
    
    public static String GET_FE_CASESHARES_6_ON = "SELECT A.CASESHARE_PATH, MAX(A.IS_PRIMARY) AS IS_PRIMARY "
            + " FROM (SELECT VALUE AS CASESHARE_PATH, "
            + " CASE WHEN NAME = 'caseconfig/Case_config_datacenter_path' THEN 1 ELSE 0 END AS IS_PRIMARY "
            + " FROM DS_DATAHUB_PREF WHERE NAME LIKE 'caseconfig/Case_config_datacenter%' AND VALUE LIKE '\\%' "
            + " UNION "
            + " SELECT CASESHARE_PATH, IS_PRIMARY "
            + " FROM WB_CASESHARE "
            + " WHERE CASESHARE_TYPE = 'frontend'"
            + " ) AS A"
            + " GROUP BY A.CASESHARE_PATH";
    
//queries for 5.5 and older cases
    public static String GET_BE_CASESHARES_6_OFF = "SELECT A.CASESHARE_PATH, MAX(A.IS_PRIMARY) AS IS_PRIMARY "
            + " FROM (SELECT VALUE AS CASESHARE_PATH, "
            + " CASE WHEN NAME = 'caseconfig/Case_config_path' THEN 1 ELSE 0 END AS IS_PRIMARY "
            + " FROM DS_DATAHUB_PREF WHERE NAME LIKE 'caseconfig/Case_config%' AND VALUE LIKE '\\%' "
            + " ) AS A"
            + " GROUP BY A.CASESHARE_PATH";
    
    public static String GET_FE_CASESHARES_6_OFF = "SELECT A.CASESHARE_PATH, MAX(A.IS_PRIMARY) AS IS_PRIMARY "
            + " FROM (SELECT VALUE AS CASESHARE_PATH, "
            + " CASE WHEN NAME = 'caseconfig/Case_config_datacenter_path' THEN 1 ELSE 0 END AS IS_PRIMARY "
            + " FROM DS_DATAHUB_PREF WHERE NAME LIKE 'caseconfig/Case_config_datacenter%' AND VALUE LIKE '\\%' "
            + " ) AS A"
            + " GROUP BY A.CASESHARE_PATH";
    
    
    
    //MySQL again
    public static String GET_COMPO_VERSION_GID = "SELECT GID FROM vms_compo_version WHERE COMP_GID = ? AND "
            + " MAJOR = ? AND MINOR = ? AND BUILD = ? AND PATCH = ? ";
    public static String INS_VERSION_INFO = "INSERT INTO vms_compo_version VALUES (NULL, ?, ?,?,?,?)";
    
    public static String GET_COMPO_MAPPING = "SELECT GID FROM vms_component_mapping WHERE CASE_SERVER_GID = ?"
            + " AND COMPO_VER_GID = ? AND PORT = ?";
    
    public static String INS_COMPO_MAPPING = "INSERT INTO vms_component_mapping(GID, CASE_SERVER_GID, COMPO_VER_GID, PORT)"
            + " VALUES(NULL, ?, ?, ?)";
    
    public static String GET_COMPO_VERSION_GID_EXC = "SELECT GID FROM vms_compo_version WHERE COMP_GID = ? AND ("
            + " MAJOR <> ? OR MINOR <> ? OR BUILD <> ? OR PATCH <> ?)";
    
    public static String UPDATE_COMPO_MAPPING = "UPDATE vms_component_mapping SET IS_DELETED = 0"
            + "  WHERE GID = ?";
    
    public static String UPDATE_MOD_DATE = "UPDATE vms_component_mapping SET MODIFIED_DATE = CURRENT_TIMESTAMP"
            + "  WHERE CASE_SERVER_GID = ? AND PORT = ?";
    
    //query related to Web tier
    public static String GET_COMPO_VER = "SELECT A.GID, A.MAJOR, A.MINOR, A.BUILD, A.PATCH, B.COMPONENT "
            + " FROM vms_compo_version A, vms_components B "
            + " WHERE A.COMP_GID = B.GID "
            + " AND (A.MAJOR <> 0 "
            + " OR A.MINOR <> 0 "
            + " OR A.BUILD <> 0 "
            + " OR A.PATCH <> 0 )"
            + " ORDER BY B.BE, B.COMPONENT, A.MAJOR, A.MINOR, A.BUILD, A.PATCH";
    
    public static String GET_MAX_RULE = "SELECT MAX(RULE) FROM vms_compatibility_rules";
    
    public static String INS_RULE = "INSERT INTO vms_compatibility_rules(RULE, COMP_VER_GID, MODIFIED_BY)VALUES(?,?, ?)";
    
    public static String GET_RULE = "SELECT DISTINCT RULES.RULE, INFO.NAME, INFO.DESCRIPTION  FROM vms_compatibility_rules RULES, vms_rules_info INFO WHERE RULES.IS_DELETED = 0 AND RULES.RULE = INFO.RULE ORDER BY RULE";
    
    public static String GET_COMPO_VER_RULE =  "SELECT A.GID, A.MAJOR, A.MINOR, A.BUILD, A.PATCH, B.COMPONENT "
            + " FROM vms_compo_version A, vms_components B, vms_compatibility_rules C "
            + " WHERE A.COMP_GID = B.GID "
            + " AND C.RULE IN (?)"
            + " AND C.COMP_VER_GID = A.GID"
            + " AND C.IS_DELETED = 0"
            + " ORDER BY B.COMPONENT";
    
    public static String DEL_RULE = "UPDATE vms_compatibility_rules SET IS_DELETED = 1 WHERE RULE = ?";
    
    //get components but not for the rule specified
    public static String GET_OTHER_COMPO_VER_RULE = "SELECT A.GID, A.MAJOR, A.MINOR, A.BUILD, A.PATCH, B.COMPONENT "
            + " FROM vms_compo_version A, vms_components B"
            + " WHERE A.GID NOT IN ( SELECT COMP_VER_GID FROM vms_compatibility_rules WHERE RULE = ? AND IS_DELETED = 0)"
            + " AND A.COMP_GID = B.GID AND A.MAJOR <> 0"
            + " ORDER BY B.BE, B.COMPONENT, A.MAJOR, A.MINOR, A.BUILD, A.PATCH";
    
    //fetch server names sans caseshares
    public static String GET_SERVERS = "SELECT GID, SERVER, DOMAIN FROM vms_servers ORDER BY DOMAIN , SERVER";
    
    public static String GET_CASE_COMPONENTS = "SELECT E.SERVER_GID, A.GID, A.CASENAME, B.COMPONENT, C.MAJOR, C.MINOR, C.BUILD, C.PATCH, D.PORT"
            + " FROM vms_cases A,  vms_components B, vms_compo_version C, vms_component_mapping D, vms_caseserver_mapping E"
            + " WHERE E.SERVER_GID = ? "
            + " AND A.GID = E.CASE_GID"
            + " AND D.CASE_SERVER_GID = E.GID"
            + " AND D.IS_DELETED = 0"
            + " AND A.IS_DELETED = 0"
            + " AND D.COMPO_VER_GID = C.GID"
            + " AND C.COMP_GID = B.GID"
            + " ORDER BY A.CASENAME, B.COMPONENT, C.MAJOR";
    
    public static String GET_CASES = "SELECT GID, CASENAME FROM vms_cases WHERE IS_DELETED = 0 ORDER BY CASENAME";
    
    public static String GET_SERVER_COMPONENTS = "SELECT C.CASENAME, C.GID, CO.COMPONENT, S.GID, S.SERVER, CM.PORT, CV.MAJOR, CV.MINOR, CV.BUILD, CV.PATCH, CM.COMPO_VER_GID "
            + " FROM vms_cases C, vms_servers S, vms_caseserver_mapping CSM, vms_component_mapping CM, vms_compo_version CV, "
            + " vms_components CO "
            + " WHERE S.GID = CSM.SERVER_GID "
            + " AND CSM.CASE_GID = ?"
            + " AND CSM.GID = CM.CASE_SERVER_GID"
            + " AND CM.COMPO_VER_GID = CV.GID"
            + " AND CV.COMP_GID = CO.GID"
            + " AND CM.IS_DELETED = 0"
            + " AND C.IS_DELETED = 0"
            + " AND C.GID = CSM.CASE_GID"
            + " ORDER BY S.DOMAIN, CO.COMPONENT, CV.MAJOR";
    
    public static String GET_SERVER_COMPONENTS_GENERIC = "SELECT C.CASENAME, C.GID, CO.COMPONENT, S.GID, S.SERVER, CM.PORT, CV.MAJOR, CV.MINOR, CV.BUILD, CV.PATCH, CM.COMPO_VER_GID, S.DOMAIN"
            + " FROM vms_cases C, vms_servers S, vms_caseserver_mapping CSM, vms_component_mapping CM, vms_compo_version CV,"
            + " vms_components CO"
            + " WHERE S.GID = CSM.SERVER_GID"
            + " AND CSM.GID = CM.CASE_SERVER_GID"
            + " AND CM.COMPO_VER_GID = CV.GID"
            + " AND CV.COMP_GID = CO.GID"
            + " AND CM.IS_DELETED = 0"
            + " AND C.IS_DELETED = 0"
            + " AND C.GID = CSM.CASE_GID";
    
    public static String GET_OUTCASTES = "SELECT DISTINCT CM.COMPO_VER_GID"
            + " FROM vms_component_mapping CM, vms_caseserver_mapping CS"
            + " WHERE CS.GID = CM.CASE_SERVER_GID"
            + " AND CS.CASE_GID = ? "
            + " AND COMPO_VER_GID NOT IN ("
            + " SELECT COMP_VER_GID FROM VMS_COMPATIBILITY_RULES"
            + " WHERE RULE = ("
            + " SELECT RULE FROM VMS_COMPATIBILITY_RULES"
            + " WHERE COMP_VER_GID IN ("
            + " SELECT CM.COMPO_VER_GID"
            + " FROM vms_component_mapping CM, vms_caseserver_mapping CS"
            + " WHERE CS.GID = CM.CASE_SERVER_GID"
            + " AND CS.CASE_GID = ?) AND IS_DELETED = 0"
            + " GROUP BY RULE"
            + " HAVING COUNT(GID) > 1"
            + " ORDER BY COUNT(GID) DESC"
            + " LIMIT 0,1) AND IS_DELETED = 0) AND CM.IS_DELETED = 0";
    
    public static String GET_VALIDS = "SELECT C.CASENAME, C.HIERARCHY_GID, C.GID, CO.COMPONENT, S.GID, S.SERVER, CM.PORT, CV.MAJOR, CV.MINOR, CV.BUILD, CV.PATCH, CM.COMPO_VER_GID, S.DOMAIN "
            + " FROM vms_cases C, vms_servers S, vms_caseserver_mapping CSM, vms_component_mapping CM, vms_compo_version CV, vms_components CO"
            + " WHERE S.GID = CSM.SERVER_GID"
            + " AND CSM.GID = CM.CASE_SERVER_GID"
            + " AND CM.COMPO_VER_GID = CV.GID"
            + " AND CV.COMP_GID = CO.GID"
            + " AND CM.IS_DELETED = 0"
            + " AND C.GID = CSM.CASE_GID"
            + " AND C.GID = ? "
            + " AND C.IS_DELETED = 0"
            + " AND (CM.COMPO_VER_GID IN ("
            + " SELECT DISTINCT CM.COMPO_VER_GID"
            + " FROM vms_component_mapping CM, vms_caseserver_mapping CS"
            + " WHERE CS.GID = CM.CASE_SERVER_GID"
            + " AND CS.CASE_GID = ?"
            + " AND COMPO_VER_GID  IN ("
            + " SELECT COMP_VER_GID FROM VMS_COMPATIBILITY_RULES"
            + " WHERE RULE ="
            + "("
            + " SELECT RULE FROM VMS_COMPATIBILITY_RULES"
            + " WHERE COMP_VER_GID IN ("
            + " SELECT CM.COMPO_VER_GID"
            + " FROM vms_component_mapping CM, vms_caseserver_mapping CS"
            + " WHERE CS.GID = CM.CASE_SERVER_GID"
            + " AND CS.CASE_GID = ?"
            + " AND CM.IS_DELETED = 0"
            + " )"
            + " AND IS_DELETED = 0"
            + " GROUP BY RULE"
            + " HAVING COUNT(GID) > 1"
            + " ORDER BY COUNT(GID) DESC"
            + " LIMIT 0,1"
            + ")"
            + " AND IS_DELETED = 0"
            + " )"
            + " AND CM.IS_DELETED = 0"
            + ") OR CV.MAJOR = 0)"
            + " ORDER BY CO.BE, S.DOMAIN, CO.COMPONENT ";
    
    public static String GET_INVALIDS = "SELECT C.CASENAME, C.HIERARCHY_GID, C.GID, CO.COMPONENT, S.GID, S.SERVER, CM.PORT, CV.MAJOR, CV.MINOR, CV.BUILD, CV.PATCH, CM.COMPO_VER_GID, S.DOMAIN "
            + " FROM vms_cases C, vms_servers S, vms_caseserver_mapping CSM, vms_component_mapping CM, vms_compo_version CV, vms_components CO"
            + " WHERE S.GID = CSM.SERVER_GID"
            + " AND CSM.GID = CM.CASE_SERVER_GID"
            + " AND CM.COMPO_VER_GID = CV.GID"
            + " AND CV.COMP_GID = CO.GID"
            + " AND CM.IS_DELETED = 0"
            + " AND C.GID = CSM.CASE_GID"
            + " AND C.GID = ? "
            + " AND C.IS_DELETED = 0 "
            + " AND CV.MAJOR <> 0 "
            + " AND CM.COMPO_VER_GID IN ("
            + " SELECT DISTINCT CM.COMPO_VER_GID"
            + " FROM vms_component_mapping CM, vms_caseserver_mapping CS"
            + " WHERE CS.GID = CM.CASE_SERVER_GID"
            + " AND CS.CASE_GID = ?"
            + " AND COMPO_VER_GID NOT IN ("
            + " SELECT COMP_VER_GID FROM VMS_COMPATIBILITY_RULES"
            + " WHERE RULE ="
            + "("
            + " SELECT RULE FROM VMS_COMPATIBILITY_RULES"
            + " WHERE COMP_VER_GID IN ("
            + " SELECT CM.COMPO_VER_GID"
            + " FROM vms_component_mapping CM, vms_caseserver_mapping CS"
            + " WHERE CS.GID = CM.CASE_SERVER_GID"
            + " AND CS.CASE_GID = ?"
            + " AND CM.IS_DELETED = 0"
            + " )"
            + " AND IS_DELETED = 0"
            + " GROUP BY RULE"
            + " HAVING COUNT(GID) > 1"
            + " ORDER BY COUNT(GID) DESC"
            + " LIMIT 0,1"
            + ")"
            + " AND IS_DELETED = 0"
            + " )"
            + " AND CM.IS_DELETED = 0"
            + ")"
            + " ORDER BY CO.BE, S.DOMAIN, CO.COMPONENT ";
    
    public static String GET_VERSION_COMPONENTS = "SELECT  C.CASENAME, S.SERVER, S.DOMAIN, "
            + " COMP.COMPONENT, CV.MAJOR, CV.MINOR, CV.BUILD,CV.PATCH, CM.PORT"
            + " FROM vms_cases C, vms_servers S, vms_components COMP, vms_compo_version CV,"
            + " vms_caseserver_mapping CSM, vms_component_mapping CM "
            + " WHERE CV.COMP_GID = COMP.GID "
            + " AND CV.GID = CM.COMPO_VER_GID "
            + " AND CM.IS_DELETED = 0 "
            + " AND C.IS_DELETED = 0 "
            + " AND CM.CASE_SERVER_GID = CSM.GID "
            + " AND CSM.CASE_GID = C.GID "
            + " AND CSM.SERVER_GID = S.GID ";
    
    public static String GET_CASE_COMPONENTS_COMPONENTS = "SELECT  C.CASENAME, S.SERVER, S.DOMAIN,  "
            + " COMP.COMPONENT, CV.MAJOR, CV.MINOR, CV.BUILD,CV.PATCH,CM.PORT "
            + " FROM vms_cases C, vms_servers S, vms_components COMP, vms_compo_version CV, "
            + " vms_caseserver_mapping CSM, vms_component_mapping CM "
            + " WHERE CV.COMP_GID = COMP.GID "
            + " AND CV.GID = CM.COMPO_VER_GID"
            + " AND CM.IS_DELETED = 0"
            + " AND C.IS_DELETED = 0"
            + " AND CM.CASE_SERVER_GID = CSM.GID"
            + " AND CSM.CASE_GID = C.GID"
            + " AND CSM.SERVER_GID = S.GID"
            + " AND COMP.GID = ?"
            + " AND C.GID = ?"
            + " ORDER BY S.DOMAIN, C.CASENAME, COMP.COMPONENT";
    
    public static String GET_COMPONENTLIST = "SELECT GID, COMPONENT FROM vms_components ORDER BY BE DESC, COMPONENT";
    
    public static String GET_CASE_COMPONENTS_COMPONENTS_CA = "SELECT  C.CASENAME, S.SERVER, S.DOMAIN,  "
            + " COMP.COMPONENT, CV.MAJOR, CV.MINOR, CV.BUILD,CV.PATCH,CM.PORT "
            + " FROM vms_cases C, vms_servers S, vms_components COMP, vms_compo_version CV, "
            + " vms_caseserver_mapping CSM, vms_component_mapping CM "
            + " WHERE CV.COMP_GID = COMP.GID "
            + " AND CV.GID = CM.COMPO_VER_GID"
            + " AND CM.IS_DELETED = 0"
            + " AND C.IS_DELETED = 0"
            + " AND CM.CASE_SERVER_GID = CSM.GID"
            + " AND CSM.CASE_GID = C.GID"
            + " AND CSM.SERVER_GID = S.GID"
            + " AND C.GID = ?"
            + " ORDER BY S.DOMAIN, C.CASENAME, COMP.COMPONENT";
    
    public static String GET_CASE_COMPONENTS_COMPONENTS_CO = "SELECT  C.CASENAME, S.SERVER, S.DOMAIN,  "
            + " COMP.COMPONENT, CV.MAJOR, CV.MINOR, CV.BUILD,CV.PATCH,CM.PORT "
            + " FROM vms_cases C, vms_servers S, vms_components COMP, vms_compo_version CV, "
            + " vms_caseserver_mapping CSM, vms_component_mapping CM "
            + " WHERE CV.COMP_GID = COMP.GID "
            + " AND CV.GID = CM.COMPO_VER_GID"
            + " AND CM.IS_DELETED = 0"
            + " AND C.IS_DELETED = 0"
            + " AND CM.CASE_SERVER_GID = CSM.GID"
            + " AND CSM.CASE_GID = C.GID"
            + " AND CSM.SERVER_GID = S.GID"
            + " AND COMP.GID = ?"
            + " ORDER BY S.DOMAIN, C.CASENAME, COMP.COMPONENT";
    
    public static String RESOLVE_CASENAME = "SELECT GID FROM vms_cases WHERE CASENAME = ?";
    
    public static String GET_MAJORS = "SELECT  DISTINCT MAJOR FROM vms_compo_version WHERE MAJOR > 0 ORDER BY MAJOR";
    
    public  static String GET_MINORS = "SELECT  DISTINCT MINOR FROM vms_compo_version ORDER BY MINOR";
    
    public static String GET_BUILDS = "SELECT  DISTINCT BUILD FROM vms_compo_version ORDER BY BUILD";
    
    public static String GET_PATCHS = "SELECT  DISTINCT PATCH FROM vms_compo_version ORDER BY PATCH";
    
    public static String UPDATE_HGID = "UPDATE vms_cases SET HIERARCHY_GID = ? WHERE CASENAME = ?";
    
    public static String GET_HGID = "SELECT IFNULL(HIERARCHY_GID, 0) FROM vms_cases WHERE CASENAME = ?";
    
    public static String GET_SNAPSHOT = "SELECT CASE_SERVER_GID, PORT, IS_DELETED from vms_component_mapping"
            + " WHERE CASE_SERVER_GID IN (SELECT GID FROM vms_caseserver_mapping WHERE CASE_GID = (SELECT GID FROM vms_cases WHERE CASENAME = ?) AND SERVER_GID IN ("
            + " SELECT GID FROM vms_servers WHERE DOMAIN = ?))";
    
    public static String RESET_DEL_FLAGS = "UPDATE vms_component_mapping A SET A.IS_DELETED = 1"
            + " WHERE A.CASE_SERVER_GID IN (SELECT GID FROM vms_caseserver_mapping where CASE_GID = (SELECT GID FROM vms_cases WHERE CASENAME = ?) AND SERVER_GID IN ("
            + " SELECT GID FROM vms_servers WHERE DOMAIN = ?))";
    
/*
    static String GET_SNAPSHOT = "SELECT CASE_SERVER_GID, PORT, IS_DELETED from vms_component_mapping"
            + " WHERE CASE_SERVER_GID IN (SELECT GID FROM vms_caseserver_mapping WHERE CASE_GID = ?)AND"
            + " COMPO_VER_GID IN (SELECT GID FROM vms_compo_version WHERE COMP_GID IN ("
            + " SELECT GID FROM vms_components WHERE COMPONENT = ?))";
 
 
    static String RESET_DEL_FLAGS = "UPDATE vms_component_mapping A SET A.IS_DELETED = 1"
            + " WHERE A.CASE_SERVER_GID IN (SELECT GID FROM vms_caseserver_mapping where CASE_GID = ?)AND"
            + " A.COMPO_VER_GID IN (SELECT GID FROM vms_compo_version WHERE COMP_GID IN ("
            + " SELECT GID FROM vms_components WHERE COMPONENT = ?))";
 */
    
    public static String RESET_DEL_FLAGS_ALL = "UPDATE vms_component_mapping A SET A.IS_DELETED = 1";
    
    public static String UPDATE_MOD_DATE_COMPOGID = "UPDATE vms_component_mapping SET MODIFIED_DATE = CURRENT_TIMESTAMP"
            + "  WHERE GID = ?";
    
    public static String INS_HISTORY = "INSERT INTO vms_mailer_history(GID, CASE_GID, ERROR_MESSAGE) VALUES (NULL, ?, ?)";
    
    public  static String INS_REDUNDANT_HISTORY = "INSERT INTO vms_mailer_history(CASE_GID, SERVER_GID, COMPONENT_GID, ERROR_MESSAGE)"
            +  " SELECT CAS.GID , SERV.GID , COMPO.GID, ?  FROM "
            + "vms_component_mapping COMPOMAP, vms_caseserver_mapping CASEMAP,"
            + "vms_compo_version VERS, vms_components COMPO, vms_servers SERV,"
            + "vms_cases CAS"
            + " WHERE COMPOMAP.GID IN("
            + " SELECT DISTINCT MAP.GID"
            + " FROM vms_component_mapping MAP, vms_component_mapping MAP1"
            + " WHERE MAP.CASE_SERVER_GID = MAP1.CASE_SERVER_GID AND"
            + " MAP.COMPO_VER_GID = MAP1.COMPO_VER_GID AND"
            + " MAP.GID <> MAP1.GID) AND"
            + " COMPOMAP.IS_DELETED = 0 AND"
            + " CAS.IS_DELETED = 0 AND"
            + " COMPOMAP.CASE_SERVER_GID = CASEMAP.GID AND"
            + " CASEMAP.CASE_GID = CAS.GID AND"
            + " CASEMAP.SERVER_GID = SERV.GID AND"
            + " COMPOMAP.COMPO_VER_GID = VERS.GID AND"
            + " VERS.COMP_GID = COMPO.GID"
            + " ORDER BY CAS.GID";
    
    public static String INS_RULE_METADATA = "INSERT INTO vms_rules_info(GID, NAME, DESCRIPTION, RULE, CREATED_BY) VALUES (NULL,?,?,?,?)";
    
    public  static String GET_RULE_OBJECT = "SELECT INFO.RULE, INFO.NAME, INFO.DESCRIPTION  FROM  vms_rules_info INFO WHERE  INFO.RULE = ? ";
    
    public  static String DEL_RULE_INFO = "DELETE FROM vms_rules_info WHERE RULE = ? ";
    
    public static String GET_COMPO_VER_GID_RULE = "SELECT COMP_VER_GID FROM vms_compatibility_rules "
            + " WHERE RULE = ? "
            + " AND IS_DELETED = 0 "
            + " ORDER BY COMP_VER_GID";
    
    public static String UPDATE_RULE_INFO = " UPDATE vms_rules_info "
            + " SET NAME = ?, DESCRIPTION = ?, MODIFIED_BY = ?, MODIFIED_AT = NOW() "
            + " WHERE RULE = ?";
    
//    static String RESET_CASE_FLAGS = " UPDATE vms_cases SET IS_DELETED = 1";
    //TODO
    public static String RESET_CASE_FLAGS = "UPDATE vms_cases AS C, vms_servers AS S, vms_caseserver_mapping AS MAP"
            + " SET C.IS_DELETED = 1 "
            + " WHERE C.GID = MAP.CASE_GID"
            + " AND S.GID = MAP.SERVER_GID"
            + " AND S.DOMAIN in (?, ?)";
    
    public static String RESET_CASE_FLAG = " UPDATE vms_cases SET IS_DELETED = 0 WHERE GID = ?";
    
    public  static String GET_CASE_SERVERS_APAC;
    
    public static String GET_ANNOTATION_SEARCH_SERVER = "SELECT SERVER_URL FROM DS_FULLTEXT_METADATA_CATALOG_LIST(NOLOCK)";
    
    public static String GET_MASTER_COMPONENTS = "SELECT [NAME] AS SERVERCATEGORY, [VALUE] AS URL FROM DBADMIN.DS_DATAHUB_PREF(NOLOCK) WHERE NAME IN "
            + "('MasterServer/Extraction', 'MasterServer/DocProcessor', 'MasterServer/ConceptOrganization')"
            + " AND LOWER(VALUE) <> 'please provide the url'";
    
    //to get secondary caseshares for 9.0
    public static String GET_FE_SECCASESHARES = "SELECT DISTINCT PREFIX FROM DS_CASECONFIG_UNIVERSE(NOLOCK)"
            + " WHERE (LEN(PREFIX) <> 0 AND PREFIX IS NOT NULL)";

    public static String INS_CRAWL_ENTRY = "INSERT INTO vms_crawler_run_history(crawl_date, start_time, stage) VALUES(?,?,?)";

    public static String UPDATE_CRAWL_ENTRY = "UPDATE vms_crawler_run_history "
            + " SET end_time = ?, status = 'SUCCESS' "
            + " WHERE crawl_date = ? AND stage = ? ";

    public static String INS_CRAWL_DEVIATION = "INSERT INTO vms_crawl_deviation_history VALUES (NULL,?,?,?,?)";
    
    
    
    /**
     * Creates a new instance of VMSDAOQueries
     */
    public VMSDAOQueries() {
        
    }
    
}
