package com.scaler.blogapp.articles;

import com.scaler.blogapp.articles.dtos.CreateArticleRequest;
import com.scaler.blogapp.articles.dtos.UpdateArticleRequest;
import com.scaler.blogapp.users.UsersRepository;
import com.scaler.blogapp.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticlesService {

    private final ArticlesRepository articlesRepository;
    private final UsersRepository usersRepository;

    public ArticlesService(ArticlesRepository articlesRepository, UsersRepository usersRepository) {
        this.articlesRepository = articlesRepository;
        this.usersRepository = usersRepository;
    }

    public Iterable<ArticleEntity> getAllArticles() {
        return articlesRepository.findAll();
    }

    public ArticleEntity getArticleBySlug(String slug) {
        var article = articlesRepository.findBySlug(slug);
        if (article == null)
            throw new ArticleNotFoundException(slug);
        return article;
    }

    public ArticleEntity createArticle(CreateArticleRequest articleRequest, Long authorId) {
        var author = usersRepository.findById(authorId).orElseThrow(() -> new UsersService.UserNotFoundException(authorId));
        return articlesRepository.save(ArticleEntity.builder()
                .title(articleRequest.getTitle())
                // TODO: create a proper slugification function
                .slug(articleRequest.getTitle().toLowerCase().replaceAll("\\s+", "-"))
                .body(articleRequest.getBody())
                .subtitle(articleRequest.getSubtitle())
                .author(author)
                .build()
        );
    }

    public ArticleEntity updateArticle(Long articleId, UpdateArticleRequest a) {
        var article = articlesRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException(articleId));

        if (a.getTitle() != null) {
            article.setTitle(a.getTitle());
            article.setSlug(a.getTitle().toLowerCase().replaceAll("\\s+", "-"));
        }
        if (a.getBody() != null)
            article.setBody(a.getBody());
        if (a.getSubtitle() != null)
            article.setSubtitle(a.getSubtitle());
        return articlesRepository.save(article);
    }

    static class ArticleNotFoundException extends IllegalArgumentException {
        public ArticleNotFoundException(String slug) {
            super("User " + slug + " not found");
        }

        public ArticleNotFoundException(Long id) {
            super("Article with id: " + id + " not found");
        }
    }

}
