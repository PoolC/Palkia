package org.poolc.api.book.domain;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

public class BookSpecification {
    public static Specification<Book> findByCategoryAndSortOption(BookCategory category, String sortOption) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.equal(root.get("category"), category);
            applySort(root, query, criteriaBuilder, sortOption);
            return predicate;
        };
    }

    public static Specification<Book> findByTitleAndSortOption(String keyword, String sortOption, BookCategory category) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
            predicate = withCategory(root, criteriaBuilder, predicate, category);
            applySort(root, query, criteriaBuilder, sortOption);
            return predicate;
        };
    }

    public static Specification<Book> findByAuthorAndSortOption(String keyword, String sortOption, BookCategory category) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.like(root.get("author"), "%" + keyword + "%");
            predicate = withCategory(root, criteriaBuilder, predicate, category);
            applySort(root, query, criteriaBuilder, sortOption);
            return predicate;
        };
    }

    public static Specification<Book> findByTagsContainingAndSortOption(String keyword, String sortOption, BookCategory category) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.isMember(keyword, root.get("tags"));
            predicate = withCategory(root, criteriaBuilder, predicate, category);
            applySort(root, query, criteriaBuilder, sortOption);
            return predicate;
        };
    }

    private static Predicate withCategory(javax.persistence.criteria.Root<Book> root,
                                          javax.persistence.criteria.CriteriaBuilder criteriaBuilder,
                                          Predicate predicate,
                                          BookCategory category) {
        return category == null ? predicate : criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("category"), category));
    }

    private static void applySort(javax.persistence.criteria.Root<Book> root,
                                  javax.persistence.criteria.CriteriaQuery<?> query,
                                  javax.persistence.criteria.CriteriaBuilder criteriaBuilder,
                                  String sortOption) {
        if ("CREATED_AT".equals(sortOption)) {
            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
        } else if ("RENT_TIME".equals(sortOption)) {
            query.orderBy(criteriaBuilder.desc(root.get("rentDate")));
        } else {
            query.orderBy(criteriaBuilder.asc(root.get("title")));
        }
    }
}
