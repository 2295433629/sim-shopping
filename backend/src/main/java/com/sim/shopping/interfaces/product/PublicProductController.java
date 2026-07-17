package com.sim.shopping.interfaces.product;

import com.sim.shopping.application.product.ProductService;
import com.sim.shopping.application.product.SearchService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.product.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品公开浏览控制器，提供商品列表、详情、搜索等公开查询接口
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/public/products")
public class PublicProductController {

    private final ProductService productService;
    private final SearchService searchService;

    public PublicProductController(ProductService productService, SearchService searchService) {
        this.productService = productService;
        this.searchService = searchService;
    }

    /**
     * 查询公开商品列表
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<ProductCardVO>> getPublicProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sort) {
        return ApiResponse.success(productService.getPublicProducts(page, size, categoryId, keyword, minPrice, maxPrice, sort));
    }

    /**
     * 获取Public Product Detail
     * @param productId productId
     * @return 返回结果
     */
    @GetMapping("/{productId}")
    public ApiResponse<ProductDetailVO> getPublicProductDetail(@PathVariable Long productId) {
        return ApiResponse.success(productService.getPublicProductDetail(productId));
    }

    /**
     * 搜索商品（关键词匹配）
     * @return 返回结果
     */
    @GetMapping("/search")
    public ApiResponse<PageResponse<ProductCardVO>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sort) {
        return ApiResponse.success(productService.searchProducts(keyword, page, size, categoryId, minPrice, maxPrice, sort));
    }

    /**
     * 获取Recommend Products
     * @return 返回结果
     */
    @GetMapping("/recommend")
    public ApiResponse<List<ProductCardVO>> getRecommendProducts(
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(productService.getRecommendProducts(size));
    }

    /**
     * 获取Hot Products
     * @return 返回结果
     */
    @GetMapping("/hot")
    public ApiResponse<List<ProductCardVO>> getHotProducts(
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(productService.getHotProducts(size));
    }

    /**
     * 获取New Products
     * @return 返回结果
     */
    @GetMapping("/new")
    public ApiResponse<List<ProductCardVO>> getNewProducts(
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(productService.getNewProducts(size));
    }

    /**
     * 获取Products By Shop
     * @return 返回结果
     */
    @GetMapping("/shop/{shopId}")
    public ApiResponse<PageResponse<ProductCardVO>> getProductsByShop(
            @PathVariable Long shopId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort) {
        return ApiResponse.success(productService.getProductsByShop(shopId, page, size, sort));
    }

    /**
     * suggest
     * @param keyword keyword
     * @return 返回结果
     */
    @GetMapping("/search/suggest")
    public ApiResponse<Map<String, Object>> suggest(@RequestParam String keyword) {
        return ApiResponse.success(searchService.suggest(keyword));
    }

    /**
     * hot Keywords
     * @return 返回结果
     */
    @GetMapping("/search/hot")
    public ApiResponse<Map<String, Object>> hotKeywords() {
        return ApiResponse.success(searchService.hotKeywords());
    }
}
