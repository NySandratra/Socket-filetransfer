package server;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.util.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Server{
	static ArrayList<MyFile>  myfiles = new ArrayList<>(); 
	public static String getFileExtension(String filename){
		int i = filename.lastIndexOf(".");
		
		if(i>0){
			return filename.substring(i+1);
		}
		else{
			return "no extension found";
		}
	}
	public static MouseListener getMouseListener(){
		return new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e){
				JPanel panel = (JPanel)e.getSource();
				int fileId = Integer.parseInt(panel.getName());
		
				for(MyFile myfile : myfiles){
					if(myfile.getId() == fileId){
						JFrame frame = createFrame(myfile.getName(),myfile.getDate(),myfile.getFileExtension()); 
						frame.setVisible(true);
					}
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e){}
			@Override
			public void mouseReleased(MouseEvent e){}
			@Override
			public void mouseEntered(MouseEvent e){}
			@Override
			public void mouseExited(MouseEvent e){}
		};
	}
	
	

	public static JFrame createFrame(String filename,byte[] fileData,String fileextension){
		JFrame frame= new JFrame("fichier download");
		frame.setSize(500,500);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		
		JLabel titre = new JLabel();
		titre.setAlignmentX(Component.CENTER_ALIGNMENT);
		titre.setFont(new Font("Arial",Font.BOLD,20));
		titre.setBorder(new EmptyBorder(20,0,50,0));
		
		JLabel label = new JLabel("Etes vous sur de vouloir telecharger " + filename);
		label.setFont(new Font("Arial", Font.BOLD,20));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton button = new JButton("oui");
		button.setPreferredSize(new Dimension(150,75));
		button.setFont(new Font("Arial", Font.BOLD,20));
		JButton button1 = new JButton("non");
		button1.setPreferredSize(new Dimension(150,75));
		button1.setFont(new Font("Arial", Font.BOLD,20));
		
		JLabel filecontent = new JLabel();
		filecontent.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel misybutton = new JPanel();
		misybutton.setBorder(new EmptyBorder(20,0,10,0));
		misybutton.add(button);
		misybutton.add(button1);
		
		if(fileextension.equalsIgnoreCase("txt")){
			filecontent.setText("<html>" +new String(fileData) + "</html>");
		}else{
			filecontent.setIcon(new ImageIcon(fileData));
		}
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				File download = new File(filename);
				try{
					FileOutputStream out = new FileOutputStream(download);
					
					out.write(fileData);
					out.close();
					
					frame.dispose();
				}
				catch(IOException ex){
					ex.printStackTrace();
				}
			} 
		});
		button1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				frame.dispose();
			} 
		});
		panel.add(titre);
		panel.add(label);
		panel.add(filecontent);
		panel.add(misybutton);
		
		frame.add(panel);
		
		return frame;
	}
public static void main(String[] args) throws IOException{
		int fileId = 0;
		
		JFrame frame = new JFrame("server");
		frame.setSize(400,400);
		frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		
		JScrollPane scroll = new JScrollPane(panel);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JLabel titre = new JLabel("Fichier(s) recu(s)");
		titre.setFont(new Font("Arial",Font.BOLD,20));
		titre.setBorder(new EmptyBorder(50,0,0,0));
		titre.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		frame.add(titre);
		frame.add(scroll);
		frame.setVisible(true);
		
		ServerSocket server= new ServerSocket(4200);
			while(true){
				try{
					Socket socket = server.accept();
					DataInputStream input = new DataInputStream(socket.getInputStream());
					
					int fileNameLength = input.readInt();
					
					if(fileNameLength>0){
						byte[] fileNameBytes = new byte[fileNameLength];
						input.readFully(fileNameBytes,0,fileNameBytes.length);
						String filename = new String(fileNameBytes);
						
						int fileContentLength = input.readInt();
						if(fileContentLength>0){
							byte[] fileContentBytes = new byte[fileContentLength];
							input.readFully(fileContentBytes,0,fileContentLength);
							
							JPanel panel1 = new JPanel();
							panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
							
							JLabel nomfichier = new JLabel(filename);
							nomfichier.setFont(new Font("Arial",Font.BOLD,20));
							nomfichier.setBorder(new EmptyBorder(10,0,0,0));
							if(getFileExtension(filename).equalsIgnoreCase("txt")){
								panel1.setName(String.valueOf(fileId));
								panel1.addMouseListener(getMouseListener());
								
								panel1.add(nomfichier);
								panel.add(panel1);
								frame.validate();
							}
							else{
								panel1.setName(String.valueOf(fileId));
								panel1.addMouseListener(getMouseListener());
								
								panel1.add(nomfichier);
								panel.add(panel1);
								frame.validate();
							}
							myfiles.add(new MyFile(fileId,filename,fileContentBytes,getFileExtension(filename)));
						}
						
					}
				}
				catch(IOException ex){
					ex.printStackTrace();
				}
			}
	}
}