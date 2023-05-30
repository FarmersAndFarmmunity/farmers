package com.shop.farmers.service;

import com.shop.farmers.dto.ItemFormDto;
import com.shop.farmers.entity.Item;
import com.shop.farmers.entity.ItemImg;
import com.shop.farmers.repository.ItemImgRepository;
import com.shop.farmers.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto,
                         List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        for (int i = 0; i < itemImgFileList.size(); i++) { // itemImgFileList를 for문을 이용해 순회하여 처리
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if (i == 0) { // 첫번 째 이미지면 "Y"를 아니라면 "N"를 부여
                itemImg.setRepimgYn("Y");
            } else {
                itemImg.setRepimgYn("N");
            }
            // 상품의 이미지 정보를 저장
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }
}
