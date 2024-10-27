package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class processJCL {

	/**
	 * Java program for SSH connection
	 * @param _controllerFile : PowerShell for kicking off the process
	 * @param _targetFilePath : File path of PowerShell for JCL kick-off
	 * @param _targetFile : PowerShell file for JCL kick-off
	 * @param _isAsync : Asynchronous processing?
	 */
	public static int processSSH(String _controllerFile,String _targetFilePath,String _targetFile,Boolean _isAsyc) {
		
        Properties properties = new Properties();
        
        String controllerFile = _controllerFile;        
        String targetFilePath = _targetFilePath;
        String targetFile = _targetFile;
        String isAsyc = _isAsyc ? "1" : "0"; // 0:False.1:True
        int retrunCode = 0;
        
        try (InputStream input = processJCL.class.getClassLoader().getResourceAsStream("sshConfig.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Property file not found in the classpath");
            }
            properties.load(input);
        
			String user = properties.getProperty("user");
	        String host = properties.getProperty("host");
	        int port = 22; 
	        String password = properties.getProperty("password");
		
	        Session session = null;
	        ChannelExec channel = null;
	        
	        
	        try {
	            JSch jsch = new JSch();
	            session = jsch.getSession(user, host, port);
	            session.setPassword(password);
	
	            // Ignore warnings setting
	            session.setConfig("StrictHostKeyChecking", "no");
	
	            // Connect the session
	            session.connect();
	            
	            // PowerShell command to execute
	            String command = "powershell.exe -ExecutionPolicy Bypass -File \""+controllerFile+"\" \""+targetFilePath+targetFile+"\" \""+isAsyc+"\"";
	
	            channel = (ChannelExec) session.openChannel("exec");
	            channel.setCommand(command);
	            channel.setErrStream(System.err); 
	
	            // Execute the command
	            InputStream in = channel.getInputStream();
	            channel.connect();
	            
	            // Read the command output
	            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                System.out.println(line);
	            }
	            
	            // Wait until the command completes
				while (!channel.isClosed()) {
				    try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}  
				}
	            
	        } catch (JSchException e) {
	            e.printStackTrace();
	            retrunCode = -1;
	        } catch (Exception e) {
	            e.printStackTrace();
	            retrunCode = -1;
	        }finally {            
	        	// disonnect the session
				if(channel != null) {channel.disconnect();}
				if(session != null) {session.disconnect();}
								
				// Set the return code from the call destination
				if(retrunCode == 0) {
					retrunCode = channel.getExitStatus(); 				
				}			
	        }        
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retrunCode;
	} 

}
