/*   1:    */ package tool;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   6:    */ import java.util.ArrayList;
/*   8:    */ import java.util.List;

/*   9:    */ import javax.swing.JOptionPane;
/*  10:    */ import javax.xml.parsers.DocumentBuilder;
/*  11:    */ import javax.xml.parsers.DocumentBuilderFactory;
/*  12:    */ import javax.xml.parsers.ParserConfigurationException;
/*  13:    */ import javax.xml.transform.Transformer;
/*  14:    */ import javax.xml.transform.TransformerException;
/*  15:    */ import javax.xml.transform.TransformerFactory;
/*  16:    */ import javax.xml.transform.TransformerFactoryConfigurationError;
/*  17:    */ import javax.xml.transform.dom.DOMSource;
/*  18:    */ import javax.xml.transform.stream.StreamResult;

/*  19:    */ import org.w3c.dom.Document;
/*  20:    */ import org.w3c.dom.Element;
/*  22:    */ import org.w3c.dom.NodeList;
/*  23:    */ import org.xml.sax.SAXException;
/*   7:    */ 
/*   5:    */ 
/*  21:    */ 
/*  24:    */ 
/*  25:    */ public class DB
/*  26:    */ {
/*  27: 42 */   private DocumentBuilder builder = null;
/*  28:    */   private static final String DB_PATH = "db.xml";
/*  29:    */   
/*  30:    */   public static void main(String[] args)
/*  31:    */   {
/*  32: 45 */     DB db = new DB();
/*  33:    */     
/*  34: 47 */     List<User> userList = db.read();
/*  35: 48 */     System.out.println(userList.size());
/*  36:    */     
/*  37: 50 */     User user = new User(0, "hook89", 12345.0D);
/*  38: 51 */     db.insert(user);
/*  39:    */     
/*  40: 53 */     userList = db.read();
/*  41: 54 */     System.out.println(userList.size());
/*  42:    */     
/*  43: 56 */     user = new User(0, "hook96", 12345.0D);
/*  44: 57 */     db.update(user);
/*  45: 58 */     userList = db.read();
/*  46: 59 */     System.out.println(userList.size());
/*  47:    */   }
/*  48:    */   
/*  49:    */   private void saveToXml(Document document)
/*  50:    */   {
/*  51:    */     try
/*  52:    */     {
/*  53: 67 */       Transformer transformer = TransformerFactory.newInstance().newTransformer();
/*  54:    */       
/*  55: 69 */       DOMSource source = new DOMSource(document);
/*  56: 70 */       StreamResult result = new StreamResult(new File("db.xml"));
/*  57: 71 */       transformer.setOutputProperty("encoding", "GBK");
/*  58: 72 */       transformer.transform(source, result);
/*  59:    */     }
/*  60:    */     catch (TransformerFactoryConfigurationError|TransformerException e)
/*  61:    */     {
/*  62: 76 */       e.printStackTrace();
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   public DB()
/*  67:    */   {
/*  68: 82 */     init();
/*  69:    */   }
/*  70:    */   
/*  71:    */   private void init()
/*  72:    */   {
/*  73:    */     try
/*  74:    */     {
/*  75: 89 */       this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
/*  76: 90 */       initDBFile(this.builder.newDocument());
/*  77:    */     }
/*  78:    */     catch (ParserConfigurationException e)
/*  79:    */     {
/*  80: 94 */       e.printStackTrace();
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void initDBFile(Document document)
/*  85:    */   {
/*  86:100 */     if (!new File("db.xml").exists())
/*  87:    */     {
/*  88:102 */       Element users = document.createElement("users");
/*  89:103 */       Element userNode = document.createElement("user");
/*  90:104 */userNode.setAttribute( "name", Client.ADMIN_NAME );
            userNode.setAttribute( "amount", "0" );
            userNode.setAttribute( "time", String.valueOf( Client.getTodayStart() ) );
/*  91:105 */       userNode.appendChild(document.createTextNode("0"));
/*  92:106 */       users.appendChild(userNode);
/*  93:107 */       document.appendChild(users);
/*  94:108 */       saveToXml(document);
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   public List<User> read()
/*  99:    */   {
/* 100:116 */     List<User> result = new ArrayList();
/* 101:    */     try
/* 102:    */     {
/* 103:119 */       Document document = this.builder.parse(new File("db.xml"));

/* 104:120 */       NodeList nodeList = document.getElementsByTagName("user");
/* 105:121 */       for (int i = 0; i < nodeList.getLength(); i++)
/* 106:    */       {
/* 107:123 */         Element node = (Element)nodeList.item(i);
                if( node.getAttribute( "name" ).equals( Client.ADMIN_NAME ) )
                {
                    Client.amount = Integer.parseInt( convertStringEmpty( node.getAttribute( "amount" ) ) );
                    Client.currentDateMilliTime = Long.parseLong( convertStringEmpty( node.getAttribute( "time" ) ) );
                }
/* 108:124 */         User user = 
/* 109:125 */           new User(0, node.getAttribute("name"), Double.parseDouble(node.getFirstChild().getNodeValue()));
/* 110:126 */         result.add(user);
/* 111:    */       }
/* 112:    */     }
/* 113:    */     catch (SAXException|IOException e)
/* 114:    */     {
/* 115:131 */       e.printStackTrace();
/* 116:    */     }
/* 117:133 */     return result;
/* 118:    */   }

    private String convertStringEmpty( String str )
    {
        if( str == null || str.equals( "" ) )
        {
            return "0";
        }
        return str;
    }
/* 119:    */   
/* 120:    */   public void update(User user)
/* 121:    */   {
/* 122:    */     try
/* 123:    */     {
/* 124:140 */       File dbFile = new File("db.xml");
/* 125:141 */       dbFile.exists();
/* 126:    */       
/* 127:    */ 
/* 128:    */ 
/* 129:145 */       Document document = this.builder.parse(dbFile);
/* 130:146 */       NodeList nodeList = document.getElementsByTagName("user");
/* 131:147 */       for (int i = 0; i < nodeList.getLength(); i++)
/* 132:    */       {
/* 133:149 */         Element node = (Element)nodeList.item(i);
/* 134:150 */         if (user.getUsername().equals(node.getAttribute("name"))) {
/* 135:152 */           node.getFirstChild().setNodeValue(String.valueOf(user.getRemaining()));
/* 136:    */         }
/* 137:    */       }
/* 138:155 */       saveToXml(document);
/* 139:    */     }
/* 140:    */     catch (SAXException|IOException e)
/* 141:    */     {
/* 142:159 */       e.printStackTrace();
/* 143:    */     }
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void update(List<User> userList)
/* 147:    */   {
/* 148:    */     try
/* 149:    */     {
/* 150:167 */       Document document = this.builder.parse(new File("db.xml"));
/* 151:168 */       NodeList nodeList = document.getElementsByTagName("user");
            for( int i = 0; i < nodeList.getLength(); i++ )
            {
                Element node = ( Element ) nodeList.item( i );
                if( Client.adminUser.getUsername().equals( node.getAttribute( "name" ) ) )
                {
                    node.setAttribute( "amount", String.valueOf( Client.amount ) );
                    node.setAttribute( "time", String.valueOf( Client.getTodayStart() ) );
                    node.getFirstChild().setNodeValue( String.valueOf( Client.adminUser.getRemaining() ) );
                    continue;
                }
                else
                {
                    for( User user : userList )
                    {
                        if( user.getUsername().equals( node.getAttribute( "name" ) ) )
                        {
                            node.getFirstChild().setNodeValue( String.valueOf( user.getRemaining() ) );
                        }
                    }
                }
            }
///* 152:    */       int i=0;
///* 153:169 */for( Iterator localIterator = userList.iterator(); localIterator.hasNext(); i++ )
///* 154:    */       {
///* 155:169 */         User user = (User)localIterator.next();
///* 156:    */         
///* 158:    */         
///* 159:173 */  Element node = ( Element ) nodeList.item( i );
///* 160:174 */         if (user.getUsername().equals(node.getAttribute("name"))) {
///* 161:176 */           node.getFirstChild().setNodeValue(String.valueOf(user.getRemaining()));
///* 162:    */         }
///* 164:    */       }
/* 165:180 */       saveToXml(document);
/* 166:    */     }
/* 167:    */     catch (SAXException|IOException e)
/* 168:    */     {
/* 169:184 */       e.printStackTrace();
/* 170:    */     }
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void delete(User user)
/* 174:    */   {
/* 175:    */     try
/* 176:    */     {
/* 177:192 */       Document document = this.builder.parse(new File("db.xml"));
/* 178:193 */       NodeList nodeList = document.getElementsByTagName("user");
/* 179:194 */       for (int i = 0; i < nodeList.getLength(); i++)
/* 180:    */       {
/* 181:196 */         Element node = (Element)nodeList.item(i);
/* 182:197 */         if (user.getUsername().equals(node.getAttribute("name")))
/* 183:    */         {
/* 184:199 */           node.getParentNode().removeChild(node);
/* 185:200 */           saveToXml(document);
/* 186:201 */           return;
/* 187:    */         }
/* 188:    */       }
/* 189:204 */       JOptionPane.showMessageDialog(null, "用户没找到");
/* 190:    */     }
/* 191:    */     catch (SAXException|IOException e)
/* 192:    */     {
/* 193:208 */       e.printStackTrace();
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void insert(User user)
/* 198:    */   {
/* 199:    */     try
/* 200:    */     {
/* 201:216 */       File dbFile = new File("db.xml");
/* 202:217 */       if (dbFile.exists())
/* 203:    */       {
/* 204:219 */         Document document = this.builder.parse(dbFile);
/* 205:220 */         NodeList nodeList = document.getElementsByTagName("users");
/* 206:221 */         Element userElement = (Element)nodeList.item(0);
/* 207:222 */         Element userNode = document.createElement("user");
/* 208:223 */         userNode.setAttribute("name", user.getUsername());
/* 209:224 */         userNode.appendChild(document.createTextNode(String.valueOf(user.getRemaining())));
/* 210:225 */         userElement.appendChild(document.adoptNode(userNode));
/* 211:226 */         saveToXml(document);
/* 212:    */       }
/* 213:    */       else
/* 214:    */       {
/* 215:230 */         handledbFileNotFound();
/* 216:    */       }
/* 217:    */     }
/* 218:    */     catch (SAXException|IOException e)
/* 219:    */     {
/* 220:235 */       e.printStackTrace();
/* 221:    */     }
/* 222:    */   }
/* 223:    */   
/* 224:    */   private void handledbFileNotFound()
/* 225:    */   {
/* 226:241 */     JOptionPane.showMessageDialog(null, "数据文件丢失!!!");
/* 227:242 */     System.exit(0);
/* 228:    */   }

    public void saveAround()
    {
        File roundFile = new File( "data" );
        if( !roundFile.exists() )
        {
//        roundFile.
        }
    }
/* 229:    */ }
