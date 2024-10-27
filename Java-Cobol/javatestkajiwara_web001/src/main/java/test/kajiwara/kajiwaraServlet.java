package test.kajiwara;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.processJCL;

/**
 * Servlet implementation class kajiwaraServlet
 */
@WebServlet("/kajiwaraServlet")
public class kajiwaraServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public kajiwaraServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Get the selected value of the radio button
        String selectedOption = request.getParameter("option");
        		       
		try {			
			/**
			 * Java program for SSH connection
			 * @param _controllerFile : PowerShell for kicking off the process
			 * @param _targetFilePath : File path of PowerShell for JCL kick-off
			 * @param _targetFile : PowerShell file for JCL kick-off
			 * @param _isAsync : Asynchronous processing?
			 */
			//processJCL.processSSH("D:\\WA_FTP\\_wk\\execJCLProcess.ps1","D:\\WA_FTP\\_wk\\","execSample1.ps1",false);      

			
	        // 選択された値によって処理を分岐
	        if (selectedOption != null) {
	            switch (selectedOption) {
	                case "1":
	                    // Sync(JCL Success)
	                	response.getWriter().append("Return_Code : " + processJCL.processSSH("D:\\_wk\\execJCLProcess.ps1","D:\\_wk\\","execSample1.ps1",false));
	        			break;
	                case "2":
	                    // Sync(JCL Error)
	                	response.getWriter().append("Return_Code : " + processJCL.processSSH("D:\\_wk\\execJCLProcess.ps1","D:\\_wk\\","execSample2.ps1",false));
	                    break;
	                case "3":
	                    // Async(JCL Success)
	                	response.getWriter().append("Return_Code : " + processJCL.processSSH("D:\\_wk\\execJCLProcess.ps1","D:\\_wk\\","execSample1.ps1",true));
	                    break;
	                case "4":
	                    // Async(JCL Error)
	                	response.getWriter().append("Return_Code : " + processJCL.processSSH("D:\\_wk\\execJCLProcess.ps1","D:\\_wk\\","execSample2.ps1",true));
	                    break;
	                default:
	                    response.getWriter().append("Invalid option selected");
	                    break;
	            }
	        } else {
	        	// When no radio button is selected
	            response.getWriter().append("No option selected");
	        }
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().append("Erorr : " + e.getMessage());
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
