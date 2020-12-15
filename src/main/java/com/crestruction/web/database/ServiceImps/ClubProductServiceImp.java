package com.crestruction.web.database.ServiceImps;

import com.crestruction.web.database.CompositeKeys.ClubProductKey;
import com.crestruction.web.database.entities.Club;
import com.crestruction.web.database.entities.ClubProduct;
import com.crestruction.web.database.entities.Product;
import com.crestruction.web.database.exceptions.AlreadyExistExceptions.ClubProductAlreadyExistException;
import com.crestruction.web.database.exceptions.AttributeEntityIncompleteException.ClubHasNoIdException;
import com.crestruction.web.database.exceptions.AttributeEntityIncompleteException.ProductHasNoIdException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.*;
import com.crestruction.web.database.repositories.ClubProductRepository;
import com.crestruction.web.database.repositories.ClubRepository;
import com.crestruction.web.database.repositories.ProductRepository;
import com.crestruction.web.database.services.ClubProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClubProductServiceImp implements ClubProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ClubProductRepository clubProductRepository;

    @Override
    public ClubProduct saveClubProduct(ClubProduct clubProduct) {
        //Club must has id
        if(clubProduct.getClubProductKey().getClub().getId() == null){
            throw new ClubHasNoIdException();
        }

        //Product must has id
        if(clubProduct.getClubProductKey().getProduct().getId() == null){
            throw new ProductHasNoIdException();
        }

        //Check if the club and product both exist
        Optional<Club> existClub = this.clubRepository.findById(clubProduct.getClubProductKey().getClub().getId());
        Optional<Product> existProduct = this.productRepository.findById(clubProduct.getClubProductKey().getProduct().getId());

        //If the club and product both exist, save the clubProduct
        if(existClub.isPresent() && existProduct.isPresent()){
            Optional<ClubProduct> existClubProduct = this.clubProductRepository.findById(clubProduct.getClubProductKey());

            //If the clubProduct already exists, throw exception
            if(existClubProduct.isPresent()){
                throw new ClubProductAlreadyExistException();
            }else{
                existClub.get().getClubProducts().add(clubProduct);
                existProduct.get().getClubProducts().add(clubProduct);

                this.clubRepository.save(existClub.get());
                this.productRepository.save(existProduct.get());

                Optional<ClubProduct> savedClubProduct = this.clubProductRepository.findById(clubProduct.getClubProductKey());
                return savedClubProduct.get();
            }

        //If the club doesn't exist, throw exception
        }else if(!existClub.isPresent()){
            throw new ClubNotFoundException(clubProduct.getClubProductKey().getClub().getId());

        //If the product doesn't exist, throw exception
        }else{
            throw new ProductNotFoundException(clubProduct.getClubProductKey().getProduct().getId());
        }
    }

    @Override
    public ClubProduct editClubProduct(ClubProduct oldClubProduct, ClubProduct newClubProduct) {
        //Check if the ClubProduct needs to be changed exists
        Optional<ClubProduct> existOldClubProduct = this.clubProductRepository.findById(oldClubProduct.getClubProductKey());

        //If the ClubProduct needs to be changed exists
        if(existOldClubProduct.isPresent()){

            //Check if the newClubProduct's club and product both exist
            Optional<Club> existClub = this.clubRepository.findById(newClubProduct.getClubProductKey().getClub().getId());
            Optional<Product> existProduct = this.productRepository.findById(newClubProduct.getClubProductKey().getProduct().getId());

            //If the club and product both exist, edit the clubProduct
            if(existClub.isPresent() && existProduct.isPresent()){
                //Remove the old ClubProduct
                oldClubProduct.getClubProductKey().getClub().getClubProducts().remove(oldClubProduct);
                oldClubProduct.getClubProductKey().getProduct().getClubProducts().remove(oldClubProduct);
                this.clubRepository.save(oldClubProduct.getClubProductKey().getClub());
                this.productRepository.save(oldClubProduct.getClubProductKey().getProduct());
                this.clubProductRepository.deleteById(oldClubProduct.getClubProductKey());

                //Add the new ClubProduct
                existClub.get().getClubProducts().add(newClubProduct);
                existProduct.get().getClubProducts().add(newClubProduct);
                this.clubRepository.save(existClub.get());
                this.productRepository.save(existProduct.get());

                Optional<ClubProduct> savedClubProduct = this.clubProductRepository.findById(newClubProduct.getClubProductKey());
                return savedClubProduct.get();

            //If the club doesn't exist, throw exception
            }else if(!existClub.isPresent()){
                throw new ClubNotFoundException(newClubProduct.getClubProductKey().getClub().getId());

            //If the product doesn't exist, throw exception
            }else{
                throw new ProductNotFoundException(newClubProduct.getClubProductKey().getProduct().getId());
            }

        //If the ClubProduct needs to be changed doesn't exist, throw exception
        }else{
            throw new ClubProductNotFoundException();
        }
    }

    @Override
    public void deleteClubProduct(ClubProduct clubProduct) {
        //Check if the ClubProduct to be deleted exists
        Optional<ClubProduct> existClub = this.clubProductRepository.findById(clubProduct.getClubProductKey());

        //If the ClubProduct to be deleted exists, delete the ClubProduct
        if(existClub.isPresent()){
            //Delete the clubProduct from the club
            clubProduct.getClubProductKey().getClub().getClubProducts().remove(clubProduct);
            this.clubRepository.save(clubProduct.getClubProductKey().getClub());

            //Delete the clubProduct from the product
            clubProduct.getClubProductKey().getProduct().getClubProducts().remove(clubProduct);
            this.productRepository.save(clubProduct.getClubProductKey().getProduct());

            //Remove the clubProduct
            this.clubProductRepository.deleteById(clubProduct.getClubProductKey());

        //If the ClubProduct to be deleted doesn't exist, throw exception
        }else{
            throw new ClubProductNotFoundException();
        }
    }

    @Override
    public void deleteAllClubProduct() {
        List<ClubProduct> clubProductList = this.clubProductRepository.findAll();
        for(ClubProduct clubProduct : clubProductList){
            deleteClubProduct(clubProduct);
        }
    }

    @Override
    public List<ClubProduct> searchClubProduct(Club club, Product product) {
        List<ClubProduct> clubProductList = new ArrayList<>();

        //Search by both club and product
        if(club != null && product != null){

            //Club must has id
            if(club.getId() == null){
                throw new ClubHasNoIdException();
            }

            //Product must has id
            if(product.getId() == null){
                throw new ProductHasNoIdException();
            }

            ClubProductKey clubProductKey = new ClubProductKey(club, product);
            Optional<ClubProduct> existClub = this.clubProductRepository.findById(clubProductKey);
            clubProductList.add(existClub.get());

        //Search only by club
        }else if(club != null){

            //Club must has id
            if(club.getId() == null){
                throw new ClubHasNoIdException();
            }

            clubProductList = this.clubProductRepository.findByClubProductKeyClub(club);

        //Search only by product
        }else{

            //Product must has id
            if(product.getId() == null){
                throw new ProductHasNoIdException();
            }

            clubProductList = this.clubProductRepository.findByClubProductKeyProduct(product);
        }

        if(clubProductList.size() == 0){
            throw new NoResultFoundException();
        }

        return clubProductList;
    }

    @Override
    public List<ClubProduct> getAllClubProduct() {
        List<ClubProduct> clubProductList = this.clubProductRepository.findAll();

        //If there is no records in the table, throw exception
        if(clubProductList.size() == 0){
            throw new TableIsEmptyException();

            //If there is records in the table, return all records
        }else{
            return clubProductList;
        }
    }
}
