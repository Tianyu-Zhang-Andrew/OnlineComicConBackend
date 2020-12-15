package com.crestruction.web;

import com.crestruction.web.database.entities.Product;
import com.crestruction.web.database.exceptions.AlreadyExistExceptions.ProductAlreadyExistException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.NoResultFoundException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.ProductNotFoundException;
import com.crestruction.web.database.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
//@Rollback(false)
public class ProductTest {
    @Autowired
    private ProductService productService;

//    @After
//    public void clear() throws Exception {
//        productService.deleteAllProduct();
//    }

    @Test
    public void testAddNewProduct() throws Exception {
        //Save a product
        Product product = new Product(100, "picture1", "name1", "type1");
        Product savedProduct = this.productService.saveProduct(product);

        List<Product> productList = this.productService.getAllProduct();
        assert(productList.size() == 1);
        assert(productList.get(0).getId().equals(savedProduct.getId()));

        //Save another product
        Product product2 = new Product(100, "picture2", "name2", "type2");
        this.productService.saveProduct(product2);
        productList = this.productService.getAllProduct();
        assert(productList.size() == 2);

        //Save a product that is already existed
        try{
            this.productService.saveProduct(product2);
        }catch (Exception  e) {
            assert(e instanceof ProductAlreadyExistException);

            //Test the existed product will not be saved again
            productList = this.productService.getAllProduct();
            assert(productList.size() == 2);
        }
    }

    @Test
    public void testEditProduct() throws Exception{
        //Save a product
        Product product = new Product(100, "picture1", "name1", "type1");
        Product savedProduct = this.productService.saveProduct(product);
        long productId = savedProduct.getId();

        List<Product> productList = this.productService.getAllProduct();
        assert(productList.size() == 1);
        assert(productList.get(0).getName().equals("name1"));

        //Edit the product name
        Product editedProduct = new Product();
        editedProduct.setId(productId);
        editedProduct.setName("name2");

        this.productService.editProduct(editedProduct);
        productList = this.productService.getAllProduct();
        assert(productList.size() == 1);
        assert(productList.get(0).getName().equals("name2"));
        assert(productList.get(0).getId().equals(productId));

        //Try to edit a product that doesn't exist
        Product editedProduct2 = new Product();
        editedProduct2.setId(productId + 1);
        editedProduct2.setName("name3");

        try{
            this.productService.editProduct(editedProduct2);
        }catch (Exception  e) {
            assert(e instanceof ProductNotFoundException);

            //Test the edit is not made
            productList = this.productService.getAllProduct();
            assert(productList.get(0).getName().equals("name2"));
        }
    }

    @Test
    public void testSearchProduct() throws Exception {
        //Save a product
        Product product = new Product(100, "picture1", "name1", "type1");
        this.productService.saveProduct(product);

        //Save another product
        Product product2 = new Product(100, "picture2", "name2", "type2");
        this.productService.saveProduct(product2);

        //Save the third product
        Product product3 = new Product(100, "picture3", "name3", "type1");
        this.productService.saveProduct(product3);

        //Search all type1 products(should have 2 results)
        Product exampleProduct = new Product();
        exampleProduct.setType("type1");
        List<Product> productList = this.productService.searchProduct(exampleProduct);
        assert(productList.size() == 2);

        //Try to search for product that doesn't exist
        Product exampleProduct2 = new Product();
        exampleProduct2.setType("type3");

        try {
            this.productService.searchProduct(exampleProduct2);
        }catch (Exception  e){
            assert(e instanceof NoResultFoundException);
        }
    }

    @Test
    public void testDeleteProduct() throws Exception{
        //Save a product
        Product product = new Product(100, "picture1", "name1", "type1");
        this.productService.saveProduct(product);

        //Save another product
        Product product2 = new Product(100, "picture2", "name2", "type2");
        this.productService.saveProduct(product2);

        //Save the third product
        Product product3 = new Product(100, "picture3", "name3", "type1");
        Product savedProduct = this.productService.saveProduct(product3);
        long productId3 = savedProduct.getId();

        //Try to delete club3
        Product deleteProduct = new Product();
        deleteProduct.setId(productId3);
        this.productService.deleteProduct(deleteProduct);

        //There should only be 2 clubs left
        List<Product> productList = this.productService.getAllProduct();
        assert(productList.size() == 2);

        //Try to delete club that doesn't exist
        Product deleteProduct2 = new Product();
        deleteProduct2.setId(productId3);

        try {
            this.productService.deleteProduct(deleteProduct2);
        }catch (Exception  e){
            assert(e instanceof ProductNotFoundException);
        }
    }
}
