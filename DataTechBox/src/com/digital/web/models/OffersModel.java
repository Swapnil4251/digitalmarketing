/**
 * 
 */
package com.digital.web.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.digital.web.core.Application;
import com.digital.web.utils.AppUtil;

/**
 * @author swapnilsarwade
 *
 */
public class OffersModel extends Model {

	/**  */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public OffersModel() {
		super();
	}
	
	/**
	 * @param ctx
	 */
	public OffersModel(Object ctx) {
		super();
		this.setContext(ctx);
	}
	
	@Override
	public String getTitle() {
		return "Offers";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void saveOffer() {
		try {
			String offerId = this.getData().getString("offerId");
			String offerName = this.getData().getString("offerName");
			String offerUrl = this.getData().getString("offerUrl");
			String unsUrl = this.getData().getString("unsubscribeUrl");
			//String offerDesc = this.getData().getString("offerDescription");
			if (AppUtil.isNullOrEmpty(offerId) || AppUtil.isNullOrEmpty(offerName) 
					|| AppUtil.isNullOrEmpty(offerUrl) || AppUtil.isNullOrEmpty(unsUrl)) {
				return;
			}
			
			String sql = "insert into offers "
					+ "(offer_id, offer_name, redirection_url, unsubscribe_url, offer_desc) values "
					+ "(:offerId, :offerName, :offerUrl, :unsubscribeUrl, :offerDescription)";
			
			//getDataSource().update(sql);
			
			NamedParameterJdbcTemplate jdbcTemplate = Application.getStaticBean("namedParameterJdbcTemplate", NamedParameterJdbcTemplate.class);
			jdbcTemplate.execute(sql, getData(), new PreparedStatementCallback() {

				@Override
				public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
					return ps.execute();
				}
				
			});
			
			System.out.println("Offer record is inserted.." + offerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
