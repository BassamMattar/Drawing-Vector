package controller;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class MyCellRenderer extends JButton implements ListCellRenderer {  
     public MyCellRenderer() {  
         setOpaque(true); 

     }
     boolean b=false;
    @Override
    public void setBackground(Color bg) {
        // TODO Auto-generated method stub
         if(!b)
         {
             return;
         }

        super.setBackground(bg);
    }
     public java.awt.Component getListCellRendererComponent(  
         JList list,  
         Object value,  
         int index,  

         boolean isSelected,  
         boolean cellHasFocus)  
     {  

         b=true;
         setText(" ");           
         setBackground((Color)value);        
         b=false;
         return this;  
     }  
}