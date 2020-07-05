/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.converter;

import at.adridi.responsivewebshop.model.ProductCategory;
import at.adridi.responsivewebshop.model.dao.ProductCategoryDAO;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author A.Dridi
 */
@FacesConverter("productCategoryConverter")
public class ProductCategoryConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String categoryNameString) {
        if (categoryNameString == null || categoryNameString.trim().isEmpty()) {
            return null;
        } else {
            ProductCategoryDAO productCategoryDAO = new ProductCategoryDAO();
            ProductCategory productCategoryOfString = productCategoryDAO.getProductCategoryByCategoryName(categoryNameString);
            return productCategoryOfString;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object productCategoryObject) {
        return productCategoryObject.toString();
    }

}
