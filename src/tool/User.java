/*   1:    */ package tool;
/*   2:    */ 
/*   3:    */ public class User
/*   4:    */ {
/*   5:    */   private int id;
/*   6:    */   private String username;
/*   7:    */   private double remaining;
/*   8:    */   private boolean isIn;
/*   9:    */   private int joinNumber;
/*  10:    */   private int resultPoint;
/*  11:    */   private double resultStep;
/*  12:    */   
/*  13:    */   public User(int id, String username, double number)
/*  14:    */   {
/*  15: 36 */     this.username = username;
/*  16: 37 */     this.remaining = number;
/*  17: 38 */     this.isIn = false;
/*  18: 39 */     this.joinNumber = 0;
/*  19: 40 */     this.resultPoint = 0;
/*  20: 41 */     this.resultStep = 0.0D;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public int getId()
/*  24:    */   {
/*  25: 46 */     return this.id;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setId(int id)
/*  29:    */   {
/*  30: 51 */     this.id = id;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String getUsername()
/*  34:    */   {
/*  35: 56 */     return this.username;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setUsername(String username)
/*  39:    */   {
/*  40: 61 */     this.username = username;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public double getRemaining()
/*  44:    */   {
/*  45: 66 */     return this.remaining;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setRemaining(double remaining)
/*  49:    */   {
/*  50: 71 */     this.remaining = remaining;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean isIn()
/*  54:    */   {
/*  55: 76 */     return this.isIn;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setIn(boolean isIn)
/*  59:    */   {
/*  60: 81 */     this.isIn = isIn;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int getJoinNumber()
/*  64:    */   {
/*  65: 86 */     return this.joinNumber;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setJoinNumber(int joinNumber)
/*  69:    */   {
/*  70: 91 */     this.joinNumber = joinNumber;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int getResultPoint()
/*  74:    */   {
/*  75: 96 */     return this.resultPoint;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setResultPoint(int resultPoint)
/*  79:    */   {
/*  80:101 */     this.resultPoint = resultPoint;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public double getResultStep()
/*  84:    */   {
/*  85:106 */     return this.resultStep;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setResultStep(double resultStep)
/*  89:    */   {
/*  90:111 */     this.resultStep = resultStep;
/*  91:    */   }
/*  92:    */ }

