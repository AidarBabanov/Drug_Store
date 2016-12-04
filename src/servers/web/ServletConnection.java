package servers.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import servers.database.DbConnection;

/**
 * Servlet implementation class ServletConnection
 */
@WebServlet("/ServletConnection")
public class ServletConnection extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DbConnection db;
	private HttpSession session;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletConnection() {
		super();
		db = new DbConnection();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		Map<String, String[]> m = request.getParameterMap();
		Gson gson = new Gson();
		session = request.getSession();
		System.out.println("Servlet connected.");

		if (m.containsKey("signup[]")) {
			System.out.println("Sign Up");
			String[] user = m.get("signup[]");
			String result;
			System.out.println(user[4]);
			try {
				if (user[0].equals("client")) {
					db.SignUpClient(user[1], user[2], user[3]);
				}
				if (user[0].equals("creator")) {
					db.SignUpCreator(user[1], user[2], user[3], user[4]);
				}
				result = "Signed Up Succesfully!";
			} catch (Exception e) {
				result = "Signed Up not Succesfully!";

			}
			String json = gson.toJson(result);
			response.getWriter().append(json);
		}

		if (m.containsKey("signin[]")) {
			System.out.println("Sign In");
			String[] user = m.get("signin[]");
			String result;
			try {
				boolean isSignedIn = db.SignIn(user[0], user[1], user[2]);
				if (isSignedIn) {
					result = "ok";
					session.setAttribute("login"+user[0], user[1]);
				} else
					result = "not ok";

			} catch (SQLException e) {
				result = "not ok";
			}
			String json = gson.toJson(result);
			response.getWriter().append(json);
		}

		if (m.containsKey("getListOfDrugs")) {
			System.out.println("Get List of Drugs");
			List<JsonObject> result = null;
			try {
				result = db.getListOfDrugs();
			} catch (SQLException e) {
				response.getWriter().append(new Gson().toJson("error"));
			}
			response.getWriter().append(new Gson().toJson(result));
		}

		if (m.containsKey("openClientDrugSession")) {
			System.out.println("Open Client Drug Session.");
			session.setAttribute("buy", m.get("openClientDrugSession")[0]);
			System.out.println((String) session.getAttribute("buy"));
			response.getWriter().append(new Gson().toJson("openClientDrugSession"));
		}
		if (m.containsKey("getClientDrugSession")) {
			System.out.println("Get Client Drug Session.");
			List<String> result = null;
			try {
				result = db.getDrugType((String) session.getAttribute("buy"));
				result.add((String) session.getAttribute("loginclient"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.getWriter().append(new Gson().toJson(result));
		}
		if (m.containsKey("closeClientDrugSession")) {
			System.out.println("Close Client Drug Session.");
			session.removeAttribute("buy");
			response.getWriter().append(new Gson().toJson("Closed"));
		}

		if (m.containsKey("getLoginSession")) {
			response.getWriter().append(new Gson().toJson(session.getAttribute("loginclient")));
		}
		
		if (m.containsKey("logoutclient")) {
			session.removeAttribute("loginclient");
		}
		if (m.containsKey("logoutcreator")) {
			session.removeAttribute("logintcreator");
		}
		
		if (m.containsKey("addBatch[]")) {
			String[] batch = m.get("addBatch[]");
			try {
				db.addBatch(batch[0], batch[1], batch[2], batch[3], batch[4], batch[5]);
			} catch (Exception e) {
				response.getWriter().append(new Gson().toJson("Not registered"));
			}
			response.getWriter().append(new Gson().toJson("Registered"));
		}
		
		if (m.containsKey("getListofBatchesAccept")) {
			List<JsonObject> result = null;
			try {
				result = db.getListofBatchesAccept(m.get("getListofBatchesAccept")[0]);
			} catch (SQLException e) {
				response.getWriter().append(new Gson().toJson("error"));
			}
			response.getWriter().append(new Gson().toJson(result));
		}
		
		if(m.containsKey("getCreator")){
			response.getWriter().append(new Gson().toJson(m.get("getCreator")[0]));
		}
	}

}
