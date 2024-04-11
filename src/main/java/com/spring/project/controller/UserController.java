package com.spring.project.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring.project.entities.Contact;
import com.spring.project.entities.User;
import com.spring.project.helper.Message;
import com.spring.project.repository.ContactRepository;
import com.spring.project.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	// method for adding common data to response

	@ModelAttribute
	public void addCommonData(Model m, Principal p) {
		// Getting userName
		String userName = p.getName();

		// Get the user object using username(Email)
		User user = userRepository.findByEmail(userName);
		m.addAttribute("user", user);
	}

	@GetMapping("/index")
	public String home(Model model, Principal principal) {

		model.addAttribute("title", "User Dashboard");

		return "user/user_dashboard";
	}

	// Add contact handler
	@GetMapping("/add-contact")
	public String addContact(Model model) {

		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "user/add_contact";
	}

	@PostMapping("/contact-process")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profile_Image") MultipartFile file,
			Principal principal, HttpSession session) {

		try {
			// Getting userName
			String userName = principal.getName();

			// Get the user object using username(Email)
			User user = userRepository.findByEmail(userName);

			if (file.isEmpty()) {
				// file is empty then database save default image
				System.out.println("File is empty!");
				contact.setImageUrl("contact.png");
			} else {
				// upload the file to folder and update the name to contact
				contact.setImageUrl(file.getOriginalFilename());

				File saveFile = new ClassPathResource("/static/images").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is uploaded");
			}
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepository.save(user);
			System.out.println("Added to database");

			// Success message..
			session.setAttribute("message", new Message("", "success"));

		} catch (Exception e) {
			e.printStackTrace();

		}
		return "user/add_contact";
	}

	// Show Contact Handler
	// Per page = 5[n]
	// Current Page = 0[page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {

		model.addAttribute("title", "Show Contact");

		String userName = principal.getName();
		User user = userRepository.findByEmail(userName);

		Pageable pageable = PageRequest.of(page, 5);

		Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "user/show_contacts";
	}

	// Showing specific contact details
	@GetMapping("/contact/{cId}")
	public String showContactDetail(@PathVariable("cId") int cId, Model model, Principal principal) {

		Optional<Contact> contactOptional = contactRepository.findById(cId);
		Contact contact = contactOptional.get();

		String userName = principal.getName();
		User user = this.userRepository.findByEmail(userName);

		if (user.getId() == contact.getUser().getId()) {

			model.addAttribute("contact", contact);
		}

		return "user/contact_detail";
	}

	// Delete contact handler
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") int cId, Model model, HttpSession session) {

		this.contactRepository.deleteById(cId);

		session.setAttribute("message", new Message("Contact deleted successfully", "deleted"));

		return "redirect:/user/show-contacts/0";
	}

	// Update contact handler
	@GetMapping("/update/{cId}")
	public String updateContactForm(@PathVariable("cId") int cId, Model model) {

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();

		model.addAttribute("contact", contact);
		return "user/update_contact";
	}

	// Update Contact Data Handler
	@PostMapping("/update-contact-data")
	public String updateContact(@ModelAttribute Contact contact, @RequestParam("profile_pic") MultipartFile file,
			Model model, HttpSession session, Principal principal) {
		try {
			// Fetch old contact details
			Contact oldContact = this.contactRepository.findById(contact.getCId()).get();

			// image..
			if (!file.isEmpty()) {
				
				// delete old photo
				if (!oldContact.getImageUrl().equals("contact.png")) {
					File deleteFile = new ClassPathResource("/static/images").getFile();
					File file1 = new File(deleteFile, oldContact.getImageUrl());
					file1.delete();
				}
				
				// update new photo
				File saveFile = new ClassPathResource("/static/images").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImageUrl(file.getOriginalFilename());
				
			} else {
				
				contact.setImageUrl(oldContact.getImageUrl());
			}
			User user = this.userRepository.findByEmail(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Contact is updated", "updated"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "redirect:/user/contact/" + contact.getCId();
	}
	
	// User profile handler
	@GetMapping("/user-profile")
	public String userProfile(Principal principal , Model model) {
		
		String userName = principal.getName();
		User user = this.userRepository.findByEmail(userName);
		
		model.addAttribute("user" , user);
		
		return "user/user_profile";
	}
	
	// Open settings handler
	@GetMapping("/settings")
	public String openSettings() {
		return "user/settings";
	}
	
	// Change password handler
	@PostMapping("/change-password")
	public String chnagePassword(@RequestParam("oldPassword") String oldPassword ,
								@RequestParam("newPassword") String newPassword ,
								Principal principal, HttpSession session) {
		
		User user = this.userRepository.findByEmail(principal.getName());
		
		if(this.passwordEncoder.matches(oldPassword, user.getPassword())){
			
			// Change Password..
			user.setPassword(this.passwordEncoder.encode(newPassword));
			this.userRepository.save(user);
			session.setAttribute("message", new Message("Password is Changed","changePassword"));
			return "user/user_dashboard";
		}
		else {
			System.out.println("Password not Match");
			return "user/settings";
		}
		
		
	}
}
