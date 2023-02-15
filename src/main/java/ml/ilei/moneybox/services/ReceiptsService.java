package ml.ilei.moneybox.services;

import ml.ilei.moneybox.repositories.ReceiptsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptsService {
    @Autowired
    private ReceiptsRepository receiptsRepository;
}
