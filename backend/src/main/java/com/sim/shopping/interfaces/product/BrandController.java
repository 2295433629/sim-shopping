package com.sim.shopping.interfaces.product;

import com.sim.shopping.application.product.BrandService;
import com.sim.shopping.infrastructure.persistence.entity.BrandDO;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品牌管理控制器，处理品牌信息的增删改查
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    /**
     * 获取Public Brands
     * @return 返回结果
     */
    @GetMapping("/public/brands")
    public ApiResponse<List<BrandDO>> getPublicBrands() {
        return ApiResponse.success(brandService.getBrands());
    }

    /**
     * 获取Admin Brands
     * @return 返回结果
     */
    @GetMapping("/admin/brands")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<BrandDO>> getAdminBrands() {
        return ApiResponse.success(brandService.getBrands());
    }

    /**
     * 创建品牌
     * @param brand brand
     * @return 返回结果
     */
    @PostMapping("/admin/brands")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BrandDO> createBrand(@Valid @RequestBody BrandDO brand) {
        return ApiResponse.success(brandService.createBrand(brand));
    }

    /**
     * 更新品牌
     * @return 返回结果
     */
    @PutMapping("/admin/brands/{brandId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BrandDO> updateBrand(@PathVariable Long brandId,
                                             @Valid @RequestBody BrandDO brand) {
        return ApiResponse.success(brandService.updateBrand(brandId, brand));
    }

    /**
     * 删除品牌
     * @param brandId brandId
     * @return 返回结果
     */
    @DeleteMapping("/admin/brands/{brandId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteBrand(@PathVariable Long brandId) {
        brandService.deleteBrand(brandId);
        return ApiResponse.success();
    }
}
