package com.sim.shopping.application.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.infrastructure.persistence.entity.ProductDO;
import com.sim.shopping.infrastructure.persistence.entity.SearchHistoryDO;
import com.sim.shopping.infrastructure.persistence.mapper.ProductMapper;
import com.sim.shopping.infrastructure.persistence.mapper.SearchHistoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final ProductMapper productMapper;
    private final SearchHistoryMapper searchHistoryMapper;

    public SearchService(ProductMapper productMapper, SearchHistoryMapper searchHistoryMapper) {
        this.productMapper = productMapper;
        this.searchHistoryMapper = searchHistoryMapper;
    }

    public Map<String, Object> suggest(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Map.of("suggestions", List.of());
        }
        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(ProductDO::getName, keyword)
               .eq(ProductDO::getStatus, "PUBLISHED")
               .last("LIMIT 10");
        List<ProductDO> products = productMapper.selectList(wrapper);
        List<String> suggestions = products.stream()
                .map(ProductDO::getName)
                .distinct()
                .collect(Collectors.toList());
        return Map.of("suggestions", suggestions);
    }

    public Map<String, Object> hotKeywords() {
        LambdaQueryWrapper<SearchHistoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SearchHistoryDO::getSearchedAt)
               .last("LIMIT 100");
        List<SearchHistoryDO> recent = searchHistoryMapper.selectList(wrapper);
        // count frequency and return top 10
        List<String> keywords = recent.stream()
                .collect(Collectors.groupingBy(SearchHistoryDO::getKeyword, Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(e -> e.getKey())
                .collect(Collectors.toList());
        return Map.of("keywords", keywords);
    }
}
