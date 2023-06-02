package com.shop.farmers.service;

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

import java.util.Optional;

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
}

