package org.poolc.api.book.domain;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

public class BookSpecification {
    public static Specification<Book> findByTitleAndSortOption(String keyword, String sortOption) {
        return (root, query, criteriaBuilder) -> {
            // 1. WHERE 조건 처리 (keyword 필터링)
            Predicate predicate = criteriaBuilder.like(root.get("title"), "%" + keyword + "%");

            // 2. ORDER BY 처리
            if ("TITLE".equals(sortOption)) {
                query.orderBy(criteriaBuilder.asc(root.get("title")));
            } else if ("CREATED_AT".equals(sortOption)) {
                query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            } else if ("RENT_TIME".equals(sortOption)) {
                query.orderBy(criteriaBuilder.desc(root.get("rentDate")));
            }else{
                query.orderBy(criteriaBuilder.asc(root.get("title")));
            }

            return predicate;
        };
    }

    public static Specification<Book> findByAuthorAndSortOption(String keyword, String sortOption) {
        return (root, query, criteriaBuilder) -> {
            // 1. WHERE 조건 처리 (keyword 필터링)
            Predicate predicate = criteriaBuilder.like(root.get("author"), "%" + keyword + "%");

            // 2. ORDER BY 처리
            if ("TITLE".equals(sortOption)) {
                query.orderBy(criteriaBuilder.asc(root.get("title")));
            } else if ("CREATED_AT".equals(sortOption)) {
                query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            } else if ("RENT_TIME".equals(sortOption)) {
                query.orderBy(criteriaBuilder.desc(root.get("rentDate")));
            }else{
                query.orderBy(criteriaBuilder.asc(root.get("title")));
            }

            return predicate;
        };
    }

    // Tag를 기준으로 검색하는 쿼리도 추가
    public static Specification<Book> findByTagsContainingAndSortOption(String keyword, String sortOption) {
        return (root, query, criteriaBuilder) -> {
            // 1. WHERE 조건 처리 (tags 필터링)
            Predicate predicate = criteriaBuilder.isMember(keyword, root.get("tags"));

            // 2. ORDER BY 처리
            if ("TITLE".equals(sortOption)) {
                query.orderBy(criteriaBuilder.asc(root.get("title")));
            } else if ("CREATED_AT".equals(sortOption)) {
                query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            } else if ("RENT_TIME".equals(sortOption)) {
                query.orderBy(criteriaBuilder.desc(root.get("rentDate")));
            }

            return predicate;
        };
    }
}
