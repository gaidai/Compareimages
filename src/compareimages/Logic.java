
package compareimages;

import javax.imageio.ImageIO ;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author S.Gaidai
 */
public class Logic extends Frame{
    // space between symbols/pixels at line: 1 px and more..
    int sensetivity = 3; 
    Label l1;
    Label l2;
    Panel panel;
    Panel center;
    Button btn1;
    Button btn2;  
    BufferedImage img1;
    BufferedImage img2;
    JLabel pic1 = new JLabel();
    JLabel pic2 = new JLabel();
    JLabel result ;
    Panel footer; 
    Button b ;
    Graphics2D g2;
    int h ; // height of images
    int w; // width of images
    ArrayList <Rect> rectlist;
    LinkedList <ArrayList<Pixel>> difh ;
    LinkedList <ArrayList<Pixel>> difv ;
    // Filtration of input files
    Pattern pattern = Pattern.compile(  "(.*/)*.+\\.(?i)(png|jpg|gif|bmp|jpeg)$"); 
    
    Logic(){

        panel = new Panel(); 
        center = new Panel(); 
        panel.setBackground(Color.white);        
        btn1 = new Button("Open Image 1");
        btn2 = new Button("Open Image 2");        
        b = new Button("Run");  
        footer = new Panel( new GridLayout(1,3));
        btn1.setSize(150, 150);
        showFileDialogDemo(this);
        runAction();
        l1 = new Label(); 
        l1.setText(" Choose File 1 ");
        l2 = new Label(); 
        l2.setText(" Choose File 2 ");
        panel.setLayout(new GridLayout(2,2));
        
        //Setting the layout for the Frame
        setLayout( new BorderLayout());
        
        addWindowListener(new WindowAdapter(){  
            @Override
            public void windowClosing(WindowEvent e) {  
                dispose();  
            }  
        });     
        b.setBounds(150, 150, 150, 150);  
        center.add(b);
        panel.add(btn1);
        panel.add(l1);
        panel.add(btn2);
        panel.add(l2); 
        
        int width = (int) (0.8*Toolkit.getDefaultToolkit().getScreenSize().width) ;
        int height = (int) (0.8*Toolkit.getDefaultToolkit().getScreenSize().height) ;
        setSize(width, height);
        setBackground(new Color(245,255,243));
        centreWindow(this);
        add( panel,BorderLayout.NORTH);
        add( center,BorderLayout.CENTER);        
        add(footer,BorderLayout.SOUTH);
        setVisible(true);  
    }    
    private void showFileDialogDemo(Frame frame){    
        final FileDialog fileDialog = new FileDialog(frame,"Select file");        
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {             
                try {
                    // File chooser
                    fileDialog.setVisible(true);
                    if(fileDialog.getFile()!=null && Filter(fileDialog.getFile())){
                        img1 = ImageIO.read(new File(fileDialog.getDirectory() + fileDialog.getFile()));
                        l1.setText(fileDialog.getDirectory() + fileDialog.getFile());
                        Image image = img1;
                        int h = img1.getHeight();
                        int w = img1.getWidth();
                        if(h>w){
                            image = image.getScaledInstance(500*w/h, 500, java.awt.Image.SCALE_DEFAULT); 
                        } else{
                            image = image.getScaledInstance(500, 500*h/w, java.awt.Image.SCALE_DEFAULT); 
                        } 
                        pic1.setIcon(new ImageIcon(image));
                        footer.add(pic1, BorderLayout.SOUTH);
                        pic1.updateUI();      
                    }                    
                } catch (IOException ex) {
                     System.out.println("Not correct File 1");
                }
            }           
        });              
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {             
                try {
                    fileDialog.setVisible(true);
                    if(fileDialog.getFile()!=null && Filter(fileDialog.getFile())){
                        img2 = ImageIO.read(new File(fileDialog.getDirectory() + fileDialog.getFile()));
                        l2.setText(fileDialog.getDirectory() + fileDialog.getFile());
                        Image image = img2;
                        h = img2.getHeight();
                        w = img2.getWidth();
                        if(h>w){
                            image = image.getScaledInstance(500*w/h, 500, java.awt.Image.SCALE_DEFAULT); 
                        } else{
                            image = image.getScaledInstance(500, 500*h/w, java.awt.Image.SCALE_DEFAULT); 
                        }                       
                        pic2.setIcon(new ImageIcon(image));
                        footer.add(pic2, BorderLayout.SOUTH);
                        pic2.updateUI();
                    }                    
                } catch (IOException ex) {
                     System.out.println("Not correct File 2");
                }
            }           
        });    
    }
    //Filtration with regex pattern
    public boolean Filter(String name){
        return pattern.matcher(name).matches();
    }
    // Start comaring process
    public void runAction(){
        b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(img1 == null || img2 == null){
                    JOptionPane.showMessageDialog(null, "Choose  files!");       
                }else if(img1.getHeight()!=img2.getHeight() && img1.getWidth()!= img2.getWidth()){
                    JOptionPane.showMessageDialog(null, " The images have a different size! Choose another files!");
                }else if(img1 != null && img2 != null){
                    System.out.println(".actionPerformed()");
                    compare();
                } 
            }
        });        
    }
    public void compare(){
        int pixel1;
        int pixel2;
        h = img1.getHeight();
        w = img1.getWidth();
        difh = new LinkedList();
        difv = new LinkedList();
        ArrayList<Pixel> row;
        ArrayList<Pixel> col;
        // fill the List with horizontaly sorted pixels
        for(int i=0; i < h ;i++){
            row= new ArrayList(0);
            for (int j = 0; j < w; j++) {   
                pixel1 = img1.getRGB(j, i);
                pixel2 = img2.getRGB(j, i); 
                if (comparePixel( pixel1,pixel2)){
                    row.add(new Pixel(i,j));     
                };
            }    
            difh.add(row);
        }
        // fill the List with verticaly sorted pixels
        for(int i=0; i < w ;i++){
            col= new ArrayList(0);
            for (int j = 0; j < h;  j++) {   
                pixel1 = img1.getRGB(i, j);
                pixel2 = img2.getRGB(i, j); 
                if (comparePixel( pixel1,pixel2)){
                    col.add(new Pixel(j,i));   
                };
            }    
            difv.add(col);
        }
        drawResult();
    } 
    public boolean comparePixel(int pixel1, int pixel2){
        int red1 = (pixel1 >> 16) & 0xff;
        int green1 = (pixel1 >> 8) & 0xff;
        int blue1 = (pixel1) & 0xff;
        int red2 = (pixel2 >> 16) & 0xff;
        int green2 = (pixel2 >> 8) & 0xff;
        int blue2 = (pixel2) & 0xff; 
        
        if(compareColor(red1, red2) || compareColor(green1,green2)|| compareColor(blue1,blue2)){
            return true;
        }
        if(compareColors(red1, green1,blue1, red2, green2,blue2)) {
            return true;
        }
        
        return false;
    }
    
    public boolean compareColor(int x, int y){
        return (Math.abs(x-y))>=25;
    }
    public boolean compareColors ( int r1, int g1,int b1,int r2, int g2,int b2){
        
        return (Math.abs(r1-r2) + Math.abs(g1-g2)+ Math.abs(b1-b2))>=25 ;        
    }
    
    public void centreWindow(Frame frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
    
    //create horizontal lines 
    public void horizontalSplit(){
        int rectx1 = -1;
        int recty1 = -1;
        int rectx2 = -1;
        int recty2 = -1;
        boolean flag = false; 
        rectlist = new ArrayList();
        Shape rect ;
        for(int i=0; i<difh.size(); i++){   
            if(  !difh.get(i).isEmpty()){ 
                Pixel last = difh.get(i).get(difh.get(i).size()-1);
                Pixel first = difh.get(i).get(0);  
                if(flag==false){                    
                    rectx1 = first.getX();
                    recty1 = first.getY();
                    flag = true; 
                }   
                if(rectx2 < last.getX()) {        
                    rectx2 = last.getX() ;
                }
                if(recty1 > first.getY()) {        
                    recty1 = first.getY() ;
                }
                if(recty2 < last.getY()) {        
                    recty2 = last.getY() ;
                }                
                
            }else if (recty2!=-1 && flag == true){
                    rectlist.add(new Rect(recty1, rectx1, recty2, rectx2));  
                    rectx1 = -1;
                    recty1 = -1;
                    rectx2 = -1;
                    recty2 = -1;   
                    flag = false;
                }           
       }        
        verticalSplit();
    }
    // draw to the fraphics object
    public void drawRect(Rectangle r){
        g2.draw(r);
    }
    // Split horizontal lines for a rectangles
    public void verticalSplit(){
        for(Rect r: rectlist){
            int recty1 = r.x1;
            int rectx1 = r.y1;
            int recty2 = r.x2;            
            int rectx2 = r.y2;
            int lastrecty1 = recty1;
            boolean flag = false;
            int px = sensetivity; // max space between two different parts 
            
            for (int i = recty1+1; i <recty2; i++ ){
                if (  flag == false && !isAgreeX(i,rectx1,rectx2) && px<=1){
                    drawRect( new Rectangle(lastrecty1, rectx1, i-lastrecty1,rectx2-rectx1) );
                    flag = true;
                    lastrecty1=i;
                    px = sensetivity;
                    continue;
                }
                if ( flag == false && !isAgreeX(i,rectx1,rectx2) && px>1){
                    px--;
                }
                if( flag==true && !isAgreeX(i,rectx1,rectx2)){
                    lastrecty1=i;
                }
                 if( flag==true && isAgreeX(i-1,rectx1,rectx2)){
                    flag = false;
                }
            }  
            drawRect( new Rectangle(lastrecty1, rectx1, recty2-lastrecty1,rectx2-rectx1) );
        }
    }
    // find the different pixels at horizontal stripe
    public boolean isAgreeX(int i, int xMin, int xMax ){
        for(Pixel a: difv.get(i)){
            if (a.getX()<=xMax && a.getX()>= xMin){ 
                return true;
            }                      
        }
        return false;
    }    
      // Create new Frame for Result imaging
    public void drawResult(){
        result = new JLabel() {
            @Override
            public void paintComponent(Graphics g) {
                g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(1));
                g2.drawImage(img2, null, 0, 0);
                g2.setColor(Color.red); 
                // separation of different areas 
                horizontalSplit();
            }
        };
        ResultFrame jFrameWindow = new ResultFrame();
        jFrameWindow.setVisible(true);
        centreWindow(jFrameWindow);
        jFrameWindow.setSize(400, 400);
	jFrameWindow.addWindowListener(new WindowAdapter(){  
            @Override
            public void windowClosing(WindowEvent e) {  
                jFrameWindow.dispose();  
            }  
        }); 
        jFrameWindow.setTitle("Result picture");
        jFrameWindow.add(result);
        result.updateUI();
    }
    
    // Needs for simple result frame
    public class ResultFrame extends Frame{
        
    }
}