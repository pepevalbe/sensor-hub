package com.pepe.sensor.controller;

import com.pepe.sensor.VarKeeper;
import com.pepe.sensor.persistence.ConfigVariable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Admin operations
 */
@Slf4j
@Controller
public class AdminController {

	private static final String ADMIN_CONFIGVARS_ALL_URL = "/admin/configvars/all";
	private static final String ADMIN_CONFIGVARS_URL = "/admin/configvars";

	@Autowired
	private VarKeeper varKeeper;

	@GetMapping(ADMIN_CONFIGVARS_ALL_URL)
	public List<ConfigVariable> getConfigVars() {

		List<ConfigVariable> configVariables = new ArrayList<>();
		varKeeper.getAll().forEach((key, value) -> configVariables.add(new ConfigVariable(key, value)));

		return configVariables;
	}

	@GetMapping(ADMIN_CONFIGVARS_URL)
	public ConfigVariable getConfigVar(@RequestParam String varKey) {

		return new ConfigVariable(varKey, varKeeper.get(varKey));
	}

	@PostMapping(ADMIN_CONFIGVARS_URL)
	public void postConfigVar(@RequestBody ConfigVariable configVariable) {

		varKeeper.put(configVariable.getVarKey(), configVariable.getVarValue());
	}
}
