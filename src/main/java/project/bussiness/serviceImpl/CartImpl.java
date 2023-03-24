package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.bussiness.service.CartDetailService;
import project.bussiness.service.CartService;
import project.bussiness.service.FlashSaleService;
import project.model.dto.request.CartDetailRequest;
import project.model.dto.request.CartRequest;
import project.model.dto.response.CartDetailResponse;
import project.model.dto.response.CartResponse;
import project.model.dto.response.ProductReportByBrand;
import project.model.dto.response.ProductReportByCatalog;
import project.model.entity.*;
import project.model.shopMess.Constants;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.*;
import project.security_jwt.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartImpl implements CartService {
    private CartRepository cartRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartDetailRepository cartDetailRepository;
    private CartDetailService cartDetailService;
    private TokenLogInReposirory tokenLogInReposirory;
    private FlashSaleRepository flashSaleRepo;
    private FlashSaleService flashSaleService;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<CartResponse> responses=cartRepository.findAll(pageable).map(this::mapPoJoToResponse);
        Map<String,Object> result= Utility.returnResponse(responses);
        return result;
    }

    @Override
    public CartResponse saveOrUpdate(CartRequest cartRequest) {
        return null;
    }

    @Override
    public CartResponse update(Integer id, CartRequest cartRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        return null;
    }

    @Override
    public List<Cart> findAll() {
        return null;
    }

    @Override
    public List<CartResponse> getAllForClient() {

        return null;
    }

    @Override
    public Cart findById(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Cart mapRequestToPoJo(CartRequest cartRequest) {
        return null;
    }

    @Override
    public CartResponse mapPoJoToResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setName(cart.getName());
        response.setStatus(cart.getStatus());
        List<CartDetailResponse> responseList = new ArrayList<>();
        if (cart.getStatus()==0){
            List<CartDetail> newDetailList = new ArrayList<>();
            flashSaleService.findAll();// cập nhập lại toàn bộ trạng thái flash sale;
            List<CartDetail> detailList = cart.getCartDetailList();
            for (CartDetail dt : detailList) {
                boolean checkFlashSale = flashSaleRepo.existsByStatusAndProduct_Id(1, dt.getProduct().getId());
                List<CartDetail> countCartDetailByProductId = cartDetailRepository.findByProduct_IdAndCart_Id(dt.getProduct().getId(), cart.getId());
                if (checkFlashSale){
                    FlashSale flashSale = flashSaleRepo.findByStatusAndProduct_Id(1, dt.getProduct().getId());
                    // co sale
                    if (countCartDetailByProductId.size()==1){
                        if (!dt.getName().contains(Constants.FLASH_SALE_NAME)&&dt.getQuantity()==1){
                            dt.setName(String.format("%s%s", dt.getName(), Constants.FLASH_SALE_NAME));
                            dt.setPrice(dt.getPrice()*(100-flashSale.getDiscount())/100);
                            cartDetailRepository.save(dt);
                        } else if (!dt.getName().contains(Constants.FLASH_SALE_NAME)) {
                            CartDetail newFlashSale= new CartDetail();
                            newFlashSale.setName(String.format("%s%s", dt.getName(), Constants.FLASH_SALE_NAME));
                            newFlashSale.setPrice(dt.getPrice()*(100-flashSale.getDiscount())/100);
                            newFlashSale.setProduct(dt.getProduct());
                            newFlashSale.setStatus(1);
                            newFlashSale.setCart(cart);
                            newFlashSale.setQuantity(1);
                            dt.setQuantity(dt.getQuantity()-1);
                            cartDetailRepository.save(dt);
                            newDetailList.add(cartDetailRepository.save(newFlashSale));
                        }
                    }
                    newDetailList.add(dt);
                }else {  // khong sale
                        if (dt.getName().contains(Constants.FLASH_SALE_NAME)){
                            if (countCartDetailByProductId.size()==1){
                                dt.setPrice(dt.getProduct().getExportPrice()*(100-dt.getProduct().getDiscount())/100);
                            }
                            cartDetailRepository.delete(dt);
                        }
                        else {
                        newDetailList.add(dt);
                        }
                }
            }
            responseList=newDetailList.stream().map(cartDetail -> {
                CartDetailResponse rp = cartDetailService.mapPoJoToResponse(cartDetail);
                return rp;
            }).collect(Collectors.toList());
        }else {
               responseList = cart.getCartDetailList().stream().map(cartDetail -> {
                CartDetailResponse rp = cartDetailService.mapPoJoToResponse(cartDetail);
                return rp;
            }).collect(Collectors.toList());

        }
        response.setDetailResponses(responseList);
        response.setDiscount(cart.getDiscount());
        response.setShipping(cart.getShipping());
        response.setTax(cart.getTax());
        CustomUserDetails userIsLoggingIn = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userIsLoggingIn.getUsername());
        response.setFirstName(users.getFirstName());
        response.setLastName(users.getLastName());
        response.setEmail(users.getEmail());
        response.setPhone(users.getPhone());
        response.setAddress(users.getAddress());
        response.setCity(users.getCity());
        response.setCountry(users.getCountry());
        response.setState(users.getState());
        return response;
    }

    @Override
    public List<Cart> findByCreatDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return cartRepository.findByCreatDateBetween(startDate, endDate);
    }

    @Override
    public ResponseEntity<?> addToCart(CartDetailRequest cartDetailRequest, String action) {
        CustomUserDetails userIsLoggingIn = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userIsLoggingIn.getUsername());
        if (tokenLogInReposirory.existsByUsers_UserId(users.getUserId())) {
            flashSaleService.findAll();// cập nhập lại toàn bộ trạng thái flash sale;
            Product product = productRepository.findById(cartDetailRequest.getProductId()).get();
            Cart pendingCart = cartRepository.findByUsers_UserIdAndStatus(users.getUserId(), 0).get(0);
            boolean checkFlashSale = flashSaleRepo.existsByStatusAndProduct_Id(1, product.getId());
            List<CartDetail> cartDetail = cartDetailRepository.findByProduct_IdAndCart_Id(product.getId(), pendingCart.getId());
            CartDetail newDetail = new CartDetail();
            newDetail.setProduct(product);
            newDetail.setCart(pendingCart);
            newDetail.setStatus(1);
            if (checkFlashSale) {
                FlashSale flashSale = flashSaleRepo.findByStatusAndProduct_Id(1, product.getId());
                List<Cart> boughtCart = cartRepository.findByStatusAndUsers_UserIdAndCreatDateBetween(1, users.getUserId(), flashSale.getStartTime(), flashSale.getEndTime());
                List<CartDetail> boughtDetail = cartDetailRepository.findByCartIn(boughtCart);
                Product checkBought = productRepository.findByIdAndCartDetailListIn(product.getId(), boughtDetail);
                if (checkBought != null) {
                    newDetail.setQuantity(cartDetailRequest.getQuantity());
                    newDetail.setPrice(product.getExportPrice()*(100- product.getDiscount())/100);
                    newDetail.setName(product.getName());
                    newDetail.setDiscount(product.getDiscount()*product.getExportPrice()/100);
                    cartDetailRepository.save(newDetail);
                    return ResponseEntity.ok().body(Message.ADD_TO_CART_SUCCESS);
                } else {
                    if (cartDetail.size() == 1) {
                        if (cartDetail.get(0).getName().contains(Constants.FLASH_SALE_NAME)) {// sản phẩm đã được thêm LẦN ĐẦU TIÊN TRONG thời gian bắt đầu sale -> tạo 1 oderDetail giá thường
                            newDetail.setQuantity(cartDetailRequest.getQuantity());
                            newDetail.setPrice(product.getExportPrice()*(100- product.getDiscount())/100);
                            newDetail.setName(product.getName());
                            newDetail.setDiscount(product.getDiscount()*product.getExportPrice()/100);

                        } else {// sản phẩm đã được thêm vào giỏ hàng TRƯỚC thời gian diễn ra sale.-> tạo 1 oderDetail với giá sale
                            newDetail.setQuantity(1);
                            newDetail.setPrice(product.getExportPrice() * (100 - flashSale.getDiscount()) / 100);
                            newDetail.setDiscount(product.getExportPrice()* flashSale.getDiscount()/100);
                            newDetail.setName(String.format("%s%s", product.getName(), Constants.FLASH_SALE_NAME));
                        }
                    } else if (cartDetail.size() == 2) { // kich thuoc list cartdetal theo san pham sale = 2(gom ca oderdetail sale và oderDetail thuong)
                        newDetail = cartDetail.stream().filter(dt -> !dt.getName().contains(Constants.FLASH_SALE_NAME)).collect(Collectors.toList()).get(0);
                        if (action.equals("create")) {
                            newDetail.setQuantity(newDetail.getQuantity() + cartDetailRequest.getQuantity());
                        } else if (action.equals("edit")) {
                            newDetail.setQuantity(cartDetailRequest.getQuantity());
                        } else {
                            return ResponseEntity.badRequest().body(Message.ERROR_400);
                        }
                    } else {
                        newDetail.setQuantity(1);
                        newDetail.setPrice(product.getExportPrice() * (100 - flashSale.getDiscount()) / 100);
                        newDetail.setDiscount(product.getExportPrice()* flashSale.getDiscount()/100);
                        newDetail.setName(String.format("%s%s", product.getName(), Constants.FLASH_SALE_NAME));
                    }
                    cartDetailRepository.save(newDetail);
                    return ResponseEntity.ok().body(Message.ADD_TO_CART_SUCCESS);
                }
            } else {
                if (cartDetail.size() != 0) {
                    if (action.equals("create")) {
                        cartDetail.get(0).setQuantity(cartDetail.get(0).getQuantity() + cartDetailRequest.getQuantity());
                        cartDetail.get(0).setPrice(product.getExportPrice()*(100- product.getDiscount())/100);
                    } else if (action.equals("edit")) {
                        cartDetail.get(0).setQuantity(cartDetailRequest.getQuantity());
                    }
                    cartDetailRepository.save(cartDetail.get(0));
                    return ResponseEntity.ok().body(Message.ADD_TO_CART_SUCCESS);
                } else {
                    newDetail.setDiscount(product.getDiscount()*product.getExportPrice()/100);
                    newDetail.setQuantity(cartDetailRequest.getQuantity());
                    newDetail.setPrice(product.getExportPrice()*(100- product.getDiscount())/100);
                    newDetail.setName(product.getName());
                    cartDetailRepository.save(newDetail);
                    return ResponseEntity.ok().body(Message.ADD_TO_CART_SUCCESS);
                }
            }
        } else {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @Override
    public Page<CartResponse> findByStatusIn(Integer status, Pageable pageable) {
        return null;
    }

    @Override
    public CartResponse showCartPending() {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Integer> arrInt = new ArrayList<>();
        arrInt.add(0);
        CartResponse response = cartRepository.findByStatusInAndUsers_UserId(arrInt, customUser.getUserId())
                .stream().map(this::mapPoJoToResponse)
                .collect(Collectors.toList())
                .get(0);
        return response;
    }

    @Override
    public Map<String, Object> getAllForClient(Pageable pageable) {
        CustomUserDetails customUser= (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<CartResponse> responses=cartRepository.findByUsers_UserIdAndStatusIsNot(customUser.getUserId(),0,pageable).map(this::mapPoJoToResponse);
        Map<String,Object> result=Utility.returnResponse(responses);
        return result;
    }

    @Override
    public ResponseEntity<?> changeStatus(Integer cartId, Integer status) {
        try {
            Cart cart=cartRepository.findById(cartId).get();
            if(cart!=null&&cart.getStatus()>Constants.CART_STATUS_PENDING&&cart.getStatus()<Constants.CART_STATUS_DONE){
                cart.setStatus(status);
                cartRepository.save(cart);
                return ResponseEntity.ok(Message.SUCCESS);
            }else {
                return ResponseEntity.badRequest().body(Message.ERROR_400);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }






}
