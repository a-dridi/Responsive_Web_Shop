/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.model.dao;

import at.adridi.responsivewebshop.model.Product;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author A.Dridi
 */
public class ProductDAO implements AutoCloseable, Serializable {

    public List<Product> getAllProduct() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Product");
            return query.list();
        } catch (HibernateException e) {
            System.out.println("Error in getAllProduct(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    public Product getProductById(Integer productId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Product WHERE product_id=:productidValue");
            query.setParameter("productidValue", productId);
            List<Product> productsWithProductId = query.list();
            if (productsWithProductId != null) {
                return productsWithProductId.get(0);
            } else {
                return null;
            }
        } catch (HibernateException e) {
            System.out.println("Error in getProductById(): " + e);
            return null;
        } finally {
            session.close();
        }

    }

    public List<Product> getProductByProductCategory(Integer productCategoryId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("SELECT product.* FROM Product product INNER JOIN product.product_category_id productcategory WHERE product.category=:productCategoryId");
            query.setParameter("productCategoryId", productCategoryId);
            List<Product> productsOfCategory = query.list();
            return productsOfCategory;

        } catch (HibernateException e) {
            System.out.println("Error in getProductByProductCategory(): " + e);
            return null;
        } finally {
            session.close();
        }

    }

    /**
     * Get n products from the row number position "position" ordered by the
     * newest added. Product_Id descending order. If 0 is passed in both
     * arguments, then return products from row number 0 to 20.
     *
     * @return
     */
    public List<Product> getNLatestProductsFromPositionN(int nProducts, int position) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM Product ORDER BY product_id DESC");
            if (nProducts > 0 && position >= 0) {
                query.setFirstResult(position);
                query.setMaxResults(nProducts);
            } else {
                query.setFirstResult(0);
                query.setMaxResults(20);
            }
            return query.list();
        } catch (HibernateException e) {
            System.out.println("Error in getLatestProductFromAToB(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    public boolean addProduct(Product product) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean returnValue = false;

        try {
            transaction = session.beginTransaction();
            session.save(product);
            transaction.commit();
            returnValue = true;

        } catch (HibernateException e) {
            System.out.println("Error in addProduct(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
            return returnValue;
        }
    }

    public boolean deleteProduct(Product product) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean returnValue = false;

        try {
            transaction = session.beginTransaction();
            session.delete(product);
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println("Error in deleteProduct(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
            return returnValue;
        }
    }

    public boolean updateProduct(Product product) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        boolean returnValue = false;

        try {
            transaction = session.beginTransaction();
            session.update(product);
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println("Error in updateProduct(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
            return returnValue;
        }
    }

    @Override
    public void close() throws Exception {
        HibernateUtil.close();
    }

}
