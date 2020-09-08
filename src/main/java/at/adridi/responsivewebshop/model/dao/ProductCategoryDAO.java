/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.model.dao;

import at.adridi.responsivewebshop.model.ProductCategory;
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
public class ProductCategoryDAO implements AutoCloseable, Serializable {

    public List<ProductCategory> getAllProductCategory() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM ProductCategory");
            return query.list();
        } catch (HibernateException e) {
            System.out.println("Error in getAllProductCategory(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    public List<String> getAllProductCategoryName() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("SELECT categoryName FROM ProductCategory");
            return query.list();
        } catch (HibernateException e) {
            System.out.println("Error in getAllProductCategoryName(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    public ProductCategory getProductCategoryById(Integer productCategoryId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM ProductCategory WHERE category_id=:categoryidvalue");
            query.setParameter("categoryidvalue", productCategoryId);
            List<ProductCategory> productcategory = query.list();
            if (productcategory != null) {
                return productcategory.get(0);
            } else {
                return null;
            }

        } catch (HibernateException e) {
            System.out.println("Error in getProductCategoryById(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    public ProductCategory getProductCategoryByCategoryName(String productCategoryName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM ProductCategory WHERE categoryName=:categorynamevalue");
            query.setParameter("categorynamevalue", productCategoryName);
            List<ProductCategory> productcategory = query.list();
            if (productcategory != null) {
                return productcategory.get(0);
            } else {
                return null;
            }

        } catch (HibernateException e) {
            System.out.println("Error in getProductCategoryByCategoryName(): " + e);
            return null;
        } finally {
            session.close();
        }
    }

    public boolean insertProductCategory(String productCategoryname) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            if (productCategoryname == null && productCategoryname.trim().equals("")) {
                return false;
            }
            ProductCategory newProductCategory = new ProductCategory();
            newProductCategory.setCategoryName(productCategoryname);

            String productCategoryAlias = productCategoryname.replace(" ", "_");
            productCategoryAlias = productCategoryAlias.replace("/", "_");
            productCategoryAlias = productCategoryAlias.replace("\\", "_");
            productCategoryAlias = productCategoryAlias.replace("=", "_");
            productCategoryAlias = productCategoryAlias.replace("\"", "_");
            productCategoryAlias = productCategoryAlias.replace("'", "_");
            productCategoryAlias = productCategoryAlias.replace("`", "_");

            newProductCategory.setAlias(productCategoryAlias);
            transaction = session.beginTransaction();
            session.save(newProductCategory);
            transaction.commit();
            return true;

        } catch (HibernateException e) {
            System.out.println("Error in insertProductCategory(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            session.close();
        }
    }

    public boolean deleteProductCategory(ProductCategory productcategory) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.delete(productcategory);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println("Error in deleteProductCategory(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            session.close();
        }
    }

    public boolean updateProductCategory(ProductCategory productcategory) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(productcategory);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println("Error in updateProductCategory(): " + e);
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public void close() throws Exception {
        HibernateUtil.close();
    }
}
