package client;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;

public class Client{
	
	public static void main(String[] args){
	final File[] filesend = new File[1];
		
		JFrame frame = new JFrame("client");
		frame.setSize(500,500);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel label = new JLabel("client");
		label.setFont(new Font("Arial",Font.BOLD,20));
		label.setBorder(new EmptyBorder(50,0,0,0));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel label1 = new JLabel("Choisir un fichier");
		label1.setFont(new Font("Arial",Font.BOLD,20));
		label1.setBorder(new EmptyBorder(50,0,0,0));
		label1.setAlignmentX(Component.CENTER_ALIGNMENT);
	
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(75,0,10,0));
		
		JButton send = new JButton("envoyer");
		send.setPreferredSize(new Dimension(150,75));
		send.setFont(new Font("Arial",Font.BOLD,20));
		
		JButton select = new JButton("choisir");
		select.setPreferredSize(new Dimension(150,75));
		select.setFont(new Font("Arial",Font.BOLD,20));
		
		panel.add(send);
		panel.add(select);
		
		select.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Choisir un fichier a envoyer");
				
				if(chooser.showOpenDialog(null)==chooser.APPROVE_OPTION){
					filesend[0] = chooser.getSelectedFile();
					label.setText("Le fichier que vosu voulez envoyer est : " + filesend[0].getName());
				}
			} 
		});
		send.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if(filesend[0] == null){
					label.setText("Veuiller choisir un fichier");
				}else{
					try{
						FileInputStream input = new FileInputStream(filesend[0].getAbsolutePath());
						Socket client = new Socket("localhost",4200);
						
						DataOutputStream data = new DataOutputStream(client.getOutputStream());
						
						String nomfile = filesend[0].getName();
						byte[] filebytes = nomfile.getBytes();
						
						byte[] file = new byte[(int)filesend[0].length()];
						input.read(file);
						
						data.writeInt(filebytes.length);
						data.write(filebytes);
						
						data.writeInt(file.length);
						data.write(file);
					}
					catch(IOException ex){
						ex.printStackTrace();
					}
				}
			} 
		});
		frame.add(label);
		frame.add(label1);
		frame.add(panel);
		frame.setVisible(true);
		
	}
}