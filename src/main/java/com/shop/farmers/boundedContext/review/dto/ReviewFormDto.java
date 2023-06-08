package com.shop.farmers.boundedContext.review.dto;

import com.shop.farmers.boundedContext.item.constant.ItemSellStatus;
import com.shop.farmers.boundedContext.item.dto.ItemImgDto;
import com.shop.farmers.boundedContext.item.entity.Item;
import com.shop.farmers.boundedContext.member.entity.Member;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ReviewFormDto {

    private Long id;

    @Length(min=20, max=200, message = "리뷰는 최소 20자 이상 입력하셔야 합니다.")
    @NotBlank(message = "리뷰 내용은 필수 입력 값입니다.")
    private String contents;

}
