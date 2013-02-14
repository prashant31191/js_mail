package org.dms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.cc.SimpleFolder;
import org.cc.SimpleMessage;
import org.mindrot.jbcrypt.BCrypt;


/**
 * Class, containing methods for dealing with BD through entities
 * 
 * @author Fomin
 * @version 1.0
 */
public class Managing {
	/**
	 * Creates a user in DB
	 * 
	 * @param data Array of Strings, containing a registration information
	 * @return String, containing the signed up user's email-address in case 
	 * the user was created, otherwise empty String
	 */
	public static String creatUser(String[] data) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("mail");
        EntityManager em = emf.createEntityManager();
        EntityTransaction trx = em.getTransaction();
		String newAddress = null;
		try {
			Email checkEmail = em.find(Email.class, data[0] + "@mail.js");
			if (checkEmail != null)
				return "";
			
			trx.begin();

			User user = new User();
			user.setFirstName(data[3]);
			user.setLastName(data[4]);
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			format.setLenient(false);
			try {
				user.setBirthDate(format.parse(data[5].trim()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			user.setPhone(data[6]);
			em.persist(user);

			Email email = new Email();
			newAddress = data[0] + "@mail.js";
			email.setEaddress(newAddress);
			String hashedPass = BCrypt.hashpw(data[1], BCrypt.gensalt(12));
			email.setPassword(hashedPass);
			email.setUser(user);
			email.setCreationDate(new Date(System.currentTimeMillis()));
			em.persist(email);
			
			Folder input = new Folder();
			input.setName("Входящие");
			input.setUndel(true);
			input.setWhose(email);
			em.persist(input);
			
			Folder output = new Folder();
			output.setName("Исходящие");
			output.setUndel(true);
			output.setWhose(email);
			em.persist(output);
			
			Email noreply = em.find(Email.class, "noreply@mail.js");
			
			Message message = new Message();
			message.setAbout("Добрый день");
			message.setEmail(noreply);
			message.setSentDate(new Date(System.currentTimeMillis()));
			message.setText("Служба mail.js приветсвует Вас!");
			em.persist(message);
			
			MessToFold mf = new MessToFold();
			mf.setFolder(input);
			mf.setMessage(message);
			em.persist(mf);
			
			MessToMail mm = new MessToMail();
			mm.setMessage(message);
			mm.setTo(email);
			mm.setRead(false);
			em.persist(mm);
			
			trx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (trx.isActive())
				trx.rollback();
			emf.close();
		}
		return newAddress;
	}

	/**
	 * Checks if such a user exists in DB
	 * 
	 * @param mailAddress email-address
	 * @param pass Password
	 * @return true, in case such a user exists, otherwise false
	 */
	public static boolean checkUser(String mailAddress, String pass) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("mail");
        EntityManager em = emf.createEntityManager();

		try {
			
			Email mail = em.find(Email.class, mailAddress);

			if (mail == null)
				return false;
			
			if (BCrypt.checkpw(pass, mail.getPassword()))
				return true;
			else 
				return false;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
		return false;
	}
	
	public static List<SimpleFolder> getEverything(String email) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("mail");
        EntityManager em = emf.createEntityManager();
        
        try { 
        	Email mail = em.find(Email.class, email);
        	
        	User user = mail.getUser();
        
        	List<Folder> dbFolders = mail.getFolders();
        	List<SimpleFolder> sFolders = new ArrayList<SimpleFolder>();
        	for(Folder folder: dbFolders) {
        		SimpleFolder sFolder = new SimpleFolder();
        		sFolder.setName(folder.getName());
        		List<MessToFold> messToFold = folder.getMessToFolds();
        		List<SimpleMessage> simpleMessages = new ArrayList<SimpleMessage>();
        		for(MessToFold mf: messToFold) {
        			Message message = mf.getMessage();
        			SimpleMessage sm = new SimpleMessage();
        			
        			sm.setDate(message.getSentDate());
        			sm.setAbout(message.getAbout());
        			sm.setText(message.getText());
        			if (message.getEmail().getEaddress().equals(email)) {
        				sm.setFrom("Меня");
        				List<MessToMail> messToMail = message.getMessToMails();
        				StringBuilder sb = new StringBuilder();
        				for (MessToMail mm: messToMail)
        					sb.append(mm.getTo() + "; ");
        				sm.setTo(sb.toString());
        				sm.setRead(true);
        			} else {
        				sm.setFrom(message.getEmail().getEaddress());
        				sm.setTo("Мне");
        				List<MessToMail> messToMail = message.getMessToMails();
        				for (MessToMail mm: messToMail)
        					if (mm.getTo().equals(email))
        						sm.setRead(mm.getRead());
        			}
        			simpleMessages.add(sm);	
        		}
        		sFolder.setMessages(simpleMessages);
        		sFolders.add(sFolder);
        	}
        	return sFolders;
        } catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			emf.close();
		}
	}
}
