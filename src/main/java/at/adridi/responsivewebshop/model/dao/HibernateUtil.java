/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.model.dao;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author A.Dridi
 */
public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY_OBJECT;
    private static final ServiceRegistry SERVER_REGISTRY_OBJECT;

    static {
        try {
            // loading configuration and mappings
            Configuration configurationObject = new Configuration().configure();
            SERVER_REGISTRY_OBJECT = new StandardServiceRegistryBuilder().applySettings(configurationObject.getProperties()).build();
            // Building a session factory from the service registry
            SESSION_FACTORY_OBJECT = configurationObject.buildSessionFactory(SERVER_REGISTRY_OBJECT);

        } catch (HibernateException e) {
            System.err.println("Initial SessionFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);

        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY_OBJECT;
    }

    public static void close() {
        if (SERVER_REGISTRY_OBJECT != null) {
            StandardServiceRegistryBuilder.destroy(SERVER_REGISTRY_OBJECT);
        }
    }
}
