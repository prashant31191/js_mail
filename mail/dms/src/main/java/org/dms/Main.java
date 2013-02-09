package org.dms;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {
	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            User u1 = new User();
            u1.setBirthDate(new Date(System.currentTimeMillis()));
            u1.setFirstName("777");
            u1.setLastName("777");
            u1.setPhone("777");
            session.save(u1);
            
            Email e1 = new Email();
            e1.setCreationDate(new Date(System.currentTimeMillis()));
            e1.setEaddress("777");
            e1.setPassword("777");
            e1.setUser(u1);
            session.save(e1);
            
            Message m1 = new Message();
            m1.setAbout("777");
            m1.setSentDate(new Date(System.currentTimeMillis()));
            m1.setText("777");
            m1.setEmail(e1);
            session.save(m1);
            
            MessToMail mm = new MessToMail();
            mm.setMessage(m1);
            mm.setTo(e1);
            mm.setRead(true);
            session.save(mm);
            
            Folder f = new Folder();
            f.setName("777");
            f.setUndel(true);
            f.setWhose(e1);
            session.save(f);
            
            MessToFold mf = new MessToFold();
            mf.setFolder(f);
            mf.setMessage(m1);
            session.save(mf);
            
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
		
	}

}
