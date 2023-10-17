package example.movieapi.service;

import example.movieapi.model.Movie;
import example.movieapi.model.Review;
import example.movieapi.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewContent, String imdbId) {
        Review newReview = reviewRepository.insert(
                Review.builder()
                        .content(reviewContent)
                        .build());
        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(newReview))
                .first();
        return newReview;
    }

}
