/*
 * VMSTransaction.java
 *
 * Created on June 26, 2009, 1:01 AM
 *
 * Confidential and Proprietary
 * (c) Copyright 1999 - 2009 Stratify, an Iron Mountain Company. ( f/k/a PurpleYogi f/k/a Calpurnia ). All rights reserved.
 * The foregoing shall not be deemed to indicate that this source has been published. 
 * Instead, it remains a trade secret of Stratify, an Iron Mountain Company.
 */

package com.stratify.apps.vms.dao;

import com.stratify.apps.vms.common.exceptions.VMSDAOException;

/**
 *
 * @author RavikishoreS
 */
public interface VMSTransaction {
     /* Start a transaction */
  public void begin() throws VMSDAOException;

  /* Rollback a transaction */
  public void rollback() throws VMSDAOException;

  /* End/Commit a transaction */
  public void end() throws VMSDAOException;

    
}
