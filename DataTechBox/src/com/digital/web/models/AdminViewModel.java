package com.digital.web.models;

import org.primefaces.event.FileUploadEvent;

import com.digital.web.utils.AppUtil;

/**
 * @author swapnilsarwade
 *
 */
public class AdminViewModel extends Model {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public AdminViewModel() {
		super();
	}
	
	Model activeModel;

	public Model getActiveModel() {
		if (activeModel == null) {
			this.activeModel = loadModel();
		}
		return activeModel;
	}

	public void setActiveModel(Model activeModel) {
		this.activeModel = activeModel;
	}

	private Model loadModel() {
		
		String viewPath = getViewId();
		
		System.out.println("View Path : " + viewPath);
		
		Model m = null;
		
		if (!AppUtil.isNullOrEmpty(viewPath)) {
			if (viewPath.contains("index.xhtml") || viewPath.contains("mailinterface")) {
				m = new MailInterfaceModel();
			} else if (viewPath.contains("offers")) {
				m = new OffersModel();
			}
		}
		
		if (m == null) {
			m = new MailInterfaceModel();
		}
		
		m.setContext(this);
		
		return m;
	}

	public void upload(FileUploadEvent event) {
		try {
				System.out.println("File is uploaded");
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	public void processMailInterface() {
		((MailInterfaceModel) this.getActiveModel()).processMailInterface();
	}
	
	public void saveOffer() {
		((OffersModel) this.getActiveModel()).saveOffer();
	}
}
