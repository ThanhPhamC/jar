package project.bussiness.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.dto.request.BlogRequest;
import project.model.dto.response.BlogResponse;
import project.model.entity.Blog;

import java.util.List;

public interface BlogService extends RootService<Blog,Integer, BlogRequest, BlogResponse>{
    List<BlogResponse> getTopNew();
    BlogResponse getBlogResponseForClient(int blogId);
    List<BlogResponse> getRelatedBlog(int catId);
    List<BlogResponse> searchByCatalogAndTag (List<Integer> listCatalogId, List<Integer> listTagId);

}
