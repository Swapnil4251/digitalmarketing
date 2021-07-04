package com.digital.web.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.primefaces.model.UploadedFile;

import com.digital.web.actions.Action;
import com.digital.web.actions.MonitorThreadsExecution;
import com.digital.web.actions.SendEmailAction;
import com.digital.web.core.DynamicObject;
import com.digital.web.core.SelectionOption;
import com.digital.web.utils.AppUtil;

/**
 * @author swapnilsarwade
 *
 */
public class MailInterfaceModel extends Model {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private List<SelectionOption> contentTypes;

	private List<SelectionOption> xDelaySelections;

	private List<SelectionOption> xMailerSelections;

	private List<SelectionOption> headerTemplates;

	private UploadedFile file;

	ConcurrentHashMap<Thread, Action> threadsMap = new ConcurrentHashMap<Thread, Action>();

	ConcurrentHashMap<String, Action> actionsMap = new ConcurrentHashMap<String, Action>();
	
	public MailInterfaceModel() {
		super();
		//checkUsageLimit(this);
	}

	public MailInterfaceModel(Object ctx) {
		super();
		this.setContext(ctx);
		//checkUsageLimit(this);
	}
	
	@Override
	public String getTitle() {
		return "Mail Interface";
	}
	
	public ConcurrentHashMap<Thread, Action> getThreadsMap() {
		return threadsMap;
	}
	
	public ConcurrentHashMap<String, Action> getActionsMap() {
		return actionsMap;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings({ "unchecked" })
	public void processMailInterface() {
		try {
			System.out.println("In process mail interface..");
			System.out.println(getData());
			
			String sendEmailsIdStr = getData().getString("sendToEmails");
			
			List<String> list = this.getSplittedIds(sendEmailsIdStr);
			
			int iCount = getData().isFieldNull("iterationCount") ? -1 : getData().getInt("iterationCount");
			String domains = getData().getString("selectedIPs");
			String[] domainsArr = domains.split(",");
			int dCount = domainsArr.length;

			//List<String> list = Arrays.asList(receipients);
			
			int divisor = list.size() / dCount; // 100 / 3 = 33
			int sIndex = 0;
			int eIndex = (list.size() / dCount); // 33
			
			String[] testEmails = null;
			int testingPeriod = -1;
			
			if (!getData().isFieldNull("testEmails") && !getData().isFieldNull("testingPeriod")) {
				testEmails = getData().getString("testEmails").split(",");
				testingPeriod = getData().getInt("testingPeriod");
			}
			
			for (int i = 0; i < domainsArr.length; i++) {
				String ip = domainsArr[i];

				List<String> subList = list.size() < domainsArr.length ? list : list.subList(sIndex, eIndex); // 0-32
				
				if (subList.size() > 0) {
					
					SendEmailAction action = new SendEmailAction(this);
	
					action.put("iterationEmails", subList);
					action.put("ip", ip);
					action.put("iterationCount", iCount);
					
					if (testEmails != null && testEmails.length > 0 && testingPeriod > 0) {
						action.put("testEmails", testEmails);
						action.put("testingPeriod", testingPeriod);
					}
					
					action.put("ipUsageExceeded", getData().getBoolean("stopProcess") 
							? -1 : (getData().isFieldNull("waitInput") ? -1 : getData().getInt("waitInput")));
	
					Thread t = action.executeAsync(this, false);
					
					threadsMap.put(t, action);
				}
				
				sIndex = eIndex; // 33
				
				if (i == domainsArr.length - 2) {
					eIndex = list.size();
				} else {
					eIndex = sIndex + divisor; // 33+33-1 = 65
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public List<Thread> getThreadsKeys() {
		
		List<Thread> list = new ArrayList<Thread>();
		
		Set<Thread> keySet = this.getThreadsMap().keySet();
		
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			Thread thread = (Thread) iterator.next();
			if (thread.isAlive()) {
				list.add(thread);
			} else {
				this.getThreadsMap().remove(thread);
			}
		}
		return list;
	}
	
	@SuppressWarnings("deprecation")
	public void stopProcess(Thread th) {
		if (th != null && th.isAlive()) {
			try {
				th.stop();
				getThreadsMap().remove(th);
				getThreadsKeys().remove(th);
				System.out.println("Killing thread: " + th.getName() + " Successful.");
			} catch (Exception e) {
				System.out.println("Error killing thread: " + th.getName());
				System.out.println(e);
			} finally {
				
			}
		}
	}
	
	public void stopAllThreads() {
		try {
			List<Thread> keys = this.getThreadsKeys();
			for (Thread th : keys) {
				this.stopProcess(th);
			}
			this.getThreadsMap().clear();
			System.out.println("All threads killed successfully.");
		} catch (Exception e) {
			System.out.println("Error while stopping all threads." + e);
		}
	}
	
	/**
	 * @param str
	 * @return
	 */
	public List<String> getSplittedIds(String str) {
		String[] recipients = new String[0];
		
		recipients = str.split("\n");
		List<String> result = new ArrayList<String>();//Arrays.asList(recipients);
		
		for (String id : recipients) {
			if (!AppUtil.isNullOrEmpty(id)) {
				if (id.contains(",")) {
					result.remove(id);
					String[] ids = id.split(",");
					for (String i : ids) {
						if (!AppUtil.isNullOrEmpty(i)) result.add(i); 
					}
				} else {
					result.add(id.trim());
				}
			}
		}
		return result;
	}
	
	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void upload() {
		try {
			if (file != null) {
				System.out.println("File is uploaded");
			}

			System.out.println(file);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void checkUsageLimit(DynamicObject object) {
		try {
			MonitorThreadsExecution monitor = new MonitorThreadsExecution(this);
			monitor.executeAsync(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public List<SelectionOption> getContentTypes() {
		if (contentTypes == null) {
			contentTypes = new ArrayList<SelectionOption>();
			contentTypes.add(new SelectionOption("Text", "text/plain"));
			contentTypes.add(new SelectionOption("HTML", "text/html"));
			contentTypes.add(new SelectionOption("Multipart", "multipart"));
		}

		return contentTypes;
	}

	/**
	 * @return
	 */
	public List<SelectionOption> getXDelaySelectionOptions() {
		if (xDelaySelections == null) {
			xDelaySelections = new ArrayList<SelectionOption>();
			xDelaySelections.add(new SelectionOption("1", "1"));
			xDelaySelections.add(new SelectionOption("100000", "100000"));
			/*
			 * <option value="200000">200000</option> <option value="300000">300000</option>
			 * <option value="400000">400000</option> <option value="500000">500000</option>
			 * <option value="600000">600000</option> <option value="700000">700000</option>
			 * <option value="800000">800000</option> <option value="900000">900000</option>
			 * <option value="1000000">1000000</option> <option
			 * value="2000000">2000000</option> <option value="3000000">3000000</option>
			 * <option value="4000000">4000000</option> <option
			 * value="5000000">5000000</option> <option value="6000000">6000000</option>
			 * <option value="7000000">7000000</option> <option
			 * value="8000000">8000000</option> <option value="9000000">9000000</option>
			 * <option value="10000000">10000000</option>
			 */
		}

		return xDelaySelections;
	}

	/**
	 * @return
	 */
	public List<SelectionOption> getXMailerSelectionOptions() {
		if (xMailerSelections == null) {
			xMailerSelections = new ArrayList<SelectionOption>();
			xMailerSelections.add(new SelectionOption("AIR MAIL", "AIR MAIL for Motif (v1.5)"));
			xMailerSelections.add(new SelectionOption("AIR Mosaic", "AIR Mosaic (16-bit) version 4.00.08.32"));
			xMailerSelections.add(new SelectionOption("ELM", "ELM [version 2.4 PL25]"));
			xMailerSelections.add(new SelectionOption("Gnus", "Gnus v5.4.39/XEmacs 19.15"));
			xMailerSelections.add(new SelectionOption("Mozilla 3.01", "Mozilla 3.01Gold (X11; I; SunOS 5.5.1 i86pc)"));
			xMailerSelections.add(new SelectionOption("Mozilla 4.0b2", "Mozilla 4.0b2 (X11; I; SunOS 5.5 sun4c)"));
			xMailerSelections.add(new SelectionOption("Mozilla 4.61", "Mozilla 4.61 [en] (Win95; I)"));
			/*
			 * <option value="PHP Version 1.0">PHP Version 1.0</option> <option
			 * value="QUALCOMM Windows Eudora Version 4.3.2">QUALCOMM Windows</option>
			 * <option value="SPRY Mail Version: 04.10.06.22">SPRY Mail</option> <option
			 * value="Worldtalk (NetConnex V4.00a)/MIME">Worldtalk </option>
			 */
		}
		return xMailerSelections;
	}

	/**
	 * @return
	 */
	public List<SelectionOption> getHeaderTemplates() {
		if (headerTemplates == null) {
			headerTemplates = new ArrayList<SelectionOption>();
			headerTemplates.add(new SelectionOption("Gmail", getProperty("Gmail_Header")));
			headerTemplates.add(new SelectionOption("YahooMail", getProperty("YahooMail_Header")));
			headerTemplates.add(new SelectionOption("Aol Mail", getProperty("AolMail_Header")));
		}

		return headerTemplates;
	}
}