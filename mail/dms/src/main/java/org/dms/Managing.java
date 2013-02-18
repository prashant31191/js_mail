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
	static SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	/**
	 * Creates a user in DB
	 * 
	 * @param data
	 *            Array of Strings, containing a registration information
	 * @return String, containing the signed up user's email-address in case the
	 *         user was created, otherwise empty String
	 */
	public static String creatUser(String[] data) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
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
			if (trx.isActive())
				trx.rollback();
			return "";
		} finally {
			emf.close();
		}
		return newAddress;
	}

	/**
	 * Checks if such a user exists in DB
	 * 
	 * @param mailAddress
	 *            email-address
	 * @param pass
	 *            Password
	 * @return true, in case such a user exists, otherwise false
	 */
	public static boolean checkUser(String mailAddress, String pass) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
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
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();

		try {
			Email mail = em.find(Email.class, email);

			User user = mail.getUser();

			List<Folder> dbFolders = mail.getFolders();
			List<SimpleFolder> sFolders = new ArrayList<SimpleFolder>();
			for (Folder folder : dbFolders) {
				SimpleFolder sFolder = new SimpleFolder();
				sFolder.setName(folder.getName());
				List<MessToFold> messToFold = folder.getMessToFolds();
				List<SimpleMessage> simpleMessages = new ArrayList<SimpleMessage>();
				for (MessToFold mf : messToFold) {
					Message message = mf.getMessage();
					SimpleMessage sm = new SimpleMessage();

					sm.setDate(message.getSentDate());
					sm.setAbout(message.getAbout());
					sm.setText(message.getText());
					if (message.getEmail().getEaddress().equals(email)) {
						sm.setFrom("Меня");
						List<MessToMail> messToMail = message.getMessToMails();
						StringBuilder sb = new StringBuilder();
						for (MessToMail mm : messToMail)
							sb.append(mm.getTo().getEaddress() + "; ");
						sm.setTo(sb.toString());
						sm.setRead(true);
					} else {
						sm.setFrom(message.getEmail().getEaddress());
						sm.setTo("Мне");
						List<MessToMail> messToMail = message.getMessToMails();
						for (MessToMail mm : messToMail)
							if (mm.getTo().getEaddress().equals(email)) 
								sm.setRead(mm.getRead());
					}
					simpleMessages.add(0, sm);
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

	public static Object[] sendMessage(String[] data, String sender) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		boolean thereIsGoodGetter = false;
		try {
			String[] emails = data[0].trim().split(";");
			
			trx.begin();
			
			Email noreply = em.find(Email.class, "noreply@mail.js");
			Email senderEmail = em.find(Email.class, sender);
			List<Folder> senderFolders = senderEmail.getFolders();
			Folder senderInput = null;
			Folder senderOutput = null;
			for (Folder folder: senderFolders) {
				if (folder.getName().equals("Входящие"))
					senderInput = folder;
				if (folder.getName().equals("Исходящие"))
					senderOutput = folder;
			}
			
			Message newMessage = new Message();
			newMessage.setEmail(senderEmail);
			newMessage.setAbout(data[1]);
			newMessage.setText(data[2]);
			Date messDate = new Date(System.currentTimeMillis());
			newMessage.setSentDate(messDate);
			
			StringBuilder getters = new StringBuilder();
			SimpleMessage outcomeMess = new SimpleMessage();
			List<SimpleMessage> incomeMessages = new ArrayList<SimpleMessage>();
			
			for (String tmp: emails) {
				Email emailTo = em.find(Email.class, tmp.trim());
				
				if (emailTo == null || tmp.trim().equals("noreply@mail.js")
						|| tmp.trim().equals(sender)) {

					Message message = new Message();
					message.setAbout("Ошибка отправки");
					message.setEmail(noreply);
					message.setSentDate(messDate);
					StringBuilder text = new StringBuilder("Ошибка отправки на адрес ");
					text.append(tmp.trim());
					text.append(". Адресса не существует.\n\nСлужба mail.js.");
					text.append("\n\nВаше сообщение: \n");
					text.append("Тема: " + data[1]);
					text.append("\n" + data[2]);
					message.setText(text.toString());
					em.persist(message);
					
					SimpleMessage incomeMessage = new SimpleMessage();
					incomeMessage.setAbout("Ошибка отправки");
					incomeMessage.setDate(messDate);
					incomeMessage.setFrom("noreply@mail.js");
					incomeMessage.setRead(false);
					incomeMessage.setText(text.toString());
					incomeMessage.setTo("Мне");
					incomeMessages.add(incomeMessage);
					
					MessToFold mf = new MessToFold();
					mf.setFolder(senderInput);
					mf.setMessage(message);
					em.persist(mf);

					MessToMail mm = new MessToMail();
					mm.setMessage(message);
					mm.setTo(senderEmail);
					mm.setRead(false);
					em.persist(mm);
				} else {
					getters.append(tmp.trim() + "; ");
					
					if (!thereIsGoodGetter) {
						em.persist(newMessage);
						MessToFold mf = new MessToFold();
						mf.setFolder(senderOutput);
						mf.setMessage(newMessage);
						em.persist(mf);
						thereIsGoodGetter = true;
						
						
						outcomeMess.setAbout(data[1]);
						outcomeMess.setDate(messDate);
						outcomeMess.setText(data[2]);
						outcomeMess.setFrom("Меня");
						outcomeMess.setRead(true);
					}
					
					List<Folder> getterFolders = emailTo.getFolders();
					Folder getterInput = null;
					for (Folder folder: getterFolders) {
						if (folder.getName().equals("Входящие"))
							getterInput = folder;
					}
					
					MessToFold mf = new MessToFold();
					mf.setFolder(getterInput);
					mf.setMessage(newMessage);
					em.persist(mf);

					MessToMail mm = new MessToMail();
					mm.setMessage(newMessage);
					mm.setTo(emailTo);
					mm.setRead(false);
					em.persist(mm);
				}	
			}
			trx.commit();
			
			Object[] serverMessages = new Object[2];
			if (thereIsGoodGetter) {
				outcomeMess.setTo(getters.toString());
				serverMessages[0] = outcomeMess;
			} else 
				serverMessages[0] = null;
			
			serverMessages[1] = incomeMessages;
			return serverMessages;
			
		} catch (Exception e) {
			e.printStackTrace();
			if (trx.isActive())
				trx.rollback();
			return null;
		} finally {
			
			emf.close();
		}
	}
	
	public static boolean setRead(SimpleMessage message, String whose) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			
			Email email = em.find(Email.class, whose);
			Message foundMessage = null;
			
			List<MessToMail> messToMails = email.getMessToMails();


				for (MessToMail messToMail : messToMails) {
					foundMessage = messToMail.getMessage();
				if (foundMessage.getAbout().equals(message.getAbout())
						&& foundMessage.getText().equals(message.getText())
						&& format.format(foundMessage.getSentDate()).equals(
								format.format(message.getDate()))) {
					messToMail.setRead(true);
					em.merge(messToMail);
					trx.commit();
					return true;
				}
			}
					
			
		} catch (Exception e) {
			e.printStackTrace();
			if (trx.isActive())
				trx.rollback();
			return false;
		} finally {
			emf.close();
		}
		return false;
	}
	
	public static boolean deleteMess(SimpleMessage message, String whose) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();

			Email email = em.find(Email.class, whose);
			Message foundMessage = null;

			List<Folder> folders = email.getFolders();
			label:
			for (Folder folder : folders) {
				for (MessToFold messToFold : folder.getMessToFolds()) {
					foundMessage = messToFold.getMessage();
					if (foundMessage.getAbout().equals(message.getAbout())
							&& foundMessage.getText().equals(message.getText())
							&& format.format(foundMessage.getSentDate())
									.equals(format.format(message.getDate()))) {
						em.remove(messToFold);
						trx.commit();
						trx.begin();
						break label;
					}
				}
			}
			List<MessToFold> messToFolds = foundMessage.getMessToFolds();
			if (messToFolds.size() == 0) {
				List<MessToMail> messToMails = foundMessage.getMessToMails();
				for (MessToMail messToMail : messToMails)
					em.remove(messToMail);
				em.remove(foundMessage);
			}
			trx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (trx.isActive())
				trx.rollback();
			return false;
		} finally {
			emf.close();
		}
	}
	
	public static SimpleFolder createFolder(String folderName, String whose) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			Email email = em.find(Email.class, whose);
			boolean thereIsSuchFolder = false;
			List<Folder> folders = email.getFolders();
			for (Folder folder : folders) {
				if (folder.getName().equalsIgnoreCase(folderName)) {
					thereIsSuchFolder = true;
					break;
				}
			}
			if (thereIsSuchFolder)
				return null;
			Folder folder = new Folder();
			folder.setName(folderName);
			folder.setUndel(false);
			folder.setWhose(email);
			em.persist(folder);
			trx.commit();
			
			SimpleFolder simpleFolder = new SimpleFolder();
			simpleFolder.setName(folderName);
			return simpleFolder;
		} catch (Exception e) {
			e.printStackTrace();
			if (trx.isActive())
				trx.rollback();
			return null;
		} finally {
			emf.close();
		}
	}
	
	public static byte deleteFolder(SimpleFolder sFolder, String whose) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			Email email = em.find(Email.class, whose);
			List<Folder> folders = email.getFolders();
			Folder foundFolder = null;
			for (Folder folder : folders)
				if (folder.getName().equals(sFolder.getName())) {
					foundFolder = folder;
					break;
				}	
			
			if (foundFolder.getUndel()) {
				return 1;
			}
			
			List<MessToFold> messToFolds = foundFolder.getMessToFolds();
			if (messToFolds.size() == 0) {
				em.remove(foundFolder);
				trx.commit();
				return 0;
			}
			for (MessToFold messToFold : messToFolds) {
				Message message = messToFold.getMessage();
				List<MessToFold> mtfs = message.getMessToFolds();
				if (mtfs.size() != 1) {
					em.remove(messToFold);
				} else {
					List<MessToMail> mtms = message.getMessToMails();
					for (MessToMail mtm : mtms) 
						em.remove(mtm);
					em.remove(messToFold);
					em.remove(message);
				}
			}
			em.remove(foundFolder);
			trx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (trx.isActive())
				trx.rollback();
			return 2;
		} finally {
			emf.close();
		}
		return 0;
	}
	
	public static boolean moveFolder(SimpleMessage message, String fromFolder, String toFolder, String whose) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			Email email = em.find(Email.class, whose);
			List<Folder> folders = email.getFolders();
			Message foundMessage = null;
			Folder fFolder = null;
			Folder tFolder = null;
			for (Folder folder : folders) {
				if (folder.getName().equals(fromFolder)) {
					fFolder = folder;
					break;
				}
			}
			for (Folder folder : folders) {
				if (folder.getName().equals(toFolder)) {
					tFolder = folder;
					break;
				}
			}
			
			List<MessToFold> messToFolds = fFolder.getMessToFolds();
			for (MessToFold messToFold : messToFolds) {
				foundMessage = messToFold.getMessage();
				if (foundMessage.getAbout().equals(message.getAbout())
						&& foundMessage.getText().equals(message.getText())
						&& format.format(foundMessage.getSentDate())
								.equals(format.format(message.getDate()))) {
					messToFold.setFolder(tFolder);
					break;
				}
			}
			trx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (trx.isActive())
				trx.rollback();
			return false;
		} finally {
			emf.close();
		}
	}
}
