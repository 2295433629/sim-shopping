package com.sim.shopping.interfaces.product;

import com.sim.shopping.application.product.BrandService;
import com.sim.shopping.infrastructure.persistence.entity.BrandDO;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/public/brands")
    public ApiResponse<List<BrandDO>> getPublicBrands() {
        return ApiResponse.success(brandService.getBrands());
    }

    @GetMapping("/admin/brands")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<BrandDO>> getAdminBrands() {
        return ApiResponse.success(brandService.getBrands());
    }

    @PostMapping("/admin/brands")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BrandDO> createBrand(@Valid @RequestBody BrandDO brand) {
        return ApiResponse.success(brandService.createBrand(brand));
    }

    @PutMapping("/admin/brands/{brandId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BrandDO> updateBrand(@PathVariable Long brandId,
                                             @Valid @RequestBody BrandDO brand) {
        return ApiResponse.success(brandService.updateBrand(brandId, brand));
    }

    @DeleteMapping("/admin/brands/{brandId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteBrand(@PathVariable Long brandId) {
        brandService.deleteBrand(brandId);
        return ApiResponse.success();
    }
}
