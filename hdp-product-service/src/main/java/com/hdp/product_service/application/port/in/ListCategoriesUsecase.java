package com.hdp.product_service.application.port.in;


import com.hdp.core.usecase.Usecase;

public interface ListCategoriesUsecase extends Usecase<ListCategoriesUsecase.Command, ListCategoriesUsecase.Result> {

    record Command(boolean treeStructure) {}

    record Result(java.util.List<CreateCategoryUsecase.Result> categories) {}
}