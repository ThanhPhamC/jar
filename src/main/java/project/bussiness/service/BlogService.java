package project.bussiness.service;

import project.model.dto.request.BlogRequest;
import project.model.dto.response.BlogResponse;
import project.model.entity.Blog;

import java.util.List;

public interface BlogService extends RootService<Blog,Integer, BlogRequest, BlogResponse>{
    List<BlogResponse> getTopNew();
}
