package project.bussiness.service;

import project.model.dto.request.CommentRequest;
import project.model.dto.response.CatalogResponse;
import project.model.dto.response.CommentResponse;
import project.model.entity.CommentBlog;

public interface CommentService extends RootService<CommentBlog,Integer, CommentRequest, CommentResponse>{
    CommentResponse finByIdResponse(int id);
}
