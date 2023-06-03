package com.shop.farmers.dto;

import com.shop.farmers.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper; //멤버 변수가 많아졌을 때 도와주는 라이브러리

@Getter
@Setter
public class ItemImgDto {

    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class); //ItemImg 객체의 자료형과 멤버변수의 이름이 같을 때 ItemImgDto로 값 복사
    }
}
