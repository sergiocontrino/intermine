package org.thalemine.web.utils;

import java.util.ArrayList;
import java.util.List;

import org.thalemine.web.query.Service;

public class CacheService {

	private List<Service> services;

	public CacheService() {
		services = new ArrayList<Service>();
	}

	public Service getService(String serviceName) {

		for (Service service : services) {
			if (service.getClassName().equalsIgnoreCase(serviceName)) {

				return service;
			}
		}
		return null;
	}

	public void addService(Service newService) {
		boolean exists = false;

		for (Service service : services) {
			if (service.getClassName().equalsIgnoreCase(newService.getClassName())) {
				exists = true;
			}
		}
		if (!exists) {
			services.add(newService);
		}
	}
}
