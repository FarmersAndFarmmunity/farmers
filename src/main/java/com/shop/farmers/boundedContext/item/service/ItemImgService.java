package com.shop.farmers.boundedContext.item.service;

import com.shop.farmers.base.aws.AwsS3Uploader;
import com.shop.farmers.boundedContext.item.entity.ItemImg;
import com.shop.farmers.boundedContext.item.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    @Value("${custom.itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;
    private final AwsS3Uploader awsS3Uploader;  // AwsS3Uploader 의존 추가

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();

        String imgName = "";
        String imgUrl = "";
        String itemImgUrl = "";

        // 파일 업로드
        if (!(oriImgName == null || "".equals(oriImgName))) { // TODO: isEmpty() -> Deprecated
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());

            // 프로필 이미지 업로드를 위한 awsS3Uploader 메서드 호출
            itemImgUrl = awsS3Uploader.uploadImage(itemImgFile, imgName);

            imgUrl = "/images/item/" + imgName;
        }

        // TODO: 추후 itemImg 에 S3 경로 또한 테이블 속성으로 추가 고려
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }


    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile)
        throws Exception {
        if (!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }


    public void deleteItemImg(Long itemImgId) throws Exception {
        ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                .orElseThrow(EntityNotFoundException::new);

        if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
            fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
        }

    }
}
