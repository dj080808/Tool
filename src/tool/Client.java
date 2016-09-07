package tool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.log4j.Logger;

public class Client
{
    private static List<User> userList = new ArrayList();

    private static List<User> removedUserRows = new ArrayList();

    private static JTable table;

    private static JCheckBox isShowAllUserCheckBox;

    private static JCheckBox showAdminCheckBox;

    public static JButton next;

    public static JComboBox userComboBox;

    static JFrame frame;

    public static boolean isOnlyShowInUser = false;

    public static final int TWO_VS_TWO_MODE = 1;

    public static final int ZHUANG_MODE = 2;

    public static int gameMode = TWO_VS_TWO_MODE;

    private static DB db = new DB();

    public static final String ADMIN_NAME = "管理员";

    private static final Logger log = Logger.getLogger( Client.class );

    public static boolean isValidateTable = true;

    public static User adminUser;

    public static void main( String[] args )
    {
        new InstanceControl().start();
        loadData();

        frame = new JFrame( "统计小工具" );
        frame.setSize( 800, 500 );

        buildUpPanel( frame );
        buildMiddleTable( frame );
        buildDownPanel( frame );
        frame.setVisible( true );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }

    private static void loadData()
    {
        userList = db.read();
        adminUser = getUserByUserName( ADMIN_NAME );
        userList.remove( adminUser );
    }

    private static void buildMiddleTable( JFrame frame )
    {
        table = new JTable( new MyTableModel( userList ) );
        table.putClientProperty( "terminateEditOnFocusLost", Boolean.TRUE );
        table.setDefaultRenderer( Object.class, new MyTableCellRender() );
//        table.getColumnModel().getColumn( 3 ).setCellRenderer( new MyTableCellRender() );
//        table.getColumnModel().getColumn( 4 ).setCellRenderer( new MyTableCellRender() );
//        table.getColumnModel().getColumn( 5 ).setCellRenderer( new MyTableCellRender() );
        JScrollPane panel = new JScrollPane( table );
        frame.add( panel, "Center" );
    }

    public static void setWater( double water )
    {
//        adminUser.setRemaining( adminUser.getRemaining() + water );
        adminUser.setResultStep( water );
    }

    public static int amount = 0;

    private static JLabel roundAmountLabel;

    private static void buildRadioMode( JPanel panel )
    {
        panel.add( new JLabel( "                " ) );
        JRadioButton radioButton1 = new JRadioButton( "2v2混战模式" );
        radioButton1.setSelected( true );
        radioButton1.addActionListener( new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                if( gameMode != TWO_VS_TWO_MODE )
                {
                    for( User user : userList )
                    {
                        user.setResultStep( 0 );
                    }
                    refreshTable();
                    if( next.isEnabled() )
                    {
                        Client.next.setEnabled( false );
                    }
                }
                gameMode = TWO_VS_TWO_MODE;
                userComboBox.setEnabled( false );
            }
        } );
        panel.add( radioButton1 );

        JRadioButton radioButton2 = new JRadioButton( "坐庄模式" );
        radioButton2.addActionListener( new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                if( gameMode != ZHUANG_MODE )
                {
                    for( User user : userList )
                    {
                        user.setResultStep( 0 );
                    }
                    refreshTable();
                    if( next.isEnabled() )
                    {
                        Client.next.setEnabled( false );
                    }
                }
                gameMode = ZHUANG_MODE;
                userComboBox.setEnabled( true );

            }
        } );

        userComboBox = new JComboBox();
        userComboBox.setPreferredSize( new Dimension( 50, 20 ) );
//        userComboBox.setSize( 50, 20 );
        userComboBox.setEnabled( false );
        userComboBox.addItem( "----庄家----" );
        userComboBox.addItemListener( new ItemListener()
        {
            @Override
            public void itemStateChanged( ItemEvent e )
            {
                for( User user : userList )
                {
                    user.setResultStep( 0 );
                }
                refreshTable();
                if( next.isEnabled() )
                {
                    Client.next.setEnabled( false );
                }

            }
        } );
        for( User user : userList )
        {
            userComboBox.addItem( user.getUsername() );
        }
        panel.add( radioButton2 );
        panel.add( userComboBox );

        ButtonGroup group = new ButtonGroup();
        group.add( radioButton1 );
        group.add( radioButton2 );
    }

    private static void buildUpPanel( JFrame frame )
    {
        JPanel panel = new JPanel();
        panel.setLayout( new BoxLayout( panel, 0 ) );
        showAdminCheckBox = new JCheckBox();
        showAdminCheckBox.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                if( showAdminCheckBox.isSelected() )
                {
                    userList.add( adminUser );
                    refreshTable();
                }
                else
                {
                    userList.remove( adminUser );
                    refreshTable();
                }

            }
        } );
        panel.add( showAdminCheckBox );
        JLabel todayRoundsLabel = new JLabel( "今日场次：" );
        panel.add( todayRoundsLabel );
        roundAmountLabel = new JLabel( String.valueOf( amount ) );
        panel.add( roundAmountLabel );
        buildRadioMode( panel );
        panel.add( Box.createHorizontalGlue() );
        isShowAllUserCheckBox = new JCheckBox( "仅显示参与用户" );
        isShowAllUserCheckBox.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                refreshTable();
            }
        } );
        panel.add( isShowAllUserCheckBox );
        JButton caculate = new JButton( "计算本局" );
        caculate.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                int totalUserAmount = 0;
                for( User user : Client.userList )
                {
                    if( user.isIn() )
                    {
                        totalUserAmount++;
                    }
                    if( ( user.isIn() ) && ( user.getJoinNumber() == 0 ) )
                    {
                        JOptionPane.showMessageDialog( null, "用户'" + user.getUsername() + "'押注为空" );
                        return;
                    }
                }
                for( User user : Client.userList )
                {
                    if( user.isIn() && isNotEnough( user.getUsername() ) )
                    {
                        JOptionPane.showMessageDialog( null, "用户'" + user.getUsername() + "'需要充值" );
                        return;
                    }
                }
                if( totalUserAmount != 0 )
                {
                    caculate();
                    Client.next.setEnabled( true );
                    isValidateTable = false;
                    ( ( MyTableModel ) Client.table.getModel() ).fireTableDataChanged();
                    isValidateTable = true;
                }
            }

            private void caculate()
            {
                if( gameMode == ZHUANG_MODE )
                {
                    User hostUser = getUserByUserName( ( String ) userComboBox.getSelectedItem() );//TODO:
                    if( hostUser == null )
                    {
                        JOptionPane.showMessageDialog( null, "坐庄模式必须选择庄家！" );
                        return;
                    }
                    Caculator.getInstance().zhuangMode( Client.userList, hostUser );
                }
                else
                {
                    Caculator.getInstance().twoVsTwo( Client.userList );
                }
            }
        } );
        panel.add( caculate );

        next = new JButton( "确定并开始下一局" );
        next.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                for( User user : userList )
                {
                    if( user.isIn() )
                    {
                        user.setRemaining( user.getRemaining() + user.getResultStep() );
                    }
                }
                if( isResetRound() )
                {
                    Client.amount = 1;
                    Client.roundAmountLabel.setText( String.valueOf( Client.amount ) );
                }
                else
                {
                    Client.amount += 1;
                    Client.roundAmountLabel.setText( String.valueOf( Client.amount ) );
                }
                adminUser.setRemaining( adminUser.getRemaining() + adminUser.getResultStep() );
                Client.db.update( Client.userList );
                if( gameMode == TWO_VS_TWO_MODE )
                {
                    log.info( "2v2模式(" + Caculator.calculate_detail_log + ")" );
                }
                else
                {
                    log.info( "坐庄模式，庄家 " + ( String ) userComboBox.getSelectedItem() + "(" +
                        Caculator.calculate_detail_log + ")" );
                }
                Caculator.calculate_detail_log = "";
//                log.info( "确定并保存本局" );
                adminUser.setResultStep( 0.0D );
                for( User user : Client.userList )
                {
                    user.setResultPoint( 0 );
                    user.setResultStep( 0.0D );
                }
                ( ( MyTableModel ) Client.table.getModel() ).fireTableDataChanged();
                Client.next.setEnabled( false );
            }
        } );
        next.setEnabled( false );
        panel.add( next );
        frame.add( panel, "North" );
    }

    public static long currentDateMilliTime = 0;

    private static long oneDaySpace = 86400000;

    public static long getTodayStart()
    {
        Calendar date1 = Calendar.getInstance();
        date1.set( Calendar.HOUR_OF_DAY, 12 );
        date1.set( Calendar.MINUTE, 0 );
        date1.set( Calendar.SECOND, 0 );
        return date1.getTimeInMillis();
    }

    private static boolean isResetRound()
    {
        if( currentDateMilliTime == 0 )
        {
            currentDateMilliTime = getTodayStart();
            return false;
        }
        if( Calendar.getInstance().getTimeInMillis() - currentDateMilliTime < oneDaySpace )
        {
            return false;
        }
        else
        {
            currentDateMilliTime = getTodayStart();
            return true;
        }
    }

    private static JTextField newUser = new JTextField( 12 );

    private static JTextField numberTextField = new JTextField( 5 );

    private static void createNewUser()
    {
        String userName = newUser.getText();
        double number = 0.0D;
        if( ( userName == null ) || ( userName.isEmpty() ) )
        {
            JOptionPane.showMessageDialog( null, "用户名不能为空，开户失败" );
            return;
        }
        try
        {
            number = Double.parseDouble( numberTextField.getText() );
        }
        catch( Exception e )
        {
            JOptionPane.showMessageDialog( null, "充值金额无效，开户失败" );
            return;
        }
        for( User tmp : userList )
        {
            if( tmp.getUsername().equals( userName ) )
            {
                JOptionPane.showMessageDialog( null, "用户名已存在，开户失败" );
                return;
            }
        }
        User user = new User( userList.size(), userName, number );
        userList.add( user );
        db.insert( user );
        newUser.setText( "" );
        numberTextField.setText( "" );
        userComboBox.addItem( userName );
        log.info( "开户      用户名:" + userName + " 金额:" + number );
        JOptionPane.showMessageDialog( null, "开户成功" );
    }

    private static void buildDownPanel( JFrame frame )
    {
        JPanel panel = new JPanel();
        JPanel newUserPanel = new JPanel();
        newUserPanel.add( new JLabel( "用户名" ) );
        newUserPanel.add( newUser );
        newUserPanel.add( new JLabel( "金额" ) );
        newUserPanel.add( numberTextField );
        JButton newUser = new JButton( "开户" );
        newUser.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                Client.createNewUser();
                ( ( MyTableModel ) Client.table.getModel() ).fireTableDataChanged();
            }
        } );
        newUserPanel.add( newUser );
        newUserPanel.setBorder( BorderFactory.createLineBorder( Color.gray ) );
        panel.add( newUserPanel );

        JButton delUser = new JButton( "销户" );
        delUser.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                if( Client.table.getSelectedRow() != -1 )
                {
                    String userName = ( String ) Client.table.getValueAt( Client.table.getSelectedRow(), 1 );
                    if( userName.equals( ADMIN_NAME ) )
                    {
                        JOptionPane.showMessageDialog( null, "管理员账户不得删除" );
                        return;
                    }
                    int result = JOptionPane.showConfirmDialog( null, "确认删除用户：" + userName );
                    if( result == 0 )
                    {
                        User delUser = Client.getUserByUserName( userName );
                        System.out.println( delUser );
                        Client.userList.remove( delUser );
                        Client.db.delete( delUser );
                        userComboBox.removeItem( userName );
                        log.info( "销户      用户名:" + userName + " 金额:" + delUser.getRemaining() );
                        ( ( MyTableModel ) Client.table.getModel() ).fireTableDataChanged();
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "请在表格中选中要销户的用户行" );
                }
            }
        } );
        panel.add( delUser );

        JButton moneyIn = new JButton( "充值" );
        moneyIn.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                if( Client.table.getSelectedRow() != -1 )
                {
                    String userName = ( String ) Client.table.getValueAt( Client.table.getSelectedRow(), 1 );
                    String result = JOptionPane.showInputDialog( null, "请输入" + userName + "充值金额" );
                    User user = Client.getUserByUserName( userName );
                    try
                    {
                        double money = Double.parseDouble( result );
                        if( money < 0 )
                        {
                            JOptionPane.showMessageDialog( null, "充值金额必须大于0，取款失败" );
                            return;
                        }
                        user.setRemaining( user.getRemaining() + money );
                        Client.db.update( Client.getUserByUserName( userName ) );
                        log.info( "充值      用户名:" + userName + " 金额:" + result );
                    }
                    catch( Exception e2 )
                    {
                        JOptionPane.showMessageDialog( null, "金额格式不正确，充值失败" );
                    }
                    ( ( MyTableModel ) Client.table.getModel() ).fireTableDataChanged();
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "请在表格中选中要充值的用户行" );
                }
            }
        } );
        panel.add( moneyIn );

        JButton moneyOut = new JButton( "取款" );
        moneyOut.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                if( Client.table.getSelectedRow() != -1 )
                {
                    String userName = ( String ) Client.table.getValueAt( Client.table.getSelectedRow(), 1 );
                    String result = JOptionPane.showInputDialog( null, "请输入" + userName + "取款金额" );
                    User user = Client.getUserByUserName( userName );
                    try
                    {
                        double money = Double.parseDouble( result );
                        if( money < 0 )
                        {
                            JOptionPane.showMessageDialog( null, "取款金额必须大于0，取款失败" );
                            return;
                        }
                        if( money > user.getRemaining() )
                        {
                            JOptionPane.showMessageDialog( null, "取款金额大于余额，取款失败" );
                            return;
                        }
                        user.setRemaining( user.getRemaining() - money );
                        log.info( "取款      用户名:" + userName + " 金额:" + result );
                    }
                    catch( Exception e2 )
                    {
                        JOptionPane.showMessageDialog( null, "金额格式不正确，取款失败" );
                    }
                    ( ( MyTableModel ) Client.table.getModel() ).fireTableDataChanged();
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "请在表格中选中要充值的用户行" );
                }
            }
        } );
        panel.add( moneyOut );

        JButton exportTodayDetail = new JButton( "导出今日明细" );
        exportTodayDetail.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
            }
        } );
        panel.add( exportTodayDetail );
        exportTodayDetail.setVisible( false );
        frame.add( panel, "South" );
    }

    private static User getUserByUserName( String userName )
    {
        for( User user : userList )
        {
            if( user.getUsername().equals( userName ) )
            {
                return user;
            }
        }
        return null;
    }

    private static User getUserById( int id )
    {
        for( User user : userList )
        {
            if( user.getId() == id )
            {
                return user;
            }
        }
        return null;
    }

    public static void refreshTable()
    {
        MyTableModel tableModel = ( MyTableModel ) table.getModel();
        List<User> dataList = tableModel.getDataList();
        if( !isShowAllUserCheckBox.isSelected() )
        {
            for( int i = removedUserRows.size() - 1; i >= 0; i-- )
            {
                dataList.add( ( User ) removedUserRows.get( i ) );
            }
            removedUserRows.clear();
        }
        else
        {
            for( int i = dataList.size() - 1; i >= 0; i-- )
            {
                if( !( ( User ) dataList.get( i ) ).isIn() )
                {
                    removedUserRows.add( ( User ) dataList.get( i ) );
                    dataList.remove( i );
                }
            }
        }
        tableModel.fireTableDataChanged();
    }

    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    public static boolean isNotEnough( String userName )
    {
        User currentUser = getUserByUserName( userName );
        boolean isSelfNotEnouth = isSelfNotEnough( currentUser );
        if( gameMode == ZHUANG_MODE )
        {
            if( userName.equals( userComboBox.getSelectedItem() ) )
            {
                return isSelfNotEnouth || currentUser.getRemaining() * 100 < getOtherUserTripleTotal( userName );
            }
            else
            {
                return isSelfNotEnouth || currentUser.getRemaining() < 3 * currentUser.getJoinNumber();
            }
        }
        else if(gameMode == TWO_VS_TWO_MODE)
        {
            return isSelfNotEnouth || currentUser.getRemaining() * 100 < getOtherUserTripleTotal( userName );
        }
        return false;
    }

    public static boolean isSelfNotEnough( User user )
    {
        return user.getJoinNumber() > user.getRemaining();
    }

    public static int getOtherUserTripleTotal( String userName )
    {
        int result = 0;
        User selfUser = getUserByUserName( userName );
        for( User user : userList )
        {
            if( user.isIn() && !user.getUsername().equals( userName ) )
            {
                result += 3 * Math.min( selfUser.getJoinNumber(), user.getJoinNumber() ) * 100;
            }
        }
        return result;
    }
}
