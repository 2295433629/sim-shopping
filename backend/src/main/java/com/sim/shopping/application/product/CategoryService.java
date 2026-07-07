package com.sim.shopping.application.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.domain.common.exception.ProductException;
import com.sim.shopping.infrastructure.persistence.entity.CategoryDO;
import com.sim.shopping.infrastructure.persistence.entity.ProductDO;
import com.sim.shopping.infrastructure.persistence.mapper.CategoryMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    public CategoryService(CategoryMapper categoryMapper, ProductMapper productMapper) {
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    public List<CategoryNode> getCategoryTree() {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(CategoryDO::getSortOrder);
        List<CategoryDO> all = categoryMapper.selectList(wrapper);
        return buildTree(all, 0L);
    }

    @Transactional
    public CategoryDO createCategory(CategoryDO category) {
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        if (category.getStatus() == null) {
            category.setStatus("ENABLED");
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        categoryMapper.insert(category);
        return category;
    }

    @Transactional
    public CategoryDO updateCategory(Long id, CategoryDO category) {
        CategoryDO existing = categoryMapper.selectById(id);
        if (existing == null) {
            throw new ProductException.CategoryNotFoundException("分类不存在");
        }
        if (category.getName() != null) {
            existing.setName(category.getName());
        }
        if (category.getIcon() != null) {
            existing.setIcon(category.getIcon());
        }
        if (category.getSortOrder() != null) {
            existing.setSortOrder(category.getSortOrder());
        }
        if (category.getStatus() != null) {
            existing.setStatus(category.getStatus());
        }
        if (category.getParentId() != null) {
            existing.setParentId(category.getParentId());
        }
        categoryMapper.updateById(existing);
        return existing;
    }

    @Transactional
    public void deleteCategory(Long id) {
        CategoryDO existing = categoryMapper.selectById(id);
        if (existing == null) {
            throw new ProductException.CategoryNotFoundException("分类不存在");
        }
        // check children
        LambdaQueryWrapper<CategoryDO> childWrapper = new LambdaQueryWrapper<>();
        childWrapper.eq(CategoryDO::getParentId, id);
        Long childCount = categoryMapper.selectCount(childWrapper);
        if (childCount > 0) {
            throw new BusinessException(400, "该分类下有子分类，无法删除");
        }
        // check products
        LambdaQueryWrapper<ProductDO> productWrapper = new LambdaQueryWrapper<>();
        productWrapper.eq(ProductDO::getCategoryId, id);
        Long productCount = productMapper.selectCount(productWrapper);
        if (productCount > 0) {
            throw new BusinessException(400, "该分类下有商品，无法删除");
        }
        categoryMapper.deleteById(id);
    }

    private List<CategoryNode> buildTree(List<CategoryDO> all, Long parentId) {
        return all.stream()
                .filter(c -> parentId.equals(c.getParentId()))
                .map(c -> {
                    CategoryNode node = new CategoryNode();
                    node.setId(c.getId());
                    node.setParentId(c.getParentId());
                    node.setName(c.getName());
                    node.setIcon(c.getIcon());
                    node.setSortOrder(c.getSortOrder());
                    node.setStatus(c.getStatus());
                    node.setChildren(buildTree(all, c.getId()));
                    return node;
                })
                .collect(Collectors.toList());
    }

    public static class CategoryNode {
        private Long id;
        private Long parentId;
        private String name;
        private String icon;
        private Integer sortOrder;
        private String status;
        private List<CategoryNode> children = new ArrayList<>();

        public Long getId() { return this.id; }
        public void setId(Long id) { this.id = id; }
        public Long getParentId() { return this.parentId; }
        public void setParentId(Long parentId) { this.parentId = parentId; }
        public String getName() { return this.name; }
        public void setName(String name) { this.name = name; }
        public String getIcon() { return this.icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public Integer getSortOrder() { return this.sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
        public String getStatus() { return this.status; }
        public void setStatus(String status) { this.status = status; }
        public List<CategoryNode> getChildren() { return this.children; }
        public void setChildren(List<CategoryNode> children) { this.children = children; }
    }
}
