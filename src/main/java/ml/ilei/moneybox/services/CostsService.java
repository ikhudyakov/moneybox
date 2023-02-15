package ml.ilei.moneybox.services;

import ml.ilei.moneybox.repositories.CostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CostsService {
    @Autowired
    private CostsRepository costsRepository;
}
