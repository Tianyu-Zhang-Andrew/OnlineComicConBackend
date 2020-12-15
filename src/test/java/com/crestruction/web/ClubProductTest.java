package com.crestruction.web;

import com.crestruction.web.database.CompositeKeys.ClubProductKey;
import com.crestruction.web.database.entities.Club;
import com.crestruction.web.database.entities.ClubProduct;
import com.crestruction.web.database.entities.Product;
import com.crestruction.web.database.exceptions.AlreadyExistExceptions.ClubProductAlreadyExistException;
import com.crestruction.web.database.exceptions.AttributeEntityIncompleteException.ClubHasNoIdException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.ClubNotFoundException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.NoResultFoundException;
import com.crestruction.web.database.services.ClubProductService;
import com.crestruction.web.database.services.ClubService;
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
public class ClubProductTest {
    @Autowired
    private ClubService clubService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ClubProductService clubProductService;

//    @After
//    public void clear() throws Exception {
//        clubProductService.deleteAllClubProduct();
//    }

    @Test
    public void testAddNewClubProduct(){
        //Save a product
        Product product = new Product(100, "picture1", "name1", "type1");
        Product savedProduct = this.productService.saveProduct(product);
        long productId = savedProduct.getId();

        //Save a club
        Club club = new Club("club1", "type1", "mainPageURL1", "logoURL1");
        Club savedClub = this.clubService.saveClub(club);
        long clubId = savedClub.getId();

        //Save a clubProduct (ids are already set to club and product entity during the saving process)
        ClubProductKey clubProductKey = new ClubProductKey(club, product);
        ClubProduct clubProduct = new ClubProduct(clubProductKey);
        this.clubProductService.saveClubProduct(clubProduct);

        List<ClubProduct> clubProductList = this.clubProductService.getAllClubProduct();
        assert(clubProductList.size() == 1);
        assert(clubProductList.get(0).getClubProductKey().getClub().getId().equals(clubId));
        assert(clubProductList.get(0).getClubProductKey().getProduct().getId().equals(productId));

        //Check the clubProduct has been added to the club and product
        Club exampleClub = new Club();
        exampleClub.setId(clubId);
        List<Club> clubList = this.clubService.searchClub(exampleClub);
        assert(clubList.get(0).getClubProducts().size() == 1);

        Product exampleProduct = new Product();
        exampleProduct.setId(productId);
        List<Product> productList = this.productService.searchProduct(exampleProduct);
        assert(productList.get(0).getClubProducts().size() == 1);

        //Save another product
        Product product2 = new Product(100, "picture2", "name2", "type2");
        savedProduct = this.productService.saveProduct(product2);
        long productId2 = savedProduct.getId();

        //Save another club
        Club club2 = new Club("club2", "type2", "mainPageURL2", "logoURL2");
        savedClub = this.clubService.saveClub(club2);
        long clubId2 = savedClub.getId();

        //Save another clubProduct
        ClubProductKey clubProductKey2 = new ClubProductKey(club2, product2);
        ClubProduct clubProduct2 = new ClubProduct(clubProductKey2);
        this.clubProductService.saveClubProduct(clubProduct2);

        clubProductList = this.clubProductService.getAllClubProduct();
        assert(clubProductList.size() == 2);
        assert(clubProductList.get(1).getClubProductKey().getClub().getId().equals(clubId2));
        assert(clubProductList.get(1).getClubProductKey().getProduct().getId().equals(productId2));

        //Test add ClubProduct to a non-existing club
        Club club3 = new Club();
        club3.setId(clubId2 + 1);

        ClubProductKey clubProductKey3 = new ClubProductKey(club3, product2);
        ClubProduct clubProduct3 = new ClubProduct(clubProductKey3);

        try {
            this.clubProductService.saveClubProduct(clubProduct3);
        }catch (Exception  e){
            assert(e instanceof ClubNotFoundException);
        }

        //Test add ClubProduct that is already existed
        Club club4 = new Club();
        club4.setId(clubId);

        Product product3 = new Product();
        product3.setId(productId);

        ClubProductKey clubProductKey4 = new ClubProductKey(club4, product3);
        ClubProduct clubProduct4 = new ClubProduct(clubProductKey4);

        try {
            this.clubProductService.saveClubProduct(clubProduct4);
        }catch (Exception  e){
            assert(e instanceof ClubProductAlreadyExistException);
        }

        //Test save clubProduct with club with no id
        Club club5 = new Club();
        ClubProductKey clubProductKey5 = new ClubProductKey(club5, product3);
        ClubProduct clubProduct5 = new ClubProduct(clubProductKey5);

        try {
            this.clubProductService.saveClubProduct(clubProduct5);
        }catch (Exception  e){
            assert(e instanceof ClubHasNoIdException);
        }
    }

    @Test
    public void testEditClubProduct(){
        //Save a product
        Product product = new Product(100, "picture1", "name1", "type1");
        Product savedProduct = this.productService.saveProduct(product);
        long productId = savedProduct.getId();

        //Save a club
        Club club = new Club("club1", "type1", "mainPageURL1", "logoURL1");
        Club savedClub = this.clubService.saveClub(club);
        long clubId = savedClub.getId();

        //Save a clubProduct
        ClubProductKey clubProductKey = new ClubProductKey(club, product);
        ClubProduct clubProduct = new ClubProduct(clubProductKey);
        this.clubProductService.saveClubProduct(clubProduct);

        //Edit the Club
        savedClub.setName("club2");
        this.clubService.editClub(savedClub);

        //Check if the club is changed in the clubProduct
        Club exampleClub = new Club();
        exampleClub.setId(clubId);
        List<ClubProduct> clubProductList = this.clubProductService.searchClubProduct(exampleClub, null);
        assert(clubProductList.size() == 1);
        assert(clubProductList.get(0).getClubProductKey().getClub().getName().equals("club2"));

        //Save another product
        Product product2 = new Product(100, "picture2", "name2", "type2");
        savedProduct = this.productService.saveProduct(product2);
        long productId2 = savedProduct.getId();

        //Edit the saved clubProduct
        ClubProductKey clubProductKey2 = new ClubProductKey(club, product2);
        ClubProduct clubProduct2 = new ClubProduct(clubProductKey2);
        this.clubProductService.editClubProduct(clubProduct, clubProduct2);

        clubProductList = this.clubProductService.getAllClubProduct();
        assert(clubProductList.size() == 1);
        assert(clubProductList.get(0).getClubProductKey().getProduct().getId().equals(product2.getId()));

        //Check the old ClubProduct has been removed from the old product
        Product exampleProduct = new Product();
        exampleProduct.setId(productId);
        List<Product> productList = this.productService.searchProduct(exampleProduct);
        assert(productList.get(0).getClubProducts().size() == 0);

        //Check the ClubProduct has changed in the club
        List<Club> clubList = this.clubService.searchClub(exampleClub);
        assert(clubList.get(0).getClubProducts().size() == 1);
        for(ClubProduct clubProductInList : clubList.get(0).getClubProducts()){
            assert(clubProductInList.getClubProductKey().getProduct().getId().equals(productId2));
        }
    }

    @Test
    public void testSearchClubProduct(){
        //Save a product
        Product product = new Product(100, "picture1", "name1", "type1");
        Product savedProduct = this.productService.saveProduct(product);
        savedProduct.getId();

        //Save a club
        Club club = new Club("club1", "type1", "mainPageURL1", "logoURL1");
        Club savedClub = this.clubService.saveClub(club);
        savedClub.getId();

        //Save a clubProduct (ids are already set to club and product entity during the saving process)
        ClubProductKey clubProductKey = new ClubProductKey(club, product);
        ClubProduct clubProduct = new ClubProduct(clubProductKey);
        this.clubProductService.saveClubProduct(clubProduct);

        //Save another product
        Product product2 = new Product(100, "picture2", "name2", "type2");
        savedProduct = this.productService.saveProduct(product2);
        long productId2 = savedProduct.getId();

        //Save another club
        Club club2 = new Club("club2", "type2", "mainPageURL2", "logoURL2");
        savedClub = this.clubService.saveClub(club2);
        long clubId2 = savedClub.getId();

        //Save another clubProduct
        ClubProductKey clubProductKey2 = new ClubProductKey(club2, product2);
        ClubProduct clubProduct2 = new ClubProduct(clubProductKey2);
        this.clubProductService.saveClubProduct(clubProduct2);

        //Save the third product
        Product product3 = new Product(100, "picture3", "name3", "type3");
        this.productService.saveProduct(product3);

        //Save another clubProduct
        ClubProductKey clubProductKey3 = new ClubProductKey(club2, product3);
        ClubProduct clubProduct3 = new ClubProduct(clubProductKey3);
        this.clubProductService.saveClubProduct(clubProduct3);

        //Search all ClubProduct belong to club2
        List<ClubProduct> clubProductList = this.clubProductService.searchClubProduct(club2, null);
        assert(clubProductList.size() == 2);

        //Search for clubProduct2
        clubProductList = this.clubProductService.searchClubProduct(club2, product2);
        assert(clubProductList.size() == 1);
        assert(clubProductList.get(0).getClubProductKey().getProduct().getId().equals(productId2));
        assert(clubProductList.get(0).getClubProductKey().getClub().getId().equals(clubId2));
    }

    @Test
    public void testDeleteClubProduct(){
        //Save a product
        Product product = new Product(100, "picture1", "name1", "type1");
        Product savedProduct = this.productService.saveProduct(product);
        long productId = savedProduct.getId();

        //Save a club
        Club club = new Club("club1", "type1", "mainPageURL1", "logoURL1");
        Club savedClub = this.clubService.saveClub(club);
        long clubId = savedClub.getId();

        //Save a clubProduct (ids are already set to club and product entity during the saving process)
        ClubProductKey clubProductKey = new ClubProductKey(club, product);
        ClubProduct clubProduct = new ClubProduct(clubProductKey);
        this.clubProductService.saveClubProduct(clubProduct);

        //Save another product
        Product product2 = new Product(100, "picture2", "name2", "type2");
        this.productService.saveProduct(product2);

        //Save another club
        Club club2 = new Club("club2", "type2", "mainPageURL2", "logoURL2");
        this.clubService.saveClub(club2);

        //Save another clubProduct
        ClubProductKey clubProductKey2 = new ClubProductKey(club2, product2);
        ClubProduct clubProduct2 = new ClubProduct(clubProductKey2);
        this.clubProductService.saveClubProduct(clubProduct2);

        //Delete clubProduct2
        this.clubProductService.deleteClubProduct(clubProduct2);

        //Check there is only 1 clubProduct left
        assert(this.clubProductService.getAllClubProduct().size() == 1);
        assert(this.clubProductService.getAllClubProduct().get(0).getClubProductKey().getClub().getId().equals(clubId));
        assert(this.clubProductService.getAllClubProduct().get(0).getClubProductKey().getProduct().getId().equals(productId));

        //Check the clubProduct has been removed from club abd product
        assert(this.clubService.searchClub(club2).get(0).getClubProducts().size() == 0);
        assert(this.productService.searchProduct(product2).get(0).getClubProducts().size() == 0);
    }
}
