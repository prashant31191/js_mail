package org.dms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	public static String createUser(String[] data) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		String newAddress = null;
		try {
			/*Ищем почтовый адрес*/
			Email checkEmail = em.find(Email.class, data[0] + "@mail.js");
			/*Проверяем, существует ли такой почтовый адрес*/
			if (checkEmail != null)
				return "";

			trx.begin();
			/*Создаем пользователя*/
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
			/*Создаем почтовый ящик*/
			Email email = new Email();
			newAddress = data[0] + "@mail.js";
			email.setEaddress(newAddress);
			String hashedPass = BCrypt.hashpw(data[1], BCrypt.gensalt(12));
			email.setPassword(hashedPass);
			email.setUser(user);
			email.setCreationDate(new Date(System.currentTimeMillis()));
			em.persist(email);
			/*Создаем неудаляемые папки Входящие, Исходящие и Корзина*/
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
			
			Folder trash = new Folder();
			trash.setName("Корзина");
			trash.setUndel(true);
			trash.setWhose(email);
			em.persist(trash);
			/*Находим почтовый адрес сервера*/
			Email noreply = em.find(Email.class, "noreply@mail.js");
			/*Создаем сообщение*/
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
			/*Находим почтовый адрес*/
			Email mail = em.find(Email.class, mailAddress);

			if (mail == null)
				return false;
			/*Если адрес существует, проверяем пароль*/
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

	/**
	 * Gets amount of new messages in input Folder
	 * 
	 * @param amount
	 *            Amount of messages the user has right now in Input folder
	 * @param whose
	 *            user's email address
	 * @return Amount of new messages
	 */
	public static int hasNewMessages(int amount, String whose) {
		int amountOfNew = 0;
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		try {
			/*Находим почтовый адрес и его папки*/
			Email mail = em.find(Email.class, whose);
			List<Folder> folders = mail.getFolders();
			/*Находим папку входящие*/
			Folder inputFolder = null;
			for (Folder folder : folders)
				if (folder.getName().equals("Входящие")) {
					inputFolder = folder;
					break;
				}
			amountOfNew = inputFolder.getMessToFolds().size() - amount;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			emf.close();
		}
		return amountOfNew;
	}

	/**
	 * Gets given amount of last messages in Input folder
	 * 
	 * @param amountOfNew
	 *            Amount of messages to get
	 * @param whose
	 *            user's email address
	 * @return List of SimpleMessages, that is, given amount of last messages
	 */
	public static List<SimpleMessage> getNewMessages(int amountOfNew,
			String whose) {
		List<SimpleMessage> newMessages = null;
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		try {
			/*Находим почтовый адрес и его папки*/
			Email mail = em.find(Email.class, whose);
			List<Folder> folders = mail.getFolders();
			/*Находим папку Входящие*/
			Folder inputFolder = null;
			for (Folder folder : folders)
				if (folder.getName().equals("Входящие")) {
					inputFolder = folder;
					break;
				}
			
			List<MessToFold> messToFold = inputFolder.getMessToFolds();
			List<SimpleMessage> simpleMessages = new ArrayList<SimpleMessage>();
			/*Достаем все сообщения в папке входящие*/
			for (MessToFold mf : messToFold) {
				Message message = mf.getMessage();
				SimpleMessage sm = new SimpleMessage();

				sm.setDate(message.getSentDate());
				sm.setAbout(message.getAbout());
				sm.setText(message.getText());
				if (message.getEmail().getEaddress().equals(mail)) {
					/*Формирование сообщение, если оно от пользователя*/
					sm.setFrom("Меня");
					List<MessToMail> messToMail = message.getMessToMails();
					StringBuilder sb = new StringBuilder();
					for (MessToMail mm : messToMail)
						sb.append(mm.getTo().getEaddress() + "; ");
					sm.setTo(sb.toString());
					sm.setRead(true);
				} else {
					/*Формирование сообщения если оно пользователю*/
					sm.setFrom(message.getEmail().getEaddress());
					sm.setTo("Мне");
					List<MessToMail> messToMail = message.getMessToMails();
					for (MessToMail mm : messToMail)
						if (mm.getTo().getEaddress().equals(mail))
							sm.setRead(mm.getRead());
				}
				simpleMessages.add(0, sm);
			}
			/*Сортируем сообщения и берем новые*/
			Collections.sort(simpleMessages);
			newMessages = new ArrayList<SimpleMessage>(simpleMessages.subList(
					0, amountOfNew));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			emf.close();
		}
		return newMessages;
	}

	/**
	 * Gets everything the user has
	 * 
	 * @param email
	 *            User's email address
	 * @return List of SimpleFolder, that is, all folders and messages of given
	 *         user
	 */
	public static List<SimpleFolder> getEverything(String email) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();

		try {
			/*Находим адрес и папки*/
			Email mail = em.find(Email.class, email);
			List<Folder> dbFolders = mail.getFolders();
			
			List<SimpleFolder> sFolders = new ArrayList<SimpleFolder>();
			/*Проходим по всем папким*/
			for (Folder folder : dbFolders) {
				SimpleFolder sFolder = new SimpleFolder();
				sFolder.setName(folder.getName());
				List<MessToFold> messToFold = folder.getMessToFolds();
				List<SimpleMessage> simpleMessages = new ArrayList<SimpleMessage>();
				/*Проходим по всем сообщениям в папке*/
				for (MessToFold mf : messToFold) {
					Message message = mf.getMessage();
					SimpleMessage sm = new SimpleMessage();

					sm.setDate(message.getSentDate());
					sm.setAbout(message.getAbout());
					sm.setText(message.getText());
					if (message.getEmail().getEaddress().equals(email)) {
						/*Формирование сообщения, если оно исходящее*/
						sm.setFrom("Меня");
						List<MessToMail> messToMail = message.getMessToMails();
						StringBuilder sb = new StringBuilder();
						for (MessToMail mm : messToMail)
							sb.append(mm.getTo().getEaddress() + "; ");
						sm.setTo(sb.toString());
						sm.setRead(true);
					} else {
						/*Формирование сообщения, если оно входящее*/
						sm.setFrom(message.getEmail().getEaddress());
						sm.setTo("Мне");
						List<MessToMail> messToMail = message.getMessToMails();
						for (MessToMail mm : messToMail)
							if (mm.getTo().getEaddress().equals(email))
								sm.setRead(mm.getRead());
					}
					simpleMessages.add(0, sm);
				}
				/*Сортируем сообщения по дате и ложим в папку*/
				Collections.sort(simpleMessages);
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

	/**
	 * Sends a message
	 * 
	 * @param data
	 *            Information about the message
	 * @param sender
	 *            User's email address
	 * @return array of Object, containing input and output messages after
	 *         sending
	 */
	public static Object[] sendMessage(String[] data, String sender) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		boolean thereIsGoodGetter = false;
		try {
			String[] tmpEmails = data[0].trim().split(";");
			/*Убераем дубликаты*/
			Set<String> emails = new HashSet<String>();
			for (String tmp : tmpEmails)
				emails.add(tmp);
			
			trx.begin();
			/*Находим почту пользователя и сервера*/
			Email noreply = em.find(Email.class, "noreply@mail.js");
			Email senderEmail = em.find(Email.class, sender);
			/*Находим папки пользователя*/
			List<Folder> senderFolders = senderEmail.getFolders();
			Folder senderInput = null;
			Folder senderOutput = null;
			/*Назодим папки входящие и исходящие*/
			for (Folder folder : senderFolders) {
				if (folder.getName().equals("Входящие"))
					senderInput = folder;
				if (folder.getName().equals("Исходящие"))
					senderOutput = folder;
			}
			/*Создаем сообщение*/
			Message newMessage = new Message();
			newMessage.setEmail(senderEmail);
			newMessage.setAbout(data[1]);
			newMessage.setText(data[2]);
			Date messDate = new Date(System.currentTimeMillis());
			newMessage.setSentDate(messDate);

			StringBuilder getters = new StringBuilder();
			SimpleMessage outcomeMess = new SimpleMessage();
			List<SimpleMessage> incomeMessages = new ArrayList<SimpleMessage>();
			/*Проверяем лист получателей*/
			for (String tmp : emails) {
				Email emailTo = em.find(Email.class, tmp.trim());

				if (emailTo == null || tmp.trim().equals("noreply@mail.js")) {
//						|| tmp.trim().equals(sender)) {
					/*Если некорректный адрес получателя, формируем входящее сообщение*/
					Message message = new Message();
					message.setAbout("Ошибка отправки");
					message.setEmail(noreply);
					message.setSentDate(messDate);
					StringBuilder text = new StringBuilder(
							"Ошибка отправки на адрес ");
					text.append(tmp.trim());
					text.append(". Адреса не существует.\n\nСлужба mail.js.");
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
					/*Если адрес корректен, формируем исходящее сообщение*/
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
					for (Folder folder : getterFolders) {
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
			/*Формируем ответ из входящий и исходящий сообщений на передачу пользователю*/
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

	/**
	 * Set the message as read
	 * 
	 * @param message
	 *            Simple message to set as read
	 * @param whose
	 *            User's email address
	 * @return true, in case the message has been set as read
	 */
	public static boolean setRead(SimpleMessage message, String whose) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			/*Находим почтовый адрес пользователя*/
			Email email = em.find(Email.class, whose);
			Message foundMessage = null;
			/*НАходим необходимае сообщение и отмечаем как прочитанное*/
			for (MessToMail messToMail : email.getMessToMails()) {
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

	/**
	 * Deletes given message
	 * 
	 * @param message
	 *            Message to delete
	 * @param whose
	 *            User's email
	 * @return true, in case the message has been deleted
	 */
	public static boolean deleteMess(SimpleMessage message, String folderName, String whose) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			/*Находим почтовый адрес*/
			Email email = em.find(Email.class, whose);
			Message foundMessage = null;
			/*Ищем папку с письмом*/
			Folder foundFolder = null;
			for (Folder folder : email.getFolders())
				if (folder.getName().equals(folderName)) {
					foundFolder = folder;
					break;
				}
			/* Ищем письмо и удаляем связку в БД */
			for (MessToFold messToFold : foundFolder.getMessToFolds()) {
				foundMessage = messToFold.getMessage();
				if (foundMessage.getAbout().equals(message.getAbout())
						&& foundMessage.getText().equals(message.getText())
						&& format.format(foundMessage.getSentDate()).equals(
								format.format(message.getDate()))) {
					em.remove(messToFold);
					trx.commit();
					trx.begin();
					break;
				}
			}
			/* Проверяем, есть ли это сообщение у кого-нибудь другого */
			List<MessToFold> messToFolds = foundMessage.getMessToFolds();
			if (messToFolds.size() == 0) {
				/* Если нет, удаляем сообщение из БД */
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

	/**
	 * Creates folder
	 * 
	 * @param folderName
	 *            Name of folder to create
	 * @param whose
	 *            User's email address
	 * @return true, in case the folder has been created
	 */
	public static SimpleFolder createFolder(String folderName, String whose) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			/*Находим почтовый адрес*/
			Email email = em.find(Email.class, whose);
			boolean thereIsSuchFolder = false;
			/*Проверяем, если ли уже такая папка*/
			for (Folder folder : email.getFolders()) {
				if (folder.getName().equalsIgnoreCase(folderName)) {
					thereIsSuchFolder = true;
					break;
				}
			}
			if (thereIsSuchFolder)
				return null;
			/*Если такой папки нет, создаем*/
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

	/**
	 * Deletes given folder
	 * 
	 * @param sFolder
	 *            Folder to delete
	 * @param whose
	 *            User's email address
	 * @return 1 in case, the folder can't be deleted, 0 in case the folder has
	 *         been deleted
	 */
	public static byte deleteFolder(SimpleFolder sFolder, String whose) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			/*Находим почтовый адрес*/
			Email email = em.find(Email.class, whose);
			Folder foundFolder = null;
			/*Ищем папку*/
			for (Folder folder : email.getFolders())
				if (folder.getName().equals(sFolder.getName())) {
					foundFolder = folder;
					break;
				}
			/*Проверяем, можно ли ее удалить*/
			if (foundFolder.getUndel()) {
				return 1;
			}
			/*Удаляем папку и сообщения в ней*/
			List<MessToFold> messToFolds = foundFolder.getMessToFolds();
			if (messToFolds.size() == 0) {
				em.remove(foundFolder);
				trx.commit();
				return 0;
			}
			
			for (MessToFold messToFold : messToFolds) {
				Message message = messToFold.getMessage();
				SimpleMessage sMessage = new SimpleMessage();
				sMessage.setAbout(message.getAbout());
				sMessage.setDate(message.getSentDate());
				sMessage.setText(message.getText());
				Managing.moveMessage(sMessage, sFolder.getName(), "Корзина", whose);
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

	/**
	 * Moves given message from one folder to another
	 * 
	 * @param message
	 *            Message to move
	 * @param fromFolder
	 *            Name of source folder
	 * @param toFolder
	 *            Name of target folder
	 * @param whose
	 *            User's email address
	 * @return true, in case the folder has been moved
	 */
	public static boolean moveMessage(SimpleMessage message, String fromFolder,
			String toFolder, String whose) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			/*Ищем почтовый адрес*/
			Email email = em.find(Email.class, whose);
			List<Folder> folders = email.getFolders();
			Message foundMessage = null;
			Folder fFolder = null;
			Folder tFolder = null;
			/*Находим папку с письмом*/
			for (Folder folder : folders) {
				if (folder.getName().equals(fromFolder)) {
					fFolder = folder;
					break;
				}
			}
			/*Находим папку, в которую надо переместить письмо*/
			for (Folder folder : folders) {
				if (folder.getName().equals(toFolder)) {
					tFolder = folder;
					break;
				}
			}
			/*Находим письмо и перемещием его*/
			List<MessToFold> messToFolds = fFolder.getMessToFolds();
			for (MessToFold messToFold : messToFolds) {
				foundMessage = messToFold.getMessage();
				if (foundMessage.getAbout().equals(message.getAbout())
						&& foundMessage.getText().equals(message.getText())
						&& format.format(foundMessage.getSentDate()).equals(
								format.format(message.getDate()))) {
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
	
	public static boolean clearTrash(String whose) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			Message message = null;
			trx.begin();
			/*Находим почтовый адрес*/
			Email email = em.find(Email.class, whose);
			/*Ищем Корзину*/
			Folder foundFolder = null;
			for (Folder folder : email.getFolders())
				if (folder.getName().equals("Корзина")) {
					foundFolder = folder;
					break;
				}
			/* Удаляем письма */
			for (MessToFold messToFold : foundFolder.getMessToFolds()) {
				message = messToFold.getMessage();
				em.remove(messToFold);
				trx.commit();
				trx.begin();
				/* Проверяем, есть ли это сообщение у кого-нибудь другого */
				List<MessToFold> messToFolds = message.getMessToFolds();
				if (messToFolds.size() == 0) {
					/* Если нет, удаляем сообщение из БД */
					List<MessToMail> messToMails = message.getMessToMails();
					for (MessToMail messToMail : messToMails)
						em.remove(messToMail);
					em.remove(message);
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
	
	public static boolean createSession(int key, String whose) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			
			Session session = new Session();
			session.setId(key);
			session.setMail(whose);
			session.setTime(new Date(System.currentTimeMillis()));
			em.persist(session);
			
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
	
	public static String getSessionUser(int key) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		Session session = null;
		try {	
			session = em.find(Session.class, key);
			if (session == null)
				return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			emf.close();
		}
		return session.getMail();
	}
	
	public static boolean deleteSession(int key) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			
			Session session = em.find(Session.class, key);
			em.remove(session);
			
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
	
	public static boolean sessionExists(int key) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		try {	
			Session session = em.find(Session.class, key);
			if (session == null)
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			emf.close();
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Session> getSessions() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		List<Session> sessions;
		try {	
			sessions = em.createQuery("SELECT s FROM Session s").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			emf.close();
		}
		return sessions;
	}
	
	public static boolean undateSessionLastRequest(int key) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		EntityTransaction trx = em.getTransaction();
		try {
			trx.begin();
			
			Session session = em.find(Session.class, key);
			session.setTime(new Date(System.currentTimeMillis()));
			em.merge(session);
			
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
	
	public static boolean userOnline(String user) {
		
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("mail");
		EntityManager em = emf.createEntityManager();
		List<Session> sessions;
		try {	
			sessions = em.createQuery("SELECT s FROM Session s").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		} finally {
			emf.close();
		}
		for (Session session : sessions)
			if (session.getMail().equals(user)) {
				return true;
			}
		
		return false;
	}
}
