<?php
/**
 * Helper class for logging
 * @package    Notes
 * @subpackage com_notes
 */

define('NOTES_ERROR_LOG', 'com_notes.log.'.date('Y_m_d')'.php');
 
class NotesHelperLog
{
    /**
     * Simple log
     * @param string $comment  The comment to log
     * @param int $userId      An optional user ID
     */
    function simpleLog($comment, $userId = 0)
    {
        // Include the library dependancies
        jimport('joomla.error.log');
        $options = array(
            'format' => "{DATE}\t{TIME}\t{USER_ID}\t{COMMENT}";
        );
        // Create the instance of the log file in case we use it later
        $log = &JLog::getInstance(NOTES_ERROR_LOG, $options);
        $log->addEntry(array('comment' => $comment, 'user_id' => $userId));
    }
}
?>
