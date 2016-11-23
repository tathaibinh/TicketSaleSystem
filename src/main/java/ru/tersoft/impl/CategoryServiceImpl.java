package ru.tersoft.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tersoft.entity.Attraction;
import ru.tersoft.entity.Category;
import ru.tersoft.repository.AttractionRepository;
import ru.tersoft.repository.CategoryRepository;
import ru.tersoft.service.CategoryService;

import java.util.List;
import java.util.UUID;

@Service("CategoryService")
@Transactional
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final AttractionRepository attractionRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, AttractionRepository attractionRepository) {
        this.categoryRepository = categoryRepository;
        this.attractionRepository = attractionRepository;
    }

    @Override
    public Iterable<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category get(UUID id) {
        return categoryRepository.findOne(id);
    }

    @Override
    public Category add(String name) {
        if(name != null) {
            Category category = new Category();
            category.setName(name);
            return categoryRepository.saveAndFlush(category);
        }
        else return null;
    }

    @Override
    public Boolean delete(UUID id) {
        Category category = categoryRepository.findOne(id);
        if(category != null) {
            List<Attraction> attractions = (List<Attraction>)attractionRepository.findByCategory(category);
            for(int i = 0; i < attractions.size(); i++) {
                attractions.get(i).setCategory(null);
                attractionRepository.saveAndFlush(attractions.get(i));
            }
            categoryRepository.delete(id);
            return true;
        } else return false;
    }

    @Override
    public Boolean edit(Category category) {
        Category existingCategory = categoryRepository.findOne(category.getId());
        if(existingCategory != null) {
            existingCategory.setName(category.getName());
            categoryRepository.saveAndFlush(existingCategory);
            return true;
        }
        else return false;
    }
}
