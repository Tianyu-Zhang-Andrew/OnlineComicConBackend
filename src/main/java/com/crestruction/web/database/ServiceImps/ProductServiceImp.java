package com.crestruction.web.database.ServiceImps;

import com.crestruction.web.database.entities.Product;
import com.crestruction.web.database.exceptions.AlreadyExistExceptions.ProductAlreadyExistException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.NoResultFoundException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.ProductNotFoundException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.TableIsEmptyException;
import com.crestruction.web.database.repositories.ProductRepository;
import com.crestruction.web.database.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        //Check if the product already exists
        Example<Product> productExample =  Example.of(product);
        Optional<Product> existProduct = this.productRepository.findOne(productExample);

        //Is the product already exists, throw exception
        if(existProduct.isPresent()) {
            throw new ProductAlreadyExistException();

        //If product doesn't exist, create product
        }else {
            Product savedProduct = this.productRepository.save(product);
            return savedProduct;
        }
    }

    @Override
    public Product editProduct(Product product) {
        //Check if the edited product exists
        Optional<Product> existProduct = this.productRepository.findById(product.getId());

        //If the product doesn't exists, throw exception
        if(!existProduct.isPresent()) {
            throw new ProductNotFoundException(product.getId());

        //If the product exists, edit the product
        }else {
            Product savedProduct = this.productRepository.save(product);
            return savedProduct;
        }
    }

    @Override
    public void deleteProduct(Product product) {
        //Check if the product to be deleted exists
        Optional<Product> existProduct = this.productRepository.findById(product.getId());

        //If the product doesn't exists, throw exception
        if(!existProduct.isPresent()) {
            throw new ProductNotFoundException(product.getId());

        //If the product exists, delete the product
        }else {
            this.productRepository.delete(product);
        }
    }

    @Override
    public void deleteAllProduct() {
        this.productRepository.deleteAll();
    }

    @Override
    public List<Product> searchProduct(Product product) {
        //Dynamically search for the result
        Example<Product> productExample =  Example.of(product);
        List<Product> productList = this.productRepository.findAll(productExample);

        //If there is no result found, throw exception
        if(productList.size() == 0){
            throw new NoResultFoundException();

            //If result found, return all results
        }else {
            return productList;
        }
    }

    @Override
    public List<Product> getAllProduct() {
        List<Product> productList = this.productRepository.findAll();

        //If there is no records in the table, throw exception
        if(productList.size() == 0){
            throw new TableIsEmptyException();

            //If there is records in the table, return all records
        }else{
            return productList;
        }
    }
}
