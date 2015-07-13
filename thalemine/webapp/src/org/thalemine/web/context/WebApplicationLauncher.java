package org.thalemine.web.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.thalemine.web.service.InfrastructureService;
import org.thalemine.web.service.Initializable;
import org.thalemine.web.service.Verifiable;
import org.thalemine.web.service.builder.ComponentManagerBuilder;
import org.thalemine.web.service.builder.ServiceManagerBuilder;
import org.thalemine.web.service.core.IComponentManager;
import org.thalemine.web.service.core.IServiceManager;
import org.thalemine.web.service.impl.InfrastructureServiceImpl;

public class WebApplicationLauncher implements Initializable, Verifiable {

	protected static final Logger log = Logger.getLogger(WebApplicationLauncher.class);
	private final static StopWatch timer = new StopWatch();
	
	private final ContextLoader contextLoader;
	
	private WebApplicationLauncher(){
		contextLoader = ContextLoader.getInstance();
	}
	
	private static class WebApplicationLauncherContextHolder {

		public static final WebApplicationLauncher INSTANCE = new WebApplicationLauncher();

	}

	public static WebApplicationLauncher getInstance() {

		return WebApplicationLauncherContextHolder.INSTANCE;
		
	}
	
	@Override
	public void validateState() throws Exception {

		
		log.info("Web Application Context validation has started...");
		
		this.contextLoader.getWebApplicationContext().validateState();	
		
		log.info("Web Application Context has been validated.");
		
	}

	@Override
	public void initialize() throws Exception {
		
		
	}

	public void initialize(ServletContext servletContext, ServletRequest request) throws Exception {
		
		Exception exception = null;
	
		
		try {	
		
		timer.reset();
		timer.start();
		
		log.info("WebApplicationLauncher: WebApplication Initialization has started...");
		
		log.info("WebApplicationLauncher: Initializing WebApplicationContext has started...");
		contextLoader.initWebApplicationContext(servletContext, request);
		log.info("WebApplicationLauncher: Initializing WebApplicationContext has completed.");
		
		log.info("WebApplicationLauncher: Initializing System Service has started...");
		InfrastructureService systemService = InfrastructureServiceImpl.getInstance();
						
		systemService.setContext(contextLoader.getWebApplicationContext());
		systemService.validateState();
		log.info("WebApplicationLauncher: Initializing System Service has completed.");
		
		
		log.info("WebApplicationLauncher: Components Initialization has started...");
		IComponentManager componentManager = new ComponentManagerBuilder().build(systemService);
		log.info("WebApplicationLauncher: Components Initialization has completed.");
		
		log.info("WebApplicationLauncher:Services Initialization has started...");
		IServiceManager serviceManager = new ServiceManagerBuilder().build(componentManager);
		log.info("WebApplicationLauncher:Services Initialization has completed.");
		
		validateState();
		
		} catch(Exception e){
			exception = e;
		}finally{
			
			timer.stop();
			
			if (exception!=null){
				log.error("Error occurred during Web Application Initialization:" + exception.getMessage());
				exception.printStackTrace();
			}else{
			
			log.info("Web Application Initialization has completed."
					+ "; Total time taken. " + timer.toString());
			}
			
		}
		
	}
}
