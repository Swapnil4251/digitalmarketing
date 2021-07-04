/**
 * 
 */
package com.digital.web.actions;

import java.util.Set;

import com.digital.web.models.MailInterfaceModel;

/**
 * @author swapnilsarwade
 *
 */
public class MonitorThreadsExecution extends Action {

	/**  */
	private static final long serialVersionUID = 1L;

	public MonitorThreadsExecution() {
		// TODO Auto-generated constructor stub
	}
	
	public MonitorThreadsExecution(Object ctx) {
		super();
		this.setContext(ctx);
	}
	
	private MailInterfaceModel getMailInterfaceModel() {
		return (MailInterfaceModel) this.getContext();
	}
	
	/* (non-Javadoc)
	 * @see com.digital.web.actions.Action#execute(java.lang.Object)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object execute(Object params) {
		synchronized (this) {
			try {
				
				while (true) {
					Set<Thread> threads = this.getMailInterfaceModel().getThreadsMap().keySet();
					
					for (Thread thread : threads) {
						Action action = this.getMailInterfaceModel().getThreadsMap().get(thread);
						if (action.isCheckUsage()) {
							try {
								if (action.getInt("ipUsageExceeded") >= 0) {
									thread.wait(action.getInt("ipUsageExceeded"));
								} else {
									thread.interrupt();
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
					}
					
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				
			}
		}
		return null;
	}

}
