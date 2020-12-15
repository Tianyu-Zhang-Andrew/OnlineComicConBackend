package com.crestruction.web.database.ServiceImps;

import com.crestruction.web.database.CompositeKeys.ProductSpotKey;
import com.crestruction.web.database.entities.ExhibitionSpot;
import com.crestruction.web.database.entities.Product;
import com.crestruction.web.database.entities.ProductSpot;
import com.crestruction.web.database.exceptions.AlreadyExistExceptions.ProductSpotAlreadyExistException;
import com.crestruction.web.database.exceptions.AttributeEntityIncompleteException.ExhibitionSpotHasNoIdException;
import com.crestruction.web.database.exceptions.AttributeEntityIncompleteException.ProductHasNoIdException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.*;
import com.crestruction.web.database.repositories.ExhibitionSpotRepository;
import com.crestruction.web.database.repositories.ProductRepository;
import com.crestruction.web.database.repositories.ProductSpotRepository;
import com.crestruction.web.database.services.ProductSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductSpotServiceImp implements ProductSpotService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ExhibitionSpotRepository exhibitionSpotRepository;
    @Autowired
    private ProductSpotRepository productSpotRepository;

    @Override
    public ProductSpot saveProductSpot(ProductSpot productSpot) {
        //Product must has id
        if(productSpot.getProductSpotKey().getProduct().getId() == null){
            throw new ProductHasNoIdException();
        }

        //ExhibitionSpot must has id
        if(productSpot.getProductSpotKey().getExhibitionSpot().getId() == null){
            throw new ExhibitionSpotHasNoIdException();
        }

        //Check if the exhibitionSpot and product both exist
        Optional<ExhibitionSpot> existExhibitionSpot = this.exhibitionSpotRepository
                .findById(productSpot.getProductSpotKey().getExhibitionSpot().getId());
        Optional<Product> existProduct = this.productRepository.findById(productSpot.getProductSpotKey().getProduct().getId());

        //If the exhibitionSpot and product both exist, save the productSpot
        if(existExhibitionSpot.isPresent() && existProduct.isPresent()){
            Optional<ProductSpot> existProductSpot = this.productSpotRepository.findById(productSpot.getProductSpotKey());

            //If the productSpot already exists, throw exception
            if(existProductSpot.isPresent()){
                throw new ProductSpotAlreadyExistException();
            }else{
                existExhibitionSpot.get().getProductSpots().add(productSpot);
                existProduct.get().getProductSpots().add(productSpot);

                this.exhibitionSpotRepository.save(existExhibitionSpot.get());
                this.productRepository.save(existProduct.get());

                Optional<ProductSpot> savedProductSpot = this.productSpotRepository.findById(productSpot.getProductSpotKey());
                return savedProductSpot.get();
            }

            //If the ExhibitionSpot doesn't exist, throw exception
        }else if(!existExhibitionSpot.isPresent()){
            throw new ExhibitionSpotNotFoundException(productSpot.getProductSpotKey().getExhibitionSpot().getId());

            //If the product doesn't exist, throw exception
        }else{
            throw new ProductNotFoundException(productSpot.getProductSpotKey().getProduct().getId());
        }
    }

    @Override
    public ProductSpot editProductSpot(ProductSpot oldProductSpot, ProductSpot newProductSpot) {
        //Check if the ProductSpot needs to be changed exists
        Optional<ProductSpot> existOldClubProduct = this.productSpotRepository.findById(oldProductSpot.getProductSpotKey());

        //If the productSpot needs to be changed exists
        if(existOldClubProduct.isPresent()){

            //Check if the newProductSpot's exhibitionSpot and product both exist
            Optional<ExhibitionSpot> exhibitionSpot = this.exhibitionSpotRepository
                    .findById(newProductSpot.getProductSpotKey().getExhibitionSpot().getId());
            Optional<Product> existProduct = this.productRepository
                    .findById(newProductSpot.getProductSpotKey().getProduct().getId());

            //If the exhibitionSpot and product both exist, edit the productSpot
            if(exhibitionSpot.isPresent() && existProduct.isPresent()){
                //Remove the old productSpot
                oldProductSpot.getProductSpotKey().getExhibitionSpot().getProductSpots().remove(oldProductSpot);
                oldProductSpot.getProductSpotKey().getProduct().getProductSpots().remove(oldProductSpot);
                this.exhibitionSpotRepository.save(oldProductSpot.getProductSpotKey().getExhibitionSpot());
                this.productRepository.save(oldProductSpot.getProductSpotKey().getProduct());
                this.productSpotRepository.deleteById(oldProductSpot.getProductSpotKey());

                //Add the new productSpot
                exhibitionSpot.get().getProductSpots().add(newProductSpot);
                existProduct.get().getProductSpots().add(newProductSpot);
                this.exhibitionSpotRepository.save(exhibitionSpot.get());
                this.productRepository.save(existProduct.get());

                Optional<ProductSpot> savedProductSpot = this.productSpotRepository.findById(newProductSpot.getProductSpotKey());
                return savedProductSpot.get();

                //If the exhibitionSpot doesn't exist, throw exception
            }else if(!exhibitionSpot.isPresent()){
                throw new ExhibitionSpotNotFoundException(newProductSpot.getProductSpotKey().getExhibitionSpot().getId());

                //If the product doesn't exist, throw exception
            }else{
                throw new ProductNotFoundException(newProductSpot.getProductSpotKey().getProduct().getId());
            }

            //If the ProductSpot needs to be changed doesn't exist, throw exception
        }else{
            throw new ProductSpotNotFoundException();
        }
    }

    @Override
    public void deleteProductSpot(ProductSpot productSpot) {
        //Check if the ProductSpot to be deleted exists
        Optional<ProductSpot> existProductSpot = this.productSpotRepository.findById(productSpot.getProductSpotKey());

        //If the ProductSpot to be deleted exists, delete the ClubProduct
        if(existProductSpot.isPresent()){
            //Delete the ProductSpot from the exhibitionSpot
            productSpot.getProductSpotKey().getExhibitionSpot().getProductSpots().remove(productSpot);
            this.exhibitionSpotRepository.save(productSpot.getProductSpotKey().getExhibitionSpot());

            //Delete the ProductSpot from the product
            productSpot.getProductSpotKey().getProduct().getProductSpots().remove(productSpot);
            this.productRepository.save(productSpot.getProductSpotKey().getProduct());

            //Remove the ProductSpot
            this.productSpotRepository.deleteById(productSpot.getProductSpotKey());

            //If the ProductSpot to be deleted doesn't exist, throw exception
        }else{
            throw new ProductSpotNotFoundException();
        }
    }

    @Override
    public void deleteAllProductSpot() {
        List<ProductSpot> productSpotList = this.productSpotRepository.findAll();
        for(ProductSpot productSpot : productSpotList){
            deleteProductSpot(productSpot);
        }
    }

    @Override
    public List<ProductSpot> searchProductSpot(ExhibitionSpot exhibitionSpot, Product product) {
        List<ProductSpot> productSpotList = new ArrayList<>();

        //Search by both exhibitionSpot and product
        if(exhibitionSpot != null && product != null){

            //ExhibitionSpot must has id
            if(exhibitionSpot.getId() == null){
                throw new ExhibitionSpotHasNoIdException();
            }

            //Product must has id
            if(product.getId() == null){
                throw new ProductHasNoIdException();
            }

            ProductSpotKey productSpotKey = new ProductSpotKey(product, exhibitionSpot);
            Optional<ProductSpot> existProductSpot = this.productSpotRepository.findById(productSpotKey);
            productSpotList.add(existProductSpot.get());

            //Search only by exhibitionSpot
        }else if(exhibitionSpot != null){

            //ExhibitionSpot must has id
            if(exhibitionSpot.getId() == null){
                throw new ExhibitionSpotHasNoIdException();
            }

            productSpotList = this.productSpotRepository.findByProductSpotKeyExhibitionSpot(exhibitionSpot);

            //Search only by product
        }else{

            //Product must has id
            if(product.getId() == null){
                throw new ProductHasNoIdException();
            }

            productSpotList = this.productSpotRepository.findByProductSpotKeyProduct(product);
        }

        if(productSpotList.size() == 0){
            throw new NoResultFoundException();
        }

        return productSpotList;
    }

    @Override
    public List<ProductSpot> getAllProductSpot() {
        List<ProductSpot> productSpotList = this.productSpotRepository.findAll();

        //If there is no records in the table, throw exception
        if(productSpotList.size() == 0){
            throw new TableIsEmptyException();

            //If there is records in the table, return all records
        }else{
            return productSpotList;
        }
    }
}
