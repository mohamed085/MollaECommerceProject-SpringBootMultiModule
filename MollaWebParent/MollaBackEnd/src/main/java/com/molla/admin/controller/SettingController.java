package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.molla.admin.helper.SettingHelper;
import com.molla.admin.repository.CurrencyRepository;
import com.molla.admin.service.SettingService;
import com.molla.admin.util.FileUploadUtil;
import com.molla.admin.util.GeneralSettingBag;
import com.molla.common.entity.Currency;
import com.molla.common.entity.Setting;

@Controller
public class SettingController {

	private SettingService service;
	
	private CurrencyRepository currencyRepo;
	
	@Autowired
	public SettingController(SettingService service, CurrencyRepository currencyRepo) {
		super();
		this.service = service;
		this.currencyRepo = currencyRepo;
	}
	
	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listSettings = service.listAllSettings();
		List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();

		model.addAttribute("listCurrencies", listCurrencies);

		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}

		return "settings/settings";
	}
	
	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
			HttpServletRequest request, RedirectAttributes ra) throws IOException {
		GeneralSettingBag settingBag = service.getGeneralSettings();

		SettingHelper.saveSiteLogo(multipartFile, settingBag);
		SettingHelper.saveCurrencySymbol(request, settingBag,currencyRepo);

		SettingHelper.updateSettingValuesFromForm(request, settingBag.list(),service);

		ra.addFlashAttribute("messageSuccess", "General settings have been saved.");

		return "redirect:/settings";
	}

	@PostMapping("/settings/save_mail_server")
	public String saveMailServerSetttings(HttpServletRequest request, RedirectAttributes ra) {
		List<Setting> mailServerSettings = service.getMailServerSettings();
		SettingHelper.updateSettingValuesFromForm(request, mailServerSettings,service);

		ra.addFlashAttribute("messageSuccess", "Mail server settings have been saved");

		return "redirect:/settings#mailServer";
	}

	@PostMapping("/settings/save_mail_templates")
	public String saveMailTemplateSetttings(HttpServletRequest request, RedirectAttributes ra) {
		List<Setting> mailTemplateSettings = service.getMailTemplateSettings();
		SettingHelper.updateSettingValuesFromForm(request, mailTemplateSettings,service);

		ra.addFlashAttribute("messageSuccess", "Mail template settings have been saved");

		return "redirect:/settings#mailTemplates";
	}
}