package org.dms;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

 
public class HibernateUtil {
 
    private static final SessionFactory sessionFactory;
 
    static {
        try {
            sessionFactory = new AnnotationConfiguration()
            					.addAnnotatedClass(MessToFold.class)
            					.addAnnotatedClass(Email.class)
            					.addAnnotatedClass(Folder.class)
            					.addAnnotatedClass(Message.class)
            					.addAnnotatedClass(MessToMail.class)
            					.addAnnotatedClass(User.class)
                                .configure().buildSessionFactory();
 
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
