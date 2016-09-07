/*   1:    */ package tool;
/*   2:    */ 
/*   3:    */ import java.text.DecimalFormat;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;

/*   7:    */ import javax.swing.table.AbstractTableModel;
/*   6:    */ 
/*   8:    */ 
/*   9:    */ class MyTableModel
/*  10:    */   extends AbstractTableModel
/*  11:    */ {
/*  12: 20 */   private final int COL_ID = 0;
/*  13: 22 */   private final int COL_USERNAME = 1;
/*  14: 24 */   private final int COL_REMAINING = 2;
/*  15: 26 */   private final int COL_ISIN = 3;
/*  16: 28 */   private final int COL_JOINNUMBER = 4;
/*  17: 30 */   private final int COL_RESULTPOINT = 5;
/*  18: 32 */   private final int COL_RESULTSTEP = 6;
/*  19: 34 */   private String[] columnNames = { "序号", "用户名", "余额", "参与本局?", "下注金额", "点数", "输赢" };
/*  20: 36 */   private List<User> dataList = new ArrayList();
/*  21:    */   
/*  22:    */   public MyTableModel(List<User> dataList)
/*  23:    */   {
/*  24: 40 */     this.dataList = dataList;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public List<User> getDataList()
/*  28:    */   {
/*  29: 45 */     return this.dataList;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setDataList(List<User> dataList)
/*  33:    */   {
/*  34: 50 */     this.dataList = dataList;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public int getColumnCount()
/*  38:    */   {
/*  39: 55 */     return this.columnNames.length;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int getRowCount()
/*  43:    */   {
/*  44: 60 */     return this.dataList.size();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String getColumnName(int col)
/*  48:    */   {
/*  49: 65 */     return this.columnNames[col];
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setValueAt(Object aValue, int rowIndex, int columnIndex)
/*  53:    */   {
/*  54: 71 */     if ((aValue != null) && (rowIndex < this.dataList.size()) && (this.dataList.get(rowIndex) != null))
/*  55:    */     {
/*  56: 73 */       User user = (User)this.dataList.get(rowIndex);
/*  57: 74 */       switch (columnIndex)
/*  58:    */       {
/*  59:    */       case 3: 
/*  60: 77 */         user.setIn(((Boolean)aValue).booleanValue());
/*  61: 78 */         Client.refreshTable();
/*  62: 79 */         break;
/*  63:    */       case 4: 
/*  64: 81 */         int joinNumber = Integer.parseInt(aValue.toString());
/*  65: 82 */         if (joinNumber > 0) {
/*  66: 84 */           user.setJoinNumber(joinNumber);
/*  67:    */         } else {
/*  68: 88 */           user.setJoinNumber(0);
/*  69:    */         }
/*  70: 90 */         Client.next.setEnabled(false);
/*  71: 91 */         break;
/*  72:    */       case 5: 
/*  73: 93 */         int point = Integer.parseInt(aValue.toString());
/*  74: 94 */      if( ( point <= 10 ) && ( point >= 0 ) )
                    {
/*  75: 96 */           user.setResultPoint(Integer.parseInt(aValue.toString()));
/*  76:    */         } else {
/*  77:100 */           user.setResultPoint(0);
/*  78:    */         }
/*  79:103 */         Client.next.setEnabled(false);
/*  80:104 */         break;
/*  81:    */       }
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public Object getValueAt(int row, int col)
/*  86:    */   {
/*  87:114 */     User user = (User)this.dataList.get(row);
/*  88:115 */     switch (col)
/*  89:    */     {
/*  90:    */     case 0: 
/*  91:118 */       return Integer.valueOf(row + 1);
/*  92:    */     case 1: 
/*  93:120 */       return user.getUsername();
/*  94:    */     case 2: 
/*  95:122 */       return new DecimalFormat("#.00").format(user.getRemaining());
/*  96:    */     case 3: 
/*  97:124 */       return Boolean.valueOf(user.isIn());
/*  98:    */     case 4: 
/*  99:126 */       return Integer.valueOf(user.getJoinNumber());
/* 100:    */     case 5: 
/* 101:128 */       return Integer.valueOf(user.getResultPoint());
/* 102:    */     case 6: 
/* 103:130 */       return Double.valueOf(user.getResultStep());
/* 104:    */     }
/* 105:132 */     return "";
/* 106:    */   }
/* 107:    */   
/* 108:    */   public Class getColumnClass(int c)
/* 109:    */   {
/* 110:139 */     switch (c)
/* 111:    */     {
/* 112:    */     case 3: 
/* 113:142 */       return Boolean.class;
/* 114:    */     case 5: 
/* 115:144 */       return Integer.class;
/* 116:    */     case 4: 
/* 117:146 */       return Integer.class;
/* 118:    */     }
/* 119:148 */     return String.class;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean isCellEditable(int rowIndex, int columnIndex)
/* 123:    */   {
/* 124:155 */     if ((columnIndex <= 2) || (columnIndex == 6)) {
/* 125:157 */       return false;
/* 126:    */     }
/* 127:159 */if( getValueAt( rowIndex, 1 ).equals( Client.ADMIN_NAME ) )
        {
/* 128:161 */       return false;
/* 129:    */     }
/* 130:163 */     return true;
/* 131:    */   }
/* 132:    */ }

