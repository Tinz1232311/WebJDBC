package DBServlet;

import jakarta.servlet.ServletException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import TestConnection.DataConnect;

/**
 * Servlet implementation class DBServlet
 */
public class DBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DBServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		String action = request.getParameter("action");
		if("Login".equals(action))
		{
			String username = request.getParameter("Name");
			String password = request.getParameter("Pass");
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;

			try {
			    conn = DataConnect.getConnection();
			    
			    if (conn != null) {
			        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
			        stmt = conn.prepareStatement(sql);
			        stmt.setString(1, username);
			        stmt.setString(2, password);

			        rs = stmt.executeQuery();

			        if (rs.next()) {
			        	getServletContext().getRequestDispatcher("/FormNew.jsp").forward(request,response);
			        } else {
			            response.getWriter().append("Invalid username or password");
			        }
			    } else {
			        response.getWriter().append("Failed to connect to the database.");
			    }
			} catch (ClassNotFoundException | SQLException e) {
			    response.getWriter().append("Connection error: ").append(e.getMessage());
			} finally {
			    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
			    if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
			    DataConnect.closeConnection(conn);
			}
		}
		else
		{
			getServletContext().getRequestDispatcher("/Register.jsp").forward(request,response);
			String regusername = request.getParameter("RegisName");
			String reguserpass = request.getParameter("RegisPass");

	        Connection conn = null;
	        PreparedStatement stmt = null;

	        try {
	             conn = DataConnect.getConnection(); 
	                
	             String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
	                
	             stmt = conn.prepareStatement(sql);
	             stmt.setString(1, regusername);
	             stmt.setString(2, reguserpass); 
	                
	             int rowsInserted = stmt.executeUpdate();
	                
	             if (rowsInserted > 0) {
	                 response.getWriter().append("Account registered successfully!");
	             } else {
	                 response.getWriter().append("Failed to register account.");
	             }

	             } catch (ClassNotFoundException | SQLException e) {
	                response.getWriter().append("Database error: ").append(e.getMessage());
	            } finally {
	                if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
	                if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
	            }
	        }
		}
	}
