/*
 */
package com.smartkaya.log;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


/**
 * 全局日志管理
 * @author LiangChen
 *	2020-08-31
 */

public class KayaLogManager {
	LogManager logManager = LogManager.getLogManager();
	static Logger Logger;
	static FileHandler LogFileHandler;
    private static KayaLogManager instance = null;

    public static KayaLogManager getInstance() {
        if (instance == null) {
            instance = new KayaLogManager();
        }
        return instance;
    }

    public static void setFileHandler(FileHandler logFileHandler){
    	LogFileHandler = logFileHandler;
    }
    private KayaLogManager() {
        try {
        	Logger = java.util.logging.Logger.getLogger(this.getClass().getName());
        	Logger.setUseParentHandlers(true);
        	LogFileHandler.setEncoding("UTF-8");
			Logger.addHandler(LogFileHandler);

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(KayaLogManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    /**
     * @brief: This method is used to log message to file.
     * @param message
     */
    public void log(Level level,String logMessage) {
        //String strLevel = StewConfig.getInstance().logLevel;
        //log.fatal("StewConfig.getInstance().logLevel=" + strLevel);      
        //setLevel(Level.toLevel(strLevel));
        if (logMessage != null && !"".equals(logMessage)) {
//            Level loglevel = log.getLevel();
//
//            if (loglevel.equals(Level.TRACE)) {
//                log.trace(logMessage);
//            }
//            if (loglevel.equals(Level.DEBUG)) {
//                log.debug(logMessage);
//            }
//            if (loglevel.equals(Level.INFO)) {
//                log.info(logMessage);
//            }
//            if (loglevel.equals(Level.WARN)) {
//                log.warn(logMessage);
//            }
//            if (loglevel.equals(Level.ERROR)) {
//                log.error(logMessage);
//            }
//            if (loglevel.equals(Level.FATAL)) {
//                log.fatal(logMessage);
//            }
        }
    }

        /// <summary>
    /// Log message as debug
    /// </summary>
    /// <param name="message">message to log</param>
    public void debug(Object message) {
//        log.debug(message);
    }

        /// <summary>
    /// Log message as debug
    /// </summary>
    /// <param name="message">message to log</param>
    /// <param name="exception">exception log to file</param>
    public void debug(Object message, Exception exception) {
//        log.debug(message, exception);
    }

        /// <summary>
    /// Log message as info
    /// </summary>
    /// <param name="message">message to log</param>
    public void info(String sourceClass, String sourceMethod, Object message) {
    	Logger.logp(Level.INFO, sourceClass, sourceMethod, message.toString());
    }

        /// <summary>
    /// Log message as info
    /// </summary>
    /// <param name="message">message to log</param>
    /// <param name="exception">exception log to file</param>
    public void info(Object message) {
    	Logger.log(Level.INFO, message.toString());
//        log.info(message, exception);
    }

        /// <summary>
    /// Log message as warning
    /// </summary>
    /// <param name="message">message to log</param>
    public void warn(Object message) {
    	Logger.log(Level.WARNING, message.toString());
//        log.warn(message);
    }

        /// <summary>
    /// Log message as warning
    /// </summary>
    /// <param name="message">message to log</param>
    /// <param name="exception">exception log to file</param>
    public void warn(Object message, Exception exception) {
//        log.warn(message, exception);
    }

        /// <summary>
    /// Log message as error
    /// </summary>
    /// <param name="message">message to log</param>
    public void error(Object message) {
//        log.error(message);
    }

        /// <summary>
    /// Log message as error
    /// </summary>
    /// <param name="message">message to log</param>
    /// <param name="exception">exception log to file</param>
    public void error(Exception exception) {
    	Logger.log(Level.SEVERE, exception.getStackTrace().toString());
    }

        /// <summary>
    /// Log message as fatal
    /// </summary>
    /// <param name="message">message to log</param>
    public void fatal(Object message) {
//        log.fatal(message);
    }

        /// <summary>
    /// Log message as fatal
    /// </summary>
    /// <param name="message">message to log</param>
    /// <param name="exception">exception log to file</param>
    public void fatal(Object message, Exception exception) {
//        log.fatal(message, exception);
    }
}
