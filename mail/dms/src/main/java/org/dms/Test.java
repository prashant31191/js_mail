package org.dms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.cc.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> list = new LinkedList<String>();
		list.add(0, "a");
		list.add(1, "b");
		list.add(2, "c");
		
		list.add(0, "d");
		
		for (String str: list)
			System.out.println(list.indexOf(str) + " - " + str);
		
//		EntityManagerFactory emf = Persistence.createEntityManagerFactory("mail");
//        EntityManager em = emf.createEntityManager();
//        
//        try { 
//        	Email mail = em.find(Email.class, "foma@mail.js");
//        	
//        	User user = mail.getUser();
//        
//        	List<Folder> folders = mail.getFolders();
//        	List<SimpleFolder> sFolders = new ArrayList<SimpleFolder>();
//        	for(Folder f: folders) {
//        		SimpleFolder sf = new SimpleFolder();
//        		sf.setName(f.getName());
//        		List<MessToFold> messToFold = f.getMessToFolds();
//        		List<SimpleMessage> sMessages = new ArrayList<SimpleMessage>();
//        		for(MessToFold mf: messToFold) {
//        			Message message = mf.getMessage();
//        			SimpleMessage sm = new SimpleMessage();
//        			
//        			sm.setDate(message.getSentDate());
//        			sm.setAbout(message.getAbout());
//        			sm.setText(message.getText());
//        			if (message.getEmail().getEaddress().equals("foma@mail.js")) {
//        				sm.setFrom("Меня");
//        				List<MessToMail> messToMail = message.getMessToMails();
//        				StringBuilder sb = new StringBuilder();
//        				for (MessToMail mm: messToMail)
//        					sb.append(mm.getTo() + "; ");
//        				sm.setTo(sb.toString());
//        				sm.setRead(true);
//        			} else {
//        				sm.setFrom(message.getEmail().getEaddress());
//        				sm.setTo("Мне");
//        				List<MessToMail> messToMail = message.getMessToMails();
//        				for (MessToMail mm: messToMail)
//        					if (mm.getTo().equals("foma.mail.js"))
//        						sm.setRead(mm.getRead());
//        			}
//        			sMessages.add(sm);	
//        		}
//        		sf.setMessages(sMessages);
//        		sFolders.add(sf);
//        	}
//        	
//        	for(SimpleFolder f: sFolders) {
//        		System.out.println("- " + f.getName());
//        		for(SimpleMessage m: f.getMessages()) {
//        			System.out.println(" --- " + m.getFrom() + "   " + m.getTo() + "   " + m.isRead());
//        		}
//        	}
//        		
//        } catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			emf.close();
//		}

	}

}
