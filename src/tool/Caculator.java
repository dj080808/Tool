package tool;

import java.util.List;

import org.apache.log4j.Logger;

public class Caculator
{

    private static Caculator instance = null;

    private Caculator()
    {
    }

    public static Caculator getInstance()
    {
        if( instance == null )
        {
            instance = new Caculator();
        }
        return instance;
    }

    public static void main( String[] args )
    {
    }

    public static Logger log = Logger.getLogger( Caculator.class );

    public void twoVsTwo( List<User> userList )
    {
        StringBuilder detail_log = new StringBuilder();
        double water = 0.0D;
        for( User currentUser : userList )
        {
            if( currentUser.isIn() )
            {
                double resultStep = 0.0D;
                for( User temp : userList )
                {
                    if( !currentUser.getUsername().equals( temp.getUsername() ) )
                    {
                        if( temp.isIn() )
                        {
                            double joinNumber =
                                currentUser.getJoinNumber() < temp.getJoinNumber() ? currentUser.getJoinNumber()
                                                : temp.getJoinNumber();
                            resultStep +=
                                joinNumber * getRegularUser1BeiLv( currentUser.getResultPoint(), temp.getResultPoint() );
                        }
                    }
                }
                if( resultStep > 0.0D )
                {
                    water += resultStep * 5.0D / 100.0D;
                    resultStep = resultStep * 95.0D / 100.0D;
                }
                buildBefore( detail_log, currentUser, resultStep );
//                currentUser.setRemaining( currentUser.getRemaining() + resultStep );
                currentUser.setResultStep( resultStep );
                buildAfter( detail_log, currentUser, ";" );
//                log.info( currentUser.getUsername() + "点数:" + currentUser.getResultPoint() + "输赢:" +
//                    currentUser.getResultStep() + "余额:" + currentUser.getRemaining() );
            }
            else
            {
                currentUser.setResultPoint( 0 );
                currentUser.setResultStep( 0 );
            }
        }

        buildBefore( detail_log, Client.adminUser, water );
        Client.setWater( water );
        buildAfter( detail_log, Client.adminUser, "." );
        calculate_detail_log = detail_log.toString();
    }

    public static String calculate_detail_log = "";

    public void zhuangMode( List<User> userlist, User hostUser )
    {
        StringBuilder detail_log = new StringBuilder(); 
        double water = 0.0d;
        double hostResult = 0.0d;
        for( User currentUser : userlist )
        {
            if( currentUser.isIn() && !hostUser.getUsername().equals( currentUser.getUsername() ) )
            {
                double resultStep = 0.0d;
                double joinNumber = currentUser.getJoinNumber();
                resultStep +=
                    joinNumber * getRegularUser1BeiLv( currentUser.getResultPoint(), hostUser.getResultPoint() );

                if( resultStep > 0.0d )
                {
                    resultStep = resultStep * 95.0D / 100.0D;
                    hostResult += -1 * resultStep;
                    water += resultStep * 5.0D / 100.0D;
                }

                String fuhao = resultStep > 0 ? " + " : " - ";
                buildBefore( detail_log, currentUser, resultStep );
//                currentUser.setRemaining( currentUser.getRemaining() + resultStep );
                currentUser.setResultStep( resultStep );

                buildAfter( detail_log, currentUser, ";" );
            }
            else if( !currentUser.isIn() && !hostUser.getUsername().equals( currentUser.getUsername() ) )
            {
                currentUser.setResultPoint( 0 );
                currentUser.setResultStep( 0 );
            }
        }
        buildBefore( detail_log, hostUser, hostResult );
        hostUser.setResultStep( hostResult );
//        hostUser.setRemaining( hostUser.getRemaining() + hostResult );
        buildAfter( detail_log, hostUser, ";" );

        buildBefore( detail_log, Client.adminUser, water );
        Client.setWater( water );
        buildAfter( detail_log, Client.adminUser, "." );
        calculate_detail_log = detail_log.toString();
    }

    private static void buildBefore( StringBuilder detail_log, User currentUser, double resultStep )
    {
        String fuhao = resultStep > 0 ? " + " : "";
        detail_log.append( currentUser.getUsername() ).append( ":点数" ).append( currentUser.getResultPoint() ).append(
            "押注" ).append( currentUser.getJoinNumber() ).append( "结果" ).append( currentUser.getRemaining() ).append(
            fuhao ).append(
            resultStep );
    }

    private static void buildAfter( StringBuilder detail_log, User currentUser, String end )
    {
        detail_log.append( " = " ).append( currentUser.getRemaining() ).append( end );
    }

    private int getRegularUser1BeiLv( int userPoint1, int userPoint2 )
    {
        if( userPoint1 == userPoint2 )
        {
            return 0;
        }
        int temp = 0;
        if( userPoint1 == 0 || userPoint1 == 10 )
        {
            return 3;
        }
        if( userPoint2 == 0 || userPoint2 == 10 )
        {
            return -3;
        }
        if( ( userPoint1 == 7 ) || ( userPoint1 == 8 ) || ( userPoint1 == 9 ) || ( userPoint2 == 7 ) ||
            ( userPoint2 == 8 ) || ( userPoint2 == 9 ) )
        {
            temp = 2;
        }
        else
        {
            temp = 1;
        }
        return userPoint1 < userPoint2 ? -1 * temp : temp;
    }
}
