package com.crestruction.web;

import com.crestruction.web.database.CompositeKeys.ProductSpotKey;
import com.crestruction.web.database.entities.ExhibitionSpot;
import com.crestruction.web.database.entities.Product;
import com.crestruction.web.database.entities.ProductSpot;
import com.crestruction.web.database.exceptions.AlreadyExistExceptions.ProductSpotAlreadyExistException;
import com.crestruction.web.database.exceptions.AttributeEntityIncompleteException.ProductHasNoIdException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.ProductNotFoundException;
import com.crestruction.web.database.services.ExhibitionSpotService;
import com.crestruction.web.database.services.ProductService;
import com.crestruction.web.database.services.ProductSpotService;
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
public class ProductSpotTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ExhibitionSpotService exhibitionSpotService;
    @Autowired
    private ProductSpotService productSpotService;

//    @After
//    public void clear() throws Exception {
//        productSpotService.deleteAllProductSpot();
//    }

    @Test
    public void testAddNewProductSpot(){
        //Save a product
        Product product = new Product(100, "picture1", "name1", "type1");
        Product savedProduct = this.productService.saveProduct(product);
        long productId = savedProduct.getId();

        //Save a exhibitionSpot
        ExhibitionSpot exhibitionSpot = new ExhibitionSpot("size1", "info1");
        ExhibitionSpot savedExhibitionSpot = this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot);
        long exhibitionSpotId = savedExhibitionSpot.getId();

        //Save a productSpot
        ProductSpotKey productSpotKey = new ProductSpotKey(product, exhibitionSpot);
        ProductSpot productSpot = new ProductSpot(productSpotKey);
        this.productSpotService.saveProductSpot(productSpot);

        List<ProductSpot> productSpotList = this.productSpotService.getAllProductSpot();
        assert(productSpotList.size() == 1);
        assert(productSpotList.get(0).getProductSpotKey().getExhibitionSpot().getId().equals(exhibitionSpotId));
        assert(productSpotList.get(0).getProductSpotKey().getProduct().getId().equals(productId));

        //Check the ProductSpot has been added to the exhibitionSpot and product
        ExhibitionSpot exampleExhibitionSpot = new ExhibitionSpot();
        exampleExhibitionSpot.setId(exhibitionSpotId);
        List<ExhibitionSpot> exhibitionSpotList = this.exhibitionSpotService.searchExhibitionSpot(exampleExhibitionSpot);
        assert(exhibitionSpotList.get(0).getProductSpots().size() == 1);

        Product exampleProduct = new Product();
        exampleProduct.setId(productId);
        List<Product> productList = this.productService.searchProduct(exampleProduct);
        assert(productList.get(0).getProductSpots().size() == 1);

        //Save another product
        Product product2 = new Product(100, "picture2", "name2", "type2");
        savedProduct = this.productService.saveProduct(product2);
        long productId2 = savedProduct.getId();

        //Save another exhibitionSpot
        ExhibitionSpot exhibitionSpot2 = new ExhibitionSpot("size2", "info2");
        ExhibitionSpot savedExhibitionSpot2 = this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot2);
        long exhibitionSpotId2 = savedExhibitionSpot2.getId();

        //Save another productSpot
        ProductSpotKey productSpotKey2 = new ProductSpotKey(product2, exhibitionSpot2);
        ProductSpot productSpot2 = new ProductSpot(productSpotKey2);
        this.productSpotService.saveProductSpot(productSpot2);

        productSpotList = this.productSpotService.getAllProductSpot();
        assert(productSpotList.size() == 2);
        assert(productSpotList.get(1).getProductSpotKey().getExhibitionSpot().getId().equals(exhibitionSpotId2));
        assert(productSpotList.get(1).getProductSpotKey().getProduct().getId().equals(productId2));

        //Test add productSpot to a non-existing product
        Product product3 = new Product();
        product3.setId(productId2 + 1);

        ProductSpotKey productSpotKey3 = new ProductSpotKey(product3, exhibitionSpot2);
        ProductSpot productSpot3 = new ProductSpot(productSpotKey3);

        try {
            this.productSpotService.saveProductSpot(productSpot3);
        }catch (Exception  e){
            assert(e instanceof ProductNotFoundException);
        }

        //Test add ClubProduct that is already existed
        Product product4 = new Product();
        product4.setId(productId2);

        ExhibitionSpot exhibitionSpot3 = new ExhibitionSpot();
        exhibitionSpot3.setId(exhibitionSpotId2);

        ProductSpotKey clubProductKey4 = new ProductSpotKey(product4, exhibitionSpot3);
        ProductSpot productSpot4 = new ProductSpot(clubProductKey4);

        try {
            this.productSpotService.saveProductSpot(productSpot4);
        }catch (Exception  e){
            assert(e instanceof ProductSpotAlreadyExistException);
        }

        //Test save clubProduct with club with no id
        Product product5 = new Product();
        ProductSpotKey productSpotKey5 = new ProductSpotKey(product5, exhibitionSpot3);
        ProductSpot productSpot5 = new ProductSpot(productSpotKey5);

        try {
            this.productSpotService.saveProductSpot(productSpot5);
        }catch (Exception  e){
            assert(e instanceof ProductHasNoIdException);
        }
    }

    @Test
    public void testEditProductSpot(){
        //Save a product
        Product product = new Product(100, "picture1", "name1", "type1");
        Product savedProduct = this.productService.saveProduct(product);
        long productId = savedProduct.getId();

        //Save a exhibitionSpot
        ExhibitionSpot exhibitionSpot = new ExhibitionSpot("size1", "info1");
        ExhibitionSpot savedExhibitionSpot = this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot);
        long exhibitionSpotId = savedExhibitionSpot.getId();
        exhibitionSpot.setId(exhibitionSpotId);

        //Save a productSpot
        ProductSpotKey productSpotKey = new ProductSpotKey(product, exhibitionSpot);
        ProductSpot productSpot = new ProductSpot(productSpotKey);
        this.productSpotService.saveProductSpot(productSpot);

        //Edit the product
        savedProduct.setName("name2");
        this.productService.editProduct(savedProduct);

        //Check if the product is changed in the productSpot
        Product exampleProduct = new Product();
        exampleProduct.setId(productId);
        List<ProductSpot> productSpotList = this.productSpotService.searchProductSpot(null, exampleProduct);
        assert(productSpotList.size() == 1);
        assert(productSpotList.get(0).getProductSpotKey().getProduct().getName().equals("name2"));

        //Save another product
        Product product2 = new Product(100, "picture2", "name2", "type2");
        savedProduct = this.productService.saveProduct(product2);
        long productId2 = savedProduct.getId();
        product2.setId(productId2);

        //Edit the saved productSpot
        ProductSpotKey clubProductKey2 = new ProductSpotKey(product2, exhibitionSpot);
        ProductSpot productSpot2 = new ProductSpot(clubProductKey2);
        this.productSpotService.editProductSpot(productSpot, productSpot2);

        productSpotList = this.productSpotService.getAllProductSpot();
        assert(productSpotList.size() == 1);
        assert(productSpotList.get(0).getProductSpotKey().getProduct().getId().equals(product2.getId()));

        //Check the old productSpot has been removed from the old product
        Product exampleProduct2 = new Product();
        exampleProduct.setId(productId);
        List<Product> productList = this.productService.searchProduct(exampleProduct2);
        assert(productList.get(0).getClubProducts().size() == 0);

        //Check the productSpot has changed in the exhibitionSpot
        ExhibitionSpot exampleExhibitionSpot = new ExhibitionSpot();
        exampleExhibitionSpot.setId(exhibitionSpotId);
        List<ExhibitionSpot> exhibitionSpotList = this.exhibitionSpotService.searchExhibitionSpot(exampleExhibitionSpot);
        assert(exhibitionSpotList.get(0).getProductSpots().size() == 1);
        for(ProductSpot productSpotInList : exhibitionSpotList.get(0).getProductSpots()){
            assert(productSpotInList.getProductSpotKey().getProduct().getId().equals(productId2));
        }
    }

    @Test
    public void testSearchProductSpot(){
        //Save a product
        Product product = new Product(100, "picture1", "name1", "type1");
        this.productService.saveProduct(product);

        //Save a exhibitionSpot
        ExhibitionSpot exhibitionSpot = new ExhibitionSpot("size1", "info1");
        this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot);

        //Save a productSpot
        ProductSpotKey productSpotKey = new ProductSpotKey(product, exhibitionSpot);
        ProductSpot productSpot = new ProductSpot(productSpotKey);
        this.productSpotService.saveProductSpot(productSpot);

        //Save another product
        Product product2 = new Product(100, "picture2", "name2", "type2");
        Product savedProduct = this.productService.saveProduct(product2);
        long productId = savedProduct.getId();

        //Save another exhibitionSpot
        ExhibitionSpot exhibitionSpot2 = new ExhibitionSpot("size2", "info2");
        ExhibitionSpot savedExhibitionSpot = this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot2);
        long exhibitionId = savedExhibitionSpot.getId();

        //Save another productSpot
        ProductSpotKey productSpotKey2 = new ProductSpotKey(product2, exhibitionSpot2);
        ProductSpot productSpot2 = new ProductSpot(productSpotKey2);
        this.productSpotService.saveProductSpot(productSpot2);

        //Save the third product
        Product product3 = new Product(100, "picture2", "name3", "type3");
        this.productService.saveProduct(product3);

        //Save third productSpot
        ProductSpotKey productSpotKey3 = new ProductSpotKey(product3, exhibitionSpot2);
        ProductSpot productSpot3 = new ProductSpot(productSpotKey3);
        this.productSpotService.saveProductSpot(productSpot3);

        //Search all ProductSpot belong to exhibitionSpot2
        List<ProductSpot> productSpotList = this.productSpotService.searchProductSpot(exhibitionSpot2, null);
        assert(productSpotList.size() == 2);

        //Search for clubProduct2
        productSpotList = this.productSpotService.searchProductSpot(exhibitionSpot2, product2);
        assert(productSpotList.size() == 1);
        assert(productSpotList.get(0).getProductSpotKey().getProduct().getId().equals(productId));
        assert(productSpotList.get(0).getProductSpotKey().getExhibitionSpot().getId().equals(exhibitionId));
    }

    @Test
    public void testDeleteClubProduct(){
        //Save a product
        Product product = new Product(100, "picture1", "name1", "type1");
        Product savedProduct = this.productService.saveProduct(product);
        long productId = savedProduct.getId();

        //Save a exhibitionSpot
        ExhibitionSpot exhibitionSpot = new ExhibitionSpot("size1", "info1");
        ExhibitionSpot savedExhibitionSpot = this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot);
        long exhibitionId = savedExhibitionSpot.getId();

        //Save a productSpot
        ProductSpotKey productSpotKey = new ProductSpotKey(product, exhibitionSpot);
        ProductSpot productSpot = new ProductSpot(productSpotKey);
        this.productSpotService.saveProductSpot(productSpot);

        //Save another product
        Product product2 = new Product(100, "picture2", "name2", "type2");
        this.productService.saveProduct(product2);

        //Save another exhibitionSpot
        ExhibitionSpot exhibitionSpot2 = new ExhibitionSpot("size2", "info2");
        this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot2);

        //Save another productSpot
        ProductSpotKey productSpotKey2 = new ProductSpotKey(product2, exhibitionSpot2);
        ProductSpot productSpot2 = new ProductSpot(productSpotKey2);
        this.productSpotService.saveProductSpot(productSpot2);

        //Delete clubProduct2
        this.productSpotService.deleteProductSpot(productSpot2);

        //Check there is only 1 clubProduct left
        assert(this.productSpotService.getAllProductSpot().size() == 1);
        assert(this.productSpotService.getAllProductSpot().get(0).getProductSpotKey().getExhibitionSpot().getId().equals(exhibitionId));
        assert(this.productSpotService.getAllProductSpot().get(0).getProductSpotKey().getProduct().getId().equals(productId));

        //Check the clubProduct has been removed from club abd product
        assert(this.exhibitionSpotService.searchExhibitionSpot(exhibitionSpot2).get(0).getProductSpots().size() == 0);
        assert(this.productService.searchProduct(product2).get(0).getProductSpots().size() == 0);
    }

}
