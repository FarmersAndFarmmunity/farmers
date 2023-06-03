package com.shop.farmers.service;

import com.shop.farmers.dto.CartDetailDto;
import com.shop.farmers.dto.CartItemDto;
import com.shop.farmers.entity.Cart;
import com.shop.farmers.entity.CartItem;
import com.shop.farmers.entity.Item;
import com.shop.farmers.entity.Member;
import com.shop.farmers.repository.CartItemRepository;
import com.shop.farmers.repository.CartRepository;
import com.shop.farmers.repository.ItemRepository;
import com.shop.farmers.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    // 장바구니 담기 로직
    public Long addCart(CartItemDto cartItemDto, String email) {
        // 먼저 장바구니에 담을 아이템이 있는지 조회
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email); // 현재 로그인한 멤버

        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null) { // 일단 장바구니가 없으면 하나 만들기
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // 상품이 장바구니에 들어가 있는지 조회
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (savedCartItem != null) { // 장바구니가 비어 있지 않으면
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());

            cartItemRepository.save(cartItem);

            return cartItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) { // 장바구니 목록을 가져옴

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>(); // 빈 장바구니 리스트를 생성

        Member member = memberRepository.findByEmail(email); // 현재 로그인한 멤버 정보를 가져옴
        Cart cart = cartRepository.findByMemberId(member.getId()); // 현재 멤버의 아이디 정보와 일치하는 장바구니를 조회

        if (cart == null) { // 없으면 빈 리스트를 반환
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {

        Member curMember = memberRepository.findByEmail(email); // 현재 로그인한 사용자

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        Member savedMember = cartItem.getCart().getMember();

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) { // 현재 멤버와 cartItem 의 멤버와 일치하는지
            return false;
        }

        return true;
    }

    // cartItem 의 수량도 함께 업데이트
    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    // 장바구니 목록 삭제
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        cartItemRepository.delete(cartItem);
    }
}

