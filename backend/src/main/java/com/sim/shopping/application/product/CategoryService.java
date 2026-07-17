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

/**
 * 分类服务，处理商品分类的增删改查和层级关系维护
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    public CategoryService(CategoryMapper categoryMapper, ProductMapper productMapper) {
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    /**
     * 查询分类树
     * @return 返回结果
     */
    public List<CategoryNode> getCategoryTree() {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(CategoryDO::getSortOrder);
        List<CategoryDO> all = categoryMapper.selectList(wrapper);
        return buildTree(all, 0L);
    }

    /**
     * 创建分类
     * @param category category
     * @return 返回结果
     */
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

    /**
     * 更新分类
     * @param id id
     * @param category category
     * @return 返回结果
     */
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

    /**
     * 删除分类
     * @param id id
     */
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

        /** 获取Id */
        public Long getId() { return this.id; }
        /** set Id */
        public void setId(Long id) { this.id = id; }
        /**
         * 获取Parent Id
         * @return 返回结果
         */
        public Long getParentId() { return this.parentId; }
        /**
         * set Parent Id
         * @param parentId parentId
         */
        public void setParentId(Long parentId) { this.parentId = parentId; }
        /** 获取Name */
        public String getName() { return this.name; }
        /** set Name */
        public void setName(String name) { this.name = name; }
        /**
         * 获取Icon
         * @return 返回结果
         */
        public String getIcon() { return this.icon; }
        /**
         * set Icon
         * @param icon icon
         */
        public void setIcon(String icon) { this.icon = icon; }
        /**
         * 获取Sort Order
         * @return 返回结果
         */
        public Integer getSortOrder() { return this.sortOrder; }
        /**
         * set Sort Order
         * @param sortOrder sortOrder
         */
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
        /**
         * 获取Status
         * @return 返回结果
         */
        public String getStatus() { return this.status; }
        /**
         * set Status
         * @param status status
         */
        public void setStatus(String status) { this.status = status; }
        /**
         * 获取Children
         * @return 返回结果
         */
        public List<CategoryNode> getChildren() { return this.children; }
        /**
         * set Children
         * @param children children
         */
        public void setChildren(List<CategoryNode> children) { this.children = children; }
    }
}
