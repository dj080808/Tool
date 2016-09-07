/*
 *  Copyright (c) 2016 Nokia. All rights reserved.
 *
 *  Revision History:
 *
 *  DATE/AUTHOR          COMMENT
 *  ---------------------------------------------------------------------
 *  2016年9月2日/junldai                            
 */
package tool;

import org.apache.log4j.Logger;

public class Log
{
    public static void main( String[] args )
    {
        Logger logger = Logger.getLogger( Log.class );
        logger.debug( "hello world" );
    }
}
