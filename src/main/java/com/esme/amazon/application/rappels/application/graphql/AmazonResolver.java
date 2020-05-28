package com.esme.amazon.application.rappels.application.graphql;

import com.esme.amazon.application.rappels.Domain.Amazon;
import com.esme.amazon.application.rappels.Domain.AmazonService;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AmazonResolver {

    private AmazonService amazonservice;

    public AmazonResolver(AmazonService amazonservice) {
        this.amazonservice = amazonservice;
    }
    @GraphQLQuery
    public List<Amazon> getAmazons(){
        return amazonservice.getAmazons();
    }
}