package org.thalemine.web.injection;

import org.thalemine.web.service.InfrastructureService;

public interface SystemServiceSetter {

	void setSystemService(InfrastructureService service) throws Exception;
}
