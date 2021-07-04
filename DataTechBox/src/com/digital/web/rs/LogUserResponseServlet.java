package com.digital.web.rs;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.digital.web.core.Application;
import com.digital.web.utils.AppUtil;

/**
 * Servlet implementation class LogUserResponseServlet
 */
//@WebServlet("/logresponse")
public class LogUserResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogUserResponseServlet() {
        super();
    }

    public JdbcTemplate getTemplate() {
    		return Application.getStaticBean("jdbcTemplate", JdbcTemplate.class);
    }
    
    public NamedParameterJdbcTemplate getNamedTemplate() {
    		return Application.getStaticBean("namedParameterJdbcTemplate", NamedParameterJdbcTemplate.class);
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			String email = request.getParameter("e");
			String action = request.getParameter("a");
			String offerId = request.getParameter("o");
			String offerUrl = request.getParameter("redirect_url");
			
			boolean isUnsubscribe = (!AppUtil.isNullOrEmpty(action)) && action.equalsIgnoreCase("unsubscribe");
			/*String tableName = "test_table";
			if (isUnsubscribe) {
				tableName = "unsubscription";
			}*/
			if (AppUtil.isNullOrEmpty(action)) {
				action = "link_open";
			}
			
			String query = "insert into test_table (id, email_id, offer_id, response_type) values "
					+ "(:id, :email, :offerId, :action)";
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("id", UUID.randomUUID().toString());
			paramMap.put("email", email);
			paramMap.put("action", action);
			paramMap.put("offerId", offerId);
			//paramMap.put("createdOn", );
			
			//getTemplate().execute(query);
			
			getNamedTemplate().execute(query, paramMap, new PreparedStatementCallback() {

				@Override
				public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
					return ps.execute();
				}
				
			});
			
			System.out.println("email updated : " + email + ", " + action);
				Map<String, String> params = new HashMap<String, String>();
				params.put("offerId", offerId);
				
				Map<String, Object> offer = getNamedTemplate().queryForMap("select * from offers where offer_id = :offerId ", params);
				
				if (offer != null) {
					if (isUnsubscribe) {
						//"/unsubscribe.xhtml";
						offerUrl = offer.get("unsubscribe_url") == null ? offerUrl : String.valueOf(offer.get("unsubscribe_url"));
					} else {
						offerUrl = offer.get("redirection_url") == null ? offerUrl : String.valueOf(offer.get("redirection_url"));
					}
				}
			
			response.sendRedirect(offerUrl);
			
		} catch (Exception e) {
			System.out.println(e);
			if (!AppUtil.isNullOrEmpty(request.getParameter("redirect_url"))) {
				response.sendRedirect(request.getParameter("redirect_url"));
			}
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
