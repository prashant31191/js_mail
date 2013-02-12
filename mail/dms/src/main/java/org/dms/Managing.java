package org.dms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

public class Managing {
	public static String creatUser(String[] data) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		String newAddress = null;
		try {
			transaction = session.beginTransaction();

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
			session.save(user);

			Email email = new Email();
			newAddress = data[0] + "@mail.js";
			email.setEaddress(newAddress);
			String hashedPass = BCrypt.hashpw(data[1], BCrypt.gensalt(12));
			email.setPassword(hashedPass);
			email.setUser(user);
			email.setCreationDate(new Date(System.currentTimeMillis()));
			session.save(email);
			
			Folder input = new Folder();
			input.setName("Входящие");
			input.setUndel(true);
			input.setWhose(email);
			session.save(input);
			
			Folder output = new Folder();
			output.setName("Исходящие");
			output.setUndel(true);
			output.setWhose(email);
			session.save(output);
			
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
			return "";
		} finally {
			session.close();
		}
		return newAddress;
	}

	public static boolean checkUser(String mailAddress, String pass) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		List<Email> mailList = null;
		try {
			transaction = session.beginTransaction();
			String hashedPass = BCrypt.hashpw(pass, BCrypt.gensalt(12));
			
			Query query = session.createQuery("from Email where e_address = :mail AND " +
					"e_password = :pass");
			query.setParameter("mail", mailAddress);
			query.setParameter("pass", hashedPass);
			mailList = (List<Email>) query.list();
			
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (mailList.isEmpty())
			return false;
		return true;
	}
}
