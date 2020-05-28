package com.esme.amazon.application.rappels.Domain;

import com.esme.amazon.application.rappels.Infrastructure.AmazonDao;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmazonService {

    private AmazonDao amazonDao;

    public AmazonService(AmazonDao amazonDao) {
        this.amazonDao = amazonDao;
    }

    public List<Amazon> getAmazons() {
        return amazonDao.findAmazons();
    }

    @Cacheable("amazons")
    public Amazon getAmazons(Long id) throws ChangeSetPersister.NotFoundException {
        return amazonDao.findAmazons(id);
    }

    public Amazon addAmazon(Amazon amazon) {
        return amazonDao.createAmazons(amazon);
    }

    public void deleteAmazons(Long id) {
        amazonDao.deleteAmazons(id);
    }

    public void patchAmazons(Amazon amazon) {
        amazonDao.updateAmazon(amazon);
    }

    public Amazon findAmazon(Long id) throws ChangeSetPersister.NotFoundException {
        return amazonDao.findAmazons(id);
    }

    public Amazon replaceAmazon(Amazon amazon) {
        return amazonDao.replaceAmazon(amazon);
    }
}
